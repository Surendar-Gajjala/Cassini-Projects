define(
    [
        'app/desktop/modules/req/req.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/reqDocTemplateService'
    ],
    function (module) {

        module.controller('NewReqDocTemplateController', NewReqDocTemplateController);

        function NewReqDocTemplateController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                             ReqDocumentService, ReqDocTemplateService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var reqNumberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var reqTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var reqNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var reqPersonValidation = parsed.html($translate.instant("REQ_OWNER_VALIDATION")).html();
            var reqDocSaveAsTemplateSuccess = parsed.html($translate.instant("REQ_DOC_SAVE_AS_TEMPLATE_SUCCESS")).html();
            $scope.selectTemplate = parsed.html($translate.instant("SELECT_TEMPLATE")).html();

            $scope.personDetails = $rootScope.loginPersonDetails;

            vm.onSelectType = onSelectType;

            vm.newReqDocTemplate = {
                id: null,
                type: null,
                name: null,
                description: null,
                documentReviewer: false,
                requirementReviewer: false
            };

            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newReqDocTemplate.type = objectType;
                    vm.type = objectType;

                }
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newReqDocTemplate.type = vm.type;
                    ReqDocTemplateService.createReqDocTemplate(vm.newReqDocTemplate).then(
                        function (data) {
                            vm.newReqDocTemplate = data;
                            vm.newReqDocTemplate = {
                                id: null,
                                type: null,
                                name: null,
                                description: null,
                                documentReviewer: false,
                                requirementReviewer: false
                            };
                            $scope.callback(vm.newReqDocTemplate);
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newReqDocTemplate.type == null || vm.newReqDocTemplate.type == undefined ||
                    vm.newReqDocTemplate.type == "") {
                    $rootScope.showErrorMessage(reqTypeValidation);
                    valid = false;
                }
                else if (vm.newReqDocTemplate.name == null || vm.newReqDocTemplate.name == undefined ||
                    vm.newReqDocTemplate.name == "") {
                    $rootScope.showErrorMessage(reqNameValidation);
                    valid = false;
                }


                return valid;
            }

            function saveAsTemplate() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newReqDocTemplate.type = vm.type;
                    ReqDocumentService.saveAsRequirementDocumentTemplate(vm.newReqDocTemplate, vm.docRevision.id).then(
                        function (data) {
                            vm.newReqDocTemplate = data;
                            vm.newReqDocTemplate = {
                                id: null,
                                type: null,
                                name: null,
                                description: null,
                                documentReviewer: false,
                                requirementReviewer: false
                            };
                            $scope.callback(vm.newReqDocTemplate);
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage(reqDocSaveAsTemplateSuccess);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                vm.docRevision = $scope.data.documentRevision;
                $rootScope.$on('app.req.doc.template.new', create);
                $rootScope.$on('app.req.doc.template.save', saveAsTemplate);
            })();
        }
    }
)
;