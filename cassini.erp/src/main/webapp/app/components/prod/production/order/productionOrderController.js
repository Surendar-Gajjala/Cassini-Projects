define(['app/app.modules', 'app/components/prod/production/order/productionOrderFactory',
        'app/components/prod/product/select/productSelectionController','app/components/prod/dashboard/widgets/productionWidgetFactory'],
    function($app) {
        $app.controller('ProductionOrderController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state','$stateParams', '$cookies','$modal','productionWidgetFactory', 'productionOrderFactory',

                function($scope, $rootScope, $timeout, $interval, $state,$stateParams, $cookies,$modal, productionWidgetFactory,productionOrderFactory) {

                    $rootScope.iconClass = "fa flaticon-plan2";
                    $rootScope.viewTitle = "Production Orders";
                    $rootScope.status = "NEW";
                    $rootScope.orderTotal = 0;

                    var templates = {
                        orderDetails: "app/components/prod/production/order/templates/orderDetails.jsp",
                        bomItems: "app/components/prod/production/order/templates/bomItems.jsp"
                    };
                    $scope.template = "";
                    $rootScope.onEditOrder=false;
                    $rootScope.saveMode=false;
                    $rootScope.onViewOrder=false;
                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 10,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
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

                    $scope.prodOrder = {
                        id:null,
                        details:[]
                    };

                    $scope.bomsList = [];
                    $scope.processList=[];
                    $scope.prodOrderItems = $scope.emptyPagedResults;

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('productionorders-view-tb');
                    });

                    function loadProcessList(){

                        productionWidgetFactory.getProcesses(false,null).then (
                            function(data) {
                                $scope.processList = data;
                            },
                            function (error) {

                            }
                        );

                    }

                    function loadBomlist() {
                        productionWidgetFactory.getBoms().then (
                            function(data) {
                                $scope.bomsList = data;
                            },
                            function (error) {

                            }
                        );
                    }
                    function loadProductionOrderDetails(orderId) {
                        productionOrderFactory.getProductionOrder(orderId).then(
                            function(data) {
                                var total = 0;

                                $rootScope.viewTitle = "Production Orders ( "+ data.orderNumber+" )";
                                $scope.template = templates.bomItems;
                                $scope.prodOrder = data;
                                $scope.prodOrderItems.content = data.details;
                                $rootScope.status = $scope.prodOrder.status;

                                angular.forEach($scope.prodOrderItems.content, function(bomitem) {

                                    angular.forEach(bomitem.itemBoms, function (custBomItem) {
                                        total += custBomItem.bomItem.quantity * bomitem.quantity*custBomItem.material.unitPrice;
                                    });

                                });

                                $rootScope.orderTotal = total;
                            }
                        )
                    };

                var selectedOrderItem = {};
                    $rootScope.createProductionOrder=function(){


                        angular.forEach($scope.prodOrderItems.content, function(bomitem) {

                            angular.forEach(bomitem.itemBoms, function (custBomItem) {
                                custBomItem.quantity = custBomItem.bomItem.quantity * bomitem.quantity;
                                custBomItem.unitPrice = custBomItem.material.unitPrice;
                                custBomItem.itemTotal = custBomItem.bomItem.quantity * bomitem.quantity*custBomItem.material.unitPrice;
                            });

                        });
                        $scope.prodOrder.details=$scope.prodOrderItems.content;
                        $scope.prodOrder.orderedBy=$app.login.person.id;
                        $scope.prodOrder.orderedDate=null;
                        productionOrderFactory.createProductionOrder($scope.prodOrder).then(

                            function(data){
                                $state.go('app.prod.production');
                            }
                        )

                    };

                    $rootScope.saveProductionOrder=function(status){

                        angular.forEach($scope.prodOrderItems.content, function(bomitem) {

                            angular.forEach(bomitem.itemBoms, function (custBomItem) {
                                custBomItem.quantity = custBomItem.bomItem.quantity * bomitem.quantity;
                                custBomItem.unitPrice = custBomItem.material.unitPrice;
                                custBomItem.itemTotal = custBomItem.bomItem.quantity * bomitem.quantity*custBomItem.material.unitPrice;
                            });

                        });
                        $scope.prodOrder.details=$scope.prodOrderItems.content;
                       $scope.prodOrder.orderedBy=$app.login.person.id;
                        $scope.prodOrder.orderedDate=null;

                        $scope.prodOrder.status = status;
                        switch(status){

                            case 'CREATED':
                                $scope.prodOrder.orderedBy=$app.login.person.id;
                                break;
                            case 'APPROVED':
                                $scope.prodOrder.approvedBy=$app.login.person.id;
                                break;
                            case 'INPRODUCTION':
                                $scope.prodOrder.startedBy=$app.login.person.id;
                                break;
                            case 'COMPLETED':
                                $scope.prodOrder.completedBy=$app.login.person.id;
                                break;
                        }
                        productionOrderFactory.updateProductionOrder($scope.prodOrder).then(

                            function(data){
                                $state.go('app.prod.production');
                                $rootScope.showSuccessMessage("Production Order Saved Successfully");
                            } ,
                            function (error) {
                                $state.go('app.prod.production');
                            }
                        )

                    };
                    $scope.changeTypeObj=function(customBom,index){
                        customBom.material=customBom.material;

                    };

                    $rootScope.addProducts = function() {

                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/product/select/productSelectionDialog.jsp',
                            controller: 'ProductSelectionController',
                            size: 'lg',
                            resolve: {
                                selectType: function () {
                                    return "check";
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedProducts) {
                                $rootScope.status = "ADDITEMS";
                                angular.forEach(selectedProducts, function(prod,key) {
                                    var isCorrect = false;

                                    var item = {
                                        product: prod,
                                        quantity: 0,
                                        editMode:true,
                                        bom:''
                                    };


                                    for(var i = 0; i < $scope.bomsList.length; i++) {
                                        if($scope.bomsList[i].type == 'PRODUCT' && $scope.bomsList[i].objId == prod.id) {
                                            item = {
                                                product: prod,
                                                quantity: 0,
                                                editMode:true,
                                                bom:$scope.bomsList[i]
                                            };
                                            isCorrect = true;
                                            break;
                                        }
                                    }

                                    if(!isCorrect) {
                                        for(var i = 0; i < $scope.bomsList.length; i++) {
                                            if($scope.bomsList[i].type == 'CATEGORY' && $scope.bomsList[i].objId == prod.category.id) {
                                                item = {
                                                    product: prod,
                                                    quantity: 0,
                                                    editMode:true,
                                                    bom:$scope.bomsList[i]
                                                };
                                                isCorrect = true;
                                                break;
                                            }
                                        }
                                    }

                                    if(!isCorrect) {
                                        for(var i = 0; i < $scope.bomsList.length; i++) {
                                            if($scope.bomsList[i].type == 'TYPE' && $scope.bomsList[i].objId == prod.type) {
                                                item = {
                                                    product: prod,
                                                    quantity: 0,
                                                    editMode:true,
                                                    bom:$scope.bomsList[i]
                                                };
                                                isCorrect = true;
                                                break;
                                            }
                                        }
                                    }

                                    $scope.prodOrderItems.content.push(item);
                                });
                            },
                            function () {

                            }
                        );
                    };
                    $scope.hideEditMode = function (selectedObj) {
                        selectedObj.sku = selectedOrderItem.product.sku;
                        selectedObj.name = selectedOrderItem.product.name;
                        selectedObj.quantity = selectedOrderItem.quantity;
                        selectedObj.editMode = false;
                        selectedOrderItem = {};
                    };

                    $scope.acceptChanges = function(selectedObj) {
                        $timeout(function() {
                            selectedObj.editMode = false;
                        }, 500);
                        //  updateChanges(selectedObj);
                    };

                    $scope.removeItem = function(index,orderItem) {
                        $scope.prodOrderItems.content.splice(index,1);
                        //  deleteChanges(workcenter);
                    };

                    $scope.showEditMode = function(orderItem) {
                        selectedOrderItem = angular.copy(orderItem);
                        $scope.orderItem = orderItem;
                        orderItem.editMode=true;

                        productionWidgetFactory.getProcesses(false,null).then (
                            function(data) {
                                $scope.processList=data;
                            },
                            function (error) {

                            }
                        );


                    };

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.prodOrderItems.content = [];
                    };

                    $rootScope.editItems=function(){
                        $rootScope.onEditOrder=true;
                        $rootScope.onViewOrder=false;
                        $rootScope.status=='ADDITEMS';
                        $scope.template = templates.orderDetails;
                        angular.forEach($scope.prodOrderItems.content, function(prod) {

                         prod.editMode=true;
                         prod.oldBom=prod.bom;
                            //var item = {
                            //    product: prod,
                            //    quantity: 0,
                            //    editMode:true,
                            //    bom:''
                            //};

                            //$state.go('app.prod.productionorder',{createDisplay:'edit',orderId: $scope.prodOrder.id })
                    });

                      //  $scope.prodOrderItems.content=$scope.prodOrder.details;

                    };


                    $rootScope.addItems = function() {
                        for (var i = 0; i < $scope.prodOrderItems.content.length; i++) {
                            if($scope.prodOrderItems.content[i].bom == "" || $scope.prodOrderItems.content[i].process == undefined) {
                                alert("Select Process and Bom details.");
                                return false;
                            }
                        }
                        $rootScope.status = "CREATORDER";
                        if($rootScope.onEditOrder==true){

                            $rootScope.status='CREATED';
                        }

                        productionOrderFactory.getCustomBomItems($scope.prodOrderItems.content).then(
                            function (data) {

                                $scope.template = templates.bomItems;
                                $scope.prodOrderItems.content = data;
                                $rootScope.onEditOrder=false;

                                $rootScope.saveMode=true;
                          }
                        )
                    };


                    (function() {
                        $scope.template = templates.orderDetails;
                        loadProcessList();
                        loadBomlist();
                        if($stateParams.createDisplay!=null && $stateParams.createDisplay!=undefined){

                            if($stateParams.createDisplay=="display"){
                                var orderId=$stateParams.orderId;
                                loadProductionOrderDetails(orderId);
                                $rootScope.onViewOrder=true;
                            }else{
                                $scope.prodOrderItems = $scope.emptyPagedResults;
                                $rootScope.onViewOrder=false;
                            }
                        }

                    })();
                }
            ]
        );
    }
);


