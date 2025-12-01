define(['app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/feedbackService'],


    function (module) {


        module.controller('FeedbackController', FeedbackController);

        function FeedbackController($scope, $rootScope, $timeout, $translate, $state, $stateParams, $cookies, LoginService, CommonService, FeedbackService) {

            var parsed = angular.element("<div></div>");

            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = parsed.html($translate.instant("SUPPORT")).html();
            $rootScope.viewInfo.showDetails = false;




            var vm = this;
            vm.next = false;
            vm.previous = true;
            vm.showNewFeedback = showNewFeedback;
            vm.getAllOpenTickets= getAllOpenTickets;
            vm.getAllClosedTickets = getAllClosedTickets;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.openpageNumber = 1;
            vm.closepageNumber = 1;
            vm.ticketType = 'open';
            vm.count = 0;
            vm.open = parsed.html($translate.instant("OPEN")).html();
            vm.closed = parsed.html($translate.instant("CLOSED")).html();
            vm.newTicket = parsed.html($translate.instant("NEW_TICKET")).html();
            vm.number = parsed.html($translate.instant("NUMBER")).html();
            vm.support = parsed.html($translate.instant("SUPPORT")).html();
            vm.summary = parsed.html($translate.instant("SUMMARY")).html();
            vm.status = parsed.html($translate.instant("STATUS")).html();
            vm.createdDate = parsed.html($translate.instant("CREATED_DATE")).html();
            vm.updatedDate = parsed.html($translate.instant("UPDATED_DATE")).html();
            vm.completedDate = parsed.html($translate.instant("COMPLETED_DATE")).html();


            function nextPage() {
                vm.count++;
                if(vm.count == 0){
                    vm.previous = true;
                } else {
                    vm.previous = false;
                }
                if(vm.ticketType == 'open') {
                    vm.openpageNumber = vm.openpageNumber + 1 ;
                    getAllOpenTickets();
                }
                else if(vm.ticketType == 'close') {
                    vm.closepageNumber = vm.closepageNumber + 1 ;
                    getAllClosedTickets();
                }
                else getAllOpenTickets();
            }

            function previousPage() {
                vm.count--;
                if(vm.count == 0){
                    vm.previous = true;
                } else {
                    vm.previous = false;
                }
                if(vm.ticketType == 'open') {
                    vm.openpageNumber = vm.openpageNumber - 1 ;
                    getAllOpenTickets();
                }
                else if(vm.ticketType == 'close') {
                    vm.closepageNumber = vm.closepageNumber - 1 ;
                    getAllClosedTickets();
                }
                else getAllOpenTickets();
            }

            function getAllOpenTickets() {
                if(vm.openTicketId != ""){
                    $rootScope.showBusyIndicator();
                    FeedbackService.getAllOpenTickets(vm.openTicketId, vm.openpageNumber).then(
                        function(data) {
                            vm.tickets = data;
                            if(vm.tickets.length == 0){
                                vm.next = true;
                                vm.previous = true;
                            } else {
                                if(vm.tickets.length > 24) {
                                    vm.next = false;
                                }
                                else {
                                    vm.next = true;
                                }
                            }
                            $rootScope.hideBusyIndicator();
                        },
                        function(error){
                            console.log(error);
                        }
                    )
                }
            }

            function getAllClosedTickets() {
                if(vm.closeTicketId != ""){
                    $rootScope.showBusyIndicator();
                    FeedbackService.getAllClosedTickets(vm.closeTicketId, vm.closepageNumber).then(
                        function(data) {
                            vm.tickets = data;
                            if(vm.tickets.length == 0){
                                vm.next = true;
                                vm.previous = true;
                            } else {
                                if(vm.tickets.length > 24) {
                                    vm.next = false;
                                }
                                else {
                                    vm.next = true;
                                }
                            }
                            $rootScope.hideBusyIndicator();
                        },
                        function(error){
                            console.log(error);
                        }
                    )
                }
            }

            var createButton = $translate.instant("CREATE");

            function showNewFeedback() {
                var options = {
                    title: vm.newTicket,
                    template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/help/feedback/new/newFeedbackView.jsp',
                    controller: 'NewFeedbackController as newFbVm',
                    resolve: 'app/assets/bower_components/cassini-platform/app/desktop/modules/help/feedback/new/newFeedbackController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.help.createNewFeedback'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            getAllOpenTickets();
                        }, 500);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function loadMappingsData() {
                FeedbackService.getFeedbackMappings().then(
                    function (data) {
                        var tenant = $application.session.tenantId;
                        vm.customerProductMappingData = data[tenant];
                        vm.openTicketId = vm.customerProductMappingData["open_tickets"];
                        vm.closeTicketId = vm.customerProductMappingData["close_tickets"];
                    }
                );
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadMappingsData();
                $timeout(function () {
                    getAllOpenTickets();
                }, 500)
                //}
            })();
        }
    }
);