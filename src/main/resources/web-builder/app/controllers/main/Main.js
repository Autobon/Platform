import './main.less';
import $ from 'jquery';
import {Injector} from 'ngES6';

export default class Main extends Injector {
    static $inject = ['$scope', 'LoginService', 'Recompile'];
    static $template = require('./main.html');

    constructor(...args) {
        super(...args);
        this.attachMethodsTo(this.$injected.$scope);
        this.counter = 1;
        this.$injected.$scope.counter = 0;
    }

    onclick(e) {
        const {$scope, LoginService, Recompile} = this.$injected;

        $(e.target).text('hello [' + [$scope.counter++, this.counter++] + ']');
        LoginService.login('18827075338', '123456').success(data => {
            console.log(data);
        });

        Recompile.compile($('#PopTest'), {
            'uib-popover': '动态指令!',
            'popover-append-to-body': true,
            'popover-trigger': 'outsideClick',
        });
    }

}
