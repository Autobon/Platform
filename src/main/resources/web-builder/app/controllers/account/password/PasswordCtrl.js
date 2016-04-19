import {Injector} from 'ngES6';

export default class PasswordCtrl extends Injector {
    static $inject = ['$scope', 'AccountService'];
    static $template = require('./password.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
    }

    changePassword() {
        const {$scope, AccountService} = this.$injected;
        AccountService.changePassword($scope.oldPassword, $scope.newPassword).then(res => {
            if (res.data) {
                if (res.data.result) {
                    $scope.message = '修改密码成功';
                } else {
                    $scope.message = res.data.message;
                }
            }
        });
    }
}
