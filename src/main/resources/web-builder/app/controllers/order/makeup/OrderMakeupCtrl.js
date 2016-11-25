import {Injector} from 'ngES6';

export default class OrderEditCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', '$uibModal', 'OrderService', 'Settings'];
    static $template = require('./makeup.html');

//    constructor(...args) {
//        super(...args);
//        const {$scope, $stateParams, OrderService} = this.$injected;
//        this.attachMethodsTo($scope);
//
//        if ($stateParams.orderNum) {
//            OrderService.getDetail($stateParams.orderNum).then(res => {
//                if (res.data && res.data.result) {
//                    $scope.order = res.data.data;
//                }
//            });
//        } else {
//            $scope.order = {};
//        }
//        $scope.uploadUrl = OrderService.uploadPhotoUrl;
//    }

}
