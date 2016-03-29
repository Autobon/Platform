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

        OrderService.getDetail($stateParams.orderNum).then(res => {
            if (res.data && res.data.result) {
                let order = $scope.order = res.data.data;

                order.position         = {lng: order.positionLon, lat: order.positionLat};
                $scope.comment.orderId = order.id;
                if (order.mainConstruct) this._setupWorkItemsText(order.mainConstruct, order.orderType);
                if (order.secondConstruct) this._setupWorkItemsText(order.secondConstruct, order.orderType);
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

    _setupWorkItemsText(construct, type) {
        const {$scope, OrderService} = this.$injected;
        const $parent = $scope.$parent;

        if (construct.workItems) {
            if (!$parent.workItems || !$parent.workItems[type]) {
                OrderService.getWorkItems(type).then(res => {
                    if (res.data && res.data.result) {
                        $parent.workItems       = $parent.workItems || {};
                        $parent.workItems[type] = res.data.data;
                        construct.workItems     = this._assembleWorkItemsText($parent.workItems[type], construct.workItems);
                    }
                });
            } else {
                construct.workItems = this._assembleWorkItemsText($parent.workItems[type], construct.workItems);
            }
        } else if (construct.workPercent) {
            construct.workItems = (construct.workPercent * 100).toFixed(0) + '%';
        }
    }
}
