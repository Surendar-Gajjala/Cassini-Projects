define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/itemService',
    ],
    function (module) {
        module.controller('NewTemplateController', NewTemplateController);

        function NewTemplateController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $http, ItemService, $translate) {
            var vm = this;
            var updateScript = "";
            var updateSuccessMessage = $translate.instant("TEMPLATE_UPDATE_SUCCESS_MESSAGE");

            vm.template = $rootScope.pageName;
            /* ------- Create Script -----*/
            vm.update = update;
            function update() {
                $rootScope.showBusyIndicator();
                var editor = ace.edit("editor");
                var code = editor.getValue();
                vm.templateConfiguration.templateSourceCode = code;
                vm.templateConfiguration.templateName = updateScript.templateName;
                if (updateScript.templateId != null) {
                    vm.templateConfiguration.templateId = updateScript.templateId;
                }
                ItemService.updateEmailTemplate(vm.templateConfiguration).then(
                    function (data) {
                        loadTemplate();
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(updateSuccessMessage);
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);

                    }
                );

            }

            vm.templateConfiguration = {
                templateId: null,
                templateName: "",
                templateSourceCode: ""
            };

            function loadTemplate() {
                $rootScope.showBusyIndicator();
                vm.templateConfiguration.templateName = vm.template;
                ItemService.getEmailTemplate(vm.templateConfiguration).then(
                    function (data) {
                        updateScript = data;
                        loadScriptData();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            /* ------ Load Script -----*/
            function loadScriptData() {
                $timeout(function () {
                    var editor = ace.edit("editor");
                    editor.setTheme("ace/theme/xcode");
                    editor.getSession().setMode("ace/mode/ftl");
                    editor.setFontSize("14px");
                    editor.setPrintMarginColumn(false);
                    editor.session.setValue(updateScript.templateSourceCode);
                    $rootScope.hideBusyIndicator();
                }, 1000);
            }

            (function () {
                loadTemplate();

            })();
        }
    }
)
;
