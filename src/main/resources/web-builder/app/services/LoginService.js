/**
 * Created by dave on 16/3/14.
 */
import { Injector } from 'ngES6';

export default class LoginService extends Injector {
    static $inject = ['$http'];

    login(username, password) {
        const {$http} = this.$injected;
        return $http.post('http://localhost:12345/api/mobile/technician/login',
            {phone: username, password: password});
    }
}
