import {Injector} from 'ngES6';

export default class AccountService extends Injector {
    static $inject = ['$http', 'Settings'];

    changePassword(oldPassword, newPassword) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/changePassword', {oldPassword: oldPassword, newPassword: newPassword});
    }

    logout() {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/logout');
    }

    searchStaff(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/staff', {params: {...params, page: page, pageSize: pageSize}});
    }

    getStaff(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/staff/' + id);
    }

    getStaffMenu(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/staff/' + id + '/menu');
    }

    addStaff(rid, username, email, name, phone, password) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/staff/' + rid + '/add', {username: username, email: email, name: name, phone: phone, password: password});
    }

    updateStaff(rid, id, username, email, name, phone) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/staff/' + rid + '/add/' + id, {username: username, email: email, name: name, phone: phone});
    }

    deleteStaff(id) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/admin/staff/' + id);
    }

    searchRole(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/role', {params: {...params, page: page, pageSize: pageSize}});
    }

    getRole(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/role/' + id);
    }

    addRole(name, remark, functionCategoryIds, coopIds, menuIds) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/role', {name: name, functionCategoryIds: functionCategoryIds, coopIds: coopIds, menuIds: menuIds, remark: remark});
    }

    updateRole(id, name, remark, functionCategoryIds, coopIds, menuIds) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/role/' + id, {name: name, functionCategoryIds: functionCategoryIds, coopIds: coopIds, menuIds: menuIds, remark: remark});
    }

    deleteRole(id) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/admin/role/' + id);
    }

    searchMenu() {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/menu');
    }

    getRoleMenu(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/role/' + id + '/menu');
    }

    getAllMenus() {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/function/category/menu');
    }

    getStaffRole() {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/staff/role');
    }
}
