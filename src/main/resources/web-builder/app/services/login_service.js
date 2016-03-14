/**
 * Created by dave on 16/3/14.
 */
import { Inject } from 'angular-es6';

export default class LoginService extends Inject {
    static $inject = ['$http'];

    login(username, account) {
        const {$http} = this.$inject;
        $http.post('/api/web/admin/login', {username: username, account: account});
    }
}
