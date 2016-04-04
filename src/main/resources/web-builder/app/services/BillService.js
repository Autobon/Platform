import {Injector} from 'ngES6';

export default class BillService extends Injector {
    static $inject = ['$http', 'Settings'];

    constructor(...args) {
        super(...args);
    }

    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/bill', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/bill/' + id);
    }

    payoff(id) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/bill/' + id + '/payoff');
    }

    getOrders(id, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/bill/' + id + '/order', {params: {page: page, pageSize: pageSize}});
    }

    generate() {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/bill/generate');
    }

    clear() {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/bill/clear');
    }
}
