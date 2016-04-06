import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class MapView extends Injector {
    static $inject = ['$timeout'];
    static MapViewOverlay = class extends window.BMap.Overlay {
        constructor(object, onmouseover, onclick) {
            super();
            this.point       = new window.BMap.Point(object.lng, object.lat);
            this.label       = object.label;
            this.data        = object;
            this.onmouseover = onmouseover;
            this.onclick     = onclick;
        }

        initialize(map) {
            this.map = map;
            let div  = this.div = $(`<div><span>${this.label}</span><div class="arrow"></div></div>`);
            div.css({
                position: 'absolute',
                border: '1px solid #BC3B3A',
                color: '#FFF',
                padding: '2px',
                'line-height': '18px',
                'white-space': 'nowrap',
                'font-size': '14px',
                'background-color': '#EE5D5B',
            });
            $('.arrow', div).css({
                background: 'url(http://map.baidu.com/fwmap/upload/r/map/fwmap/static/house/images/label.png) no-repeat',
                position: 'absolute',
                width: '11px',
                height: '10px',
                top: '22px',
                left: '10px',
                overflow: 'hidden',
            });
            if (this.onmouseover) {
                div.mouseover(this.onmouseover);
            }
            if (this.onclick) {
                div.click(this.onclick);
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
            center     : '@',
            apiKey     : '@',
            items      : '=',
            onClick    : '&',
            onMouseover: '&',
        };
    }

    link(scope, element) {
        let mapId = element.attr('id');
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
                scope.markers.push(new MapView.MapViewOverlay(i, scope.onClick, scope.onMouseover));
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
                scope.markers.push(new MapView.MapViewOverlay(i, scope.onClick, scope.onMouseover));
            });
            scope.markerClusterer = new window.BMapLib.MarkerClusterer(map, {markers: scope.markers});
        }
    }
}
