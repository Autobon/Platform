import {Injector} from 'ngES6';
import './editor.scss';

export default class OrderEditorCtrl extends Injector {
    static $inject   = ['$scope', 'Settings'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.order = {};
        $scope.uploadUrl = '/api/web/admin/order/photo';
    }

    uploaded(data) {
        if (data.result) {
            return data.data;
        } else {
            alert(data.message);
            return '';
        }
    }
}
