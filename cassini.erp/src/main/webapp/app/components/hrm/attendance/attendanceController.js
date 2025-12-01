define(['app/app.modules', 'dropzone', 'app/components/hrm/attendance/attendanceFactory'],
    function($app) {
        $app.controller('AttendanceController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'attendanceFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies, attendanceFactory) {

                    $rootScope.iconClass = "fa flaticon-finance-and-business4";
                    $rootScope.viewTitle = "Attendance";
                    
                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('attendance-view-tb')
                    });

                    var staticMonths = [
                            [{name: 'JAN', index: 1},{name: 'FEB', index: 2},{name: 'MAR', index: 3}],
                            [{name: 'APR', index: 4},{name: 'MAY', index: 5},{name: 'JUN', index: 6}],
                            [{name: 'JUL', index: 7},{name: 'AUG', index: 8},{name: 'SEP', index: 9}],
                            [{name: 'OCT', index: 10},{name: 'NOV', index: 11},{name: 'DEC', index: 12}]
                        ],
                        selectedMonth = 1,
                        isCurrentMonthImported = false;
                        isPageLoaded = false,
                        init = function() {
                            var dt = new Date(),
                                param = {};
                                param.month = dt.getMonth()-1;
                                param.year = dt.getFullYear();

                            attendanceFactory.getCurrentMonthImportStatus(param).then (
                                function(response) {
                                    if(response > 0)
                                    isCurrentMonthImported = true;
                                    $scope.months = angular.copy(staticMonths);
                                },
                                function (error) {

                                }
                            );
                        };

                    $scope.currentYear = new Date().getFullYear();
                    $scope.months = [];
                    $scope.file = {
                        isUploaded: false
                    };

                    $scope.importAttendance = function() {
                        if(fileDropZone.files.length > 0) {
                            var url = "api/hrm/employeeattendace/attachment"
                            fileDropZone.options.url = url;
                            fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                        }
                    };

                    $scope.isAttendanceImported = function(month) {
                        var isPayrollRun = false,
                            isCurrentMonth = (month === new Date().getMonth());

                        if(isCurrentMonth && isCurrentMonthImported){
                            isPayrollRun = true;
                        }

                        return  isPayrollRun;
                    };

                    $scope.isMonthCurrent = function(index) {
                        return new Date().getMonth() == index;
                    };

                    $scope.isMonthBeforeCurrentMonth = function(index) {
                        return (index < new Date().getMonth());
                    };

                    $scope.isYearCurrent = function() {
                        return ($scope.currentYear == new Date().getFullYear());
                    };

                    $scope.nextYear = function() {
                        $scope.currentYear++;
                        $scope.months = [];
                        $scope.months = angular.copy(staticMonths);
                    };

                    $scope.previousYear = function() {
                        $scope.currentYear--;
                        $scope.months = angular.copy(staticMonths);
                    };

                    
                    $rootScope.navigateTimeOffs = function () {
                    	$state.go('app.hrm.timeoffs');
                    };

                    $rootScope.navigate = function () {
                    	
                    };
                    
                    $scope.viewAttendance = function(month) {
                        $state.go('app.hrm.attendance.employees', {'month':month, 'year': $scope.currentYear });
                    };


                    $scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
                        if(!isPageLoaded && !isCurrentMonthImported) {
                            initDropzone();
                            isPageLoaded = true;
                        }
                    });

                    function initDropzone() {
                        var containerId = "#attachmentsContainer";
                        var formId = "#attachmentsForm";
                        var dropZoneId = "#attachmentsDropZone";

                        $(containerId).on('dragover', handleDragEnter);
                        $(containerId).on('dragleave', handleDragLeave);
                        $(containerId).on('drop', handleDragLeave);

                        var previewNode = document.querySelector("#template");
                        previewNode.id = "";
                        var previewTemplate = previewNode.parentNode.innerHTML;
                        previewNode.parentNode.removeChild(previewNode);

                        $('#importAttendanceBtn').hide();

                        var myDropzone = new Dropzone(document.querySelector(formId), { // Make the whole body a dropzone
                            url: "/target", // Set the url
                            thumbnailWidth: 80,
                            thumbnailHeight: 80,
                            parallelUploads: 20,
                            previewTemplate: previewTemplate,
                            autoQueue: false, // Make sure the files aren't queued until manually added
                            previewsContainer: null, // Define the container to display the previews
                            clickable: ".fileinput-button" // Define the element that should be used as click trigger to select files.
                        });

                        myDropzone.on("addedfile", function (file) {
                            // Hookup the start button
                            $(dropZoneId).hide();
                            $('#addAttendanceBtn,#importAttendanceBtn').toggle();
                            isCurrentMonthImported = true;
                            $scope.months = angular.copy(staticMonths);
                        });

                        myDropzone.on("removedfile", function (file) {
                            if (myDropzone.files.length === 0) {
                                $(dropZoneId).show();
                                $('#addAttendanceBtn,#importAttendanceBtn').toggle();
                            }
                        });

                        myDropzone.on("sending", function (file) {

                        });

                        // Hide the total progress bar when nothing's uploading anymore
                        myDropzone.on("queuecomplete", function (progress) {
                            alert("Attendence Uploaded Successfully");
                        });


                        fileDropZone = myDropzone;

                        function handleDragEnter(e) {
                            this.classList.add('drag-over');
                        }

                        function handleDragLeave(e) {
                            this.classList.remove('drag-over');
                        }
                    }

                    init();
                }
            ]
        );
    }
);




