import {Injector} from 'ngES6';

export default class OrderMakeupCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', '$uibModal', 'ProductService', 'Settings'];
    static $template = require('./makeup.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, ProductService} = this.$injected;
        this.attachMethodsTo($scope);

        ProductService.getOrderProduct($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.product = res.data.message;
            } else {
                $scope.error = res.data.message;
            }
        });
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
        ProductService.saveProduct($scope.product.orderId, {productIds: productIdStr}).then(res => {
            if (res.data.status === true) {
                $state.go('^');
            } else {
                $scope.error = res.data.message;
            }
        });
    }
}
