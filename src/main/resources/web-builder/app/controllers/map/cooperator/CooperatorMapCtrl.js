import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class CooperatorMapCtrl extends Injector {
    static $inject   = ['$scope', 'CooperatorService', '$compile', '$timeout', '$sce', '$state'];
    static $template = `<map-view items="items" center="中国" item-template="{{itemTemplate}}"
                            style="height: 100%; width: 100%; border: 1px solid #cabe9c; position: absolute;"></map-view>`;

    constructor(...args) {
        super(...args);
        const {$scope, CooperatorService, $sce} = this.$injected;

        CooperatorService.mapLocations().then(res => {
            if (res.data && res.data.result) {
                let items = res.data.data.list;
                items.forEach(i => {
                    i.lng = i.longitude;
                    i.lat = i.latitude;
                });
                $scope.items = items;
            }
        });
        $scope.itemTemplate = $sce.trustAsHtml(`<div class="mv-marker" style="text-align: center;">
                                                    <span>{{data.fullname}}</span>
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
                <a href="${$state.href('console.cooperator.detail', {id: scope.data.id})}">
                    <h4>${scope.data.fullname}<small class="btn btn-success btn-xs m-l-5">${['未认证', '认证通过', '认证失败'][scope.data.statusCode]}</small></h4>
                </a>
            </div>
            <ul class="list-group" style="min-width: 250px; max-width: 400px;">
                <li class="list-group-item"><b>法人姓名:</b> ${scope.data.corporationName}</li>
                <li class="list-group-item"><b>联系电话:</b> ${scope.data.contactPhone}</li>
                <li class="list-group-item"><b>位置:</b> ${[scope.data.province, scope.data.city, scope.data.district].join('-')}<br>${scope.data.address}</li>
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
