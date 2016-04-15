import {Injector} from 'ngES6';
import './console.scss';

export default class ConsoleCtrl extends Injector {
    static $inject = ['$state'];
    static $template = require('./console.html');

    constructor(...args) {
        super(...args);
        const {$state} = this.$injected;
        if ($state.is('console')) {
            $state.go('console.home');
        }
    }
}
