/**
 * Created by liz on 2016/2/20.
 */
define(['../autobon','jquery','angular'],function(module,$,angular){


    module.filter('genderType',function(){
        return function(input) {
            var genderType = "";
            if (input == 0) genderType = "男";
            else if (input == 1) genderType = "女";
            return genderType;
        }
    });
    
    module.filter('pattern',function(){
        return function(input) {
            var pattern = "";
            if (input) pattern = "调试模式";
            else  pattern = "用户模式";
            return pattern;
        }
    })

});