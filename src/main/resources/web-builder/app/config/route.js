export default function routeConfig($locationProvider, $routeProvider, templateProvider) {
    $locationProvider.html5Mode(true);
    const templateCache = templateProvider.$get();

    $routeProvider
        .when('/', {
            template  : templateCache.LoginCtrl,
            controller: 'LoginCtrl',
        })
        .when('/console', {
            template  : templateCache.ConsoleCtrl,
            controller: 'ConsoleCtrl',
        })
        .when('/main', {
            template  : templateCache.Main,
            controller: 'Main',
        })
        .when('/test', {
            template: 'ABDDDD',
        })
        .otherwise({redirectTo: '/'});
}

routeConfig.$inject = ['$locationProvider', '$routeProvider', 'templateProvider'];
