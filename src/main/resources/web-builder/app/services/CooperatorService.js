import {Injector} from 'ngES6';

export default class CooperatorService extends Injector {
    static $inject = ['$http', 'Settings'];

    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/cooperator', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/cooperator/' + id);
    }

    verify(id, verified, verifyMsg) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/cooperator/verify/' + id, {verified: verified, verifyMsg: verifyMsg});
    }
}
