import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';
import './map.scss';

export default class MapView extends Injector {
    static $inject        = ['$timeout', '$compile', 'MapService'];

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
                items = angular.copy(scope.items, items);
                scope.markerClusterer && scope.markerClusterer.clearMarkers();
                this._addMarkers(scope);
            }
        });
    }

    _addMarkers(scope) {
        const {MapService} = this.$injected;
        scope.markers = [];
        scope.items && scope.items.forEach(i => {
            let _scope      = scope.$new();
            _scope.data     = i;
            scope.markers.push(new MapService.HtmlMarkerOverlay(_scope, new window.BMap.Point(i.lng, i.lat),
                scope.itemTemplate, scope.itemOffset()));
        });
        if (scope.markers.length > 0) {
            scope.markerClusterer = new window.BMapLib.MarkerClusterer(scope.map, {markers: scope.markers});
        }
    }
}
