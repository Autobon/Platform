import {Injector} from 'ngES6';
import angular from 'angular';
import '../../../css/tree-control-attribute.css';
import '../../../css/tree-control.css';

export default class EditorRoleCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'StudyService', 'AccountService', 'CooperatorService', '$timeout'];
    static $template = require('./editorRole.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, AccountService, CooperatorService, $timeout} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.chooseMenuIds = [];
        $scope.chooseCategorieIds = [];
        $scope.chooseCoopIds = [];
        $scope.coops = [];
        $scope.selected = [];
        $scope.expandedNodes = [];
        CooperatorService.search(null, 1, 1000).then(res => {
            if (res.data.result && res.data.data) {
                $scope.coops = res.data.data.list;
            }
        });
        AccountService.getAllMenus().then(res =>{
            $scope.treedata = res.data.data;
            $scope.treedata.forEach(t =>{
                $scope.expandedNodes.push(t);
            })

        });
        $scope.searchSelectAllSettings = {
            displayProp: 'fullname',
            searchField: 'fullname',
            enableSearch: true,
            showSelectAll: true,
            keyboardControls: true,
            selectedToTop: true,
            scrollable: true
        };
        if ($stateParams.id) {
            $scope.selected = [];
            AccountService.getRole($stateParams.id).then(res => {
                if (res.data && res.data.result) {
                    $scope.roleData = res.data.data;
                    $timeout(() => {
                        if ($scope.roleData.coopIds !== null) {
                            $scope.chooseCoopIds = $scope.roleData.coopIds.split(/[,"']/);
                        }
                        $scope.chooseCoopIds.forEach(item => {
                            let chooseCoop = $scope.coops.find(c => c.id == item * 1);
                            $scope.selected.push(chooseCoop);
                        });
                        // 权限
                        if ($scope.roleData.functionCategoryIds != null) {
                            $scope.chooseCategorieIds = $scope.roleData.functionCategoryIds.split(/[,"']/);
                        }
                        if ($scope.roleData.menuIds != null) {
                            $scope.chooseMenuIds = $scope.roleData.menuIds.split(/[,"']/);
                        }
                        console.log($scope.chooseCategorieIds);
                        console.log($scope.chooseMenuIds);
                        $scope.treedata.forEach(item =>{
                            if($scope.chooseMenuIds.indexOf(item.id.toString()) != -1){
                                item.selected = true;
                                if(item.functionCategories && item.functionCategories.length > 0){
                                    item.functionCategories.forEach(f => {
                                        if($scope.chooseCategorieIds.indexOf(f.id.toString()) != -1){
                                            f.selected = true;
                                        }
                                    })
                                }
                            }
                        });
                    }, 200);
                    // AccountService.getRoleMenu($scope.roleData.id).then(menu => {
                    //     if (menu.data.data.menuId !== null) {
                    //         $scope.chooseCoopIds = menu.data.data.menuId.split(/[,"]/);
                    //     }
                    //     if ($scope.menus !== null && $scope.menus.length > 0) {
                    //         for (let i = 0; i < $scope.menus.length; i++) {
                    //             $scope.menus[i].selected = false;
                    //             for (let j = 0; j < $scope.chooseIds.length; j++) {
                    //                 if ($scope.chooseIds[j] === $scope.menus[i].id) {
                    //                     $scope.menus[i].selected = true;
                    //                 }
                    //             }
                    //             // if (menu.data.data.menuId.indexOf($scope.menus[i].id < 0)) {
                    //             //     $scope.menus[i].selected = false;
                    //             // }
                    //         }
                    //     }
                    // });
                }
            });
        } else {
            $scope.roleData = {
                position : {lng: null, lat: null},
            };
        }
        $scope.treeOptions = {
            nodeChildren: "functionCategories",
            dirSelectable: true
        };
        // $scope.names = ['Homer', 'Marge', 'Bart', 'Lisa', 'Mo'];
        // $scope.treedata = this.createSubTree(2, 4, "");
        // $scope.buttonClick = function($event, node) {
        //     $scope.lastClicked = node;
        //     $event.stopPropagation();
        // };
    }

    createSubTree(level, width, prefix) {
        const {$scope} = this.$injected;
        if (level > 0) {
            var res = [];
            for (var i=1; i <= width; i++)
                res.push({ "label" : "Node " + prefix + i, "id" : "id"+prefix + i, "i": i, "children": this.createSubTree(level-1, width, prefix + i +"."), "name": $scope.names[i%$scope.names.length] });
            return res;
        }
        else
            return [];
    }

    onCheckChange(id, flag) {
        const {$scope} = this.$injected;

        if (flag) {
            if (id) {
                $scope.chooseIds.push(id);
            }
        } else {
            $scope.chooseIds = $scope.chooseIds.filter(items => items !== id);
        }
    }

    onMenuChange(node, flag, parentNode) {
        const {$scope} = this.$injected;
        if (node.functionCategories && node.functionCategories.length > 0) {
            node.functionCategories.forEach(item =>{
                item.selected = flag;
                if(flag){
                    $scope.chooseCategorieIds.push(item.id);
                }else{
                    $scope.chooseCategorieIds.splice($scope.chooseCategorieIds.indexOf(item.id), 1);
                }
            });
            if(flag){
                $scope.chooseMenuIds.push(node.id);
            }else{
                $scope.chooseMenuIds.splice($scope.chooseMenuIds.indexOf(node.id), 1);
            }
        }
        if(parentNode){
            if(flag){
                $scope.chooseCategorieIds.push(node.id);
                $scope.chooseMenuIds.push(parentNode.id);
            }else{
                $scope.chooseCategorieIds.splice($scope.chooseCategorieIds.indexOf(node.id), 1);
            }
            if(parentNode.functionCategories.length > 0){
                let res = parentNode.functionCategories.find(f => f.selected == true);
                if (res == null) {
                    $scope.treedata.forEach(t => {
                        if(t.id == parentNode.id){
                            t.selected = false;
                            $scope.chooseMenuIds.splice($scope.chooseMenuIds.indexOf(t.id), 1);
                        }
                    });
                }
                let res2 = parentNode.functionCategories.find(f => f.selected == false);
                if (res2 == null) {
                    $scope.treedata.forEach(t => {
                        if(t.id == parentNode.id){
                            t.selected = true;
                        }
                    });
                }
            }
        }
        console.log($scope.chooseMenuIds);
        console.log($scope.chooseCategorieIds);
    }

    save() {
        const {$scope, $state, AccountService} = this.$injected;
        if ($scope.roleData.name === null || $scope.roleData.name === '') {
            $scope.error = '姓名不能为空';
            return;
        }
        if($scope.selected || $scope.selected.length < 1){
            $scope.error = '请选择商户';
            return;
        }
        if($scope.chooseMenuIds.length < 1 || $scope.chooseCategorieIds.length < 1){
            $scope.error = '请选择权限';
            return;
        }
        if ($scope.selected.length > 0) {
            let ids = [];
            $scope.selected.forEach(item =>{
                ids.push(item.id);
            });
            $scope.roleData.coopIds = ids;
        }
        let q, isUpdate       = !!$scope.roleData.id;
        if (isUpdate) {
            q = AccountService.updateRole($scope.roleData.id, $scope.roleData.name, $scope.roleData.remark, $scope.chooseIds.join(','));
        } else {
            q = AccountService.addRole($scope.roleData.name, $scope.roleData.remark, $scope.chooseIds.join(','));
        }

        q.then(res => {
            if (res.data) {
                if (res.data.result) {
                    if (isUpdate) {
                        let data = $scope.$parent.roleDatas.find(c => c.id === res.data.data.id);
                        if (data) {
                            angular.extend(data, res.data.data);
                        }
                    } else if (!isUpdate && $scope.$parent.pagination.page === 1) {
                        $scope.$parent.roleDatas.unshift(res.data.data);
                    }
                    $state.go('^');
                } else {
                    if (res.data.result === false) {
                        $scope.error = res.data.message;
                    }
                }
            }
        });
    }

}
