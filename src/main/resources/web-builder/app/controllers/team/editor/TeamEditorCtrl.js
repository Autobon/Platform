import {Injector} from 'ngES6';
import angular from 'angular';

export default class TeamEditorCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'TeamService', 'TechnicianService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, TeamService, TechnicianService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.chooseIds = [];
        $scope.techs = [];
        TechnicianService.search(null, 1, 1000).then(res => {
            if (res.data && res.data.result) {
                $scope.techs = res.data.data.list;
            }
        });
        if ($stateParams.id) {
            TeamService.getDetail($stateParams.id).then(res => {
                if (res.data && res.data.message) {
                    $scope.teamData = res.data.message;
                    $scope.selectedTech = $scope.teamData.managerId;
                    // $scope.selectedTech = $scope.techs.find(c => c.id === $scope.teamData.managerId);
                }
            });
        } else {
            $scope.teamData = {
                position : {lng: null, lat: null},
            };
        }
    }

    selectTech() {
        const {$scope} = this.$injected;
        let data = $scope.techs.find(c => c.id === $scope.selectedTech);
        $scope.teamData.managerId = data.id;
        $scope.teamData.managerName = data.name;
        $scope.teamData.managerPhone = data.phone;
    }

    save() {
        const {$scope, $state, TeamService} = this.$injected;
        if ($scope.teamData.name === null || $scope.teamData.name === '' || !$scope.teamData.name) {
            $scope.error = '团队名称不能为空';
            return;
        }
        if (!$scope.teamData.managerId || $scope.teamData.managerId < 1) {
            $scope.error = '团队负责人不能为空';
            return;
        }
        let q, isUpdate       = !!$scope.teamData.id;
        if (isUpdate) {
            q = TeamService.update($scope.teamData.id, $scope.teamData);
        } else {
            q = TeamService.add($scope.teamData);
        }

        q.then(res => {
            if (res.data) {
                if (res.data.status === true) {
                    if (isUpdate) {
                        let data = $scope.$parent.teamDatas.find(c => c.id === res.data.message.id);
                        if (data) {
                            angular.extend(data, res.data.message);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.teamDatas.unshift(res.data.message);
                    }
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
