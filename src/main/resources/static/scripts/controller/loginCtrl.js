
define(['../autobon','jquery','md5'],function(module,$){
    module.controller("loginCtrl",function($scope, $resource, $http, $location,commonService){
          var loginCookie = commonService.getCookie('token_staff');//获取token
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
           postData.userName = $scope.loginInfo.username;
           postData.password = hex_md5($scope.loginInfo.password);
             loginApi.save(postData,function(data){
                 if(data.result){
                     window.location.href = "/web/main";
                 }else{
                     alert(data.message);
                 }
             },function(err){
                 console.log(err.data.error);
             });
          };



        //点击Enter键登录
        $scope.loginEnter = function(e){
            if(e.keyCode === 13) $scope.login();
        }
    });
});
