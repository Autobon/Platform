import {Injector} from 'ngES6';
import moment from 'moment';

export default class StatCtrl extends Injector {
    static $inject   = ['$scope'];
    static $template = require('./stat.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.filter = {
            startDay  : moment().subtract(1, 'M').format('YYYY-MM-DD'),
            endDay    : moment().format('YYYY-MM-DD'),
            startMonth: moment().subtract(1, 'Y').format('YYYY-MM'),
            endMonth  : moment().format('YYYY-MM'),
        };
        $scope.stat   = {
            type: 1,
            show: 'NewOrder',
        };
    }

    beforeRenderDatetimepicker($view, $dates) {
        const now = moment();
        for (let i = 0; i < $dates.length; i++) {
            if ($dates[i].localDateValue() > now.valueOf()) {
                $dates[i].selectable = false;
            }
        }
    }
}
