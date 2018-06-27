import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class OrderMakeupCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', '$uibModal', 'ProductService', 'OrderService', 'Settings'];
    static $template = require('./makeup.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, ProductService, OrderService} = this.$injected;
        this.attachMethodsTo($scope);

        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data.status === true) {
                let order = $scope.order = res.data.message;

                order.position         = {lng: order.longitude, lat: order.latitude};
                $scope.photoList = [];
                $scope.photoList = $scope.order.photo.split(',');
            }
        });
        ProductService.getOrderProduct($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.product = res.data.message;
            } else {
                $scope.error = res.data.message;
            }
        });
        // $('.carousel').carousel();
        // $('#myCarousel').carousel();
    }

    save() {
        const {$scope, ProductService, $state} = this.$injected;
        let project = $scope.product.project;
        let productIdStr = '';
        for (let i = 0; i < project.length; i++) {
            for (let j = 0; j < project[i].productShowList.length; j++) {
                if (project[i].productShowList[j].productId > 0) {
                    if (productIdStr === '') {
                        productIdStr = project[i].productShowList[j].productId;
                    } else {
                        productIdStr = productIdStr + ',' + project[i].productShowList[j].productId;
                    }
                }
            }
        }
        ProductService.saveProduct($scope.product.orderId, {productIds: productIdStr, vehicleModel: $scope.vehicleModel, license: $scope.license, vin: $scope.vin, realOrderNum: $scope.realOrderNum, customerName: $scope.customerName, customerPhone: $scope.customerPhone, turnover: $scope.turnover, salesman: $scope.salesman}).then(res => {
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
