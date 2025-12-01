define(
    [
        'app/desktop/modules/items/item.module',
        'moment',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService'
    ],
    function (module) {
        module.controller('ItemAttributesController', ItemAttributesController);

        function ItemAttributesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                          ItemTypeService, ItemService) {
            var vm = this;

            vm.itemId = $stateParams.itemId;
            vm.item = null;
            vm.attributes = [];
            vm.flags = ['TRUE', 'FALSE'];
            vm.saveItemAttribute = saveItemAttribute;
            $scope.opened = {};
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            $scope.open = function ($event, elementOpened) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened[elementOpened] = !$scope.opened[elementOpened];
            };

            $scope.$on('attributes', function (event, arg) {
                vm.attributes = arg;
            });

            function loadItem() {
                vm.loading = true;
                ItemService.getItem(vm.itemId).then(
                    function (data) {
                        vm.item = data;
                        loadAttributeDefs();
                    }
                )
            }

            function loadAttributeDefs() {
                ItemTypeService.getAttributesWithHierarchy(vm.item.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.item.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.item.id,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
                                    listValue: [],
                                    newListValue: null,
                                    listValueEditMode: false
                                    /*listValue: null*/
                                }
                            };
                            vm.attributes.push(att);
                        });

                        loadAttributes();
                    }
                )
            }

            function loadAttributes() {
                ItemService.getItemAttributes(vm.itemId).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.attributes, function (attribute) {
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                attribute.value.doubleValue = value.doubleValue;
                                if(value.booleanValue!=undefined || value.booleanValue!=null){

                                    if(value.booleanValue==false){
                                        attribute.value.booleanValue='FALSE';
                                    }else if(value.booleanValue==true){
                                        attribute.value.booleanValue='TRUE';
                                    }

                                }
                              //  attribute.value.booleanValue = value.booleanValue;
                                attribute.value.dateValue = value.dateValue;
                                attribute.value.listValue = value.listValue;
                            }
                        });
                    }
                )
            }

            function isInteger(x) {
                return x % 1 === 0;
            }

            function saveItemAttribute(attribute) {
                var valid = true;
                if (!isInteger(attribute.value.integerValue)) {
                    if (typeof attribute.value.integerValue === 'number') {
                        $rootScope.showErrorMessage("Please enter integer type value");
                        valid = false;
                        var div = document.querySelector('#intClick');
                        angular.element(div).click();
                    }
                }
                if (valid) {
                    if (attribute.value.dateValue != null && attribute.value.dateValue != undefined) {
                        var attributeDate = moment(attribute.value.dateValue);
                        attribute.value.dateValue = attributeDate.format('DD/MM/YYYY');
                    }
                    if (attribute.value.id == undefined || attribute.value.id == null) {
                        ItemService.createItemAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                attribute.value.id = data.id;
                                attribute.listValueEditMode = false;
                                $rootScope.showSuccessMessage("Item attribute created successfully..")
                            }
                        )
                    }
                    else {

                        if(attribute.value.booleanValue!=undefined || attribute.value.booleanValue!=null){

                            if(attribute.value.booleanValue=="FALSE"){
                                attribute.value.booleanValue=false;
                            }else if(attribute.value.booleanValue=="TRUE"){
                                attribute.value.booleanValue=true;
                            }

                        }
                        ItemService.updateItemAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Item attribute updated successfully")
                                attribute.listValueEditMode = false;
                                if(attribute.value.booleanValue!=undefined || attribute.value.booleanValue!=null){

                                    if(attribute.value.booleanValue==false){
                                        attribute.value.booleanValue='FALSE';
                                    }else if(attribute.value.booleanValue==true){
                                        attribute.value.booleanValue='TRUE';
                                    }

                                }
                            }
                        )
                    }
                }
            }

            function addToListValue(attribute) {
                if (attribute.value.listValue == undefined) {
                    attribute.value.listValue = [];
                }

                attribute.value.listValue.push(attribute.newListValue);
                attribute.newListValue="";
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadItem();
                }
            })();
        }
    }
);