import {Injector} from 'ngES6';

export default class TechnicianService extends Injector {
    static $inject = ['$http', 'Settings'];

    get uploadPhotoUrl() {
        return '/api/web/admin/cooperator/photo';
    }

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

    mapLocations(province, city, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/technician/mapview', {province: province, city: city, page: page, pageSize: pageSize});
    }

    mapTrack(techId, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/technician/maptrack/' + techId, {params: {page: page, pageSize: pageSize}});
    }

    getV2Detail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/v2/technician/' + id);
    }

    update(tech) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/v2/technician/' + tech.id, tech);
    }
}
