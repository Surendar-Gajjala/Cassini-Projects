define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService'
    ],
    function (module) {
        module.controller('ReqBasicInfoController', ReqBasicInfoController);

        function ReqBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, LoginService, RequirementService, ReqDocumentService, CommonService, $translate) {
            var vm = this;
            vm.loading = true;
            vm.reqId = $stateParams.requirementId;
            vm.reqVersion = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.editDescription = false; 
            vm.updateRequirement = updateRequirement;
            $rootScope.loadRequirementDetails = loadRequirementDetails;
            function loadRequirementDetails() {
                vm.loading = true;
                RequirementService.getRequirementVersionObject(vm.reqId).then(
                    function (data) {
                        vm.reqVersion = data.requirementVersion;
                        vm.reqObject = data;
                        $rootScope.reqVersion = vm.reqVersion;
                        $scope.name = vm.reqVersion.name;
                        $scope.description = vm.reqVersion.description;
                        if (vm.reqVersion.description != null && vm.reqVersion.description != undefined) {
                            vm.reqVersion.master.descriptionHtml = $sce.trustAsHtml(vm.reqVersion.master.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            vm.reqVersion.descriptionHtml = $sce.trustAsHtml(vm.reqVersion.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));  
                            var text =vm.reqVersion.description.replace(/(<([^>]+)>)/g, "");
                            if (text == null || text == "" || text == undefined) {
                                vm.reqVersion.master.descriptionHtml = null;
                                vm.reqVersion.descriptionHtml = null;
                            }
                        }
                        $rootScope.viewInfo.description = vm.reqVersion.name;
                        vm.loading = false;
                        vm.editStatus = false;
                        CommonService.getMultiplePersonReferences([vm.reqVersion], ['createdBy', 'assignedTo', 'modifiedBy']);
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ECO_DESCRIPTION_VALIDATION")).html();
            var reqVersionUpdatedSuccess = parsed.html($translate.instant("REQ_UPDATED_SUCCESS")).html();


            function validateRequirement() {
                var valid = true;
                if (vm.reqVersion.name == null || vm.reqVersion.name == ""
                    || vm.reqVersion.name == undefined) {
                    valid = false;
                    vm.reqVersion.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                } else if (vm.reqVersion.description == null || vm.reqVersion.description == ""
                    || vm.reqVersion.description == undefined) {
                    valid = false;
                    vm.reqVersion.description = $scope.description;
                    $rootScope.showWarningMessage(descriptionValidation);

                }
                return valid;
            }

            function updateRequirement() {
                if (validateRequirement()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    if (vm.reqVersion.master.plannedFinishDate == "" || vm.reqVersion.master.plannedFinishDate == undefined) {
                        vm.reqVersion.master.plannedFinishDate = null;
                    }
                    vm.reqVersion.assignedTo = vm.reqVersion.assignedToObject.id;
                    RequirementService.updateRequirementVersion(vm.reqId, vm.reqVersion).then(
                        function (data) {
                             vm.editDescription = false;
                            if (vm.reqVersion.description != null && vm.reqVersion.description != undefined) {
                                vm.reqVersion.master.descriptionHtml = $sce.trustAsHtml(vm.reqVersion.master.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                vm.reqVersion.descriptionHtml = $sce.trustAsHtml(vm.reqVersion.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));  
                                var text =vm.reqVersion.description.replace(/(<([^>]+)>)/g, "");
                                if (text == null || text == "" || text == undefined) {
                                    vm.reqVersion.master.descriptionHtml = null;
                                    vm.reqVersion.descriptionHtml = null;
                                }
                            }
                            $rootScope.showSuccessMessage(reqVersionUpdatedSuccess);
                            $rootScope.hideBusyIndicator();
                            vm.finishDateFlag = false;
                            $rootScope.loadRequirementDetails();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.changeFinishDate = changeFinishDate;
            vm.cancelFinishDate = cancelFinishDate;
            function changeFinishDate() {
                vm.reqVersion.editMode = true;
            }

            function cancelFinishDate() {
                vm.reqVersion.editMode = false;
                $rootScope.loadRequirementDetails();
            }

            vm.finishDateFlag = false;
            vm.changeFinishDate = changeFinishDate;
            vm.cancelFinishDate = cancelFinishDate;
            function changeFinishDate() {
                vm.finishDateFlag = true;
            }

            function cancelFinishDate() {
                vm.finishDateFlag = false;
                $rootScope.loadRequirementDetails();
            }
            vm.editRequirement = editRequirement;
            function editRequirement(){
              vm.editDescription = true;
            }
           vm.cancelRequirement = cancelRequirement;
            function cancelRequirement(){
                vm.editDescription = false;
                loadRequirementDetails();
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
                );
            }

            (function () {
                $scope.$on('app.req.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadRequirementDetails();
                        loadPersons();
                    }
                });
            })();

        }
    }
);