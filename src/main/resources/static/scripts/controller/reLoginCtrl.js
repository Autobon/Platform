define(['../autobon','jquery'],function(module,$){
    module.controller("reLoginCtrl",function($scope,$http,$location,$timeout,$resource,$filter,commonService){
        $scope.signinTemplate = "loginStep";
        $scope.loginInfo={};  //登录信息
        $scope.verification={};  //验证信息
        $scope.phonePattern = commonService.regex('phone');//定义phone的正则表达式

        $scope.warningShow = true;  //用户名或密码错误提示
        $scope.clickInfoShow=false;////提示信息是否显示
        $scope.sendShow = false;   //发送是否成功
        $scope.sendInfoShow = false;  //发送的提示
        $scope.unExistShow = false;//手机号未验证提示

        $scope.alertShow = true; //动态显示warning的提示语

        var loginCookie = commonService.getCookie('token');
        if(loginCookie != ""){
            $scope.loginMain(loginCookie,"/web/main");
        }else{
            $scope.changeHead(false,false);//显示当前头部
            $location.path('/web/main/reLogin');
        }

        //定义接口
        var loginApi = $resource("/api/login");  //登录
        var sendInfoApi = $resource("/api/user/:uid/phone/:sim/verify");  //通过手机号发送短信
        var resetPwdApi = $resource('/api/user/:uid/phone/:sim/verify/:code');  //验证收到短信的验证码

        //忘记密码跳转到验证页面
        $scope.forgetPassword = function(){
            $scope.signinTemplate = "verificationStep";
        };

        //返回到登录页面
        $scope.returnPrev=function(){
            $scope.verification.phone=null;
            $scope.verification.code=null;
            $scope.signinTemplate = "loginStep";
            $scope.alertShow = false;  //用于控制reLogin页面显示的warning
        };

        $scope.postData = {};
        var loginCookieApi = $resource('/api/login/:token');
        //登录验证
        $scope.login=function(){
            if($scope.loginInfo.name==null || $scope.loginInfo.pwd==null){
                $scope.alertShow = true; //用于控制reLogin页面显示的warning
                $scope.warningShow = false;
            }else{
                $scope.postData.name = $scope.loginInfo.name;
                $scope.postData.pwd = hex_md5($scope.loginInfo.pwd);

                loginApi.save($scope.postData,function(data){
                    var loginCookie = commonService.getCookie('token');
                    $scope.loginMain(loginCookie,false);   //调用indexCtrl.js里的方法
                },function (errData) {
                    $scope.alertShow = true; //用于控制reLogin页面显示的warning
                    $scope.warningShow = true;
                    $scope.loginInfo.pwd=null;
                    console.log(errData.data.error);
                });
            }
        };

        /**
         * 向手机发送验证码
         * 调用API   HTTP: GET  /api/phone/{sim}   根据phone获取uid
         * 调用API   HTTP: GET /api/user/{uid}/phone/{sim}/verify
         * 发送成功：（给出提示）
         * 发送失败：（给出提示）
         *
         */
        var getIdByPhoneApi = $resource('/api/user');
        $scope.info="BUTTON_SEND";
        $scope.sendInfo=function(flag){
            $scope.hideTips=true;//显示电话号码不能为空提示
            if(!flag) return;
            $scope.clickInfoShow=true;//提示信息是否显示
           $http.get('/api/user',{params:{phone:$scope.verification.phone}}).success(function(data){
               if(data.length>0){
                   $scope.uid =  data[0].id;
                   sendInfoApi.get({uid:$scope.uid,sim:$scope.verification.phone},function(data){
                       if(data.status == 'success'){
                           $scope.sendShow = true;   //发送是否成功
                           $scope.sendInfoShow = true;  //发送的提示
                           $scope.check=true;
                           $scope.totalTime=120;
                           $scope.showTime=function(){
                               $scope.totalTime=$scope.totalTime-1;
                               if($scope.totalTime>0 && $scope.totalTime<10){
                                   $scope.info="0"+$scope.totalTime;
                               }else{
                                   $scope.info=$scope.totalTime;
                               }
                               if($scope.totalTime==0){
                                   $scope.check=false;
                                   $scope.clickInfoShow=false;
                                   $scope.info="BUTTON_SEND";
                                   return;
                               }
                               $timeout(function(){
                                   $scope.showTime();
                               },1000);
                           };
                           $scope.showTime();
                       }
                   },function(errData){
                       console.log(errData.data.error);
                       $scope.getError = errData.data.error;
                       $scope.sendShow = true;   //发送是否成功
                       $scope.sendInfoShow = false;  //发送的提示
                   });
               }else{
                   $scope.sendShow = false;   //发送是否成功
                   $scope.unExistShow=true; //手机号是否验证
               }
           }).error(function(data){
               console.log(data);
           })
        };

        /**
         * 动态隐藏提示信息
         */
        $scope.clickShow=function(){
            $scope.clickInfoShow=false;//提示信息是否显示
        };
        /**
         * 提交验证码
         * 调用API进行校验  HTTP: GET /api/user/{uid}/phone/{sim}/verify/{code}
         * 如果校验通过,进入"重置密码界面"
         * 如果失败：（给出提示）
         */
        $scope.resetPassword=function(flag){
            if(!flag) return;
            if($scope.verification.code==undefined){
                $scope.hideShow=true;
                $scope.errorShow=false;
                $scope.verificationCodeIsRequired=true;
                return;
            }
            resetPwdApi.get({uid:$scope.uid,sim:$scope.verification.phone,code:$scope.verification.code},function(data){
                if(data.status == 'success'){
                    $location.path("/web/main/resetPassword");
                }
            },function(errData){
                if(errData.status==404){
                    $scope.hideShow=true;
                    $scope.errorShow=true;
                    $scope.errorInfoShow=true;
                }else if(errData.status==406){
                    $scope.hideShow=true;
                    $scope.errorShow=true;
                    $scope.errorInfoShow=false;
                }
            })
        };
        $scope.hideInfo=function(){
            $scope.hideShow=false;
        }
    });
});
