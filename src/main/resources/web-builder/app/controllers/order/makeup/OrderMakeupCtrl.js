import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class OrderMakeupCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', '$uibModal', 'ProductService', 'OrderService', 'Settings', '$timeout'];
    static $template = require('./makeup.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, ProductService, OrderService, $timeout} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.$parent.showMore = false;
        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data.status === true) {
                let order = $scope.order = res.data.message;

                order.position         = {lng: order.longitude, lat: order.latitude};
                $scope.photoList = [];
                $scope.photoList = $scope.order.photo.split(',');
                if ($scope.photoList.length > 0) $scope.currentImg = $scope.photoList[$scope.imgNum];
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
        $scope.myInterval = 2000;
        $scope.imgNum = 0;
        $scope.active = 0;
        $scope.slides = [];
        $scope.addSlide = function () {
            $scope.photoList.forEach((ph,i) =>{
                $scope.slides.push({
                    image: ph,
                    text: (i + 1).toString(),
                    id: i
                });
            });
            /*$scope.slides.push({
                image: '/uploads/order/photo/20180615173955992780.jpeg',
                text: '1',
                id: 0
            });
            $scope.slides.push({
                image: '/uploads/order/photo/20180615173919974603.jpeg',
                text: '2',
                id: 1
            });
            $scope.slides.push({
                image: '/uploads/order/photo/20180615175332230701.jpeg',
                text: '3',
                id: 2
            });*/
        };
        $timeout(() => {
            $scope.addSlide();
            $scope.imgWidth = document.querySelector(".imgdiv").offsetWidth;
        }, 1000);

        //图片操作
        $scope.winWidth = 0;
        $scope.winHeight = 0;
        $scope.isPicture = false;
        $scope.num = 0;

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
        ProductService.saveProduct($scope.product.orderId, {productIds: productIdStr, vehicleModel: $scope.vehicleModel, license: $scope.license, vin: $scope.vin, realOrderNum: $scope.realOrderNum, customerName: $scope.customerName, customerPhone: $scope.customerPhone, turnover: $scope.turnover, salesman: $scope.salesman, remark: $scope.remark}).then(res => {
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
    checkSelect(index){
        const {$scope} = this.$injected;
        let project = $scope.product.project;
        if(index != 28){
            for (let i = 0; i < project.length; i++) {
                let aaa = project[i].productShowList;
                let ccc = aaa.find((item) => (item.positionId == 28));
                console.log(ccc);
                if(ccc.productId > 0) return true;
                if (index == 108 || index == 109) {
                    let bbb = aaa.find((item) => (item.positionId == 29));
                    console.log(bbb);
                    console.log(aaa);
                    if(bbb.productId > 0){
                        return true;
                    }
                }
            }
        }
        // if(index == 18) {
        //     return true;
        // }
        return false;
    }

    // ===================图片操作=============================
    next() {
        const {$scope} = this.$injected;
        $scope.imgNum ++;
        if ($scope.imgNum < $scope.photoList.length) $scope.currentImg = $scope.photoList[$scope.imgNum];
        else $scope.imgNum --;
    }
    previous() {
        const {$scope} = this.$injected;
        $scope.imgNum --;
        if ($scope.imgNum >= 0 && $scope.imgNum < $scope.photoList.length) $scope.currentImg = $scope.photoList[$scope.imgNum];
        else $scope.imgNum ++;
    }
    clickImg(){
        const {$scope} = this.$injected;
        this.getWindowWH();
        $scope.num ++;
        angular.element(document.querySelector(".img-view-content")).css({ transform: 'rotate(' + 90 * $scope.num + 'deg) scale(1, 1)' });
        let img = new Image();
        img.src = $scope.currentImg;
        angular.element(document.querySelector(".img-view-content")).css({ width: $scope.imgWidth + 'px' });
    }
    // 获取浏览器宽高
    getWindowWH() {
        const {$scope} = this.$injected;
        if (window.innerWidth)
            $scope.winWidth = window.innerWidth;
        else if ((document.body) && (document.body.clientWidth))
            $scope.winWidth = document.body.clientWidth;
        // 获取窗口高度
        if (window.innerHeight)
            $scope.winHeight = window.innerHeight;
        else if ((document.body) && (document.body.clientHeight))
            $scope.winHeight = document.body.clientHeight;
        // 通过深入 Document 内部对 body 进行检测，获取窗口大小
        if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
            $scope.winHeight = document.documentElement.clientHeight;
            $scope.winWidth = document.documentElement.clientWidth;
        }
    }
}
