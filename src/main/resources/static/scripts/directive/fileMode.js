/**
 * Created by liz on 2016/2/20.
 */
define(['../autobon'],function(module){
    module.directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs, ngModel) {
                console.log(scope);
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;
                element.bind('change', function(event){
                    scope.$apply(function(){
                        modelSetter(scope, element[0].files[0]);
                    });
                    //附件预览
                    scope.file = (event.srcElement || event.target).files[0];
                    scope.getFile();
                });
            }
        };
    }]);
});