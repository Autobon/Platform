import {Injector} from 'ngES6';
import angular from 'angular';
import '../../../css/tree-control-attribute.css';
import '../../../css/tree-control.css';

export default class EditorRoleCtrl extends Injector {
    static $inject   = ['$scope', '$state', '$stateParams', 'StudyService', 'AccountService'];
    static $template = require('./editorRole.html');

    constructor(...args) {
        super(...args);
        const {$scope, $stateParams, AccountService} = this.$injected;
        this.attachMethodsTo($scope);
        $scope.chooseIds = [];
        $scope.menus = [];
        AccountService.searchMenu().then(res => {
            if (res.data && res.data.result) {
                $scope.menus = res.data.data;
            }
        });
        if ($stateParams.id) {
            AccountService.getRole($stateParams.id).then(res => {
                if (res.data && res.data.result) {
                    $scope.roleData = res.data.data;
                    AccountService.getRoleMenu($scope.roleData.id).then(menu => {
                        if (menu.data.data.menuId !== null) {
                            $scope.chooseIds = menu.data.data.menuId.split(/[,"]/);
                        }
                        if ($scope.menus !== null && $scope.menus.length > 0) {
                            for (let i = 0; i < $scope.menus.length; i++) {
                                $scope.menus[i].selected = false;
                                for (let j = 0; j < $scope.chooseIds.length; j++) {
                                    if ($scope.chooseIds[j] === $scope.menus[i].id) {
                                        $scope.menus[i].selected = true;
                                    }
                                }
                                // if (menu.data.data.menuId.indexOf($scope.menus[i].id < 0)) {
                                //     $scope.menus[i].selected = false;
                                // }
                            }
                        }
                    });
                }
            });
        } else {
            $scope.roleData = {
                position : {lng: null, lat: null},
            };
        }
        $scope.treeOptions = {
            nodeChildren: "children",
            dirSelectable: true,
            injectClasses: {
                ul: "a1",
                li: "a2",
                liSelected: "a7",
                iExpanded: "a3",
                iCollapsed: "a4",
                iLeaf: "a5",
                label: "a6",
                labelSelected: "a8"
            }
        };
        $scope.names = ['Homer', 'Marge', 'Bart', 'Lisa', 'Mo'];
        $scope.treedata = this.createSubTree(2, 4, "");
        $scope.buttonClick = function($event, node) {
            $scope.lastClicked = node;
            $event.stopPropagation();
        }
    }

    showSelected(sel) {
        const {$scope} = this.$injected;
        $scope.selectedNode = sel;
    };

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
            console.log($scope.chooseIds);
        }
    }

    onMenuChange(node, flag) {
        console.log(node);
        if (node.children.length > 0) {
            node.children.forEach(item =>{
                item.selected = flag;
            })
        }
    }

    save() {
        const {$scope, $state, AccountService} = this.$injected;
        if ($scope.roleData.name === null || $scope.roleData.name === '') {
            $scope.error = '姓名不能为空';
            return;
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
