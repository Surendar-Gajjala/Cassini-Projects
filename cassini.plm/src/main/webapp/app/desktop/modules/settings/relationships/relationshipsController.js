define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/relationshipService',
        'app/shared/services/core/relatedItemService'
    ],
    function (module) {
        module.controller('RelationshipsController', RelationshipsController);

        function RelationshipsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                         CommonService, $translate, ItemTypeService, RelationshipService, RelatedItemService) {
            var vm = this;

            vm.createRelationship = createRelationship;
            vm.deleteRelationship = deleteRelationship;
            vm.editRelationship = editRelationship;
            vm.pageSize = pageSize;
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
            vm.relationships = [];
            vm.relationships = angular.copy(pagedResults);

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.relationships.last != true) {
                    vm.pageable.page++;
                    loadRelationships();
                }
            }

            function previousPage() {
                if (vm.relationships.first != true) {
                    vm.pageable.page--;
                    loadRelationships();
                }
            }

            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadRelationships();
            }

            var parsed = angular.element("<div></div>");
            var newRelationshipTitle = parsed.html($translate.instant("NEW_RELATIONSHIP")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var editButton = parsed.html($translate.instant("EDIT")).html();
            var updateButton = parsed.html($translate.instant("UPDATE")).html();
            var editRelationshipTitle = parsed.html($translate.instant("EDIT_RELATIONSHIP")).html();
            var relationshipCreatedMessage = parsed.html($translate.instant("RELATIONSHIP_CREATED_MESSAGE")).html();
            var relationshipUpdateMessage = parsed.html($translate.instant("RELATIONSHIP_UPDATE_MESSAGE")).html();
            var deleteRelationshipTitle = parsed.html($translate.instant("DELETE_RELATIONSHIP_TITLE")).html();
            var deleteRelationshipMessage = parsed.html($translate.instant("DELETE_RELATIONSHIP_MESSAGE")).html();
            var relationshipDeletedMessage = parsed.html($translate.instant("RELATIONSHIP_DELETED_MESSAGE")).html();
            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();
            var cannotDeleteRelationship = parsed.html($translate.instant("DELETE_RELATIONSHIP_VALIDATION")).html();
            var cannotEditRelationship = parsed.html($translate.instant("EDIT_RELATIONSHIP_VALIDATION")).html();
            var relationshipValidation = parsed.html($translate.instant("RELATIONSHIP_ALREADY_IN_USE")).html();

            function createRelationship() {
                var options = {
                    title: newRelationshipTitle,
                    template: 'app/desktop/modules/settings/relationships/new/newRelationshipView.jsp',
                    controller: 'NewRelationshipController as newRelationshipVm',
                    resolve: 'app/desktop/modules/settings/relationships/new/newRelationshipController',
                    width: 700,
                    showMask: true,
                    data: {
                        relationshipData: null
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.relationship.new'}
                    ],
                    callback: function () {
                        $rootScope.showSuccessMessage(relationshipCreatedMessage);
                        $rootScope.hideSidePanel();
                        loadRelationships();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editRelationship(relationship) {
                RelatedItemService.getRelatedItemByRelationship(relationship.id).then(
                    function (data) {
                        vm.relatedItems = data;
                        if (vm.relatedItems.length == 0) {
                            var options = {
                                title: relationship.name + " Details",
                                template: 'app/desktop/modules/settings/relationships/new/newRelationshipView.jsp',
                                controller: 'NewRelationshipController as newRelationshipVm',
                                resolve: 'app/desktop/modules/settings/relationships/new/newRelationshipController',
                                width: 700,
                                data: {
                                    relationshipData: relationship
                                },
                                showMask: true,
                                buttons: [
                                    {text: updateButton, broadcast: 'app.relationship.edit'}
                                ],
                                callback: function () {
                                    $rootScope.showSuccessMessage(relationshipUpdateMessage);
                                    loadRelationships();
                                }
                            };

                            $rootScope.showSidePanel(options);
                        } else {
                            $rootScope.showWarningMessage(relationship.name + " : " + cannotEditRelationship)
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function deleteRelationship(relationship) {
                RelatedItemService.getRelatedItemByRelationship(relationship.id).then(
                    function (data) {
                        vm.relatedItems = data;
                        if (vm.relatedItems.length == 0) {
                            var options = {
                                title: deleteRelationshipTitle,
                                message: deleteRelationshipMessage + " " + relationship.name + " ?",
                                okButtonClass: 'btn-danger',
                                okButtonText: yes,
                                cancelButtonText: no
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    RelationshipService.deleteRelationship(relationship).then(
                                        function (data) {
                                            loadRelationships();
                                            $rootScope.showSuccessMessage(relationshipDeletedMessage);
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            });
                        } else {
                            $rootScope.showWarningMessage(relationship.name + " : " + cannotDeleteRelationship)
                        }
                    }
                )
            }

            function loadRelationships() {
                RelationshipService.getAllRelationships(vm.pageable).then(
                    function (data) {
                        vm.relationships = data;
                        angular.forEach(vm.relationships.content, function (relationship) {
                            relationship.editMode = false;
                        })
                        $timeout(function () {
                            resizeScreen();
                            $rootScope.hideBusyIndicator();
                        }, 500);
                    }
                )
            }

            function resizeScreen() {
                var viewContent = $('.view-content').outerHeight();
                var paginationHeader = $('.pagination-header').outerHeight();
                $('.headerSticky').height(viewContent - (paginationHeader + 10));
            }

            (function () {
                loadRelationships();
                $(window).resize(function () {
                    resizeScreen();
                })

                $scope.$on("settings.new.relationship", function (evnt, args) {
                    createRelationship();
                });
            })();
        }
    }
);