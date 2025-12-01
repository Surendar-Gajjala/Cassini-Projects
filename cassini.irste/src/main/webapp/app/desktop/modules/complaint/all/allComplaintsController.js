/**
 * Created by Nageshreddy on 20-11-2018.
 */
define(
    [
        'app/desktop/modules/complaint/complaint.module',
        'app/shared/services/core/complaintService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AllComplaintsController', AllComplaintsController);

        function AllComplaintsController($scope, $rootScope, $timeout, $window, $state, $cookies, $uibModal,
                                         ComplaintService, CommonService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-wpforms";
            $rootScope.viewInfo.title = "Complaints";

            var vm = this;
            vm.loading = false;
            vm.searchTerm = null;
            vm.showComplaintDetails = showComplaintDetails;
            vm.showComplaintHistory = showComplaintHistory;
            vm.deleteComplaint = deleteComplaint;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.statusUpdate = statusUpdate;
            vm.exportReport = exportReport;
            vm.openComplaintDetails = openComplaintDetails;
            vm.clearFilter = clearFilter;
           // vm.clearDateFilter =  clearDateFilter;
            vm.showSearchMode = false;
            $scope.freeTextQuery = null;

            var pageable = {
                page: 0,
                size: 15,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.complaints = angular.copy(vm.pagedResults);
            $scope.personType = null;

            vm.emptyFilters = {
                location: null,
                searchQuery: null,
                utility: null,
                groupUtility: null,
                responder: null,
                facilitator: null,
                assistor: null
            };

            vm.filters = angular.copy(vm.emptyFilters);
            vm.selectedStepName = null;
            vm.showClearButton = false;

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.newComplaint = newComplaint;
            vm.startComplaint = startComplaint;
            vm.resolvedComplaint = resolvedComplaint;


            function resolvedComplaint(complaint) {
                if ($scope.personType != null) {
                    if ($scope.personType.name.toLowerCase() == ('Responder').toLowerCase() ||
                        $scope.personType.name.toLowerCase() == ('Administrator').toLowerCase()) {
                        complaint.status = 'COMPLETED';
                    } else if ($scope.personType.name.toLowerCase() == ('Assistor').toLowerCase()) {
                        complaint.status = 'ASSISTED';
                    } else if ($scope.personType.name.toLowerCase() == ('Facilitator').toLowerCase()) {
                        complaint.status = 'FACILITATED';
                    }
                    statusUpdate(complaint);
                }
            }

            function startComplaint(complaint) {
                complaint.status = 'INPROGRESS';
                statusUpdate(complaint);
            }

            function statusUpdate(complaint) {
                ComplaintService.updateComplaint(complaint).then(
                    function (data) {
                        var complaint1 = data;
                        CommonService.getPersonReferences([complaint1], 'person');
                        var index = vm.complaints.content.indexOf(complaint);
                        if (index !== -1) {
                            vm.complaints.content[index] = complaint1;
                        }
                    }
                )
            }

            function newComplaint() {
                var options = {
                    title: 'New Complaint',
                    template: 'app/desktop/modules/main/new/newComplaintView.jsp',
                    controller: 'NewComplaintController as newCompVm',
                    resolve: 'app/desktop/modules/main/new/newComplaintController',
                    width: 500,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.comps.new'}
                    ],
                    callback: function (complaint) {
                        $rootScope.showSuccessMessage("Complaint " + complaint.complaintNumber + " Created Successfully");
                        $rootScope.hideSidePanel();
                        loadComplaintsBasedOnLogin();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                pageable.page++;
                loadComplaintsBasedOnLogin();
            }

            function previousPage() {
                pageable.page--;
                loadComplaintsBasedOnLogin();
            }

            function freeTextSearch(searchTerm) {
                $scope.freeTextQuery = searchTerm;
                vm.loading = true;
                if (searchTerm != null && searchTerm != undefined && searchTerm.trim() != "") {
                    pageable.page = 0;
                    vm.filters.searchQuery = searchTerm;
                    ComplaintService.freeTextSearch(pageable, vm.filters).then(
                        function (data) {
                            vm.complaints = data;
                            vm.showSearchMode = true;
                            assignValues();
                            vm.loading = false;
                        }
                    )
                } else {
                    resetPage();
                    loadComplaintsBasedOnLogin();
                    $scope.freeTextQuery = null;
                    vm.filters.searchQuery = null;
                }
            }

            function resetPage() {
                pageable.page = 0;
                vm.filters.searchQuery = null;
                vm.showSearchMode = false;
                loadComplaintsBasedOnLogin();
            }

            function clearFilter() {
                loadComplaints();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

           /* function clearDateFilter() {
                vm.errorMessage = null;
                vm.filters.createDate = null;
                vm.filters.toDate = null;
                loadComplaints();
            }*/

            function loadPersonTypes() {
                CommonService.getAllPersonTypes().then(
                    function (data) {
                        vm.personTypeMap = {};
                        angular.forEach(data, function (type) {
                            vm.personTypeMap[type.id] = type;
                        })
                    }
                )
            }

            function loadComplaints() {
                vm.loading = true;
                ComplaintService.getComplaints(pageable, vm.filters).then(
                    function (data) {
                        vm.complaints = data;
                        assignValues();
                        vm.loading = false;
                    }
                )
            }

            function assignValues() {
                CommonService.getPersonReferences(vm.complaints.content, 'person');
            }

            function showComplaintHistory(complaint) {
                var options = {
                    title: "Complaint: " + complaint.complaintNumber + " History",
                    template: 'app/desktop/modules/complaint/history/complaintHistoryView.jsp',
                    controller: 'ComplaintHistoryController as compHistVm',
                    resolve: 'app/desktop/modules/complaint/history/complaintHistoryController',
                    width: 600,
                    showMask: true,
                    data: {
                        complaintData: complaint
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.history.close'}
                    ],
                    callback: function (result) {
                        if (result == 'update') {
                            statusUpdate(complaint);
                        }

                    }
                };
                $rootScope.showSidePanel(options);
            }

            function statusUpdate(complaint) {
                var comp = complaint;
                ComplaintService.updateComplaintStatus(complaint).then(
                    function (data) {
                        complaint.status = data.status;
                        CommonService.getPersonReferences([complaint], 'person');
                        var index = vm.complaints.content.indexOf(comp);
                        if (index !== -1) {
                            vm.complaints.content[index] = complaint;
                        }
                    }
                )
            }


            function deleteComplaint(complaint) {
                var options = {
                    title: 'Delete Complaint',
                    message: 'Please confirm to delete this complaint',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ComplaintService.deleteComplaint(complaint.id).then(
                            function (data) {
                                var index = vm.complaints.content.indexOf(complaint);
                                vm.complaints.content.splice(index, 1);
                                $rootScope.showSuccessMessage("Complaint " + complaint.complaintNumber + " Deleted Successfully");
                            }
                        )
                    }
                });

            }

            function showComplaintDetails(complaint) {
                //$state.go('app.complaints.details', {complaintId: complaint.id});
                var options = {
                    title: "Complaint Details",
                    template: 'app/desktop/modules/complaint/details/complaintDetailsView.jsp',
                    controller: 'ComplaintDetailsController as detailsVm',
                    resolve: 'app/desktop/modules/complaint/details/complaintDetailsController',
                    width: 550,
                    showMask: true,
                    data: {
                        complaintData: complaint,
                        personType: $scope.personType
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.details.close'}
                    ],
                    callback: function (comp) {
                        var index = vm.complaints.content.indexOf(complaint);
                        if (index !== -1) {
                            vm.complaints.content[index] = comp;
                            complaint = comp;
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function loadComplaintsBasedOnLogin() {
                vm.filters = angular.copy(vm.emptyFilters);
                if ($rootScope.loginPersonDetails.person.id == 1) {
                    CommonService.getPersonType($rootScope.loginPersonDetails.person.personType).then(
                        function (data) {
                            $scope.personType = data;
                        });
                    loadComplaints()
                } else {
                    CommonService.getPersonType($rootScope.loginPersonDetails.person.personType).then(
                        function (data) {
                            $scope.personType = data;
                            if (data.name.toLowerCase() == ('Responder').toLowerCase()) {
                                vm.filters.responder = $rootScope.loginPersonDetails.person.id;
                            } else if (data.name.toLowerCase() == ('Assistor').toLowerCase()) {
                                vm.filters.assistor = $rootScope.loginPersonDetails.person.id;
                            } else if (data.name.toLowerCase() == ('Facilitator').toLowerCase()) {
                                vm.filters.facilitator = $rootScope.loginPersonDetails.person.id;
                            }
                            loadUserComplaints();
                        }
                    )
                }
            }

            function loadUserComplaints() {
                vm.loading = true;
                ComplaintService.getComplaintsByFilter(pageable, vm.filters).then(
                    function (data) {
                        vm.complaints = data;
                        assignValues();
                        vm.loading = false;
                    }
                )
            }




            vm.filters = {
                searchQuery: ""
            };
            vm.reportRows = [];

            function searchReport() {
                vm.reportRows = [];
                vm.loading = true;
                vm.maprows = [];
                ComplaintService.getReportByDates(vm.complaint.id).then(
                    function (data) {
                        vm.reportRows = data;
                        vm.loading = false;
                    }
                )
            }

            function openComplaintDetails(complaint) {
                $('#search-results-container').hide();
                vm.selectedProject = complaint;
                vm.filters.searchQuery = complaint.name;
                if(complaint != null) {
                    searchReport();
                }
            }
            function exportReport() {
                $rootScope.showExportMessage("Report Exporting in progress");
                ComplaintService.exportUtilityReport().then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        var c = document.cookie.split("; ");
                        for (i in c)
                            document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
                        window.open(url, '_self');
                        $rootScope.showSuccessMessage("Report exported successfully");
                    }
                )
            }

            (function () {
                loadPersonTypes();
                loadComplaintsBasedOnLogin();
            })();
        }
    }
);