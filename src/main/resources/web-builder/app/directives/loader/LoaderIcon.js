import angular from 'angular';
import {Injector} from 'ngES6';
import template from './loader.html';
import './loader.scss';

export default class LoaderIcon extends Injector {

    constructor(...args) {
        super(...args);
        this.template = template;
        this.replace = true;
        this.restrict = 'EA';
        this.scope = {
            option: '@',
        };
        this.link.$inject = ['scope', 'element'];
    }

    link(scope, element) {
        let opt = {
            width: '30px',
            height: '20px',
            color: '#333',
            'item-width': '3px',
        };
        angular.extend(opt, scope.option);
        element.css({width: opt.width, height: opt.height});
        element.find('div').css({width: opt['item-width'], 'background-color': opt.color});
    }
}
