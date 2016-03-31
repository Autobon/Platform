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
}
