import {Injector} from 'ngES6';
import './console.scss';

export default class ConsoleCtrl extends Injector {
    static $inject   = ['$scope', '$state', 'AccountService', 'LoginService', 'AccountService'];
    static $template = require('./console.html');

    constructor(...args) {
        super(...args);
        const {$scope, $state, LoginService, AccountService} = this.$injected;
        this.attachMethodsTo($scope);
        if ($state.is('console')) {
            $state.go('console.home');
        }
        $scope.staffMenu = {};
        $scope.homeShow = false;
        $scope.orderShow = false;
        $scope.coopShow = false;
        $scope.techShow = false;
        $scope.finaShow = false;
        $scope.prodShow = false;
        $scope.statShow = false;
        $scope.studyShow = false;
        $scope.techMapShow = false;
        $scope.coopMapShow = false;
        $scope.roleStaffShow = false;
        var menus = [];
        if (LoginService.getCookie('ro') != null) {
            AccountService.getStaffMenu(LoginService.getCookie('ro')).then(res => {
                if (res.data && res.data.result) {
                    $scope.staffMenu = res.data.data;
                    menus = res.data.data.menuId.split(',');
                    for (var i = 0; i < menus.length; i++) {
                        if (menus[i] === '1') {
                            $scope.homeShow = true;
                            continue;
                        }
                        if (menus[i] === '2') {
                            $scope.orderShow = true;
                            continue;
                        }
                        if (menus[i] === '3') {
                            $scope.coopShow = true;
                            continue;
                        }
                        if (menus[i] === '4') {
                            $scope.techShow = true;
                            continue;
                        }
                        if (menus[i] === '5') {
                            $scope.finaShow = true;
                            continue;
                        }
                        if (menus[i] === '6') {
                            $scope.prodShow = true;
                            continue;
                        }
                        if (menus[i] === '7') {
                            $scope.statShow = true;
                            continue;
                        }
                        if (menus[i] === '8') {
                            $scope.studyShow = true;
                            continue;
                        }
                        if (menus[i] === '9') {
                            $scope.techMapShow = true;
                            continue;
                        }
                        if (menus[i] === '10') {
                            $scope.coopMapShow = true;
                            continue;
                        }
                        if (menus[i] === '11') {
                            $scope.roleStaffShow = true;
                            continue;
                        }
                    }
                }
            });

        }

    }

    logout() {
        this.$injected.AccountService.logout().then(res => {
            if (res.data && res.data.result) window.location.href = '/';
        });
    }
}
