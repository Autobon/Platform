import {Injector} from 'ngES6';
import angular from 'angular';

export default class StudyEditCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'StudyService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, StudyService} = this.$injected;
        this.attachMethodsTo($scope);

        $scope.typeList = [
            {id: 1, name: '培训资料'},
            {id: 2, name: '施工标准'},
            {id: 3, name: '业务规则'},
        ];

        if ($stateParams.id) {
            StudyService.getDetail($stateParams.id).then(res => {
                if (res.data && res.data.result) {
                    $scope.studyData = res.data.data;
                }
            });
        } else {
            $scope.studyData = {
                position : {lng: null, lat: null},
            };
        }
        // $scope.$watch('coop.position', (newVal) => {
        //     if (newVal) {
        //         let cb = 'geocoder' + Math.random().toString().substr(2);
        //         $('body').append($(`<script src="http://api.map.baidu.com/geocoder/v2/?ak=FPzmlgz02SERkbPsRyGOiGfj&output=json&location=${newVal.lat},${newVal.lng}&callback=${cb}"></script>`));
        //         window[cb] = data => {
        //             $scope.$apply(() => {
        //                 let addr             = data.result.addressComponent;
        //                 $scope.coop.province = addr.province;
        //                 $scope.coop.city     = addr.city;
        //                 $scope.coop.district = addr.district;
        //             });
        //         };
        //     }
        // });
        $scope.uploadUrl = StudyService.uploadPhotoUrl;
    }

    onUploaded(data) {
        const {$scope, $uibModal} = this.$injected;
        if (!data.result) {
            $scope.message = data.message;
            $uibModal.open({
                size     : 'sm',
                scope    : $scope,
                animation: true,
                template : `
                    <div class="modal-header">
                        <h3 class="modal-title">提示消息</h3>
                    </div>
                    <div class="modal-body">
                        <b>{{message}}</b>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" ng-click="$close()">确定</button>
                    </div>`,
            });
        } else {
            $scope.studyData.fileName = data.res.fileName;
            $scope.studyData.fileLength = data.res.fileLength;
        }
        return data.result ? data.data : '';
    }

    save() {
        const {$scope, $state, StudyService} = this.$injected;
        let q, isUpdate       = !!$scope.studyData.id;
        if (isUpdate) {
            q = StudyService.update($scope.studyData);
        } else {
            q = StudyService.add($scope.studyData);
        }

        q.then(res => {
            if (res.data) {
                if (res.data.result) {
                    if (isUpdate) {
                        let data = $scope.$parent.studyDatas.find(c => c.id === res.data.data.id);
                        if (data) {
                            angular.extend(data, res.data.data);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.studyDatas.unshift(res.data.data);
                    }
                    $state.go('^.detail', {id: res.data.data.id});
                } else {
                    if (res.data.result === false) {
                        $scope.error = res.data.message;
                    }
                }
            }
        });
    }

}
