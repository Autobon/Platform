import './main.less';
import angular from 'angular';
import { Inject } from 'angular-es6';

export default class Main extends Inject {
    static $inject = ['$scope', '$http'];

    constructor(...args) {
        super(...args);
        let {$scope} = this.$inject;

        $scope.count = 0;
    }

    onThisClick($event) {
        console.log(Main.$instance);
        console.log(Main.$injected);
        angular.element($event.target).text('hello ' + ++this.count);
    }
}
