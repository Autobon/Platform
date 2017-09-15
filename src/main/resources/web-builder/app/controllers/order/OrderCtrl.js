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
        $scope.showExport = 0;
        this.getOrders();
    }

    getOrders(resetPageNo) {
        const {$scope, OrderService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        OrderService.search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.orders = res.data.data.list;
                let datatime = new Date().getTime();
                for (let i = 0; i < $scope.orders.length; i++) {
                    if ($scope.orders[i].agreedStartTime - datatime < 3600000 && $scope.orders[i].agreedStartTime - datatime > 0) {
                        $scope.orders[i].style = {'background-color':'#ec9104'};
                    }
                }

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

    toExport(){
        const {$scope, Settings} = this.$injected;
        $scope.showExport = 1;
    }

    exportExcel() {
        const {$scope, Settings} = this.$injected;
        $scope.showExport = 0;
        // window.location.href = Settings.domain + '/api/web/admin/order/excel/download/view?id=' + ($scope.filter.id === undefined ? '' : $scope.filter.id)
        //                     + '&orderTime=' +  ($scope.filter.orderTime === undefined ? '' : $scope.filter.orderTime)
        //                     + '&orderNum=' +  ($scope.filter.orderNum === undefined ? '' : $scope.filter.orderNum)
        //                     + '&orderCreator=' +  ($scope.filter.orderCreator === undefined ? '' : $scope.filter.orderCreator)
        //                     + '&tech=' +  ($scope.filter.tech === undefined ? '' : $scope.filter.tech)
        //                     + '&orderType=' +  ($scope.filter.orderType === undefined ? '' : $scope.filter.orderType)
        //                     + '&orderStatus=' +  ($scope.filter.orderStatus === undefined ? '' : $scope.filter.orderStatus);
        window.location.href = Settings.domain + '/api/web/admin/order/excel/download/view?id=' + ($scope.filter.id === undefined ? '' : $scope.filter.id)
            + '&startDate=' +  ($scope.filter.startDate === undefined ? '' : $scope.filter.startDate)
            + '&endDate=' +  ($scope.filter.endDate === undefined ? '' : $scope.filter.endDate)
            + '&tech=' +  ($scope.filter.tech === undefined ? '' : $scope.filter.tech);
    }
}
