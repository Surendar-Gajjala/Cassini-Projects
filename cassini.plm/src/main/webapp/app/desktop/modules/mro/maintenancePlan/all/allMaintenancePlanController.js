define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/maintenancePlanService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllMaintenancePlanPlanController', AllMaintenancePlanPlanController);

        function AllMaintenancePlanPlanController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, MaintenancePlanService, ObjectTypeAttributeService,
                                                  ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                                  RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = true;
            vm.newMaintenancePlan = newMaintenancePlan;
            vm.deleteMaintenancePlan = deleteMaintenancePlan;
            vm.maintenancePlan = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;


            vm.searchText = null;
            vm.filterSearch = null;


            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.filters = {
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.maintenancePlans = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newMaintenancePlanHeading = parsed.html($translate.instant("NEW_MAINTENANCE_PLAN")).html();

            function newMaintenancePlan() {
                var options = {
                    title: newMaintenancePlanHeading,
                    template: 'app/desktop/modules/mro/maintenancePlan/new/newMaintenancePlanView.jsp',
                    controller: 'NewMaintenancePlanController as newMaintenancePlanVm',
                    resolve: 'app/desktop/modules/mro/maintenancePlan/new/newMaintenancePlanController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.maintenancePlan.new'}
                    ],
                    callback: function (maintenancePlan) {
                        $timeout(function () {
                            loadMaintenancePlans();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.maintenancePlans.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMaintenancePlans();
                }
            }

            function previousPage() {
                if (vm.maintenancePlans.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMaintenancePlans();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadMaintenancePlans();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadMaintenancePlans();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.maintenancePlans = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadMaintenancePlans();
            }

            function loadMaintenancePlans() {
                vm.loading = true;
                MaintenancePlanService.getAllMaintenancePlans(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.maintenancePlans = data;
                        CommonService.getPersonReferences(vm.maintenancePlans.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                )
            }

            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.maintenancePlans.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.maintenancePlans.content);
                if (vm.objectIds.length > 0 && vm.attributeIds.length > 0 && vm.selectedAttributes.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.objectIds, vm.attributeIds).then(
                        function (data) {
                            if (data != null) {
                                vm.selectedObjectAttributes = data;

                                var map = new Hashtable();
                                angular.forEach(vm.selectedAttributes, function (att) {
                                    if (att.id != null && att.id != "" && att.id != 0) {
                                        map.put(att.id, att);
                                    }
                                });

                                angular.forEach(vm.maintenancePlans.content, function (item) {
                                    var attributes = [];
                                    var revisionAttributes = vm.selectedObjectAttributes[item.latestRevision];
                                    if (revisionAttributes != null && revisionAttributes != undefined) {
                                        attributes = attributes.concat(revisionAttributes);
                                    }
                                    var itemAttributes = vm.selectedObjectAttributes[item.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
                                    if (attributes.length > 0) {
                                        angular.forEach(attributes, function (attribute) {
                                            var selectatt = map.get(attribute.id.attributeDef);
                                            if (selectatt != null) {
                                                var attributeName = selectatt.id;
                                                if (selectatt.dataType == 'TEXT') {
                                                    item[attributeName] = attribute.stringValue;
                                                } else if (selectatt.dataType == 'LONGTEXT') {
                                                    item[attributeName] = attribute.longTextValue;
                                                } else if (selectatt.dataType == 'RICHTEXT') {
                                                    item[attributeName] = attribute;
                                                } else if (selectatt.dataType == 'INTEGER') {
                                                    item[attributeName] = attribute.integerValue;
                                                } else if (selectatt.dataType == 'BOOLEAN') {
                                                    item[attributeName] = attribute.booleanValue;
                                                } else if (selectatt.dataType == 'DOUBLE') {

                                                    if (attribute.doubleValue != null && attribute.measurementUnit != null) {

                                                        if (selectatt.measurement != null) {
                                                            var measurement = selectatt.measurement;
                                                            var measurementUnits = measurement.measurementUnits;
                                                            var baseUnit = null;
                                                            var attributeUnit = null;
                                                            angular.forEach(measurementUnits, function (unit) {
                                                                if (unit.baseUnit) {
                                                                    baseUnit = unit;
                                                                }
                                                                if (attribute.measurementUnit != null && attribute.measurementUnit.id == unit.id) {
                                                                    attributeUnit = unit;
                                                                }
                                                            })

                                                            var baseUnitIndex = measurementUnits.indexOf(baseUnit);
                                                            var attributeIndex = measurementUnits.indexOf(attributeUnit);

                                                            if (attributeIndex != baseUnitIndex) {
                                                                item[attributeName] = (attribute.doubleValue * attributeUnit.conversionFactor) + " " + attribute.measurementUnit.symbol;
                                                            } else {
                                                                item[attributeName] = attribute.doubleValue + " " + attribute.measurementUnit.symbol;
                                                            }
                                                        } else {
                                                            item[attributeName] = attribute.doubleValue;
                                                        }
                                                    } else {
                                                        item[attributeName] = attribute.doubleValue;
                                                    }
                                                } else if (selectatt.dataType == 'HYPERLINK') {
                                                    item[attributeName] = attribute.hyperLinkValue;
                                                } else if (selectatt.dataType == 'DATE') {
                                                    item[attributeName] = attribute.dateValue;
                                                } else if (selectatt.dataType == 'LIST' && !selectatt.listMultiple) {
                                                    item[attributeName] = attribute.listValue;
                                                } else if (selectatt.dataType == 'LIST' && selectatt.listMultiple) {
                                                    item[attributeName] = attribute.mlistValue;
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
                                                    var revisionAttachmentIds = [];
                                                    if (attribute.attachmentValues.length > 0) {
                                                        angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                            revisionAttachmentIds.push(attachmentId);
                                                        });
                                                        AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                            function (data) {
                                                                vm.revisionAttachments = data;
                                                                item[attributeName] = vm.revisionAttachments;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    }
                                                } else if (selectatt.dataType == 'IMAGE') {
                                                    if (attribute.imageValue != null) {
                                                        item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                    }
                                                } else if (selectatt.dataType == 'OBJECT') {
                                                    if (selectatt.refType != null) {
                                                        if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                            ItemService.getItem(attribute.refValue).then(
                                                                function (itemValue) {
                                                                    item[attributeName] = itemValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                            ItemService.getRevisionId(attribute.refValue).then(
                                                                function (revisionValue) {
                                                                    item[attributeName] = revisionValue;
                                                                    ItemService.getItem(revisionValue.itemMaster).then(
                                                                        function (data) {
                                                                            item[attributeName].itemMaster = data.itemNumber;
                                                                        }, function (error) {
                                                                            $rootScope.showErrorMessage(error.message);
                                                                        }
                                                                    )
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                            ECOService.getECO(attribute.refValue).then(
                                                                function (changeValue) {
                                                                    item[attributeName] = changeValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                            WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                                function (workflowValue) {
                                                                    item[attributeName] = workflowValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                            MfrService.getManufacturer(attribute.refValue).then(
                                                                function (mfrValue) {
                                                                    item[attributeName] = mfrValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                            MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                                function (mfrPartValue) {
                                                                    item[attributeName] = mfrPartValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                            CommonService.getPerson(attribute.refValue).then(
                                                                function (person) {
                                                                    item[attributeName] = person;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'PROJECT' && attribute.refValue != null) {
                                                            ProjectService.getProject(attribute.refValue).then(
                                                                function (project) {
                                                                    item[attributeName] = project;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'REQUIREMENT' && attribute.refValue != null) {
                                                            SpecificationsService.getRequirement(attribute.refValue).then(
                                                                function (reqValue) {
                                                                    item[attributeName] = reqValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                    }
                                    else {
                                        angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                                            if (selectedAttribute.dataType == "TEXT" && selectedAttribute.defaultTextValue != null) {
                                                var attributeName = selectedAttribute.id;
                                                item[attributeName] = selectedAttribute.defaultTextValue;
                                            }
                                            if (selectedAttribute.dataType == "LIST" && selectedAttribute.defaultListValue != null) {
                                                var attributeName = selectedAttribute.id;
                                                item[attributeName] = selectedAttribute.defaultListValue;
                                            }
                                        });
                                    }
                                })
                            } else {
                                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                                    if (selectedAttribute.attributeDef.dataType == "TEXT" && selectedAttribute.attributeDef.defaultTextValue == null) {
                                        item[selectedAttribute] = selectedAttribute.attributeDef.defaultTextValue;
                                    }
                                });
                            }
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    );
                }
            }


            vm.showMaintenancePlan = showMaintenancePlan;
            function showMaintenancePlan(maintenancePlan) {
                $state.go('app.mro.maintenancePlan.details', {
                    maintenancePlanId: maintenancePlan.id,
                    tab: 'details.basic'
                });
                /* vm.recentlyVisited = {};
                 vm.recentlyVisited.objectId = maintenancePlan.id;
                 vm.recentlyVisited.objectType = maintenancePlan.objectType;
                 vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                 RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                 function (data) {
                 $state.go('app.mes.maintenancePlan.details', {maintenancePlanId: 5.id, tab: 'details.basic'});
                 }, function (error) {
                 $state.go('app.mes.maintenancePlan.details', {maintenancePlanId: maintenancePlan.id, tab: 'details.basic'});
                 }
                 )*/
            }

            var deleteMaintenancePlanTitle = parsed.html($translate.instant("DELETE_MAINTENANCE_PLAN")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var maintenancePlanDeleteMsg = parsed.html($translate.instant("M_PLAN_DELETED_SUCCESS")).html();


            function deleteMaintenancePlan(maintenancePlan) {
                var options = {
                    title: deleteMaintenancePlanTitle,
                    message: deleteDialogMessage + " [ " + maintenancePlan.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (maintenancePlan.id != null && maintenancePlan.id != undefined) {
                            MaintenancePlanService.deleteMaintenancePlan(maintenancePlan.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(maintenancePlanDeleteMsg);
                                    loadMaintenancePlans();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                });
            }

            var currencyMap = new Hashtable();
            var attributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: attributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "WORKORDERTYPE",
                        objectType: "MROWORKORDER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("maintenancePlanAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadMaintenancePlans();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("maintenancePlanAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("maintenancePlanAttributes"));
                } else {
                    setAttributes = null;
                }
                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.objectIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("maintenancePlanAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadMaintenancePlans();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadMaintenancePlans();
                }
            })();

        }
    }
);