import {Injector} from 'ngES6';
import moment from 'moment';

export default class OrderEditCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', '$uibModal', 'OrderService', 'Settings'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        this.attachMethodsTo($scope);

        if ($stateParams.orderNum) {
            OrderService.getDetail($stateParams.orderNum).then(res => {
                if (res.data && res.data.result) {
                    $scope.order = res.data.data;
                }
            });
        } else {
            $scope.order = {};
        }
        $scope.uploadUrl = OrderService.uploadPhotoUrl;
    }

    uploaded(data) {
        const {$scope, $uibModal} = this.$injected;
        if (!data.result) {
            $uibModal.open({
                size     : 'sm',
                scope    : $scope,
                animation: true,
                template : `
                    <div class="modal-header">
                        <h3 class="modal-title">提示消息</h3>
                    </div>
                    <div class="modal-body">
                        <b>${data.message}</b>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="$close()">确定</button>
                    </div>`,
            });
        }
        return data.result ? data.data : '';
    }

    beforeRenderDatetimepicker($view, $dates) {
        const now = moment();
        for (let i = 0; i < $dates.length; i++) {
            if ($dates[i].localDateValue() < now.valueOf()) {
                $dates[i].selectable = false;
            }
        }
    }

    save() {
        const {$scope, $state, OrderService} = this.$injected;
        if ($scope.order.orderNum) {
            console.log('update mode has not implemented');
        } else {
            $scope.order.positionLon = $scope.order.position.lng;
            $scope.order.positionLat = $scope.order.position.lat;
            OrderService.add($scope.order).then(res => {
                if (res.data) {
                    if (res.data.result) {
                        $state.go(`^.detail`, {orderNum: res.data.data.orderNum});
                        if ($scope.$parent.pagination.page === 1) {
                            $scope.$parent.orders.unshift(res.data.data);
                        }
                    } else {
                        $scope.error = res.data.message;
                    }
                }
            });
        }
    }
}
