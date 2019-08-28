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
        $scope.modulus = 0;
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
            $scope.imgWidth = document.querySelector(".imgdiv").offsetWidth
        }, 1000);

        //图片操作
        $scope.winWidth = 0;
        $scope.winHeight = 0;
        $scope.isPicture = false;
        $scope.num = 0;



        $scope.getCss = function (o,key) {
            //console.log(99999999977777)
            return o.currentStyle? o.currentStyle[key] : document.defaultView.getComputedStyle(o,false)[key];

        }

        this.startDrag(document.querySelector(".img-responsive"),document.querySelector(".img-responsive"))


    }

    save() {
        const {$scope, ProductService, $state} = this.$injected;
        let project = $scope.product.project;
        console.log($scope.product.project)
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

    //图片拖拽
    startDrag(bar,target,callback) {
        const {$scope} = this.$injected;
        let params = {
            left: 0,
            top: 0,
            currentX: 0,
            currentY: 0,
            flag: false
        }
        //console.log(4564464566464)
        if($scope.getCss(target, "left") !== "auto"){
            params.left = $scope.getCss(target, "left");
        }
        if($scope.getCss(target, "top") !== "auto"){
            params.top = $scope.getCss(target, "top");
        }
        bar.onmousedown = function(event){
            params.flag = true;
            if(!event){
                event = window.event;
            }
            let e = event;
            params.currentX = e.clientX;
            params.currentY = e.clientY;
        };
        document.onmouseup = function(){
            params.flag = false;

            if($scope.getCss(target, "left") !== "auto"){
                params.left = $scope.getCss(target, "left");
            }
            if($scope.getCss(target, "top") !== "auto"){
                params.top = $scope.getCss(target, "top");
            }

        };
        document.onmousemove = function(event){
            let e = event ? event: window.event;

            if(params.flag){
                let nowX = e.clientX, nowY = e.clientY;
                let disX = nowX - params.currentX, disY = nowY - params.currentY;
                target.style.left = parseInt(params.left) + disX+ "px";
                target.style.top = parseInt(params.top) + disY+ "px";

                if (typeof callback == "function") {
                    callback((parseInt(params.left) || 0) + disX, (parseInt(params.top) || 0) + disY);
                }

                if (event.preventDefault) {
                    event.preventDefault();
                }
                return false;
            }

        }

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
   
    //点击放大图片
    magnify(){
        const {$scope} = this.$injected;
        if($scope.modulus>=9){
            return
        }
        $scope.modulus++;
        angular.element(document.querySelector(".img-responsive")).css({ transform: 'rotate(' + 90 * $scope.num + 'deg) scale(' + (1 + $scope.modulus / 10 )+  ')'
        })

    }
    //点击缩小图片
    shrink(){
        const {$scope} = this.$injected;
        if($scope.modulus<=-9){
            return
        }
        $scope.modulus--;
        angular.element(document.querySelector(".img-responsive")).css({ transform: 'rotate(' + 90 * $scope.num + 'deg) scale(' + (1 + $scope.modulus / 10 )+  ')'
        });

    }
    clickImg(){
        const {$scope} = this.$injected;
        this.getWindowWH();
        $scope.num ++;
        angular.element(document.querySelector(".img-responsive")).css({ transform: 'rotate(' + 90 * $scope.num + 'deg) scale(' + (1 + $scope.modulus / 10 )+  ')' });
        let img = new Image();
        img.src = $scope.currentImg;
        angular.element(document.querySelector(".img-responsive")).css({ width: $scope.imgWidth + 'px' });
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
