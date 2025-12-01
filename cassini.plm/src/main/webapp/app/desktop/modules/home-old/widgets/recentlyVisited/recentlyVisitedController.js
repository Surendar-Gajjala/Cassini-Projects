define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RecentlyVisitedController', RecentlyVisitedController);

        function RecentlyVisitedController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $translate,
                                           RecentlyVisitedService, CommonService) {
            var vm = this;

            vm.recentlyVisitedObjects = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showObjectDetails = showObjectDetails;
            vm.loading = true;

            var parsed = angular.element("<div></div>");
            vm.showFileTitle = parsed.html($translate.instant("ITEM_HAS_FILES")).html();
            vm.detailsTitle = parsed.html($translate.instant("CLICK_TO_SHOW_DETAILS")).html();
            vm.configurableItem = parsed.html($translate.instant("CONFIGURABLE_ITEM")).html();
            vm.configuredItem = parsed.html($translate.instant("CONFIGURED_ITEM")).html();
            vm.itemBom = parsed.html($translate.instant("ITEM_HAS_BOM")).html();

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
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

            vm.recentlyVisitedObjects = angular.copy(vm.pagedResults);
            var owner = null;

            function nextPage() {
                vm.pageable.page++;
                loadRecentlyVisited();
            }

            function previousPage() {
                vm.pageable.page--;
                loadRecentlyVisited();
            }

            function loadRecentlyVisited() {
                vm.clear = false;
                vm.loading = true;
                RecentlyVisitedService.getAllRecentlyVisited(owner, vm.pageable).then(
                    function (data) {
                        vm.recentlyVisitedObjects = data;
                        angular.forEach(vm.recentlyVisitedObjects.content, function (obj) {
                            if (obj.visitedDate) {
                                obj.visitedDatede = moment(obj.visitedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        vm.loading = false;
                    }, function (error) {
                        /*$rootScope.showErrorMessage(error.message);*/
                        vm.loading = false;
                    }
                )
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function showObjectDetails(recentlyVisited) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                if (recentlyVisited.type == 'ITEM') {
                    $rootScope.seletedItemId = recentlyVisited.objectId;
                    $state.go('app.items.details', {itemId: recentlyVisited.item.latestRevision});
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'ECO') {
                    $state.go('app.changes.eco.details', {ecoId: recentlyVisited.objectId});
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: recentlyVisited.objectId});
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'TERMINOLOGY') {
                    $state.go('app.rm.glossary.details', {glossaryId: recentlyVisited.objectId});
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: recentlyVisited.objectId})
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'TEMPLATE') {
                    $state.go('app.templates.details', {templateId: recentlyVisited.objectId});
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'WORKFLOW') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: recentlyVisited.objectId});
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: recentlyVisited.manufacturer.id,
                        manufacturePartId: recentlyVisited.objectId
                    });
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'SPECIFICATION') {
                    $state.go('app.rm.specifications.details', {specId: recentlyVisited.objectId})
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                } else if (recentlyVisited.type == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: recentlyVisited.objectId});
                    vm.recentlyVisited.objectId = recentlyVisited.objectId;
                    vm.recentlyVisited.objectType = recentlyVisited.objectType;
                    vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                        function (data) {

                        }
                    )
                }
            }

            (function () {
                $timeout(function () {
                    $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                    if ($rootScope.localStorageLogin != null) {
                        owner = $rootScope.localStorageLogin.login.person.id;
                    }
                    loadRecentlyVisited();
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify('details.plan'));
                    $window.localStorage.setItem("lastSelectedGlossaryTab", JSON.stringify('details.entries'));
                    $window.localStorage.setItem("lastSelectedSpecificationTab", JSON.stringify('details.sections'));
                }, 1000);

            })();
        }

    });
