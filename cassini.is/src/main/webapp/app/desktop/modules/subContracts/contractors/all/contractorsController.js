/**
 * Created by swapna on 21/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ContractorsController', ContractorsController);

        function ContractorsController($scope, $rootScope, $timeout, $state, $cookies, $sce, $window,
                                       SubContractService, CommonService, DialogService, ObjectAttributeService, AttributeAttachmentService, ObjectTypeAttributeService, ItemService) {

            $rootScope.viewInfo.title = "Contractors";
            $rootScope.viewInfo.icon = "fa fa-users";
            var vm = this;
            vm.loading = false;
            vm.objectIds = [];
            vm.contractorAttributes = [];
            var currencyMap = new Hashtable();
            var contractorsMap = new Hashtable();
            vm.newContractor = newContractor;
            vm.contractorDetails = contractorDetails;
            vm.deleteContractor = deleteContractor;
            vm.showContractorAttributes = showContractorAttributes;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.showRequiredImage = showRequiredImage;
            vm.showImage = showImage;
            vm.openAttachment = openAttachment;
            vm.removeAttribute = removeAttribute;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"

                }
            };

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
            vm.contractors = pagedResults;

            vm.workOrderListPopover = {
                templateUrl: 'app/desktop/modules/subContracts/contractors/all/contractorWorkOrdersView.jsp'
            };

            function newContractor() {
                var options = {
                    title: 'New Contractor',
                    showMask: true,
                    template: 'app/desktop/modules/subContracts/contractors/new/newContractorView.jsp',
                    controller: 'NewContractorController as newContractorVm',
                    resolve: 'app/desktop/modules/subContracts/contractors/new/newContractorController',
                    width: 500,
                    buttons: [
                        {text: 'Create', broadcast: 'app.contractor.new'}
                    ],
                    callback: function () {
                        loadContractors();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function contractorDetails(contractor) {
                var options = {
                    title: 'Contractor Details',
                    showMask: true,
                    template: 'app/desktop/modules/subContracts/contractors/details/contractorDetailsView.jsp',
                    controller: 'ContractorDetailsController as contractorDetailsVm',
                    resolve: 'app/desktop/modules/subContracts/contractors/details/contractorDetailsController',
                    width: 500,
                    data: {
                        contractor: contractor
                    },
                    callback: function (con) {
                        var contractor = contractorsMap.get(con.id);
                        var index = vm.contractors.content.indexOf(contractor);
                        var workOrders = contractor.workOrders;
                        vm.contractors.content[index] = con;
                        vm.contractors.content[index].workOrders = workOrders;

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadContractors() {
                vm.loading = true;
                contractorsMap = new Hashtable();
                SubContractService.getPageableContractors(pageable).then(
                    function (data) {
                        vm.contractors = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.contractors.content, 'contact');
                        CommonService.getPersonReferences(vm.contractors.content, 'createdBy');
                        CommonService.getPersonReferences(vm.contractors.content, 'modifiedBy');
                        angular.forEach(vm.contractors.content, function (contractor) {
                            contractorsMap.put(contractor.id, contractor);
                        });
                        loadAttributeValues();
                    },
                    function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function deleteContractor(contractor) {
                if (contractor.workOrders.length > 0) {
                    var options = {
                        title: 'Delete Contractor',
                        message: 'This contractor has work orders! Are you sure you want to delete this contractor?',
                        okButtonClass: 'btn-danger'
                    };

                }
                else {
                    var options = {
                        title: 'Delete Contractor',
                        message: 'Are you sure you want to delete this (' + contractor.name + ')  contractor?',
                        okButtonClass: 'btn-danger'
                    };
                }
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            SubContractService.deleteContractor(contractor.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Contractor " + contractor.name + " deleted successfully");
                                    loadContractors();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                )
            }

            /*  attributes block start */

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("contractorAttributes"));
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
                                $window.localStorage.setItem("contractorAttributes", "");
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
                ItemService.getTypeAttributesRequiredTrue("CONTRACTOR").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadContractors();
                    }
                )
            }

            function showImage(attribute) {
                var modal = document.getElementById('conModal');
                var modalImg = document.getElementById('conImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage7")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('image12');
                var modalImg = document.getElementById('img12');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage12")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            $rootScope.workOrderDetails = workOrderDetails;

            function workOrderDetails(workOrder) {
                $state.go('app.contracts.workOrderDetails', {workOrderId: workOrder.id});
            }

            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
            }

            function showContractorAttributes() {
                var options = {
                    title: 'Contractor Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'CONTRACTOR'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("contractorAttributes", JSON.stringify(vm.objectAttributes));
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
                angular.forEach(vm.contractors.content, function (obj) {
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

                            angular.forEach(vm.contractors.content, function (item) {
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

                            angular.forEach(vm.contractors.content, function (item) {
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

            /* ........... end attribute block........*/

            function previousPage() {
                if (vm.contractors.first != true) {
                    pageable.page--;
                    loadContractors();
                }
            }

            function nextPage() {
                if (vm.contractors.last != true) {
                    pageable.page++;
                    loadContractors();
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("contractorAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

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

            (function () {
                if ($application.homeLoaded == true) {
                    loadAttributes();
                    loadCurrencies();
                    $rootScope.$on('app.contractors.all', loadAttributes);
                }
            })();
        }
    }
);