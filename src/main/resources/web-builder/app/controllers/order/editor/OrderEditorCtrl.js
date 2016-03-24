import {Injector} from 'ngES6';
import $ from 'jquery';
import './editor.scss';

export default class OrderEditorCtrl extends Injector {
    static $inject   = ['$scope', '$uibModal'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        $scope.order     = {};
        $scope.uploadUrl = '/api/web/admin/order/photo';
        this.attachMethodsTo($scope);

        $('body').append($('<script src="http://api.map.baidu.com/api?v=2.0&ak=FPzmlgz02SERkbPsRyGOiGfj&callback=initMap"></script>'));
        window.initMap = () => {
            let map = $scope.map = new window.BMap.Map('baiduMap');
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
        };
    }

    uploaded(data) {
        const {$scope, $uibModal} = this.$injected;
        if (!data.result) {
            $scope.message = data.message;
            $uibModal.open({
                size     : 'sm',
                scope    : $scope,
                animation: true,
                template : `
                    <div class="modal-header">
                        <h3 class="modal-title">提示消息</h3>
                    </div>
                    <div class="modal-body">
                        <b>{{ message }}</b>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="$close()">确定</button>
                    </div>`,
            });
        }
        return data.result ? data.data : '';
    }
}
