import {Injector} from 'ngES6';
import './home.scss';

export default class HomeCtrl extends Injector {
    static $inject = ['$scope'];
    static $template = require('./home.html');

    constructor(...args) {
        super(...args);
        this.attachMethodsTo(this.$injected.$scope);
    }
}
