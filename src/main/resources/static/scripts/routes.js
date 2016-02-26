define([], function () {
    return {
        defaultRoutePath: '/web/notFound',
        routes: {
            '/web/main': {
                templateUrl: '/view/main.html',
                dependencies: [
                    'controller/mainCtrl'
                ]
            },
            '/web/notFound': {
                templateUrl: '/view/notFound.html',
                dependencies: [
                    'controller/notFoundCtrl'
                ]
            },
            '/web/order': {
                templateUrl: '/view/order.html',
                dependencies: [
                    'controller/orderCtrl'
                ]
            }
        }
    };
});