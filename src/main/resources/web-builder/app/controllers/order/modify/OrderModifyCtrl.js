import {Injector} from 'ngES6';
import angular from 'angular';
import moment from 'moment';
import './modify.scss';

export default class OrderModifyCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings', 'TechnicianService'];
    static $template = require('./modify.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.orderTypeList = [{id: 1, name: '隔热膜', state: false}, {id: 2, name:  '隐形车衣', state: false}, {id: 3, name:  '车身改色', state: false}, {id: 4, name: '美容清洁', state: false}];
        $scope.orderStatusList = [
            {id: 'CREATED_TO_APPOINT', name: '新建待指定技师'},
            {id:  'NEWLY_CREATED', name: '已推送未接单'},
            {id: 'TAKEN_UP', name: '已接单'},
            {id:  'SEND_INVITATION', name: '邀请合作'},
            {id:  'INVITATION_ACCEPTED', name: '邀请已接受'},
            {id:  'INVITATION_REJECTED', name: '邀请已拒绝'},
            {id:  'IN_PROGRESS', name: '施工中'},
            {id:  'SIGNED_IN', name: '已签到'},
            {id:  'FINISHED', name: '施工完成'},
            {id:  'COMMENTED', name: '已评论'},
            {id:  'GIVEN_UP', name: '已放弃'},
            {id:  'CANCELED', name: '已取消'},
            {id:  'EXPIRED', name: '已超时'},
        ];
        $scope.technicianShow = false; // 技师列表展示
        $scope.Settings   = Settings;
        $scope.filter     = {sort: 'id'};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 5};
        this.getTechnician();
        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data.status === true) {
                let orderShow = $scope.orderShow = res.data.message;
                orderShow.position = {lng: orderShow.longitude, lat: orderShow.latitude};
                let typeList =  orderShow.type.split(',');

                for (let i = 0; i < typeList.length; i++) {
                    if (typeList[i] === '1') {
                        $scope.orderTypeList[0].state = true;
                    } else if (typeList[i] === '2') {
                        $scope.orderTypeList[1].state = true;
                    } else if (typeList[i] === '3') {
                        $scope.orderTypeList[2].state = true;
                    } else if (typeList[i] === '4') {
                        $scope.orderTypeList[3].state = true;
                    }
                }
                for (let i = 0; i < $scope.orderStatusList.length; i++) {
                    // 当前状态
                    if ($scope.orderStatusList[i].id === $scope.orderShow.status) {
                        $scope.orderStatusList[i].flag = false;
                    } else {
                        // 取消状态
                        if ($scope.orderStatusList[i].id === 'CANCELED') {
                            $scope.orderStatusList[i].flag = false;
                        } else {
                            $scope.orderStatusList[i].flag = true;
                        }
                    }
                }
            }
        });
        OrderService.getAllPosition($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.positionShow = res.data.message;
            }
        });
    }

    // 获取技师列表
    getTechnician(resetPageNo) {
        const {$scope, TechnicianService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        TechnicianService.getV2Search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data.status === true) {
                $scope.technicians = res.data.message.content;
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

    // 重置技师列表
    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
    }

    beforeRenderDatetimepicker($view, $dates) {
        const now = moment();
        for (let i = 0; i < $dates.length; i++) {
            if ($dates[i].localDateValue() < now.valueOf()) {
                $dates[i].selectable = false;
            }
        }
    }

    // 技师列表展示
    changeView() {
        const {$scope} = this.$injected;
        $scope.technicianShow = !$scope.technicianShow;
    }

    // 派遣
    dispatchTechnician(t) {
        const {$scope} = this.$injected;
        $scope.orderShow.techName = t.name;
        $scope.orderShow.techPhone = t.phone;
        $scope.orderShow.techId = t.id;
        $scope.technicianShow = !$scope.technicianShow;
    }

    save() {
        const {$scope, $state, OrderService} = this.$injected;
//        console.log(JSON.stringify($scope.orderTypeList));
        let q, isUpdate       = !!$scope.orderShow.id;

        if (isUpdate) {
            let typeOrder = '';
            for (let i = 0; i < $scope.orderTypeList.length; i++) {
                if ($scope.orderTypeList[i].state === true) {
                    if (typeOrder === '') {
                        typeOrder = $scope.orderTypeList[i].id;
                    } else {
                        typeOrder = typeOrder + ',' +  $scope.orderTypeList[i].id;
                    }
                }
            }
            $scope.orderShow.type = typeOrder;  // 订单施工项目
            $scope.orderShow.positionLon = $scope.orderShow.position.lng;
            $scope.orderShow.positionLat = $scope.orderShow.position.lat;

            // 时间转毫秒数
            $scope.orderShow.agreedStartTime = (new Date($scope.orderShow.agreedStartTime)).getTime();
            $scope.orderShow.agreedEndTime = (new Date($scope.orderShow.agreedEndTime)).getTime();
            q = OrderService.update($scope.orderShow);
        } else {
//            q = OrderService.add($scope.orderShow);
        }
        q.then(res => {
            if (res.data) {
                if (res.data.status) {
                    if (isUpdate) {
                        let pOrder = $scope.$parent.orders.find(c => c.id === res.data.message.id);
                        if (pOrder) {
                            angular.extend(pOrder, res.data.message);
                        }
//                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
//                        $scope.$parent.orders.unshift(res.data.data);
                    }
                    $state.go('^.detail', {id: res.data.message.id});
                } else {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
