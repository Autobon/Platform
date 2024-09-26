import {Injector} from 'ngES6';
import './login.scss';

export default class LoginCtrl extends Injector {
    static $inject = ['$scope', '$state', 'LoginService', 'AccountService'];
    static $template = require('./login.html');

    constructor(...args) {
        super(...args);
        this.attachMethodsTo(this.$injected.$scope);
    }

    login() {
        const {$scope, $state, LoginService, AccountService} = this.$injected;
        LoginService.login($scope.account, $scope.password).then((res) => {
            if (res.data) {
                if (!res.data.result) $scope.msg = res.data.message;
                else {
                    AccountService.getStaffRole().then((res) => {
                        if (!res.data.result) $scope.msg = res.data.message;
                        else {
                            let menus = res.data.data[0].menuIds.split(',');
                            let j = 0;
                            for (let i = 0; i < menus.length; i++) {
                                if (menus[i] === '1') {
                                    $state.go('console.home');
                                    break;
                                } else {
                                    j++;
                                }
                            }
                            if (j === menus.length) {
                                if (menus.indexOf('1') !== -1) {
                                    $state.go('console.home');
                                } else if (menus.indexOf('2') !== -1) {
                                    $state.go('console.order');
                                } else if (menus.indexOf('3') !== -1) {
                                    $state.go('console.cooperator');
                                } else if (menus.indexOf('4') !== -1) {
                                    $state.go('console.technician');
                                } else if (menus.indexOf('5') !== -1) {
                                    $state.go('console.finance');
                                } else if (menus.indexOf('6') !== -1) {
                                    $state.go('console.product');
                                } else if (menus.indexOf('7') !== -1) {
                                    $state.go('console.stat');
                                } else if (menus.indexOf('8') !== -1) {
                                    $state.go('console.study');
                                } else if (menus.indexOf('9') !== -1) {
                                    $state.go('console.staff');
                                } else if (menus.indexOf('10') !== -1) {
                                    $state.go('console.role');
                                } else if (menus.indexOf('11') !== -1) {
                                    $state.go('console.role');
                                } else if (menus.indexOf('12') !== -1) {
                                    $state.go('console.merchandiser');
                                }
                            }
                        }
                    });
                }
            } else {
                $scope.msg = '访问网络失败';
            }
        });
    }
}
