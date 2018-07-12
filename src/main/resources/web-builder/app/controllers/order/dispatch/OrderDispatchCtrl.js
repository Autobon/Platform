import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class OrderDispatchCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings', 'TechnicianService', '$compile', '$timeout', '$sce'];
    static $template = require('./dispatch.html');

    constructor(...args) {
        super(...args);

        $scope.$parent.showMore = false;
        const {$scope, $stateParams, OrderService, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings   = Settings;
        $scope.filter     = {sort: 'id'};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 5};

        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.orderShow = res.data.message;
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
                $scope.mapTechnicians = res.data.message;
                $scope.items = res.data.message.localStatuses;
                $scope.items.push({techName: '商户', lng: $scope.mapTechnicians.coopLongitude,
                    lat: $scope.mapTechnicians.coopLatitude, myclass: 'mv-marker-yellow', status: 0});  // 将商户加到技师的列表中，在地图中显示出来，状态默认为0
                if ($scope.items.length > 0) {
                    for (let i = 0; i < $scope.items.length; i++) {
                        if ($scope.items[i].status === 1) {
                            $scope.items[i].myclass = 'mv-marker-green';
                        } else if ($scope.items[i].status === 2) {
                            $scope.items[i].myclass = 'mv-marker-red';
                        } else if ($scope.items[i].status === 3) {
                            $scope.items[i].myclass = 'mv-marker-gray';
                        }
                        if ($scope.items[i].techId === $scope.orderShow.techId) {
                            $scope.items[i].flag = true;
                        } else {
                            $scope.items[i].flag = false;
                        }
                    }
                }
            }
        });
        $scope.itemTemplate = $sce.trustAsHtml(`<div class="{{data.myclass}}" style="text-align: center;" onclick="">
                                                    <span>{{data.techName}}</span>
                                                    <div class="arrow"></div>
                                                </div>`);
        $scope.$on('map.marker.click', (e, evt) => {
            this.onItemClick(e, evt);
        });
    }

    onItemClick(e, evt) {
        e.stopPropagation();
        const {$timeout, $scope} = this.$injected;
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
        if (scope.data.status === 0 ) return;  // 默认规定 状态为0的是商户，点击名称不触发派遣。
        $scope.dispatchTechnician(scope.data, 1);
    }

    // 获取技师列表
    getTechnician(resetPageNo) {
        const {$scope, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TechnicianService.getV2Search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data.status === true) {
                $scope.technicians = res.data.message.content;
                for (let i = 0; i < $scope.technicians.length; i++) {
                    if ($scope.technicians[i].id === $scope.orderShow.techId) {
                        $scope.technicians[i].flag = true;
                    } else {
                        $scope.technicians[i].flag = false;
                    }
                }
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

    // 重置技师列表
    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
    }

    // 指派
    dispatchTechnician(t, num) {
        const {$scope, $state, OrderService} = this.$injected;
        let techId = '';
        if (num === 2) {
            for (let i = 0; i < $scope.technicians.length; i++) {
                $scope.technicians[i].flag = false;
            }
            t.flag = true;
            techId = t.id;
        } else {
            techId = t.techId;
        }
        OrderService.dispatch({orderId: $scope.orderShow.id, techId: techId}).then(res => {
            if (res.data.status === true) {
                let pCoop = $scope.$parent.orders.find(c => c.id === res.data.message.id);
                if (pCoop) {
                    angular.extend(pCoop, res.data.message);
                }
                $state.go('^');
            } else {
                $scope.error = res.data.message;
            }
        });
    }
}
