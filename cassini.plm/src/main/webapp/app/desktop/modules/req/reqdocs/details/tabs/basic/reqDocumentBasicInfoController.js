define(
    [
        'app/desktop/modules/req/req.module',
        'app/shared/services/core/reqDocumentService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ReqDocumentBasicInfoController', ReqDocumentBasicInfoController);

        function ReqDocumentBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, LoginService, ReqDocumentService, CommonService, $translate) {
            var vm = this;
            vm.loading = true;
            vm.reqDocId = $stateParams.reqId;
            vm.reqDocumentRevision = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateReqDocument = updateReqDocument;

            $rootScope.loadReqDocumentDetails = loadReqDocumentDetails;
            function loadReqDocumentDetails() {
                vm.loading = true;
                ReqDocumentService.getReqDocumentRevision(vm.reqDocId).then(
                    function (data) {
                        vm.reqDocumentRevision = data;
                        $rootScope.reqDocumentRevision = vm.reqDocumentRevision;
                        $scope.name = vm.reqDocumentRevision.master.name;
                        if (vm.reqDocumentRevision.description != null && vm.reqDocumentRevision.description != undefined) {
                            vm.reqDocumentRevision.master.descriptionHtml = $sce.trustAsHtml(vm.reqDocumentRevision.master.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        }
                        $rootScope.viewInfo.description = vm.reqDocumentRevision.master.name;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                        vm.loading = false;
                        vm.editStatus = false;
                        CommonService.getMultiplePersonReferences([vm.reqDocumentRevision], ['createdBy', 'modifiedBy']);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var reqDocumentRevisionUpdatedSuccess = parsed.html($translate.instant("REQ_DOCUMENT_UPDATED_SUCCESS")).html();


            function validate() {
                var valid = true;
                if (vm.reqDocumentRevision.master.name == null || vm.reqDocumentRevision.master.name == ""
                    || vm.reqDocumentRevision.master.name == undefined) {
                    valid = false;
                    vm.reqDocumentRevision.master.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }
                return valid;
            }

            function updateReqDocument() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    ReqDocumentService.updateReqDocument(vm.reqDocumentRevision.master).then(
                        function (data) {
                            loadReqDocumentDetails();
                            $rootScope.showSuccessMessage(reqDocumentRevisionUpdatedSuccess);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.updatePerson = updatePerson;
            function updatePerson() {
                $rootScope.showBusyIndicator($('.view-container'));
                ReqDocumentService.updateReqDocumentOwner(vm.reqDocumentRevision).then(
                    function (data) {
                        loadReqDocumentDetails();
                        $rootScope.showSuccessMessage(reqDocumentRevisionUpdatedSuccess);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.req.document.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadReqDocumentDetails();
                        loadPersons();
                    }
                });

            })();

        }
    }
)
;