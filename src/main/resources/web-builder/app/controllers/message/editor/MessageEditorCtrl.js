import {Injector} from 'ngES6';

export default class MessageEditorCtrl extends Injector {
    static $inject = ['$scope', 'MessageService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
    }

    save() {
        const {$scope, MessageService} = this.$injected;
        if ($scope.message.id) {
            MessageService.update($scope.message).then(res => {
                if (res.data) {
                    if (res.data.result) {
                        $scope.$emit('form.message.updated', $scope.message);
                    } else {
                        $scope.error = res.data.message;
                    }
                }
            });
        } else {
            MessageService.add($scope.message).then(res => {
                if (res.data && res.data.result) {
                    $scope.$emit('form.message.added', res.data.data);
                } else {
                    $scope.error = res.data.message;
                }
            });
        }
    }
}
