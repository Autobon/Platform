export default function routeConfig($locationProvider, $urlRouterProvider, $stateProvider, templateProvider) {
    const templateCache = templateProvider.$get();

    $locationProvider.html5Mode(true);
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
        .state('console.cooperator', {
            url       : '/cooperator',
            controller: 'CooperatorCtrl',
            template  : templateCache.CooperatorCtrl,
        })
        .state('console.cooperator.detail', {
            url       : '/{id:\\d+}',
            controller: 'CooperatorDetailCtrl',
            template  : templateCache.CooperatorDetailCtrl,
        })
        .state('console.bill', {
            url       : '/bill',
            controller: 'BillCtrl',
            template  : templateCache.BillCtrl,
        })
        .state('console.bill.order', {
            url       : '/{id:\\d+}/order',
            controller: 'BillOrderCtrl',
            template  : templateCache.BillOrderCtrl,
        })
        .state('console.map', {
            url: '/map',
            abstract: true,
            template: '<div ui-view style="display: table-cell;"></div>',
        })
        .state('console.map.technician', {
            url       : '/technician',
            controller: 'TechnicianMapCtrl',
            template  : templateCache.TechnicianMapCtrl,
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
