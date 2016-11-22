import {Injector} from 'ngES6';

export default class ProductImportCtrl extends Injector {
    static $inject   = ['$scope', '$state', 'ProductService'];
    static $template = require('./import.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
    }
}
