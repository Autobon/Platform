import {Injector} from 'ngES6';

export default class TechnicianService extends Injector {
    static $inject = ['$http', 'Settings'];

    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/technician', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/technician/' + id);
    }

    verify(id, verified, verifyMsg) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/technician/verify/' + id, {verified: verified, verifyMsg: verifyMsg});
    }
}
