import {Injector} from 'ngES6';
import $ from 'jquery';

export default class MapView extends Injector {
    static $inject = [];

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
                this._showItems(scope, mapId);
                scope.$watch('items', () => {
                    this._showItems(scope, mapId);
                });
            };
        } else {
            this.$injected.$timeout(() => {
                this._showItems(scope, mapId);
                scope.$watch('items', () => {
                    this._showItems(scope, mapId);
                });
            });
        }
    }

    _showItems(scope, mapId) {
        if (!scope.items || scope.items.length) return;
        if (scope.map) {
            scope.markerClusterer.clearMarkers();
            scope.markers = [];
            scope.items.forEach(i => {
                scope.markers.push(new MapViewOverlay(i, scope.onClick, scope.onMouseover));
            });
            scope.markerClusterer = new window.BMapLib.MarkerClusterer(map, {markers: scope.markers});
        } else {
            let map = scope.map = new window.BMap.Map(mapId);
            map.enableScollWheelZoom(true);
            map.addControl(new window.BMap.NavigationControl({
                anchor: window.BMAP_ANCHOR_TOP_RIGHT,
                type  : window.BMAP_NAVIGATION_CONTROL_ZOOM,
            }));
            map.centerAndZoom(scope.center || '北京', 5);

            scope.markers = [];
            scope.items.forEach(i => {
                scope.markers.push(new MapViewOverlay(i, scope.onClick, scope.onMouseover));
            });
            scope.markerClusterer = new window.BMapLib.MarkerClusterer(map, {markers: scope.markers});
        }
    }
}

class MapViewOverlay extends window.BMap.Overlay {
    constructor(object, onmouseover, onclick) {
        this.point       = new window.BMap.Point(object.lng, object.lat);
        this.label       = object.label;
        this.data        = object;
        this.onmouseover = onmouseover;
        this.onclick     = onclick;
    }

    initialize(map) {
        this.map = map;
        var div  = this.div = $(`<div>${this.label}</div>`);
        div.css({
            
        });
        div.onmouseout(this.onmouseover).onclick(this.onclick);
        map.getPanes().markerPane.appendChild(div);
        return div;
    }

    draw() {
        var pixel = this.map.pointToOverlayPixel(this.point);
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
}
