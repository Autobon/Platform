import {Injector} from 'ngES6';

export default class NgMatch extends Injector {
    static $inject = ['$parse'];

    constructor(...args) {
        super(...args);
        this.restrict = 'A';
        this.require = '?ngModel';
    }

    link(scope, elem, attrs, ctrl) {
        let directive = 'ngMatch';
        if (!ctrl) return; // if ngModel not exist, do nothing
        if (!attrs[directive]) return;

        let validator = v => {
            let firstPassword = this.$injected.$parse(attrs[directive])(scope);
            ctrl.$setValidity('match', v === firstPassword);
            return v;
        };

        ctrl.$parsers.unshift(validator);
        ctrl.$formatters.push(validator);
        attrs.$observe(directive, () => {
            validator(ctrl.$viewValue);
        });
    }
}
