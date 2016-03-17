import './main.less';
import angular from 'angular';
import { Injector } from 'ngES6';

export default class Main extends Injector {
    static $inject = ['$scope', 'LoginService'];

    constructor(...args) {
        super(...args);
        this.attachMethodsTo(this.$injected.$scope);
        this.count = 0;
    }

    onclick($event) {
        const {LoginService} = this.$injected;

        angular.element($event.target).text('hello ' + ++this.count);
        LoginService.login('18827075338', '123456').success(data => {
            console.log(data);
        });
    }

}
