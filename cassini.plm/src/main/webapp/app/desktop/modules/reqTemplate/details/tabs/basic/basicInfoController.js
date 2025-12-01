define(
    [
        'app/desktop/modules/req/req.module',
        'app/shared/services/core/reqDocTemplateService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ReqDocTemplateBasicController', ReqDocTemplateBasicController);

        function ReqDocTemplateBasicController($scope, $rootScope,$translate, $timeout,$sce, $state, $stateParams, $cookies,CommonService,ReqDocTemplateService) {

            var vm = this;
            vm.loading = true;
            vm.reqDocId = $stateParams.reqDocId;
            vm.reqDocTemplate = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
           vm.updateReqDocTemplate = updateReqDocTemplate;

            $rootScope.loadReqDocTemplateDetails = loadReqDocTemplateDetails;
            function loadReqDocTemplateDetails() {
                vm.loading = true;
                ReqDocTemplateService.getReqDocTemplate(vm.reqDocId).then(
                    function (data) {
                        vm.reqDocTemplate = data;
                        $rootScope.reqDocTemplate = vm.reqDocTemplate;
                        $scope.name = vm.reqDocTemplate.name;
                        if (vm.reqDocTemplate.description != null && vm.reqDocTemplate.description != undefined) {
                            vm.reqDocTemplate.descriptionHtml = $sce.trustAsHtml(vm.reqDocTemplate.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        }
                        vm.loading = false;
                        vm.editStatus = false;
                        CommonService.getMultiplePersonReferences([vm.reqDocTemplate], ['createdBy', 'modifiedBy']);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var reqDocumentRevisionUpdatedSuccess = parsed.html($translate.instant("REQ_DOCUMENT_TEMPLATE_UPDATED_SUCCESS")).html();


            function validate() {
                var valid = true;
                if (vm.reqDocTemplate.name == null || vm.reqDocTemplate.name == ""
                    || vm.reqDocTemplate.name == undefined) {
                    valid = false;
                    vm.reqDocTemplate.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }
                return valid;
            }

            function updateReqDocTemplate() {
                if (validate()) {
                    $rootScope.showBusyIndicator();
                    ReqDocTemplateService.updateReqDocTemplate(vm.reqDocTemplate).then(
                        function (data) {
                            loadReqDocTemplateDetails();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(reqDocumentRevisionUpdatedSuccess);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            (function () {
                $scope.$on('app.req.documentTemplate.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadReqDocTemplateDetails();
                    }
                });
            })();
        }
    }
);