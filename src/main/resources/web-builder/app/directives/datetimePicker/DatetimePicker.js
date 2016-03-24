import {Injector} from 'ngES6';
// import moment from 'moment';
// import './picker.scss';

export default class DatetimePicker extends Injector {
    static $inject = ['$parse'];

    constructor(...args) {
        super(...args);
        this.require      = 'ngModel';
        this.restrict     = 'EA';
        this.scope        = {datetimePickerOptions: '@'};
        this.link.$inject = ['scope', 'element', 'attrs', 'controller'];
    }

    link(/* scope, element, attrs, controller */) {
        // let options = scope.$eval(attrs.datetimePickerOptions) || {};
        // if (!options.format) options.format = 'YYYY-MM-DD HH:mm';
        //
        // let input = this.$injected.$parse(attrs.ngModel);
        // scope.currentDate = moment(input, options.format);
        // if (!scope.currentDate.isValid()) {
        //     scope.currentDate = moment();
        // }
        // scope.viewDate = scope.currentDate;

        // let options = scope.$eval(attrs.datetimepickerOptions) || {};
        // if (!options.format) options.format = 'YYYY-MM-DD HH:mm';
        //
        // element.on('dp.change dp.update change', function() {
        //     let dtp = element.data('DateTimePicker');
        //     controller.$setViewValue(dtp.date().format(options.format));
        // });
        //
        // element.datetimepicker(options);
    }
}
