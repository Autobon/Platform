export default function routeConfig(
    $locationProvider, $urlRouterProvider, $stateProvider, templateProvider) {
    $locationProvider.html5Mode(true);
    const templateCache = templateProvider.$get();

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('home', {
            url: '/',
            controller: 'LoginCtrl',
            template: templateCache.LoginCtrl,
        })
        .state('console', {
            url: '/console',
            controller: 'ConsoleCtrl',
            template: templateCache.ConsoleCtrl,
        })
        .state('main', {
            url: '/main',
            controller: 'Main',
            template  : templateCache.Main,
        })
        .state('test', {
            url: '/test',
            template: 'ABDDDD',
        });
}

routeConfig.$inject = ['$locationProvider', '$urlRouterProvider', '$stateProvider', 'templateProvider'];
