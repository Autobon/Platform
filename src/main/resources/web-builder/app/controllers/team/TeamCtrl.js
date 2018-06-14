import {Injector} from 'ngES6';
import './team.scss';

export default class StaffCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'TeamService'];
    static $template = require('./team.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.Settings = Settings;
        $scope.staffData = {};
        $scope.staffDatas = [];
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getDatas();
    }

    getDatas(resetPageNo) {
        const {$scope, TeamService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TeamService.search($scope.staffData, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data && res.data.status === true) {
                $scope.teamDatas = res.data.message.content;
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.teamData = {};
        // $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    deleteTeam(id) {
        const {$scope, TeamService} = this.$injected;
        let message = confirm('确定删除吗？');
        if (message === false) {
            return;
        }
        TeamService.deleteTeam(id).then(res => {
            if (res.data && res.data.result) {
                this.getDatas();
            } else {
                if (res.data.result === false) {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
