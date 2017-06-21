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

        CooperatorService.getAccounts($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.accounts = res.data.data;
            }
        });
    }

    change(id) {
        const {$scope, $stateParams, $uibModal} = this.$injected;
        $scope.aaa = $stateParams.id;
        $scope.bbb = id;
        $scope.modalInstance = $uibModal.open({
            size     : 'sm',
            scope    : $scope,
            animation: true,
            template : `
                    <div class="modal-header">
                        <h3 class="modal-title">选择新商户</h3>
                    </div>
                    <div class="modal-body">
                        <b>{{aaa}}</b>
                        <b>{{bbb}}</b>
                        <select class="form-control" id="gender" ng-model="params.gender">
                            <option value="0">男</option>
                            <option value="1">女</option>
                        </select>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="toClose()">确定</button>
                    </div>`,
        });

        $scope.modalInstance.result.then(()  => {
            console.log(11111111111);
            console.log($scope.params.gender);
        });
    }

    toClose() {
        const {$scope} = this.$injected;
        $scope.modalInstance.close();
    }
}
