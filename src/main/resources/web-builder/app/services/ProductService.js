import {Injector} from 'ngES6';

export default class ProductService extends Injector {
    static $inject = ['$http', 'Settings'];

    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/order/product', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/order/product/' + id);
    }

    add(pro) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/order/product', pro);
    }

    update(pro) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/order/product/' + pro.id, pro);
    }

    delete(pro) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/admin/order/product/' + pro.id);
    }

    getOrderProduct(orderId) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/order/' + orderId + '/product');
    }

    saveProduct(orderId, productIds) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/order/' + orderId + '/product', productIds);
    }

    getPositionByProject(projectId) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/order/project/' + projectId + '/position');
    }
}
