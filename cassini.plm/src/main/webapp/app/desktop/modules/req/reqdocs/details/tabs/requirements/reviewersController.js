define(
    [
        'app/desktop/modules/req/req.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/desktop/directives/person-avatar/personAvatarDirective',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/reqDocTemplateService'
    ],
    function (module) {
        module.controller('ReviewersController', ReviewersController);

        function ReviewersController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                     CommonService, LoginService, RequirementService, PersonGroupService, ReqDocumentService, ReqDocTemplateService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            $scope.noUserSelect = parsed.html($translate.instant("NO_USER_SELECT_MSG")).html();
            $scope.selectReviewerApproverMsg = parsed.html($translate.instant("SELECT_REVIEWER_APPROVER_MSG")).html();

            vm.logins = {
                content: []
            };

            var pageable = {
                page: 0,
                size: 100,
                sort: {
                    field: "firstName",
                    order: "desc"
                }
            };

            vm.searchUsers = '';
            vm.reviewers = [];
            vm.requirement = $scope.data.requirement;
            vm.reqDoc = $scope.data.reqDoc;
            vm.type = $scope.data.type;

            var mapUsers = new Hashtable();

            function loadUsers() {
                $rootScope.showBusyIndicator();
                LoginService.getAllActiveLogins(pageable).then(
                    function (data) {
                        angular.forEach(data.content, function (login) {
                            mapUsers.put(login.person.id, login);
                        });
                        var revLogins = updateReviewerRefs(data);
                        vm.logins = data;
                        PersonGroupService.getLoginPersonGroupReferences(vm.logins.content, 'defaultGroup');
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
                if (vm.type == 'REQUIREMENTDOCUMENT' || vm.type == 'REQUIREMENT') {
                    angular.forEach(vm.requirement.reviewers, function (reviewer) {
                        var login = mapUsers.get(reviewer.reviewer);
                        if (login != null) {
                            reviewer.login = login;
                            logins.content.splice(logins.content.indexOf(login), 1);
                            mapUsers.remove(reviewer.reviewer);
                            revLogins.push(login);
                        }
                    });
                } else if (vm.type == 'REQUIREMENTDOCUMENTTEMPLATE' || vm.type == 'REQUIREMENTTEMPLATE') {
                    angular.forEach(vm.requirement.reviewers, function (reviewer) {
                        var login = mapUsers.get(reviewer.reviewer);
                        if (login != null) {
                            reviewer.login = login;
                            logins.content.splice(logins.content.indexOf(login), 1);
                            mapUsers.remove(reviewer.reviewer);
                            revLogins.push(login);
                        }
                    });
                }
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
                if (vm.type == 'REQUIREMENT') {
                    reviewer.requirementVersion = vm.requirement.id;
                    reviewer.reqDoc = vm.reqDoc;
                    RequirementService.addReviewer(vm.requirement.id, reviewer).then(
                        function (data) {
                            data.login = login;
                            vm.requirement.reviewers.push(data);
                            vm.logins.content.splice(vm.logins.content.indexOf(login), 1);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.type == 'REQUIREMENTDOCUMENT') {
                    reviewer.requirementDocumentRevision = vm.requirement.id;
                    ReqDocumentService.addReqDocumentReviewer(vm.requirement.id, reviewer).then(
                        function (data) {
                            data.login = login;
                            vm.requirement.reviewers.push(data);
                            vm.logins.content.splice(vm.logins.content.indexOf(login), 1);
                            $rootScope.loadDocumentReviewers();
                            $rootScope.loadReqDocument();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.type == 'REQUIREMENTDOCUMENTTEMPLATE') {
                    reviewer.documentTemplate = vm.requirement.id;
                    ReqDocTemplateService.addReqDocTemplateReviewer(reviewer).then(
                        function (data) {
                            data.login = login;
                            vm.requirement.reviewers.push(data);
                            vm.logins.content.splice(vm.logins.content.indexOf(login), 1);
                            $rootScope.loadDocumentTemplateReviewers();
                            //$rootScope.loadReqDocumentTabCounts();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.type == 'REQUIREMENTTEMPLATE') {
                    reviewer.requirementTemplate = vm.requirement.id;
                    ReqDocTemplateService.addRequirementTemplateReviewer(reviewer).then(
                        function (data) {
                            data.login = login;
                            vm.requirement.reviewers.push(data);
                            vm.logins.content.splice(vm.logins.content.indexOf(login), 1);
                            //$rootScope.loadRequirementTemplateReviwers();
                            // $rootScope.loadReqDocumentTabCounts();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }


            }


            vm.updateReviewer = updateReviewer;
            function updateReviewer(reviewer) {
                if (vm.type == 'REQUIREMENT') {
                    reviewer.reqDoc = vm.reqDoc;
                    RequirementService.updateReviewer(vm.requirement.id, reviewer).then(
                        function (data) {
                            $rootScope.loadRequirement();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                } else if (vm.type == 'REQUIREMENTDOCUMENT') {
                    ReqDocumentService.updateReqDocumentReviewer(vm.requirement.id, reviewer).then(
                        function (data) {
                            $rootScope.loadDocumentReviewers();
                            $rootScope.loadReqDocument();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                } else if (vm.type == 'REQUIREMENTDOCUMENTTEMPLATE') {
                    ReqDocTemplateService.updateReqDocTemplateReviewer(reviewer).then(
                        function (data) {
                            $rootScope.loadDocumentTemplateReviewers();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                } else if (vm.type == 'REQUIREMENTTEMPLATE') {
                    ReqDocTemplateService.updateRequirementTemplateReviewer(reviewer).then(
                        function (data) {
                            $rootScope.loadRequirementTemplateReviwers();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }


            vm.deleteReviewer = deleteReviewer;
            function deleteReviewer(reviewer) {
                if (vm.type == 'REQUIREMENT') {
                    reviewer.reqDoc = vm.reqDoc;
                    RequirementService.deleteReviewer(vm.requirement.id, reviewer).then(
                        function (data) {
                            vm.requirement.reviewers.splice(vm.requirement.reviewers.indexOf(reviewer), 1);
                            vm.logins.content.push(reviewer.login);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);

                        }
                    );
                } else if (vm.type == 'REQUIREMENTDOCUMENT') {
                    ReqDocumentService.deleteReqDocumentReviewer(vm.requirement.id, reviewer).then(
                        function (data) {
                            vm.requirement.reviewers.splice(vm.requirement.reviewers.indexOf(reviewer), 1);
                            vm.logins.content.push(reviewer.login);
                            $rootScope.loadDocumentReviewers();
                            $rootScope.loadReqDocument();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                } else if (vm.type == 'REQUIREMENTDOCUMENTTEMPLATE') {
                    ReqDocTemplateService.deleteReqDocTemplateReviewer(reviewer).then(
                        function (data) {
                            vm.requirement.reviewers.splice(vm.requirement.reviewers.indexOf(reviewer), 1);
                            vm.logins.content.push(reviewer.login);
                            $rootScope.loadDocumentTemplateReviewers();
                            //$rootScope.loadReqDocumentTabCounts();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                } else if (vm.type == 'REQUIREMENTTEMPLATE') {
                    ReqDocTemplateService.deleteRequirementTemplateReviewer(reviewer).then(
                        function (data) {
                            vm.requirement.reviewers.splice(vm.requirement.reviewers.indexOf(reviewer), 1);
                            vm.logins.content.push(reviewer.login);
                            $rootScope.loadRequirementTemplateReviwers();
                            //$rootScope.loadReqDocumentTabCounts();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            (function () {

                loadUsers();

            })();
        }
    }
)
;