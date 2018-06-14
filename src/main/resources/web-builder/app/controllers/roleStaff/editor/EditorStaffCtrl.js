import {Injector} from 'ngES6';
import angular from 'angular';

export default class EditorStaffCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'AccountService'];
    static $template = require('./editorStaff.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, AccountService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.chooseIds = [];
        $scope.roles = [];
        AccountService.searchRole({pageSize:100}).then(res => {
            if (res.data && res.data.result) {
                $scope.roles = res.data.data.list;
            }
        });
        if ($stateParams.id) {
            AccountService.getStaff($stateParams.id).then(res => {
                if (res.data && res.data.result) {
                    $scope.staffData = res.data.data;
                    if ($scope.staffData.roleId) {
                        $scope.staffData.roleId = $scope.staffData.roleId.toString();
                    }
                }
            });
        } else {
            $scope.staffData = {
                position : {lng: null, lat: null},
            };
        }
    }

    save() {
        const {$scope, $state, AccountService} = this.$injected;
        if ($scope.staffData.username === null || $scope.staffData.username === '') {
            $scope.error = '用户名不能为空';
            return;
        }
        if ($scope.staffData.name === null || $scope.staffData.name === '') {
            $scope.error = '姓名不能为空';
            return;
        }
        if ($scope.staffData.phone === null || $scope.staffData.phone === '') {
            $scope.error = '电话不能为空';
            return;
        }
        if ($scope.staffData.email === null || $scope.staffData.email === '') {
            $scope.error = '邮箱不能为空';
            return;
        }
        if ($scope.staffData.id && $scope.staffData.password === null || $scope.staffData.password === '') {
            $scope.error = '密码不能为空';
            return;
        }
        if (!$scope.staffData.roleId || $scope.staffData.roleId < 1) {
            $scope.error = '角色不能为空';
            return;
        }
        let q, isUpdate       = !!$scope.staffData.id;
        if (isUpdate) {
            q = AccountService.updateStaff($scope.staffData.roleId, $scope.staffData.id, $scope.staffData.username, $scope.staffData.email, $scope.staffData.name, $scope.staffData.phone);
        } else {
            q = AccountService.addStaff($scope.staffData.roleId, $scope.staffData.username, $scope.staffData.email, $scope.staffData.name, $scope.staffData.phone, $scope.staffData.password);
        }

        q.then(res => {
            if (res.data) {
                if (res.data.result) {
                    if (isUpdate) {
                        let data = $scope.$parent.staffDatas.find(c => c.id === res.data.data.id);
                        if (data) {
                            angular.extend(data, res.data.data);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.staffDatas.unshift(res.data.data);
                    }
                    $state.go('^');
                } else {
                    if (res.data.result === false) {
                        $scope.error = res.data.message;
                    }
                }
            }
        });
    }

}
