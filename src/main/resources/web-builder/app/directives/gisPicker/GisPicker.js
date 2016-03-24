import {Injector} from 'ngES6';
import $ from 'jquery';

export default class GisPicker extends Injector {

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
            let map = scope.map = new window.BMap.Map(attrs.id);
            map.centerAndZoom('北京', 12);
            map.enableScrollWheelZoom(true);
            map.addControl(new window.BMap.CityListControl({
                anchor: window.BMAP_ANCHOR_TOP_LEFT,
                offset: new window.BMap.Size(10, 20),
            }));
            map.addControl(new window.BMap.NavigationControl({
                anchor: window.BMAP_ANCHOR_TOP_RIGHT,
                type  : window.BMAP_NAVIGATION_CONTROL_ZOOM,
            }));
            map.addEventListener('click', e => {
                if (scope.marker) map.removeOverlay(scope.marker);
                scope.marker = new window.BMap.Marker(new window.BMap.Point(e.point.lng, e.point.lat)); // 创建点
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
