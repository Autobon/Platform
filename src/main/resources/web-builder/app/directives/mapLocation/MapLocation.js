import {Injector} from 'ngES6';
import $ from 'jquery';

export default class MapLocation extends Injector {

    constructor(...args) {
        super(...args);
        this.template = '<div></div>';
        this.replace = true;
        this.restrict = 'EA';
        this.scope = {
            position: '@',
        };
        this.link.$inject = ['scope', 'element', 'attrs'];
    }

    link(scope, element, attrs) {
        $('body').append($('<script src="http://api.map.baidu.com/api?v=2.0&ak=FPzmlgz02SERkbPsRyGOiGfj&callback=initMap"></script>'));
        window.initMap = () => {
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

            if (typeof scope.position === 'string') {
                scope.position = JSON.parse(scope.position);
            }
            if (!scope.position || !scope.position.lng) {
                map.centerAndZoom('北京', 12);
            } else {
                const point = new window.BMap.Point(scope.position.lng, scope.position.lat);
                const marker = new window.BMap.Marker(point);
                map.centerAndZoom(point, 15);
                map.addOverlay(marker);
            }
        };
    }
}
