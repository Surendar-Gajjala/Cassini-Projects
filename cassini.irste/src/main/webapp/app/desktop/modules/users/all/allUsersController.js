/**
 * Created by Nageshreddy on 16-11-2018.
 */
/**
 * Created by Nageshreddy on 08-11-2018.
 */
define(
    [
        'app/desktop/modules/users/users.module',
        'app/shared/services/core/userService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AllUsersController', AllUsersController);

        function AllUsersController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, UserService,
                                         AttachmentService, CommonService, DialogService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Users";


            vm.emptyFilters = {
                status: null,
                searchQuery: null,
                system: null,
                personTypes: []
            };

            vm.workplacePersons = true;
            vm.competencePersons = true;
            vm.councilMembers = true;
            vm.registers = true;
            vm.applicants = true;
            vm.officeAssistant = true;
            vm.advisoryCouncil = true;
            vm.viceChairman = true;
            vm.filters = angular.copy(vm.emptyFilters);
            vm.newResponder = newResponder;
            vm.usersView = 'Responder';
            //vm.facilitatorsView = false;
            //vm.assistorsView = false;
            vm.searchTerm = null;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.clearFilter = clearFilter;
            vm.showSearchMode = false;
            $scope.freeTextQuery = null;
            vm.showCompetenceDetails = showCompetenceDetails;
            vm.loading = true;

            var pageable = {
                page: 0,
                size: 15,
                sort: {
                    field: "modifiedDate",
                    order: "ASC"
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

            vm.responders = angular.copy(vm.pagedResults);

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.filterUsers = filterUsers;

            function nextPage() {
                pageable.page++;
               // loadResponders();
                loadUsers();
            }

            function previousPage() {
                pageable.page--;
                //loadResponders();
                loadUsers();
            }


            function freeTextSearch(searchTerm) {
                $scope.freeTextQuery = searchTerm;
                vm.loading = true;
                if (searchTerm != null && searchTerm != undefined && searchTerm.trim() != "") {
                    pageable.page = 0;
                    vm.filters.searchQuery = searchTerm;
                    UserService.freeTextSearch(pageable, vm.filters).then(
                        function (data) {
                            vm.users = data;
                            vm.showSearchMode = true;
                            assignValues();
                            vm.loading = false;
                        }
                    )
                } else {
                    resetPage();
                    loadUsers();
                    $scope.freeTextQuery = null;
                    vm.filters.searchQuery = null;
                }
            }

            function assignValues() {
                CommonService.getPersonReferences(vm.responders.content, 'person');
            }

            function resetPage() {
                pageable.page = 0;
                vm.filters.searchQuery = null;
                vm.showSearchMode = false;
                loadUsers();
            }


            function clearFilter() {
                loadUsers();
                vm.clear = false;
                $rootScope.showSearch = false;
            }


            function showCompetenceDetails(competence) {
                $state.go('app.competence.details', {competenceId: competence.id})
            }

            function filterUsers(userType) {
                vm.filters = angular.copy(vm.emptyFilters);
                pageable.page = 0;
                vm.filters.personTypes = [];
                vm.usersView = userType;
                vm.filters.personTypes.push(userType);
               // loadResponders();
                loadUsers();
            }

            vm.responders = [];
            function loadUsers(){
                UserService.getFilterUsers(pageable, vm.filters).then(
                    function (data) {
                        vm.users = data;
                        if(vm.users.content.length > 0){
                            angular.forEach(vm.users.content, function(user){
                                if(user.utilities != null){
                                    if(user.utilities.length > 0){
                                        var uties = "";
                                        angular.forEach(user.utilities, function(utility, $index){
                                            uties = uties + utility;
                                            if($index == user.utilities.length-1){
                                                uties = uties + " ";
                                            }else if($index == user.utilities.length-2){
                                                uties = uties + ' and ';
                                            }else{
                                                uties = uties + ', ';
                                            }
                                        });
                                    }
                                }
                                user.utilities = uties;
                            });
                        }

                        vm.loading = false;
                    }
                );
            }

            function newResponder() {
                var options = {
                    title: 'New ' + vm.usersView,
                    template: 'app/desktop/modules/users/new/newUserView.jsp',
                    controller: 'NewUserController as newUserVm',
                    resolve: 'app/desktop/modules/users/new/newUserController',
                    width: 600,
                    showMask: true,
                    buttons: [{text: 'Create', broadcast: 'app.user.new'}
                    ],
                    data: {
                        personType: vm.usersView
                    },
                    callback: function (result) {
                       // loadResponders();
                        loadUsers();
                        $rootScope.showSuccessMessage(result.firstName + " :  created successfully.");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteResponder = deleteResponder;
            function deleteResponder(responder) {
                var options = {
                    title: 'Delete ' + responder.firstName,
                    message: 'Please confirm to delete  ' +  responder.firstName,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        UserService.deleteResponder(responder.id).then(
                            function (data) {
                                var index = vm.users.content.indexOf(responder);
                                vm.users.content.splice(index, 1);
                                vm.users.totalElements--;
                                vm.users.numberOfElements--;
                                $rootScope.showSuccessMessage( responder.firstName + ' deleted successfully');
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            (function () {
                vm.filters.personTypes.push('Responder');
                //loadResponders()
                loadUsers()
            })();
        }

    })
;

