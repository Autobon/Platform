import {Injector} from 'ngES6';

export default class OrderMakeupCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', '$uibModal', 'ProductService', 'Settings'];
    static $template = require('./makeup.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, ProductService} = this.$injected;
        this.attachMethodsTo($scope);

        if ($stateParams.id) {
            ProductService.getOrderProduct($stateParams.id).then(res => {
                if (res.data.status === true) {
                    $scope.product = res.data.message;
                    console.log(JSON.stringify($scope.product));
                }
            });
        } else {
            $scope.product = {};
        }
    }

}
