/**
 * Created by Nageshreddy on 02-01-2019.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/failureListService',
        'app/shared/services/core/failureValueListService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('CreateFailureListController', CreateFailureListController);

        function CreateFailureListController($scope, $rootScope, CommonService, AttachmentService, FailureListService,
                                             FailureValueListService, BomService, ItemService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;
            vm.failureLists = null;
            vm.failureValueList2 = [];
            vm.item = $scope.data.selectedItem;
            vm.missileObject = $scope.data.missileObject;
            vm.systemObject = $scope.data.systemObject;
            var instance = $scope.data.instance;
            vm.attachments = [];

            vm.bugs = [
                'Major',
                'Minor'
            ];

            vm.activities = [
                'Phase 2',
                'Vibration',
                'Phase 3',
                'Phase 4'
            ];

            vm.check = check;
            var emptyFailureValue = {
                item: null,
                failureStep: null,
                status: null,
                sno: null,
                dataType: null,
                value: null,
                upn: null
            };

            function loadLists() {
                vm.failureValueList = [];
                vm.failureValueList1 = [];
                var idPath = vm.item.idPath;
                var idPathArray = idPath.split("/");
                FailureListService.getLists().then(
                    function (data) {
                        vm.failureLists = data;
                        BomService.getByInstanceById(idPathArray[1]).then(
                            function (secBom) {
                                BomService.getByInstanceById(idPathArray[2]).then(
                                    function (subSystem) {
                                        angular.forEach(vm.failureLists[0].failureSteps, function (step, index) {
                                            var failureValue = angular.copy(emptyFailureValue);
                                            if (!vm.item.item.itemMaster.itemType.hasLots) {
                                                failureValue.instance = instance.id;
                                            } else if (vm.item.item.itemMaster.itemType.hasLots) {
                                                failureValue.lotInstance = instance.id;
                                                failureValue.instance = instance.itemInstance.id;
                                            }
                                            failureValue.item = vm.item.id;
                                            failureValue.failureStep = step.id;
                                            failureValue.status = step.status;
                                            failureValue.sno = step.serialNo;
                                            failureValue.dataType = step.dataType;
                                            switch (index) {
                                                case 0:
                                                    failureValue.value = vm.systemObject.itemMaster.itemCode + "-" + vm.missileObject.instanceName + "-" + secBom.typeRef.name;
                                                    break;
                                                case 1:
                                                    failureValue.value = secBom.status;
                                                    break;
                                                case 2:
                                                    var today = moment(new Date());
                                                    var todayStr = today.format('DD/MM/YYYY HH:mm');
                                                    failureValue.value = todayStr;
                                                    break;
                                                case 3:
                                                    failureValue.value = subSystem.typeRef.name + "-" + vm.item.item.itemMaster.itemName;
                                                    break;
                                                case 5:
                                                    failureValue.value = vm.item.item.itemMaster.drawingNumber;
                                                    break;
                                                case 6:
                                                    failureValue.value = secBom.status;
                                                    break;
                                                default :
                                                    failureValue.value = "";
                                                    break;
                                            }
                                            vm.failureValueList.push(failureValue);
                                        });
                                        angular.forEach(vm.failureValueList, function (value) {
                                            if (value.dataType == 'BOOLEAN') {
                                                vm.failureValueList1.push(value);
                                            } else {
                                                vm.failureValueList2.push(value);
                                            }
                                        });
                                        CommonService.getPersonReferences(vm.failureValueList1, 'checkedBy');
                                    })
                            })
                    }
                )
            }

            function check(step) {
                FailureValueListService.createFailListValue(instance.id, step).then
                (function (data) {
                    step.id = data.id;
                    step.checkedBy = data.checkedBy;
                    step.checkedDate = data.checkedDate;
                    CommonService.getPersonReferences([step], 'checkedBy');
                }), function (error) {
                    $rootScope.showErrorMessage(error.message);
                }
            }

            function validate() {
                var valid = true;
                if (vm.failureValueList[1].sno == 2 && (vm.failureValueList[1].value == "" || vm.failureValueList[1].value == null || vm.failureValueList[1].value == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please Select Activity");
                } else if (vm.failureValueList[4].sno == 2 && (vm.failureValueList[4].value == "" || vm.failureValueList[4].value == null || vm.failureValueList[4].value == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please Select Observation Type");
                }
                return valid;
            }

            function createFailureValueList() {
                if (validate()) {
                    FailureValueListService.createFailListValues(vm.failureValueList).then
                    (function (data) {
                        if (vm.attachments.length > 0) {
                            var attachmentStep = data[9];
                            if (attachmentStep.sno == 10) {
                                var ids = [];
                                angular.forEach(vm.attachments, function (at) {
                                    ids.push(attachmentStep.id);
                                });
                                AttachmentService.saveMultipleAttachments('FAILUREVALUELIST', ids, vm.attachments).then(
                                    function (data) {
                                        angular.forEach(data, function (att, $index) {
                                            if ($index == 0) {
                                                attachmentStep.value = att.id;
                                            } else {
                                                attachmentStep.value = attachmentStep.value + ", " + att.id;
                                            }
                                        });
                                        FailureValueListService.createFailListValue(attachmentStep.id, attachmentStep).then(
                                            function (data) {

                                            }
                                        )
                                    });
                            }
                        }

                        $rootScope.showSuccessMessage("Failure value list created successfully");
                        $scope.callback();
                        $rootScope.hideSidePanel('left');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    })
                }
            }

            function loadFailureActivities() {
                CommonService.getLovByName("Failure Activities").then(
                    function (data) {
                        if (data.values != null || data.values != undefined)
                            vm.activities = data.values;
                    });
            }

            (function () {
                loadLists();
                loadFailureActivities();
                $rootScope.$on('create.failureList', createFailureValueList);
            })();
        }
    }
)
;
