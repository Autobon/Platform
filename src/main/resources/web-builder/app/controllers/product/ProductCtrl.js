import {Injector} from 'ngES6';

export default class ProductCtrl extends Injector {
    static $inject   = ['$scope', 'Settings', 'ProductService'];
    static $template = require('./product.html');

    constructor(...args) {
        super(...args);
        const {$scope, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings   = Settings;
        $scope.filter     = {sort: 'id'};
        $scope.pagination = {page: 1, totalItems: 0, pageSize: 15};
        $scope.techQuery = {};
        this.getProduces();
    }

    getProduces(resetPageNo) {
        const {$scope, ProductService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        ProductService.search($scope.filter, resetPageNo ? 1 : page, pageSize).then(res => {
            if (res.data.status === true) {
                $scope.products = res.data.message.content;
                $scope.pagination.totalItems = res.data.message.totalElements;
            }
        });
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
    }
}
