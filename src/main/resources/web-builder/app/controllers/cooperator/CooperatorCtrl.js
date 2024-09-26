import {Injector} from 'ngES6';

export default class CooperatorCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'CooperatorService'];
    static $template = require('./cooperator.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.Settings = Settings;
        $scope.filter = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getCooperators();
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

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    upload() {
        const {CooperatorService} = this.$injected;
        CooperatorService.upload().then(() => {

        });
    }

    exportFile() {
        console.log(111);
        let url = '/api/web/admin/cooperator/download';

        window.location.href = url;
    }


    banCoop(id) {
        const {$scope, CooperatorService} = this.$injected;
        let message = confirm('确定禁用当前商户吗？');
        if (message === false) {
            return;
        }
        CooperatorService.banCoop(id).then(res => {
            if (res.data && res.data.result) {
                this.getCooperators();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }
    pickCoop(id) {
        const {$scope, CooperatorService} = this.$injected;
        let message = confirm('确定解禁当前商户吗？');
        if (message === false) {
            return;
        }
        CooperatorService.pickCoop(id).then(res => {
            if (res.data && res.data.result) {
                this.getCooperators();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
