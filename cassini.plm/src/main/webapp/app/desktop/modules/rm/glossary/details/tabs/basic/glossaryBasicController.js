define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'

    ],
    function (module) {
        module.controller('GlossaryBasicController', GlossaryBasicController);

        function GlossaryBasicController($scope, $translate, $window, $rootScope, $timeout, $sce, $state, $stateParams, $cookies, $application,
                                         GlossaryService, CommonService, DialogService) {


            $rootScope.viewInfo.title = "Glossary";

            var vm = this;
            var glossaryId = $stateParams.glossaryId;
            $rootScope.selectedGlossary = null;
            vm.updateGlossary = updateGlossary;
            $rootScope.demoteGlossary = demoteGlossary;
            $rootScope.promoteGlossary = promoteGlossary;
            $rootScope.reviseGlossary = reviseGlossary;

            var parsed = angular.element("<div></div>");

            var statusUpdateMsg = parsed.html($translate.instant("STATUS_UPDATE_MSG")).html();
            var newRevisionCreateMsg = parsed.html($translate.instant("NEW_TERMINOLOGY_MSG")).html();
            var nameValidation = parsed.html($translate.instant("PROJECT_NAME_VALIDATION")).html();
            var glossaryUpdateMsg = parsed.html($translate.instant("TERMINOLOGY_UPDATE_MSG")).html();

            function loadGlossary() {
                vm.loading = true;
                $rootScope.selectedGlossaryLanguages = [];
                GlossaryService.getGlossary(glossaryId).then(
                    function (data) {
                        vm.glossary = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function back() {
                window.history.back();
            }

            function updateGlossary() {
                if (validate()) {
                    GlossaryService.updateGlossaryDetail($rootScope.selectedGlossary.defaultDetail).then(
                        function (data) {
                            $rootScope.selectedGlossary.defaultDetail = data;
                            $rootScope.showSuccessMessage(glossaryUpdateMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if ($rootScope.selectedGlossary.defaultDetail.name == "" || $rootScope.selectedGlossary.defaultDetail.name == null) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                    $rootScope.loadGlossaryDetails();
                }

                return valid;
            }

            function promoteGlossary() {
                GlossaryService.promoteGlossary(glossaryId).then(
                    function (data) {
                        $rootScope.loadGlossaryDetails();
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function demoteGlossary() {
                GlossaryService.demoteGlossary(glossaryId).then(
                    function (data) {
                        $rootScope.loadGlossaryDetails();
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function reviseGlossary() {
                GlossaryService.reviseGlossary(glossaryId).then(
                    function (data) {
                        vm.glossary = data;
                        $window.localStorage.setItem("lastSelectedGlossaryTab", JSON.stringify('details.entries'));
                        $state.go('app.rm.glossary.details', {glossaryId: vm.glossary.id})
                        $timeout(function () {
                            $rootScope.showSuccessMessage(newRevisionCreateMsg + " : " + vm.glossary.revision);
                        }, 100);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //loadGlossary();
            })();
        }
    }
);

