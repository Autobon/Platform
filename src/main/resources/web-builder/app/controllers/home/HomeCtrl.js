import {Injector} from 'ngES6';
import './home.scss';

export default class HomeCtrl extends Injector {
    static $inject = ['$scope', '$http', 'Settings', 'LoginService', 'AccountService'];
    static $template = require('./home.html');

    constructor(...args) {
        super(...args);
        const {$scope, $http, Settings, AccountService, LoginService} = this.$injected;
        $http.get(Settings.domain + '/api/web/admin/console/statInfo').then(res => {
            if (res.data && res.data.result) {
                $scope.stat = res.data.data;
            }
        });
        $scope.getOrder = false;
        $scope.getCoop = false;
        $scope.getTech = false;
        if (LoginService.getCookie('ro') !== null) {
            AccountService.getStaffRole().then((res) => {
                if (res.data && res.data.result) {
                    let menus = res.data.data[0].menuIds.split(',');
                    if (menus.indexOf('2') !== -1) {
                        $scope.getOrder = true;
                    }
                    if (menus.indexOf('3') !== -1) {
                        $scope.getCoop = true;
                    }
                    if (menus.indexOf('4') !== -1) {
                        $scope.getTech = true;
                    }
                }
            });
        }
    }
}
