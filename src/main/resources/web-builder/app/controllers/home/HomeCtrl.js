import {Injector} from 'ngES6';
import './home.scss';

export default class HomeCtrl extends Injector {
    static $inject = ['$scope', '$http', 'Settings', 'LoginService'];
    static $template = require('./home.html');

    constructor(...args) {
        super(...args);
        const {$scope, $http, Settings} = this.$injected;
        $http.get(Settings.domain + '/api/web/admin/console/statInfo').then(res => {
            if (res.data && res.data.result) {
                $scope.stat = res.data.data;
            }
        });
    }
}
