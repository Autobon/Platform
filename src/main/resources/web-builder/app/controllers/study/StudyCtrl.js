import {Injector} from 'ngES6';
import './study.scss';

export default class StudyCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'StudyService'];
    static $template = require('./study.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.Settings = Settings;
        $scope.studyData = {};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getDatas();
    }

    getDatas(resetPageNo) {
        const {$scope, StudyService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        StudyService.search($scope.studyData, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.studyDatas = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.studyData = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }
}
