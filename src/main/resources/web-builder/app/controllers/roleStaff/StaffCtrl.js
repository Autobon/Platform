import {Injector} from 'ngES6';
import './role.scss';

export default class StaffCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'AccountService'];
    static $template = require('./staff.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.Settings = Settings;
        $scope.staffData = {};
        $scope.staffDatas = [];
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getDatas();
    }

    getDatas(resetPageNo) {
        const {$scope, AccountService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        AccountService.searchStaff($scope.staffData, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.staffDatas = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.staffData = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    deleteStaff(id) {
        const {$scope, AccountService} = this.$injected;
        let message = confirm('确定删除吗？');
        if (message === false) {
            return;
        }
        AccountService.deleteStaff(id).then(res => {
            if (res.data && res.data.result) {
                this.getDatas();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
