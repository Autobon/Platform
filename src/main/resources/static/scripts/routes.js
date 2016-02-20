define([], function()
{
    return {
        defaultRoutePath: '/web/notFound',
        routes: {
            '/': {
                templateUrl: '/view/main.html',
                dependencies: [
                    'controller/mainCtrl'
                ]
            },
            '/web/main': {
                templateUrl: '/view/main.html',
                dependencies: [
                    'controller/mainCtrl'
                ]
            },
            '/web/main/reLogin': {
                templateUrl: '/view/reLogin.html',
                dependencies: [
                    'controller/reLoginCtrl'
                ]
            }，
            '/web/notFound': {
                templateUrl: '/view/notFound.html',
                 dependencies: [
                     'controller/notFoundCtrl'
                 ]
             }
        }
    };
});