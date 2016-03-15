/**
 * Created by dave on 16/3/14.
 */
import { Inject } from 'angular-es6';

export default class LoginCtrl extends Inject {
    static $inject = ['$scope', 'LoginService'];

    constructor(...args) {
        super(...args);
        let {$scope} = this.$inject;
        $scope.ctrl = this;
    }

    login() {
        const {$scope, LoginService} = this.$inject;
        LoginService.login($scope.username, $scope.password);
    }
}
