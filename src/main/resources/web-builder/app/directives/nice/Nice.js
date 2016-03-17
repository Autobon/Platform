import template from './nice.html';
import { Injector } from 'ngES6';

export default class Nice extends Injector {
    static $inject = ['$http'];

    constructor(...args) {
        super(...args);

        this.template = template;
        this.restrict = 'E';
        this.scope = '';
    }

    link(scope) {
        let self = this;
        scope.onClick = () => this.onClick.apply(self);
        scope.text = 'Directives are working';
        scope.counter = 1;
    }

    onClick() {
        const { scope } = this.link.$injected;

        scope.text = 'Directives events are working too (' + scope.counter++ + ')';
    }
}
