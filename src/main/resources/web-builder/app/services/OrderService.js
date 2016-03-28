import {Injector} from 'ngES6';

export default class OrderService extends Injector {
    static $inject = ['$http', 'Settings'];

    get uploadPhotoUrl() {
        return '/api/web/admin/order/photo';
    }

    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/order', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(orderNum) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/order/' + orderNum);
    }

    createOrder(order) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/order', order);
    }

    getWorkItems(orderType) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/pub/technician/workItems',
                {params: {orderType: orderType}});
    }

    addComment(comment) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/order/comment', comment);
    }
}
