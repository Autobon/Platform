import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class MapTrack extends Injector {
    static $inject = ['$timeout', 'MapService'];

    constructor(...args) {
        super(...args);
        this.template = '<div></div>';
        this.replace  = true;
        this.restrict = 'EA';
        this.scope    = {
            points: '=',
            apiKey: '@',
        };
    }

    link(scope, element) {
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
        let {MapService} = this.$injected;
        let points = angular.copy(scope.points);
        let map    = scope.map = new window.BMap.Map(mapId);
        map.enableScrollWheelZoom(true);
        map.addControl(new window.BMap.NavigationControl({
            anchor: window.BMAP_ANCHOR_TOP_RIGHT,
            offset: new window.BMap.Size(10, 50),
            type  : window.BMAP_NAVIGATION_CONTROL_ZOOM,
        }));
        map.addControl(new MapService.FullScreenCtrl(window.BMAP_ANCHOR_TOP_RIGHT, 10, 10));

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
                    map.centerAndZoom(point, 11);
                    resolve();
                });
            });
        }

        // Only $timeout can save my life, but I don't know why I need a $timeout here.
        this.$injected.$timeout(() => {
            scope.$watch('points', (newVal) => {
                if (!angular.equals(points, newVal)) {
                    points = angular.copy(scope.points);
                    this._addPoints(scope);
                }
            });
        });
    }

    _addPoints(scope) {
        scope.map.clearOverlays();
        if (scope.points && scope.points.length) {
            let points = [], lng = 0, lat = 0;
            scope.points.forEach(p => {
                lng += parseFloat(p.lng);
                lat += parseFloat(p.lat);
                let point = new window.BMap.Point(p.lng, p.lat);
                point.$data = p;
                points.push(point);
            });
            scope.map.centerAndZoom(new window.BMap.Point(lng / scope.points.length, lat / scope.points.length), 11);
            points.forEach((p, i) => {
                let url = require('./map-marker.png');
                if (i === 0) url = require('./map-marker-start.png');
                else if (i === points.length - 1) url = require('./map-marker-end.png');
                let icon = new window.BMap.Icon(url, new window.BMap.Size(32, 32));
                icon.anchor = new window.BMap.Size(16, 32);

                let marker = new window.BMap.Marker(p, {icon: icon});
                marker.addEventListener('click', (e) => {
                    scope.$emit('map.track.point.click', e, p.$data);
                });
                marker.addEventListener('mouseover', (e) => {
                    scope.$emit('map.track.point.mouseover', e, p.$data);
                });
                scope.map.addOverlay(marker);
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
