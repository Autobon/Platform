import {Injector} from 'ngES6';
import angular from 'angular';
import './modify.scss';

export default class OrderDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./modify.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                let orderShow = $scope.orderShow = res.data.data;
                orderShow.position = {lng: orderShow.longitude, lat: orderShow.latitude};
            }
        });
        OrderService.getAllPosition($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.positionShow = res.data.data;
            }
        });
    }

    save() {
        const {$scope, $state, OrderService} = this.$injected;
        let q, isUpdate       = !!$scope.order.id;
        if (isUpdate) {
            q = OrderService.update($scope.orderShow);
        } else {
            q = OrderService.add($scope.orderShow);
        }
        q.then(res => {
            if (res.data) {
                if (res.data.result) {
                    if (isUpdate) {
                        let pOrder = $scope.$parent.orders.find(c => c.id === res.data.data.id);
                        if (pOrder) {
                            angular.extend(pOrder, res.data.data);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.orders.unshift(res.data.data);
                    }
                    $state.go('^.detail', {id: res.data.data.id});
                } else {
                    $scope.error = res.data.message;
                }
            }
        });
    }

}
