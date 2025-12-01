define(
    [
        'app/desktop/modules/storage/storage.module',
        'split-pane',
        'jquery.easyui',
        'app/desktop/modules/storage/details/storageDetailsController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/storageService',
        'app/shared/services/core/bomService',
        'app/desktop/modules/storage/directive/storageTreeViewDirective'
    ],
    function (module) {
        module.controller('StorageController', StorageController);

        function StorageController($scope, $window, $rootScope, $timeout, $state, $stateParams, $cookies, DialogService,
                                   StorageService, BomService) {

            $rootScope.viewInfo.icon = "fa fa-bank";
            $rootScope.viewInfo.title = "Storage";

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.valid = true;

            vm.setViewType = setViewType;
            vm.object = null;


            vm.newOnHoldStorage = newOnHoldStorage;
            vm.newReturnStorage = newReturnStorage;
            vm.newWarehouse = newWarehouse;
            vm.newStockRoom = newStockRoom;
            vm.newRack = newRack;
            vm.newShelf = newShelf;
            vm.newBin = newBin;
            vm.newArea = newArea;
            vm.deleteStorageType = deleteStorageType;
            vm.onSave = onSave;
            var storageTree = null;
            var nodeId = 0;
            var rootNode = null;
            vm.viewType = 'tree';
            vm.isTree = true;
            vm.selectedType = null;
            vm.addAttribute = addAttribute;
            vm.editMode = false;
            vm.addNewStorageType = addNewStorageType;
            vm.applyChanges = applyChanges;
            vm.lastSelectedStorage = null;
            $rootScope.checkWithSystemWithMissile = false;
            $rootScope.checkWithSystem = false;
            $rootScope.checkReqSystemWithMissile = false;
            $rootScope.storeType = null;
            $rootScope.selectedType = null;

            vm.transferValid = false;

            vm.deleteWarehouseTitle = "Delete Warehouse";

            vm.newStorageType = {
                id: null,
                name: null,
                description: null,
                dataType: 'TEXT',
                required: false,
                objectType: 'STORAGE',
                storageObjectType: null
            };

            function addNewStorageType() {
                vm.editMode = true;
                vm.newStorageType = {
                    id: null,
                    name: null,
                    description: null,
                    dataType: 'TEXT',
                    required: false,
                    objectType: 'STORAGE',
                    storageObjectType: vm.newStorageType.storageObjectType
                };
            }

            function applyChanges() {
                var promise = null;
                if (vm.newStorageType.id == null || vm.newStorageType.id == undefined) {
                    if (vm.newStorageType.storageObjectType != null) {

                    }
                    else {

                    }
                }
                else {

                }

                promise.then(
                    function (data) {
                        vm.editMode = false;

                        $rootScope.showSuccessMessage("StorageAttribute Created Successfully!");
                    }
                )
            }

            function addAttribute(type) {
                vm.editMode = false;
                vm.selectedType = type;
                vm.newStorageType.storageObjectType = type;
            }

            function setViewType(view) {
                vm.isTree = (view === 'tree') ? true : false;
                vm.viewType = view;
            }

            $rootScope.storagePartsCount = 0;
            function deleteStorageType() {
                var selectedNode = storageTree.tree('getSelected');

                if ($rootScope.storagePartsCount > 0) {
                    $rootScope.showErrorMessage("Storage Location has Items. We can't delete");
                } else {
                    if (selectedNode != null &&
                        (selectedNode.object == null || selectedNode.object.id != undefined)) {
                        var options = {
                            title: 'Delete Storage',
                            message: 'Are you sure you want to delete (' + selectedNode.object.name + ')?',
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                StorageService.deleteStorage(selectedNode.object.id).then(
                                    function (data) {
                                        storageTree.tree('remove', selectedNode.target);
                                        $rootScope.showSuccessMessage(selectedNode.object.name + " " + "Storage deleted successfully");
                                        $rootScope.$broadcast('app.storage.selected', {object: null});
                                    }
                                )
                            }
                        })
                    } else {
                        storageTree.tree('remove', selectedNode.target);
                        $scope.$broadcast('app.storage.selected', {object: null});
                    }
                }


            }


            function newReturnStorage() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'wh-ret-node',
                                text: 'Return Store',
                                type: 'WAREHOUSE',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null,
                                    returned: true
                                },
                            }
                        ]
                    });
                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                    }
                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            function newOnHoldStorage() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'wh-on-node',
                                text: 'On Hold Store',
                                type: 'WAREHOUSE',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null,
                                    onHold: true
                                },
                            }
                        ]
                    });
                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                    }
                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            function newWarehouse() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'wh-node',
                                text: 'New Warehouse',
                                type: 'WAREHOUSE',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null
                                },
                            }
                        ]
                    });
                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                    }
                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            function newStockRoom() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'str-node',
                                text: 'New StockRoom',
                                type: 'STOCKROOM',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null
                                },
                            }
                        ]
                    });

                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                    }

                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);
                        // vm.storeType=newNode.text;


                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            function newRack() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'rack-node',
                                text: 'New Rack',
                                type: 'RACK',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null
                                },
                            }
                        ]
                    });
                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                        //vm.storeType=newNode.text;
                    }
                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            function newArea() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'area-node',
                                text: 'New Area',
                                type: 'AREA',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null
                                },
                            }
                        ]
                    });
                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                    }
                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);
                        //vm.storeType=newNode.text;
                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            function newShelf() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'shelf-node',
                                text: 'New Shelf',
                                type: 'SHELF',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null
                                },
                            }
                        ]
                    });
                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                    }

                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);
                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            function newBin() {
                $rootScope.show = true;
                vm.selectedNode = storageTree.tree('getSelected');

                if (vm.selectedNode != null &&
                    (vm.selectedNode.object == null || vm.selectedNode.object.id != undefined)) {
                    var nodeid = ++nodeId;

                    storageTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'bin-node',
                                text: 'New BIN',
                                type: 'BIN',
                                object: {
                                    name: null,
                                    description: null,
                                    parent: null
                                },
                            }
                        ]
                    });
                    if (vm.selectedNode.children.length != null) {
                        var newNode = storageTree.tree('find', nodeid);
                        storageTree.tree('expandTo', newNode.target);
                    }
                    var newNode = storageTree.tree('find', nodeid);
                    if (newNode != null) {
                        storageTree.tree('select', newNode.target);
                        storageTree.tree('beginEdit', newNode.target);
                        // vm.storeType=newNode.text;
                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                } else {
                    $rootScope.showWarningMessage("Please save Selected Storage");
                }
            }

            $rootScope.storeType = null;
            function onSelectType(node) {
                vm.lastSelectedStorage = null;
                var data = storageTree.tree('getData', node.target);
                if (data.object == null/* || data.object.id == undefined*/) {
                    vm.lastSelectedStorage = node;
                    $rootScope.showSaveButton = false;
                    $rootScope.storeType = null;
                    $scope.$apply();
                    $scope.$broadcast('app.storage.selected', {object: null});
                } else {
                    var object = data.object;
                    object.type = data.type;
                    vm.lastSelectedStorage = node;
                    $rootScope.storeType = (vm.lastSelectedStorage.type) + " [ " + object.name + " ]";
                    $rootScope.checkStorage = true;

                    $scope.$broadcast('app.storage.selected', {object: object, nodeId: node.id});
                }
            }

            function onSave() {
                $scope.$broadcast('app.storage.save');
            }

            function update(event, args) {

                var node = storageTree.tree('find', args.nodeId);
                if (node) {
                    node.object = args.object;
                    storageTree.tree('update', {
                        target: node.target,
                        text: node.object.name,
                        object: node.object
                    });
                    $scope.$broadcast('app.storage.selected', {object: node.object, nodeId: node.id});
                }
            }

            function onAfterEdit(node) {
                var data = storageTree.tree('getData', node.target);
                var parent = storageTree.tree('getParent', node.target);
                var parentData = storageTree.tree('getData', parent.target);
                if (parentData.id != 0) {
                    data.object.parentType = parent.object.id;
                }
                $rootScope.storeType = (vm.lastSelectedStorage.type) + " [ " + node.text + " ]";
                if (data.type == 'WAREHOUSE') {
                    StorageService.getWarehouseTypeByName(data.text).then(
                        function (warehouse) {
                            if (warehouse == "" || warehouse == null) {
                                data.object.id = null;
                                data.object.name = data.text;
                                data.object.type = data.type;
                                data.object.parent = null;
                                data.object.parentData = null;
                                data.object.parentType = parent.id;
                                StorageService.createStorage(data.object).then(
                                    function (storage) {
                                        data.object = storage;
                                        storageTree.tree('update', {
                                            target: node.target,
                                            object: node.object
                                        });
                                        storageTree.tree('select', node.target);
                                        $rootScope.$broadcast('app.storage.selected', {
                                            object: data.object,
                                            nodeId: node.id
                                        });
                                        $rootScope.showSuccessMessage("Storage created successfully");
                                    }
                                )

                            } else {
                                $rootScope.showErrorMessage("Warehouse name already exist.");
                                storageTree.tree('beginEdit', node.target);
                            }
                        }
                    )
                } else if (data.type == 'STOCKROOM') {
                    if (data.object.parentType == null || data.object.parentType == undefined) {
                        StorageService.getStockRoomByName(data.text).then(
                            function (stockroom) {
                                if (stockroom == "" || stockroom == null) {
                                    data.object.id = null;
                                    data.object.name = data.text;
                                    data.object.type = data.type;
                                    data.object.parent = null;
                                    data.object.parentData = null;
                                    data.object.parentType = null;
                                    StorageService.createStorage(data.object).then(
                                        function (storage) {
                                            data.object = storage;
                                            storageTree.tree('update', {
                                                target: node.target,
                                                object: node.object
                                            });
                                            storageTree.tree('select', node.target);
                                            $rootScope.$broadcast('app.storage.selected', {
                                                object: data.object,
                                                nodeId: node.id
                                            });
                                            $rootScope.showSuccessMessage("Storage created successfully");
                                        }
                                    )
                                } else {
                                    $rootScope.showErrorMessage("Stockroom name already exist");
                                    storageTree.tree('beginEdit', node.target);
                                }
                            }
                        )
                    } else {
                        StorageService.getStockRoomByParentAndName(data.object.parentType, data.text).then(
                            function (stockroom) {
                                if (stockroom == "" || stockroom == null) {
                                    data.object.id = null;
                                    data.object.name = data.text;
                                    data.object.type = data.type;
                                    data.object.parent = parent.object.id;
                                    data.object.parentData = parent.object;
                                    data.object.parentType = parent.id;
                                    data.object.bom = data.object.parentData.bom;
                                    data.object.onHold = data.object.parentData.onHold;
                                    data.object.returned = data.object.parentData.returned;
                                    StorageService.createStorage(data.object).then(
                                        function (storage) {
                                            data.object = storage;
                                            storageTree.tree('update', {
                                                target: node.target,
                                                object: node.object
                                            });
                                            storageTree.tree('select', node.target);
                                            $rootScope.$broadcast('app.storage.selected', {
                                                object: data.object,
                                                nodeId: node.id
                                            });
                                            $rootScope.showSuccessMessage("Storage created successfully");
                                        }
                                    )
                                } else {
                                    $rootScope.showErrorMessage("Stockroom name already exist");
                                    storageTree.tree('beginEdit', node.target);
                                }
                            }
                        )
                    }
                } else if (data.type == 'AREA') {
                    StorageService.getAreaByParentAndName(data.object.parentType, data.text).then(
                        function (area) {
                            if (area == "" || area == null) {
                                data.object.id = null;
                                data.object.name = data.text;
                                data.object.type = data.type;
                                data.object.parent = parent.object.id;
                                data.object.parentData = parent.object;
                                data.object.parentType = parent.id;
                                data.object.bom = data.object.parentData.bom;
                                data.object.onHold = data.object.parentData.onHold;
                                data.object.returned = data.object.parentData.returned;
                                StorageService.createStorage(data.object).then(
                                    function (storage) {
                                        data.object = storage;
                                        storageTree.tree('update', {
                                            target: node.target,
                                            object: node.object
                                        });
                                        storageTree.tree('select', node.target);
                                        $rootScope.$broadcast('app.storage.selected', {
                                            object: data.object,
                                            nodeId: node.id
                                        });
                                        $rootScope.showSuccessMessage("Storage created successfully");
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("Area name already exist");
                                storageTree.tree('beginEdit', node.target);
                            }
                        }
                    )
                } else if (data.type == 'RACK') {
                    StorageService.getRackByParentAndName(data.object.parentType, data.text).then(
                        function (rack) {
                            if (rack == "" || rack == null) {
                                data.object.id = null;
                                data.object.name = data.text;
                                data.object.type = data.type;
                                data.object.parent = parent.object.id;
                                data.object.parentData = parent.object;
                                data.object.parentType = parent.id;
                                data.object.bom = data.object.parentData.bom;
                                data.object.onHold = data.object.parentData.onHold;
                                data.object.returned = data.object.parentData.returned;
                                StorageService.createStorage(data.object).then(
                                    function (storage) {
                                        data.object = storage;
                                        storageTree.tree('update', {
                                            target: node.target,
                                            object: node.object
                                        });
                                        storageTree.tree('select', node.target);
                                        $rootScope.$broadcast('app.storage.selected', {
                                            object: data.object,
                                            nodeId: node.id
                                        });
                                        $rootScope.showSuccessMessage("Storage created successfully");
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("Rack name already exist");
                                storageTree.tree('beginEdit', node.target);
                            }
                        }
                    )
                } else if (data.type == 'SHELF') {
                    StorageService.getShelfByParentAndName(data.object.parentType, data.text).then(
                        function (shelf) {
                            if (shelf == "" || shelf == null) {
                                data.object.id = null;
                                data.object.name = data.text;
                                data.object.type = data.type;
                                data.object.parent = parent.object.id;
                                data.object.parentData = parent.object;
                                data.object.parentType = parent.id;
                                data.object.bom = data.object.parentData.bom;
                                data.object.onHold = data.object.parentData.onHold;
                                data.object.returned = data.object.parentData.returned;
                                StorageService.createStorage(data.object).then(
                                    function (storage) {
                                        data.object = storage;
                                        storageTree.tree('update', {
                                            target: node.target,
                                            object: node.object
                                        });
                                        storageTree.tree('select', node.target);
                                        $rootScope.$broadcast('app.storage.selected', {
                                            object: data.object,
                                            nodeId: node.id
                                        });
                                        $rootScope.showSuccessMessage("Storage created successfully");
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("Shelf name already exist");
                                storageTree.tree('beginEdit', node.target);
                            }
                        }
                    )
                } else if (data.type == 'BIN') {
                    StorageService.getBinByParentAndName(data.object.parentType, data.text).then(
                        function (bin) {
                            if (bin == "" || bin == null) {
                                data.object.id = null;
                                data.object.name = data.text;
                                data.object.type = data.type;
                                data.object.parent = parent.object.id;
                                data.object.parentData = parent.object;
                                data.object.parentType = parent.id;
                                data.object.bom = data.object.parentData.bom;
                                data.object.onHold = data.object.parentData.onHold;
                                data.object.returned = data.object.parentData.returned;
                                StorageService.createStorage(data.object).then(
                                    function (storage) {
                                        data.object = storage;
                                        storageTree.tree('update', {
                                            target: node.target,
                                            object: node.object
                                        });
                                        storageTree.tree('select', node.target);
                                        $rootScope.$broadcast('app.storage.selected', {
                                            object: data.object,
                                            nodeId: node.id
                                        });
                                        $rootScope.showSuccessMessage("Storage created successfully");
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("Bin name already exist");
                                storageTree.tree('beginEdit', node.target);
                            }
                        }
                    )
                }
            }

            function initStorageTree() {
                storageTree = $('#storageTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Storage',
                            iconCls: 'bom-root',
                            type: null,
                            object: null,
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    //onDblClick: onDblClick,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectType
                });

                rootNode = storageTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                    $("#whcontextMenu").hide();
                    $("#strcontextMenu").hide();
                    $("#areacontextMenu").hide();
                    $("#rackcontextMenu").hide();
                    $("#shelfcontextMenu").hide();
                    $("#bincontextMenu").hide();
                });
            }

            $rootScope.storageTreeDetails = false;
            function loadStorageTree() {
                StorageService.getStorageTree().then(
                    function (data) {
                        if (data.length == 0) {
                            $rootScope.storageTreeDetails = true;
                        }
                        var nodes = [];
                        angular.forEach(data, function (object) {
                            var node = makeNode(object);

                            if (object.children != null && object.children != undefined && object.children.length > 0) {

                                visitChildren(node, object.children);
                            }
                            nodes.push(node);
                        });

                        storageTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function visitChildren(parent, objects) {
                parent.state = "closed";
                var nodes = [];
                angular.forEach(objects, function (object) {
                    var node = makeNode(object);
                    if (object.children != null && object.children != undefined && object.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, object.children);
                    }
                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(object) {
                var treeNode = {
                    id: ++nodeId,
                    text: object.name,
                    object: object,
                    type: object.type,
                    iconCls: null,
                }
                if (object.type == 'WAREHOUSE') {
                    if (object.returned) {
                        treeNode.iconCls = 'wh-ret-node';
                    } else if (object.onHold) {
                        treeNode.iconCls = 'wh-on-node';
                    } else {
                        treeNode.iconCls = 'wh-node';
                    }
                } else if (object.type == 'STOCKROOM') {
                    treeNode.iconCls = 'str-node';
                } else if (object.type == 'SHELF') {
                    treeNode.iconCls = 'shelf-node';
                } else if (object.type == 'BIN') {
                    treeNode.iconCls = 'bin-node';
                } else if (object.type == 'RACK') {
                    treeNode.iconCls = 'rack-node';
                } else if (object.type == 'AREA') {
                    treeNode.iconCls = 'area-node';
                } else {
                    treeNode.iconCls = 'bom-node';
                }
                return treeNode;
            }

            $rootScope.storageCodeAndTypeValid = null;
            function onContextMenu(e, node) {
                e.preventDefault();
                vm.selectedNode = storageTree.tree('getSelected');

                $rootScope.storageTreeDetails = false;
                if (vm.selectedNode.type == null) {
                    vm.menu1 = $("#contextMenu");
                    vm.menu1.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu1.show();
                    if (vm.menu2 != undefined) {
                        vm.menu2.contextmenu = null;
                        vm.menu2.hide();
                    } else if (vm.menu3 != undefined) {
                        vm.menu3.contextmenu = null;
                        vm.menu3.hide();
                    } else if (vm.menu4 != undefined) {
                        vm.menu4.contextmenu = null;
                        vm.menu4.hide();
                    } else if (vm.menu5 != undefined) {
                        vm.menu5.contextmenu = null;
                        vm.menu5.hide();
                    } else if (vm.menu6 != undefined) {
                        vm.menu6.contextmenu = null;
                        vm.menu6.hide();
                    } else if (vm.menu7 != undefined) {
                        vm.menu7.contextmenu = null;
                        vm.menu7.hide();
                    }
                    vm.menu1.on("click", "a", function () {
                        vm.menu1.hide();
                    });
                }

                if (vm.selectedNode.type == 'WAREHOUSE') {
                    $rootScope.storageCodeAndTypeValid = false;
                    vm.menu2 = $("#whcontextMenu");
                    vm.menu2.css({
                        left: e.pageX,
                        top: e.pageY
                    });

                    vm.menu2.show();
                    if (vm.menu3 != undefined) {
                        vm.menu3.contextmenu = null;
                        vm.menu3.hide();
                    } else if (vm.menu4 != undefined) {
                        vm.menu4.contextmenu = null;
                        vm.menu4.hide();
                    } else if (vm.menu5 != undefined) {
                        vm.menu5.contextmenu = null;
                        vm.menu5.hide();
                    } else if (vm.menu6 != undefined) {
                        vm.menu6.contextmenu = null;
                        vm.menu6.hide();
                    } else if (vm.menu7 != undefined) {
                        vm.menu7.contextmenu = null;
                        vm.menu7.hide();
                    }
                    vm.menu2.on("click", "a", function () {
                        vm.menu2.hide();
                    });
                    showHideDeleteOption(node, '#delWarehouse');
                }

                if (vm.selectedNode.type == 'STOCKROOM') {
                    $rootScope.storageCodeAndTypeValid = false;
                    vm.menu3 = $("#strcontextMenu");
                    vm.menu3.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu3.show();
                    if (vm.menu2 != undefined) {
                        vm.menu2.contextmenu = null;
                        vm.menu2.hide();
                    } else if (vm.menu4 != undefined) {
                        vm.menu4.contextmenu = null;
                        vm.menu4.hide();
                    } else if (vm.menu5 != undefined) {
                        vm.menu5.contextmenu = null;
                        vm.menu5.hide();
                    } else if (vm.menu6 != undefined) {
                        vm.menu6.contextmenu = null;
                        vm.menu6.hide();
                    } else if (vm.menu7 != undefined) {
                        vm.menu7.contextmenu = null;
                        vm.menu7.hide();
                    }
                    vm.menu3.on("click", "a", function () {
                        vm.menu3.hide();
                    });
                    showHideDeleteOption(node, '#delStockroom');
                }

                if (vm.selectedNode.type == 'AREA') {
                    $rootScope.storageCodeAndTypeValid = true;
                    vm.menu4 = $("#areacontextMenu");
                    vm.menu4.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu4.show();
                    if (vm.menu2 != undefined) {
                        vm.menu2.contextmenu = null;
                        vm.menu2.hide();
                    } else if (vm.menu3 != undefined) {
                        vm.menu3.contextmenu = null;
                        vm.menu3.hide();
                    } else if (vm.menu5 != undefined) {
                        vm.menu5.contextmenu = null;
                        vm.menu5.hide();
                    } else if (vm.menu6 != undefined) {
                        vm.menu6.contextmenu = null;
                        vm.menu6.hide();
                    } else if (vm.menu7 != undefined) {
                        vm.menu7.contextmenu = null;
                        vm.menu7.hide();
                    }
                    vm.menu4.on("click", "a", function () {
                        vm.menu4.hide();
                    });
                    showHideDeleteOption(node, '#delArea');
                }

                if (vm.selectedNode.type == 'RACK') {
                    $rootScope.storageCodeAndTypeValid = true;
                    vm.menu4 = $("#rackcontextMenu");
                    vm.menu4.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu4.show();
                    if (vm.menu2 != undefined) {
                        vm.menu2.contextmenu = null;
                        vm.menu2.hide();
                    } else if (vm.menu3 != undefined) {
                        vm.menu3.contextmenu = null;
                        vm.menu3.hide();
                    } else if (vm.menu5 != undefined) {
                        vm.menu5.contextmenu = null;
                        vm.menu5.hide();
                    } else if (vm.menu6 != undefined) {
                        vm.menu6.contextmenu = null;
                        vm.menu6.hide();
                    }
                    vm.menu4.on("click", "a", function () {
                        vm.menu4.hide();
                    });
                    showHideDeleteOption(node, '#delRack');
                }

                if (vm.selectedNode.type == 'SHELF') {
                    $rootScope.storageCodeAndTypeValid = true;
                    vm.menu5 = $("#shelfcontextMenu");
                    vm.menu5.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu5.show();
                    if (vm.menu2 != undefined) {
                        vm.menu2.contextmenu = null;
                        vm.menu2.hide();
                    } else if (vm.menu3 != undefined) {
                        vm.menu3.contextmenu = null;
                        vm.menu3.hide();
                    } else if (vm.menu4 != undefined) {
                        vm.menu4.contextmenu = null;
                        vm.menu4.hide();
                    } else if (vm.menu6 != undefined) {
                        vm.menu6.contextmenu = null;
                        vm.menu6.hide();
                    } else if (vm.menu7 != undefined) {
                        vm.menu7.contextmenu = null;
                        vm.menu7.hide();
                    }
                    vm.menu5.on("click", "a", function () {
                        vm.menu5.hide();
                    });
                    showHideDeleteOption(node, '#delShelf');
                }

                if (vm.selectedNode.type == 'BIN') {
                    $rootScope.storageCodeAndTypeValid = true;
                    vm.menu6 = $("#bincontextMenu");
                    vm.menu6.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu6.show();
                    if (vm.menu2 != undefined) {
                        vm.menu2.contextmenu = null;
                        vm.menu2.hide();
                    } else if (vm.menu3 != undefined) {
                        vm.menu3.contextmenu = null;
                        vm.menu3.hide();
                    } else if (vm.menu4 != undefined) {
                        vm.menu4.contextmenu = null;
                        vm.menu4.hide();
                    } else if (vm.menu5 != undefined) {
                        vm.menu5.contextmenu = null;
                        vm.menu5.hide();
                    } else if (vm.menu7 != undefined) {
                        vm.menu7.contextmenu = null;
                        vm.menu7.hide();
                    }
                    vm.menu6.on("click", "a", function () {
                        vm.menu6.hide();
                    });
                    showHideDeleteOption(node, '#delBin');
                }
            }

            function showHideCreateOptions(node) {
                if (node.type == "ONHOLD") {
                    $("#newWarehouse").hide();
                    $("#newStockRoom").hide();
                    $("#newReturnStorage").hide();
                    $("#newOnHoldStorage").show();
                } else if (node.type == "RETURN") {
                    $("#newWarehouse").hide();
                    $("#newStockRoom").hide();
                    $("#newOnHoldStorage").hide();
                    $("#newReturnStorage").show();
                } else {
                    $("#newReturnStorage").hide();
                    $("#newOnHoldStorage").hide();
                }
            }

            function showHideDeleteOption(node, type) {
                if (node.children == undefined || node.children.length == 0) {
                    $(type).show();
                } else {
                    $(type).hide();
                }

                if (node.type == "WAREHOUSE") {
                    if (node.object.isLeafNode) {
                        $("#whStockRoom").hide();
                        $("#whArea").hide();
                        $("#whRack").hide();
                    } else {
                        $("#whStockRoom").show();
                        $("#whArea").show();
                        $("#whRack").show();
                    }

                    vm.deleteWarehouseTitle = "Delete Warehouse";

                    /*if (node.object.onHold) {
                     vm.deleteWarehouseTitle = "Delete On Hold Store";
                     } else if (node.object.returned) {
                     vm.deleteWarehouseTitle = "Delete Return Store";
                     } else {
                     vm.deleteWarehouseTitle = "Delete Warehouse";
                     }*/

                    $scope.$apply();
                } else if (node.type == "STOCKROOM") {
                    if (node.object.isLeafNode) {
                        $("#strArea").hide();
                        $("#strRack").hide();
                        $("#strBin").hide();
                    } else {
                        $("#strArea").show();
                        $("#strRack").show();
                        $("#strBin").show();
                    }

                } else if (node.type == "AREA") {
                    if (node.object.isLeafNode) {
                        $("#subArea").hide();
                        $("#arRack").hide();
                        $("#arBin").hide();
                    } else {
                        $("#subArea").show();
                        $("#arRack").show();
                        $("#arBin").show();
                    }
                } else if (node.type == "RACK") {
                    if (node.object.isLeafNode) {
                        $("#rcShelf").hide();
                    } else {
                        $("#rcShelf").show();
                    }
                } else if (node.type == "SHELF") {
                    if (node.object.isLeafNode) {
                        $("#shBin").hide();
                    } else {
                        $("#shBin").show();
                    }
                }
            }

            /*function onDblClick(node) {
             var data = storageTree.tree('getData', node.target);
             if (data.id != 0) {
             storageTree.tree('beginEdit', node.target);
             }

             $timeout(function () {
             $('.tree-editor').focus().select();
             }, 1);
             }*/

            vm.searchValue = null;

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#storageTree').tree('expandAll');
                }
                $('#storageTree').tree('doFilter', vm.searchValue);
            }

            vm.validateStorage = validateStorage;

            function validateStorage() {
                var options = {
                    title: 'Validate Storage',
                    template: 'app/desktop/modules/storage/details/validateStorageView.jsp',
                    controller: 'ValidateStorageController as validateStorageVm',
                    resolve: 'app/desktop/modules/storage/details/validateStorageController',
                    width: 700,
                    data: {
                        storageMode: "VALIDATE"
                    },
                    buttons: [],
                    callback: function (result) {

                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.transferStorageParts = transferStorageParts;
            function transferStorageParts() {
                var options = {
                    title: 'Transfer Storage Parts',
                    template: 'app/desktop/modules/storage/details/validateStorageView.jsp',
                    controller: 'ValidateStorageController as validateStorageVm',
                    resolve: 'app/desktop/modules/storage/details/validateStorageController',
                    width: 700,
                    data: {
                        storageMode: "TRANSFER",
                        selectedFromStorage: vm.lastSelectedStorage.object
                    },
                    buttons: [
                        {text: 'Transfer', broadcast: 'app.storage.transfer'}
                    ],
                    callback: function (result) {
                        $rootScope.showSuccessMessage("Parts transferred successfully");
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.collapseAll = collapseAll;
            vm.expandAll = expandAll;
            function collapseAll() {
                var node = $('#storageTree').tree('getSelected');
                if (node) {
                    $('#storageTree').tree('collapseAll', node.target);
                }
                else {
                    $('#storageTree').tree('collapseAll');
                }
            }

            function expandAll() {
                var node = $('#storageTree').tree('getSelected');
                if (node) {
                    $('#storageTree').tree('expandAll', node.target);
                }
                else {
                    $('#storageTree').tree('expandAll');
                }
            }

            $rootScope.storageBOMs = [];
            function loadBoms() {
                BomService.getAllBoms().then(
                    function (data) {
                        $rootScope.storageBOMs = data;
                    }
                )
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    initStorageTree();
                    loadStorageTree();
                    loadBoms();
                    $rootScope.$on('app.storage.update', update);
                });
            })();
        }
    }
);