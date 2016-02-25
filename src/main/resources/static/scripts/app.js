requirejs.config({
    paths:{
        angular:"http://apps.bdimg.com/libs/angular.js/1.3.13/angular",
        jquery:'http://apps.bdimg.com/libs/jquery/2.1.4/jquery',
        angularResource: "http://apps.bdimg.com/libs/angular.js/1.3.13/angular-resource",
        angularSanitize:"http://apps.bdimg.com/libs/angular.js/1.3.13/angular-sanitize",
        angularRoute:"http://apps.bdimg.com/libs/angular-route/1.3.13/angular-route",
        bootstrap:"http://apps.bdimg.com/libs/bootstrap/3.3.4/js/bootstrap",
        ajaxFileUpload:"ajaxfileupload",
        md5:"md5"
    },
    shim:{
        jquery: { exports: '$' },
        angular: { exports: 'angular',deps:['jquery']},
        angularResource: ['angular'],
        angularSanitize:['angular'],
        angularRoute:['angular'],
        bootstrap:['jquery'],
        ajaxFileUpload:['jquery']
    }
});

require
(
    [
        'angular','autobon', './controller/indexCtrl', './controller/loginCtrl','./service/commonService','./filter/commonFilter'
    ],
    function(angular)
    {
        angular.bootstrap(document, ['autobon']);
    }
);

