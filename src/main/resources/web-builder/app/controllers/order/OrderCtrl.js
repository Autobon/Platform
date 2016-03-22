import {Injector} from 'ngES6';
import './order.scss';

export default class OrderCtrl extends Injector {
    static $inject   = ['$scope', 'Settings', 'OrderService'];
    static $template = require('./order.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings   = Settings;
        $scope.filter     = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 1};
        this.getOrders();
    }

    getOrders() {
        const {$scope, OrderService} = this.$injected;
        OrderService.search($scope.filter, $scope.pagination.page, $scope.pagination.pageSize).then(res => {
            console.log(res.data);
            if (res.data.result) {
                $scope.orders = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    reset() {
        const $scope = this.$injected.$scope;
        $scope.filter = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 20};
    }

}
