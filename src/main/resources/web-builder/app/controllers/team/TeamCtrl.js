import {Injector} from 'ngES6';
import './team.scss';

export default class StaffCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'TeamService', '$uibModal'];
    static $template = require('./team.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.Settings = Settings;
        $scope.teamData = {};
        $scope.teamDatas = [];
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        $scope.members = [];
        $scope.pagination0 = {page: 1, totalItems: 0, pageSize: 15};
        this.getDatas();
    }

    getDatas(resetPageNo) {
        const {$scope, TeamService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TeamService.search($scope.teamData, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.status === true) {
                $scope.teamDatas = res.data.message.content;
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.teamData = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    deleteTeam(id) {
        const {$scope, TeamService} = this.$injected;
        let message = confirm('确定删除吗？');
        if (message === false) {
            return;
        }
        TeamService.deleteTeam(id).then(res => {
            if (res.data && res.data.result) {
                this.getDatas();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    teamMembers(id) {
        const {$scope, $uibModal} = this.$injected;
        $scope.members = [];
        $scope.pagination0 = {page: 1, totalItems: 0, pageSize: 15};
        $scope.chooseId = id;
        this.getMembers();
        $scope.modalInstance = $uibModal.open({
            size     : 'lg',
            scope    : $scope,
            animation: true,
            template : `
                    <div class="modal-header">
                        <h3 class="modal-title">成员列表</h3>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive m-l-20 m-r-20">
                            <table class="table table-striped align-middle">
                                <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>手机号</th>
                                        <th>姓名</th>
                                        <th>认证日期</th>
                                        <th>帐户状态</th>
                                        <th>工作状态</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="m in members">
                                    <td><b>{{$index+1+pagination0.pageSize*(pagination0.page-1)}}</b></td>
                                    <td>{{m.phone}}</td>
                                    <td>{{m.name}}</td>
                                    <td>{{m.verifyAt | date : 'YYYY-MM-DD HH:mm'}}</td>
                                    <td>{{Settings.technicianStatus[m.status]}}</td>
                                    <td>{{Settings.technicianType[m.workStatus]}}</td>
                                </tr>
                                </tbody>
                            </table>
                            <footer class="text-right">
                                <div class="pull-left">第{{pagination0.page}}页 / 共{{(pagination0.totalItems/pagination0.pageSize) | ceil}}页</div>
                                <uib-pagination total-items="pagination0.totalItems" ng-model="pagination0.page" boundary-link-numbers="true"
                                                items-per-page="pagination0.pageSize" force-ellipses="true" ng-change="getMembers()"
                                                previous-text="上一页" next-text="下一页" max-size="7"></uib-pagination>
                            </footer>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="toAdd()">确定</button>
                    </div>`,
        });

        $scope.modalInstance.result.then(()  => {
            console.log(11111111111);
        });
    }

    getMembers() {
        const {$scope, $stateParams, TeamService} = this.$injected;
        TeamService.getMembers($scope.chooseId).then(res => {
            console.log($stateParams);
            if (res.data && res.data.status === true) {
                $scope.members = res.data.message.content;
                $scope.pagination0.totalItems = res.data.message.totalElements;
            }
        });
    }
}
