import {Injector} from 'ngES6';

export default class ApplyRecordCtrl extends Injector {
    static $inject = ['$scope', 'Settings', '$stateParams', 'TechnicianService'];
    static $template = require('./applyRecord.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
        $scope.filter = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getViews();
    }

    getViews() {
        const {$scope, $stateParams, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        console.log($stateParams);
        if ($stateParams.id === 0) {
            $scope.filter.state = 0;
        }
        let params = {
            ...$scope.filter,
            //techId : $stateParams.id,
        };
        if ($stateParams.id > 0) {
            params.techId = $stateParams.id;
        }

        console.log(params);
        TechnicianService.searchApply(params, page, pageSize).then(res => {
            console.log(res.data);
            if (res.data && res.data.result) {
                $scope.views = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
                $scope.able = res.data.message;
            }
        });
    }
    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
    }

    endPay(id) {
        const {TechnicianService} = this.$injected;
        TechnicianService.endPay(id).then(res => {
            console.log(res.data);
            if (res.data.status) {
                this.getViews();
                alert('完成支付');
            } else {
                alert(res.data.message);
            }
        });
    }
}
