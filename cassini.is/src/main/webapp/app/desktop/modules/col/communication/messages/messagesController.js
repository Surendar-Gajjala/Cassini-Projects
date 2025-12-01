define(
    [
        'app/desktop/modules/col/col.module',
        'app/desktop/modules/col/communication/editGroupDialogController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/messageService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('MessagesController', MessagesController);

        function MessagesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $q, $window, $sce,
                                    CommonService, MessageService, DialogService, AttachmentService, LoginService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-comments";
            $rootScope.viewInfo.title = "Communication";
            vm.showInfo = false;
            vm.selectView = false;
            vm.addGroup = false;
            vm.loadingMembers = false;
            vm.selectedGrp = null;
            var colorIndex = 0;
            vm.addedMembers = [];
            vm.loginPersons = [];
            vm.groups = [];
            var grps = [];
            vm.groupMembers = [];
            vm.criteria = {
                searchQuery: ''
            };
            vm.messageCriteria = {
                searchQuery: ''
            };
            var searchMode = false;
            var messageSearchMode = false;
            var scrollToBottom = false;
            var count = 0;
            var groupSearchText = null;
            var messageSearchText = null;
            vm.groupEditMode = false;
            vm.groupIcon = null;
            vm.viewGroupMembers = viewGroupMembers;
            vm.addToGroup = addToGroup;
            vm.back = back;
            vm.removePerson = removePerson;
            vm.newGroup = newGroup;
            vm.createNewGrp = createNewGrp;
            vm.loadGrpMessages = loadGrpMessages;
            vm.postMessage = postMessage;
            vm.deleteGroup = deleteGroup;
            vm.deleteGroupMember = deleteGroupMember;
            vm.exitGroup = exitGroup;
            vm.uploadAttachment = uploadAttachment;
            vm.openAttachment = openAttachment;
            vm.getVideo = getVideo;
            vm.loadGroups = loadGroups;
            vm.cancelNewGrp = cancelNewGrp;
            vm.loadMessages = loadMessages;
            vm.showImage = showImage;
            vm.removeAttachment = removeAttachment;
            vm.editGroup = editGroup;
            vm.updateGroup = updateGroup;
            vm.newMessageGroup = {
                name: null,
                description: null,
                ctxObjectType: 'PROJECT',
                ctxObjectId: $stateParams.projectId,
                messagesCount: 0.0,
                msgGrpMembers: []
            };
            vm.newMessage = {
                msgText: "",
                postedDate: new Date(),
                postedBy: $rootScope.login.person.id,
                ctxObjectType: 'PROJECT',
                ctxObjectId: $stateParams.projectId,
                attachments: []
            };

            var pageableGrp = {
                page: 0,
                size: 12,
                sort: {
                    field: "createdDate",
                    order: "ASC"

                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageableGrp.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            grps = angular.copy(pagedResults);
            var msgs = pagedResults;

            var pageableMessages = {
                page: 0,
                size: 10,
                sort: {
                    field: "postedDate",
                    order: "ASC"

                }
            };

            vm.colors = [
                "#2980b9",
                "#e67e22",
                "#8e44ad",
                "#16a085",
                "#c0392b",
                "#f78fb3",
                "#546de5",
                "#596275",
                "#f19066",
                "#3dc1d3",
                "#cf6a87",
                "#786fa6",
                "#3B3B98",
                "#6D214F",
                "#BDC581",
                "#B33771"
            ];

            function getColor() {
                //var index = Math.floor(Math.random() * Math.floor(vm.colors.length));
                colorIndex++;
                if (colorIndex === vm.colors.length) {
                    colorIndex = 0;
                }
                return {'background-color': vm.colors[colorIndex]};
            }

            function viewGroupMembers(person) {
                vm.selectView = true;
                vm.showInfo = false;
            }

            function back() {
                vm.addedMembers = [];
                if (vm.selectView) {
                    vm.showInfo = true;
                    vm.selectView = false;
                }
                else if (vm.showInfo) {
                    vm.showInfo = false;
                    vm.selectView = false;
                }
                else {
                    vm.showInfo = true;
                }
            }

            function addToGroup(person) {
                vm.loadingMembers = true;
                var grpMember = {
                    person: person.person.id,
                    ctxObjectType: 'PROJECT',
                    ctxObjectId: $stateParams.projectId,
                    messageGroup: vm.selectedGrp
                };
                vm.addedMembers.push(person);
                var index = vm.loginPersons.indexOf(person);
                vm.loginPersons.splice(index, 1);
                MessageService.createGrpMember(grpMember).then(
                    function (data) {
                        data.personName = person.person.fullName;
                        vm.selectedGrp.msgGrpMembers.push(data);
                        person.grpMemberId = data.id;
                        vm.loadingMembers = false;
                    }
                )
            }

            function removePerson(person) {
                vm.loginPersons.push(person);
                MessageService.deleteGrpMember(person.grpMemberId).then(
                    function (data) {
                        var index = vm.selectedGrp.msgGrpMembers.indexOf(person);
                        vm.selectedGrp.msgGrpMembers.splice(index, 1);
                    }
                )
            }

            function exitGroup() {
                var options = {
                    title: 'Exit from ' + vm.selectedGrp.name,
                    message: 'Are you sure you want to exit from this Group?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        MessageService.deleteGrpMember($rootScope.login.grpMemberId).then(
                            function (data) {
                                vm.selectedGrp = null;
                                pageableGrp.page = 0;
                                vm.groups = [];
                                grps = angular.copy(pagedResults);
                                loadGroups();
                            }
                        )
                    }
                });
            }

            function deleteGroupMember(person) {
                var options = {
                    title: 'Remove' + person.personName,
                    message: 'Are you sure you want to remove this Group Member?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        MessageService.deleteGrpMember(person.id).then(
                            function (data) {
                                getLogins();
                                var index = vm.selectedGrp.msgGrpMembers.indexOf(person);
                                vm.selectedGrp.msgGrpMembers.splice(index, 1);
                            }
                        )
                    }
                });
            }

            function newGroup() {
                vm.addGroup = true;
                vm.selectedGrp = null;
                vm.showInfo = false;
                vm.selectView = false;
                loadCreateImage();
            }

            function cancelNewGrp() {
                vm.addGroup = false;
                vm.groupEditMode = false;
                vm.selectedGrp = null;
                vm.groupIcon = null;
                pageableGrp.page = 0;
                vm.groups = [];
                loadGroups();
            }

            function getLogins() {
                LoginService.getActiveLogins().then(
                    function (data) {
                        vm.loginPersons = data;
                        angular.forEach(vm.selectedGrp.msgGrpMembers, function (member) {
                            angular.forEach(vm.loginPersons, function (login) {
                                if (login.person.id == member.person) {
                                    var index = vm.loginPersons.indexOf(login);
                                    vm.loginPersons.splice(index, 1);
                                }
                            });
                        });
                    }
                )
            }

            function createNewGrp() {
                if (vm.newMessageGroup.name == null || vm.newMessageGroup.name == '' || vm.newMessageGroup.name == undefined) {
                    $rootScope.showErrorMessage("Group name cannot be empty");
                }
                else {
                    var grpMember = {
                        person: $rootScope.login.person.id,
                        ctxObjectType: 'PROJECT',
                        ctxObjectId: $stateParams.projectId,
                        isAdmin: true
                    };
                    vm.newMessageGroup.msgGrpMembers.push(grpMember);
                    vm.messageCriteria.searchQuery = "";
                    vm.criteria.searchQuery = "";
                    MessageService.createGroup(vm.newMessageGroup).then(
                        function (data) {
                            vm.addGroup = false;
                            vm.selectedGroupId = data.id;
                            pageableGrp.page = 0;
                            vm.groups = [];
                            grps = angular.copy(pagedResults);
                            vm.newMessageGroup.name = null;
                            vm.newMessageGroup.description = null;
                            vm.newMessageGroup.msgGrpMembers = [];
                            if (vm.groupIcon != null) {
                                MessageService.uploadGroupIcon(vm.selectedGroupId, vm.groupIcon.files[0]).then(
                                    function (data) {
                                        vm.groupIcon = null;
                                        loadGroups();

                                    },
                                    function (error) {
                                        vm.groupIcon = null;
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            } else {
                                loadGroups();
                            }
                        },
                        function (error) {
                            vm.newMessageGroup.msgGrpMembers = [];
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadGroups() {
                vm.groups = [];
                messageSearchMode = false;
                vm.loadingGroups = true;
                vm.messageCriteria.searchQuery = "";
                if (vm.criteria.searchQuery != "" && vm.criteria.searchQuery != null) {
                    if (groupSearchText != null && vm.criteria.searchQuery == groupSearchText && grps.last != true) {
                        pageableGrp.page++;
                    }
                    else if (groupSearchText != null && vm.criteria.searchQuery != groupSearchText) {
                        pageableGrp.page = 0;
                        vm.groups = [];
                        grps = angular.copy(pagedResults);
                        groupSearchText = vm.criteria.searchQuery;
                    }
                    else if (groupSearchText == null) {
                        groupSearchText = vm.criteria.searchQuery;
                        pageableGrp.page = 0;
                        vm.groups = [];
                        grps = angular.copy(pagedResults);
                    }
                }
                else {
                    groupSearchText = null;
                    if (vm.groups.length == 0) {
                        pageableGrp.page = 0;
                        vm.groups = [];
                    }
                    else if (vm.groups.length > 0 && searchMode) {
                        pageableGrp.page = 0;
                        vm.groups = [];
                        grps = angular.copy(pagedResults);
                    }
                    else if (vm.groups.length > 0 && !searchMode) {
                        pageableGrp.page++;
                    }
                }

                if (vm.criteria.searchQuery != "" && vm.criteria.searchQuery != null) {
                    searchMode = true;
                }
                else {
                    searchMode = false;
                }
                if (vm.groups.length == 0 || (vm.groups.length > 0 && grps.last != true)) {
                    MessageService.messageGroupFreeTextSearch("PROJECT", $stateParams.projectId, pageableGrp, vm.criteria).then(
                        function (data) {
                            vm.loadingGroups = false;
                            grps = data;
                            angular.forEach(data.content, function (group) {
                                group.groupIconSrc = null;
                                if (group.groupIcon != null) {
                                    group.groupIconSrc = "api/col/messaging/groups/" + group.id + "/icon?" + new Date().getTime();
                                }
                                group.color = getColor();
                                vm.groups.push(group);
                                pageableMessages.page = Math.ceil(group.messagesCount / pageableMessages.size) - 1;
                            });
                        },
                        function (error) {
                            vm.loadingGroups = false;
                        }
                    );
                }
                else {
                    vm.loadingGroups = false;
                }
            }

            function postMessage() {
                if ((vm.newMessage.msgText != null && vm.newMessage.msgText != "" && vm.newMessage.msgText != " ") || vm.importFile.length > 0) {
                    vm.newMessage.msgGrpId = vm.selectedGrp.id;
                    MessageService.createMessage(vm.newMessage).then(
                        function (groupMessage) {
                            if (vm.importFile) {
                                MessageService.createGroupMessageAttachment(vm.selectedGrp.id, groupMessage.id, "PROJECT", $stateParams.projectId, vm.importFile).then(
                                    function (data) {
                                        groupMessage.attachments = data;
                                        document.getElementById("file").value = "";
                                        $rootScope.hideBusyIndicator();
                                        vm.importFile = [];

                                    }, function () {
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage("Message sent");
                                        document.getElementById("file").value = "";
                                        vm.importFile = [];
                                    }
                                )
                            }
                            vm.selectedGrp.messageList.push(groupMessage);
                            vm.newMessage.msgText = "";
                            if (vm.selectedGrp.messagesCount == 0) {
                                vm.selectedGrp.messagesCount++;
                            }

                            $timeout(function () {
                                var objDiv = document.getElementById("messages");
                                objDiv.scrollTop = objDiv.scrollHeight;

                            }, 1);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadGrpMessages(group) {
                vm.addedMembers = [];
                vm.importFile = [];
                count = 0;
                pageableMessages.page = Math.ceil(group.messagesCount / pageableMessages.size) - 1;
                getLogins();
                vm.showInfo = false;
                vm.selectView = false;
                vm.selectedGrp = group;
                vm.messageCriteria.searchQuery = "";
                messageSearchMode = false;
                msgs = [];
                if (vm.selectedGrp.messagesCount > 0) {
                    MessageService.getAllMessage("PROJECT", $stateParams.projectId, group.id, pageableMessages, vm.messageCriteria).then(
                        function (data) {
                            msgs = data.groupMessages;
                            CommonService.getPersonReferences(data.listGroupMessages, 'createdBy');
                            vm.selectedGrp.messageList = data.listGroupMessages;
                            vm.selectedGrp.groupMessages = data.groupMessages;
                            if (data.listGroupMessages.length < 10 && msgs.first != true) {
                                loadMessages();
                            }
                            else {
                                count = 1;
                                scrollToBottom = true;
                                scroll();
                            }

                        }
                    );
                }
                else {
                    vm.selectedGrp.messageList = [];
                }
                angular.forEach(group.msgGrpMembers, function (member) {
                    member.grpMemberId = member.id;
                    if (member.person == $rootScope.login.person.id) {
                        $rootScope.login.grpMemberId = member.id;
                        $rootScope.login.isAdmin = member.admin;
                    }
                });
            }

            function deleteGroup() {
                MessageService.deleteGroup(vm.selectedGrp.id).then(
                    function (data) {
                        var index = vm.groups.indexOf(vm.selectedGrp);
                        vm.groups.splice(index, 1);
                        vm.selectedGrp = null;
                    }
                )
            }

            function loadMessages() {
                vm.loadingGrpMessages = true;

                // as we do not know the size for search results pagination start from 0 in search mode
                if (vm.messageCriteria.searchQuery != "" && vm.messageCriteria.searchQuery != null) {
                    if (messageSearchText != null && vm.messageCriteria.searchQuery == messageSearchText && msgs.last != true) {
                        pageableMessages.page++;
                    }
                    else if (messageSearchText != null && vm.messageCriteria.searchQuery != messageSearchText) {
                        pageableMessages.page = 0;
                        vm.selectedGrp.messageList = [];
                        msgs = angular.copy(pagedResults);
                        count = 0;
                        messageSearchText = vm.messageCriteria.searchQuery;
                    }
                    else if (messageSearchText == null) {
                        messageSearchText = vm.messageCriteria.searchQuery;
                        pageableMessages.page = 0;
                        vm.selectedGrp.messageList = [];
                        msgs = angular.copy(pagedResults);
                        count = 0;
                    }
                }
                else {
                    messageSearchText = null;
                    if (vm.selectedGrp.messageList.length == 0) {
                        pageableMessages.page = Math.ceil(vm.selectedGrp.messagesCount / pageableMessages.size) - 1;
                        vm.selectedGrp.messageList = [];
                        count = 0;
                    }
                    else if (vm.selectedGrp.messageList.length > 0 && messageSearchMode) {
                        pageableMessages.page = Math.ceil(vm.selectedGrp.messagesCount / pageableMessages.size) - 1;
                        vm.selectedGrp.messageList = [];
                        msgs = pagedResults;
                        count = 0;
                    }
                    else if (vm.selectedGrp.messageList.length > 0 && !messageSearchMode) {
                        pageableMessages.page--;
                    }
                }

                if (vm.messageCriteria.searchQuery != "" && vm.messageCriteria.searchQuery != null) {
                    messageSearchMode = true;
                }
                else {
                    messageSearchMode = false;
                }
                if ((messageSearchMode && msgs.last != true && msgs.content.length > 0) || (!messageSearchMode && msgs.first != true && msgs.content.length > 0) || msgs.content.length == 0) {
                    MessageService.getAllMessage("PROJECT", $stateParams.projectId, vm.selectedGrp.id, pageableMessages, vm.messageCriteria).then(
                        function (data) {
                            vm.loadingGrpMessages = false;
                            count++;
                            msgs = data.groupMessages;
                            var messagesLength = vm.selectedGrp.messageList.length;
                            CommonService.getPersonReferences(data.listGroupMessages, 'createdBy');
                            for (var i = data.groupMessages.content.length - 1, j = 0; i >= 0; i--, j++) {
                                if (!messageSearchMode) {
                                    vm.selectedGrp.messageList.splice(0, 0, data.groupMessages.content[i]);
                                }
                                else {
                                    vm.selectedGrp.messageList.push(data.groupMessages.content[j]);
                                }
                                if (i == 0 && !messageSearchMode) {
                                    scrollToBottom = true;
                                    scroll();
                                }
                            }
                            if ((messagesLength + data.groupMessages.content.length) < 10 && messageSearchMode && msgs.last != true) {
                                count = 0;
                                loadMessages();
                            }
                            else {
                                if (data.groupMessages.content.length < 10 && !messageSearchMode && (pageableMessages.page == Math.ceil(vm.selectedGrp.messagesCount / pageableMessages.size) - 1)) {
                                    count = 0;
                                    loadMessages();
                                }
                            }
                        },
                        function (error) {
                            vm.loadingGrpMessages = false;
                        }
                    );
                }
                else {
                    vm.loadingGrpMessages = false;
                }
            }

            function uploadAttachment() {
                document.getElementById("file").onchange = function () {
                    var file = document.getElementById("file");
                    if (vm.importFile != undefined && vm.importFile.length > 0) {
                        angular.forEach(file.files, function (file) {
                            vm.importFile.push(file);
                        })
                    }
                    else {
                        vm.importFile = Array.from(file.files);
                    }
                    $scope.$apply();
                };
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function getVideo(fileId) {
                var src = "api/col/attachments/" + fileId + "/download";
                return $sce.trustAsResourceUrl(src);
            }

            function showImage(image) {
                if (image.extension == '.JPEG' || image.extension == '.jpeg' || image.extension == '.jpg' || image.extension == '.JPG' || image.extension == '.PNG' || image.extension == '.png') {
                    var modal = document.getElementById('myModal0');
                    var modalImg = document.getElementById('img10');

                    modal.style.display = "block";
                    modalImg.src = "api/col/attachments/" + image.id + "/download";

                    var span = document.getElementsByClassName("closeImage10")[0];

                    span.onclick = function () {
                        modal.style.display = "none";
                    }
                }
            }

            function removeAttachment(file) {
                document.getElementById("file").value = null;
                var index = vm.importFile.indexOf(file);
                vm.importFile.splice(index, 1);
            }

            function editGroup(group) {
                vm.groupEditMode = true;
                vm.selectedGrp = group;
                vm.selectedGrp.groupIconSrc = "api/col/messaging/groups/" + group.id + "/icon?" + new Date().getTime();
                loadEditImage();
            }

            function updateGroup() {
                MessageService.updateGroup(vm.selectedGrp, vm.selectedGrp.id).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Group updated successfully");
                        vm.groupEditMode = false;
                        if (vm.groupIcon != null) {
                            MessageService.uploadGroupIcon(data.id, vm.groupIcon.files[0]).then(
                                function (icon) {
                                    vm.selectedGrp.groupIconSrc = null;
                                    vm.selectedGrp.groupIconSrc = "api/col/messaging/groups/" + data.id + "/icon?" + new Date().getTime();
                                    vm.groupIcon = null;
                                },
                                function (error) {
                                    vm.selectedGrp = null;
                                    vm.groupIcon = null;
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        else {
                            vm.selectedGrp = null;
                        }
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            // scroll functionality
            module.directive("whenScrolled", function () {
                return {

                    restrict: 'A',
                    link: function (scope, elem, attrs) {

                        // we get a list of elements of size 1 and need the first element
                        var raw = elem[0];

                        // we load more elements when scrolled past a limit
                        elem.bind("scroll", function () {
                            if (!vm.groupEditMode && !vm.addGroup && !vm.loadingGroups && !vm.loadingGrpMessages) {
                                if (((raw.scrollTop + raw.offsetHeight + 5) >= raw.scrollHeight)) {
                                    if (raw.className == 'conversations' && vm.groups.length <= grps.totalElements && !vm.loadingGroups) {
                                        scope.$apply(attrs.whenScrolled);
                                    }
                                    else if (raw.className != 'conversations' && messageSearchMode) {
                                        scope.$apply(attrs.whenScrolled);
                                    }
                                }
                                else if (raw.scrollTop == 0 && msgs.first != undefined && msgs.first >= 0 && (vm.selectedGrp.messageList.length <= msgs.totalElements) && !vm.loadingGrpMessages && raw.className != 'conversations') {
                                    scope.loading = true;

                                    // we can give any function which loads more elements into the list
                                    scope.$apply(attrs.whenScrolled);
                                }
                            }
                        });
                    }
                }
            });

            function scroll() {
                if (scrollToBottom && count == 1) {
                    $timeout(function () {
                        var objDiv = document.getElementById("messages");
                        objDiv.scrollTop = objDiv.scrollHeight + 10;
                        scrollToBottom = false;
                    }, 200);
                }
                else if (msgs.last != true && msgs.first != true) {
                    var objDiv = document.getElementById("messages");
                    objDiv.scrollTop = 50;
                }
            }

            function loadCreateImage() {
                document.getElementById("groupIcon").onchange = function () {
                    var reader = new FileReader();
                    if (vm.addGroup) {
                        vm.groupIcon = document.getElementById("groupIcon");
                        reader.onload = function (e) {
                            $('#icon')
                                .attr('src', e.target.result)
                        };
                        reader.readAsDataURL(vm.groupIcon.files[0]);
                        $scope.$apply();
                    }

                };

            }

            function loadEditImage() {
                document.getElementById("groupEditIcon").onchange = function () {
                    var file = document.getElementById("groupEditIcon");
                    vm.groupIcon = file;
                    var reader = new FileReader();
                    if (vm.selectedGrp.groupIconSrc == undefined) {
                        reader.onload = function (e) {
                            $('#image1')
                                .attr('src', e.target.result)

                        };
                        vm.groupIcon = file;
                        reader.readAsDataURL(file.files[0]);
                        $scope.$apply();
                    }
                    else {
                        reader.onload = function (e) {
                            $('#image')
                                .attr('src', e.target.result)
                        };
                        vm.groupIcon = file;
                        reader.readAsDataURL(file.files[0]);
                    }
                }
            }

            (function () {
                $scope.$on('app.project.communication', function (event, data) {
                    if (data.tabId == 'communication.messaging') {
                        vm.groups = [];
                        grps = angular.copy(pagedResults);
                        loadGroups();
                    }
                });
            })();

        }
    }
)
;
