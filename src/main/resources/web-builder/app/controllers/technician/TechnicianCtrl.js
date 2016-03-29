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

    getTechnicians() {
        const {$scope, TechnicianService} = this.$injected;
        TechnicianService.search($scope.filter, $scope.pagination.page, $scope.pagination.pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.technicians = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    verify(technician) {
        const {TechnicianService} = this.$injected;
        TechnicianService.verify(technician.id, true).then(res => {
            if (res.data && res.data.result) {
                technician.status = 'VERIFIED';
            }
        });
    }

    reject(technician, msg) {
        const {TechnicianService} = this.$injected;
        TechnicianService.verify(technician.id, false, msg).then(res => {
            if (res.data && res.data.result) {
                technician.status = 'REJECTED';
            }
        });
    }
}
