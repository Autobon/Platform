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
        $scope.positionList = [
            {id:1, name: '前风挡'},
            {id:2, name: '左前门'},
            {id:3, name: '右前门'},
            {id:4, name: '左后门+角'},
            {id:5, name: '右后门+角'},
            {id:6, name: '后风挡'},
            {id:7, name: '前保险杠'},
            {id:8, name: '引擎盖'},
            {id:9, name: '左右前叶子板'},
            {id:10, name: '四门'},
            {id:11, name: '左右后叶子板'},
            {id:12, name: '尾盖'},
            {id:13, name: '后保险杠'},
            {id:14, name: 'ABC柱套件'},
            {id:15, name: '车顶'},
            {id:16, name: '门拉手'},
            {id:17, name: '反光镜'},
            {id:18, name: '整车'},
        ];
        if ($stateParams.id) {
            ProductService.getDetail($stateParams.id).then(res => {
                if (res.data.status === true) {
                    $scope.product = res.data.message;
                }
            });
        } else {
            $scope.product = {};
        }
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
