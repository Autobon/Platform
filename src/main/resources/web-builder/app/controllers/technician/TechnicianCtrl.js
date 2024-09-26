import {Injector} from 'ngES6';

export default class TechnicianCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'TechnicianService'];
    static $template = require('./technician.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
        $scope.filter = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getTechnicians();
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

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    exportFile() {
        console.log(111);
        let url = '/api/web/admin/technician/download';

        window.location.href = url;
    }

    banTech(id) {
        const {$scope, TechnicianService} = this.$injected;
        let message = confirm('确定禁用当前技师吗？');
        if (message === false) {
            return;
        }
        TechnicianService.banTech(id).then(res => {
            if (res.data && res.data.result) {
                this.getTechnicians();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }
    pickTech(id) {
        const {$scope, TechnicianService} = this.$injected;
        let message = confirm('确定解禁当前技师吗？');
        if (message === false) {
            return;
        }
        TechnicianService.pickTech(id).then(res => {
            if (res.data && res.data.result) {
                this.getTechnicians();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
