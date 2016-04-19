import {Injector} from 'ngES6';
import './console.scss';

export default class ConsoleCtrl extends Injector {
    static $inject   = ['$scope', '$state', 'AccountService'];
    static $template = require('./console.html');

    constructor(...args) {
        super(...args);
        const {$scope, $state} = this.$injected;
        this.attachMethodsTo($scope);
        if ($state.is('console')) {
            $state.go('console.home');
        }
    }

    logout() {
        this.$injected.AccountService.logout().then(res => {
            if (res.data && res.data.result) window.location.href = '/';
        });
    }
}
