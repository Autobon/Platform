import {Injector} from 'ngES6';
import $ from 'jquery';

export default class LocationPicker extends Injector {
    static $inject = ['$timeout', 'MapService'];

    constructor(...args) {
        super(...args);
        this.template     = '<div></div>';
        this.replace      = true;
        this.require      = 'ngModel';
        this.restrict     = 'EA';
        this.scope        = {
            position: '=ngModel',
            apiKey  : '@',
        };
    }

    link(scope, element) {
        let mapId = element.attr('id');
        if (!mapId) {
            mapId = 'maplp' + Math.random().toString().substr(2);
            element.attr('id', mapId);
        }

        if (scope.apiKey) {
            const callback = 'mapcb' + Math.random().toString().substr(2);
            $('body').append($(`<script src="http://api.map.baidu.com/api?v=2.0&ak=${scope.apiKey}&callback=${callback}"></script>`));
            window[callback] = () => {
                this._showMap(scope, mapId);
                scope.$watch('position', () => {
                    this._showMap(scope, mapId);
                });
            };
        } else {
            this.$injected.$timeout(() => {
                this._showMap(scope, mapId);
                scope.$watch('position', () => {
                    this._showMap(scope, mapId);
                });
            });
        }

        scope.$on('map.action.back', (e, map) => {
            e.stopPropagation();
            let position = scope.position;
            if (typeof position === 'string' && position !== '') position = JSON.parse(position);
            if (position && position.lng) map.panTo(new window.BMap.Point(position.lng, position.lat));
        });
    }

    _showMap(scope, mapId) {
        const {MapService} = this.$injected;
        if (scope.map && scope.position) {
            const oldMarker = scope.marker;
            const point = new window.BMap.Point(scope.position.lng, scope.position.lat);
            scope.marker = new window.BMap.Marker(point);
            scope.marker.enableDragging();
            scope.marker.addEventListener('mouseup', () => {
                scope.$apply(() => {
                    scope.position = scope.marker.getPosition();
                });
            });
            if (this.init) {
                this.init = false;
                scope.map.centerAndZoom(point, 12);
            } else {
                scope.map.panTo(point);
            }
            scope.map.addOverlay(scope.marker);

            if (oldMarker) {
                this.$injected.$timeout(() => { // 如果立即删除,浏览器会报异常
                    scope.map.removeOverlay(oldMarker);
                });
            }
        } else if (!scope.map) {
            let map = scope.map = new window.BMap.Map(mapId);
            if (scope.position) {
                map.centerAndZoom(new window.BMap.Point(scope.position.lng, scope.position.lat), 12);
            } else {
                this.init = true; // 获取当前城市速度较慢, 作为一个标志位, 防止过慢覆盖了初始值
                new window.BMap.LocalCity().get(result => {
                    if (this.init) {
                        this.init = false;
                        map.centerAndZoom(result.name, 12);
                    }
                });
            }
            map.enableScrollWheelZoom(true);
            map.addControl(new window.BMap.CityListControl({
                anchor: window.BMAP_ANCHOR_TOP_LEFT,
                offset: new window.BMap.Size(60, 20),
            }));
            map.addControl(new window.BMap.NavigationControl({
                anchor: window.BMAP_ANCHOR_TOP_RIGHT,
                offset: new window.BMap.Size(10, 50),
                type  : window.BMAP_NAVIGATION_CONTROL_ZOOM,
            }));
            map.addControl(new MapService.FullScreenCtrl(window.BMAP_ANCHOR_TOP_RIGHT, 10, 10));
            map.addControl(new MapService.BackButtonCtrl(window.BMAP_ANCHOR_TOP_LEFT, 10, 20));

            map.addEventListener('click', e => {
                if (scope.marker) map.removeOverlay(scope.marker);
                scope.marker = new window.BMap.Marker(new window.BMap.Point(e.point.lng, e.point.lat));
                scope.marker.enableDragging();
                scope.marker.addEventListener('mouseup', () => {
                    scope.$apply(() => {
                        scope.position = scope.marker.getPosition();
                    });
                });
                map.addOverlay(scope.marker);
                scope.$apply(() => {
                    scope.position = e.point;
                });
            });
        }
    }
}
