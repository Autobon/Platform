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
                TechnicianService.mapTrack($stateParams.id, 1, 60).then(res2 => {
                    if (res2.data && res2.data.result) {
                        $scope.technician.locations = res2.data.data.list;
                    }
                });
            }
        });
    }

    verify(verified, msg) {
        const {$scope, TechnicianService} = this.$injected;
        TechnicianService.verify($scope.technician.id, verified, msg).then(res => {
            if (res.data && res.data.result) {
                const verifyObj = {status: verified ? 'VERIFIED' : 'REJECTED', verifyAt: new Date(), verifyMsg: msg};
                angular.extend($scope.technician, verifyObj);

                let technicians = $scope.$parent.technicians;
                let pTech = technicians.find(t => {return t.id === $scope.technician.id;});
                if (pTech) angular.extend(pTech, verifyObj);
            }
        });
    }
}
