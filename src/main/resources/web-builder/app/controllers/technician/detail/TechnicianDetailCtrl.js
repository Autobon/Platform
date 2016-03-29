import {Injector} from 'ngES6';
import angular from 'angular';

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

    verify(technician) {
        const {TechnicianService} = this.$injected;
        TechnicianService.verify(technician.id, true).then(res => {
            if (res.data && res.data.result) {
                technician.status = 'VERIFIED';
                technician.verifyAt = new Date();
            }
        });
    }

    reject(technician, msg) {
        const {$scope, TechnicianService} = this.$injected;
        TechnicianService.verify(technician.id, false, msg).then(res => {
            if (res.data && res.data.result) {
                const verifyObj = {status: 'REJECTED', verifyAt: new Date(), verifyMsg: msg};
                angular.extend(technician, verifyObj);

                let technicians = $scope.$parent.technicians;
                let pTech = technicians.find(t => {return t.id === technician.id;});
                if (pTech) angular.extend(pTech, verifyObj);
            }
        });
    }
}
