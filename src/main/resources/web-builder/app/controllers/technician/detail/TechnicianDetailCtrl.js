import {Injector} from 'ngES6';
import angular from 'angular';
import moment from 'moment';
import $ from 'jquery';

export default class TechnicianDetailCtrl extends Injector {
    static $inject = ['$scope', '$state', '$stateParams', 'Settings', 'TechnicianService', '$compile'];
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
                        $scope.technician.locations = res2.data.data.list.reverse();
                    }
                });
            }
        });

        $scope.$on('map.track.point.mouseover', (e, evt, d) => {
            e.stopPropagation();
            const {$compile} = this.$injected;
            let element = $(evt.domEvent.target || evt.domEvent.srcElement);
            let scope   = angular.element(element).scope();

            if (element.data('has-tooltip')) return;

            element.attr('uib-tooltip', '定位时间: ' + moment(d.createAt).format('YYYY-MM-DD HH:mm'));
            element.attr('tooltip-append-to-body', true);
            element.attr('tooltip-is-open', true);
            $compile(element)(scope);
            element.data('has-tooltip', true);
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
