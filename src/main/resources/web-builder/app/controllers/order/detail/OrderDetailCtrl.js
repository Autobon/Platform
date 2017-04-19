import {Injector} from 'ngES6';
import './detail.scss';

export default class OrderDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        $scope.comment = {
            arriveOnTime  : true,
            completeOnTime: true,
            professional  : true,
            dressNeatly   : true,
            carProtect    : true,
            goodAttitude  : true,
        };
        this.attachMethodsTo($scope);

        OrderService.getDetail2($stateParams.id).then(res => {
            if (res.data.status === true) {
                let order = $scope.order = res.data.message;

                order.position         = {lng: order.longitude, lat: order.latitude};
                $scope.order.photoList = [];
                $scope.order.photoList = $scope.order.photo.split(',');
                console.log($scope.order.beforePhotos);
                $scope.order.startPhotoList = [];
                $scope.order.startPhotoList = $scope.order.beforePhotos.split(',');
                console.log(JSON.stringify($scope.order.startPhotoList));

                $scope.order.afterPhotoList = [];
                $scope.order.afterPhotoList = $scope.order.afterPhotos.split(',');
            }
        });
        OrderService.getOrder($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.or = res.data.message;
            }
        });

        OrderService.getOrderProduct($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.products = res.data.message;
            }
        });
    }

    saveComment() {
        const {$scope, OrderService} = this.$injected;
        OrderService.addComment($scope.comment).then(res => {
            if (res.data) {
                if (res.data.result) {
                    delete $scope.commentResult;
                    $scope.order.comment = res.data.data;
                } else {
                    $scope.commentResult = res.data.message;
                }
            }
        });
    }
}
