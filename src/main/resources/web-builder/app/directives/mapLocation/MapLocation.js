import {Injector} from 'ngES6';
import $ from 'jquery';

export default class MapLocation extends Injector {
    static $inject = ['$timeout'];

    constructor(...args) {
        super(...args);
        this.template     = '<div></div>';
        this.replace      = true;
        this.restrict     = 'EA';
        this.scope        = {
            position: '@',
            apiKey  : '@',
        };
        this.link.$inject = ['scope', 'element', 'attrs'];
    }

    link(scope, element, attrs) {
        if (scope.apiKey) {
            const callback = 'mapcb' + Math.random().toString().substr(2);
            $('body').append($(`<script src="http://api.map.baidu.com/api?v=2.0&ak=${scope.apiKey}&callback=${callback}"></script>`));
            window[callback] = () => {
                scope.$watch('position', () => {
                    this._showPosition(scope, element, attrs);
                });
            };
        } else {
            this.$injected.$timeout(() => {
                scope.$watch('position', () => {
                    this._showPosition(scope, element, attrs);
                });
            });
        }
    }

    _showPosition(scope, element, attrs) {
        if (scope.map) {
            let position = scope.position;
            if (typeof position === 'string' && position !== '') position = JSON.parse(position);
            if (scope.marker) scope.map.removeOverlay(scope.marker);
            const point = new window.BMap.Point(position.lng, position.lat);
            scope.marker = new window.BMap.Marker(point);
            scope.map.panTo(point);
            scope.map.addOverlay(scope.marker);
        } else {
            if (!attrs.id) {
                attrs.id = 'map' + Math.random().toString().substr(2);
                $(element).attr('id', attrs.id);
            }
            let map = new window.BMap.Map(attrs.id);
            map.enableScrollWheelZoom(true);
            map.addControl(new window.BMap.NavigationControl({
                anchor: window.BMAP_ANCHOR_TOP_RIGHT,
                type  : window.BMAP_NAVIGATION_CONTROL_ZOOM,
            }));

            let ShowCurrentCtrl = function() {
                this.defaultAnchor = window.BMAP_ANCHOR_TOP_LEFT;
                this.defaultOffset = new window.BMap.Size(10, 20);
            };

            ShowCurrentCtrl.prototype            = new window.BMap.Control();
            ShowCurrentCtrl.prototype.initialize = _map => {
                let div = $('<div>').css({
                    background   : '#FFF',
                    color        : '#000',
                    cursor       : 'pointer',
                    padding      : '0 10px',
                    border       : '1px solid #CCC',
                    'font-size'  : '12px',
                    'line-height': '22px',
                });
                div.text('返回');
                div.on('click', () => {
                    if (scope.position) {
                        _map.panTo(new window.BMap.Point(scope.position.lng, scope.position.lat));
                    }
                });
                _map.getContainer().appendChild(div[0]);
                return div[0];
            };
            map.addControl(new ShowCurrentCtrl());

            let position = scope.position;
            if (typeof position === 'string' && position !== '') position = JSON.parse(position);
            if (!position || !position.lng) {
                map.centerAndZoom('北京', 12);
            } else {
                const point  = new window.BMap.Point(position.lng, position.lat);
                scope.marker = new window.BMap.Marker(point);
                map.centerAndZoom(point, 15);
                map.addOverlay(scope.marker);
            }
            scope.map = map;
        }
    }

}
