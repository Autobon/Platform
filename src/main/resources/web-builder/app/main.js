import angular from 'angular';
import 'angular-route';
import 'angular-animate';
import 'angular-ui-router';
import 'angular-ui-bootstrap';
import 'jquery';

import directives from './directives';
import controllers, {templateCache} from './controllers';
import services from './services';
import config from './config';

export const App = 'app';

const app = angular.module(App, ['ngRoute', 'ngAnimate', 'ui.router', 'ui.bootstrap',
                directives, services, controllers])
            .provider('template', function() {
                this.$get = () => {return templateCache;};
            })
    .value('Settings', {
        domain: 'http://localhost:12345',
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
