define(
    [
        'app/desktop/desktop.app',
        'jquery-ui'
    ],

    function (module) {
        module.directive('itemStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': item.lifeCyclePhase.phaseType == 'PRELIMINARY' && !item.rejected, " +
                "'label-info label-outline bg-light-success': item.lifeCyclePhase.phaseType == 'REVIEW' && !item.rejected," +
                "'label-success label-outline bg-light-success': item.lifeCyclePhase.phaseType == 'APPROVED' && !item.rejected," +
                "'label-lightblue label-outline bg-light-primary': item.lifeCyclePhase.phaseType == 'RELEASED' && !item.rejected," +
                "'label-danger label-outline bg-light-danger': item.lifeCyclePhase.phaseType == 'OBSOLETE' || item.lifeCyclePhase.phaseType == 'CANCELLED' || item.rejected}\">" +
                "{{item.lifeCyclePhase.phase}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'item': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);
        module.directive('ppapStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': object.status.phaseType == 'PRELIMINARY', " +
                "'label-info label-outline bg-light-success': object.status.phaseType == 'REVIEW'," +
                "'label-success label-outline bg-light-success': object.status.phaseType == 'APPROVED'," +
                "'label-lightblue label-outline bg-light-primary': object.status.phaseType == 'RELEASED'," +
                "'label-danger label-outline bg-light-danger': object.status.phaseType == 'OBSOLETE' || object.status.phaseType == 'CANCELLED'}\">" +
                "{{object.status.phase}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('projectType', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-primary label-outline bg-light-primary': object.typeName == 'Project', " +
                "'label-info label-outline bg-light-success': object.typeName == 'Group'," +
                "'label-success label-outline bg-light-success': object.typeName == 'Task'," +
                "'label-lightblue label-outline bg-light-danger': object.typeName == 'Activity'," +
                "'label-danger label-outline bg-light-success': object.typeName == 'Milestone'," +
                "'label-warning label-outline bg-light-warning': object.typeName == 'Phase'}\">" +
                "{{object.typeName}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('itemClass', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': item == 'ASSEMBLY', " +
                "'label-info label-outline bg-light-primary': item == 'PART'," +
                "'label-lightblue label-outline bg-light-success': item == 'DOCUMENT'," +
                "'label-success label-outline bg-light-danger': item == 'PRODUCT'," +
                "'label-flat-khaki label-outline bg-light-dark': item == 'OTHER'}\">" +
                "{{item}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'item': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);
        module.directive('affectedStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': item.phaseType == 'PRELIMINARY', " +
                "'label-info label-outline bg-light-success': item.phaseType == 'REVIEW'," +
                "'label-lightblue label-outline bg-light-primary': item.phaseType == 'RELEASED'," +
                "'label-success label-outline bg-light-success': item.phaseType == 'APPROVED'," +
                "'label-danger label-outline bg-light-danger': item.phaseType == 'OBSOLETE' || item.phaseType == 'CANCELLED'}\">" +
                "{{item.phase}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'item': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('ecoStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-dark': eco.statusType == 'PENDING', " +
                "'label-info label-outline bg-light-warning': eco.statusType == 'REVIEW'," +
                "'label-lightblue label-outline bg-light-primary': eco.statusType == 'RELEASED'," +
                "'label-success label-outline bg-light-success': eco.statusType == 'COMPLETE'}\">" +
                "{{eco.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'eco': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('deliveryStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-primary': delivery.deliverableStatus == 'PENDING', " +
                "'label-success label-outline bg-light-success': delivery.deliverableStatus == 'FINISHED'}\">" +
                "{{delivery.deliverableStatus}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'delivery': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('workflowStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-lightblue label-outline bg-light-warning': workflow.statusType == 'NORMAL' || workflow.statusType == 'NONE' || workflow.statusType == 'UNDEFINED' || workflow.workflowStatusType == 'NORMAL' || workflow.workflowStatusType == 'NONE' || workflow.workflowStatusType == 'UNDEFINED'," +
                "'label-success label-outline bg-light-success': workflow.statusType == 'RELEASED' || workflow.workflowStatusType == 'RELEASED'," +
                "'label-info label-outline bg-light-primary': workflow.statusType == 'START'|| workflow.workflowStatusType == 'START'," +
                "'label-danger label-outline bg-light-danger': workflow.statusType == 'REJECTED' || workflow.statusType == 'TERMINATE' || workflow.workflowStatusType == 'REJECTED' || workflow.workflowStatusType == 'TERMINATE'}\">" +
                "{{workflow.status || workflow.currentStatus || workflow.workflowStatus}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'workflow': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('workflowStatusSettings', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-lightblue label-outline bg-light-warning':  workflow.workflowSettingStatusType == 'NORMAL' || workflow.workflowSettingStatusType == 'NONE' || workflow.workflowSettingStatusType == 'UNDEFINED'," +
                "'label-success label-outline bg-light-success':workflow.workflowSettingStatusType == 'RELEASED'," +
                "'label-info label-outline bg-light-primary': workflow.workflowSettingStatusType == 'START'," +
                "'label-danger label-outline bg-light-danger': workflow.workflowSettingStatusType == 'REJECTED' || workflow.workflowSettingStatusType == 'TERMINATE'}\">" +
                "{{workflow.workflowSettingStatus}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'workflow': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('wfStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-lightblue label-outline bg-light-warning': workflow.statusType == 'NORMAL' || workflow.statusType == 'NONE'" +
                "|| workflow.statusType == 'UNDEFINED'," +
                "'label-success label-outline bg-light-success': workflow.statusType == 'RELEASED'," +
                "'label-info label-outline bg-light-primary': workflow.statusType == 'START'," +
                "'label-danger label-outline bg-light-danger': workflow.statusType == 'REJECTED' || workflow.statusType == 'TERMINATE'}\">" +
                "{{workflow.workflowStatus || workflow.currentStatus}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'workflow': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('declarationStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-primary label-outline bg-light-primary': object.status == 'OPEN'," +
                "'label-danger label-outline bg-light-danger': object.status == 'SUBMITTED'," +
                "'label-success label-outline bg-light-success': object.status == 'ACCEPTED'," +
                "'label-warning label-outline bg-light-warning': object.status == 'RECEIVED'}\">" +
                "{{object.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('taskStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': task.status == 'PENDING', " +
                "'label-lightblue label-outline bg-light-primary': task.status == 'INPROGRESS' || task.status == 'NONE'," +
                "'label-success label-outline bg-light-success': task.status == 'FINISHED'}\">" +
                "{{task.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'task': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('varianceFor', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': variance.varianceFor == 'ITEMS', " +
                "'label-success label-outline bg-light-success': variance.varianceFor == 'MATERIALS'}\">" +
                "{{variance.varianceFor}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'variance': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('checklistStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-primary label-outline bg-light-primary': status == 'PENDING' || status == 'NONE', " +
                "'label-success label-outline bg-light-success': status == 'FINISHED' || status == 'PASS'," +
                "'label-warning label-outline bg-light-warning': status == 'HOLD'," +
                "'label-danger label-outline bg-light-danger': status == 'CANCEL' || status == 'FAIL'}\">" +
                "{{status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'status': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('dcrStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': dcr.status == 'HOLD', " +
                "'label-lightblue label-outline bg-light-primary': dcr.status == 'NONE'," +
                "'label-danger label-outline bg-light-danger': dcr.status == 'REJECT' || dcr.status == 'CANCEL'," +
                "'label-success label-outline bg-light-success': dcr.status == 'APPROVE'}\">" +
                "{{dcr.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'dcr': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('mcoStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': mco.statusType == 'HOLD', " +
                "'label-lightblue label-outline bg-light-dark': mco.statusType == 'NONE' || mco.statusType == 'UNDEFINED'," +
                "'label-primary label-outline bg-light-primary': mco.statusType == 'PENDING'," +
                "'label-info label-outline bg-light-dark': mco.statusType == 'REVIEW'," +
                "'label-danger label-outline bg-light-danger': mco.statusType == 'REJECT' || mco.statusType == 'CANCEL'," +
                "'label-success label-outline bg-light-success': mco.statusType == 'APPROVE' || mco.statusType == 'RELEASED' || mco.statusType == 'COMPLETE'}\">" +
                "{{mco.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'mco': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('dcrUrgency', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-success label-outline bg-light-success': dcr.urgency == 'LOW', " +
                "'label-primary label-outline bg-light-primary': dcr.urgency == 'MEDIUM'," +
                "'label-danger label-outline bg-light-danger': dcr.urgency == 'CRITICAL'," +
                "'label-warning label-outline bg-light-warning': dcr.urgency == 'HIGH'}\">" +
                "{{dcr.urgency}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'dcr': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('reqstatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label label-outline bg-light-warning': object.lifeCyclePhase.phaseType == 'PRELIMINARY', " +
                "'label label-outline bg-light-primary': object.lifeCyclePhase.phaseType == 'REVIEW'," +
                "'label label-outline bg-light-success': object.lifeCyclePhase.phaseType == 'RELEASED'}\">" +
                "{{object.lifeCyclePhase.phase}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('priority', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-info label-outline bg-light-success': object.priority == 'LOW', " +
                "'label-lightblue label-outline bg-light-primary': object.priority == 'MEDIUM'," +
                "'label-danger label-outline bg-light-danger': object.priority == 'CRITICAL'," +
                "'label-warning label-outline bg-light-warning': object.priority == 'HIGH'}\">" +
                "{{object.priority}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('readingType', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-primary label-outline bg-light-primary': object.meterReadingType == 'ABSOLUTE', " +
                "'label-warning label-outline bg-light-warning': object.meterReadingType == 'CHANGE'}\">" +
                "{{object.meterReadingType}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);
        module.directive('meterType', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-success label-outline bg-light-success': object.meterType == 'CONTINUOUS', " +
                "'label-info label-outline bg-light-primary': object.meterType == 'GUAGE'}\">" +
                "{{object.meterType}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('woStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-info label-outline bg-light-primary': object.status == 'OPEN' ||  object.status == 'PENDING'," +
                "'label-success label-outline bg-light-success': object.status == 'FINISH'," +
                "'label-danger label-outline bg-light-danger': object.status == 'ONHOLD'," +
                "'label-warning label-outline bg-light-warning': object.status == 'INPROGRESS'}\">" +
                "{{object.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);


        module.directive('objectStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-info label-outline bg-light-primary': object.status == 'OPEN' || object.status == 'NONE'," +
                "'label-success label-outline bg-light-success': object.status == 'APPROVED'," +
                "'label-danger label-outline bg-light-danger': object.status == 'REJECTED'," +
                "'label-warning label-outline bg-light-warning': object.status == 'PENDING' || object.status == 'REVIEWED' || object.status == 'HOLD'}\">" +
                "{{object.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('mcoChangeType', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-success label-outline bg-light-success': type == 'REPLACED', " +
                "'label-danger label-outline bg-light-danger': type == 'REMOVED'}\">" +
                "{{type}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'type': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('objectTypeStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': object.type == 'ITEM' || object.type == 'PLANT' || object.type == 'OPERATION' || object.type == 'MFRSUPPLIER' || object.objectType == 'ITEM' || object.objectType == 'PLANT' || object.objectType == 'OPERATION' || object.objectType == 'MFRSUPPLIER', " +
                "'label-primary': object.type == 'TEMPLATE' || object.type == 'MANUFACTURERPART' || object.type == 'PROJECT' || object.type == 'WORKCENTER' || object.objectType == 'TEMPLATE' || object.objectType == 'MANUFACTURERPART' || object.objectType == 'PROJECT' || object.objectType == 'WORKCENTER'," +
                "'label-info': object.type == 'MANUFACTURER' || object.type == 'SPECIFICATION' || object.type == 'SHIFT' || object.type == 'ASSEMBLYLINE' || object.objectType == 'MANUFACTURER' || object.objectType == 'SPECIFICATION' || object.objectType == 'SHIFT' || object.objectType == 'ASSEMBLYLINE'," +
                "'label-lightblue': object.type == 'ECO' || object.type == 'CHANGE' || object.type == 'MANPOWER' || object.type == 'PGCSUBSTANCE' || object.objectType == 'ECO' || object.objectType == 'CHANGE' || object.objectType == 'MANPOWER' || object.objectType == 'PGCSUBSTANCE'," +
                "'label-success': object.type == 'WORKFLOW'|| object.type == 'PROGRAM' || object.type == 'REQUIREMENT' || object.type == 'REQUIREMENTDOCUMENT' || object.type == 'MATERIAL' || object.objectType == 'WORKFLOW' || object.objectType == 'REQUIREMENT' || object.objectType == 'REQUIREMENTDOCUMENT' || object.objectType == 'MATERIAL'," +
                "'label-flat-orange': object.type == 'INSPECTIONPLAN' || object.type == 'PRODUCTINSPECTIONPLAN' || object.type == 'MATERIALINSPECTIONPLAN' || object.objectType == 'INSPECTIONPLAN' || object.objectType == 'PRODUCTINSPECTIONPLAN' || object.objectType == 'MATERIALINSPECTIONPLAN'," +
                "'label-flat-darkblue': object.type == 'INSPECTION' || object.type == 'ITEMINSPECTION' || object.type == 'MATERIALINSPECTION' || object.objectType == 'INSPECTION' || object.objectType == 'ITEMINSPECTION' || object.objectType == 'MATERIALINSPECTION'," +
                "'label-flat-khaki': object.type == 'PROBLEMREPORT' || object.type == 'EQUIPMENT' || object.type == 'MROASSET' || object.type == 'PLMNPR' || object.objectType == 'PROBLEMREPORT' || object.objectType == 'EQUIPMENT' || object.objectType == 'MROASSET' || object.objectType == 'PLMNPR'," +
                "'label-flat-slategray': object.type == 'NCR' || object.type == 'INSTRUMENT' || object.type == 'MROMETER' || object.type == 'PPAP' || object.objectType == 'NCR' || object.objectType == 'INSTRUMENT' || object.objectType == 'MROMETER' || object.objectType == 'PPAP'," +
                "'label-flat-dimgray': object.type == 'QCR' || object.type == 'PROJECTACTIVITY' || object.type == 'MROSPAREPART' || object.type == 'CUSTOMER' || object.objectType == 'QCR' || object.objectType == 'PROJECTACTIVITY' || object.objectType == 'MROSPAREPART' || object.objectType == 'CUSTOMER'," +
                "'label-flat-olive': object.type == 'ECR' || object.type == 'DCR' || object.type == 'MROMAINTENANCEPLAN' || object.type == 'SUPPLIERAUDIT' || object.objectType == 'ECR' || object.objectType == 'DCR' || object.objectType == 'MROMAINTENANCEPLAN' || object.objectType == 'SUPPLIERAUDIT'," +
                "'label-flat-purple': object.type == 'DCO' || object.type == 'PROJECTTASK' || object.type == 'MROWORKREQUEST' || object.objectType == 'DCO' || object.objectType == 'PROJECTTASK' || object.objectType == 'MROWORKREQUEST'," +
                "'label-flat-indigo': object.type == 'MCO' || object.type == 'TOOL' || object.type == 'MACHINE' || object.type == 'PGCDECLARATION' || object.objectType == 'MCO' || object.objectType == 'TOOL' || object.objectType == 'MACHINE' || object.objectType == 'PGCDECLARATION'," +
                "'label-flat-brown': object.type == 'DEVIATION' || object.type == 'WAIVER' || object.type == 'MROWORKORDER' || object.type == 'PGCSPECIFICATION' || object.objectType == 'DEVIATION' || object.objectType == 'WAIVER' || object.objectType == 'MROWORKORDER' || object.objectType == 'PGCSPECIFICATION'," +
                "'label-danger': object.type == 'CUSTOMOBJECT' || object.objectType == 'CUSTOMOBJECT' || object.type == 'TERMINOLOGY' || object.type == 'JIGFIXTURE' || object.objectType == 'TERMINOLOGY' || object.objectType == 'JIGFIXTURE'}\">" +
                "{{object.type || object.objectType}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('specStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': spec.lifecyclePhase.phaseType == 'PRELIMINARY', " +
                "'label-info': spec.lifecyclePhase.phaseType == 'REVIEW'," +
                "'label-lightblue': spec.lifecyclePhase.phaseType == 'RELEASED'," +
                "'label-danger': spec.lifecyclePhase.phaseType == 'OBSOLETE'}\">" +
                "{{spec.lifecyclePhase.phase}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'spec': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('reporterType', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': reporterType == 'CUSTOMER', " +
                "'label-primary label-outline bg-light-primary': reporterType == 'INTERNAL'," +
                "'label-success label-outline bg-light-success': reporterType == 'SUPPLIER'}\">" +
                "{{reporterType}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'reporterType': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('mfrStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': mfrpart.status == 'ALTERNATE', " +
                "'label-info label-outline bg-light-primary': mfrpart.status == 'PREFERRED'}\">" +
                "{{mfrpart.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'mfrpart': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        var lastDragOverId = "";
        var lastDragOverElem = null;
        var draggedId = null;
        var draggedName = null;
        var targetObject = null;
        var actualRow = null;
        var actualRows = [];
        var actualRowParentId = null;
        var draggingElement = null;
        module.directive('dragfile', function () {
            return {
                restrict: 'A',
                scope: {
                    files: '=',
                    updateFile: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        if (scope.files.length > 0) {
                            var actualRow = scope.files[draggedId];
                            if (actualRow.fileType == 'FOLDER') {
                                element.prop('draggable', false);
                                event.preventDefault();
                            }
                            else {
                                element.prop('draggable', true);
                                /** @namespace event.dataTransfer */
                                event.dataTransfer.setData('text', event.target.id)
                            }
                        }
                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();

                        var dropPlaceId = element[0].id;

                        if (dropPlaceId == "parentFile" || dropPlaceId == "parentFile1") {
                            if (scope.files.length > 0) {
                                var actualRow = scope.files[draggedId];
                                if (actualRow != undefined && actualRow != null && actualRow.parentFile != null) {
                                    actualRowParentId = actualRow.parentFile;
                                    actualRow.parentFile = null;
                                } else {
                                    actualRowParentId = null;
                                    actualRow.parentFile = null;
                                }
                            }
                            scope.updateFile(actualRow, null, actualRowParentId);
                            draggingElement = null;
                        } else {
                            if (draggingElement != null) {
                                //console.log(files.length);
                                if (scope.files.length > 0) {
                                    actualRow = scope.files[draggedId];
                                    targetObject = scope.files[dropPlaceId];
                                    if (actualRow != undefined && actualRow != null && actualRow.parentFile != null) {
                                        actualRowParentId = actualRow.parentFolder;
                                    } else {
                                        actualRowParentId = null;
                                    }
                                }

                                lastDragOverElem.removeClass('hover');
                                if (targetObject != null && targetObject != undefined) {
                                    if (targetObject.fileType == 'FOLDER') {
                                        actualRow.parentFile = targetObject.id;
                                        draggingElement = null;
                                        scope.updateFile(actualRow, targetObject, actualRowParentId);
                                    }
                                }

                            }
                        }
                    });
                }
            }
        });

        module.directive('dragattribute', function () {
            return {
                restrict: 'A',
                scope: {
                    attributes: '=',
                    updateAttribute: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        if (scope.attributes.length > 0) {
                            var actualRow = scope.attributes[draggedId];
                            element.prop('draggable', true);
                            /** @namespace event.dataTransfer */
                            event.dataTransfer.setData('text', event.target.id)
                        }
                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();

                        var dropPlaceId = element[0].id;

                        if (draggingElement != null) {
                            if (scope.attributes.length > 0) {
                                var actualRow = scope.attributes[draggedId];
                                targetObject = scope.attributes[dropPlaceId];
                                if (actualRow != undefined && actualRow != null && actualRow.parentFile != null) {
                                    actualRowParentId = actualRow.parentFolder;
                                } else {
                                    actualRowParentId = null;
                                }
                            }

                            lastDragOverElem.removeClass('hover');
                            if (targetObject != null && targetObject != undefined) {
                                actualRow.parentFile = targetObject.id;
                                draggingElement = null;
                                scope.updateAttribute(actualRow, targetObject, actualRowParentId);
                            }

                        }
                    });
                }
            }
        });
        module.directive('draglov', function () {
            return {
                restrict: 'A',
                scope: {
                    lov: '=',
                    values: '=',
                    updateLov: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        if (scope.values.length > 0) {
                            var actualRow = scope.values[draggedId];
                            element.prop('draggable', true);
                            /** @namespace event.dataTransfer */
                            event.dataTransfer.setData('text', event.target.id)
                        }
                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();

                        var dropPlaceId = element[0].id;

                        if (draggingElement != null) {
                            if (scope.values.length > 0) {
                                var actualRow = scope.values[draggedId];
                                targetObject = scope.values[dropPlaceId];
                            }

                            lastDragOverElem.removeClass('hover');
                            if (targetObject != null && targetObject != undefined) {
                                draggingElement = null;
                                scope.updateLov(scope.lov, actualRow, targetObject);
                            }

                        }
                    });
                }
            }
        });

        module.directive('dragbomitem', function () {
            return {
                restrict: 'A',
                scope: {
                    bomItems: '=',
                    updateBomItemSeq: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        var actualRow = scope.bomItems[draggedId];
                        if (actualRow != null && actualRow != undefined && actualRow.id != null) {
                            element.prop('draggable', true);
                            event.dataTransfer.setData('text', event.target.id);
                        }

                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();
                        var dropPlaceId = element[0].id;

                        if (draggingElement != null) {
                            if (scope.bomItems.length > 0) {
                                var actualRow = scope.bomItems[draggedId];
                                targetObject = scope.bomItems[dropPlaceId];
                                if (actualRow.parent == targetObject.parent && actualRow.id != targetObject.id) {
                                    scope.updateBomItemSeq(targetObject, actualRow);
                                }
                                lastDragOverElem.removeClass('hover');
                                draggingElement = null;
                            }
                        }
                    });
                }
            }
        });


        module.directive('dragbomitemtombom', function () {
            return {
                restrict: 'A',
                scope: {
                    bomItems: '=',
                    mBomItems: '=',
                    createMBomItem: '=',
                    dragName: '=',
                    dropName: '=',
                    mbom: '=',
                    selectedItems: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        if (scope.dragName == "bomItem") {
                            draggedName = scope.dragName;
                            if (scope.bomItems.length > 0) {
                                actualRow = scope.bomItems[draggedId];
                                if (actualRow != null && actualRow.quantity > actualRow.consumedQty) {
                                    element.prop('draggable', true);
                                    event.dataTransfer.setData('text', event.target.id)
                                } else {
                                    element.prop('draggable', false);
                                }
                            }
                        } else {
                            draggedName = scope.dragName;
                            if (scope.mBomItems.length > 0) {
                                actualRow = scope.mBomItems[draggedId];
                                if (actualRow != null && actualRow != undefined) {
                                    element.prop('draggable', true);
                                    event.dataTransfer.setData('text', event.target.id)
                                }
                            }
                        }
                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();

                        var dropPlaceId = element[0].id;
                        if (dropPlaceId == "mbomHeader" || dropPlaceId == "no-items-row" || dropPlaceId == "mbom-view") {
                            if (draggedName == "bomItem") {
                                if (!scope.mbom.released && !scope.mbom.rejected && scope.selectedItems.length > 0) {
                                    scope.createMBomItem(scope.selectedItems, null, 'multiple', "BOMITEM");
                                } else {
                                    actualRow = scope.bomItems[draggedId];
                                    if (!scope.mbom.released && !scope.mbom.rejected && actualRow != undefined && actualRow != null && actualRow.quantity > actualRow.consumedQty) {
                                        scope.createMBomItem(actualRow, null, 'single', actualRow.objectType);
                                    }
                                }
                            } else {
                                actualRow = scope.mBomItems[draggedId];
                                scope.createMBomItem(actualRow, null, 'single', actualRow.objectType);
                            }
                            draggingElement = null;
                        } else {
                            if (draggingElement != null && scope.dropName == "mbomItem") {
                                if (draggedName == "bomItem") {
                                    actualRow = scope.bomItems[draggedId];
                                } else {
                                    actualRow = scope.mBomItems[draggedId];
                                }
                                targetObject = scope.mBomItems[dropPlaceId];

                                lastDragOverElem.removeClass('hover');
                                if (!scope.mbom.released && !scope.mbom.rejected && targetObject != null && targetObject != undefined && targetObject.objectType == "MBOMITEM") {
                                    draggingElement = null;
                                    if (draggedName == "bomItem") {
                                        if (scope.selectedItems.length > 0) {
                                            if (targetObject.type == "PHANTOM" || (targetObject.type == "NORMAL" && targetObject.itemRevisionHasBom)) {
                                                scope.createMBomItem(scope.selectedItems, targetObject, 'multiple', "BOMITEM");
                                            }
                                        } else {
                                            if (actualRow != null && actualRow != undefined && actualRow.quantity > actualRow.consumedQty && actualRow.asReleasedRevision != targetObject.asReleasedRevision) {
                                                if (targetObject.type == "PHANTOM" || (targetObject.type == "NORMAL" && targetObject.itemRevisionHasBom)) {
                                                    scope.createMBomItem(actualRow, targetObject, 'single', actualRow.objectType);
                                                }
                                            }
                                        }
                                    } else {
                                        if ((targetObject.type == "PHANTOM" || targetObject.itemRevisionHasBom) && actualRow.parent != targetObject.id) {
                                            scope.createMBomItem(actualRow, targetObject, 'single', actualRow.objectType);
                                        }
                                    }
                                }

                            }
                        }
                    });
                }
            }
        });

        module.directive('dragactivitytask', function () {
            return {
                restrict: 'A',
                scope: {
                    activityTasks: '=',
                    updateTaskSeq: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        var actualRow = scope.activityTasks[draggedId];

                        element.prop('draggable', true);
                        event.dataTransfer.setData('text', event.target.id);

                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();
                        var dropPlaceId = element[0].id;

                        if (draggingElement != null) {
                            if (scope.activityTasks.length > 0) {
                                var actualRow = scope.activityTasks[draggedId];
                                targetObject = scope.activityTasks[dropPlaceId];
                                if (actualRow.activity == targetObject.activity) {
                                    scope.updateTaskSeq(actualRow, targetObject);
                                }
                                lastDragOverElem.removeClass('hover');
                                draggingElement = null;
                            }
                        }
                    });
                }
            }
        });
        module.directive('dragprojectwbs', function () {
            return {
                restrict: 'A',
                scope: {
                    wbsItems: '=',
                    updateWbsSeq: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        var actualRow = scope.wbsItems[draggedId];

                        element.prop('draggable', true);
                        event.dataTransfer.setData('text', event.target.id);

                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();
                        var dropPlaceId = element[0].id;

                        if (draggingElement != null) {
                            if (scope.wbsItems.length > 0) {
                                var actualRow = scope.wbsItems[draggedId];
                                targetObject = scope.wbsItems[dropPlaceId];
                                if (actualRow.parentId == targetObject.parentId) {
                                    scope.updateWbsSeq(actualRow, targetObject);
                                }
                                lastDragOverElem.removeClass('hover');
                                draggingElement = null;
                            }
                        }
                    });
                }
            }
        });
        module.directive('dragwidget', function () {
            return {
                restrict: 'A',
                scope: {
                    widgets: '=',
                    updateWidgetSeq: '='
                },
                link: function (scope, element, attrs) {
                    element.prop('draggable', true);
                    element.on('dragstart', function (event) {
                        draggingElement = element;
                        draggedId = element[0].id;
                        var actualRow = scope.widgets[draggedId];

                        element.prop('draggable', true);
                        event.dataTransfer.setData('text', event.target.id);

                    });

                    element.on('dragover', function (event) {
                        event.preventDefault();
                        var id = element[0].id;
                        if (lastDragOverId != id) {
                            element.removeClass('hover');
                            if (lastDragOverElem != null) {
                                element.removeClass('hover');
                            }
                            element.addClass('hover');
                            lastDragOverElem = element;
                            lastDragOverId = id;
                        }
                    });
                    element.on('drop', function (event) {
                        event.preventDefault();
                        var dropPlaceId = element[0].id;

                        if (draggingElement != null) {
                            if (scope.widgets.length > 0) {
                                var actualRow = scope.widgets[draggedId];
                                targetObject = scope.widgets[dropPlaceId];
                                scope.updateWidgetSeq(actualRow, targetObject);
                                lastDragOverElem.removeClass('hover');
                                draggingElement = null;
                            }
                        }
                    });
                }
            }
        });

        module.directive('fitcontent', ['$compile', '$timeout', '$rootScope',
                function ($compile, $timeout, $rootScope) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(adjustHeight);

                            function adjustHeight() {
                                var hasToolbar = $(elm).find('.view-toolbar').length > 0;

                                var tabContent = $(elm).find('.tab-content').length > 0;
                                var adminRightSideView = $(elm).find("#admin-rightView").length > 0;
                                var barHeight = $('#admin-rightView-bar').outerHeight();
                                var detailsFooter = $(elm).find("#detailsFooter").length > 0;
                                //var detailsHeader = $(elm).find('#viewTitleContainer').outerHeight();

                                var height = $(window).height();
                                $("#contentpanel").height(height - 80);
                                height = $("#contentpanel").outerHeight();
                                if ($rootScope.viewInfo.showDetails) {
                                    $("#contentpanel").height(height - 60);
                                }
                                height = $("#contentpanel").outerHeight();
                                //$(elm).height(height-5);
                                if (hasToolbar) {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(height - 92);
                                    } else {
                                        $(elm).find('.view-content').height(height - 47);
                                    }

                                }
                                else {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(height - 42);
                                    } else {
                                        $(elm).find('.view-content').height(height - 2);
                                        if (adminRightSideView) {
                                            if (barHeight != null) {
                                                $("#admin-rightView").height(height - barHeight);
                                            } else {
                                                $("#admin-rightView").height(height - 30);
                                            }
                                        }
                                    }
                                }

                                if (tabContent) {
                                    var height1 = $("#contentpanel").height();
                                    if (detailsFooter) {
                                        $('.tab-content').height(height1 - 155);
                                        $('.tab-pane').height('100%');
                                    } else {
                                        $('.tab-content').height(height1 - 115);
                                        $('.tab-pane').height('100%');

                                        $('.itemtype-view .tab-content').height(height1 - 75);
                                    }
                                }
                            }

                            $timeout(function () {
                                adjustHeight();
                            });
                        }
                    };
                }
            ]
        );

        module.directive('privilegeType', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-success label-outline bg-light-success': type == 'GRANTED', " +
                "'label-danger label-outline bg-light-danger': type == 'DENIED'}\">" +
                "{{type}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'type': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('auditPlanStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning label-outline bg-light-warning': object.status == 'NONE', " +
                "'label-info label-outline bg-light-success': object.status == 'PLANNED'," +
                "'label-lightblue label-outline bg-light-primary': object.status == 'APPROVED'," +
                "'label-success label-outline bg-light-danger': object.status == 'COMPLETED'}\">" +
                "{{object.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('autofocus', ['$timeout', function ($timeout) {
            return {
                restrict: 'A',
                link: function ($scope, $element) {
                    $timeout(function () {
                        $element[0].focus();
                    });
                }
            }
        }]);

        module.filter('ellipsis', function () {
            return function (text, length, bomFreeTextQuery) {
                var firstIndex = null;
                var copyText = null;
                var firstText = null;
                var secondText = null;
                var searchText = null;
                if (text != null && text.length > length) {
                    copyText = angular.copy(text.toLowerCase());

                    copyText = copyText.substr(0, length) + "...";
                    text = text.substr(0, length) + "...";

                    if (bomFreeTextQuery != null && bomFreeTextQuery != "") {
                        bomFreeTextQuery = bomFreeTextQuery.toLowerCase();
                        firstIndex = copyText.indexOf(bomFreeTextQuery);
                        if (firstIndex != -1) {
                            firstText = text.substr(0, firstIndex);
                            secondText = text.substr(firstIndex + bomFreeTextQuery.length, length);
                            searchText = text.substr(firstIndex, bomFreeTextQuery.length);

                            text = firstText + "<span class='highlighted'>{0}</span>".format(searchText) + secondText;
                        }
                    }
                    return text;
                } else {
                    if (text != null && bomFreeTextQuery != null && bomFreeTextQuery != "") {
                        copyText = angular.copy(text.toLowerCase());

                        copyText = copyText.substr(0, length);
                        text = text.substr(0, length);
                        bomFreeTextQuery = bomFreeTextQuery.toLowerCase();
                        firstIndex = copyText.indexOf(bomFreeTextQuery);
                        if (firstIndex != -1) {
                            firstText = text.substr(0, firstIndex);
                            secondText = text.substr(firstIndex + bomFreeTextQuery.length, length);
                            searchText = text.substr(firstIndex, bomFreeTextQuery.length);

                            text = firstText + "<span class='highlighted'>{0}</span>".format(searchText) + secondText;
                        }
                    }
                    return text;
                }
            }
        });

        module.filter('bytes', function () {
            return function (bytes, precision) {
                if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) return '-';
                if (typeof precision === 'undefined') precision = 1;
                var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
                    number = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + ' ' + units[number];
            }
        });
        module.directive('infiniteScroll', ['$compile', '$timeout', '$rootScope', '$parse', '$window',
                function ($compile, $timeout, $rootScope, $parse, $window) {
                    return {
                        restrict: 'A',
                        scope: {
                            infiniteScrollCallbackFn: '&'
                        },
                        link: function (scope, elem, attrs) {
                            var $element = elem[0];

                            $($element).bind("wheel", function () {
                                var scrollTop = $element.scrollTop,
                                    scrollHeight = $element.scrollHeight,
                                    offsetHeight = $element.offsetHeight;
                                if (scrollTop >= (scrollHeight - offsetHeight)) {
                                    scope.$apply(function () {
                                        scope.infiniteScrollCallbackFn(scope);
                                    });
                                }
                            });
                        }
                    };
                }
            ]
        );

        module.directive('onMonthPicker', ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    var today = new Date();
                    return {
                        restrict: 'A',
                        scope: {
                            month: '=',
                            plan: '=',
                            plannedYear: '=',
                            updatePlannedDate: '=',
                            updateCompletedDate: '='
                        },
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var month = 0;
                            if (scope.month != null && scope.month != "" && scope.month != undefined) {
                                month = scope.month.value;
                            }
                            var d = new Date();
                            var currYear = d.getFullYear();
                            if (scope.plannedYear != null && scope.plannedYear != "" && scope.plannedYear != undefined) {
                                currYear = scope.plannedYear;
                            }
                            var startDate = new Date(currYear, month, 1);
                            $(elem).datepicker({
                                autoclose: true,
                                dateFormat: 'dd/mm/yy',
                                changeMonth: false,
                                changeYear: false,
                                defaultDate: new Date(currYear, month, 1),
                                onSelect: function (dateText) {
                                    var dt2 = $('#' + attrs["name"]);
                                    dt2.datepicker('setDate', new Date(currYear, month, 1));
                                    dt2.datepicker('option', 'minDate', startDate);
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                        if (scope.updatePlannedDate != undefined) {
                                            scope.updatePlannedDate(scope.plan, scope.month, dateText);
                                        }
                                        if (scope.updateCompletedDate != null) {
                                            scope.updateCompletedDate(scope.plan, scope.month, dateText);
                                        }
                                    });
                                }
                            }).focus(function () {
                                $(".ui-datepicker-next").hide();
                                $(".ui-datepicker-prev").hide();
                            });
                        }
                    };
                }
            ]
        );
    }
);
