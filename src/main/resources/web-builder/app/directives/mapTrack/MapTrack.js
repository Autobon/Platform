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
        let points = angular.copy(scope.points);
        let map = scope.map = new window.BMap.Map(mapId);
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
        scope.points && scope.map.addOverlay(new window.Polyline(scope.points));
    }
}
