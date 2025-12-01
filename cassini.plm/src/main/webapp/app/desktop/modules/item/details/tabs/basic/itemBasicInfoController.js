define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/githubService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/pdmService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ItemBasicInfoController', ItemBasicInfoController);

        function ItemBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams,
                                         CommonService, ItemTypeService, ItemService, DialogService,
                                         PDMService, GitHubService, MfrPartsService) {
            var vm = this;

            $scope.$sce = $sce;
            vm.loading = true;
            vm.loadingAttributes = true;
            vm.itemId = $stateParams.itemId;
            vm.item = null;
            vm.itemRevision = null;
            vm.lifeCycles = null;
            vm.updateItem = updateItem;

            vm.addAttachment = addAttachment;
            vm.saveThumbnail = saveThumbnail;
            $scope.saveThumbnail = saveThumbnail;
            vm.thumbnail = null;
            vm.cancelThumbnail = cancelThumbnail;
            vm.addImage = false;
            vm.changeThumbnail = changeThumbnail;

            var currencyMap = new Hashtable();
            vm.currencies = $application.currencies;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;
            vm.itemMode = $rootScope.itemDetailsMode;
            vm.changeRequireMaintenance = changeRequireMaintenance;
            vm.editRequireCompliance = false;

            var parsed = angular.element("<div></div>");

            var itemUpdate = parsed.html($translate.instant("ITEM_UPDATE_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            vm.titleImage = parsed.html($translate.instant("CLICK_TO_SHOW_LARGE_IMAGE")).html();
            var effectiveFromValidation = parsed.html($translate.instant("EFFECTIVE_FROM_VALIDATION")).html();
            var effectiveFromAfterCreatedDate = parsed.html($translate.instant("EFFECTIVE_FROM_AFTER_CREATION")).html();
            var effectiveToAfterCreatedDate = parsed.html($translate.instant("EFFECTIVE_TO_AFTER_CREATION")).html();
            var effectiveToValidation = parsed.html($translate.instant("EFFECTIVE_TO_VALIDATION")).html();
            $scope.effectiveFromPlaceholder = parsed.html($translate.instant("EFFECTIVE_FROM_PLACEHOLDER")).html();
            $scope.effectiveToPlaceholder = parsed.html($translate.instant("EFFECTIVE_TO_PLACEHOLDER")).html();

            /*---------  To get Item Details  ---------*/

            $rootScope.loatItemBasicInfo = loatItemBasicInfo;
            $rootScope.getItemMfrParts = getItemMfrParts;
            $rootScope.itemMfrParts = [];
            function getItemMfrParts() {
                $rootScope.itemMfrParts = [];
                MfrPartsService.getAllItemMfrPart(vm.itemId).then(
                    function (data) {
                        $rootScope.itemMfrParts = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loatItemBasicInfo() {
                vm.loading = true;
                if (vm.itemId != null && vm.itemId != undefined) {
                    ItemService.getRevisionId(vm.itemId).then(
                        function (data) {
                            vm.itemRevision = data;
                            vm.lifeCycleStatus = data.lifeCyclePhase.phaseType;
                            $rootScope.lifeCyclePhaseStatus = vm.itemRevision.lifeCyclePhase.phase;
                            ItemService.getItem(vm.itemRevision.itemMaster).then(
                                function (data) {
                                    vm.item = data;
                                    loadDesignObject();
                                    getItemMfrParts();

                                    if (vm.item.description != null && vm.item.description != undefined) {
                                        vm.item.descriptionHtml = $sce.trustAsHtml(vm.item.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                    }
                                    if (vm.item.createdDate) {
                                        if ($rootScope.currentLang == 'de') {
                                            vm.item.createDateDe = moment(vm.item.createdDate, $rootScope.applicationFromDateFormat).format($rootScope.applicationDateFormat);

                                        } else {
                                            vm.item.createDateDe = vm.item.createdDate;
                                        }

                                    }
                                    if (vm.itemRevision.modifiedDate) {
                                        if ($rootScope.currentLang == 'de') {
                                            vm.itemRevision.modifiedDateDe = moment(vm.itemRevision.modifiedDate, $rootScope.applicationFromDateFormat).format($rootScope.applicationDateFormat);

                                        } else {
                                            vm.itemRevision.modifiedDateDe = vm.itemRevision.modifiedDate;
                                        }
                                    }
                                    $rootScope.item = vm.item;
                                    $scope.itemName = vm.item.itemName;
                                    vm.item.lifeCyclePhase = vm.itemRevision.lifeCyclePhase;
                                    vm.itemRevision.description = vm.item.description;
                                    vm.itemRevision.itemName = vm.item.itemName;
                                    if (vm.item.thumbnail != null) {
                                        vm.item.thumbnailImage = "api/plm/items/" + vm.item.id + "/itemImageAttribute/download?" + new Date().getTime();
                                    }
                                    else if (vm.itemRevision.designObject != null) {
                                        vm.item.thumbnail = '-';
                                        vm.item.thumbnailImage = "api/pdm/core/parts/{0}/thumbnail".format(vm.itemRevision.designObject);
                                    }
                                    $timeout(function () {
                                        $scope.$broadcast('app.attributes.tabActivated', {});
                                    }, 1000);
                                    loadPersons();
                                    loadGitHubRepository();
                                    $scope.$evalAsync();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadDesignObject() {
                if (vm.itemRevision.designObject != null) {
                    PDMService.getRevisionedObject(vm.itemRevision.designObject).then(
                        function (data) {
                            vm.itemRevision.designObjectRef = data;
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            /*------  To get createdBy and CreatedDate by Ids ---------*/

            function loadPersons() {
                var personIds = [vm.item.createdBy];

                if (vm.item.createdBy != vm.item.modifiedBy) {
                    personIds.push(vm.item.modifiedBy);
                }
                if (vm.itemRevision.modifiedBy != null) {
                    personIds.push(vm.itemRevision.modifiedBy);
                }

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.item.createdBy != null) {
                            var person = map.get(vm.item.createdBy);
                            if (person != null) {
                                vm.item.createdByPerson = person;
                            }
                            else {
                                vm.item.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.item.modifiedBy != null) {
                            person = map.get(vm.item.modifiedBy);
                            if (person != null) {
                                vm.item.modifiedByPerson = person;
                            }
                            else {
                                vm.item.modifiedByPerson = {firstName: ""};
                            }
                        }
                        if (vm.itemRevision.modifiedBy != null) {
                            person = map.get(vm.itemRevision.modifiedBy);
                            if (person != null) {
                                vm.itemRevision.modifiedByPerson = person;
                            }
                            else {
                                vm.itemRevision.modifiedByPerson = {firstName: ""};
                            }
                        }
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            /*----  To update changes Item Details  -----*/

            function updateItem() {
                if (validateItem()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    if (vm.item.makeOrBuy == "MAKE") {
                        vm.item.requireCompliance = false;
                    }
                    if (vm.Configuration == "NORMAL") {
                        vm.item.configurable = false;
                    }
                    if (vm.Configuration == "CONFIGURABLE") {
                        vm.item.configurable = true;
                    }
                    ItemService.updateItemMaster(vm.item).then(
                        function (data) {
                            vm.item.name = data.name;
                            vm.item.description = data.description;
                            if (vm.item.description != null && vm.item.description != undefined) {
                                vm.item.descriptionHtml = $sce.trustAsHtml(vm.item.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $rootScope.viewInfo.description = vm.item.itemName;
                            vm.editMakeOrBuy = false;
                            vm.editConfiguration = false;
                            vm.editRequireCompliance = false;
                            $rootScope.showSuccessMessage(itemUpdate);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.editEffectiveFrom = false;
            vm.editEffectiveTo = false;
            vm.effectiveFromDate = null;
            vm.effectiveToDate = null;
            vm.updateEffectiveFrom = updateEffectiveFrom;
            vm.updateEffectiveTo = updateEffectiveTo;
            vm.updateItemRevision = updateItemRevision;

            function updateEffectiveFrom() {
                if (validateEffectiveFrom()) {
                    updateItemRevision();
                }
            }

            function updateEffectiveTo() {
                if (validateEffectiveTo()) {
                    updateItemRevision();
                }
            }

            function updateItemRevision() {
                ItemService.updateItem(vm.itemRevision).then(
                    function (data) {
                        vm.itemRevision.effectiveFrom = data.effectiveFrom;
                        vm.itemRevision.effectiveTo = data.effectiveTo;
                        vm.editEffectiveFrom = false;
                        vm.editEffectiveTo = false;
                        $rootScope.showSuccessMessage(itemUpdate);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function validateEffectiveFrom() {
                var valid = true;
                if (vm.itemRevision.effectiveFrom != null && vm.itemRevision.effectiveFrom != "" && (vm.itemRevision.effectiveTo == null || vm.itemRevision.effectiveTo == "")) {
                    var createdDate = moment(vm.itemRevision.createdDate, $rootScope.applicationDateSelectFormat);
                    var effectiveFrom = moment(vm.itemRevision.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var value = effectiveFrom.isSame(createdDate) || effectiveFrom.isAfter(createdDate);
                    if (!value) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveFromAfterCreatedDate);
                    }
                } else if (vm.itemRevision.effectiveFrom != null && vm.itemRevision.effectiveFrom != null && vm.itemRevision.effectiveTo != "" && vm.itemRevision.effectiveTo != "") {
                    var effectiveTo = moment(vm.itemRevision.effectiveTo, $rootScope.applicationDateSelectFormat);
                    var effectiveFrom = moment(vm.itemRevision.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var val = effectiveFrom.isSame(effectiveTo) || effectiveFrom.isBefore(effectiveTo);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveFromValidation);
                    }
                }

                return valid;
            }

            function validateEffectiveTo() {
                var valid = true;
                if (vm.itemRevision.effectiveTo != null && vm.itemRevision.effectiveTo != "" && (vm.itemRevision.effectiveFrom == null || vm.itemRevision.effectiveFrom == "")) {
                    var createdDate = moment(vm.itemRevision.createdDate, $rootScope.applicationDateSelectFormat);
                    var effectiveTo = moment(vm.itemRevision.effectiveTo, $rootScope.applicationDateSelectFormat);
                    var value = effectiveTo.isSame(createdDate) || effectiveTo.isAfter(createdDate);
                    if (!value) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveToAfterCreatedDate);
                    }
                } else if (vm.itemRevision.effectiveFrom != null && vm.itemRevision.effectiveFrom != null && vm.itemRevision.effectiveTo != "" && vm.itemRevision.effectiveTo != null) {
                    var effectiveTo = moment(vm.itemRevision.effectiveTo, $rootScope.applicationDateSelectFormat);
                    var effectiveFrom = moment(vm.itemRevision.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var val = effectiveTo.isSame(effectiveFrom) || effectiveTo.isAfter(effectiveFrom);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveToValidation);
                    }
                }

                return valid;
            }

            /*--- To get Lifecycle Phases  ---*/

            function loadLifeCycles() {
                ItemTypeService.getLifeCyclesPhases().then(
                    function (data) {
                        vm.lifeCycles = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function addAttachment(attribute) {
                attribute.showAttachment = true;
                var itemAttachmentFile = document.getElementById("itemAttachmentFile");
                if (itemAttachmentFile != null && itemAttachmentFile != undefined) {
                    document.getElementById("itemAttachmentFile").value = "";
                }
                var itemRevisionAttachmentFile = document.getElementById("itemRevisionAttachmentFile");
                if (itemRevisionAttachmentFile != null && itemRevisionAttachmentFile != undefined) {
                    document.getElementById("itemRevisionAttachmentFile").value = "";
                }
            }

            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.itemId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.itemId);
                $("#thumbnail-image-basic" + vm.itemId).width($('#thumbnail-view-basic' + vm.itemId).outerWidth());
                $("#thumbnail-image-basic" + vm.itemId).height($('#thumbnail-view-basic' + vm.itemId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            function saveThumbnail(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.thumbnail = image;
                    if (validate()) {
                        ItemService.uploadImage(vm.item.id, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage(itemUpdate);
                                loatItemBasicInfo();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            });
                    }
                }
            }

            var imageValidation = parsed.html($translate.instant("IMAGE_VALIDATION")).html();
            var SelectThumbnail = parsed.html($translate.instant("SELECT_THUMBNAIL")).html();
            $scope.removeThumnailTitle = parsed.html($translate.instant("REMOVE_THUMBNAIL")).html();
            $scope.updateThumnailTitle = parsed.html($translate.instant("UPDATE_THUMBNAIL")).html();

            function validate() {
                var valid = true;
                if (vm.thumbnail != null) {
                    if (vm.item.thumbnail == null || vm.item.thumbnail == "") {
                        var fup = document.getElementById('thumbnailFile');
                        var fileName = fup.value;
                        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                        if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                            return true;
                        } else {
                            $rootScope.showWarningMessage(imageValidation);
                            fup.focus();
                            valid = false;
                        }
                    } else {
                        var fup = document.getElementById('thumbnailUpdateFile');
                        var fileName = fup.value;
                        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                        if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                            return true;
                        } else {
                            $rootScope.showWarningMessage(imageValidation);
                            fup.focus();
                            valid = false;
                        }
                    }
                }

                return valid;
            }

            vm.deleteItemThumbnail = deleteItemThumbnail;
            function deleteItemThumbnail() {
                var options = {
                    title: "Confirmation",
                    message: "Are you sure you want to remove Item Thumbnail?",
                    okButtonClass: 'btn-danger'
                }
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        vm.item.thumbnail = null;
                        ItemService.updateItemMaster(vm.item).then(
                            function (data) {
                                $rootScope.showSuccessMessage(itemUpdate);
                                loatItemBasicInfo();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            function validateItem() {
                var valid = true;
                if (vm.item.itemName == null || vm.item.itemName == ""
                    || vm.item.itemName == undefined) {
                    valid = false;
                    vm.item.itemName = $scope.itemName;
                    $rootScope.showWarningMessage(nameValidation);

                }
                return valid;
            }

            function cancelThumbnail() {
                vm.thumbnail = null;
                vm.addImage = false;
            }

            function changeThumbnail() {
                vm.addImage = true;
            }

            vm.makeOrBuyTypes = ["MAKE", "BUY"];
            vm.editMakeOrBuy = false;
            vm.changeMakeOrBuy = changeMakeOrBuy;
            function changeMakeOrBuy() {
                vm.editMakeOrBuy = true;
                vm.makeOrBuy = vm.item.makeOrBuy;
            }

            vm.cancelMakeOrBuy = cancelMakeOrBuy;
            function cancelMakeOrBuy() {
                vm.editMakeOrBuy = false;
                vm.item.makeOrBuy = vm.makeOrBuy;
            }

            /* vm.configurationTypes = ["NORMAL","CONFIGURABLE"];
             vm.editConfiguration = false;
             vm.changeConfiguration = changeConfiguration;
             function changeConfiguration() {
             vm.editConfiguration = true;
             vm.Configuration = vm.item.Configuration;
             }

             vm.cancelConfiguration = cancelConfiguration;
             function cancelConfiguration() {
             vm.editConfiguration = false;
             vm.item.Configuration = vm.Configuration;
             }
             */
            vm.configurationTypes = ["NORMAL", "CONFIGURABLE"];
            vm.editConfiguration = false;
            vm.changeConfiguration = changeConfiguration;
            function changeConfiguration() {
                vm.editConfiguration = true;
                if (vm.item.configurable == true)
                    vm.Configuration = "CONFIGURABLE";
                if (vm.item.configurable == false)
                    vm.Configuration = "NORMAL";
            }

            vm.cancelConfiguration = cancelConfiguration;
            function cancelConfiguration() {
                vm.editConfiguration = false;
                if (vm.Configuration = "CONFIGURABLE")
                    vm.item.configurable == true;
                if (vm.Configuration = "NORMAL") {
                    vm.item.configurable == false;
                }

            }


            vm.showInstances = showInstances;
            function showInstances() {

                var options = {
                    title: "Instances",
                    template: 'app/desktop/modules/item/details/tabs/basic/bomItemCompareView.jsp',
                    controller: 'BomItemCompareController as bomItemCompareVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/bomItemsCompareController',
                    data: {
                        item: vm.item
                    },
                    width: 500,
                    /*buttons: [
                     {text: bomCompareBtn, broadcast: 'app.item.bom.compare'}
                     ],*/
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.githubRepos = [];
            function loadGithubRepos() {
                GitHubService.getAllGitHubRepositories().then(
                    function (data) {
                        Array.prototype.push.apply(vm.githubRepos, data);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.githubItemRepo = null;
            function loadGitHubRepository() {
                if (vm.item.itemType.softwareType) {
                    GitHubService.getGitHubItemRepository(vm.item.id).then(
                        function (data) {
                            vm.githubItemRepo = data;
                            loadGitHubRepoReleases();
                            loadGitHubItemRevisionRelease();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.setGitHubItemRepo = setGitHubItemRepo;
            function setGitHubItemRepo(repo) {
                if (repo != null && repo != "" && repo != undefined) {
                    $rootScope.showBusyIndicator();
                    GitHubService.setGitHubItemRepository(repo.id, vm.item.id).then(
                        function (data) {
                            vm.githubItemRepo = data;
                            loadGitHubRepoReleases();
                            loadGitHubItemRevisionRelease();
                            if (vm.itemRevisionGitHubRelease != null && vm.itemRevisionGitHubRelease != "") {
                                GitHubService.deleteGitHubItemRevisionRelease(vm.itemRevisionGitHubRelease).then(
                                    function (data) {
                                        vm.itemRevisionGitHubRelease = null;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                );
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }

            }

            vm.githubRepoReleases = [];
            function loadGitHubRepoReleases() {
                vm.githubRepoReleases = [];
                if (vm.githubItemRepo != null && vm.githubItemRepo.repository != null) {
                    GitHubService.getGitHubRepositoryReleases(vm.githubItemRepo.repository.id).then(
                        function (data) {
                            vm.githubRepoReleases = data;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.itemRevisionGitHubRelease = null;
            function loadGitHubItemRevisionRelease() {
                GitHubService.getGitHubItemRevisionRelease(vm.itemRevision.id).then(
                    function (data) {
                        vm.itemRevisionGitHubRelease = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.setGitHubItemRelease = setGitHubItemRelease;
            function setGitHubItemRelease(release) {
                if (vm.itemRevisionGitHubRelease != null && vm.itemRevisionGitHubRelease != "") {
                    vm.itemRevisionGitHubRelease.itemRevision = vm.itemRevision.id;
                    GitHubService.setGitHubItemRevisionRelease(vm.itemRevisionGitHubRelease).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Saved GitHub release for item");
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

            }

            function changeRequireMaintenance() {
                vm.editRequireCompliance = true;
                vm.oldRequireCompliance = vm.item.requireCompliance;
            }

            vm.cancelRequireCompliance = cancelRequireCompliance;
            function cancelRequireCompliance() {
                vm.editRequireCompliance = false;
                vm.item.requireCompliance = vm.oldRequireCompliance;
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                });
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                        }
                        loatItemBasicInfo();
                        loadLifeCycles();
                    }
                });
                $timeout(function () {
                    loadGithubRepos();
                    //loatItemBasicInfo();
                    loadLifeCycles();
                }, 200)
            })();
        }
    }
)
;