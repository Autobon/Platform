import { Injector } from 'ngES6';
import './login.scss';

export default class LoginCtrl extends Injector {
    static $inject = ['$scope', '$state', 'LoginService'];
    static $template = require('./login.html');

    constructor(...args) {
        super(...args);
        this.attachMethodsTo(this.$injected.$scope);
    }

    login() {
        const {$scope, $state, LoginService} = this.$injected;
        LoginService.login($scope.account, $scope.password).then((res) => {
            if (!res.data.result) {
                $scope.msg = res.data.message;
            } else {
                $state.go('console');
            }
        });
    }
}
