import {Injector} from 'ngES6';

export default class StatCtrl extends Injector {
    static $inject   = ['$scope'];
    static $template = require('./stat.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.filter = {};
        $scope.stat = {
            type: 1,
            show: 'NewOrder',
        };
    }
}
