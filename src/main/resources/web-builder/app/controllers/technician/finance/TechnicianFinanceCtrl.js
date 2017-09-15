import {Injector} from 'ngES6';

export default class TechnicianCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'TechnicianService'];
    static $template = require('./finance.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
        $scope.filter = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getTechFinance();

    }
    getTechFinance() {
        const {$scope, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TechnicianService.searchFinance($scope.filter, page, pageSize).then(res => {
            if (res.data.message && res.data.status) {
                $scope.finances = res.data.message.list;
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    exportDetail(id) {
        const {$scope, Settings} = this.$injected;

        window.location.href = Settings.domain + '/api/web/admin/order/excel/download/work/' + id;
    }
}
