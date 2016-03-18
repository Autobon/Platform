import './main.less';
import angular from 'angular';
import { Injector } from 'ngES6';

export default class Main extends Injector {
    static $inject = ['$scope', 'LoginService'];
    static $template = require('./main.html');

    constructor(...args) {
        super(...args);
        this.attachMethodsTo(this.$injected.$scope);
        this.counter = 1;
        this.$injected.$scope.counter = 0;
    }

    onclick(e) {
        const {$scope, LoginService} = this.$injected;

        angular.element(e.target).text('hello [' + [$scope.counter++, this.counter++] + ']');
        LoginService.login('18827075338', '123456').success(data => {
            console.log(data);
        });
    }

}
