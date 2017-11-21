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
                            j++;
                        }
                    }
                    if (j == menus.length) {
                        switch (menus[0]) {
                            case '1':
                                $state.go('console.home');
                            case '2':
                                $state.go('console.order');
                            case '3':
                                $state.go('console.cooperator');
                            case '4':
                                $state.go('console.technician');
                            case '5':
                                $state.go('console.finance');
                            case '6':
                                $state.go('console.product');
                            case '7':
                                $state.go('console.stat');
                            case '8':
                                $state.go('console.study');
                            case '9':
                                $state.go('console.map.technician');
                            case '10':
                                $state.go('console.map.cooperator');
                            case '11':
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
