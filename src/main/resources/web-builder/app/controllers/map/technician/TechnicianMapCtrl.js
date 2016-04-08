import {Injector} from 'ngES6';
import moment from 'moment';
import $ from 'jquery';

export default class TechnicianMapCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'TechnicianService', '$compile', '$timeout', '$sce'];
    static $template = require('./map.html');

    constructor(...args) {
        super(...args);
        const {$scope, TechnicianService, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
        TechnicianService.mapLocations().then(res => {
            if (res.data && res.data.result) {
                let items = res.data.data.list;
                items.forEach(i => {
                    i.label = i.technician.name;
                });
                $scope.items = items;
            }
        });
    }

    onItemClick(scope, evt) {
        const {$compile, $timeout, $sce} = this.$injected;
        let element = $(evt.target);
        if (!element.hasClass('mv-marker')) {
            $timeout(() => {
                element.closest('.mv-marker').click();
            });
            return;
        }
        if (element.data('has-popover')) return;

        scope.getHtml = () => {
            return scope.popoverHtml;
        };

        scope.popoverHtml = $sce.trustAsHtml(`
            <div class="clearfix">
                <img src="${scope.data.technician.avatar}" class="img-thumbnail pull-left m-r-10 m-b-10" style="width: 60px; height: 60px;">
                <h3>${scope.data.label}<small class="btn btn-success btn-xs m-l-5">${this.$injected.Settings.technicianStatus[scope.data.technician.status]}</small></h3>
            </div>
            <ul class="list-group">
                <li class="list-group-item">电话: ${scope.data.technician.phone}</li>
                <li class="list-group-item">上报时间: ${moment(scope.data.createAt).format('YYYY-MM-DD HH:mm')}</li>
                <li class="list-group-item">定位: ${scope.data.city + scope.data.district + scope.data.street + scope.data.streetNumber}</li>
            </ul>`);
        element.attr('uib-popover-html', 'popoverHtml');
        // element.attr('uib-popover', 'test');
        element.attr('popover-append-to-body', true);
        element.attr('popover-trigger', 'outsideClick');
        element.attr('popover-placement', 'right');
        element.attr('popover-is-open', true);
        $compile(element)(scope);
        element.data('has-popover', true);
    }
}
