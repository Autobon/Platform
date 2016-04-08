import {Injector} from 'ngES6';
import moment from 'moment';
import $ from 'jquery';

export default class TechnicianMapCtrl extends Injector {
    static $inject = ['$scope', 'Settings', 'TechnicianService', '$compile', '$timeout', '$sce', '$state'];
    static $template = require('./map.html');

    constructor(...args) {
        super(...args);
        const {$scope, TechnicianService, Settings} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.Settings = Settings;
        let items = {"result":true,"message":"","error":"","data":{"page":1,"totalElements":2,"totalPages":1,"pageSize":300,"count":2,"list":[{"id":2,"createAt":1459958400000,"lng":"114.287685","lat":"30.639203","province":"湖北省","city":"武汉市","district":"江岸区","street":"软件园中路","streetNumber":"188号","technician":{"id":2,"phone":"18812345670","name":"tom2","gender":null,"avatar":"http://bbs.ruideppt.com/data/attachment/forum/201104/11/12091175gjmmzmi992nk8y.jpg","idNo":"422302198608266313","idPhoto":"/etc/a.jpg","bank":"工商银行","bankAddress":"光谷","bankCardNo":"88888888888","verifyAt":null,"requestVerifyAt":null,"verifyMsg":null,"lastLoginAt":1456195103000,"lastLoginIp":"127.0.0.1","createAt":1455724800000,"skill":"1","pushId":null,"status":"NEWLY_CREATED"}},{"id":1,"createAt":1460044800000,"lng":"114.37306","lat":"30.556143","province":"湖北省","city":"武汉市","district":"洪山区","street":null,"streetNumber":null,"technician":{"id":1,"phone":"18812345678","name":"tom","gender":null,"avatar":"\nhttp://bbs.ruideppt.com/data/attachment/forum/201104/11/12091175gjmmzmi992nk8y.jpg","idNo":"422302198608266313","idPhoto":"/etc/a.jpg","bank":"工商银行","bankAddress":"光谷","bankCardNo":"88888888888","verifyAt":null,"requestVerifyAt":null,"verifyMsg":null,"lastLoginAt":1456195103000,"lastLoginIp":"127.0.0.1","createAt":1455724800000,"skill":"1","pushId":null,"status":"NEWLY_CREATED"}}]}}.data.list;
        items.forEach(i => {
            i.label = i.technician.name;
        });
        $scope.items = items;
        // TechnicianService.mapLocations().then(res => {
        //     if (res.data && res.data.result) {
        //         let items = res.data.data.list;
        //         items.forEach(i => {
        //             i.label = i.technician.name;
        //         });
        //         $scope.items = items;
        //     }
        // });
    }

    onItemClick(scope, evt) {
        const {$compile, $timeout, $sce, $state} = this.$injected;
        let element = $(evt.target);
        if (!element.hasClass('mv-marker')) {
            $timeout(() => {
                element.closest('.mv-marker').click();
            });
            return;
        }
        if (element.data('has-popover')) return;

        scope.popoverHtml = $sce.trustAsHtml(`
            <div class="clearfix">
                <a href="${$state.href('console.technician.detail', {id: scope.data.technician.id})}">
                    <img src="${scope.data.technician.avatar}" class="img-thumbnail pull-left m-r-10 m-b-10" style="width: 60px; height: 60px;">
                    <h3>${scope.data.label}<small class="btn btn-success btn-xs m-l-5">${this.$injected.Settings.technicianStatus[scope.data.technician.status]}</small></h3>
                </a>
            </div>
            <ul class="list-group" style="min-width: 250px; max-width: 400px;">
                <li class="list-group-item"><b>电话:</b> ${scope.data.technician.phone}</li>
                <li class="list-group-item"><b>定位时间:</b> ${moment(scope.data.createAt).format('YYYY-MM-DD HH:mm')}</li>
                <li class="list-group-item"><b>位置:</b> ${[scope.data.city, scope.data.district, scope.data.street, scope.data.streetNumber].join('')}</li>
            </ul>`);
        element.attr('uib-popover-html', 'popoverHtml');
        element.attr('popover-append-to-body', false);
        element.attr('popover-trigger', 'outsideClick');
        element.attr('popover-placement', 'right');
        element.attr('popover-is-open', true);
        $compile(element)(scope);
        element.data('has-popover', true);
    }
}
