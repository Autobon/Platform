import {Injector} from 'ngES6';

export default class OrderDispatchCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./dispatch.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
    }
}
