import {Injector} from 'ngES6';

export default class MemberCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'TeamService', 'TechnicianService', '$uibModal'];
    static $template = require('./member.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.params = {};
        $scope.modalInstance = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};

        this.getMembers();
    }

    getMembers() {
        const {$scope, $stateParams, TeamService} = this.$injected;
        TeamService.getMembers($stateParams.id).then(res => {
            console.log($stateParams);
            if (res.data && res.data.status === true) {
                $scope.members = res.data.message.content;
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

    addMember() {
        const {$scope, $stateParams, $uibModal} = this.$injected;
        this.getTechnicians(true);
        $scope.params.id = $stateParams.id;
        $scope.modalInstance = $uibModal.open({
            size     : 'lg',
            scope    : $scope,
            animation: true,
            template : `
                    <div class="modal-header">
                        <h3 class="modal-title">添加成员</h3>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive m-l-20 m-r-20">
                            <table class="table table-striped align-middle">
                                <thead><tr>
                                    <th>序号</th>
                                    <th>手机号</th>
                                    <th>姓名</th>
                                    <th>技能项</th>
                                    <th>身份证号</th>
                                    <th>认证日期</th>
                                    <th>帐户状态</th>
                                    <th>工作状态</th>
                                    <th>操作</th>
                                </tr></thead>
                                <tbody>
                                <tr ng-repeat="t in technicians">
                                    <td><b>{{$index+1+pagination.pageSize*(pagination.page-1)}}</b></td>
                                    <td>{{t.phone}}</td>
                                    <td>{{t.name}}</td>
                                    <td><span ng-repeat="s in t.skill.split(',')" class="fa fa-tag m-l-5">{{Settings.orderTypes[s]}}</span></td>
                                    <td>{{t.idNo}}</td>
                                    <td>{{t.verifyAt | date : 'YYYY-MM-DD HH:mm'}}</td>
                                    <td>{{Settings.technicianStatus[t.status]}}</td>
                                    <td>{{Settings.technicianType[t.workStatus]}}</td>
                                    <td>
                                        <button class="btn btn-xs btn-default" ng-click="chooseNew(t)"><i class="fa fa-mouse-pointer"></i>&nbsp;选择</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <footer class="text-right">
                                <div class="pull-left">第{{pagination.page}}页 / 共{{(pagination.totalItems/pagination.pageSize) | ceil}}页</div>
                                <uib-pagination total-items="pagination.totalItems" ng-model="pagination.page" boundary-link-numbers="true"
                                                items-per-page="pagination.pageSize" force-ellipses="true" ng-change="getTechnicians()"
                                                previous-text="上一页" next-text="下一页" max-size="7"></uib-pagination>
                            </footer>
                            <div><b>已选择技师: </b><span class="m-l-20">{{techName}}</span></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="toClose()">确定</button>
                    </div>`,
        });

        $scope.modalInstance.result.then(()  => {
            console.log(11111111111);
        });
    }

    chooseNew(tech) {
        const {$scope} = this.$injected;
        $scope.params.techId = tech.id;
        $scope.techName = tech.name;
    }

    toClose() {
        const {$scope, TeamService} = this.$injected;
        if (!$scope.params.techId) {
            $scope.error = '请选择技师';
            return;
        }
        TeamService.addMember($scope.params.id, $scope.params.techId).then(res => {
            if (res.data && res.data.status === true) {
                $scope.modalInstance.close();
                this.getMembers();
            } else {
                if (res.data.status === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    deleteMember(id) {
        const {$scope, $stateParams, TeamService} = this.$injected;
        let message = confirm('确定删除吗？');
        if (message === false) {
            return;
        }
        TeamService.deleteMember($stateParams.id, id).then(res => {
            if (res.data && res.data.status === true) {
                this.getMembers();
            } else {
                if (res.data.status === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    getTechnicians(resetPageNo) {
        const {$scope, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TechnicianService.search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.technicians = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }
}
