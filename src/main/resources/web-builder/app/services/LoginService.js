import {Injector} from 'ngES6';

export default class LoginService extends Injector {
    static $inject = ['$http', 'Settings'];

    login(username, password) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/login',
            {username: username, password: password});
    }

    getCookie(name) {
        let arr = document.cookie.split('; ');
        for (let i = 0, len = arr.length; i < len; i++) {
            let item = arr[i].split('=');
            if (item[0] === name) {
                return item[1];
            }
        }
        return '';
    }
}
