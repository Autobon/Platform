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
                    totalStatus: {
                        '1': '1个',
                        '2': '2个',
                        '3': '3个',
                        '4': '4个',
                        '5': '5个',
                        '6': '6个',
                        '7': '7个',
                        '8': '8个',
                    },
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
                    positionTypes: {
                        '1': '前风挡',
                        '2': '左前门',
                        '3': '右前门',
                        '4': '左后门+角',
                        '5': '右后门+角',
                        '6': '后风挡',
                        '7': '前保险杠',
                        '8': '引擎盖',
                        '9': '左右前叶子板',
                        '10': '四门',
                        '11': '左右后叶子板',
                        '12': '尾盖',
                        '13': '后保险杠',
                        '14': 'ABC柱套件',
                        '15': '车顶',
                        '16': '门拉手',
                        '17': '反光镜',
                        '18': '整车',
                    },
                    technicianStatus: {
                        'NEWLY_CREATED': '新注册',
                        'IN_VERIFICATION': '认证中',
                        'VERIFIED': '认证通过',
                        'REJECTED': '认证失败',
                        'BANNED': '帐户禁用',
                    },
                    yearStatus: {
                        '0': '无',
                        '1': '1年',
                        '2': '2年',
                        '3': '3年',
                        '4': '4年',
                        '5': '5年',
                        '6': '6年',
                        '7': '7年',
                        '8': '8年',
                        '9': '9年',
                        '10': '10年',
                    },
                    starStatus: {
                        '0': '无',
                        '1': '1星',
                        '2': '2星',
                        '3': '3星',
                        '4': '4星',
                        '5': '5星',
                    },
                    technicianType: {
                        '1': '可接单',
                        '2': '工作中',
                        '3': '休息中',
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
