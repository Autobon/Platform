import {Injector} from 'ngES6';
// import angular from 'angular';

export default class TeamEditorCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'MerchandiserService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, MerchandiserService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.chooseIds = [];
        $scope.genders = [{id: 1, name: '男'}, {id: 2, name: '女'}];
        if ($stateParams.id) {
            MerchandiserService.getDetail($stateParams.id).then(res => {
                if (res.data && res.data.message) {
                    $scope.merchandiserData = res.data.message;
                }
            });
        } else {
            $scope.teamData = {
                position : {lng: null, lat: null},
            };
        }
    }


    save() {
        const {$scope, $state, MerchandiserService} = this.$injected;
        if ($scope.merchandiserData.name === null || $scope.merchandiserData.name === '' || !$scope.merchandiserData.name) {
            $scope.error = '跟单员姓名不能为空';
            return;
        }
        if ($scope.merchandiserData.phone === null || $scope.merchandiserData.phone === '' || !$scope.merchandiserData.phone) {
            $scope.error = '跟单员电话不能为空';
            return;
        }
        if (!$scope.merchandiserData.id && ($scope.merchandiserData.password === null || $scope.merchandiserData.password === '' || !$scope.merchandiserData.password)) {
            $scope.error = '跟单员密码不能为空';
            return;
        }
        if ($scope.merchandiserData.gender === null || $scope.merchandiserData.gender === '' || !$scope.merchandiserData.gender) {
            $scope.error = '跟单员性别不能为空';
            return;
        }
        let q, isUpdate       = !!$scope.merchandiserData.id;
        if (isUpdate) {
            q = MerchandiserService.update($scope.merchandiserData);
        } else {
            q = MerchandiserService.add($scope.merchandiserData);
        }

        q.then(res => {
            if (res.data) {
                if (res.data.result === true) {
                    $scope.$parent.getDatas(true);
                    $state.go('^');
                } else {
                    // if (res.data.status === false) {
                    //     $scope.error = res.data.message;
                    // }
                    $scope.error = res.data.message;
                }
            }
        });
    }

}
