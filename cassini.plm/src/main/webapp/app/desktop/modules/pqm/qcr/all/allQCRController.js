define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllQCRController', AllQCRController);

        function AllQCRController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $sce, $application,
                                  QcrService, DialogService, ItemService, ECOService, WorkflowDefinitionService, ObjectTypeAttributeService, RecentlyVisitedService,
                                  MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.loading = false;
            var currencyMap = new Hashtable();
            var parsed = angular.element("<div></div>");
            $scope.createQcrTitle = parsed.html($translate.instant("CREATE_NEW_QCR")).html();
            var newQcrTitle = parsed.html($translate.instant("NEW_QCR")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_QCR")).html();
            var deleteQcrDialogMessage = parsed.html($translate.instant("DELETE_QCR_DIALOG_MESSAGE")).html();
            var qcrDeletedMessage = parsed.html($translate.instant("QCR_DELETED_MESSAGE")).html();
            $scope.cannotDeleteApprovedQcr = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_QCR")).html();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();
            $scope.noQcrs = parsed.html($translate.instant("NO_QCRS")).html();


            vm.newQcr = newQcr;

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
            vm.qcrFilter = {
                qcrNumber: null,
                qcrType: '',
                title: null,
                searchQuery: null
            };

            vm.qcrs = angular.copy(pagedResults);
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            function newQcr() {
                var options = {
                    title: newQcrTitle,
                    template: 'app/desktop/modules/pqm/qcr/new/newQcrView.jsp',
                    controller: 'NewQcrController as newQcrVm',
                    resolve: 'app/desktop/modules/pqm/qcr/new/newQcrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.qcrs.new'}
                    ],
                    callback: function (qcr) {
                        $timeout(function () {
                            showQcr(qcr)
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAllQcrs() {
                $rootScope.showBusyIndicator();
                vm.loading=true;
                QcrService.getAllQcrs(vm.pageable, vm.qcrFilter).then(
                    function (data) {
                        vm.qcrs = data;
                        loadAttributeValues();
                        vm.loading=false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                angular.forEach(vm.qcrs.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.qcrs.content);
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }

            function nextPage() {
                if (vm.qcrs.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAllQcrs();
                }
            }

            function previousPage() {
                if (vm.qcrs.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAllQcrs();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadAllQcrs();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.qcrFilter.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAllQcrs();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.qcrs = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.qcrFilter.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAllQcrs();
            }

            vm.showQcr = showQcr;
            function showQcr(qcr) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = qcr.id;
                vm.recentlyVisited.objectType = "QCR";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go("app.pqm.qcr.details", {qcrId: qcr.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go("app.pqm.qcr.details", {qcrId: qcr.id, tab: 'details.basic'});
                    }
                )
            }

            vm.deleteQCR = deleteQCR;
            function deleteQCR(qcr) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteQcrDialogMessage + " [" + qcr.qcrNumber + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        QcrService.deleteQcr(qcr.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(qcrDeletedMessage);
                                loadAllQcrs();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;
            vm.selectedAttributes = [];
            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionView.jsp',
                    resolve: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionController',
                    controller: 'QualityTypeAttributeSelectionController as qualityTypeAttributeSelectionVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        selectedObjectType: "QCRTYPE",
                        selectedObject: "QCR",
                        selectedParentObjectType: "QUALITY"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("qcrAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadAllQcrs();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("qcrAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("qcrAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showAttributeDetails = showAttributeDetails;
            function showAttributeDetails(attribute) {
                if (attribute.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'CHANGE') {
                    $state.go('app.changes.ecos.details', {ecoId: attribute.id});
                } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id})
                } else if (attribute.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.manufacturer,
                        manufacturePartId: attribute.id
                    });
                } else if (attribute.objectType == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: attribute.id});
                } else if (attribute.objectType == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: attribute.id});
                }
            }


            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.getQualityAttributes("QUALITY", "QCRTYPE", vm.attributeIds);
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("qcrAttributes"));
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
                                $window.localStorage.setItem("qcrAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadAllQcrs();
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                } else {
                    loadAllQcrs();
                }
                //}
            })();
        }
    }
);


