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
            }
        });
    }

    verify(cooperator, verified, msg) {
        const {$scope, CooperatorService} = this.$injected;
        CooperatorService.verify(cooperator.id, verified, msg).then(res => {
            if (res.data && res.data.result) {
                const verifyObj = {status: verified ? 1 : 2, verifyMsg: msg};
                angular.extend(cooperator, verifyObj);

                let cooperators = $scope.$parent.cooperators;
                let pCoop = cooperators.find(p => {return p.id === cooperator.id;});
                if (pCoop) angular.extend(pCoop, verifyObj);
            }
        });
    }
}
