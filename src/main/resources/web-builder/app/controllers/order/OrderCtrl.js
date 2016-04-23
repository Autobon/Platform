import {Injector} from 'ngES6';
import angular from 'angular';

export default class OrderCtrl extends Injector {
    static $inject   = ['$scope', 'Settings', 'OrderService', 'TechnicianService'];
    static $template = require('./order.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings   = Settings;
        $scope.filter     = {sort: 'id'};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        $scope.assignTemplate = 'assignOrder.html';
        $scope.techQuery = {};
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

    searchTech(q) {
        const {$scope, TechnicianService} = this.$injected;
        let params = {};
        if (/\d+/.test(q)) params.phone = q;
        else params.name = q;
        TechnicianService.search(params, 1, 50).then(res => {
            if (res.data && res.data.result) {
                $scope.techQuery.techs = res.data.data.list;
            }
        });
    }

    assign(order, tech) {
        const {$scope, OrderService} = this.$injected;
        OrderService.assign(order.id, tech.id).then(res => {
            if (res.data) {
                if (res.data.result) {
                    angular.extend(order, res.data.data);
                    order.mainTech = tech;
                    if ($scope.orders) {
                        let pOrder = $scope.orders.find(o => o.id === order.id);
                        angular.extend(pOrder, order);
                    }
                    this.$injected.$scope.techQuery['show' + order.id] = false;
                } else {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
