import {Injector} from 'ngES6';

export default class CoopEditorCtrl extends Injector {
    static $inject = ['$scope', '$state', '$stateParams', 'CooperatorService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, CooperatorService} = this.$injected;
        this.attachMethodsTo($scope);

        if ($stateParams.id) {
            CooperatorService.getDetail($stateParams.id).then(res => {
                if (res.data && res.data.result) {
                    $scope.coop = res.data.data;
                }
            });
        } else {
            $scope.coop = {};
        }

        $scope.uploadUrl = CooperatorService.uploadPhotoUrl;
    }

    onUploadedPhoto(data) {
        const {$scope, $uibModal} = this.$injected;
        if (!data.result) {
            $scope.message = data.message;
            $uibModal.open({
                size     : 'sm',
                scope    : $scope,
                animation: true,
                template : `
                    <div class="modal-header">
                        <h3 class="modal-title">提示消息</h3>
                    </div>
                    <div class="modal-body">
                        <b>{{message}}</b>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="$close()">确定</button>
                    </div>`,
            });
        }
        return data.result ? data.data : '';
    }

    save() {
        
    }

}
