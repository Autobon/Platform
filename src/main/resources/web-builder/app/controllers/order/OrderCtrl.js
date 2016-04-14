import {Injector} from 'ngES6';

export default class OrderCtrl extends Injector {
    static $inject   = ['$scope', 'Settings', 'OrderService'];
    static $template = require('./order.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings   = Settings;
        $scope.filter     = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getOrders();
    }

    getOrders(resetPageNo) {
        const {$scope, OrderService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        OrderService.search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.orders = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }
}
