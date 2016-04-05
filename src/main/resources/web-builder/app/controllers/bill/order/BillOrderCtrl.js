import {Injector} from 'ngES6';

export default class BillOrderCtrl extends Injector {
    static $inject = ['$scope', '$stateParams', 'BillService', 'OrderService', 'Settings'];
    static $template = require('./order.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, BillService, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
        $scope.BillService = BillService;
        $scope.pagination = {page: 1, pageSize: 20, totalItems: 0};

        BillService.getDetail($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.bill = res.data.data;
                this.getOrders();
            }
        });
    }

    async getOrders() {
        const {$scope, BillService, OrderService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        const res = await BillService.getOrders($scope.bill.id, page, pageSize);

        if (res.data && res.data.result) {
            let orders = res.data.data.list;
            orders.forEach(o => {
                o.construct = $scope.bill.technician.id === o.mainTech.id ? o.mainConstruct : o.secondConstruct;
                OrderService.assembleWorkItemsText(o.construct.workItems, o.construct.workPercent, o.orderType).then(d => {
                    $scope.$apply(() => {
                        o.construct.workItems = d;
                    });
                });
            });
            $scope.orders = orders;
            $scope.pagination.totalItems = res.data.data.totalElements;
        }
    }
}
