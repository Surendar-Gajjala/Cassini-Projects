/**
 * Created by swapna on 10/08/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/store/loanService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'

    ],
    function (module) {
        module.controller('NewLoanDialogController', NewLoanDialogController);

        function NewLoanDialogController($scope, $rootScope, $timeout, $window, $state, $cookies, AutonumberService, LoanService,
                                         $stateParams, $application, ProjectService, TopStoreService, AttributeAttachmentService,
                                         ObjectAttributeService, $q, ObjectTypeAttributeService) {
            var vm = this;
            vm.autoNumber = autoNumber;
            vm.loanIssueItems = [];
            var loanIssueItemsMap = new Hashtable();
            vm.back = back;
            vm.remove = remove;
            vm.create = create;
            vm.addItems = addItems;
            vm.fromProjects = [];
            vm.toProjects = [];
            vm.selectProjects = selectProjects;
            vm.validate = validate;
            vm.stores = [];
            vm.newLoanAttributes = [];
            vm.attributes = [];
            vm.requiredAttributes = [];
            vm.attributesList = [];

            vm.newLoan = {
                loanNumber: null,
                fromProject: null,
                fromProjectObject: null,
                toProject: null,
                toProjectObject: null,
                fromStore: $rootScope.storeId,
                toStore: null,
                status: "PENDING",
                loanIssueItems: []
            };
            vm.store = null;

            vm.emptyLoanItem = {
                materialItem: null,
                notes: null,
                quantity: null
            };

            function addItems() {
                if (validate()) {
                    var options = {
                        title: 'Loan Items',
                        showMask: true,
                        template: 'app/desktop/modules/stores/details/tabs/loanIssued/new/newLoanItemsView.jsp',
                        controller: 'NewLoanItemsController as loanItemsVm',
                        resolve: 'app/desktop/modules/stores/details/tabs/loanIssued/new/newLoanItemsController',
                        width: 700,
                        data: {
                            newLoan: vm.newLoan,
                            addedItemsMap: loanIssueItemsMap
                        },
                        buttons: [],
                        callback: function (loanIssueItem) {
                            var item = loanIssueItemsMap.get(loanIssueItem.itemDTO.itemNumber);
                            if (item == null) {
                                loanIssueItemsMap.put(loanIssueItem.itemDTO.itemNumber, loanIssueItem);
                                vm.loanIssueItems.push(loanIssueItem);
                            }
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'LOAN'});
            }

            function autoNumber() {
                var prefix = $application.config.autoNumber.prefix;
                /*var year = $application.config.autoNumber.year;*/
                var year = new Date().getFullYear();
                AutonumberService.getAutonumberByName('Default Loan Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "") {
                                    vm.newLoan.loanNumber = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newLoan.loanNumber = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            };

            function validateLoan() {
                var valid = false;
                var counter = 0;
                var loanItems = [];
                angular.forEach(vm.loanIssueItems, function (loanIssueItem) {
                    if (loanIssueItem.quantity > 0) {
                        var copiedloanIssueItem = angular.copy(vm.emptyLoanItem);
                        copiedloanIssueItem.item = loanIssueItem.item;
                        copiedloanIssueItem.notes = loanIssueItem.notes;
                        copiedloanIssueItem.quantity = loanIssueItem.quantity;
                        copiedloanIssueItem.recordedBy = window.$application.login.person.id;
                        copiedloanIssueItem.movementType = "LOANISSUED";

                        loanItems.push(copiedloanIssueItem);
                        counter++;
                        if (loanIssueItem.storeOnHand < loanIssueItem.quantity) {
                            valid = false;
                            $rootScope.showErrorMessage("Loan qty cannot be greater than Inventory qty");
                        }
                        else {
                            if (counter == vm.loanIssueItems.length) {
                                valid = true;
                                vm.newLoan.loanIssueItems = loanItems;
                            }
                            else {
                                valid = false;
                                $rootScope.showErrorMessage("Please enter qty for item(s)");
                            }
                        }
                    }
                });

                return valid;
            }

            function remove(item) {
                var index = vm.loanIssueItems.indexOf(item);
                vm.loanIssueItems.splice(index, 1);
                loanIssueItemsMap.remove(item.itemDTO.itemNumber);
            }

            function create() {
                if (validateLoan()) {
                    vm.newLoan.fromProject = vm.newLoan.fromProjectObject.id;
                    vm.newLoan.toProject = vm.newLoan.toProjectObject.id;
                    vm.newLoan.toStore = vm.newLoan.toStore.id;
                    attributesValidate().then(
                        function (success) {
                            LoanService.createLoan($rootScope.storeId, vm.newLoan).then(
                                function (data) {
                                    vm.newLoan = data;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.loanId = vm.newLoan.id;
                                            $state.go('app.store.stock.loanIssuedDetails', {loanId: vm.newLoan.id});
                                            $rootScope.showSuccessMessage("Loan (" + vm.newLoan.loanNumber + ") issued successfully");
                                        }, function (error) {

                                        }
                                    )
                                }, function (error) {

                                }
                            )
                        }, function (error) {

                        });
                }
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newLoanIssueAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newLoanIssueAttributes, function (attribute) {
                        attribute.id.objectId = vm.newLoan.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = vm.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.newLoanIssueAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newLoanIssueAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newLoanIssueAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newLoanIssueAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve();
                                    }, function (error) {
                                        defered.reject();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'LOAN', attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newLoanIssueAttributes != null && vm.newLoanIssueAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newLoanIssueAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newLoanIssueAttributes.length > 0) {
                    angular.forEach(vm.newLoanIssueAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }

                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newLoan.id, vm.newLoanIssueAttributes).then(
                        function (data) {
                            var secCount = 0;
                            if (vm.propertyImageAttributes.length > 0) {
                                angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                    ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                        function (data) {
                                        });
                                    secCount++;
                                    if (secCount == vm.propertyImageAttributes.length) {
                                        defered.resolve();
                                    }
                                });
                            } else {
                                defered.resolve();
                            }
                        },
                        function (error) {
                            defered.reject();
                        }
                    )

                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function validate() {
                var valid = true;
                if (vm.newLoan.loanNumber == null) {
                    valid = false;
                    $rootScope.showErrorMessage("Auto Number cannot be empty");
                }

                if (vm.newLoan.fromProjectObject == null || vm.newLoan.fromProjectObject == "" || vm.newLoan.fromProjectObject == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage("Please select 'From Project'");
                }

                else if (vm.newLoan.toProjectObject == null || vm.newLoan.toProjectObject == "" || vm.newLoan.toProjectObject == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage("Please select 'To Project'");
                }

                else if (vm.newLoan.toStore == null || vm.newLoan.toStore == "" || vm.newLoan.toStore == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage("Please select 'To Store'");
                }
                return valid;
            }

            function back() {
                $window.history.back();
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.fromProjects = data;
                    }
                );
            }

            function selectProjects(project) {
                vm.toProjects = [];
                vm.toProjects = angular.copy(vm.fromProjects);
                var index = vm.fromProjects.indexOf(project);
                vm.toProjects.splice(index, 1);
            }

            function loadStores() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        vm.store = data;
                    }
                ).then(
                    TopStoreService.getTopStores().then(
                        function (stores) {
                            vm.stores = stores;
                        }
                    )
                )
            }

            function resize() {
                var height = $(window).height();
                var projectHeaderHeight = $("#project-headerbar").outerHeight();
                if (projectHeaderHeight != null) {
                    if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
                        $('#contentpanel').height(height - 297);
                    } else {
                        $('#contentpanel').height(height - 267);
                    }
                } else if (projectHeaderHeight == null) {
                    $('#contentpanel').height(height - 217);
                }
            }

            function loadObjectAttributeDefs() {
                vm.newLoanIssueAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("LOAN").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newLoan.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.newLoanIssueAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            $rootScope.$on('app.storeItems.loan', function () {
                create();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjects();
                    loadObjectAttributeDefs();
                    resize();
                    loadStores();
                }
            })();
        }
    }
)
;