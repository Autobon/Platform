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
            controller: 'OrderEditCtrl',
            template  : templateCache.OrderEditCtrl,
        })
        .state('console.order.makeup', {
            url       : '/makeup/{orderNum:\\d+[^/]*}',
            controller: 'OrderMakeupCtrl',
            template  : templateCache.OrderMakeupCtrl,
        })
        .state('console.order.modify', {
            url       : '/modify/{id:\\d+}',
            controller: 'OrderModifyCtrl',
            template  : templateCache.OrderModifyCtrl,
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
        .state('console.technician.edit', {
            url       : '/edit/{id:\\d+}',
            controller: 'TechnicianEditorCtrl',
            template  : templateCache.TechnicianEditorCtrl,
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
        .state('console.cooperator.new', {
            url       : '/new',
            controller: 'CooperatorEditCtrl',
            template  : templateCache.CooperatorEditCtrl,
        })
        .state('console.cooperator.edit', {
            url       : '/edit/{id:\\d+}',
            controller: 'CooperatorEditCtrl',
            template  : templateCache.CooperatorEditCtrl,
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
            url     : '/map',
            abstract: true,
            template: '<div ui-view style="display: table-cell; height: 100%;"></div>',
        })
        .state('console.map.technician', {
            url       : '/technician',
            controller: 'TechnicianMapCtrl',
            template  : templateCache.TechnicianMapCtrl,
        })
        .state('console.map.cooperator', {
            url       : '/cooperator',
            controller: 'CooperatorMapCtrl',
            template  : templateCache.CooperatorMapCtrl,
        })
        .state('console.message', {
            url       : '/message',
            controller: 'MessageCtrl',
            template  : templateCache.MessageCtrl,
        })
        .state('console.stat', {
            url       : '/stat',
            controller: 'StatCtrl',
            template  : templateCache.StatCtrl,
        })
        .state('console.account', {
            url       : '/account',
            controller: 'AccountCtrl',
            template  : templateCache.AccountCtrl,
        })
        .state('console.password', {
            url       : '/password',
            controller: 'PasswordCtrl',
            template  : templateCache.PasswordCtrl,
        })
        .state('console.main', {
            url       : '/main',
            controller: 'Main',
            template  : templateCache.Main,
        })
        .state('console.test', {
            url     : '/test',
            template: 'ABDDDD',
        })
        .state('console.product', {
            url       : '/product',
            controller: 'ProductCtrl',
            template  : templateCache.ProductCtrl,
        })
        .state('console.product.detail', {
            url       : '/{id:\\d+}',
            controller: 'ProductDetailCtrl',
            template  : templateCache.ProductDetailCtrl,
        })
        .state('console.product.import', {
            url       : '/import',
            controller: 'ProductImportCtrl',
            template  : templateCache.ProductImportCtrl,
        })
        .state('console.product.new', {
            url       : '/new',
            controller: 'ProductEditCtrl',
            template  : templateCache.ProductEditCtrl,
        })
        .state('console.product.edit', {
            url       : '/edit/{id:\\d+}',
            controller: 'ProductEditCtrl',
            template  : templateCache.ProductEditCtrl,
        })
        ;
}

routeConfig.$inject = ['$locationProvider', '$urlRouterProvider', '$stateProvider', 'templateProvider'];
