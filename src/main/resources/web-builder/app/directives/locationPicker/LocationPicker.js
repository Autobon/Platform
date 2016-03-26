import {Injector} from 'ngES6';
import $ from 'jquery';

export default class LocationPicker extends Injector {

    constructor(...args) {
        super(...args);
        this.template     = '<div></div>';
        this.replace      = true;
        this.require      = 'ngModel';
        this.restrict     = 'EA';
        this.scope        = {
            position: '=ngModel',
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
            let map = scope.map = new window.BMap.Map(attrs.id);
            map.centerAndZoom('北京', 12);
            map.enableScrollWheelZoom(true);
            map.addControl(new window.BMap.CityListControl({
                anchor: window.BMAP_ANCHOR_TOP_LEFT,
                offset: new window.BMap.Size(60, 20),
            }));
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
                        _map.panTo(scope.position);
                    }
                });
                _map.getContainer().appendChild(div[0]);
                return div[0];
            };
            map.addControl(new ShowCurrentCtrl());


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
        };
    }
}