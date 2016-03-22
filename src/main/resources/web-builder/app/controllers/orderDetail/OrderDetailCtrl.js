import {Injector} from 'ngES6';
import './detail.scss';

export default class OrderDetailCtrl extends Injector {
    static $inject = ['$scope', '$state', '$stateParams', 'OrderService'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        this.attachMethodsTo($scope);
        this.orderId = $stateParams.orderId;
        OrderService.getDetail(this.orderId).then(res => {
            if (res.data.result) {
                console.log(res.data);
                const $parent = $scope.$parent;
                const order = res.data.data;
                $parent.currentOrder = $scope.order = order;

                if (order.mainConstruct && order.mainConstruct.workItems) {
                    if (!$parent.workItems || !$parent.workItems[order.orderType]) {
                        console.log(1);
                        OrderService.getWorkItems($scope.order.orderType).then(res2 => {
                            if (res2.data.result) {
                                console.log(res2.data);
                                $parent.workItems = $parent.workItems || {};
                                $parent.workItems[order.orderType] = res2.data.data;
                                order.mainConstruct.workItems = this._assembleWorkItemsText($parent.workItems[order.orderType], order.mainConstruct.workItems);
                            }
                        });
                    } else {
                        console.log('2');
                        order.mainConstruct.workItems = this._assembleWorkItemsText($parent.workItems[order.orderType], order.mainConstruct.workItems);
                    }
                }
            }
        });
    }

    _assembleWorkItemsText(dictionary, items) {
        console.log(dictionary, items);
        let map = {};
        dictionary.forEach(d => map[d.id] = d.name);
        return items.split(',').map(i => map[i]).join(',');
    }
}
