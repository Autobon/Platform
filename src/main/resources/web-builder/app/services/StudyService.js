import {Injector} from 'ngES6';

export default class StudyService extends Injector {
    static $inject = ['$http', 'Settings'];

    get uploadPhotoUrl() {
        return '/api/web/admin/study/upload';
    }

    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/study', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/study/' + id);
    }

    // verify(id, verified, verifyMsg) {
    //     const {$http, Settings} = this.$injected;
    //     return $http.post(Settings.domain + '/api/web/admin/cooperator/verify/' + id, {verified: verified, remark: verifyMsg});
    // }
    //
    // mapLocations(province, city, page, pageSize) {
    //     const {$http, Settings} = this.$injected;
    //     return $http.get(Settings.domain + '/api/web/admin/cooperator/mapview', {params: {province: province, city: city, page: page, pageSize: pageSize}});
    // }

    add(study) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/study', study);
    }

    update(study) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/study/' + study.id, study);
    }

    download() {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/study/upload');

    }

    // get uploadFile() {
    //     return '/api/web/admin/study/upload';
    // }
}
