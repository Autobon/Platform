import angular from 'angular';
import 'angular-animate';
import 'angular-ui-router';
import 'angular-ui-bootstrap';
import 'jquery';

import directives from './directives';
import controllers, {templateCache} from './controllers';
import services from './services';
import filters from './filters';
import config from './config';

export const App = 'app';

const app = angular.module(App, ['ngAnimate', 'ui.router', 'ui.bootstrap',
                directives, services, controllers, filters])
            .provider('template', function() {
                this.$get = () => {return templateCache;};
            })
    .value('Settings', {
        domain: 'http://localhost:12345',
        orderTypes: {'1': '隔热层', '2': '隐形车衣', '3': '车身改色', '4': '美容清洁"'},
        orderStatus: {
            'NEWLY_CREATED': '新建',
            'TAKEN_UP': '已接单',
            'SEND_INVITATION': '已发送合作邀请',
            'INVITATION_ACCEPTED': '邀请已接受',
            'INVITATION_REJECTED': '邀请已拒绝',
            'IN_PROGRESS': '施工中',
            'FINISHED': '施工完成',
            'COMMENTED': '已评论',
            'CANCELED': '已取消',
        },
    });

config.forEach(c => {
    app.config(c);
});

import 'bootstrap/dist/css/bootstrap.css';
import './styles/common.scss';
window.name = 'NG_DEFER_BOOTSTRAP!';
angular.element().ready(() => {
    angular.resumeBootstrap([App]);
});

export default app;
