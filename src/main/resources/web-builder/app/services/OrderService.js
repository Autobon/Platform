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
    getDetail2(orderId) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/v2/order/v2/' + orderId);
    }
    getAllPosition(orderId) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/v2/order/project/position/' + orderId);
    }
    assign(orderId, techId) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/order/assign', {orderId: orderId, techId: techId});
    }

    add(order) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/order', order);
    }
    update(orderShow) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/v2/order/v2/' + orderShow.id, orderShow);
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

    async assembleWorkItemsText(workItems, workPercent, orderType) {
        if (workPercent) return (workPercent * 100).toFixed(0) + '%';
        if (!workItems) return '';
        if (!this.workItems || !this.workItems[orderType]) {
            let res = await this.getWorkItems(orderType);
            if (res.data && res.data.result) {
                this.workItems = this.workItems || {};
                this.workItems[orderType] = res.data.data;
                return this._assembleWorkItemsText(this.workItems[orderType], workItems);
            }
        } else {
            return this._assembleWorkItemsText(this.workItems[orderType], workItems);
        }
    }

    _assembleWorkItemsText(dictionary, items) {
        let map = {};
        dictionary.forEach(d => map[d.id] = d.name);
        return items.split(',').map(i => map[i]).join(',');
    }

}
