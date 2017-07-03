import {Injector} from 'ngES6';

export default class AccountCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'CooperatorService','$uibModal'];
    static $template = require('./account.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, CooperatorService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.params = {};
        $scope.modalInstance = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};

        this.getAccounts();
    }

    getAccounts() {
        const {$scope, $stateParams, CooperatorService} = this.$injected;
        CooperatorService.getAccounts($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.accounts = res.data.data;
            }
        });
    }

    change(id) {
        const {$scope, $stateParams, $uibModal} = this.$injected;
        this.getCooperators(true);
        $scope.params.coopId = $stateParams.id;
        $scope.params.saleId = id;
        $scope.modalInstance = $uibModal.open({
            size     : 'lg',
            scope    : $scope,
            animation: true,
            template : `
                    <div class="modal-header">
                        <h3 class="modal-title">选择新商户</h3>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive m-l-20 m-r-20">
                            <table class="table table-striped align-middle">
                                <thead><tr><th>序号</th><th>企业名称</th><th>法人姓名</th><th>联系电话</th><th>所在城市</th><th>订单数</th><th>认证状态</th><th>注册日期</th><th>操作</th></tr></thead>
                                <tbody>
                                <tr ng-repeat="c in cooperators">
                                    <td>{{$index+1+pagination.pageSize*(pagination.page-1)}}</td>
                                    <td>{{c.fullname}}</td>
                                    <td>{{c.corporationName}}</td>
                                    <td>{{c.contactPhone}}</td>
                                    <td>{{c.city}}</td>
                                    <td>{{c.orderNum}}</td>
                                    <td>{{['未认证','认证通过','认证失败'][c.statusCode]}}</td>
                                    <td>{{c.createTime | date : 'YYYY-MM-DD HH:mm'}}</td>
                                    <td>
                                        <button class="btn btn-xs btn-default" ng-click="chooseNew(c)"><i class="fa fa-mouse-pointer"></i>&nbsp;选择</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <footer class="text-right">
                                <div class="pull-left">第{{pagination.page}}页 / 共{{(pagination.totalItems/pagination.pageSize) | ceil}}页</div>
                                <uib-pagination total-items="pagination.totalItems" ng-model="pagination.page" boundary-link-numbers="true"
                                                items-per-page="pagination.pageSize" force-ellipses="true" ng-change="getCooperators()"
                                                previous-text="上一页" next-text="下一页" max-size="7"></uib-pagination>
                            </footer>
                            <div><b>已选择企业名称: </b><span class="m-l-20">{{fullName}}</span></div>
                            <uib-alert type="danger" close="error = ''" ng-show="error" class="alert-danger">{{error}}</uib-alert>
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

    chooseNew(coop) {
        const {$scope} = this.$injected;
        $scope.params.newCoopId = coop.id;
        $scope.fullName = coop.fullname
    }

    toClose() {
        const {$scope, CooperatorService} = this.$injected;
        CooperatorService.modifyAccount($scope.params).then(res => {
            if (res.data && res.data.result) {
                $scope.modalInstance.close();
                this.getAccounts();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    getCooperators(resetPageNo) {
        const {$scope, CooperatorService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        CooperatorService.search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.cooperators = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }
}
