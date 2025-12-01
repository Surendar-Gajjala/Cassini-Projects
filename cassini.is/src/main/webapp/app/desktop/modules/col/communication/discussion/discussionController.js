define(
    [
        'app/desktop/modules/col/col.module',
        'app/desktop/modules/col/communication/newGroupDialogController',
        'app/shared/services/communication/communicationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/col/communication/discussion/discussionsDirective',
        'dropzone'
    ],
    function (module) {
        module.controller('DiscussionController', DiscussionController);

        function DiscussionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                      $uibModal, CommonService, CommunicationService) {

            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-comments";
            $rootScope.viewInfo.title = "Communication";

            var vm = this;

            vm.discussionGroups = [];
            vm.discussionMessages = [];
            vm.newMode = false;
            vm.emptyDiscussionGroup = {
                "name": vm.name,
                "description": vm.description,
                "moderator": window.$application.login.person.id,
                "ctxObjectType": "PROJECT",
                "ctxObjectId": $state.params.projectId
            };
            vm.selectedGroup = null;
            vm.replyMessageMode = false;
            vm.replySelectedId = 0;
            vm.projectId = $state.params.projectId;
            var pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "name",
                    order: "DESC"
                }
            };

            var discussionPageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "commentedDate",
                    order: "DESC"
                }
            };
            var emptyDiscussionMessage = {
                "commentedDate": null,
                "commentedBy": window.$application.login.person,
                "comment": null,
                "replyTo": null,
                "discussionGroupId": null,
                "ctxObjectType": "PROJECT",
                "ctxObjectId": $stateParams.projectId,
                "messageAttachments": []
            };
            vm.newDiscussionMessage = angular.copy(emptyDiscussionMessage);

            vm.newReplyDiscussionMessage = angular.copy(emptyDiscussionMessage);

            var postedBy = null;

            vm.loadMessages = loadMessages;
            vm.addNewDiscussionGroup = addNewDiscussionGroup;
            vm.cancelNew = cancelNew;
            vm.createDiscussionGroup = createDiscussionGroup;
            vm.selectGroup = selectGroup;
            vm.sendMessage = sendMessage;
            vm.sendReplyMessage = sendReplyMessage;
            vm.adjustHeight = adjustHeight;
            vm.replyMessage = replyMessage;

            function addNewDiscussionGroup() {
                vm.newDiscussionGroup = angular.copy(vm.emptyDiscussionGroup);
                vm.newMode = true;
            }

            function cancelNew() {
                vm.newMode = false;
            }

            function loadMessages(group) {
                vm.messages = [];
                CommunicationService.getDiscussionGrpMessagesAll(group.id, discussionPageable, $state.params.projectId).then(
                    function (result) {
                        vm.discussionMessages = result.content.reverse();
                        // updateScroll();
                    }
                );
            }

            function updateScroll() {
                $timeout(function () {
                    var scroller = document.getElementById("group-messages");
                    scroller.scrollTop = scroller.scrollHeight;
                }, 1000, false);
            }

            function loadDiscussionGroups() {

                CommunicationService.getDiscussionGroups(pageable, $stateParams.projectId).then(
                    function (data) {
                        vm.discussionGroups = data.content;
                        angular.forEach(vm.discussionGroups, function (group) {
                            group.selected = false;
                        });

                        if (vm.discussionGroups.length > 0) {
                            vm.discussionGroups[0].selected = true;
                            selectGroup(vm.discussionGroups[0]);

                        }
                    }
                )
            }

            function createDiscussionGroup() {

                CommunicationService.createDiscussionGroup(vm.newDiscussionGroup).then(
                    function (result) {
                        vm.discussionGroups.push(result);
                        vm.newMode = false;
                    }
                )

            }

            /*    function createMember(person) {
             var currentId = window.$application.login.person.id;
             var admin = false;

             if(person.id == currentId) {
             admin = true;
             }

             return {
             ctxObjectType: 'PROJECT',
             ctxObjectId: $stateParams.projectId,
             status: 'ACTIVE',
             isAdmin: admin,
             person: person.id
             };
             }*/

            /*   function loadPersons() {
             CommonService.getAllPersons().then(
             function(data) {
             vm.persons = data;
             }
             )
             }*/

            function selectGroup(group) {
                angular.forEach(vm.discussionGroups, function (group) {
                    group.selected = false;
                })

                /*   postedBy = null;

                 var currentId = window.$application.login.person.id;
                 angular.forEach(group.msgGrpMembers, function(member) {
                 if(currentId == member.person) {
                 postedBy = member.id;
                 }
                 });*/

                vm.selectedGroup = group;
                group.selected = true;
                //  loadMessages(group);
            }

            /* function initNewGroup() {
             var currentId = window.$application.login.person.id;
             angular.forEach(vm.persons, function(person) {
             if(person.id == currentId) {
             vm.newGroup.persons.push(person);
             }
             });
             }*/

            function sendMessage() {
                vm.newDiscussionMessage.discussionGroupId = vm.selectedGroup.id;
                CommunicationService.createDiscussionMessage(vm.newDiscussionMessage).then(
                    function (result) {
                        vm.discussionMessages.push(result);
                        //    updateScroll();
                        vm.newDiscussionMessage = angular.copy(emptyDiscussionMessage);
                    }
                );
            }

            function replyMessage(discussion) {
                vm.replyMessageMode = true;
                vm.replySelectedId = discussion.id;

            }

            function sendReplyMessage(discussion) {

                vm.newReplyDiscussionMessage.discussionGroupId = vm.selectedGroup.id;
                vm.newReplyDiscussionMessage.replyTo = discussion.id;
                CommunicationService.createDiscussionMessage(vm.newReplyDiscussionMessage).then(
                    function (result) {
                        //vm.discussionMessages.push(result);
                        discussion.children.push(result);
                        //   updateScroll();
                        //  vm.newDiscussionMessage = angular.copy(emptyDiscussionMessage);
                    }
                );
                vm.replyMessageMode = false;

            }

            function adjustHeight() {
                var el = document.getElementById('message-input');
                el.style.height = (el.scrollHeight > el.clientHeight) ? (el.scrollHeight) + "px" : "65px";
            }

            (function () {
                // loadPersons();
                loadDiscussionGroups();
            })();
        }
    }
);