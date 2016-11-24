import {Injector} from 'ngES6';
import angular from 'angular';
import './modify.scss';

export default class OrderModifyCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./modify.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.orderTypeList = [{id: 1, name: '隔热膜', state: false}, {id: 2, name:  '隐形车衣', state: false}, {id: 3, name:  '车身改色', state: false}, {id: 4, name: '美容清洁', state: false}];
        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                let orderShow = $scope.orderShow = res.data.data;
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
                console.log(JSON.stringify($scope.orderTypeList));
            }
        });
        OrderService.getAllPosition($stateParams.id).then(res => {
            if (res.data && res.data.result) {
                $scope.positionShow = res.data.data;
            }
        });
    }

    save() {
        const {$scope, $state, OrderService} = this.$injected;
        console.log(JSON.stringify($scope.orderTypeList));
        let q, isUpdate       = !!$scope.orderShow.id;
        if (isUpdate) {
            q = OrderService.update($scope.orderShow, $scope.workDetailShows, $scope.constructionWasteShows);
        } else {
            q = OrderService.add($scope.orderShow);
        }
        q.then(res => {
            if (res.data) {
                if (res.data.result) {
                    if (isUpdate) {
                        let pOrder = $scope.$parent.orders.find(c => c.id === res.data.data.id);
                        if (pOrder) {
                            angular.extend(pOrder, res.data.data);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.orders.unshift(res.data.data);
                    }
                    $state.go('^.detail', {id: res.data.data.id});
                } else {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
