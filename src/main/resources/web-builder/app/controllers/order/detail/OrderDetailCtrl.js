import {Injector} from 'ngES6';
import './detail.scss';

export default class OrderDetailCtrl extends Injector {
    static $inject = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService, Settings} = this.$injected;
        $scope.baiduMapKey = Settings.baiduMapKey;
        this.attachMethodsTo($scope);

        OrderService.getDetail($stateParams.orderNum).then(res => {
            if (res.data && res.data.result) {
                const $parent = $scope.$parent;
                let order = res.data.data;
                $scope.order = order;
                order.position = {lng: order.positionLon, lat: order.positionLat};

                if (order.mainConstruct && order.mainConstruct.workItems) {
                    if (!$parent.workItems || !$parent.workItems[order.orderType]) {
                        OrderService.getWorkItems($scope.order.orderType).then(res2 => {
                            if (res2.data && res2.data.result) {
                                $parent.workItems = $parent.workItems || {};
                                $parent.workItems[order.orderType] = res2.data.data;
                                order.mainConstruct.workItems = this._assembleWorkItemsText(
                                    $parent.workItems[order.orderType], order.mainConstruct.workItems);
                            }
                        });
                    } else {
                        order.mainConstruct.workItems = this._assembleWorkItemsText(
                            $parent.workItems[order.orderType], order.mainConstruct.workItems);
                    }
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
