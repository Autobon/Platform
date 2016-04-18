import 'babel-polyfill';
import angular from 'angular';
import 'angular-animate';
import 'angular-ui-router';
import 'angular-ui-bootstrap';
import 'angular-chart.js';
import $ from 'jquery';
import moment from 'moment';
import 'angular-bootstrap-datetimepicker';
import 'angular-bootstrap-datetimepicker/src/js/datetimepicker.templates.js';
import 'angular-bootstrap-datetimepicker/src/css/datetimepicker.css';
import 'angular-chart.js/dist/angular-chart.min.css';

import directives from './directives';
import controllers, {templateCache} from './controllers';
import services from './services';
import filters from './filters';
import config from './config';

window.$ = window.jQuery = $;
moment.locale('zh-cn');
export const App = 'app';

const app = angular.module(App, ['ngAnimate', 'ui.router', 'ui.bootstrap',
                'chart.js', 'ui.bootstrap.datetimepicker',
                directives, services, controllers, filters])
            .provider('template', function() {
                this.$get = () => {return templateCache;};
            })
            .service('Settings', ['$location', function($location) {
                return {
                    domain: $location.protocol() + '://' + $location.host() + ':' + $location.port(),
                    baiduMapKey: 'FPzmlgz02SERkbPsRyGOiGfj',
                    orderTypes: {'1': '隔热层', '2': '隐形车衣', '3': '车身改色', '4': '美容清洁'},
                    orderStatus: {
                        'NEWLY_CREATED': '未接单',
                        'TAKEN_UP': '已接单',
                        'SEND_INVITATION': '邀请合作',
                        'INVITATION_ACCEPTED': '邀请已接受',
                        'INVITATION_REJECTED': '邀请已拒绝',
                        'IN_PROGRESS': '施工中',
                        'FINISHED': '施工完成',
                        'COMMENTED': '已评论',
                        'CANCELED': '已取消',
                        'EXPIRED': '已超时',
                    },
                    technicianStatus: {
                        'NEWLY_CREATED': '新注册',
                        'IN_VERIFICATION': '认证中',
                        'VERIFIED': '认证通过',
                        'REJECTED': '认证失败',
                        'BANNED': '帐户禁用',
                    },
                };
            }])
            .run(['$rootScope', '$state', function($rootScope, $state) {
                $rootScope.$state = $state;
            }]);

config.forEach(c => {
    app.config(c);
});

import 'bootstrap/dist/css/bootstrap.css';
import 'font-awesome/css/font-awesome.min.css';
import './styles/common.scss';
window.name = 'NG_DEFER_BOOTSTRAP!';
angular.element().ready(() => {
    angular.resumeBootstrap([App]);
});

export default app;
