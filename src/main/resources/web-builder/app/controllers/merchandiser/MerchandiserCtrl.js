import {Injector} from 'ngES6';
import './merchandiser.scss';

export default class MerchandiserCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'MerchandiserService', 'CooperatorService', '$uibModal', '$timeout'];
    static $template = require('./merchandiser.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.Settings = Settings;
        $scope.searchData = {};
        $scope.merchandiserDatas = [];
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};

        $scope.cooperators = [];
        $scope.pagination0 = {page: 1, totalItems: 0, pageSize: 15};
        this.getDatas();
    }

    getDatas(resetPageNo) {
        const {$scope, MerchandiserService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        MerchandiserService.search($scope.searchData, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result === true) {
                $scope.merchandiserDatas = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.searchData = {};
        $scope.filter = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    deleteMerchandiser(id) {
        const {$scope, MerchandiserService} = this.$injected;
        let message = confirm('确定删除吗？');
        if (message === false) {
            return;
        }
        MerchandiserService.deleteMerchandiser(id).then(res => {
            if (res.data && res.data.result) {
                this.getDatas();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    banMerchandiser(id) {
        const {$scope, MerchandiserService} = this.$injected;
        let message = confirm('确定禁用当前跟单员吗？');
        if (message === false) {
            return;
        }
        MerchandiserService.banMerchandiser(id).then(res => {
            if (res.data && res.data.result) {
                this.getDatas();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    pickMerchandiser(id) {
        const {$scope, MerchandiserService} = this.$injected;
        let message = confirm('确定启用当前跟单员吗？');
        if (message === false) {
            return;
        }
        MerchandiserService.pickMerchandiser(id).then(res => {
            if (res.data && res.data.result) {
                this.getDatas();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    getCooperators(resetPageNo) {
        const {$scope, CooperatorService} = this.$injected;
        const {page, pageSize} = $scope.pagination0;
        CooperatorService.search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.cooperators = res.data.data.list;
                $scope.pagination0.totalItems = res.data.data.totalElements;
            }
        });
    }

    setCoop(id) {
        const {$scope, $uibModal} = this.$injected;
        $scope.merchandiserId = id;
        $scope.filter = {};
        this.getCoops();
        this.getCooperators(true);
        $scope.modalInstance = $uibModal.open({
            size     : 'lg',
            scope    : $scope,
            animation: true,
            template : `
                    <div class="modal-header">
                        <h3 class="modal-title">选择商户</h3>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <form class="form-inline pull-left">
                                <div class="form-group">
                                    <label for="name">企业名称:</label>
                                    <input type="text" class="form-control" id="fullname" ng-model="filter.fullname">
                                </div>
                                <button type="submit" class="btn btn-primary m-l-5" ng-click="getCooperators(true)"><span class="glyphicon glyphicon-search"></span>&nbsp;查找</button>
                                <button type="button" class="btn btn-sm btn-default m-l-5" ng-click="reset()">重置</button>
                            </form>
                        </div>
                        <div class="table-responsive m-l-20 m-r-20">
                            <table class="table table-striped align-middle">
                                <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>企业名称</th>
                                        <th>联系电话</th>
                                        <th>所在城市</th>
                                        <th>认证状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="c in cooperators">
                                    <td>{{$index+1+pagination0.pageSize*(pagination0.page-1)}}</td>
                                    <td>{{c.fullname}}</td>
                                    <td>{{c.contactPhone}}</td>
                                    <td>{{c.city}}</td>
                                    <td>{{['未认证','认证通过','认证失败'][c.statusCode]}}</td>
                                    <td>
                                        <button class="btn btn-xs btn-default" ng-click="chooseNew(c)"><i class="fa fa-mouse-pointer"></i>&nbsp;选择</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <footer class="text-right">
                                <div class="pull-left">第{{pagination0.page}}页 / 共{{(pagination0.totalItems/pagination0.pageSize) | ceil}}页</div>
                                <uib-pagination total-items="pagination0.totalItems" ng-model="pagination0.page" boundary-link-numbers="true"
                                                items-per-page="pagination0.pageSize" force-ellipses="true" ng-change="getCooperators()"
                                                previous-text="上一页" next-text="下一页" max-size="7"></uib-pagination>
                            </footer>
                            <div><b>已选择商户: </b><span class="m-l-20">{{coopName}}</span></div>
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

    chooseNew(coop) {
        const {$scope} = this.$injected;
            if($scope.coopIds.indexOf(coop.id) != -1){
                confirm('跟单员已匹配此商户，请重新选择');
                return;
            }
        $scope.coopId = coop.id;
        $scope.coopName = coop.fullname;
    }

    toAdd() {
        const {$scope, MerchandiserService} = this.$injected;
        if (!$scope.coopId) {
            $scope.error = '请选择商户';
            return;
        }
        MerchandiserService.coopAddMerchandiser($scope.merchandiserId, $scope.coopId).then(res => {
            if (res.data && res.data.result === true) {
                $scope.modalInstance.close();
                confirm('添加成功');
                $scope.pagination0 = {page: 1, totalItems: 0, pageSize: 15};
                $scope.coopId = null;
                $scope.coopName = null;
                this.getDatas();
            } else {
                if (res.data.status === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }

    // 查询已配置的商户
    getCoops(resetPageNo) {
        const {$scope, MerchandiserService} = this.$injected;
        MerchandiserService.getMerchandisers($scope.merchandiserId).then(res => {
            if (res.data && res.data.status) {
                $scope.coops = res.data.message;
                $scope.coopIds = [];
                $scope.coops.forEach(a =>{
                    $scope.coopIds.push(a.cooperatorId);
                })
            }
        });
    }

    getCoop(id) {
        const {$scope, $uibModal} = this.$injected;
        $scope.merchandiserId = id;
        this.getCoops(true);
        $scope.modalInstance2 = $uibModal.open({
            size     : 'md',
            scope    : $scope,
            animation: true,
            template : `
                    <div class="modal-header">
                        <h3 class="modal-title">商户列表</h3>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive m-l-20 m-r-20">
                            <table class="table table-striped align-middle">
                                <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>商户名称</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="c in coops">
                                    <td>{{$index + 1}}</td>
                                    <td>{{c.cooperatorName}}</td>
                                    <td>
                                        <button class="btn btn-xs btn-default" ng-click="chooseNew2(c)"><i class="fa fa-mouse-pointer"></i>&nbsp;选择删除</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <div><b>已选择商户: </b><span class="m-l-20">{{coopName2}}</span></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="toDelete()">确定删除</button>
                    </div>`,
        });

        $scope.modalInstance2.result.then(()  => {
            console.log(11111111111);
        });
    }

    chooseNew2(coop) {
        const {$scope} = this.$injected;
        $scope.deleteCoopId = coop.cooperatorId;
        $scope.coopName2 = coop.cooperatorName;
    }

    // 删除关联的商户
    toDelete() {
        const {$scope, MerchandiserService} = this.$injected;
        if (!$scope.deleteCoopId) {
            $scope.error = '请选择商户';
            return;
        }
        MerchandiserService.coopDeleteMerchandiser($scope.merchandiserId, $scope.deleteCoopId).then(res => {
            if (res.data && res.data.result === true) {
                $scope.modalInstance2.close();
                confirm('删除成功');
                this.getDatas();
            } else {
                if (res.data.status === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
