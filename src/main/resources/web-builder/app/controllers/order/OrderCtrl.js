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
        $scope.orders = [];

        $scope.chooseIds = [];
        this.getOrders();
    }

    allSelect(flag) {
        const {$scope} = this.$injected;
        if (flag) {
            for (let i = 0; i < $scope.orders.length; i++) {
                if($scope.chooseIds.indexOf($scope.orders[i].id) < 0){
                    $scope.chooseIds.push($scope.orders[i].id);
                }
            }
        } else {
            for (let i = 0; i < $scope.orders.length; i++) {
                let max = $scope.chooseIds.length;
                for (let j = 0; j < max; j++) {
                    if ($scope.chooseIds[j] === $scope.orders[i].id) {
                        $scope.chooseIds.splice(j, 1);
                    }
                }
            }
        }
        this.getOrders();
    }

    getOrders(resetPageNo) {
        const {$scope, OrderService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        OrderService.search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.orders = res.data.data.list;
                $scope.filter.toSelectAll = true;
                let datatime = new Date().getTime();
                for (let i = 0; i < $scope.orders.length; i++) {
                    if ($scope.orders[i].agreedStartTime - datatime < 3600000 && $scope.orders[i].agreedStartTime - datatime > 0) {
                        $scope.orders[i].style = {'background-color':'#ec9104'};
                    }
                    $scope.orders[i].selected = this.isSelected($scope.orders[i].id);
                    if($scope.chooseIds.indexOf($scope.orders[i].id) < 0){
                        $scope.filter.toSelectAll = false;
                    }
                }

                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    isSelected(id) {
        const {$scope} = this.$injected;

        if ($scope.chooseIds.indexOf(id) > -1) {
            return true;
        }
        return false;
    }

    onCheckChange(id, flag) {
        const {$scope} = this.$injected;

        $scope.filter.toSelectAll = false;
        if (flag) {
            if (id) {
                $scope.chooseIds.push(id);
            }
        }else {
            let max = $scope.chooseIds.length;
            for (let i = 0; i < max; i++) {
                if ($scope.chooseIds[i] === id) {
                    $scope.chooseIds.splice(i, 1);
                    break;
                }
            }
        }
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

        if($scope.chooseIds.length > 0){
            window.location.href = Settings.domain + '/api/web/admin/order/excel/download/view?idList=' + $scope.chooseIds.join(",");
        }else{
            window.location.href = Settings.domain + '/api/web/admin/order/excel/download/view?id=' + ($scope.filter.id === undefined ? '' : $scope.filter.id)
                + '&startDate=' +  ($scope.filter.startDate === undefined ? '' : $scope.filter.startDate)
                + '&endDate=' +  ($scope.filter.endDate === undefined ? '' : $scope.filter.endDate)
                + '&tech=' +  ($scope.filter.tech === undefined ? '' : $scope.filter.tech);
        }
    }
}
