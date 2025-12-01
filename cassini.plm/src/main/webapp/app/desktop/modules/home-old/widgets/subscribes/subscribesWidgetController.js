define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('SubscribesController', SubscribesController);

        function SubscribesController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $translate,
                                      SpecificationsService, CommonService, ItemService) {
            var vm = this;

            vm.subscribeObjects = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showObjectDetails = showObjectDetails;
            vm.loading = true;

            var parsed = angular.element("<div></div>");
            vm.detailsTitle = parsed.html($translate.instant("CLICK_TO_SHOW_DETAILS")).html();

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

            vm.subscribeObjects = angular.copy(vm.pagedResults);
            var owner = null;

            function nextPage() {
                vm.pageable.page++;
                loadSubScribes();
            }

            function previousPage() {
                vm.pageable.page--;
                loadSubScribes();
            }

            function loadSubScribes() {
                vm.clear = false;
                vm.loading = true;
                SpecificationsService.getAllSubscribesByPerson(owner, vm.pageable).then(
                    function (data) {
                        vm.subscribeObjects = data;
                        vm.loading = false;
                    }
                )
            }

            vm.subscribe = {
                id: null,
                objectId: null,
                person: null,
                subscribe: null
            };

            var itemTitle = parsed.html($translate.instant("ITEM")).html();
            var specificationTitle = parsed.html($translate.instant("SPECIFICATION")).html();
            var unsubscribeMsg = parsed.html($translate.instant("UN_SUBSCRIBE_MSG")).html();
            var subscribeMsg = parsed.html($translate.instant("SUBSCRIBE_MSG")).html();
            var subscribeTitle = parsed.html($translate.instant("SUBSCRIBE_TITLE")).html();
            var unsubscribeTitle = parsed.html($translate.instant("UN_SUBSCRIBE_TITLE")).html();

            vm.subscribeSpecification = subscribeSpecification;
            vm.subscribeItem = subscribeItem;

            function subscribeSpecification(item) {
                SpecificationsService.subscribeSpecificationAndRequirement(item.objectId).then(
                    function (data) {
                        vm.subscribe = data;
                        loadSubScribes();
                        $rootScope.showSuccessMessage(unsubscribeMsg.format(specificationTitle));
                    }
                )
            }

            function subscribeItem(item) {
                ItemService.subscribe(item.objectId).then(
                    function (data) {
                        vm.subscribe = data;
                        loadSubScribes();
                        $rootScope.showSuccessMessage(unsubscribeMsg.format(itemTitle));
                    }
                )
            }

            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            function showObjectDetails(subscribe) {
                if (subscribe.type == 'ITEM') {
                    $state.go('app.items.details', {itemId: subscribe.item.latestRevision});
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.type == 'ECO') {
                    $state.go('app.changes.ecos.details', {ecoId: subscribe.objectId});
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: subscribe.objectId});
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.type == 'GLOSSARY') {
                    $state.go('app.rm.glossary.details', {glossaryId: subscribe.objectId});
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.type == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: subscribe.objectId})
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.type == 'TEMPLATE') {
                    $state.go('app.templates.details', {templateId: subscribe.objectId});
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.type == 'WORKFLOW') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: subscribe.objectId});
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.type == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: subscribe.manufacturer.id,
                        manufacturePartId: subscribe.objectId
                    });
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                } else if (subscribe.type == 'SPECIFICATION') {
                    $state.go('app.rm.specifications.details', {specId: subscribe.objectId})
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                }

                else if (subscribe.type == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: subscribe.objectId})
                    vm.subscribe.objectId = subscribe.objectId;
                    vm.subscribe.objectType = subscribe.objectType;
                    vm.subscribe.person = $rootScope.loginPersonDetails.person.id;

                }
            }

            function addChart() {
                var options = {
                    chart: {
                        type: 'line',
                        height: 350,
                        width: '100%'
                    },
                    series: [{
                        name: 'sales',
                        data: [30,40,35,50,49,60,70,91,125]
                    }],
                    xaxis: {
                        categories: [1991,1992,1993,1994,1995,1996,1997, 1998,1999]
                    }
                };

                var chart = new ApexCharts(document.querySelector("#apexChart"), options);

                chart.render();
            }

            (function () {
                $timeout(function () {
                    $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                    if ($rootScope.localStorageLogin != null) {
                        owner = $rootScope.localStorageLogin.login.person.id;
                    }
                    loadSubScribes();
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify('details.plan'));
                    $window.localStorage.setItem("lastSelectedGlossaryTab", JSON.stringify('details.entries'));
                }, 1000);

                $timeout(function () {
                    //addChart();
                }, 1000);
            })();
        }

    });
