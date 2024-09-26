import {Injector} from 'ngES6';

export default class TeamService extends Injector {
    static $inject = ['$http', 'Settings'];


    search(params, page, pageSize) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/team', {params: {...params, page: page, pageSize: pageSize}});
    }

    getDetail(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/team/' + id);
    }

    add(team) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/team', team);
    }

    update(id, team) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/team/' + id, team);
    }

    deleteTeam(id) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/admin/team/' + id);
    }

    getMembers(id) {
        const {$http, Settings} = this.$injected;
        return $http.get(Settings.domain + '/api/web/admin/team/' + id + '/member');
    }

    addMember(id, techId) {
        const {$http, Settings} = this.$injected;
        return $http.post(Settings.domain + '/api/web/admin/team/' + id + '/member/' + techId);
    }

    deleteMember(id, techId) {
        const {$http, Settings} = this.$injected;
        return $http.delete(Settings.domain + '/api/web/admin/team/' + id + '/member/' + techId);
    }
}
