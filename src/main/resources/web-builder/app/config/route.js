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
            url       : '/detail/{id:\\d+}',
            controller: 'OrderDetailCtrl',
            template  : templateCache.OrderDetailCtrl,
        })
        .state('console.order.edit', {
            url       : '/edit/{id:\\d+[^/]*}',
            controller: 'OrderEditorCtrl',
            template  : templateCache.OrderEditorCtrl,
        })
        .state('console.order.new', {
            url       : '/new',
            controller: 'OrderEditCtrl',
            template  : templateCache.OrderEditCtrl,
        })
        .state('console.order.makeup', {
            url       : '/makeup/{id:\\d+[^/]*}',
            controller: 'OrderMakeupCtrl',
            template  : templateCache.OrderMakeupCtrl,
        })
        .state('console.order.modify', {
            url       : '/modify/{id:\\d+}',
            controller: 'OrderModifyCtrl',
            template  : templateCache.OrderModifyCtrl,
        })
        .state('console.order.dispatch', {
            url       : '/dispatch/{id:\\d+}',
            controller: 'OrderDispatchCtrl',
            template  : templateCache.OrderDispatchCtrl,
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
        .state('console.finance', {
            url       : '/finance',
            controller: 'TechnicianFinanceCtrl',
            template  : templateCache.TechnicianFinanceCtrl,
        })
        .state('console.finance.workDetail', {
            url       : '/workDetail/{id:\\d+}',
            controller: 'WorkDetailCtrl',
            template  : templateCache.WorkDetailCtrl,
        })
        .state('console.finance.applyRecord', {
            url       : '/applyRecord/{id:\\d+}',
            controller: 'ApplyRecordCtrl',
            template  : templateCache.ApplyRecordCtrl,
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
        .state('console.study', {
            url       : '/study',
            controller: 'StudyCtrl',
            template  : templateCache.StudyCtrl,
        })
        .state('console.study.new', {
            url       : '/new',
            controller: 'StudyEditCtrl',
            template  : templateCache.StudyEditCtrl,
        })
        .state('console.study.edit', {
            url       : '/edit/{id:\\d+}',
            controller: 'StudyEditCtrl',
            template  : templateCache.StudyEditCtrl,
        })
        .state('console.study.detail', {
            url       : '/{id:\\d+}',
            controller: 'StudyDetailCtrl',
            template  : templateCache.StudyDetailCtrl,
        })
        .state('console.cooperator.account', {
            url       : '/account/{id:\\d+}',
            controller: 'AccountCtrl',
            template  : templateCache.AccountCtrl,
        })
        .state('console.role', {
            url       : '/role',
            controller: 'RoleCtrl',
            template  : templateCache.RoleCtrl,
        })
        .state('console.role.new', {
            url       : '/new',
            controller: 'EditorRoleCtrl',
            template  : templateCache.EditorRoleCtrl,
        })
        .state('console.role.edit', {
            url       : '/edit/{id:\\d+}',
            controller: 'EditorRoleCtrl',
            template  : templateCache.EditorRoleCtrl,
        })
        .state('console.staff', {
            url       : '/staff',
            controller: 'StaffCtrl',
            template  : templateCache.StaffCtrl,
        })
        .state('console.staff.new', {
            url       : '/new',
            controller: 'EditorStaffCtrl',
            template  : templateCache.EditorStaffCtrl,
        })
        .state('console.staff.edit', {
            url       : '/edit/{id:\\d+}',
            controller: 'EditorStaffCtrl',
            template  : templateCache.EditorStaffCtrl,
        })
        .state('console.team', {
            url       : '/team',
            controller: 'TeamCtrl',
            template  : templateCache.TeamCtrl,
        })
        .state('console.team.new', {
            url       : '/new',
            controller: 'TeamEditorCtrl',
            template  : templateCache.TeamEditorCtrl,
        })
        .state('console.team.edit', {
            url       : '/edit/{id:\\d+}',
            controller: 'TeamEditorCtrl',
            template  : templateCache.TeamEditorCtrl,
        })
        .state('console.team.member', {
            url       : '/team/{id:\\d+}/member',
            controller: 'MemberCtrl',
            template  : templateCache.MemberCtrl,
        })
        ;
}

routeConfig.$inject = ['$locationProvider', '$urlRouterProvider', '$stateProvider', 'templateProvider'];
