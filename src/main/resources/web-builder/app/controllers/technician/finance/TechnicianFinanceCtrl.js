import {Injector} from 'ngES6';

export default class TechnicianCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'TechnicianService'];
    static $template = require('./finance.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
		$scope.filter = {project1: true, project2: true, project3: true, project4: true};
		$scope.chooseProjectIds = [1,2,3,4];
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        this.getTechFinance();
    }

    getTechFinance() {
        const {$scope, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TechnicianService.searchFinance($scope.filter, page, pageSize).then(res => {
            if (res.data.message && res.data.status) {
                $scope.finances = res.data.message.list;
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

	onChangeProject(projectId, flag) {
		const {$scope} = this.$injected;
		if (flag) {
			if (projectId) {
				$scope.chooseProjectIds.push(projectId);
			}
		} else {
			if($scope.chooseProjectIds.length < 2){
				$scope.error = "请至少选择一个施工项目";
				if (projectId == 1)  $scope.filter.project1 = true;
				if (projectId == 2)  $scope.filter.project2 = true;
				if (projectId == 3)  $scope.filter.project3 = true;
				if (projectId == 4)  $scope.filter.project4 = true;
				return;
			}
			$scope.chooseProjectIds = $scope.chooseProjectIds.filter(items => items !== projectId);
		}
		console.log($scope.chooseProjectIds.join(","));
	}

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    closeExport() {
		const {$scope} = this.$injected;
		$scope.showExport = 0
	}

    exportDetail(id) {
        const {$scope, Settings} = this.$injected;

        window.location.href = Settings.domain + '/api/web/admin/order/excel/download/work/' + id
			+ '?startTime=' +  ($scope.filter.startTime === undefined ? '' : $scope.filter.startTime)
			+ '&endTime=' +  ($scope.filter.endTime === undefined ? '' : $scope.filter.endTime)
			+ '&chooseProjectIds=' +  $scope.chooseProjectIds.join(',');
    }

	toExport(isAll, id) {
		const {$scope} = this.$injected;
		$scope.showExport = 1;
		$scope.isAll = isAll;
		$scope.exportId = id;
	}

	startExport(){
		const {$scope} = this.$injected;
        if($scope.isAll == 1){
            this.exportAllDetail();
        } else {
			this.exportDetail($scope.exportId);
        }
    }

    exportAllDetail() {
        const {$scope, Settings} = this.$injected;

        window.location.href = Settings.domain + '/api/web/admin/order/excel/download/work?startTime=' +
            ($scope.filter.startTime === undefined ? '' : $scope.filter.startTime) + '&endTime=' +
			($scope.filter.endTime === undefined ? '' : $scope.filter.endTime)
            + '&chooseProjectIds=' +  $scope.chooseProjectIds.join(',');
    }
}
