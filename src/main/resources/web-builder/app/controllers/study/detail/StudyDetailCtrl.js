import {Injector} from 'ngES6';

export default class StudyDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'StudyService'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, StudyService} = this.$injected;
        this.attachMethodsTo($scope);

        StudyService.getDetail($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.studyData = res.data.data;
            }
        });
    }

    downloadFile() {
        const {$scope} = this.$injected;
        console.log(111);
        let url = '/api/web/admin/study/download?path=' + $scope.studyData.path;

        window.location.href = url;
    }
}
