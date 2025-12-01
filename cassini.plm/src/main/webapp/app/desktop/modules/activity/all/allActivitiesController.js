define(
    [
        'app/desktop/modules/activity/activity.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('AllActivitiesController', AllActivitiesController);

        function AllActivitiesController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, $translate, $window, $interval,
                                         CommonService) {

            $rootScope.viewInfo.icon = "fa fa-history";
            $rootScope.viewInfo.title = $translate.instant('LOG_HISTORY_TITLE');
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            var parsed = angular.element("<div></div>")

            vm.loading = true;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "time",
                    order: "DESC"
                }
            };
            vm.logHistoryExport = [];
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.logHistory = angular.copy(pagedResults);
            vm.logHistory = [];
            vm.persons = [];


            var eventHeaders = ["Type", "Date", "Activity", "User"];
            var initColumns = {
                "Type": {
                    "columnName": "Type",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Date": {
                    "columnName": "Date",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Activity": {
                    "columnName": "Activity",
                    "columnValue": null,
                    "columnType": "string"
                },
                "User": {
                    "columnName": "User",
                    "columnValue": null,
                    "columnType": "string"
                }
            };

            vm.exportExcel = exportExcel;
            function htmlToPlaintext(text) {
                return text ? String(text).replace(/<[^>]+>/gm, '') : '';
            }

            function exportExcel() {
                var exportRows = [];
                var empty = null;
                $rootScope.showBusyIndicator();
                for (var i = 0; i < vm.logHistoryExport.length; i++) {
                    var exportRwDetails = [];
                    var emptyColumns = angular.copy(initColumns);
                    angular.forEach(eventHeaders, function (header) {
                        empty = emptyColumns[header];
                        if (empty != undefined) {
                            var event = vm.logHistoryExport[i];
                            if (empty.columnName == "Type" || empty.columnName == "Date"
                                || empty.columnName == "Activity" || empty.columnName == "User") {
                                if (empty.columnName == "Type") {
                                    empty.columnValue = event.type;
                                }
                                if (empty.columnName == "Date") {
                                    empty.columnValue = event.time;
                                }
                                if (empty.columnName == "Activity") {
                                    empty.columnValue = htmlToPlaintext(event.name);
                                }
                                if (empty.columnName == "User") {
                                    empty.columnValue = event.userName;

                                }

                            }

                            exportRwDetails.push(empty);
                        }
                    });
                    var exporter = {
                        exportRowDetails: exportRwDetails
                    };
                    exportRows.push(exporter);
                }
                var mostUsedMfrParts = parsed.html($translate.instant("ACTIVITY_STREAMS_DOWNLOAD_NAME")).html();
                var exportObject = {
                    "exportRows": exportRows,
                    "fileName": "Activity_Streams",
                    "headers": angular.copy(eventHeaders)
                };

                CommonService.exportReport("EXCEL", exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');
                        $rootScope.hideBusyIndicator();

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                });
            })();
        }
    }
);