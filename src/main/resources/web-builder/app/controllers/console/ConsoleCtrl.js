import {Injector} from 'ngES6';
import './console.scss';

export default class ConsoleCtrl extends Injector {
    static $inject = ['$scope'];
    static $template = require('./console.html');

    constructor(...args) {
        super(...args);
        this.attachMethodsTo(this.$injected.$scope);
    }
}
