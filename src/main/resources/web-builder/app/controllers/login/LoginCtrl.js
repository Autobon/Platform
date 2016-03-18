/**
 * Created by dave on 16/3/14.
 */
import { Injector } from 'ngES6';

export default class LoginCtrl extends Injector {
    static $inject = ['$scope', 'LoginService'];

    constructor(...args) {
        super(...args);
        let {$scope} = this.$injected;
        $scope.ctrl = this;
    }

    login() {
        const {$scope, LoginService} = this.$injected;
        LoginService.login($scope.username, $scope.password);
    }
}
