define(
    [
        'app/desktop/modules/main/main.module',
        'app/shared/services/core/objectFileService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/recentlyVisitedService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/all-view-icons/allViewIconsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/reqDocumentService'
    ],

    function (module) {
        module.controller('CommonController', CommonController);

        function CommonController($scope, $rootScope, $timeout, $interval, $state, $location, $sce, $application, $translate, $window, ObjectFileService,
                                  CommonService, ItemTypeService, ItemService, AttributeAttachmentService, ECOService, MfrService, MfrPartsService, ReqDocumentService,
                                  WorkflowDefinitionService, ProjectService, SpecificationsService, RequirementService, CustomObjectService, MESObjectTypeService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            $rootScope.selectMfrTitle = parsed.html($translate.instant("SELECT_MFR_TITLE")).html();
            $rootScope.buttonSelect = parsed.html($translate.instant("UPDATE")).html();
            $rootScope.imageMessage = parsed.html($translate.instant("IMAGE_SUCCESS_MESSAGE")).html();
            $rootScope.imageWarningMessage = parsed.html($translate.instant("IMAGE_WARNING_MESSAGE")).html();
            $rootScope.attachmentWarningMessage = parsed.html($translate.instant("ATTACHMENT_WARNING_MESSAGE")).html();
            $rootScope.attachmentMessage = parsed.html($translate.instant("ATTACHMENT_MESSAGE")).html();
            $rootScope.attachmentDialogTitle = parsed.html($translate.instant("ATTACHMENT_DIALOG_TITLE")).html();
            $rootScope.attachmentDialogMessage = parsed.html($translate.instant("ATTACHMENT_DIALOG_MESSAGE")).html();
            $rootScope.deleteAttachmentConfirmation = parsed.html($translate.instant("DELETE_ATTACHMENT_CONFIRMATION")).html();
            $rootScope.deleteMessage = parsed.html($translate.instant("ATTACHMENT_DELETE_MESSAGE")).html();
            $rootScope.deleteDialogTitle = parsed.html($translate.instant("ATTACHMENT_DIALOG_DELETE_TITLE")).html();
            $rootScope.deleteDialogMessage = parsed.html($translate.instant("ATTACHMENT_DIALOG_DELETE_MESSAGE")).html();
            $rootScope.objectItemInfo = parsed.html($translate.instant("OBJECT_ITEM_ADDED_INFO")).html();
            $rootScope.objectItemRevisionInfo = parsed.html($translate.instant("OBJECT_ITEM_REVISION_ADDED_INFO")).html();
            $rootScope.objectProjectInfo = parsed.html($translate.instant("OBJECT_PROJECT_ADDED_INFO")).html();
            $rootScope.objectECOInfo = parsed.html($translate.instant("OBJECT_ECO_ADDED_INFO")).html();
            $rootScope.objectWorkflowInfo = parsed.html($translate.instant("OBJECT_WORKFLOW_ADDED_INFO")).html();
            $rootScope.objectMfrInfo = parsed.html($translate.instant("OBJECT_MFR_ADDED_INFO")).html();
            $rootScope.objectMfrPartInfo = parsed.html($translate.instant("OBJECT_MFR_PART_ADDED_INFO")).html();
            $rootScope.changeImageMessage = parsed.html($translate.instant("CHANGE_IMAGE")).html();
            $rootScope.addAttachments = parsed.html($translate.instant("ADD_ATTACHMENTS")).html();
            $rootScope.saveAttachmentsTitle = parsed.html($translate.instant("SAVE_ATTACHMENTS")).html();
            $rootScope.deleteAttachment = parsed.html($translate.instant("DELETE_ATTACHMENT")).html();
            $rootScope.changeValue = parsed.html($translate.instant("CHANGE_VALUE")).html();
            $rootScope.changeDate = parsed.html($translate.instant("CHANGE_DATE")).html();
            $rootScope.changeTime = parsed.html($translate.instant("CHANGE_TIME")).html();
            $rootScope.clickToSetDate = parsed.html($translate.instant("CLICK_TO_SET_DATE")).html();
            $rootScope.clickToSetTime = parsed.html($translate.instant("CLICK_TO_SET_TIME")).html();
            $rootScope.clickToChangeTime = parsed.html($translate.instant("CLICK_TO_CHANGE_TIME")).html();
            $rootScope.addImage = parsed.html($translate.instant("ADD_IMAGE")).html();
            $rootScope.cancelChangesTitle = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            $rootScope.addAttribute = parsed.html($translate.instant("ADD_ATTRIBUTE")).html();
            $rootScope.changeAttributeTitle = parsed.html($translate.instant("CHANGE_ATTRIBUTE")).html();
            $rootScope.saveAttributeTitle = parsed.html($translate.instant("SAVE_ATTRIBUTE")).html();
            $rootScope.changeCurrencyValueTitle = parsed.html($translate.instant("CHANGE_CURRENCY_VALUE")).html();
            $rootScope.saveCurrencyTitle = parsed.html($translate.instant("SAVE_CURRENCY_VALUE")).html();
            $rootScope.enterValue = parsed.html($translate.instant("ENTER_VALUE")).html();
            $rootScope.attributeCreateMessage = parsed.html($translate.instant("ATTRIBUTE_SAVED_MESSAGE")).html();
            $rootScope.attributeUpdateMessage = parsed.html($translate.instant("ATTRIBUTE_UPDATE_MESSAGE")).html();
            $rootScope.integerValid = parsed.html($translate.instant("INTEGER_VALIDATE_MESSAGE")).html();
            $rootScope.timeAttributeMessage = parsed.html($translate.instant("ITEM_TIME_ATTRIBUTE_MESSAGE")).html();
            $rootScope.timestampAttributeMessage = parsed.html($translate.instant("ATTRIBUTE_TIMESTAMP_MESSAGE")).html();
            $rootScope.Clicktoshowlargeimage = parsed.html($translate.instant("CLICK_LARGE_IMAGE")).html();
            $rootScope.ClickAddWidget = parsed.html($translate.instant("ADD_HOME_WIDGETS")).html();
            $rootScope.fileHistoryTitle = parsed.html($translate.instant("FILE_HISTORY_TITLE")).html();
            $rootScope.fileDownloadHistoryTitle = parsed.html($translate.instant("FILE_DOWNLOAD_HISTORY_TITLE")).html();
            $rootScope.setCurrencyValue = parsed.html($translate.instant("CLICK_TO_SET_CURRENCY_VALUE")).html();
            $rootScope.removeThisColumn = parsed.html($translate.instant("REMOVE_THIS_COLUMN")).html();
            $rootScope.delete = $sce.trustAsHtml(parsed.html($translate.instant("DELETE")).html());
            $rootScope.clickToShowLargeImage = parsed.html($translate.instant("CLICK_TO_SHOW_LARGE_IMAGE")).html();
            $rootScope.cancelTitle = parsed.html($translate.instant("CANCEL")).html();
            $rootScope.configurableItem = parsed.html($translate.instant("CONFIGURABLE_ITEM")).html();
            var dateValidationMsg = parsed.html($translate.instant("DATE_VALIDATION")).html();

            $rootScope.selectEcoTitle = parsed.html($translate.instant("SELECT_ECO_TITLE")).html();
            $rootScope.selectMfrPartTitle = parsed.html($translate.instant("SELECT_MFR_PART_TITLE")).html();
            $rootScope.selectItemTitle = parsed.html($translate.instant("SELECT_ITEM_TITLE")).html();
            $rootScope.selectReqTitle = parsed.html($translate.instant("SELECT_REQUIREMENT_TITLE")).html();
            $rootScope.selectItemRevisionTitle = parsed.html($translate.instant("SELECT_ITEM_REVISION_TITLE")).html();
            $rootScope.selectPersonTitle = parsed.html($translate.instant("SELECT_PERSON_TITLE")).html();
            $rootScope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW_TITLE")).html();
            $rootScope.ClickAddWidget = parsed.html($translate.instant("ADD_HOME_WIDGETS")).html();
            $rootScope.dataSpaceInformation = parsed.html($translate.instant("ABOUT")).html();
            $rootScope.clickOnToVersion = parsed.html($translate.instant("CLICK_ON_VERSION_DOWNLOAD")).html();
            $rootScope.clickToAddFilesTitle = parsed.html($translate.instant("CLICK_TO_ADD_FILES")).html();
            $rootScope.clickOnVersionDownload = parsed.html($translate.instant("CLICK_ON_VERSION_DOWNLOAD")).html();
            $rootScope.fileVersionHasNoComments = parsed.html($translate.instant("FILE_VERSION_COMMENTS")).html();
            $rootScope.hideComments = parsed.html($translate.instant("HIDE_COMMENTS")).html();
            $rootScope.showComment = parsed.html($translate.instant("SHOW_COMMENT")).html();
            $rootScope.FileVersionHasNoDownloads = parsed.html($translate.instant("FILE_VERSION_DOWNLOADS")).html();
            $rootScope.hideDownloads = parsed.html($translate.instant("HIDE_DOWNLOADS")).html();
            $rootScope.showDownloads = parsed.html($translate.instant("SHOW_DOWNLOADS")).html();
            $rootScope.NoDownloadHistory = parsed.html($translate.instant("NO_DOWNLOAD_HISTORY")).html();
            $rootScope.reNameReportTitle = parsed.html($translate.instant("RENAME_REPORT_TITLE")).html();
            $rootScope.reNameFile = parsed.html($translate.instant("RENAME_FILE")).html();
            $rootScope.enterText = parsed.html($translate.instant("ENTER_TEXT")).html();
            $rootScope.reNameWarningMsg = parsed.html($translate.instant("RENAME_FILE_TEXT_MESSAGE")).html();
            $rootScope.reNameSuccessMsg = parsed.html($translate.instant("RENAME_FILE_TEXT_SUCCESS_MESSAGE")).html();
            $rootScope.downloadTitle = parsed.html($translate.instant("DOWNLOAD_TITLE")).html();
            $rootScope.copyFileToClipboard = parsed.html($translate.instant("CLIPBOARD_FILES")).html();
            $rootScope.add = parsed.html($translate.instant("ADD")).html();
            $rootScope.done = parsed.html($translate.instant("DONE")).html();
            $rootScope.about = parsed.html($translate.instant("ABOUT")).html();
            $rootScope.backTitle = parsed.html($translate.instant("DETAILS_BACK")).html();
            $rootScope.addFolder = parsed.html($translate.instant("ADD_FOLDER")).html();
            $rootScope.addFolderAndClipboard = parsed.html($translate.instant("ADD_FOLDER_CLIPBOARD")).html();
            $rootScope.affectedItems = parsed.html($translate.instant("AFFECTED_ITEMS")).html();
            $rootScope.actionDeleted = parsed.html($translate.instant("ACTION_DELETED")).html();
            $rootScope.ecoCreated = parsed.html($translate.instant("ECO_CREATED")).html();
            $rootScope.promoted = parsed.html($translate.instant("PROMOTED")).html();
            $rootScope.added = parsed.html($translate.instant("ADDED")).html();
            $rootScope.backTitle = parsed.html($translate.instant("BACK")).html();
            $rootScope.expandAll = parsed.html($translate.instant("EXPAND_ALL")).html();
            $rootScope.collapseAll = parsed.html($translate.instant("COLLAPSE_ALL")).html();
            $rootScope.save = parsed.html($translate.instant("SAVE")).html();
            $rootScope.preferredPage = parsed.html($translate.instant("PREFERRED_PAGE")).html();
            $rootScope.assetAlreadyAdded = parsed.html($translate.instant("ASSET_ALREADY_ADDED")).html();
            $rootScope.cannotDeleteAddedDeclaration = parsed.html($translate.instant("CANNOT_DELETE_ADDED_DECLARATION")).html();
            $rootScope.cannotDeleteMeterValues = parsed.html($translate.instant("CANNOT_DELETE_ADDED_METER_VALUES")).html();
            $rootScope.noPermission = parsed.html($translate.instant("NO_PERMISSION_PERFORM")).html();
            $rootScope.cannotDeleteAddedSubstance = parsed.html($translate.instant("CANNOT_DELETE_ADDED_SUBSTANCE")).html();
            $rootScope.cannotDeleteAddedSpecification = parsed.html($translate.instant("CANNOT_DELETE_ADDED_SPECIFICATION")).html();
            $rootScope.cannotDeleteAddedPermission = parsed.html($translate.instant("CANNOT_DELETE_ADDED_PERMISSION")).html();
            $rootScope.selectObjectTitle = parsed.html($translate.instant("SELECT")).html();
            $rootScope.objectTitle = parsed.html($translate.instant("OBJECT_SELECT")).html();

            var currencyMap = new Hashtable();
            $rootScope.changeAttribute = changeAttribute;
            $rootScope.$on("loadMesObjectFilesCount", function (event, args) {
                ObjectFileService.getObjectFilesCount(args.objectId, args.objectType).then(
                    function (data) {
                        var filesTab = document.getElementById("files");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = args.heading +
                                tmplStr.format(data);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            });

            $rootScope.$on("loadMroObjectFilesCount", function (event, args) {
                ObjectFileService.getObjectFilesCount(args.objectId, args.objectType).then(
                    function (data) {
                        var filesTab = document.getElementById("files");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = args.heading +
                                tmplStr.format(data);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            });


            $rootScope.$on("loadPgcObjectFilesCount", function (event, args) {
                ObjectFileService.getObjectFilesCount(args.objectId, args.objectType).then(
                    function (data) {
                        var filesTab = document.getElementById("files");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = args.heading +
                                tmplStr.format(data);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            });


            $rootScope.selectObjectValues = function (attribute) {
                var options = null;
                if (attribute.attributeDef.refType == 'MANUFACTURER') {

                    options = {
                        title: $rootScope.selectMfrTitle,
                        template: 'app/desktop/modules/select/mfrSelectionView.jsp',
                        controller: 'MfrSelectionController as mfrSelectVm',
                        resolve: 'app/desktop/modules/select/mfrSelectionController',
                        width: 600,
                        side: 'left',
                        buttons: [
                            {text: $rootScope.buttonSelect, broadcast: 'app.select.mfr'}
                        ],
                        callback: function (result) {
                            attribute.refValue = result.id;
                            attribute.ref = result.name;
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'MANUFACTURERPART') {
                    options = {
                        title: $rootScope.selectMfrPartTitle,
                        template: 'app/desktop/modules/select/mfrPartSelectionView.jsp',
                        controller: 'MfrPartSelectionController as mfrPartSelectVm',
                        resolve: 'app/desktop/modules/select/mfrPartSelectionController.js',
                        width: 600,
                        side: 'left',
                        buttons: [
                            {text: $rootScope.buttonSelect, broadcast: 'app.select.mfrPart'}
                        ],
                        callback: function (result) {
                            attribute.refValue = result.id;
                            attribute.ref = result.partName;
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'CHANGE') {
                    options = {
                        title: $rootScope.selectEcoTitle,
                        template: 'app/desktop/modules/select/changeSelectionView.jsp',
                        controller: 'ChangeSelectionController as changeSelectVm',
                        resolve: 'app/desktop/modules/select/changeSelectionController',
                        width: 600,
                        side: 'left',
                        buttons: [
                            {text: $rootScope.buttonSelect, broadcast: 'app.select.change'}
                        ],
                        callback: function (result) {
                            attribute.refValue = result.id;
                            attribute.ref = result.ecoNumber;
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'ITEM') {
                    options = {
                        title: $rootScope.selectItemTitle,
                        template: 'app/desktop/modules/select/itemSelectionView.jsp',
                        controller: 'ItemSelectionController as itemSelectVm',
                        resolve: 'app/desktop/modules/select/itemSelectionController',
                        width: 600,
                        side: 'left',
                        buttons: [
                            {text: $rootScope.buttonSelect, broadcast: 'app.select.item'}
                        ],
                        callback: function (result) {
                            attribute.refValue = result.id;
                            attribute.ref = result.itemNumber;
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'WORKFLOW') {
                    options = {
                        title: $rootScope.selectWorkflowTitle,
                        template: 'app/desktop/modules/select/workflowSelectionView.jsp',
                        controller: 'WorkflowSelectionController as wrkSelectVm',
                        resolve: 'app/desktop/modules/select/workflowSelectionController',
                        width: 600,
                        side: 'left',
                        buttons: [
                            {text: $rootScope.buttonSelect, broadcast: 'app.select.workflow'}
                        ],
                        callback: function (result) {
                            attribute.refValue = result.id;
                            attribute.ref = result.name;
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'ITEMREVISION') {
                    options = {
                        title: $rootScope.selectItemRevisionTitle,
                        template: 'app/desktop/modules/select/itemRevisionSelection.jsp',
                        controller: 'ItemRevisionSelectionController as itemRevSelectVm',
                        resolve: 'app/desktop/modules/select/itemRevisionSelectionController',
                        width: 600,
                        side: 'left',
                        buttons: [
                            {text: $rootScope.buttonSelect, broadcast: 'app.select.itemRevision'}
                        ],
                        callback: function (result) {
                            attribute.refValue = result.id;
                            attribute.revision = result.revision;
                            attribute.itemNumber = result.itemMasterObject.itemNumber;
                            attribute.phase = result.lifeCyclePhase.phase;
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'PERSON') {
                    options = {
                        title: $rootScope.selectPersonTitle,
                        template: 'app/desktop/modules/select/personSelectionView.jsp',
                        controller: 'PersonSelectionController as personSelectVm',
                        resolve: 'app/desktop/modules/select/personSelectionController',
                        width: 600,
                        side: 'left',
                        buttons: [
                            {text: $rootScope.buttonSelect, broadcast: 'app.select.person'}
                        ],
                        callback: function (result) {
                            attribute.refValue = result.id;
                            attribute.ref = result.firstName;
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
            };

            $rootScope.showRichTextSidePanel = showRichTextSidePanel;
            function showRichTextSidePanel(richText, objAttr, project) {
                var options = {
                    title: 'Rich Text',
                    template: 'app/desktop/modules/main/richTextSidePanelView.jsp',
                    controller: 'RichTextSidePanelController as richTextSidePanelVm',
                    resolve: 'app/desktop/modules/main/richTextSidePanelController',
                    width: 600,
                    side: 'left',
                    data: {
                        selectedRichTextObj: richText,
                        selectedObjectAttribute: objAttr,
                        selectedProject: project
                    },
                    callback: function (result) {

                    }
                };

                $rootScope.showSidePanel(options);

            }

            /*-----------  To check Required Attributes empty or not ----------*/

            $rootScope.checkAttribute = function (attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.longTextValue != null && attribute.longTextValue != undefined && attribute.longTextValue != "") ||
                    (attribute.richTextValue != null && attribute.richTextValue != undefined && attribute.richTextValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.attributeDef.measurement != null && attribute.attributeDef.measurement != undefined && attribute.measurementUnit != null && attribute.measurementUnit != undefined && attribute.measurementUnit != ""
                    && attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.booleanValue != null && attribute.booleanValue != undefined && attribute.booleanValue != "") ||
                    (attribute.hyperLinkValue != null && attribute.hyperLinkValue != undefined && attribute.hyperLinkValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.attributeDef.listMultiple == true && attribute.mlistValue.length != 0) ||
                    (attribute.attributeDef.listMultiple == false && attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            };

            $window.localStorage.setItem("lastSelectedRequirementHeight", "");

            $rootScope.stringValue = null;
            $rootScope.integerValue = null;
            $rootScope.dateValue = null;
            $rootScope.doubleValue = null;
            $rootScope.booleanValue = null;
            $rootScope.currencyValue = null;

            function changeAttribute(attribute) {
                $rootScope.stringValue = null;
                $rootScope.integerValue = null;
                $rootScope.dateValue = null;
                $rootScope.doubleValue = null;
                $rootScope.booleanValue = null;
                $rootScope.currencyValue = null;
                $rootScope.textValue = null;
                $rootScope.longTextValue = null;
                $rootScope.hyperLinkValue = null;
                $rootScope.listValue = null;
                attribute.editMode = true;
                $rootScope.stringValue = attribute.value.stringValue;
                $rootScope.integerValue = attribute.value.integerValue;
                $rootScope.dateValue = attribute.value.dateValue;
                $rootScope.doubleValue = attribute.value.doubleValue;
                $rootScope.booleanValue = attribute.value.booleanValue;
                $rootScope.currencyValue = attribute.value.currencyValue;
                $rootScope.listValue = attribute.value.listValue;
                $rootScope.mlistValue = attribute.value.mlistValue;
                $rootScope.textValue = attribute.value.textValue;
                $rootScope.longTextValue = attribute.value.longTextValue;
                $rootScope.hyperLinkValue = attribute.value.hyperLinkValue;
                $rootScope.attributeMeasurementUnit = attribute.value.measurementUnit;

            }

            $rootScope.cancelChanges = cancelChanges;

            function cancelChanges(attribute) {
                attribute.editMode = false;
                attribute.value.refValue = vm.previousValue;
                attribute.changeCurrency = false;
                attribute.value.stringValue = $rootScope.stringValue;
                attribute.value.integerValue = $rootScope.integerValue;
                attribute.value.dateValue = $rootScope.dateValue;
                attribute.value.doubleValue = $rootScope.doubleValue;
                attribute.value.booleanValue = $rootScope.booleanValue;
                attribute.value.currencyValue = $rootScope.currencyValue;
                attribute.value.listValue = $rootScope.listValue;
                attribute.value.mlistValue = $rootScope.mlistValue;
                attribute.value.longTextValue = $rootScope.longTextValue;
                attribute.value.hyperLinkValue = $rootScope.hyperLinkValue;
                attribute.value.measurementUnit = $rootScope.attributeMeasurementUnit;
            }

            $rootScope.showHyperLink = showHyperLink;
            function showHyperLink(url) {
                $window.open(url);

            };


            $rootScope.showSelectionDialog = showSelectionDialog;
            function showSelectionDialog(objectType, attribute) {
                vm.previousValue = attribute.value.refValue;
                var objectSelector = $application.getObjectSelector(objectType);
                if (objectSelector != null) {
                    if (objectType == "PERSON" && attribute.value.refValue != null && attribute.value.refValue != "") {
                        $rootScope.personObjectValue = attribute.value.refValue.id;
                    }
                    objectSelector.show($rootScope, function (object, displayValue) {
                        attribute.refValue = object.id;
                        attribute.editMode = true;
                        attribute.value.refValue = object;

                    });
                }
            }

            /*
             * Icon Colours
             * */

            $rootScope.getIconColor = function (fileName) {
                var fileExtensionPattern = /\.([0-9a-z]+)(?=[?#])|(\.)(?:[\w]+)$/gmi;
                if (fileName.includes(".")) {
                    var extension = fileName.toLowerCase().match(fileExtensionPattern)[0];
                    if ([".html", ".csv", ".ttf", ".exe", ".log", ".bat", ".css", ".jsp", ".js", ".sql", ".java", ".class", ".c", ".net", ".py", ".ipynb"].indexOf(extension) > -1) {
                        return "{'color': '#e699ff'}";
                    }
                    else if ([".png", ".jpeg", ".jpg", ".bmp", ".eps", ".gif", ".pict", ".esd", ".tif"].indexOf(extension) > -1) {
                        return "{'color': '#9999ff'}";
                    }
                    else if ([".xlsx", ".xls"].indexOf(extension) > -1) {
                        return "{'color': '#00b377'}";
                    }
                    else if ([".mp4", ".avi", ".flv", ".mpg", ".mpeg", ".mov", ".wmv"].indexOf(extension) > -1) {
                        return "{'color': '#ff6600'}";
                    }
                    else if ([".mp3", ".wav", ".mid", ".mkv"].indexOf(extension) > -1) {
                        return "{'color': '#ff6600'}";
                    }
                    else if ([".docx", ".doc"].indexOf(extension) > -1) {
                        return "{'color': '#4d79ff'}";
                    }
                    else if ([".pdf"].indexOf(extension) > -1) {
                        return "{'color': '#ff0000'}";
                    }
                    else if ([".ppt"].indexOf(extension) > -1) {
                        return "{'color': '#ff531a'}";
                    }
                    else if ([".txt", ".rtf", ".wps", ".wpd"].indexOf(extension) > -1) {
                        return "{'color': '#5b1f07'}";
                    }
                    else if ([".zip", ".war", ".jar", ".arc", ".arj", ".gz", ".hqx", ".sit", ".tar", ".z"].indexOf(extension) > -1) {
                        return "{'color': '#ffb84d'}";
                    }
                    else {
                        return "{'color': '#ff6600'}";
                    }
                }
            }
            /*
             *Conversion Date
             * */


            function convertYear(date) {
                var year = parseInt("20" + date.substr(6, 2));
                var dateMonth = date.slice(0, 6);
                return dateMonth + year;
            }

            $rootScope.convertDate = convertDate;
            function convertDate(date) {
                var dateRegex2 = /^\d{1,2}\/\d{1,2}\/\d{2}$/;

                if (dateRegex2.test(date)) {
                    date = convertYear(date);
                }
                return date;
            }

            /*
             *
             * Date Validation
             * */

            $rootScope.dateValidation = dateValidation;
            function dateValidation(date) {
                var valid = true;
                var dateRegex2 = /^\d{1,2}\/\d{1,2}\/\d{2}$/;
                var dateRegex4 = /^\d{1,2}\/\d{1,2}\/\d{4}$/;
                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                    dateRegex2 = /^\d{1,2}\ \d{1,2}\ \d{2}$/;
                    dateRegex4 = /^\d{1,2}\ \d{1,2}\ \d{4}$/;
                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                    dateRegex2 = /^\d{1,2}\.\d{1,2}\.\d{2}$/;
                    dateRegex4 = /^\d{1,2}\.\d{1,2}\.\d{4}$/;
                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                    dateRegex2 = /^\d{1,2}\-\d{1,2}\-\d{2}$/;
                    dateRegex4 = /^\d{1,2}\-\d{1,2}\-\d{4}$/;
                }

                if (dateRegex2.test(date)) {
                    date = convertYear(date);
                    valid = true;
                }
                if (!dateRegex4.test(date)) {
                    valid = false;
                    $rootScope.showWarningMessage(dateValidationMsg);
                }
                return valid;
            }

            /*
             * For Checking Attribute validations
             * */
            $rootScope.checkAttributeValidations = checkAttributeValidations;
            function checkAttributeValidations(attributes) {
                var valid = true;
                angular.forEach(attributes, function (attribute) {
                    attribute.attributeDef.newValidations = JSON.parse(attribute.attributeDef.validations);
                    if (valid) {
                        if (attribute.attributeDef.dataType == "TEXT" && attribute.attributeDef.newValidations != null && attribute.attributeDef.newValidations != "" && attribute.stringValue != null && attribute.stringValue != "") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (validation.key == "PATTERN" && validation.value != null) {
                                    validation.patternValue = new RegExp(validation.value);
                                }
                                if (valid) {
                                    if (validation.key == "MIN_LENGTH_OF_CHARACTERS" && validation.value != null && (attribute.stringValue == null || attribute.stringValue == "")) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Enter at least " + validation.value + " characters for " + attribute.attributeDef.name);
                                    } else if (validation.key == "MIN_LENGTH_OF_CHARACTERS" && validation.value != null && attribute.stringValue.length < validation.value) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Enter at least " + validation.value + " characters for " + attribute.attributeDef.name);
                                    } else if (validation.key == "MAX_LENGTH_OF_CHARACTERS" && validation.value != null && attribute.stringValue.length > validation.value) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Value should not exceed " + validation.value + " characters for " + attribute.attributeDef.name);
                                    } else if (validation.key == "PATTERN" && validation.value != null && !validation.patternValue.test(attribute.stringValue)) {
                                        valid = false;
                                        $rootScope.showWarningMessage(attribute.attributeDef.name + " : Value should match to given pattern");
                                    }
                                }
                            })
                        } else if (attribute.attributeDef.dataType == "INTEGER" && attribute.attributeDef.newValidations != null && attribute.attributeDef.newValidations != "" && attribute.integerValue != null && attribute.integerValue != "") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (valid) {
                                    if (validation.key == "ONLY_POSITIVE_VALUES" && validation.value && attribute.integerValue != null && attribute.integerValue < 0) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Please enter positive value");
                                    } else if (validation.key == "ONLY_NEGATIVE_VALUES" && validation.value && attribute.integerValue != null && attribute.integerValue >= 0) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Please enter negative value for " + attribute.attributeDef.name);
                                    } else if (validation.key == "MIN_VALUE" && validation.value != null && attribute.integerValue != null && attribute.integerValue != "" && parseInt(validation.value) > parseInt(attribute.integerValue)) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Value should be greater than " + validation.value + " for " + attribute.attributeDef.name);
                                    } else if (validation.key == "MAX_VALUE" && validation.value != null && attribute.integerValue != null && attribute.integerValue != "" && parseInt(validation.value) < parseInt(attribute.integerValue)) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Value should be less than " + validation.value + " for " + attribute.attributeDef.name);
                                    }
                                }
                            })
                        } else if (attribute.attributeDef.dataType == "DOUBLE" && attribute.attributeDef.newValidations != null && attribute.attributeDef.newValidations != "" && attribute.doubleValue != null && attribute.doubleValue != "") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (valid) {
                                    if (validation.key == "ONLY_POSITIVE_VALUES" && validation.value && attribute.doubleValue != null && attribute.doubleValue < 0) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Please enter positive value  for " + attribute.attributeDef.name);
                                    } else if (validation.key == "ONLY_NEGATIVE_VALUES" && validation.value && attribute.doubleValue != null && attribute.doubleValue >= 0) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Please enter negative value for " + attribute.attributeDef.name);
                                    } else if (validation.key == "MIN_VALUE" && validation.value != null && attribute.doubleValue != null && attribute.doubleValue != "" && parseInt(validation.value) > parseInt(attribute.doubleValue)) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Value should be greater than " + validation.value);
                                    } else if (validation.key == "MAX_VALUE" && validation.value != null && attribute.doubleValue != null && attribute.doubleValue != "" && parseInt(validation.value) < parseInt(attribute.doubleValue)) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Value should be less than " + validation.value + " for " + attribute.attributeDef.name);
                                    } else if (validation.key == "NO_OF_DECIMALS_TO_ENTER" && validation.value != null && validation.value != "") {
                                        var decimalCheck = attribute.doubleValue.toString().split('.');
                                        if (decimalCheck[1] != undefined && decimalCheck[1].length > validation.value) {
                                            valid = false;
                                            $rootScope.showWarningMessage("Decimal value should not exceed " + validation.value + " for " + attribute.attributeDef.name);
                                        }
                                    }
                                }
                            })
                        } else if (attribute.attributeDef.dataType == "CURRENCY" && attribute.attributeDef.newValidations != null && attribute.attributeDef.newValidations != "" && attribute.currencyValue != null && attribute.currencyValue != "") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (valid) {
                                    if (validation.key == "ONLY_POSITIVE_VALUES" && validation.value && attribute.currencyValue != null && attribute.currencyValue < 0) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Please enter positive value for " + attribute.attributeDef.name);
                                    } else if (validation.key == "ONLY_NEGATIVE_VALUES" && validation.value && attribute.currencyValue != null && attribute.currencyValue >= 0) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Please enter negative value for " + attribute.attributeDef.name);
                                    } else if (validation.key == "NO_OF_DECIMALS_TO_ENTER" && validation.value != null && validation.value != "") {
                                        var decimalCheck = attribute.currencyValue.toString().split('.');
                                        if (decimalCheck[1] != undefined && decimalCheck[1].length > validation.value) {
                                            valid = false;
                                            $rootScope.showWarningMessage("Decimal value should not exceed " + validation.value + " for " + attribute.attributeDef.name);
                                        }
                                    }
                                }
                            })
                        } else if (attribute.attributeDef.dataType == "LONGTEXT" && attribute.attributeDef.newValidations != null && attribute.attributeDef.newValidations != "") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (valid) {
                                    if (validation.key == "MAX_LENGTH_OF_CHARACTERS" && validation.value != null && attribute.longTextValue.length > validation.value) {
                                        valid = false;
                                        $rootScope.showWarningMessage("Value should not exceed " + validation.value + " characters for " + attribute.attributeDef.name);
                                    }
                                }
                            })
                        } else if (attribute.attributeDef.dataType == "DATE" && attribute.attributeDef.newValidations != null && attribute.attributeDef.newValidations != "" && attribute.dateValue != null && attribute.dateValue != "") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (valid) {
                                    if (validation.key == "FROM_DATE" && validation.value != null) {
                                        var fromDate = moment(attribute.dateValue, $rootScope.applicationDateSelectFormat);
                                        var validationDate = moment(validation.value, $rootScope.applicationDateSelectFormat);
                                        var val = fromDate.isSame(validationDate) || fromDate.isAfter(validationDate);
                                        if (!val) {
                                            valid = false;
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + " should be on (or) after " + validation.value);
                                        }
                                    } else if (validation.key == "TO_DATE" && validation.value != null) {
                                        var toDate = moment(attribute.dateValue, $rootScope.applicationDateSelectFormat);
                                        var validationDate = moment(validation.value, $rootScope.applicationDateSelectFormat);
                                        var val = toDate.isSame(validationDate) || toDate.isBefore(validationDate);
                                        if (!val) {
                                            valid = false;
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + " should be on (or) before " + validation.value);
                                        }
                                    }
                                }
                            })
                        } else if (attribute.attributeDef.dataType == "TIME" && attribute.attributeDef.newValidations != null && attribute.attributeDef.newValidations != "" &&
                            attribute.timeValue != null && attribute.timeValue != "") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (valid) {
                                    if (validation.key == "FROM_TIME" && validation.value != null) {
                                        var fromTime = moment(attribute.timeValue, 'hh:mm:ss');
                                        var validationTime = moment(validation.value, 'hh:mm:ss');
                                        var val = fromTime.isSame(validationTime) || fromTime.isAfter(validationTime);
                                        if (!val) {
                                            valid = false;
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + " should be on (or) after " + validation.value);
                                        }
                                    } else if (validation.key == "TO_TIME" && validation.value != null && attribute.timeValue != null && attribute.timeValue != "") {
                                        var toTime = moment(attribute.timeValue, 'hh:mm:ss');
                                        var validationTime = moment(validation.value, 'hh:mm:ss');
                                        var val = toTime.isSame(validationTime) || toTime.isBefore(validationTime);
                                        if (!val) {
                                            valid = false;
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + " should be on (or) before " + validation.value);
                                        }
                                    }
                                }
                            })
                        }
                    }
                })
                return valid;
            }


            /*
             * Arrange action columns after Freetext search
             * */

            $rootScope.arrangeFreeTextSearch = arrangeFreeTextSearch;
            function arrangeFreeTextSearch() {
                $timeout(function () {
                    var width = $("#action-buttons").outerWidth();
                    var freeTextSearch = document.getElementById("freeTextSearchDirective");
                    if (freeTextSearch != null) {
                        freeTextSearch.style.right = (width + 20) + "px";
                    }
                }, 200)
            }

            /*
             * IE Version
             * */
            function ieVersion() {
                var ua = window.navigator.userAgent;
                if (ua.indexOf("Trident/7.0") > -1) {
                    $rootScope.iebrowserCheck = true;
                    return 11;
                }

                else if (ua.indexOf("Trident/6.0") > -1) {
                    return 10;
                }

                else if (ua.indexOf("Trident/5.0") > -1) {
                    return 9;
                }

                else {
                    $rootScope.iebrowserCheck = false;
                    return 0;  // not IE9, 10 or 11
                }
            }


            $rootScope.getObjectAttributeValues = getObjectAttributeValues;
            function getObjectAttributeValues(objectIds, attributeIds, selectedAttributes, objects) {
                if (objectIds.length > 0 && attributeIds.length > 0 && selectedAttributes.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(objectIds, attributeIds).then(
                        function (data) {
                            if (data != null) {
                                var selectedObjectAttributes = data;

                                var map = new Hashtable();
                                angular.forEach(selectedAttributes, function (att) {
                                    if (att.id != null && att.id != "" && att.id != 0) {
                                        map.put(att.id, att);
                                    }
                                });

                                angular.forEach(objects, function (item) {
                                    var attributes = [];
                                    var itemAttributes = selectedObjectAttributes[item.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
                                    if (item.latestRevision != null) {
                                        var itemRevAttributes = selectedObjectAttributes[item.latestRevision];
                                        if (itemRevAttributes != null && itemRevAttributes != undefined) {
                                            attributes = attributes.concat(itemRevAttributes);
                                        }
                                    }
                                    if (attributes.length > 0) {
                                        angular.forEach(attributes, function (attribute) {
                                            var selectatt = map.get(attribute.id.attributeDef);
                                            if (selectatt != null) {
                                                var attributeName = selectatt.id;
                                                if (selectatt.dataType == 'TEXT') {
                                                    item[attributeName] = attribute.stringValue;
                                                } else if (selectatt.dataType == 'LONGTEXT') {
                                                    item[attributeName] = attribute.longTextValue;
                                                } else if (selectatt.dataType == 'RICHTEXT') {
                                                    item[attributeName] = attribute;
                                                } else if (selectatt.dataType == 'INTEGER') {
                                                    item[attributeName] = attribute.integerValue;
                                                } else if (selectatt.dataType == 'FORMULA') {
                                                    item[attributeName] = attribute.formulaValue;
                                                } else if (selectatt.dataType == 'BOOLEAN') {
                                                    item[attributeName] = attribute.booleanValue;
                                                } else if (selectatt.dataType == 'DOUBLE') {

                                                    if (attribute.doubleValue != null && attribute.measurementUnit != null) {

                                                        if (selectatt.measurement != null) {
                                                            var measurement = selectatt.measurement;
                                                            var measurementUnits = measurement.measurementUnits;
                                                            var baseUnit = null;
                                                            var attributeUnit = null;
                                                            angular.forEach(measurementUnits, function (unit) {
                                                                if (unit.baseUnit) {
                                                                    baseUnit = unit;
                                                                }
                                                                if (attribute.measurementUnit != null && attribute.measurementUnit.id == unit.id) {
                                                                    attributeUnit = unit;
                                                                }
                                                            })

                                                            var baseUnitIndex = measurementUnits.indexOf(baseUnit);
                                                            var attributeIndex = measurementUnits.indexOf(attributeUnit);

                                                            if (attributeIndex != baseUnitIndex) {
                                                                item[attributeName] = (attribute.doubleValue * attributeUnit.conversionFactor) + " " + attribute.measurementUnit.symbol;
                                                            } else {
                                                                item[attributeName] = attribute.doubleValue + " " + attribute.measurementUnit.symbol;
                                                            }
                                                        } else {
                                                            item[attributeName] = attribute.doubleValue;
                                                        }
                                                    } else {
                                                        item[attributeName] = attribute.doubleValue;
                                                    }
                                                } else if (selectatt.dataType == 'HYPERLINK') {
                                                    item[attributeName] = attribute.hyperLinkValue;
                                                } else if (selectatt.dataType == 'DATE') {
                                                    item[attributeName] = attribute.dateValue;
                                                } else if (selectatt.dataType == 'LIST' && !selectatt.listMultiple) {
                                                    item[attributeName] = attribute.listValue;
                                                } else if (selectatt.dataType == 'LIST' && selectatt.listMultiple) {
                                                    item[attributeName] = attribute.mlistValue;
                                                } else if (selectatt.dataType == 'TIME') {
                                                    item[attributeName] = attribute.timeValue;
                                                } else if (selectatt.dataType == 'TIMESTAMP') {
                                                    item[attributeName] = attribute.timestampValue;
                                                } else if (selectatt.dataType == 'CURRENCY') {
                                                    item[attributeName] = attribute.currencyValue;
                                                    if (attribute.currencyType != null) {
                                                        item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                                    }
                                                } else if (selectatt.dataType == 'ATTACHMENT') {
                                                    var revisionAttachmentIds = [];
                                                    if (attribute.attachmentValues.length > 0) {
                                                        angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                            revisionAttachmentIds.push(attachmentId);
                                                        });
                                                        AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                            function (data) {
                                                                $rootScope.revisionAttachments = data;
                                                                item[attributeName] = $rootScope.revisionAttachments;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    }
                                                } else if (selectatt.dataType == 'IMAGE') {
                                                    if (attribute.imageValue != null) {
                                                        item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                    }
                                                } else if (selectatt.dataType == 'OBJECT') {
                                                    if (selectatt.refType != null) {
                                                        if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                            ItemService.getItem(attribute.refValue).then(
                                                                function (itemValue) {
                                                                    item[attributeName] = itemValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                            ItemService.getRevisionId(attribute.refValue).then(
                                                                function (revisionValue) {
                                                                    item[attributeName] = revisionValue;
                                                                    ItemService.getItem(revisionValue.itemMaster).then(
                                                                        function (data) {
                                                                            item[attributeName].itemMaster = data.itemNumber;
                                                                        }, function (error) {
                                                                            $rootScope.showErrorMessage(error.message);
                                                                        }
                                                                    )
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                            ECOService.getChangeObject(attribute.refValue).then(
                                                                function (changeValue) {
                                                                    item[attributeName] = changeValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                            WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                                function (workflowValue) {
                                                                    item[attributeName] = workflowValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                            MfrService.getManufacturer(attribute.refValue).then(
                                                                function (mfrValue) {
                                                                    item[attributeName] = mfrValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                            MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                                function (mfrPartValue) {
                                                                    item[attributeName] = mfrPartValue;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                            CommonService.getPerson(attribute.refValue).then(
                                                                function (person) {
                                                                    item[attributeName] = person;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'PROJECT' && attribute.refValue != null) {
                                                            ProjectService.getProject(attribute.refValue).then(
                                                                function (project) {
                                                                    item[attributeName] = project;
                                                                }
                                                            )
                                                        }
                                                        else if (selectatt.refType == 'REQUIREMENT' && attribute.refValue != null) {
                                                            RequirementService.getRequirement(attribute.refValue).then(
                                                                function (data) {
                                                                    item[attributeName] = data;
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'REQUIREMENTDOCUMENT' && attribute.refValue != null) {
                                                            ReqDocumentService.getReqDocument(attribute.refValue).then(
                                                                function (data) {
                                                                    item[attributeName] = data;
                                                                }
                                                            )
                                                        }
                                                        else if (selectatt.refType == 'MESOBJECT' && attribute.refValue != null) {
                                                            MESObjectTypeService.getMESObject(attribute.refValue).then(
                                                                function (data) {
                                                                    item[attributeName] = data;
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'MROOBJECT' && attribute.refValue != null) {
                                                            MESObjectTypeService.getMROObject(attribute.refValue).then(
                                                                function (data) {
                                                                    item[attributeName] = data;
                                                                }
                                                            )
                                                        } else if (selectatt.refType == 'CUSTOMOBJECT' && attribute.refValue != null) {
                                                            CustomObjectService.getCustomObject(attribute.refValue).then(
                                                                function (data) {
                                                                    item[attributeName] = data;
                                                                }
                                                            )
                                                        }


                                                    }
                                                }
                                            }
                                        })
                                    }
                                    else {
                                        angular.forEach(selectedAttributes, function (selectedAttribute) {
                                            if (selectedAttribute.dataType == "TEXT" && selectedAttribute.defaultTextValue != null) {
                                                var attributeName = selectedAttribute.id;
                                                item[attributeName] = selectedAttribute.defaultTextValue;
                                            }
                                            if (selectedAttribute.dataType == "LIST" && selectedAttribute.defaultListValue != null) {
                                                var attributeName = selectedAttribute.id;
                                                item[attributeName] = selectedAttribute.defaultListValue;
                                            }
                                        });
                                    }
                                })
                            } else {
                                angular.forEach(selectedAttributes, function (selectedAttribute) {
                                    if (selectedAttribute.attributeDef.dataType == "TEXT" && selectedAttribute.attributeDef.defaultTextValue == null) {
                                        item[selectedAttribute] = selectedAttribute.attributeDef.defaultTextValue;
                                    }
                                });
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }


            (function () {
                ieVersion();
            })();
        }
    }
);