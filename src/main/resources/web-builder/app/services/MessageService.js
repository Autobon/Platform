import {Injector} from 'ngES6';

export default class MessageService extends Injector {
    static $inject = ['$http', 'Settings'];

    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/message', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(msgId) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/message/' + msgId);
    }

    add(msg) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/message', msg);
    }

    update(msg) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/message/' + msg.id, msg);
    }

    delete(msgId) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/admin/message/' + msgId);
    }

    publish(msgId) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/message/publish/' + msgId);
    }
}
