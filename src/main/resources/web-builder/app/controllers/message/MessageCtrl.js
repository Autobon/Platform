import {Injector} from 'ngES6';
import angular from 'angular';
import MessageEditorCtrl from './editor/MessageEditorCtrl.js';

export default class MessageCtrl extends Injector {
    static $inject   = ['$scope', 'MessageService', '$uibModal'];
    static $template = require('./message.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.filter     = {};
        $scope.pagination = {page: 1, pageSize: 15, totalItems: 0};
        $scope.messages   = [];
        $scope.$on('form.message.updated', (e, msg) => {
            this.onUpdated(e, msg);
        });
        $scope.$on('form.message.added', (e, msg) => {
            this.onAdded(e, msg);
        });
        this.getMessages();
    }

    getMessages() {
        const {$scope, MessageService} = this.$injected;
        const {page, pageSize} = $scope.pagination;
        MessageService.search($scope.filter, page, pageSize).then(res => {
            if (res.data && res.data.result) {
                $scope.messages              = res.data.data.list;
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
    }

    reset() {
        const {$scope} = this.$injected;
        $scope.filter     = {};
        $scope.pagination = {...$scope.pagination, page: 1, totalItems: 0};
    }

    onAdd() {
        const {$scope} = this.$injected;
        $scope.message = {};
        this.openModal();
    }

    onEdit(message) {
        const {$scope} = this.$injected;
        $scope.message = message;
        this.openModal();
    }

    openModal() {
        const {$scope, $uibModal} = this.$injected;
        $scope.modal = $uibModal.open({
            size      : 'lg',
            scope     : $scope,
            template  : MessageEditorCtrl.$template,
            controller: MessageEditorCtrl,
        });
    }

    onAdded(event, message) {
        event.stopPropagation();
        const {$scope} = this.$injected;
        $scope.messages.unshift(message);
        $scope.modal.close();
    }

    onUpdated(event, message) {
        event.stopPropagation();
        const {$scope} = this.$injected;
        $scope.messages.forEach(m => {
            if (m.id === message.id) {
                angular.extend(m, message);
            }
        });
        $scope.modal.close();
    }
}
