define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/reqDocTemplateService'
    ],
    function (module) {
        module.controller('ReqTemplateBasicInfoController', ReqTemplateBasicInfoController);

        function ReqTemplateBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, LoginService, RequirementService, ReqDocumentService, ReqDocTemplateService, CommonService, $translate) {
            var vm = this;
            vm.loading = true;
            vm.reqId = $stateParams.requirementId;
            vm.reqTemplate = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateRequirement = updateRequirement;
            $rootScope.loadRequirementDetails = loadRequirementDetails;
            function loadRequirementDetails() {
                vm.loading = true;
                ReqDocTemplateService.getRequirementTemplate(vm.reqId).then(
                    function (data) {
                        vm.reqTemplate = data;
                        $rootScope.reqTemplate = vm.reqTemplate;
                        $scope.name = vm.reqTemplate.name;
                        $scope.description = vm.reqTemplate.description;
                        if (vm.reqTemplate.description != null && vm.reqTemplate.description != undefined) {
                            vm.reqTemplate.descriptionHtml = $sce.trustAsHtml(vm.reqTemplate.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            vm.reqTemplate.descriptionHtml = $sce.trustAsHtml(vm.reqTemplate.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        }
                        CommonService.getMultiplePersonReferences([vm.reqTemplate], ['createdBy', 'assignedTo', 'modifiedBy']);
                        vm.loading = false;
                        vm.editStatus = false;
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var reqTemplateUpdatedSuccess = parsed.html($translate.instant("REQ_TEMPLATE_UPDATED_SUCCESS")).html();


            function validateRequirement() {
                var valid = true;
                if (vm.reqTemplate.name == null || vm.reqTemplate.name == ""
                    || vm.reqTemplate.name == undefined) {
                    valid = false;
                    vm.reqTemplate.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);
                }
                return valid;
            }

            function updateRequirement() {
                if (validateRequirement()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.reqTemplate.assignedTo = vm.reqTemplate.assignedToObject.id;
                    ReqDocTemplateService.updateRequirementTemplate(vm.reqTemplate).then(
                        function (data) {
                            $rootScope.showSuccessMessage(reqTemplateUpdatedSuccess);
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
                $scope.$on('app.req.template.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadRequirementDetails();
                        loadPersons();
                    }
                });
            })();

        }
    }
);