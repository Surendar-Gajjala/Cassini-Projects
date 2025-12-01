define(['app/app.modules',
        'app/components/prod/dashboard/widgets/productionWidgetFactory',
        'app/shared/constants/constants',
        'app/components/prod/product/select/productSelectionController',
        'app/shared/services/cachingService',
        'app/components/prod/product/productFactory',
        'app/components/prod/materialpo/select/materialSelectionController',
        'app/components/prod/material/materialFactory'
    ],
    function ($app) {
        $app.controller('BomController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal','cachingService','materialFactory', 'productFactory', 'productionWidgetFactory', 'CONSTANTS',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal,cachingService,materialFactory, productFactory, productionWidgetFactory, CONSTANTS) {

                     var  selectedBom = {},
                        productClassificationList = [],
                        init = function () {
                            loadProductTypes();
                            getBomTypes();
                            getBoms();
                            getProductsClassification();
                        }, getBoms = function() {
                            productionWidgetFactory.getBoms(false, $scope.pageable).then(
                                function (data) {

                                    $scope.bomList = data;
                                    $scope.loading = false;


                                    angular.forEach($scope.bomList, function (item) {
                                        var p = angular.copy(item);
                                        item.bom = p;
                                        item.obj=item.bomObj;
                                        item.editMode = false;
                                        item.showDetails = false;
                                        item.detailsOpen = false;

                                        $scope.changeTypeObj(item);
                                    });
                                },

                                function (error) {

                                }
                            );
                        },
                        getProductsClassification = function() {
                            productFactory.getProductsClassification().then (
                                function(data) {
                                    productClassificationList = data;
                                },
                                function(error) {
                                    console.error(error);
                                }
                            );
                        },
                        loadProductTypes=function() {
                            productFactory.getProductTypes().then(
                                function(data) {
                                    $scope.productTypes = data;
                                }
                            )
                        },

                        getBomTypes = function () {
                            productionWidgetFactory.getBomTypes().then(
                                function (results) {
                                    $scope.bomTypeList=results;
                                },

                                function (error) {

                                }
                            );
                        },
                        updateChanges = function (currentObj) {
                            if(currentObj.type == 'TYPE' || currentObj.type == 'CATEGORY'){
                                if(currentObj.obj!=null && currentObj.obj.id!=undefined){
                                    currentObj.objId = currentObj.obj.id;
                                }

                            }

                            productionWidgetFactory.createBom(currentObj).then(
                                function (response) {
                                    currentObj = response;

                                    currentObj.editMode=false;

                                    getBoms();
                                    //  $scope.changeTypeObj(currentObj);
                                },

                                function (error) {

                                }
                            );
                        },
                        deleteChanges = function (currentObj) {
                            productionWidgetFactory.deleteBom(currentObj).then(
                                function (response) {
                                    getBoms();
                                },

                                function (error) {

                                }
                            );
                        };

                    $scope.productTypes = [];
                    $scope.bomTobomItem={};
                    $scope.nodeId = 0;
                    $scope.categoriesTreeRoot = {
                        id: 0,
                        text: "<b>Categories</b>",
                        iconCls: "product-categories-root-node",
                        attributes: {
                            catId: 0
                        },
                        children: []
                    };
                    $scope.lastSelectedBom = null;
                    $scope.bomDetailsRow = {
                        showDetails: false
                    };

                    $scope.toggleBomDetails=function(bom){

                        getBomItems(bom.bomId);
                        $scope.bomTobomItem=angular.copy(bom);
                        if($scope.lastSelectedBom != bom) {
                            if($scope.lastSelectedBom != null){
                                $scope.lastSelectedBom.detailsOpen = false;
                            }

                            bom.detailsOpen = true;
                        }

                        var index = $scope.bomList.indexOf($scope.bomDetailsRow);
                        if(index != -1) {
                            $scope.bomDetailsRow.showDetails = false;
                            $scope.bomList.splice(index, 1);
                        }

                        if($scope.lastSelectedBom == null || $scope.lastSelectedBom != bom ||
                            $scope.lastSelectedBom.detailsOpen == false) {
                            $scope.bomDetailsRow.showDetails = true;
                            index = $scope.bomList.indexOf(bom);
                            $scope.bomList.splice(index + 1, 0, $scope.bomDetailsRow);
                        }

                        if($scope.lastSelectedBom != null && $scope.lastSelectedBom != bom) {
                            $scope.lastSelectedBom = false;
                        }
                        else if ($scope.lastSelectedBom != null) {
                            $scope.lastSelectedBom.detailsOpen = !$scope.lastSelectedBom.detailsOpen;
                        }

                        $scope.lastSelectedBom = bom;
                    };



                    var selectedBomItem = {},
                        materialClassificationList = [],
                        initBomItems = function () {
                            loadMaterialTypes();
                            getBomItemTypes();
                          //  getBomItems();
                            getMaterialsClassification();
                        },
                        getBomItems = function(bomId) {
                            productionWidgetFactory.getBomItems(bomId).then(
                                function (data) {

                                    $scope.bomItemList = data;
                                    $scope.loading = false;


                                    angular.forEach($scope.bomItemList, function (item) {
                                        var p = angular.copy(item);
                                        item.bomItem = p;
                                        item.obj=item.bomItemObj;
                                        item.quantity=item.quantity;
                                        item.editMode = false;
                                        item.showDetails = false;
                                        item.detailsOpen = false;

                                        $scope.changeBomItemTypeObj(item);
                                    });

                                },

                                function (error) {

                                }
                            );
                        },
                        getMaterialsClassification = function() {
                            materialFactory.getMaterialsClassification().then (
                                function(data) {
                                    materialClassificationList = data;
                                },
                                function(error) {
                                    console.error(error);
                                }
                            );
                        },
                        loadMaterialTypes=function() {
                            materialFactory.getMaterialTypes().then(
                                function(data) {
                                    $scope.materialTypes = data;
                                }
                            )
                        },

                        getBomItemTypes = function () {
                            productionWidgetFactory.getBomItemTypes().then(
                                function (results) {
                                    $scope.bomItemTypeList=results;
                                },

                                function (error) {

                                }
                            );
                        },
                        updateBomItemChanges = function (currentObj) {
                            if(currentObj.itemType == 'TYPE' || currentObj.itemType == 'CATEGORY'){
                                if(currentObj.obj!=null && currentObj.obj.id!=undefined){
                                    currentObj.itemId = currentObj.obj.id;
                                }

                            }
                            currentObj.bom=$scope.bomTobomItem;

                            productionWidgetFactory.createBomItem(currentObj).then(
                                function (response) {
                                    currentObj = response;

                                    currentObj.editMode=false;

                                    currentObj.bom=$scope.bomTobomItem;
                                    getBomItems(currentObj.bom.bomId);
                                    //  $scope.changeTypeObj(currentObj);
                                },

                                function (error) {

                                }
                            );
                        },
                        deleteBomItemChanges = function (currentObj) {
                            productionWidgetFactory.deleteBomItem(currentObj).then(
                                function (response) {
                                    getBomItems(currentObj.bom.bomId);
                                },

                                function (error) {

                                }
                            );
                        };

                    $scope.materialTypes = [];

                    $scope.nodeMatId = 0;
                    $scope.categoriesBomItemTreeRoot = {
                        id: 0,
                        text: "<b>Categories</b>",
                        iconCls: "product-categories-root-node",
                        attributes: {
                            catId: 0
                        },
                        children: []
                    };
                     $scope.changeBomItemTypeObj=function(bomItem,index){

                        if(bomItem.editMode==true) {

                            if (bomItem.itemType == 'TYPE') {
                                bomItem.hideMaterialTypeMode = false;
                                bomItem.showMaterialTypeMode = true;
                                bomItem.hideMaterialCatMode = false;
                                bomItem.showMaterialCatMode = false;
                                bomItem.hideMaterialMode = false;
                                bomItem.showMaterialMode = false;
                            } else if (bomItem.itemType == 'CATEGORY') {
                                bomItem.hideMaterialTypeMode = false;
                                bomItem.showMaterialTypeMode = false;
                                bomItem.hideMaterialCatMode = false;
                                bomItem.showMaterialCatMode = true;
                                bomItem.hideMaterialMode = false;
                                bomItem.showMaterialMode = false;
                                createBomItemTreeNodes(bomItem,index);
                            } else if (bomItem.itemType == 'MATERIAL') {
                                bomItem.hideMaterialTypeMode = false;
                                bomItem.showMaterialTypeMode = false;
                                bomItem.hideMaterialCatMode = false;
                                bomItem.showMaterialCatMode = false;
                                bomItem.hideMaterialMode = false;
                                bomItem.showMaterialMode = true;
                            }
                        }else if(bomItem.editMode==false){

                            if (bomItem.itemType == 'TYPE') {
                                bomItem.hideMaterialTypeMode = true;
                                bomItem.showMaterialTypeMode = false;
                                bomItem.hideMaterialCatMode = false;
                                bomItem.showMaterialCatMode = false;
                                bomItem.hideMaterialMode = false;
                                bomItem.showMaterialMode = false;
                            } else if (bomItem.itemType == 'CATEGORY') {
                                bomItem.hideMaterialTypeMode = false;
                                bomItem.showMaterialTypeMode = false;
                                bomItem.hideMaterialCatMode = true;
                                bomItem.showMaterialCatMode = false;
                                bomItem.hideMaterialMode = false;
                                bomItem.showMaterialMode = false;

                            } else if (bomItem.itemType == 'MATERIAL') {
                                bomItem.hideMaterialTypeMode = false;
                                bomItem.showMaterialTypeMode = false;
                                bomItem.hideMaterialCatMode = false;
                                bomItem.showMaterialCatMode = false;
                                bomItem.hideMaterialMode = true;
                                bomItem.showMaterialMode = false;

                            }
                        }

                    };


                    createBomItemTreeNodes = function(bomItem,index) {

                        $('#materialCategoriesTree'+index).tree({
                            data: [$scope.categoriesTreeRoot],

                            onSelect: function (node) {
                                window.$("body").trigger("click");
                                bomItem.obj = node.attributes.category;
                                $scope.$apply();
                            }
                        });

                        var bomItemCategories = materialClassificationList;
                        var bomItemRoots = [bomItemCategories.length];

                        for(var i=0; i<bomItemCategories.length; i++) {
                            var category = bomItemCategories[i];

                            bomItemRoots[i] = bomItemBuildNode(category);

                            if(category.children.length > 0) {
                                bomItemTraverseChildren(category, bomItemRoots[i]);
                            }
                        }

                        var bomItemRootNode = $('#materialCategoriesTree'+index).tree('find', 0);

                        if (bomItemRootNode != null && bomItemRoots.length > 0) {
                            $('#materialCategoriesTree'+index).tree('append', {
                                parent: bomItemRootNode.target,
                                data: bomItemRoots
                            });
                        }
                    };

                    bomItemBuildNode = function(category) {
                        var state = "closed";
                        if(category.children.length == 0) {
                            state = "open";
                        }

                        state = "open";

                        return {
                            id: $scope.nodeMatId++,
                            text: category.name,
                            state: state,
                            iconCls: "product-categories-category-node",
                            attributes: {
                                catId: category.id,
                                category: category
                            }
                        };
                    };

                    bomItemTraverseChildren = function(category, parentNode) {
                        var nodes = [category.children.length];

                        for(var i=0; i<category.children.length; i++) {
                            var cat = category.children[i];
                            nodes[i] = bomItemBuildNode(cat);

                            if(cat.children.length > 0) {
                                bomItemTraverseChildren(cat, nodes[i]);
                            }
                        }


                        parentNode.children = nodes;
                    };

                    $scope.pageable = {
                        page: 1,
                        size: 5,
                        sort: {
                            label: "Name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.paginationObj = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    },
                        $scope.materialTypes=[];
                    $scope.bomItemList = [];
                    $scope.bomItemTypeList = [];
                    $scope.newBomItem={
                        quantity:0,
                        itemType:'',
                        itemId:null,
                        bom:null,
                        editMode:true,
                        hideMaterialMode:false,
                        showMaterialMode:false,
                        hideMaterialCatMode:false,
                        hideMaterialCatMode:false,
                        hideMaterialTypeMode:true,
                        showMaterialTypeMode:true

                    };

                    $scope.constants = CONSTANTS;

                    $scope.addNewBomItem = function () {

                        var temp=angular.copy($scope.newBomItem);
                        $scope.bomItemList.unshift(temp);
                    };

                    $scope.bomItemHideEditMode = function (selectedObj,index) {
                        if(selectedObj.rowId == undefined) {
                            $scope.bomItemList.splice(index,1);
                        }else {
                            selectedObj.itemType = selectedBomItem.itemType;
                            selectedObj.obj = selectedBomItem.bomItemObj;
                            selectedObj.quantity = selectedBomItem.quantity;
                            selectedObj.editMode = false;
                            selectedBomItem = {};

                            $scope.changeBomItemTypeObj(selectedObj);
                        }

                    };

                    $rootScope.selectMaterial = function(currentObj) {

                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/materialpo/select/materialSelectionDialog.jsp',
                            controller: 'MaterialSelectionController',
                            size: 'lg',
                            resolve: {
                                selectType: function () {
                                    return "radio";
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedMaterials) {
                                angular.forEach(selectedMaterials, function(mat) {
                                    var item = {
                                        material: mat,
                                        quantity: 0,
                                        editMode:true
                                    };

                                    /*  $scope.bomItem.obj=item.material.name;
                                     $scope.bomItem.objId=item.material.id;
                                     $scope.bomItem.hideMaterialMode=true;
                                     $scope.bomItem.showMaterialMode=false;*/
                                    currentObj.obj=item.material.name;
                                    currentObj.itemId=item.material.id;
                                    currentObj.hideMaterialMode=true;
                                    currentObj.showMaterialMode=false;

                                });
                            },
                            function () {

                            }
                        );
                    };

                    $scope.bomItemAcceptChanges = function (selectedObj) {
                        $timeout(function () {
                            selectedObj.editMode = false;
                        }, 500);
                        updateBomItemChanges(selectedObj);
                    };

                    $scope.bomItemRemoveItem = function (index, bomItem) {
                        $scope.bomItemList.splice(index, 1);
                        deleteBomItemChanges(bomItem);
                    };

                    $scope.bomItemShowEditMode = function (bomItem) {
                        selectedBomItem = angular.copy(bomItem);
                        $scope.bomItem = bomItem;
                        /*    productionWidgetFactory.getWorkCenters(false).then(
                         function (data) {
                         $scope.workcenterList = data;
                         },
                         function (error) {

                         }
                         );
                         */
                        bomItem.editMode = true;

                    };

                    $scope.pageChanged = function () {
                     //   getBomItems();
                    };

                    initBomItems();


                    $scope.changeTypeObj=function(bom,index){

                        if(bom.editMode==true) {

                            if (bom.type == 'TYPE') {
                                bom.hideProductTypeMode = false;
                                bom.showProductTypeMode = true;
                                bom.hideProductCatMode = false;
                                bom.showProductCatMode = false;
                                bom.hideProductMode = false;
                                bom.showProductMode = false;
                            } else if (bom.type == 'CATEGORY') {
                                bom.hideProductTypeMode = false;
                                bom.showProductTypeMode = false;
                                bom.hideProductCatMode = false;
                                bom.showProductCatMode = true;
                                bom.hideProductMode = false;
                                bom.showProductMode = false;
                                createTreeNodes(bom,index);
                            } else if (bom.type == 'PRODUCT') {
                                bom.hideProductTypeMode = false;
                                bom.showProductTypeMode = false;
                                bom.hideProductCatMode = false;
                                bom.showProductCatMode = false;
                                bom.hideProductMode = false;
                                bom.showProductMode = true;
                            }
                        }else if(bom.editMode==false){

                            if (bom.type == 'TYPE') {
                                bom.hideProductTypeMode = true;
                                bom.showProductTypeMode = false;
                                bom.hideProductCatMode = false;
                                bom.showProductCatMode = false;
                                bom.hideProductMode = false;
                                bom.showProductMode = false;
                            } else if (bom.type == 'CATEGORY') {
                                bom.hideProductTypeMode = false;
                                bom.showProductTypeMode = false;
                                bom.hideProductCatMode = true;
                                bom.showProductCatMode = false;
                                bom.hideProductMode = false;
                                bom.showProductMode = false;

                            } else if (bom.type == 'PRODUCT') {
                                bom.hideProductTypeMode = false;
                                bom.showProductTypeMode = false;
                                bom.hideProductCatMode = false;
                                bom.showProductCatMode = false;
                                bom.hideProductMode = true;
                                bom.showProductMode = false;

                            }
                        }

                    };


                    createTreeNodes = function(bom,index) {

                        $('#productCategoriesTree'+index).tree({
                            data: [$scope.categoriesTreeRoot],

                            onSelect: function (node) {
                                window.$("body").trigger("click");
                                bom.obj = node.attributes.category;
                                $scope.$apply();
                            }
                        });

                        var categories = productClassificationList;
                        var roots = [categories.length];

                        for(var i=0; i<categories.length; i++) {
                            var category = categories[i];

                            roots[i] = buildNode(category);

                            if(category.children.length > 0) {
                                traverseChildren(category, roots[i]);
                            }
                        }

                        var rootNode = $('#productCategoriesTree'+index).tree('find', 0);

                        if (rootNode != null && roots.length > 0) {
                            $('#productCategoriesTree'+index).tree('append', {
                                parent: rootNode.target,
                                data: roots
                            });
                        }
                    };

                    buildNode = function(category) {
                        var state = "closed";
                        if(category.children.length == 0) {
                            state = "open";
                        }

                        state = "open";

                        return {
                            id: $scope.nodeId++,
                            text: category.name,
                            state: state,
                            iconCls: "product-categories-category-node",
                            attributes: {
                                catId: category.id,
                                category: category
                            }
                        };
                    };

                    traverseChildren = function(category, parentNode) {
                        var nodes = [category.children.length];

                        for(var i=0; i<category.children.length; i++) {
                            var cat = category.children[i];
                            nodes[i] = buildNode(cat);

                            if(cat.children.length > 0) {
                                traverseChildren(cat, nodes[i]);
                            }
                        }


                        parentNode.children = nodes;
                    };

                    $scope.pageable = {
                        page: 1,
                        size: 5,
                        sort: {
                            label: "Name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.productTypes=[];
                    $scope.bomList = $scope.paginationObj;
                    $scope.bomTypeList = [];
                    $scope.newBom={
                        name:'',
                        description:'',
                        type:'',
                        obj:null,
                        editMode:true,
                        hideProductMode:false,
                        showProductMode:false,
                        hideProductCatMode:false,
                        hideProductCatMode:false,
                        hideProductTypeMode:true,
                        showProductTypeMode:true

                    };

                    $scope.constants = CONSTANTS;

                    $scope.addNewBom = function () {

                        var temp=angular.copy($scope.newBom);
                        $scope.bomList.unshift(temp);
                    };

                    $scope.hideEditMode = function (selectedObj,index) {
                        if(selectedObj.bomId == undefined) {
                            $scope.bomList.splice(index, 1);
                        }else {
                            selectedObj.name = selectedBom.name;
                            selectedObj.description = selectedBom.description;
                            selectedObj.type = selectedBom.type;
                            selectedObj.obj = selectedBom.bomObj;
                            selectedObj.editMode = false;
                            selectedBom = {};
                            $scope.changeTypeObj(selectedObj);
                        }

                    };

                    $rootScope.selectProduct = function(currentObj) {

                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/product/select/productSelectionDialog.jsp',
                            controller: 'ProductSelectionController',
                            size: 'lg',
                            resolve: {
                                selectType: function () {
                                    return "radio";
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedProducts) {
                                angular.forEach(selectedProducts, function(prod) {
                                    var item = {
                                        product: prod,
                                        quantity: 0,
                                        editMode:true
                                    };

                                    /*  $scope.bom.obj=item.product.name;
                                     $scope.bom.objId=item.product.id;
                                     $scope.bom.hideProductMode=true;
                                     $scope.bom.showProductMode=false;*/
                                    currentObj.obj=item.product.name;
                                    currentObj.objId=item.product.id;
                                    currentObj.hideProductMode=true;
                                    currentObj.showProductMode=false;

                                });
                            },
                            function () {

                            }
                        );
                    };

                    $scope.acceptChanges = function (selectedObj) {
                        $timeout(function () {
                            selectedObj.editMode = false;
                        }, 500);
                        updateChanges(selectedObj);
                    };

                    $scope.removeItem = function (index, bom) {
                        $scope.bomList.splice(index, 1);
                        deleteChanges(bom);
                    };

                    $scope.showEditMode = function (bom) {
                        selectedBom = angular.copy(bom);
                        $scope.bom = bom;
                        /*    productionWidgetFactory.getWorkCenters(false).then(
                         function (data) {
                         $scope.workcenterList = data;
                         },
                         function (error) {

                         }
                         );
                         */
                        bom.editMode = true;

                    };

                    $scope.pageChanged = function () {
                        getBoms();
                    };

                    init();
                }
            ]
        );
    }
);