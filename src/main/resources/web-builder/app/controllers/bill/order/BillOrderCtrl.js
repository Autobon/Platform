import {Injector} from 'ngES6';

export default class BillOrderCtrl extends Injector {
    static $inject = ['$scope', '$stateParams', 'BillService'];
    static $template = require('./order.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, BillService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.pagination = {page: 1, pageSize: 20, totalItems: 0};

        BillService.getDetail($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.bill = res.data.data;
                this.getOrders();
            }
        });
    }

    getOrders() {
        const {$scope, BillService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        BillService.getOrders($scope.bill.id, page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.orders = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

}
