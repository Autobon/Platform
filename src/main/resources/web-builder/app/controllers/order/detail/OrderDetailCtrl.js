import {Injector} from 'ngES6';
import './detail.scss';

export default class OrderDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        $scope.comment = {
            arriveOnTime  : true,
            completeOnTime: true,
            professional  : true,
            dressNeatly   : true,
            carProtect    : true,
            goodAttitude  : true,
        };
        this.attachMethodsTo($scope);

        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                let order = $scope.order = res.data.data;

                order.position         = {lng: order.longitude, lat: order.latitude};
//                $scope.comment.orderId = order.id;
//                if (order.mainConstruct) {
//                    OrderService.assembleWorkItemsText(order.mainConstruct.workItems, order.mainConstruct.workPercent, order.orderType).then(d => {
//                        $scope.$apply(() => {
//                            order.mainConstruct.workItems = d;
//                        });
//                    });
//                }
//                if (order.secondConstruct) {
//                    OrderService.assembleWorkItemsText(order.secondConstruct.workItems, order.secondConstruct.workPercent, order.orderType).then(d => {
//                        $scope.$apply(() => {
//                            order.secondConstruct.workItems = d;
//                        });
//                    });
//                }
            }
        });
    }

    saveComment() {
        const {$scope, OrderService} = this.$injected;
        OrderService.addComment($scope.comment).then(res => {
            if (res.data) {
                if (res.data.result) {
                    delete $scope.commentResult;
                    $scope.order.comment = res.data.data;
                } else {
                    $scope.commentResult = res.data.message;
                }
            }
        });
    }
}
