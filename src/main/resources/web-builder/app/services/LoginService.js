/**
 * Created by dave on 16/3/14.
 */
import { Injector } from 'ngES6';

export default class LoginService extends Injector {
    static $inject = ['$http', 'Settings'];

    login(username, password) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/mobile/technician/login',
            {phone: username, password: password});
    }
}
