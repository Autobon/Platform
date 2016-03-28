import {Injector} from 'ngES6';
import './detail.scss';

export default class OrderDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        $scope.comment     = {
            arriveOnTime  : true,
            completeOnTime: true,
            professional  : true,
            dressNeatly   : true,
            carProtect    : true,
            goodAttitude  : true,
        };
        this.attachMethodsTo($scope);

        OrderService.getDetail($stateParams.orderNum).then(res => {
            if (res.data && res.data.result) {
                const $parent          = $scope.$parent;
                let order              = res.data.data;
                $scope.order           = order;
                order.position         = {lng: order.positionLon, lat: order.positionLat};
                $scope.comment.orderId = order.id;

                if (order.mainConstruct) {
                    if (order.mainConstruct.workItems) {
                        if (!$parent.workItems || !$parent.workItems[order.orderType]) {
                            OrderService.getWorkItems($scope.order.orderType).then(res2 => {
                                if (res2.data && res2.data.result) {
                                    $parent.workItems                  = $parent.workItems || {};
                                    $parent.workItems[order.orderType] = res2.data.data;
                                    order.mainConstruct.workItems      = this._assembleWorkItemsText(
                                        $parent.workItems[order.orderType], order.mainConstruct.workItems);
                                }
                            });
                        } else {
                            order.mainConstruct.workItems = this._assembleWorkItemsText(
                                $parent.workItems[order.orderType], order.mainConstruct.workItems);
                        }
                    } else if (order.mainConstruct.workPercent) {
                        order.mainConstruct.workItems = (order.mainConstruct.workPercent * 100).toFixed(0) + '%';
                    }
                }
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

    _assembleWorkItemsText(dictionary, items) {
        let map = {};
        dictionary.forEach(d => map[d.id] = d.name);
        return items.split(',').map(i => map[i]).join(',');
    }
}
