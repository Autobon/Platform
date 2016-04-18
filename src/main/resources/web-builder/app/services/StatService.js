import {Injector} from 'ngES6';

export default class StatService extends Injector {
    static $inject = ['$http', 'Settings'];

    getStat(start, end, type) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/stat', {params: {start: start, end: end, type: type}});
    }
}
