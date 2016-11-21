import {Injector} from 'ngES6';
import './modify.scss';

export default class OrderDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./modify.html');

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
                let orderShow = $scope.orderShow = res.data.data;

                orderShow.position         = {lng: orderShow.longitude, lat: orderShow.latitude};
                $scope.comment.orderId = orderShow.id;
                if (orderShow.mainConstruct) {
                    OrderService.assembleWorkItemsText(orderShow.mainConstruct.workItems, orderShow.mainConstruct.workPercent, orderShow.orderType).then(d => {
                        $scope.$apply(() => {
                            orderShow.mainConstruct.workItems = d;
                        });
                    });
                }
                if (orderShow.secondConstruct) {
                    OrderService.assembleWorkItemsText(orderShow.secondConstruct.workItems, orderShow.secondConstruct.workPercent, orderShow.orderType).then(d => {
                        $scope.$apply(() => {
                            orderShow.secondConstruct.workItems = d;
                        });
                    });
                }
            }
        });
    }

}
