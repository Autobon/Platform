export default function routeConfig($locationProvider, $urlRouterProvider, $stateProvider, templateProvider) {
    $locationProvider.html5Mode(true);
    const templateCache = templateProvider.$get();

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('home', {
            url       : '/',
            controller: 'LoginCtrl',
            template  : templateCache.LoginCtrl,
        })
        .state('console', {
            url       : '/console',
            controller: 'ConsoleCtrl',
            template  : templateCache.ConsoleCtrl,
        })
        .state('console.home', {
            url       : '/home',
            controller: 'HomeCtrl',
            template  : templateCache.HomeCtrl,
        })
        .state('console.order', {
            url       : '/order',
            controller: 'OrderCtrl',
            template  : templateCache.OrderCtrl,
        })
        .state('console.order.detail', {
            url       : '/{orderNum:\\d+[^/]*}',
            controller: 'OrderDetailCtrl',
            template  : templateCache.OrderDetailCtrl,
        })
        .state('console.order.edit', {
            url       : '/edit/{orderNum:\\d+[^/]*}',
            controller: 'OrderEditorCtrl',
            template  : templateCache.OrderEditorCtrl,
        })
        .state('console.order.new', {
            url       : '/new',
            controller: 'OrderEditorCtrl',
            template  : templateCache.OrderEditorCtrl,
        })
        .state('console.technician', {
            url       : '/technician',
            controller: 'TechnicianCtrl',
            template  : templateCache.TechnicianCtrl,
        })
        .state('console.technician.detail', {
            url       : '/{id:\\d+}',
            controller: 'TechnicianDetailCtrl',
            template  : templateCache.TechnicianDetailCtrl,
        })
        .state('console.main', {
            url       : '/main',
            controller: 'Main',
            template  : templateCache.Main,
        })
        .state('console.test', {
            url     : '/test',
            template: 'ABDDDD',
        });
}

routeConfig.$inject = ['$locationProvider', '$urlRouterProvider', '$stateProvider', 'templateProvider'];
