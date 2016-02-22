
define(['../autobon','jquery','md5'],function(module,$){
    module.controller("loginCtrl",function($scope, $resource, $http, $location,commonService){
          var loginCookie = commonService.getCookie('token');//获取token
          if (loginCookie != "") {
              window.location.href="/web/main";
          }

          //API define
          var loginApi = $resource("/api/staff/login");

          //变量 define
          $scope.loginInfo = {};
          var postData = {};

           //登录
           $scope.login = function(){
           postData.username = $scope.loginInfo.username
           postData.password = hex_md5($scope.loginInfo.password);
             loginApi.save(postData,function(data){

             },function(err){
                 console.log(errData.data.error);
             });
          }
    });
});
