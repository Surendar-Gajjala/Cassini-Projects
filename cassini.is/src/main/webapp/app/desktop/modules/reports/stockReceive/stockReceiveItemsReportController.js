/**
 * Created by swapna on 30/07/19.
 */
define(['app/desktop/modules/reports/reports.module',
        'app/shared/services/store/topStoreService',
        'app/shared/services/store/topStockReceivedService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('StockReceiveItemsController', StockReceiveItemsController);

        function StockReceiveItemsController($scope, $rootScope, $timeout, $state, $cookies,
                                             TopStoreService, TopStockReceivedService, ItemService, AttributeAttachmentService) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-bar-chart";
            $rootScope.viewInfo.title = "Stock Receive Report";

            vm.exportReport = exportReport;
            vm.stores = [];
            vm.openItemDetails = openItemDetails;
            vm.loadStores = loadStores;
            $scope.selectedRow = 0;
            vm.searchReport = searchReport;
            vm.receiveAttributeSearch = receiveAttributeSearch;
            vm.applyFilters = applyFilters;
            vm.resetPage = resetPage;
            vm.attributeFilter = attributeFilter;
            vm.removeAttribute = removeAttribute;
            vm.attributeSearch = false;
            vm.reportRows = [];
            vm.itemAttributesList = [];
            vm.loading = false;
            vm.startDate = null;
            vm.searchMode = false;
            vm.endDate = moment(new Date()).format("DD/MM/YYYY");
            var minimumDate = null;
            var attributeMap = new Hashtable();
            vm.filters = {
                searchQuery: ""
            };
            vm.selectedStore = null;

            vm.emptyFilters = {
                itemName: null,
                description: null,
                itemNumber: null,
                projectName: null,
                units: null,
                quantity: 0,
                movementType: "RECEIVED",
                itemType: 0,
                fromDate: null,
                toDate: null,
                store: null,
                searchAttributes: []
            };

            function openItemDetails(store) {
                $('#search-results-container1').hide();
                vm.selectedStore = store;
                vm.filters.searchQuery = store.storeName;
                vm.emptyFilters.store = store.id;
                TopStockReceivedService.getMinimumDate(store.id).then(
                    function (data) {
                        if (data != null && data != "") {
                            var dateObj = new Date(data);
                            var momentObj = moment(dateObj);
                            vm.startDate = momentObj.format('DD/MM/YYYY');
                            minimumDate = vm.startDate;
                        } else {
                            vm.startDate = moment(new Date()).format("DD/MM/YYYY");
                        }
                    }
                )
            }

            function loadStores() {
                vm.searchMode = false;
                vm.reportRows = [];
                $scope.selectedRow = 0;
                $('#search-results-container1').show();
                TopStoreService.searchStores(vm.filters).then(
                    function (data) {
                        vm.stores = data;
                    }
                )
            }

            function searchReport() {
                vm.reportRows = [];
                vm.searchMode = true;
                if (vm.endDate.trim() != "") {
                    vm.loading = true;
                    vm.emptyFilters.quantity = 0;
                    vm.emptyFilters.fromDate = vm.startDate;
                    vm.emptyFilters.toDate = vm.endDate;
                    vm.emptyFilters.store = vm.selectedStore.id;
                    TopStockReceivedService.getStockReceivedItemsByAttributes(vm.selectedStore.id, vm.emptyFilters).then(
                        function (data) {
                            vm.reportRows = data;
                            vm.loading = false;
                            loadAttributeValues();
                        }
                    );
                } else {
                    $rootScope.showErrorMessage("Please select ToDate");
                }

            }

            function exportReport() {
                $rootScope.showExportMessage("Store Report, Exporting in progress");
                TopStockReceivedService.exportStockReceiveItemsReport(vm.selectedStore.id, vm.emptyFilters).then(
                    function (data) {
                        var url = "{0}//{1}//api/is/stores/".format(window.location.protocol, window.location.host);
                        url += vm.selectedStore.id + "/stockReceive/file/" + data + "/download";
                        window.open(url, '_self');
                        $rootScope.showSuccessMessage("Report exported successfully");
                    }
                )
            }

            function receiveAttributeSearch() {
                var options = {
                    title: 'Attribute Search',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 500,
                    buttons: [
                        {text: 'Search', broadcast: 'app.items.attributes.select'}
                    ],
                    data: {
                        attributesMode: 'RECEIVE',
                        selectedAttributes: vm.itemAttributesList
                    },
                    callback: function (result) {
                        vm.itemAttributesList = result;
                        loadAttributeValues();
                        $rootScope.hideSidePanel();
                        $rootScope.hideBusyIndicator();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getStockReceivedItemsByAttributes() {
                vm.loading = true;
                TopStockReceivedService.getStockReceivedItemsByAttributes(vm.selectedStore.id, vm.emptyFilters).then(
                    function (data) {
                        vm.loading = false;
                        vm.reportRows = data;
                        //vm.attributeSearch = true;
                        loadAttributeValues();
                    }
                );
            }

            function applyFilters() {
                vm.emptyFilters.quantity = 0;
                vm.emptyFilters.fromDate = vm.startDate;
                vm.emptyFilters.toDate = vm.endDate;
                vm.emptyFilters.store = vm.selectedStore.id;

                getStockReceivedItemsByAttributes();

                TopStockReceivedService.getStockReceivedItemsByAttributes(vm.selectedStore.id, vm.emptyFilters).then(
                    function (data) {
                        vm.reportRows = data;
                        loadAttributeValues();
                    });
            }

            function resetPage() {
                vm.emptyFilters = {
                    itemName: null,
                    description: null,
                    itemNumber: null,
                    itemType: 0,
                    units: null,
                    projectName: null,
                    quantity: 0,
                    timeStamp: null,
                    movementType: "RECEIVED",
                    fromDate: vm.startDate,
                    toDate: vm.endDate,
                    searchAttributes: []
                };
                searchReport();
            }

            function attributeFilter(selectatt) {
                var searchString = null;
                vm.attributeSearchDto = {
                    objectTypeAttribute: null,
                    text: null,
                    longText: null,
                    integer: null,
                    list: null,
                    aBoolean: null,
                    booleanSearch: false,
                    aDouble: null,
                    doubleSearch: false,
                    currency: null,
                    date: null,
                    time: null,
                    mListValue: []
                };
                if (selectatt.dataType == 'TEXT') {
                    vm.attributeSearchDto.text = selectatt.textValue;
                    searchString = selectatt.textValue;
                } else if (selectatt.dataType == 'INTEGER') {
                    vm.attributeSearchDto.integer = selectatt.integerValue;
                    searchString = selectatt.integerValue;
                } else if (selectatt.dataType == 'BOOLEAN') {
                    vm.attributeSearchDto.aBoolean = selectatt.booleanValue;
                    searchString = selectatt.booleanValue;
                } else if (selectatt.dataType == 'DOUBLE') {
                    vm.attributeSearchDto.aDouble = selectatt.doubleValue;
                    searchString = selectatt.doubleValue;
                } else if (selectatt.dataType == 'DATE') {
                    vm.attributeSearchDto.date = selectatt.dateValue;
                    searchString = selectatt.dateValue;
                } else if (selectatt.dataType == 'LIST') {
                    vm.attributeSearchDto.list = selectatt.listValue;
                    searchString = selectatt.listValue;
                } else if (selectatt.dataType == 'TIME') {
                    vm.attributeSearchDto.time = selectatt.timeValue;
                    searchString = selectatt.timeValue;
                } else if (selectatt.dataType == 'CURRENCY') {
                    vm.attributeSearchDto.currency = selectatt.currencyValue;
                    searchString = selectatt.currencyValue;
                } else if (selectatt.dataType == 'LONGTEXT') {
                    vm.attributeSearchDto.longText = selectatt.longTextValue;
                    searchString = selectatt.longTextValue;
                }
                var existingAttributeSearchDto = attributeMap.get(selectatt.id);
                if (existingAttributeSearchDto == null) {
                    vm.attributeSearchDto.objectTypeAttribute = selectatt;
                    attributeMap.put(selectatt.id, vm.attributeSearchDto);
                    vm.emptyFilters.searchAttributes.push(vm.attributeSearchDto);
                } else {
                    attributeMap.remove(selectatt.id, existingAttributeSearchDto);
                    var index = vm.emptyFilters.searchAttributes.indexOf(existingAttributeSearchDto);
                    vm.emptyFilters.searchAttributes.splice(index, 1);
                    if (searchString != null && searchString != "") {
                        vm.attributeSearchDto.objectTypeAttribute = selectatt;
                        vm.emptyFilters.searchAttributes.push(vm.attributeSearchDto);
                        attributeMap.put(selectatt.id, vm.attributeSearchDto);
                    }
                }
                applyFilters();
            }

            function loadAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.reportRows, function (item) {
                    if (item.itemDTO != null && item.itemDTO != undefined) {
                        vm.itemIds.push(item.itemDTO.receiveId);
                    }
                    else {
                        vm.itemIds.push(item.receiveId);
                    }
                });
                angular.forEach(vm.itemAttributesList, function (inventoryAttribute) {
                    if (inventoryAttribute.id != null && inventoryAttribute.id != "" && inventoryAttribute.id != 0) {
                        vm.attributeIds.push(inventoryAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.itemAttributesList, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.reportRows, function (item) {
                                var attributes = [];

                                var itemAttributes = [];
                                if (item.itemDTO != null && item.itemDTO != undefined) {
                                    itemAttributes = vm.selectedObjectAttributes[item.itemDTO.receiveId];
                                }
                                else {
                                    itemAttributes = vm.selectedObjectAttributes[item.receiveId];
                                }
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.taskAttachments = data;
                                                        item[attributeName] = vm.taskAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }
            }

            function removeAttribute(att) {
                vm.itemAttributesList.remove(att);
                var existingAttributeSearchDto = attributeMap.get(att.id);
                attributeMap.remove(att.id, existingAttributeSearchDto);
                var index = vm.emptyFilters.searchAttributes.indexOf(existingAttributeSearchDto);
                vm.emptyFilters.searchAttributes.splice(index, 1);
                applyFilters();
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $(document).click(function (e) {
                        if ($(e.target).is('.search-element1 input')) {
                            loadStores();
                        } else
                            $('#search-results-container1').hide();
                    });
                    $(document).on('keydown', function (evt) {
                        if (evt.keyCode == 27) {
                            $('#search-results-container1').hide();
                        }
                    });
                    $(document).on('keydown', function (e) {
                        if ($scope.selectedRow == null) {
                            $scope.selectedRow = 0;
                        }
                        if ($('#search-results-container1').is(':visible')) {
                            if (e.keyCode == 38) {
                                if ($scope.selectedRow == 0) {
                                    return;
                                }
                                $scope.selectedRow--;
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 40) {
                                if ($scope.selectedRow == vm.stores.length - 1) {
                                    return;
                                }
                                $scope.selectedRow++;
                                $scope.$apply();
                                e.preventDefault();
                            } else if (e.keyCode == 13) {
                                openItemDetails(vm.stores[$scope.selectedRow]);
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 13) {
                                openItemDetails(vm.stores[$scope.selectedRow]);
                                $scope.$apply();
                                e.preventDefault();
                            }
                        }
                    });

                }
            })();
        }
    }
);