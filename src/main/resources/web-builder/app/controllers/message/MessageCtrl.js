import {Injector} from 'ngES6';

export default class MessageCtrl extends Injector {
    static $inject   = ['$scope', 'MessageService', '$uibModal'];
    static $template = require('./message.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.filter     = {};
        $scope.pagination = {page: 1, pageSize: 15, totalItems: 0};
        $scope.$on('form.message.updated', this.onUpdated);
        $scope.$on('form.message.added', this.onAdded);
        this.getMessages();
    }

    getMessages() {
        const {$scope, MessageService} = this.$injected;
        const {page, pageSize} = this.pagination;
        MessageService.search($scope.filter, page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.messages = res.data.data.list;
                $scope.pagination.totalItems = res.data.data.totalElements;
            }
        });
    }

    publish(message) {
        this.$injected.MessageService.publish(message.id).then(res => {
            if (res.data && res.data.result) {
                message.status = 1;
            }
        });
    };

    reset() {
        const {$scope} = this.$injected;
        $scope.filter = {};
        $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    onAdd() {

    }

    onEdit(message) {

    }

    onAdded(event, message) {
        event.stopPropagation();
        const {$scope} = this.$injected;
        $scope.messages.unshift(message);
    }

    onUpdated(event, message) {
        event.stopPropagation();
        const {$scope} = this.$injected;
        $scope.messages.forEach(m => {
            if (m.id == message.id) {
                angular.extend(m, message);
            }
        });
    }
}
