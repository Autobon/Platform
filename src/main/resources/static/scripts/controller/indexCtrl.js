
define(['../autobon','jquery','md5'],function(module,$){
    module.controller("indexCtrl",function($scope, $resource, $http, $location,commonService){

        var loginCookie = commonService.getCookie('token_staff');//获取token
          if (loginCookie == "") {
              window.location.href="/login.html";
          }


        var logoutApi = $resource("/api/staff/logout");

        $scope.loginInfo={};
        $scope.type = "login";  //点击左上角的系统名称，所要跳转的页面


        //注销登录
        $scope.logout=function(){
            logoutApi.delete(function(data){
                window.location.href="/login.html";
            },function(errData){
                commonService.deleteCookie('token_staff');  //防止服务器注销失败后，浏览器还保留token
                console.log(errData.data.error);
            })
        };


        //点击“华晨汽车”标志动态跳转
        $scope.returnHome= function(type){
            if(type == 'login'){
                window.location.href="/web/main";
            }else{
                $location.path('/web/main');
                $scope.active(0); //菜单激活样式
            }

        };



        $scope.postData = {};
        //用户登录
        $scope.login=function(){
            $scope.postData.name = $scope.loginInfo.name;
            $scope.postData.pwd = $scope.loginInfo.pwd;
            if($scope.postData.pwd !=null && $scope.postData.pwd !=""){
                $scope.postData.pwd = hex_md5($scope.postData.pwd);
            }
            loginApi.save($scope.postData,function(data){
                var loginCookie = commonService.getCookie('token');
                $scope.loginMain(loginCookie,false);
            },function (errData) {
                $location.path("/web/main/reLogin");
                console.log(errData.data.error);
            });
        };


        /**
         * 通过token获取用户的详细信息
         *
         * @param loginCookie   登录成功或者已存在的浏览器的cookie
         * @param flag  true:登录成功获取用户详细并跳转相应的页面
         *               false:通过浏览器的有效cookie 来判断所要跳转的页面
         */
        var loginReInfoApi = $resource('/api/login/:token');  //返回登录用户的信息
        $scope.loginMain = function(loginCookie,flag){
            loginReInfoApi.get({token:loginCookie},function(data) {
                $scope.userInfo = data;
                $scope.getUserDetail($scope.userInfo.id);
                $scope.type = "main";  //点击左上角的系统名称，所要跳转的页面

                if(flag==false){
                    window.location.href="/web/main";
                }else{
                    $location.path(flag);
                }
            },function (errData) {
                commonService.deleteCookie('token');
                $location.path('/web/main');
                console.log(errData.data.error);
            })
        };

        /**
         * 获取登录用户详细信息
         */
        var userDetailApi = $resource('/api/user/:id');
        $scope.getUserDetail = function(id){
            userDetailApi.get({id:id},function(data){
                $scope.userDetail = data;
                $('[data-toggle="tooltip"]').tooltip()
            },function(errData){
                console.log(errData.data.error);
            })
        };

        //初始化激活菜单
        $scope.activeMenu=[{active:false,name:"mycar"},{active:false,name:"user"},{active:false,name:"vehicle"},{active:false,name:"organisation"},{active:false,name:"upload"}];
        $scope.active = function(index){
            for(var i=0;i<$scope.activeMenu.length;i++) {
                $scope.activeMenu[i].active = false;
            }
            if(index!=0){
                $scope.activeMenu[index-1].active = true;
            }
        };
    });
});
