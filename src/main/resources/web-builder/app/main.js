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
import 'angularjs-dropdown-multiselect';

import directives from './directives';
import controllers, {templateCache} from './controllers';
import services from './services';
import filters from './filters';
import config from './config';

moment.locale('zh-cn');
export const App = 'app';

const app = angular.module(App, ['ngAnimate', 'ui.router', 'ui.bootstrap', 'treeControl', 'ui.bootstrap.carousel',
                'chart.js', 'ui.bootstrap.datetimepicker', 'angularjs-dropdown-multiselect',
                directives, services, controllers, filters])
            .provider('template', function() {
                this.$get = () => {return templateCache;};
            })
            .service('Settings', ['$location', function($location) {
                return {
                    domain: $location.protocol() + '://' + $location.host() + ':' + $location.port(),
                    baiduMapKey: 'FPzmlgz02SERkbPsRyGOiGfj',
                    orderTypes: {'1': '隔热膜', '2': '隐形车衣', '3': '车身改色', '4': '美容清洁'},
                    headTypes: {'1': '主负责人', '0': '次负责人'},
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
                        'IN_PROGRESS': '技师出发',
                        'SIGNED_IN': '已签到',
                        'AT_WORK': '工作中',
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
                        '4': '左后门',
                        '5': '右后门',
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
                        '19': '大天窗',
                        '20': '中天窗',
                        '21': '小天窗',
                        '22': '左小角',
                        '23': '右小角',
                        '24': '左大角',
                        '25': '右大角',
                        '26': '左裙边',
                        '27': '右裙边',
                        '28': '整车不含顶',
                        '29': '前杠',
                        '30': '后杠',
                        '31': '左前叶子板',
                        '32': '右前叶子板',
                        '33': '左后叶子板',
                        '34': '右后叶子板',
                        '35': '左后视镜',
                        '36': '右后视镜',
                        '37': '手扣',
                        '38': '中控台',
                        '39': '四门脚踏',
                        '40': '四门饰条',
                        '41': '两前座椅',
                        '42': '两后座椅',
                        '43': '喜悦套餐',
                        '44': '前杠角',
                        '45': '后杠角',
                        '46': '四轮眉',
                        '47': '新车漆面处理',
                        '48': '新车整备',
                        '49': '二手车全车整备翻新',
                        '50': '完美交车',
                        '51': '镀膜/镀晶（新车）',
                        '52': '镀膜/镀晶（旧车）',
                        '53': '镀膜/镀晶（维护）',
                        '54': '打蜡封釉',
                        '55': '抛光（旧车）',
                        '56': '轮毂镀膜/镀晶',
                        '57': '轮毂清洗翻新',
                        '58': '外观镀铬件翻新',
                        '59': '外观镀铬件养护',
                        '60': '门板镀膜',
                        '61': '玻璃镀膜',
                        '62': '真皮镀膜',
                        '63': '真皮清洁',
                        '64': '氧触媒套装',
                        '65': '内饰消毒去味',
                        '66': '内饰清洗',
                        '67': '内部聚乙烯养护',
                        '68': '底盘装甲',
                        '69': '发动机排气管/皮带维护',
                        '70': '发动机舱线路保护',
                        '71': '发动机表面清洁',
                        '72': '天窗护理',
                        '73': '滤芯更换',
                        '74': '行车记录仪安装',
                        '75': '大灯翻新',
                        '76': '全车撕改色膜/车衣',
                        '77': '后视镜犀牛皮',
                        '78': '四门手扣犀牛皮',
                        '79': '整车车身',
                        '80': '前天窗',
                        '81': '中天窗',
                        '82': '后天窗',
                        '83': '新车封釉/镀膜/镀晶',
                        '84': '新车揭膜除胶或打蜡',
                        '85': '新车精洗整备（除胶除锈）',
                        '86': '杀菌/去味套餐',
                        '87': '二手车全车整备翻新',
                        '88': '旧车封釉/镀膜/镀晶',
                        '89': '内饰清洗/翻新',
                        '90': '发动机舱清洁养护',
                        '91': '轮毂翻新/镀膜',
                        '92': '玻璃清洁/镀膜',
                        '93': '真皮清洁/镀膜',
                        '94': '镀膜/镀晶维护',
                        '95': '后视镜犀牛皮',
                        '96': '四门手扣犀牛皮',
                        '97': '底盘装甲',
                        '98': '滤芯更换',
                        '99': '零售店镀晶（含机舱轮毂轮胎内饰整备）',
                        '100': '零售店极光养护（含机舱轮毂轮胎内饰整备）',
                        '101': '零售店内饰翻新（含机舱轮毂轮胎漆面打蜡）',
                        '102': '零售店真皮清洗镀膜（含机舱轮毂轮胎漆面打蜡）',
                        '103': '零售店撕车衣/改色膜',
                        '104': '零售店行车记录仪安装',
                        '105': '零售店裁脚垫',
                        '106': '左机盖',
                        '107': '右机盖',
                        '108': '左前杠',
                        '109': '右前杠',
                        '110': '左后杠',
                        '111': '右后杠',
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
                    productStatus: {
                        '0': '未补录',
                        '1': '已补录',
                    },
                    reassignmentStatus: {
                        '0': '未申请改派',
                        '1': '已申请改派',
                        '2': '已处理',
                    },
                    studyTypes: {
                        '1': '培训资料',
                        '2': '施工标准',
                        '3': '业务规则',
                    },
                    payStatus: {
                        '0': '未出账',
                        '1': '已出账',
                        '2': '已转账支付',
                    },
                    applyStatus: {
                        '0': '已申请',
                        '1': '部分扣款',
                        '2': '已扣款',
                        '3': '已被取消',
                    },
                    merchandiserStatus: {
                        'VERIFIED': '可用',
                        'BANNED': '禁用',
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
