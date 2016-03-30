import {Injector} from 'ngES6';
import angular from 'angular';

export default class CooperatorDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'CooperatorService'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, CooperatorService} = this.$injected;
        this.attachMethodsTo($scope);

        CooperatorService.getDetail($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.cooperator = res.data.data;
                $scope.cooperator.position = {lng: $scope.cooperator.longitude, lat: $scope.cooperator.latitude};
            }
        });
    }

    verify(verified, msg) {
        const {$scope, CooperatorService} = this.$injected;
        CooperatorService.verify($scope.cooperator.id, verified, msg).then(res => {
            if (res.data && res.data.result) {
                const verifyObj = {statusCode: verified ? 1 : 2, remark: msg};
                angular.extend($scope.cooperator, verifyObj);

                let cooperators = $scope.$parent.cooperators;
                let pCoop = cooperators.find(p => {return p.id === $scope.cooperator.id;});
                if (pCoop) angular.extend(pCoop, verifyObj);
            }
        });
    }
}
