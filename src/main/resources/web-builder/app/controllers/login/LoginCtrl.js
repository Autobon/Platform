import {Injector} from 'ngES6';
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
            if (res.data) {
                if (!res.data.result) $scope.msg = res.data.message;
                else {
                    var menus = res.data.data.menuId.split(',');
                    var j = 0;
                    for (var i = 0; i < menus.length; i++) {
                        if (menus[i] === '1') {
                            $state.go('console.home');
                            break;
                        } else {

                            j++;
                        }
                    }
                    if (j == menus.length) {
                        if (menus.indexOf("1") != -1) {
                            $state.go('console.home');
                        } else if (menus.indexOf("2") != -1) {
                            $state.go('console.order');
                        } else if (menus.indexOf("3") != -1) {
                            $state.go('console.cooperator');
                        } else if (menus.indexOf("4") != -1) {
                            $state.go('console.technician');
                        } else if (menus.indexOf("5") != -1) {
                            $state.go('console.finance');
                        } else if (menus.indexOf("6") != -1) {
                            $state.go('console.product');
                        } else if (menus.indexOf("7") != -1) {
                            $state.go('console.stat');
                        } else if (menus.indexOf("8") != -1) {
                            $state.go('console.study');
                        } else if (menus.indexOf("9") != -1) {
                            $state.go('console.map.technician');
                        } else if (menus.indexOf("10") != -1) {
                            $state.go('console.map.cooperator');
                        } else if (menus.indexOf("11") != -1) {
                            $state.go('console.role');
                        }
                    }

                }
            } else {
                $scope.msg = '访问网络失败';
            }
        });
    }
}
