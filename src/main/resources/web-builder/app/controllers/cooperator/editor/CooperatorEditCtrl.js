import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';

export default class CooperatorEditCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'CooperatorService'];
    static $template = require('./editor.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, CooperatorService} = this.$injected;
        this.attachMethodsTo($scope);

        if ($stateParams.id) {
            CooperatorService.getDetail($stateParams.id).then(res => {
                if (res.data && res.data.result) {
                    $scope.coop          = res.data.data;
                    $scope.coop.position = {lng: $scope.coop.longitude, lat: $scope.coop.latitude};
                }
            });
        } else {
            $scope.coop = {};
        }
        $scope.$watch('coop.position', (newVal) => {
            if (newVal) {
                let cb = 'geocoder' + Math.random().toString().substr(2);
                $('body').append($(`<script src="http://api.map.baidu.com/geocoder/v2/?ak=FPzmlgz02SERkbPsRyGOiGfj&output=json&location=${newVal.lat},${newVal.lng}&callback=${cb}"></script>`));
                window[cb] = data => {
                    $scope.$apply(() => {
                        let addr             = data.result.addressComponent;
                        $scope.coop.province = addr.province;
                        $scope.coop.city     = addr.city;
                        $scope.coop.district = addr.district;
                    });
                };
            }
        });
        $scope.uploadUrl = CooperatorService.uploadPhotoUrl;
    }

    onUploadedPhoto(data) {
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
        }
        return data.result ? data.data : '';
    }

    save() {
        const {$scope, $state, CooperatorService} = this.$injected;
        $scope.coop.longitude = $scope.coop.position.lng;
        $scope.coop.latitude  = $scope.coop.position.lat;
        let q, isUpdate       = !!$scope.coop.id;
        if (isUpdate) {
            q = CooperatorService.update($scope.coop);
        } else {
            q = CooperatorService.add($scope.coop);
        }

        q.then(res => {
            if (res.data) {
                if (res.data.result) {
                    if (isUpdate) {
                        let pCoop = $scope.$parent.cooperators.find(c => c.id === res.data.data.id);
                        if (pCoop) {
                            angular.extend(pCoop, res.data.data);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.cooperators.unshift(res.data.data);
                    }
                    $state.go('^.detail', {id: res.data.data.id});
                } else {
                    $scope.error = res.data.message;
                }
            }
        });
    }

}
