define(['app/desktop/modules/issues/issues.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/issue/issueService',
        'app/desktop/modules/issues/new/newIssueDialogController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('IssuesController', IssuesController);

        function IssuesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce, $window,
                                  CommonService, IssueService, DialogService, TaskService, ObjectAttributeService, ItemService, AttributeAttachmentService, ObjectTypeAttributeService) {

            $rootScope.viewInfo.icon = "fa flaticon-marketing8";
            $rootScope.viewInfo.title = "Problems";

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showImage = showImage;
            vm.openAttachment = openAttachment;
            vm.removeAttribute = removeAttribute;
            vm.showProblemAttributes = showProblemAttributes;
            vm.showRequiredImage = showRequiredImage;
            vm.problemAttributes = [];
            var currencyMap = new Hashtable();
            var issueMap = new Hashtable();
            vm.mode = null;
            vm.objectIds = [];
            vm.issues = [];
            var criteria = {
                searchQuery: null,
                targetObjectType: 'PROJECT'
            };
            var pageable = {
                page: 0,
                size: 10,
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
            vm.issues = angular.copy(pagedResults);

            function nextPage() {
                pageable.page++;
                loadIssues();
            }

            function previousPage() {
                pageable.page--;
                loadIssues();
            }

            vm.newIssue = newIssue;
            vm.openIssue = openIssue;
            vm.deleteProjectIssue = deleteProjectIssue;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.taskDetails = taskDetails;
            $scope.freeTextQuery = null;

            function taskDetails(taskId) {
                $state.go('app.pm.project.taskdetails', {taskId: taskId})
            }

            function freeTextSearch(freeText) {
                vm.loading = true;
                $scope.freeTextQuery = freeText;
                searchMode = "freetext";
                criteria.searchQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    IssueService.freeSearch(pageable, criteria).then(
                        function (data) {
                            vm.issues = data;
                            vm.loading = false;
                            CommonService.getPersonReferences(vm.issues.content, 'createdBy');
                            CommonService.getPersonReferences(vm.issues.content, 'assignedTo');
                            TaskService.getTaskReferences($stateParams.projectId, vm.issues.content, 'task');
                            angular.forEach(vm.issues.content, function (issue) {
                                TaskService.getProjectTask($stateParams.projectId, issue.task).then(
                                    function (data) {
                                        issue.taskName = data.name;
                                    })
                            })

                        }
                    );
                    vm.clear = true;

                } else {
                    resetPage();
                    loadIssues();
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function newIssue() {
                var options = {
                    title: 'New Problem',
                    showMask: true,
                    template: 'app/desktop/modules/issues/new/newIssueDialog.jsp',
                    controller: 'NewIssueDialogController as newIssueVm',
                    resolve: 'app/desktop/modules/issues/new/newIssueDialogController',
                    width: 500,
                    data: {
                        targetObjectType: "PROJECT",
                        targetObjectId: $stateParams.projectId,
                        issueMap: issueMap
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.issue.new'}
                    ],
                    callback: function () {
                        loadIssues();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function openIssue(issue) {
                $rootScope.problemId = issue.id;
                $state.go('app.pm.project.issuedetails', {issueId: issue.id});
            }

            function deleteProjectIssue(issue) {
                var options = {
                    title: 'Delete Problem',
                    message: 'Are you sure you want to delete this (' + issue.title + ') Problem?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        IssueService.deleteIssue(issue.id).then(
                            function (data) {
                                var index = vm.issues.content.indexOf(issue);
                                vm.issues.content.splice(index, 1);
                                $rootScope.showSuccessMessage("Problem deleted successfully");
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            function loadIssues() {
                vm.loading = true;
                IssueService.getPageableIssues('PROJECT', $stateParams.projectId, pageable).then(
                    function (data) {
                        vm.issues = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.issues.content, 'createdBy');
                        CommonService.getPersonReferences(vm.issues.content, 'assignedTo');
                        TaskService.getTaskReferences($stateParams.projectId, vm.issues.content, 'task');
                        angular.forEach(vm.issues.content, function (issue) {
                            issueMap.put(issue.title, issue);
                        });
                        loadAttributeValues();
                    }
                );
            }

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("problemAttributes"));
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
                                $window.localStorage.setItem("problemAttributes", "");
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
                ItemService.getTypeAttributesRequiredTrue("PROBLEM").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadIssues();
                    }
                )
            }

            function showImage(attribute) {
                var modal = document.getElementById('probModal');
                var modalImg = document.getElementById('probImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

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

            function showRequiredImage(attribute) {
                var modal = document.getElementById('probModal1');
                var modalImg = document.getElementById('probImg1');

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

            function showProblemAttributes() {
                var options = {
                    title: 'Problem Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'PROBLEM'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("problemAttributes", JSON.stringify(vm.objectAttributes));
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
                angular.forEach(vm.issues.content, function (obj) {
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

                            angular.forEach(vm.issues.content, function (item) {
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

                            angular.forEach(vm.issues.content, function (item) {
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

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("problemAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadAttributes();
                    loadCurrencies();
                    $rootScope.$on('app.problems.all', function () {
                        loadIssues();
                    });
                }
            })();

        }
    }
);