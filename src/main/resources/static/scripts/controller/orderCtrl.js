define(['../autobon','jquery','../directive/fileMode','../service/fileService'],function(module,$) {
    module.controller("orderCtrl", function ($scope, $http, $location, $timeout, $resource, $filter, commonService,fileReader) {

        var orderApi = $resource("/api/order");
        var orderApiUpdate = $resource("/api/order/modify", null ,{"update":{ method:'PUT' }});
        $scope.orderInfo = {};

        //增加订单
        $scope.aa = function(){
            $scope.postData = {orderNum:"", orderType:1};
            orderApi.save($scope.postData, function(data){
                alert(data.message);
            },function(err){
                console.log(err.data.error);
            });
        };

        //修改订单
        $scope.updateOrder = function(){
            $scope.postData = {id:2, orderNum:"145628482387006249", orderType:2};
            orderApiUpdate.update($scope.postData, function(data){
                alert(data.message);
            },function(err){
                console.log(err.data.error);
            });
        };

        //添加订单
        $scope.addOrder = function(){
            $scope.orderInfo.file = $scope.myFile;
            var fd = new FormData();
            var url = "/api/order";
            angular.forEach($scope.orderInfo, function(val, key) {
                fd.append(key, val);
            });
            var args = {
                method: 'POST',
                url: url,
                data: fd,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            };
            $http(args).success(function(data){
                alert(data.message);
            });
        };

        $scope.getFile = function() {
            fileReader.readAsDataUrl($scope.file, $scope)
                .then(function(result) {
                    $scope.imageSrc = result;
                });
        };

    });
});