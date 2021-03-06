import 'babel-polyfill';
import angular from 'angular';
import 'angular-animate';
import 'angular-ui-router';
import 'angular-ui-bootstrap';
import 'angular-chart.js';
import moment from 'moment';

import 'bootstrap/dist/css/bootstrap.css';
import 'font-awesome/css/font-awesome.min.css';
import 'angular-bootstrap-datetimepicker';
import 'angular-bootstrap-datetimepicker/src/js/datetimepicker.templates.js';
import 'angular-bootstrap-datetimepicker/src/css/datetimepicker.css';
import 'angular-chart.js/dist/angular-chart.css';
import './styles/common.scss';

import directives from './directives';
import controllers, {templateCache} from './controllers';
import services from './services';
import filters from './filters';
import config from './config';

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
                    orderTypes: {'1': '隔热膜', '2': '隐形车衣', '3': '车身改色', '4': '美容清洁'},
                    orderStatus: {
                        'CREATED_TO_APPOINT': '新建待指定技师',
                        'NEWLY_CREATED': '已推送未接单',
                        'TAKEN_UP': '已接单',
                        'SEND_INVITATION': '邀请合作',
                        'INVITATION_ACCEPTED': '邀请已接受',
                        'INVITATION_REJECTED': '邀请已拒绝',
                        'IN_PROGRESS': '施工中',
                        'SIGNED_IN': '已签到',
                        'FINISHED': '施工完成',
                        'COMMENTED': '已评论',
                        'GIVEN_UP': '已放弃',
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

config.forEach(c => app.config(c));

window.name = 'NG_DEFER_BOOTSTRAP!';
angular.element().ready(() => {
    angular.resumeBootstrap([App]);
});

export default app;
