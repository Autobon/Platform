import {Injector} from 'ngES6';
import angular from 'angular';

export default class CooperatorEditCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'ProductService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, ProductService} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.starLevelList = [{id:1, name: '1星'}, {id:2, name: '2星'}, {id:3, name: '3星'}, {id:4, name: '4星'}, {id:5, name: '5星'}];
        $scope.orderTypeList = [{id:1, name:'隔热膜'}, {id:2, name:'隐形车衣'}, {id:3, name:'车身改色'}, {id:4, name:'美容清洁'}];
        $scope.positionList = [];
        if ($stateParams.id) {
            ProductService.getDetail($stateParams.id).then(res => {
                if (res.data.status === true) {
                    $scope.product = res.data.message;
                    this.getPosition($scope.product.type);
                }
            });
        } else {
            $scope.product = {};
        }
    }

    getPosition(pid) {
        const {$scope, ProductService} = this.$injected;
        ProductService.getPositionByProject(pid).then(res => {
            if (res.data.status === true) {
                $scope.positionList = res.data.message;
            }
        });
    }


    save() {
        const {$scope, $state, ProductService} = this.$injected;
        let q, isUpdate       = !!$scope.product.id;
        if (isUpdate) {
            q = ProductService.update($scope.product);
        } else {
            q = ProductService.add($scope.product);
        }

        q.then(res => {
            if (res.data) {
                if (res.data.status === true) {
                    if (isUpdate) {
                        let pCoop = $scope.$parent.products.find(c => c.id === res.data.message.id);
                        if (pCoop) {
                            angular.extend(pCoop, res.data.message);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.products.unshift(res.data.message);
                    }
                    $state.go('^.detail', {id: res.data.message.id});
                } else {
                    $scope.error = res.data.message;
                }
            }
        });
    }
}
