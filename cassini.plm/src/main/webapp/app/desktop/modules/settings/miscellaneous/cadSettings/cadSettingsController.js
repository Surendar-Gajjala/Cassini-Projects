define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/pdmService'
    ],
    function (module) {
        module.controller('CadSettingsController', CadSettingsController);

        function CadSettingsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                       CommonService, MeasurementService, $translate, ItemTypeService, PDMService, AttachmentService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var templateDialogMsg = parsed.html($translate.instant("DRAWING_TEMPLATE_DIALOG_MSG")).html();
            var drawingTemplateDeleted = parsed.html($translate.instant("DRAWING_TEMPLATE_DELETED")).html();
            var pleaseEnterName = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var pleaseSelectFile = parsed.html($translate.instant("P_S_TEMPLATE_FILE")).html();
            $scope.editTitle = parsed.html($translate.instant("EDIT")).html();
            $scope.deleteTitle = parsed.html($translate.instant("DELETE")).html();
            $scope.cancelChangesTitle = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            $scope.saveTitle = parsed.html($translate.instant("SAVE")).html();
            $scope.previewTitle = parsed.html($translate.instant("FILE_PREVIEW")).html();
            $scope.addDrawingTemplate = parsed.html($translate.instant("ADD_DRAWING_TEMPLATE")).html();
            var emptyTemplate = {
                id: null,
                name: null,
                description: null,
                templateFile: null,
                file: null
            };

            vm.loadDrawingTemplates = loadDrawingTemplates;
            function loadDrawingTemplates() {
                PDMService.getDrawingTemplates().then(
                    function (data) {
                        vm.drawingTemplates = data;
                        $timeout(function () {
                            $(".responsive-table").height($(".miscellaneous-right").outerHeight() - 80);
                        }, 200);
                    }
                )
            }

            function resizeScreen() {
                $(".responsive-table").height($(".miscellaneous-right").outerHeight() - 80);
            }

            vm.addTemplate = addTemplate;
            vm.editTemplate = editTemplate;
            vm.deleteTemplate = deleteTemplate;
            vm.cancelChanges = cancelChanges;
            vm.saveDrawingTemplate = saveDrawingTemplate;

            function cancelChanges(template) {
                if (template.id == null || template.id == "" || template.id == undefined) {
                    vm.drawingTemplates.splice(vm.drawingTemplates.indexOf(template), 1);
                } else {
                    template.name = template.oldName;
                    template.description = template.oldDescription;
                    template.editMode = false;
                }
            }

            vm.newTemplates = 0;
            function addTemplate() {
                var record = angular.copy(emptyTemplate);
                record.editMode = true;
                vm.drawingTemplates.push(record);
                vm.newTemplates++;
            }

            function editTemplate(template) {
                template.oldName = template.name;
                template.oldDescription = template.description;
                template.editMode = true;
            }

            function deleteTemplate(template) {
                if (template.id == null || template.id == "" || template.id == undefined) {
                    vm.drawingTemplates.splice(vm.drawingTemplates.indexOf(template), 1);
                    vm.newTemplates--;
                } else {
                    var options = {
                        title: confirmation,
                        message: templateDialogMsg,
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            PDMService.deleteDrawingTemplate(template.id).then(
                                function (data) {
                                    loadDrawingTemplates();
                                    $rootScope.showSuccessMessage(drawingTemplateDeleted);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })
                }
            }

            vm.filePreview = filePreview;
            function filePreview(template) {
                var url = "{0}//{1}/api/col/attachments/{2}/preview".
                    format(window.location.protocol, window.location.host, template.attachmentId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = template.attachmentName;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            function saveDrawingTemplate(template) {
                if (validateTemplate(template)) {
                    if (template.id == null || template.id == "" || template.id == undefined) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        PDMService.createDrawingTemplate(template).then(
                            function (data) {
                                var templateData = data;
                                data.id = templateData.id;
                                if (template.file != null && template.file != "") {
                                    AttachmentService.saveAttachment(templateData.id, templateData.objectType, template.file).then(
                                        function (data) {
                                            templateData.templateFile = data[0].id;
                                            PDMService.updateDrawingTemplate(templateData).then(
                                                function (data) {
                                                    vm.newTemplates--;
                                                    template.editMode = false;
                                                    if (vm.newTemplates == 0) {
                                                        loadDrawingTemplates();
                                                    }
                                                    $rootScope.hideBusyIndicator();
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                            )
                                        }
                                    )
                                } else {
                                    loadDrawingTemplates();
                                    $rootScope.hideBusyIndicator();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        $rootScope.showBusyIndicator($('.view-container'));
                        PDMService.updateDrawingTemplate(template).then(
                            function (data) {
                                var templateData = data;
                                if (template.file != null && template.file != "") {
                                    AttachmentService.saveAttachment(templateData.id, templateData.objectType, template.file).then(
                                        function (data) {
                                            templateData.templateFile = data[0].id;
                                            PDMService.updateDrawingTemplate(templateData).then(
                                                function (data) {
                                                    loadDrawingTemplates();
                                                    $rootScope.hideBusyIndicator();
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                            )
                                        }
                                    )
                                } else {
                                    loadDrawingTemplates();
                                    $rootScope.hideBusyIndicator();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validateTemplate(template) {
                var valid = true;
                if (template.name == null || template.name == "" || template.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterName);
                } else if ((template.templateFile == null || template.templateFile == "" || template.templateFile == undefined) && (template.file == null || template.file == "" || template.file == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectFile);
                }

                return valid;
            }


            (function () {
                loadDrawingTemplates();
                $(window).resize(function () {
                    resizeScreen();
                })
                $rootScope.hideBusyIndicator();
            })();
        }
    }
)
;