define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/jigsFixService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllJigsAndFixturesController', AllJigsAndFixturesController);

        function AllJigsAndFixturesController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, CommonService,
                                              $stateParams, $cookies, $sce, JigsFixtureService, ObjectTypeAttributeService, ItemService, ECOService, WorkflowDefinitionService,
                                              AttributeAttachmentService, MfrService, MfrPartsService, ProjectService, SpecificationsService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            var parsed = angular.element("<div></div>");
            vm.newJigsAndFixtures = newJigsAndFixtures;
            vm.jigsAndFixtures = [];
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
                number: null,
                type: '',
                name: null,
                description: null,
                jigType: null,
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.jigsAndFixtures = angular.copy(pagedResults);

            var newJigHeading = parsed.html($translate.instant("NEW_JIG_TITLE_MSG")).html();
            var deleteJigMsg = parsed.html($translate.instant("DELETE_JIG_MSG")).html();
            var deleteJigTitleMsg = parsed.html($translate.instant("DELETE_JIG_TITLE_MSG")).html();
            var deleteJigDialogMsg = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var newFixtureHeading = parsed.html($translate.instant("NEW_FIXTURE_TITLE_MSG")).html();
            var deleteFixtureMsg = parsed.html($translate.instant("DELETE_FIXTURE_MSG")).html();
            var deleteFixturesTitleMsg = parsed.html($translate.instant("DELETE_FIXTURE_TITLE_MSG")).html();
            var deleteFixtureDialogMsg = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            $scope.newJig = parsed.html($translate.instant("NEW_JIG_TITLE_MSG")).html();
            $scope.newFixture = parsed.html($translate.instant("NEW_FIXTURE_TITLE_MSG")).html();

            function newJigsAndFixtures() {
                var newJigTitle = null;
                if ($rootScope.jigsFixtureType == 'JIG') {
                    newJigTitle = newJigHeading;
                } else if ($rootScope.jigsFixtureType == 'FIXTURE') {
                    newJigTitle = newFixtureHeading;
                }
                var options = {
                    title: newJigTitle,
                    template: 'app/desktop/modules/mes/jigsAndFixtures/new/newJigsAndFixturesView.jsp',
                    controller: 'NewJigsAndFixturesController as newJigsAndFixturesVm',
                    resolve: 'app/desktop/modules/mes/jigsAndFixtures/new/newJigsAndFixturesController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedObject: $rootScope.jigsFixtureType,
                        actionType: ""
                    },
                    buttons: [
                        {text: create, broadcast: 'app.jigsAndFixtures.new'}
                    ],
                    callback: function (jigsAndFixture) {
                        $timeout(function () {
                            loadJigsAndFixtures();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.jigFixtureFilePopover = {
                templateUrl: 'app/desktop/modules/mes/jigsAndFixtures/all/jigFixtureFilePopoverTemplate.jsp'
            };
 
            function nextPage() {
                if (vm.jigsAndFixtures.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadJigsAndFixtures();
                }
            }

            function previousPage() {
                if (vm.jigsAndFixtures.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadJigsAndFixtures();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadJigsAndFixtures();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadJigsAndFixtures();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.jigsAndFixtures = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadJigsAndFixtures();
            }

            vm.selectJigsFixType = selectJigsFixType;
            function selectJigsFixType(type) {
                $rootScope.showBusyIndicator($('.view-container'));
                $rootScope.jigsFixtureType = type;
                vm.pageable.page = 0;
                loadJigsAndFixtures();
            }

            function loadJigsAndFixtures() {
                vm.loading = true;
                if ($rootScope.jigsFixtureType == 'JIG') {
                    vm.filters.jigType = 'JIG';
                } else {
                    vm.filters.jigType = 'FIXTURE';
                }
                JigsFixtureService.getAllJigsFixs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.jigsAndFixtures = data;
                        vm.loading = false;
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.jigsAndFixtures.content, function (item) {
                    vm.objectIds.push(item.id);
                    if (item.hasImage) {
                        item.imagePath = "api/mes/jigsfixs/" + item.id + "/image/download?" + new Date().getTime();
                    }
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.jigsAndFixtures.content);

            }


            vm.showJigsFixture = showJigsFixture;
            function showJigsFixture(jigFixture) {
                $state.go('app.mes.masterData.jigsAndFixtures.details', {
                    jigsFixId: jigFixture.id,
                    tab: 'details.basic'
                });
            }

            vm.deleteJigsFixture = deleteJigsFixture;

            function deleteJigsFixture(jigFixture) {
                var deleteJigsAndFixturesTitleMsg = null;
                var deleteJigsAndFixtureDialogMsg = null;
                if ($rootScope.jigsFixtureType == 'JIG') {
                    deleteJigsAndFixturesTitleMsg = deleteJigTitleMsg;
                    deleteJigsAndFixtureDialogMsg = deleteJigDialogMsg + " [" + jigFixture.number + "] ?";
                } else if ($rootScope.jigsFixtureType == 'FIXTURE') {
                    deleteJigsAndFixturesTitleMsg = deleteFixturesTitleMsg;
                    deleteJigsAndFixtureDialogMsg = deleteFixtureDialogMsg + " [" + jigFixture.number + "] ?";
                }
                var options = {
                    title: deleteJigsAndFixturesTitleMsg,
                    message: deleteJigsAndFixtureDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (jigFixture.id != null && jigFixture.id != undefined) {
                            JigsFixtureService.deleteJigsFix(jigFixture.id).then(
                                function (data) {
                                    var index = vm.jigsAndFixtures.content.indexOf(jigFixture);
                                    vm.jigsAndFixtures.content.splice(index, 1);
                                    vm.jigsAndFixtures.totalElements--;
                                    if ($rootScope.jigsFixtureType == 'JIG') {
                                        $rootScope.showSuccessMessage(deleteJigMsg);
                                    } else if ($rootScope.jigsFixtureType == 'FIXTURE') {
                                        $rootScope.showSuccessMessage(deleteFixtureMsg);
                                    }
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                });
            }

            var currencyMap = new Hashtable();
            var plantsAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: plantsAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "JIGFIXTURETYPE",
                        objectType: "JIGFIXTURE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("jigFixtureAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadJigsAndFixtures();
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
                    JSON.parse($window.localStorage.getItem("jigFixtureAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showImage = showImage;
            function showImage(machine) {
                var modal = document.getElementById('item-thumbnail' + machine.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + machine.id);
                $("#thumbnail-image" + machine.id).width($('#thumbnail-view' + machine.id).outerWidth());
                $("#thumbnail-image" + machine.id).height($('#thumbnail-view' + machine.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                if ($rootScope.jigsFixtureType == null || $rootScope.jigsFixtureType == "" || $rootScope.jigsFixtureType == undefined) {
                    $rootScope.jigsFixtureType = 'JIG';
                }
                if ($rootScope.jigsFixtureType == "FIXTURE") {
                    document.getElementById("jigsType").checked = false;
                    document.getElementById("fixtureType").checked = true;
                }
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("jigFixtureAttributes"));
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
                                $window.localStorage.setItem("jigFixtureAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadJigsAndFixtures();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadJigsAndFixtures();
                }
            })();

        }
    }
);