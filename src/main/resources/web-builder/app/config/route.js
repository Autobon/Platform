export default function routeConfig($routeProvider) {
    $routeProvider
        .when('/', {
            template  : require('../controllers/main/main.html'),
            controller: 'Main',
        })
        .when('/test', {
            template: 'ABDDDD',
        })
        .otherwise({redirectTo: '/'});
}

routeConfig.$inject = ['$routeProvider'];
