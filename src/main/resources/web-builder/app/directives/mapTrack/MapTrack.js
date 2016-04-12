import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class MapTrack extends Injector {
    static $inject = ['$timeout'];

    constructor(...args) {
        super(...args);
        this.template = '<div></div>';
        this.replace  = true;
        this.restrict = 'EA';
        this.scope    = {
            // points: '=',
            apiKey: '@',
        };
    }

    link(scope, element) {
        scope.points = [{lng: 114.402524, lat: 30.559999}, {lng: 114.413304, lat: 30.543951}, {lng: 114.424514, lat: 30.525164}];
        window.scope = scope;
        let mapId    = element.attr('id');
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
        let points = angular.copy(scope.points);
        let map    = scope.map = new window.BMap.Map(mapId);
        map.enableScrollWheelZoom(true);
        map.addControl(new window.BMap.NavigationControl({
            anchor: window.BMAP_ANCHOR_TOP_RIGHT,
            type  : window.BMAP_NAVIGATION_CONTROL_ZOOM,
        }));

        if (points && points.length) {
            this._addPoints(scope);
        } else {
            let center = null;
            await new Promise(resolve => {
                new window.BMap.LocalCity().get(result => {
                    center = result.name;
                    resolve();
                });
            });
            await new Promise(resolve => {
                new window.BMap.Geocoder().getPoint(center, point => {
                    map.centerAndZoom(point, 5);
                    resolve();
                });
            });
        }

        scope.$watch('points', (newVal) => {
            if (!angular.equals(points, newVal)) {
                this._addPoints(scope);
            }
        });
    }

    _addPoints(scope) {
        scope.map.clearOverlays();
        if (scope.points && scope.points.length) {
            let points = [], lng = 0, lat = 0;
            scope.points.forEach(p => {
                lng += p.lng;
                lat += p.lat;
                points.push(new window.BMap.Point(p.lng, p.lat));
            });
            scope.map.centerAndZoom(new window.BMap.Point(lng / points.length, lat / points.length), 11);
            points.forEach(p => {
                let icon = new window.BMap.Icon(require('./map-marker.png'), new window.BMap.Size(32, 32));
                icon.anchor = new window.BMap.Size(16, 32);
                scope.map.addOverlay(new window.BMap.Marker(p, {icon: icon}));
            });
            scope.map.addOverlay(new window.BMap.Polyline(points, {
                strokeColor  : '#336D1C',
                strokeOpacity: 0.8,
                strokeWeight : 3,
                strokeStyle  : 'dashed',
            }));
        }
    }
}
