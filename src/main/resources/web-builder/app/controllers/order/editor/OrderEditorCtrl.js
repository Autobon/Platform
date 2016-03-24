import {Injector} from 'ngES6';
import './editor.scss';

export default class OrderEditorCtrl extends Injector {
    static $inject   = ['$scope', '$uibModal', 'OrderService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, OrderService} = this.$injected;
        $scope.order     = {};
        $scope.uploadUrl = OrderService.uploadPhotoUrl;
        this.attachMethodsTo($scope);
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
