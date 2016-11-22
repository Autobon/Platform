import {Injector} from 'ngES6';

export default class ProductDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'Settings',  'ProductService'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, Settings, ProductService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings   = Settings;

        ProductService.getDetail($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.product = res.data.message;
            }
        });
    }
}
