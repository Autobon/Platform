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
            const $injector = angular.injector(['ng']);
            const $compile  = $injector.get('$compile');
            this.map        = map;
            let div         = this.div = $($compile(`<div class="mv-marker" ng-click="onClick($event)" ng-mouseenter="onMouseenter($event)"
                                                            ng-mouseleave="onMouseleave($event)">
                                                        <span>${this.scope.data.label}</span>
                                                        <div class="arrow"></div>
                                                    </div>`)(this.scope));
            if (this.scope.itemMouseenter) {
                this.scope.onMouseenter = e => {
                    this.scope.itemMouseenter(this.scope, e);
                };
            }
            if (this.scope.itemMouseout) {
                this.scope.onMouseleave = e => {
                    this.scope.itemMouseleave(this.scope, e);
                };
            }
            if (this.scope.itemClick) {
                this.scope.onClick = e => {
                    this.scope.itemClick(this.scope, e);
                };
            }
            map.getPanes().markerPane.appendChild(div[0]);
            return div[0];
        }

        draw() {
            let pixel = this.map.pointToOverlayPixel(this.point);
            this.div.css({
                left: pixel.x - 10,
                top : pixel.y - 30,
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
            center        : '@',
            apiKey        : '@',
            items         : '=',
            itemClick     : '&',
            itemMouseenter: '&',
            itemMouseleave: '&',
        };
    }

    link(scope, element) {
        scope.itemClick      = scope.itemClick();
        scope.itemMouseenter = scope.itemMouseenter();
        scope.itemMouseleave = scope.itemMouseleave();
        let mapId            = element.attr('id');
        if (!mapId) {
            mapId = 'map' + Math.random().toString().substr(2);
            element.attr('id', mapId);
        }

        if (scope.apiKey) {
            const callback = 'mapcb' + Math.random().toString().substr(2);
            $('body').append($(`<script src="http://api.map.baidu.com/api?v=2.0&ak=${scope.apiKey}&callback=${callback}"></script>`));
            window[callback] = () => {
                let items = [];
                angular.copy(scope.items, items);
                this._showItems(scope, mapId);
                scope.$watch('items', (newVal) => {
                    if (!angular.equals(items, newVal)) {
                        this._showItems(scope, mapId);
                    }
                });
            };
        } else {
            this.$injected.$timeout(() => {
                let items = [];
                angular.copy(scope.items, items);
                this._showItems(scope, mapId);
                scope.$watch('items', (newVal) => {
                    if (!angular.equals(items, newVal)) {
                        this._showItems(scope, mapId);
                    }
                });
            });
        }
    }

    async _showItems(scope, mapId) {
        if (!scope.items || !scope.items.length) return;
        if (scope.map) {
            scope.markerClusterer.clearMarkers();
            scope.markers = [];
            scope.items.forEach(i => {
                scope.markers.push(new MapView.MapViewOverlay(i, scope.itemMouseover, scope.itemClick));
            });
            scope.markerClusterer = new window.BMapLib.MarkerClusterer(scope.map, {markers: scope.markers});
        } else {
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

            scope.markers = [];
            scope.items.forEach(i => {
                let _scope  = scope.$new();
                _scope.data = i;
                scope.markers.push(new MapView.MapViewOverlay(_scope));
            });
            scope.markerClusterer = new window.BMapLib.MarkerClusterer(map, {markers: scope.markers});
        }
    }
}
