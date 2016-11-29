import {Injector} from 'ngES6';
import angular from 'angular';
import moment from 'moment';
import $ from 'jquery';

export default class OrderDispatchCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings', 'TechnicianService', '$compile', '$timeout', '$sce'];
    static $template = require('./dispatch.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings   = Settings;
        $scope.dispatchType = 1;
        $scope.filter     = {sort: 'id'};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 5};

        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.orderShow = res.data.message;
                console.log(JSON.stringify($scope.orderShow));
                this.getTechnician();
                this.getMapTechnician();
            }
        });
    }

    // 获取技师地图
    getMapTechnician() {
        const {$scope, OrderService, $sce} = this.$injected;
        // 默认10km
        OrderService.mapTechnician($scope.orderShow.id, {kilometre: 10}).then(res => {
            if (res.data.status === true) {
                $scope.items = res.data.message.localStatuses;
                if ($scope.items.length > 0) {
                    for (let i = 0; i < $scope.items.length; i++) {
                        console.log($scope.items[i].technician.status);
                        if ($scope.items[i].technician.status === 1) {
                            $scope.items[i].technician.myclass = 'mv-marker-green';
                        } else if ($scope.items[i].technician.status === 2) {
                            $scope.items[i].technician.myclass = 'mv-marker-red';
                        } else if ($scope.items[i].technician.status === 3) {
                            $scope.items[i].technician.myclass = 'mv-marker-gray';
                        }
                    }
                }
                console.log('AA:' + JSON.stringify($scope.items));
            }
        });
        $scope.itemTemplate = $sce.trustAsHtml(`<div class="{{data.technician.myclass}}" style="text-align: center;">
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
        if (!element.hasClass('mv-marker') && !element.hasClass('mv-marker-red')  && !element.hasClass('mv-marker-green')  && !element.hasClass('mv-marker-gray')) {
            $timeout(() => {
                element.closest('.mv-marker').click();
                element.closest('.mv-marker-red').click();
                element.closest('.mv-marker-green').click();
                element.closest('.mv-marker-gray').click();
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

    // 获取技师列表
    getTechnician(resetPageNo) {
        const {$scope, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TechnicianService.getV2Search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data.status === true) {
                $scope.technicians = res.data.message.list;
                for (let i = 0; i < $scope.technicians.length; i++) {
                    if ($scope.technicians[i].id === $scope.orderShow.techId) {
                        $scope.technicians[i].flag = true;
                    } else {
                        $scope.technicians[i].flag = false;
                    }
                }
                $scope.pagination.totalItems = res.data.message.totalPages;
            }
        });
    }

    // 重置技师列表
    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
    }

    // 选择指派模式
    changeRadio(num) {
        const {$scope} = this.$injected;
        if (num === 1) {
            $scope.dispatchType = 1;
        } else if (num === 2) {
            $scope.dispatchType = 2;
        }
    }

    // 指派
    dispatchTechnician(t) {
        const {$scope, $state, OrderService} = this.$injected;
        for (let i = 0; i < $scope.technicians.length; i++) {
            $scope.technicians[i].flag = false;
        }
        t.flag = true;
        OrderService.dispatch({orderId: $scope.orderShow.id, techId: t.id}).then(res => {
            if (res.data.status === true) {
                console.log(res.data.message);
                $state.go('^');
            } else {
                $scope.error = res.data.message;
            }
        });
    }


}
