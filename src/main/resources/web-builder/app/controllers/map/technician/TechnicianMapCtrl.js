import {Injector} from 'ngES6';
import angular from 'angular';
import moment from 'moment';
import $ from 'jquery';

export default class TechnicianMapCtrl extends Injector {
    static $inject   = ['$scope', 'Settings', 'TechnicianService', '$compile', '$timeout', '$sce', '$state'];
    static $template = require('./map.html');

    constructor(...args) {
        super(...args);
        const {$scope, TechnicianService, Settings, $sce} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings     = Settings;

        TechnicianService.mapLocations().then(res => {
            if (res.data && res.data.result) {
                $scope.items = res.data.data.list;
            }
        });
        $scope.itemTemplate = $sce.trustAsHtml(`<div class="mv-marker" style="text-align: center;">
                                                    <img src="{{data.technician.avatar}}" style="width: 50px; height: 50px; border: 1px solid rgba(0,0,0,.2); border-radius: 10px;"><br>
                                                    <span>{{data.technician.name}}</span>
                                                    <div class="arrow"></div>
                                                </div>`);
        $scope.$on('map.marker.click', (e, evt) => {
            this.onItemClick(e, evt);
        });
    }

    onItemClick(e, evt) {
        e.stopPropagation();
        const {$compile, $timeout, $sce, $state} = this.$injected;
        let scope   = angular.element(evt.target).scope();
        let element = $(evt.target);
        if (!element.hasClass('mv-marker')) {
            $timeout(() => {
                element.closest('.mv-marker').click();
            });
            return;
        }
        if (element.data('has-popover')) return;

        scope.popoverHtml = $sce.trustAsHtml(`
            <div class="clearfix">
                <a href="${$state.href('console.technician.detail', {id: scope.data.technician.id})}">
                    <img src="${scope.data.technician.avatar}" class="img-thumbnail pull-left m-r-10 m-b-10" style="width: 60px; height: 60px;">
                    <h3>${scope.data.technician.name}<small class="btn btn-success btn-xs m-l-5">${this.$injected.Settings.technicianStatus[scope.data.technician.status]}</small></h3>
                </a>
            </div>
            <ul class="list-group" style="min-width: 250px; max-width: 400px;">
                <li class="list-group-item"><b>电话:</b> ${scope.data.technician.phone}</li>
                <li class="list-group-item"><b>定位时间:</b> ${moment(scope.data.createAt).format('YYYY-MM-DD HH:mm')}</li>
                <li class="list-group-item"><b>位置:</b> ${[scope.data.city, scope.data.district, scope.data.street, scope.data.streetNumber].join('')}</li>
            </ul>`);
        element.attr('uib-popover-html', 'popoverHtml');
        element.attr('popover-append-to-body', false);
        element.attr('popover-trigger', 'outsideClick');
        element.attr('popover-placement', 'right');
        element.attr('popover-is-open', true);
        $compile(element)(scope);
        element.data('has-popover', true);
    }
}
