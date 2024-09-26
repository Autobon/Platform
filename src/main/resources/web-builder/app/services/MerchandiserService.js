import {Injector} from 'ngES6';

export default class MerchandiserService extends Injector {
    static $inject = ['$http', 'Settings'];


    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/merchandiser/list', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/merchandiser/' + id);
    }

    add(team) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/merchandiser/add', team);
    }

    update(team) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/merchandiser/modify', team);
    }

    deleteMerchandiser(id) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/merchandiser/delete/' + id);
    }

    banMerchandiser(id) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/merchandiser/ban/' + id);
    }

    pickMerchandiser(id) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/merchandiser/unban/' + id);
    }

    getMerchandisers(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/merchandiser/' + id + '/cooperator/list');
    }

    coopAddMerchandiser(mid, cid) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/merchandiser/' + mid + '/cooperator/' + cid);
    }

    coopDeleteMerchandiser(mid, cid) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/merchandiser/' + mid + '/cooperator/' + cid);
    }
}
