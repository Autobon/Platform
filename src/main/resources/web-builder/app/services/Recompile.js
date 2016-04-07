import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class Recompile extends Injector {
    static $inject = ['$compile'];

    constructor(...args) {
        super(...args);
    }

    compile(element, attr) {
        const {$compile} = this.$injected;
        let _element = $(element);
        Object.keys(attr).forEach(k => {
            _element.attr(k, attr[k]);
        });
        _element.replaceWith($compile(_element[0].outerHTML)(angular.element(element).scope()));
    }
}
