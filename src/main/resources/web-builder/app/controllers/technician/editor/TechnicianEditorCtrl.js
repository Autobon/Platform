import {Injector} from 'ngES6';
import './index.scss';

export default class TechnicianEditorCtrl extends Injector {
    static $inject = ['$scope', '$state', '$stateParams', 'Settings', 'TechnicianService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, TechnicianService, Settings} = this.$injected;
        $scope.Settings = Settings;
        this.attachMethodsTo($scope);

        TechnicianService.getV2Detail($stateParams.id).then(res => {
            if (res.data.status === true) {
                $scope.technician = res.data.message;
                console.log(JSON.stringify($scope.technician));
            }
        });
    }
}
