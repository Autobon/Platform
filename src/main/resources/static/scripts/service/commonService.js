/**
 * Created by wh on 2015/8/24.
 */
define(['../autobon'],function(module){

    return module.service('commonService', (function(){
        function CommonService($http,$resource,$timeout,$location){
            this.$http = $http;
            this.$resource = $resource;
            this.$timeout = $timeout;
            this.$location = $location;
        }

        //通用正则表达式
        CommonService.prototype.regex = function(type){
            var regex ;
            if(type == 'phone'){
                regex = /^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;//定义phone的正则表达式
            }else if(type == 'password'){
                regex = /^(?![0-9]+$)(?![a-zA-Z]+$).{7,}$/;//定义password的正则表达式
            }else if(type == 'name'){
                regex =/^[a-zA-Z_][a-zA-Z0-9_]{0,49}$/;//定义name的正则表达式
            }else if(type == 'breCode'){
                regex =/^.{1,8}$/;
            }else if(type == 'nick'||type=='vin'||type=='tboxsn'||type=='orgName'){
                regex =/^.{1,50}$/;
            }else if(type=='vendor'||type=='model'||type=='descript'){
                regex =/^.{1,100}$/;
            }else if(type=='displacement'){
                regex =/^\d{1,1}(\.\d{1,1})$/;
            }else if(type=='license_plate'||type=='termName'){
                regex =/^.{1,10}$/;
            }else if(type=='product_date'){
                regex=/((^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(10|12|0?[13578])([-\/\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(11|0?[469])([-\/\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(0?2)([-\/\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([3579][26]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][13579][26])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][13579][26])([-\/\._])(0?2)([-\/\._])(29)$))/
            }else if(type=='mileage'){
                regex = /^\d*$/;
            }
            return regex;
        };

        //获取cookie
        CommonService.prototype.getCookie = function(name){
            var arr = document.cookie.split("; ");
            for(var i=0,len=arr.length;i<len;i++){
                var item = arr[i].split("=");
                if(item[0]==name){
                    return item[1];
                }
            }
            return "";
        };

        CommonService.prototype.deleteCookie = function(name){
            var exp = new Date();
            exp.setTime(exp.getTime() - 1000);
            var cval=this.getCookie(name);
            if(cval!=null){
                document.cookie= name+"="+cval+";expires="+exp.toUTCString()+"; path=/";
            }
        };

        //毫秒转日期
        CommonService.prototype.dateFormat = function(time, format){
            var t = new Date(time);
            var tf = function(i){return (i < 10 ? '0' : '') + i};
            return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
                switch(a){
                    case 'yyyy':
                        return tf(t.getFullYear());
                        break;
                    case 'MM':
                        return tf(t.getMonth() + 1);
                        break;
                    case 'mm':
                        return tf(t.getMinutes());
                        break;
                    case 'dd':
                        return tf(t.getDate());
                        break;
                    case 'HH':
                        return tf(t.getHours());
                        break;
                    case 'ss':
                        return tf(t.getSeconds());
                        break;
                }
            })
        };

        //获取登录用户信息
        CommonService.prototype.getLoginInfo = function(cb){
            var loginReInfoApi = this.$resource('/api/login/:token');  //返回登录用户的信息
            var token = this.getCookie("token");
            loginReInfoApi.get({token:token},function(data){
                cb(data);
            },function(errData){
                console.log(errData.data.error);
            })
        };

        //通过url的关键字获取值
        CommonService.prototype.delQueryUrl =function (key,cb){
            var queryUrl = this.$location.url().split("?");
            if (queryUrl.length > 1) {
                var params = decodeURIComponent(queryUrl[1]).split("&");
                var flag = false;
                var value = "";
                for (var i = 0; i < params.length; i++) {
                    var param = params[i].split("=");
                    if (param[0] == key) {
                        value = param[1];
                        flag = true;
                    }
                }
                if(flag) cb(value);
                else cb(null);
            }else{
                cb(null);
            }
        };

        //获取当前日期与时间
        CommonService.prototype.getLocationDate =function(cb){
            var now = new Date();
            var date = now.getFullYear() + "-" + ((now.getMonth() + 1) < 10 ? "0" : "") + (now.getMonth() + 1) + "-" + (now.getDate() < 10 ? "0" : "") + now.getDate();
            var time = (now.getHours() < 10 ? "0" : "") + now.getHours() + ":" + (now.getMinutes() < 10 ? "0" : "") + now.getMinutes() + ":" + (now.getSeconds() < 10 ? "0" : "") + now.getSeconds();
            cb({date:date,time:time});
        };

        //获取现在相对于东八区的时间
        CommonService.prototype.getunowStr = function(){
            var unow = new Date(new Date().getTime() + new Date().getTimezoneOffset()*60000);
            var year = unow.getFullYear();
            var mon = unow.getMonth()+1;
            var day = unow.getDate();
            var hours = unow.getHours();
            var minute = unow.getMinutes();
            var second = unow.getSeconds();
            var s = year+"-"+(mon<10?('0'+mon):mon)+"-"+(day<10?('0'+day):day)+" "+hours+":"+minute+":"+second;
            return s;
        };


        //最近n天的日期
        CommonService.prototype.getBeforeDate = function(n){
            var n = n;
            var d = new Date();
            var year = d.getFullYear();
            var mon=d.getMonth()+1;
            var day=d.getDate();
            if(day <= n){
                if(mon>1) {
                    mon = mon-1;
                }
                else {
                    year = year-1;
                    mon = 12;
                }
            }
            d.setDate(d.getDate()-n);
            year = d.getFullYear();
            mon=d.getMonth()+1;
            day=d.getDate();
            var s = year+"-"+(mon<10?('0'+mon):mon)+"-"+(day<10?('0'+day):day);
            return s;
        };

        return ['$http', '$resource','$timeout','$location', CommonService];
    })());
});