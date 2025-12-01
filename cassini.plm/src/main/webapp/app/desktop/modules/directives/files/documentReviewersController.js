define(
    [
        'app/desktop/modules/main/main.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/desktop/directives/person-avatar/personAvatarDirective',
        'app/shared/services/core/documentService',
        'app/shared/services/core/mfrPartsService'
    ],
    function (module) {
        module.controller('DocumentReviewersController', DocumentReviewersController);

        function DocumentReviewersController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                             CommonService, LoginService, PersonGroupService, DocumentService, MfrPartsService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            $scope.noUserSelect = parsed.html($translate.instant("NO_USER_SELECT_MSG")).html();
            $scope.selectReviewerApproverMsg = parsed.html($translate.instant("SELECT_REVIEWER_APPROVER_MSG")).html();

            vm.logins = [];

            vm.searchUsers = '';
            vm.document = $scope.data.documentData;
            vm.documentType = $scope.data.reviewerFileType;

            var mapUsers = new Hashtable();

            function loadUsers() {
                $rootScope.showBusyIndicator();
                LoginService.getInternalActiveLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            mapUsers.put(login.person.id, login);
                        });
                        vm.logins = data;
                        var revLogins = updateReviewerRefs(vm.logins);
                        PersonGroupService.getLoginPersonGroupReferences(vm.logins, 'defaultGroup');
                        PersonGroupService.getLoginPersonGroupReferences(revLogins, 'defaultGroup');
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateReviewerRefs(logins) {
                var revLogins = [];
                angular.forEach(vm.document.reviewers, function (reviewer) {
                    var login = mapUsers.get(reviewer.reviewer);
                    if (login != null) {
                        reviewer.login = login;
                        logins.splice(logins.indexOf(login), 1);
                        mapUsers.remove(reviewer.reviewer);
                        revLogins.push(login);
                    }
                });
                return revLogins;
            }

            vm.addReviewer = addReviewer;
            function addReviewer(login) {
                $rootScope.showBusyIndicator();
                var reviewer = {
                    login: login,
                    reviewer: login.person.id,
                    approver: false
                };
                reviewer.document = vm.document.id;
                DocumentService.addReviewer(vm.document.id, reviewer).then(
                    function (data) {
                        data.login = login;
                        vm.document.reviewers.push(data);
                        vm.document.signOffCount++;
                        vm.document.approver = false;
                        vm.document.reviewer = false;
                        angular.forEach(vm.document.reviewers, function (reviewer) {
                            if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                vm.document.approver = true;
                            } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                vm.document.reviewer = true;
                            }
                        })
                        vm.logins.splice(vm.logins.indexOf(login), 1);
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }


            vm.updateReviewer = updateReviewer;
            function updateReviewer(reviewer) {
                $rootScope.showBusyIndicator();
                DocumentService.updateReviewer(vm.document.id, reviewer).then(
                    function (data) {
                        vm.document.approver = false;
                        vm.document.reviewer = false;
                        angular.forEach(vm.document.reviewers, function (reviewer) {
                            if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                vm.document.approver = true;
                            } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                vm.document.reviewer = true;
                            }
                        })
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            vm.deleteReviewer = deleteReviewer;
            function deleteReviewer(reviewer) {
                $rootScope.showBusyIndicator();
                DocumentService.deleteReviewer(vm.document.id, reviewer).then(
                    function (data) {
                        vm.document.reviewers.splice(vm.document.reviewers.indexOf(reviewer), 1);
                        vm.document.signOffCount--;
                        vm.document.approver = false;
                        vm.document.reviewer = false;
                        angular.forEach(vm.document.reviewers, function (reviewer) {
                            if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                vm.document.approver = true;
                            } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                vm.document.reviewer = true;
                            }
                        })
                        vm.logins.push(reviewer.login);
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadReviewers() {
                DocumentService.getReviewersAndApprovers(vm.document.id).then(
                    function (data) {
                        vm.document.reviewers = data;
                    }
                )
            }


            (function () {

                loadUsers();
                loadReviewers();

            })();
        }
    }
)
;