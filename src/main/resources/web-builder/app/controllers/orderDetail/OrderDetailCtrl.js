import {Injector} from 'ngES6';

export default class OrderDetailCtrl extends Injector {
    static $inject = ['$scope', '$state', '$stateParams', 'OrderService'];
    static $template = require('./detail.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, OrderService} = this.$injected;
        this.attachMethodsTo($scope);
        this.orderId = $stateParams.orderId;
        OrderService.getDetail(this.orderId).then(res => {
            if (res.data.result) $scope.order = res.data.data;
        });
    }

}
