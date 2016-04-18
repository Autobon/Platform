import {Injector} from 'ngES6';
import moment from 'moment';

export default class StatCtrl extends Injector {
    static $inject   = ['$scope', 'StatService'];
    static $template = require('./stat.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
        this.reset();
        this.getStat();
        $scope.stat.show = 'order';
    }

    beforeRenderDatetimepicker($view, $dates) {
        const now = moment();
        for (let i = 0; i < $dates.length; i++) {
            if ($dates[i].localDateValue() > now.valueOf()) {
                $dates[i].selectable = false;
            }
        }
    }

    getStat() {
        const {$scope, StatService} = this.$injected;
        let stat = $scope.stat;
        let [start, end] = stat.type === 1 ? [stat.startDay, stat.endDay] : [stat.startMonth, stat.endMonth];
        $scope.series = {
            order: ['新建订单', '完成订单'],
            coop: ['注册商户', '认证商户'],
            tech: ['注册技师', '认证技师'],
        };
        StatService.getStat(start, end, stat.type).then(res => {
            if (res.data && res.data.result) {
                $scope.labels = [];
                $scope.orderData = [[], []];
                $scope.techData = [[], []];
                $scope.coopData = [[], []];
                res.data.data.forEach(d => {
                    $scope.labels.push(moment(d.statTime).format(stat.type === 1 ? 'YYYY-MM-DD' : 'YYYY-MM'));
                    $scope.orderData[0].push(d.newOrderCount);
                    $scope.orderData[1].push(d.finishedOrderCount);
                    $scope.techData[0].push(d.newTechCount);
                    $scope.techData[1].push(d.verifiedTechCount);
                    $scope.coopData[0].push(d.newCoopCount);
                    $scope.coopData[1].push(d.verifiedCoopCount);
                });
            }
        });
    }

    reset() {
        this.$injected.$scope.stat   = {
            type: 1,
            startDay  : moment().subtract(1, 'M').format('YYYY-MM-DD'),
            endDay    : moment().format('YYYY-MM-DD'),
            startMonth: moment().subtract(1, 'Y').format('YYYY-MM'),
            endMonth  : moment().format('YYYY-MM'),
        };
    }
}
