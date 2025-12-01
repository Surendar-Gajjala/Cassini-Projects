/**
 * Created by swapna on 22/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('WorkOrdersController', WorkOrdersController);

        function WorkOrdersController($scope, $rootScope, $timeout, $state, $cookies, $sce, $window,
                                      SubContractService, DialogService, ObjectAttributeService, AttributeAttachmentService, ObjectTypeAttributeService, ItemService, ProjectService) {

            $rootScope.viewInfo.title = "Work Orders";
            var vm = this;
            vm.loading = true;
            vm.objectIds = [];
            vm.workOrderAttributes = [];
            var currencyMap = new Hashtable();
            vm.workOrderDetails = workOrderDetails;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"

                }
            };
            vm.newWorkOrder = newWorkOrder;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.deleteWorkOrder = deleteWorkOrder;

            vm.showWorkOrderAttributes = showWorkOrderAttributes;
            vm.showRequiredImage = showRequiredImage;
            vm.showImage = showImage;
            vm.openAttachment = openAttachment;
            vm.removeAttribute = removeAttribute;

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.workOrders = pagedResults;

            function loadWorkOrders() {
                vm.loading = true;
                SubContractService.getPageableWorkOrders(pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.workOrders = data;
                        SubContractService.getContractorReferences(vm.workOrders.content, 'contractor');
                        ProjectService.getProjectReferences(vm.workOrders.content, 'project');
                        loadAttributeValues();
                    }
                )
            }

            function newWorkOrder() {
                var options = {
                    title: 'New Work Order',
                    showMask: true,
                    template: 'app/desktop/modules/subContracts/workOrders/new/newWorkOrderDialogView.jsp',
                    controller: 'NewWorkOrderController as newWorkOrderVm',
                    resolve: 'app/desktop/modules/subContracts/workOrders/new/newWorkOrderDialogController',
                    width: 500,
                    buttons: [
                        {text: 'Create', broadcast: 'app.workOrder.new'}
                    ],
                    callback: function () {
                        loadWorkOrders();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function workOrderDetails(workOrder) {
                $state.go('app.contracts.workOrderDetails', {workOrderId: workOrder.id});
            }

            function deleteWorkOrder(workOrder) {
                var options = {
                    title: 'Delete Work Order',
                    message: 'Are you sure you want to delete this (' + workOrder.number + ')  Work Order ?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SubContractService.deleteWorkOrder(workOrder.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Work Order deleted successfully");
                                loadWorkOrders();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            /*  attributes block start */

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("workOrderAttributes"));
                } else {
                    var setAttributes = null;
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
                                $window.localStorage.setItem("workOrderAttributes", "");
                                vm.objectAttributes = setAttributes
                            } else {
                                vm.objectAttributes = setAttributes;
                            }

                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                loadRequiredAttributes();
                            }
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        loadRequiredAttributes();
                    }
                }

            }

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("WORKORDER").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadWorkOrders();
                    }
                )
            }

            vm.showRequiredImage = showRequiredImage;

            function showImage(attribute) {
                var modal = document.getElementById('woModal');
                var modalImg = document.getElementById('woImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('woModal1');
                var modalImg = document.getElementById('woImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
            }

            function showWorkOrderAttributes() {
                var options = {
                    title: 'Work Order Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'WORKORDER'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("workOrderAttributes", JSON.stringify(vm.objectAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributes();
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.workOrders.content, function (obj) {
                    obj.refValueString = null;
                    vm.objectIds.push(obj.id);
                });
                angular.forEach(vm.objectAttributes, function (objAttr) {
                    if (objAttr.id != null && objAttr.id != "" && objAttr.id != 0) {
                        vm.attributeIds.push(objAttr.id);
                    }
                });

                angular.forEach(vm.requiredObjectAttributes, function (reqAttr) {
                    if (reqAttr.id != null && reqAttr.id != "" && reqAttr.id != 0) {
                        vm.requiredAttributeIds.push(reqAttr.id);
                    }
                });

                if (vm.objectIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByObjectIdsAndAttributeIds(vm.objectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.objectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.workOrders.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.selectedObjectAttributes[item.id];
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
                                                        vm.objectAttachments = data;
                                                        item[attributeName] = vm.objectAttachments;
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

                        }, function (error) {

                        }
                    );
                }

                if (vm.objectIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    ItemService.getAttributesByObjectIdsAndAttributeIds(vm.objectIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.requiredAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.requiredObjectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.workOrders.content, function (item) {
                                var attributes = [];

                                var objectAttributes = vm.requiredAttributes[item.id];
                                if (objectAttributes != null && objectAttributes != undefined) {
                                    attributes = attributes.concat(objectAttributes);
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
                                                        vm.requiredObjectAttachments = data;
                                                        item[attributeName] = vm.requiredObjectAttachments;
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

                        }, function (error) {

                        }
                    );
                }

            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            /* ........... end attribute block........*/

            function loadCurrencies() {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );
            }

            function previousPage() {
                if (vm.workOrders.first != true) {
                    pageable.page--;
                    loadWorkOrders();
                }
            }

            function nextPage() {
                if (vm.workOrders.last != true) {
                    pageable.page++;
                    loadWorkOrders();
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("workOrderAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadAttributes();
                    loadCurrencies();
                    $rootScope.$on('app.workOrders.all', loadAttributes);
                }
            })();
        }
    }
);