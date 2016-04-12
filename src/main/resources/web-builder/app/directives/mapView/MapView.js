import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';
import './map.scss';

export default class MapView extends Injector {
    static $inject        = ['$timeout', '$compile'];
    static MapViewOverlay = class extends window.BMap.Overlay {
        constructor(scope) {
            super();
            scope.data = scope.data || {};
            this.scope = scope;
            this.point = new window.BMap.Point(scope.data.lng, scope.data.lat);
        }

        initialize(map) {
            const $injector    = angular.injector(['ng']);
            const $compile     = $injector.get('$compile');
            const $interpolate = $injector.get('$interpolate');
            this.map           = map;
            let template       = this.scope.template || `<div class="mv-marker">
                                                            <span>★</span>
                                                            <div class="arrow"></div>
                                                        </div>`;
            let div            = this.div = $($compile($interpolate(template)(this.scope))(this.scope));
            div.click(e => {
                this.scope.$emit('map.marker.click', e);
            }).mouseenter(e => {
                this.scope.$emit('map.marker.mouseenter', e);
            }).mouseleave(e => {
                this.scope.$emit('map.marker.mouseleave', e);
            });
            map.getPanes().markerPane.appendChild(div[0]);
            return div[0];
        }

        draw() {
            let pixel             = this.map.pointToOverlayPixel(this.point);
            let itemOffset, scope = this.scope;

            if (!scope.offsetX || !scope.offsetY) {
                itemOffset = scope.itemOffset();
                if (itemOffset) {
                    [scope.offsetX, scope.offsetY] = itemOffset(this.div);
                } else {
                    scope.offsetX = -10;
                    scope.offsetY = -(this.div.height() + 10);
                }
            }

            this.div.css({
                left: pixel.x + scope.offsetX,
                top : pixel.y + scope.offsetY,
            });
        }

        getPosition() {
            return this.point;
        }

        getMap() {
            return this.map;
        }
    };

    constructor(...args) {
        super(...args);
        this.template = '<div></div>';
        this.replace  = true;
        this.restrict = 'EA';
        this.scope    = {
            center      : '@',
            apiKey      : '@',
            items       : '=',
            itemTemplate: '@',
            itemOffset  : '&',
        };
    }

    link(scope, element) {
        window.scope = scope;
        let mapId = element.attr('id');
        if (!mapId) {
            mapId = 'map' + Math.random().toString().substr(2);
            element.attr('id', mapId);
        }

        if (scope.apiKey) {
            const callback = 'mapcb' + Math.random().toString().substr(2);
            $('body').append($(`<script src="http://api.map.baidu.com/api?v=2.0&ak=${scope.apiKey}&callback=${callback}"></script>`));
            window[callback] = () => {
                this._showMap(scope, mapId);
            };
        } else {
            this.$injected.$timeout(() => {
                this._showMap(scope, mapId);
            });
        }
    }

    async _showMap(scope, mapId) {
        let items = angular.copy(scope.items, items);

        let map = scope.map = new window.BMap.Map(mapId);
        map.enableScrollWheelZoom(true);
        map.addControl(new window.BMap.NavigationControl({
            anchor: window.BMAP_ANCHOR_TOP_RIGHT,
            type  : window.BMAP_NAVIGATION_CONTROL_ZOOM,
        }));

        if (!scope.center) {
            await new Promise(resolve => {
                new window.BMap.LocalCity().get(result => {
                    scope.center = result.name;
                    resolve();
                });
            });
        }
        await new Promise(resolve => {
            new window.BMap.Geocoder().getPoint(scope.center, point => {
                map.centerAndZoom(point, 5);
                resolve();
            });
        });
        if (scope.items && scope.items.length) {
            this._addMarkers(scope);
        }

        scope.$watch('items', (newVal) => {
            if (!angular.equals(items, newVal)) {
                scope.markerClusterer.clearMarkers();
                this._addMarkers(scope);
            }
        });
    }

    _addMarkers(scope) {
        scope.markers = [];
        scope.items && scope.items.forEach(i => {
            let _scope      = scope.$new();
            _scope.data     = i;
            _scope.template = scope.itemTemplate;
            scope.markers.push(new MapView.MapViewOverlay(_scope));
            scope.markerClusterer = new window.BMapLib.MarkerClusterer(scope.map, {markers: scope.markers});
        });
    }
}
