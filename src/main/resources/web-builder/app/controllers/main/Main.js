import './main.less';
import angular from 'angular';
import { Inject } from 'angular-es6';

export default class Main extends Inject {
    static $inject = ['$scope'];

    constructor(...args) {
        super(...args);
        let {$scope} = this.$inject;
        $scope.count = 0;
        $scope.onThisClick = this.onThisClick;
    }

    onThisClick($event) {
        angular.element($event.target).text('hello ' + ++this.count);
    }
}
