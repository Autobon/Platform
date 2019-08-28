import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';
export default class OrderIssueCtrl extends Injector{
    static $template = require('./issue.html');
    static $inject   = ['$scope','$state',  'orderService'];


    save(){
        const {$scope,$state, orderService} = this.$injected;
        orderService.createOrder().then(res=>{
            console.log(res)
        })
    }
}
