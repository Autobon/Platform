import {Injector} from 'ngES6';

export default class WorkDetailCtrl extends Injector {
    static $inject = ['$scope', 'Settings', '$stateParams', 'TechnicianService'];
    static $template = require('./workDetail.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
        $scope.filter = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getWorkDetailViews();
    }

    getWorkDetailViews() {
        const {$scope, $stateParams, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        console.log($stateParams);
        TechnicianService.searchWorkDetail($stateParams.id, page, pageSize).then(res => {
            console.log(res.data);
            if (res.data && res.data.result) {
                $scope.views = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }
}
