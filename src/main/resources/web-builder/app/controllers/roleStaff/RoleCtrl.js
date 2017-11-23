import {Injector} from 'ngES6';
import './role.scss';

export default class RoleCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'AccountService'];
    static $template = require('./role.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.Settings = Settings;
        $scope.roleData = {};
        $scope.roleDatas = [];
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getDatas();
    }

    getDatas(resetPageNo) {
        const {$scope, AccountService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        AccountService.searchRole($scope.roleData, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.roleDatas = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.roleData = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    deleteRole(id) {
        const {$scope, AccountService} = this.$injected;
        var message = confirm("确定删除吗？");
        if (message == false) {
            return;
        }
        AccountService.deleteRole(id).then(res => {
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
