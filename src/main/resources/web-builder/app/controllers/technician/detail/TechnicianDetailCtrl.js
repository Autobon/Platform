import {Injector} from 'ngES6';

export default class TechnicianDetailCtrl extends Injector {
    static $inject = ['$scope', '$state', '$stateParams', 'Settings', 'TechnicianService'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, TechnicianService, Settings} = this.$injected;
        $scope.Settings = Settings;
        this.attachMethodsTo($scope);

        TechnicianService.getDetail($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.technician = res.data.data;
                if (!$scope.technician.starRate) $scope.technician.starRate = 3;
            }
        });
    }
}
