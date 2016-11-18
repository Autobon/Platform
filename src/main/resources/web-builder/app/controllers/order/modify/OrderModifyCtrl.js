import {Injector} from 'ngES6';
import './modify.scss';
export default class OrderDetailCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'OrderService', 'Settings'];
    static $template = require('./modify.html');


}
