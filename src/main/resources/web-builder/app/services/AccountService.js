import {Injector} from 'ngES6';

export default class AccountService extends Injector {
    static $inject = ['$http', 'Settings'];

    changePassword(oldPassword, newPassword) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/changePassword', {oldPassword: oldPassword, newPassword: newPassword});
    }

    logout() {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/logout');
    }
}
