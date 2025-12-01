define(
    [
        'app/desktop/modules/reqTemplate/reqDocTemplate.module',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/reqDocTemplateService'
    ],

    function (module) {
        module.controller('NewRequirementTemplateController', NewRequirementTemplateController);

        function NewRequirementTemplateController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                  $translate, RequirementService, LoginService, ReqDocTemplateService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var reqTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var reqNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var reqDescriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var reqCreatedMsg = parsed.html($translate.instant("REQUIREMENT_CREATE_MSG")).html();
            var reqPersonValidation = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            $scope.selectPerson = parsed.html($translate.instant("SELECT_PERSONS")).html();

            vm.parent = $scope.data.parent;
            vm.reqDocTemplate = $scope.data.reqDoc;
            vm.onSelectType = onSelectType;
            vm.newReqTemplate = {
                id: null,
                type: null,
                name: null,
                description: null,
                documentTemplate: vm.reqDocTemplate,
                parent: null,
                assignedTo: null,
                priority: 'LOW'
            };

            vm.prioritys = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];

            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newReqTemplate.type = objectType;
                    vm.type = objectType;
                }
            }

            function create() {
                if (validate()) {
                    if (vm.parent != null) {
                        vm.newReqTemplate.parent = vm.parent.id;
                    }
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ReqDocTemplateService.createRequirementTemplate(vm.newReqTemplate).then(
                        function (data) {
                            vm.newReqTemplate = data;
                            $scope.callback(vm.newReqTemplate);
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage(reqCreatedMsg);
                            vm.newReqTemplate = {
                                id: null,
                                type: null,
                                name: null,
                                description: null,
                                documentTemplate: null,
                                parent: null,
                                assignedTo: null,
                                priority: 'LOW'
                            };
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newReqTemplate.type == null || vm.newReqTemplate.type == undefined ||
                    vm.newReqTemplate.type == "") {
                    $rootScope.showErrorMessage(reqTypeValidation);
                    valid = false;
                }
                else if (vm.newReqTemplate.name == null || vm.newReqTemplate.name == undefined ||
                    vm.newReqTemplate.name == "") {
                    $rootScope.showErrorMessage(reqNameValidation);
                    valid = false;
                }
                else if (vm.newReqTemplate.description == null || vm.newReqTemplate.description == undefined ||
                    vm.newReqTemplate.description == "") {
                    $rootScope.showErrorMessage(reqDescriptionValidation);
                    valid = false;
                } else if (vm.newReqTemplate.assignedTo == null || vm.newReqTemplate.assignedTo == undefined ||
                    vm.newReqTemplate.assignedTo == "") {
                    $rootScope.showErrorMessage(reqPersonValidation);
                    valid = false;
                }

                return valid;
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
                loadPersons();
                $rootScope.$on('app.requirement.template.new', create);
            })();
        }
    }
);