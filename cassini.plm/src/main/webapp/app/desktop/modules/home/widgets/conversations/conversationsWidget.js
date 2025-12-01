define(
    [
        'app/desktop/modules/home/home.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/userTasksService'
    ],
    function (module) {
        module.controller('ConversationsController', ConversationsController);

        function ConversationsController($scope, $rootScope, $sce, $translate, $state, $timeout, $window,
                                         CommonService, CommentsService, ItemService, DialogService, UserTasksService) {
            var vm = this;

            var pageable = {
                page: 0,
                size: 25,
                sort: {
                    field: "commentedDate",
                    order: "DESC"
                }
            };

            vm.comments = [];
            vm.pagenatedResults = {
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

            vm.newComment = {
                objectType: null,
                objectId: null,
                comment: null
            };

            vm.showFilesDropZone = false;
            vm.searchQuery = '';
            vm.searchMode = false;

            vm.loading = true;
            var parsed = angular.element("<div></div>");
            $scope.startConversation = parsed.html($translate.instant("START_CONVERSATION")).html();
            $scope.search = parsed.html($translate.instant("ALL_VIEW_SEARCH")).html();
            $scope.conversationMessage = parsed.html($translate.instant("START_CONVERSATION_MESSAGE")).html();
            $scope.clearSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            $scope.deleteConversationTitle = parsed.html($translate.instant("DELETE_CONVERSATION")).html();
            $scope.editConversationTitle = parsed.html($translate.instant("EDIT_CONVERSATION")).html();
            $scope.updateConversationTitle = parsed.html($translate.instant("UPDATE_CONVERSATION")).html();
            $scope.cancelChangesTitle = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            $scope.deleteConversationMessage = parsed.html($translate.instant("P_C_DELETE_CONVERSATION")).html();
            $scope.conversationDeleted = parsed.html($translate.instant("CONVERSATION_DELETED")).html();
            var pleaseEnterMsg = parsed.html($translate.instant("PLEASE_ENTER_MESSAGE")).html();
            vm.loadComments = loadComments;
            vm.commentIds = [];
            function loadComments(flag) {
                vm.loading = true;
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                UserTasksService.getAllComments(null, null, pageable).then(
                    function (data) {
                        vm.pagenatedResults = data;
                        vm.commentIds = [];
                        angular.forEach(data.content, function (comment) {
                            comment.editMode = false;
                            vm.commentIds.push(comment.id);
                            comment.time = moment(comment.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                            comment.commentHtml = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        });


                        if (flag) {
                            Array.prototype.push.apply(vm.comments, data.content);
                        }
                        else {
                            vm.comments = data.content;
                        }
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        vm.loading = true;
                        $rootScope.hideBusyIndicator();
                    }
                );

            }

            vm.showComments = showComments;
            function showComments(objectType, objectId) {
                var parsed = angular.element('<div></div>');
                var commentsTitle = parsed.html($translate.instant("CONVERSATION")).html();
                var options = {
                    title: commentsTitle,
                    template: 'app/desktop/modules/shared/comments/newCommentsView.jsp',
                    controller: 'NewCommentsController as commentsVm',
                    resolve: 'app/desktop/modules/shared/comments/newCommentsController',
                    width: 600,
                    showMask: true,
                    data: {
                        objectType: objectType,
                        objectId: objectId
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.createComment = createComment;
            function createComment() {
                if (vm.commentFiles.length > 0 && vm.newComment.comment == null) {
                    vm.newComment.comment = "";
                }
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                CommentsService.createComment(vm.newComment).then(
                    function (comment) {
                        comment.time = moment(comment.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                        comment.commentHtml = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        if (vm.commentFiles.length > 0) {
                            $(".progress").show();
                            CommentsService.uploadCommentFiles(comment.id, vm.commentFiles).then(
                                function (data) {
                                    CommentsService.getComment(comment.id).then(
                                        function (data) {
                                            vm.comments.unshift(data);
                                            vm.newComment.comment = null;
                                            vm.pagenatedResults.totalElements = vm.pagenatedResults.totalElements + 1;
                                            toggleCommentFiles();
                                            $("#conversationsBody").animate({scrollTop: 0}, "fast");

                                            var textarea = document.getElementById("messageText");
                                            textarea.style.height = "";
                                            textarea.style.height = "34px";
                                            $scope.callback();
                                            $rootScope.hideBusyIndicator();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    );
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        else {
                            vm.comments.unshift(comment);
                            vm.newComment.comment = null;
                            vm.pagenatedResults.totalElements = vm.pagenatedResults.totalElements + 1;
                            $("#conversationsBody").animate({scrollTop: 0}, "fast");

                            var textarea = document.getElementById("messageText");
                            textarea.style.height = "";
                            textarea.style.height = "34px";
                        }
                        $scope.callback();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            vm.gotoCommentObjectDetails = gotoCommentObjectDetails;
            function gotoCommentObjectDetails(comment) {
                if (comment.objectType === 'ITEM') {
                    $state.go('app.items.details', {itemId: comment.parentObjectId, tab: 'details.basic'});
                } else if (comment.objectType == "CHANGE") {
                    if (comment.type == "ECO") {
                        $state.go('app.changes.eco.details', {ecoId: comment.objectId, tab: 'details.basic'});
                    } else if (comment.type == "ECR") {
                        $state.go('app.changes.ecr.details', {ecrId: comment.objectId, tab: 'details.basic'});
                    } else if (comment.type == "DCO") {
                        $state.go('app.changes.dco.details', {dcoId: comment.objectId, tab: 'details.basic'});
                    } else if (comment.type == "DCR") {
                        $state.go('app.changes.dcr.details', {dcrId: comment.objectId, tab: 'details.basic'});
                    } else if (comment.type == "MCO") {
                        $state.go('app.changes.mco.details', {mcoId: comment.objectId, tab: 'details.basic'});
                    } else if (comment.type == "DEVIATION" || comment.type == "WAIVER") {
                        $state.go('app.changes.variance.details', {varianceId: comment.objectId, tab: 'details.basic'});
                    }
                }
            }

            vm.selectedCommentImages = [];
            vm.selectedImage = null;
            vm.showImages = showImages;
            function showImages(comment, image) {
                vm.selectedCommentImages = [];
                vm.selectedImage = image;
                vm.selectedCommentImages = comment.images;
                angular.forEach(vm.selectedCommentImages, function (commentImage) {
                    if (image.id === commentImage.id) {
                        commentImage.showImage = true;
                    } else {
                        commentImage.showImage = false;
                    }
                });
                var modal = document.getElementById("comment-image-previewer");
                modal.style.display = "block";

                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                });
            }

            $scope.showPreviousImage = showPreviousImage;
            function showPreviousImage(image) {
                var index = vm.selectedCommentImages.indexOf(image);
                vm.selectedImage = null;
                if (index === 0) {
                    image.showImage = false;
                    vm.selectedCommentImages[vm.selectedCommentImages.length - 1].showImage = true;
                    vm.selectedImage = vm.selectedCommentImages[vm.selectedCommentImages.length - 1];
                } else {
                    image.showImage = false;
                    vm.selectedCommentImages[index - 1].showImage = true;
                    vm.selectedImage = vm.selectedCommentImages[index - 1];
                }
                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                });
            }

            $scope.showNextImage = showNextImage;
            function showNextImage(image) {
                var index = vm.selectedCommentImages.indexOf(image);
                vm.selectedImage = null;
                if (index === 0 || index < (vm.selectedCommentImages.length - 1)) {
                    image.showImage = false;
                    vm.selectedCommentImages[index + 1].showImage = true;
                    vm.selectedImage = vm.selectedCommentImages[index + 1];
                } else {
                    image.showImage = false;
                    vm.selectedCommentImages[0].showImage = true;
                    vm.selectedImage = vm.selectedCommentImages[0];
                }

                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                });
            }

            $scope.hideImagesView = hideImagesView;
            function hideImagesView() {
                var modal = document.getElementById("comment-image-previewer");
                modal.style.display = "none";
            }

            var dropZoneComponent = null;
            vm.commentFiles = [];
            vm.toggleCommentFiles = toggleCommentFiles;
            function toggleCommentFiles() {
                vm.showFilesDropZone = !vm.showFilesDropZone;
                if (vm.showFilesDropZone) {
                    $timeout(function () {
                        initDropZone();
                    }, 1000);
                }
                else {
                    if (dropZoneComponent != null) {
                        vm.commentFiles = [];
                        dropZoneComponent.destroy();
                    }
                }
            }

            var editDropZoneComponent = null;
            vm.toggleEditCommentFiles = toggleEditCommentFiles;
            function toggleEditCommentFiles(comment) {
                vm.showEditFilesDropZone = !vm.showEditFilesDropZone;
                if (vm.showEditFilesDropZone) {
                    $timeout(function () {
                        initEditDropZone(comment);
                    }, 1000);
                }
                else {
                    if (editDropZoneComponent != null) {
                        comment.editCommentFiles = [];
                        editDropZoneComponent.destroy();
                    }
                }
            }

            function initDropZone() {
                //Dropzone.options.url
                var previewNode = document.getElementById("fileUploadTemplate");
                previewNode.id = "";
                previewNode.style.display = "block";
                var previewTemplate = previewNode.parentNode.innerHTML;
                previewNode.parentNode.removeChild(previewNode);

                dropZoneComponent = new Dropzone(document.getElementById("commentFiles"), {
                    url: "/target", // Set the url
                    thumbnailWidth: 50,
                    thumbnailHeight: 50,
                    parallelUploads: 20,
                    autoProcessQueue: false, // Make sure the files aren't queued until manually processed
                    previewTemplate: previewTemplate,
                    previewsContainer: "#fileUploadPreviews",
                    success: function (file, response) {
                    },
                    error: function (file, response) {
                    }
                });

                dropZoneComponent.on("queuecomplete", function (progress) {
                });

                dropZoneComponent.on("addedfiles", function (files) {
                    $(".drop-files-label").hide();
                    vm.commentFiles = dropZoneComponent.files;
                    $scope.$evalAsync();
                });
            }

            $scope.selectFiles = selectFiles;
            function selectFiles() {
                $('#commentFiles')[0].click();
            }

            vm.downloadFile = downloadFile;
            function downloadFile(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                window.open(url);
            }

            vm.clearSearch = clearSearch;
            function clearSearch() {
                pageable.page = 0;
                vm.searchMode = false;
                loadComments(false);
            }

            vm.searchComments = searchComments;
            function searchComments() {
                pageable.page = 0;
                vm.searchMode = true;
                vm.loading = true;
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                if (vm.searchQuery.trim().length > 0) {
                    performSearch(false);
                }
                else {
                    loadComments(false);
                }
            }

            function performSearch(flag) {
                UserTasksService.searchComments(vm.searchQuery, pageable).then(
                    function (data) {
                        vm.pagenatedResults = data;
                        angular.forEach(data.content, function (comment) {
                            comment.editMode = false;
                            comment.time = moment(comment.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                            comment.commentHtml = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        });


                        if (flag) {
                            Array.prototype.push.apply(vm.comments, data.content);
                        }
                        else {
                            vm.comments = data.content;
                        }

                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.loadMore = loadMore;
            function loadMore() {
                pageable.page = pageable.page + 1;
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                if (!vm.searchMode) {
                    loadComments(true);
                }
                else {
                    performSearch(true);
                }
            }

            function initTextArea() {
                var textarea = document.getElementById("messageText");
                var limit = 100; //height limit

                textarea.oninput = function () {
                    textarea.style.height = "";
                    textarea.style.height = Math.min(textarea.scrollHeight, limit) + "px";
                };
            }

            function closeSidePanel() {
                $rootScope.hideSidePanel();
            }

            $scope.deleteConversation = deleteConversation;
            function deleteConversation(comment) {
                var options = {
                    title: $scope.deleteConversationTitle,
                    message: $scope.deleteConversationMessage,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        CommentsService.deleteComment(comment.id).then(
                            function (data) {
                                vm.comments.splice(vm.comments.indexOf(comment), 1);
                                vm.pagenatedResults.totalElements = vm.pagenatedResults.totalElements - 1;
                                loadConversationsCount();
                                $scope.callback();
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage($scope.conversationDeleted);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            $scope.editConversation = editConversation;
            function editConversation(comment) {
                comment.editMode = true;
                comment.oldComment = comment.comment;
                /*$timeout(function () {
                 initEditDropZone(comment);
                 }, 1000);*/
            }

            function initEditDropZone(comment) {
                //Dropzone.options.url
                var previewNode = document.getElementById(comment.id + "fileUploadTemplate");
                previewNode.id = "";
                previewNode.style.display = "block";
                var previewTemplate = previewNode.parentNode.innerHTML;
                previewNode.parentNode.removeChild(previewNode);

                editDropZoneComponent = new Dropzone(document.getElementById(comment.id + "commentFiles"), {
                    url: "/target", // Set the url
                    thumbnailWidth: 50,
                    thumbnailHeight: 50,
                    parallelUploads: 20,
                    autoProcessQueue: false, // Make sure the files aren't queued until manually processed
                    previewTemplate: previewTemplate,
                    previewsContainer: "#" + comment.id + "fileUploadPreviews",
                    success: function (file, response) {
                    },
                    error: function (file, response) {
                    }
                });

                editDropZoneComponent.on("queuecomplete", function (progress) {
                });

                editDropZoneComponent.on("addedfiles", function (files) {
                    $(".drop-edit-files-label").hide();
                    comment.commentFiles = editDropZoneComponent.files;
                    $scope.$evalAsync();
                });
            }

            $scope.selectEditFiles = selectEditFiles;
            function selectEditFiles(comment) {
                $('#' + comment.id + 'commentFiles')[0].click();
            }

            $scope.updateComment = updateComment;
            function updateComment(comment) {
                if (comment.comment != null && comment.comment != "" && comment.comment != undefined) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var images = comment.images;
                    var attachments = comment.attachments;
                    var object = comment.object;
                    comment.images = [];
                    comment.attachments = [];
                    comment.object = null;
                    CommentsService.updateComment(comment).then(
                        function (data) {
                            if (vm.commentFiles.length > 0) {
                                $(".progress").show();
                                CommentsService.uploadCommentFiles(comment.id, vm.commentFiles).then(
                                    function (data) {
                                        CommentsService.getComment(comment.id).then(
                                            function (data) {
                                                comment = data;
                                                comment.editMode = false;
                                                comment.object = object;
                                                comment.time = moment(comment.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                                                comment.commentHtml = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                                $rootScope.showSuccessMessage("Comment updated successfully");
                                                $rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        );
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                );
                            } else {
                                comment.images = images;
                                comment.attachments = attachments;
                                comment.editMode = false;
                                comment.object = object;
                                comment.time = moment(data.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                                comment.commentHtml = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                $rootScope.showSuccessMessage("Comment updated successfully");
                                $rootScope.hideBusyIndicator();
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage(pleaseEnterMsg);
                }
            }

            $scope.cancelChanges = cancelChanges;
            function cancelChanges(comment) {
                comment.editMode = false;
                comment.comment = comment.oldComment;
            }

            vm.updateUserReadComment = updateUserReadComment;
            function updateUserReadComment(comment) {
                if (!comment.read) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    CommentsService.updateUserReadComment(comment.id, $rootScope.loginPersonDetails.person.id).then(
                        function (data) {
                            comment.read = true;
                            $scope.callback();
                            loadConversationsCount();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadConversationsCount() {
                UserTasksService.getConversationCountByPerson($rootScope.loginPersonDetails.person.id).then(
                    function (data) {
                        vm.unreadMessagesCount = data;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.unreadMessages = false;
            vm.showAllUnreadMessages = showAllUnreadMessages;
            function showAllUnreadMessages() {
                UserTasksService.getUnreadMessagesByPerson($rootScope.loginPersonDetails.person.id, pageable).then(
                    function (data) {
                        vm.unreadMessages = true;
                        vm.pagenatedResults = data;
                        vm.commentIds = [];
                        angular.forEach(data.content, function (comment) {
                            comment.editMode = false;
                            vm.commentIds.push(comment.id);
                            comment.time = moment(comment.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                            comment.commentHtml = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        });
                        loadConversationsCount();
                        vm.comments = data.content;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateConversationAsRead = updateConversationAsRead;
            function updateConversationAsRead() {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                CommentsService.updateUnReadCommentsByPerson($rootScope.loginPersonDetails.person.id).then(
                    function (data) {
                        loadComments(false);
                        loadConversationsCount();
                        $scope.callback();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.loadFileTab = loadFileTab;
            function loadFileTab(comment) {
                if (comment.type == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: comment.parentObjectId, tab: 'details.files'});
                } else if (comment.type == "ECO") {
                    $state.go('app.changes.eco.details', {ecoId: comment.parentObjectId, tab: 'details.files'});
                } else if (comment.type == "ECR") {
                    $state.go('app.changes.ecr.details', {ecrId: comment.parentObjectId, tab: 'details.files'});
                } else if (comment.type == "DCO") {
                    $state.go('app.changes.dco.details', {dcoId: comment.parentObjectId, tab: 'details.files'});
                } else if (comment.type == "DCR") {
                    $state.go('app.changes.dcr.details', {dcrId: comment.parentObjectId, tab: 'details.files'});
                } else if (comment.type == "MCO") {
                    $state.go('app.changes.mco.details', {mcoId: comment.parentObjectId, tab: 'details.files'});
                } else if (comment.type == "DEVIATION" || comment.type == "WAIVER") {
                    $state.go('app.changes.variance.details', {
                        varianceId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'PROBLEMREPORT') {
                    $state.go('app.pqm.pr.details', {problemReportId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'INSPECTIONPLAN') {
                    $state.go('app.pqm.inspectionPlan.details', {planId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'ITEMINSPECTION') {
                    $state.go('app.pqm.inspection.details', {
                        inspectionId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MATERIALINSPECTION') {
                    $state.go('app.pqm.inspection.details', {
                        inspectionId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: comment.parentId,
                        manufacturePartId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MFRPARTINSPECTIONREPORT') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: comment.parentId,
                        manufacturePartId: comment.parentObjectId,
                        tab: 'details.inspectionReports'
                    });
                }
                else if (comment.type == 'PGCSPECIFICATION') {
                    $state.go('app.compliance.specification.details', {
                        specificationId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'PLANT') {
                    $state.go('app.mes.masterData.plant.details', {
                        plantId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'ASSEMBLYLINE') {
                    $state.go('app.mes.masterData.assemblyline.details', {
                        assemblyLineId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'WORKCENTER') {
                    $state.go('app.mes.masterData.workcenter.details', {
                        workcenterId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MACHINE') {
                    $state.go('app.mes.masterData.machine.details', {
                        machineId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'INSTRUMENT') {
                    $state.go('app.mes.masterData.instrument.details', {
                        instrumentId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'TOOL') {
                    $state.go('app.mes.masterData.tool.details', {
                        toolId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'JIGFIXTURE') {
                    $state.go('app.mes.masterData.jigsAndFixtures.details', {
                        jigsFixId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MATERIAL') {
                    $state.go('app.mes.masterData.material.details', {
                        materialId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MANPOWER') {
                    $state.go('app.mes.masterData.manpower.details', {
                        manpowerId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'SHIFT') {
                    $state.go('app.mes.masterData.shift.details', {
                        shiftId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'OPERATION') {
                    $state.go('app.mes.masterData.operation.details', {
                        operationId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'REQUIREMENTDOCUMENT') {
                    $state.go('app.req.document.details', {reqId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'REQUIREMENT') {
                    $state.go('app.req.requirements.details', {
                        requirementId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'PROGRAM') {
                    $state.go('app.pm.program.details', {programId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'PROJECTACTIVITY') {
                    $state.go('app.pm.project.activity.details', {
                        activityId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'PROJECTTASK') {
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: comment.parentId,
                        taskId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'SUPPLIER') {
                    $state.go('app.mfr.supplier.details', {supplierId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'BOP') {
                    $state.go('app.mes.bop.details', {bopId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'PGCSUBSTANCE') {
                    $state.go('app.compliance.substance.details', {
                        substanceId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'PGCDECLARATION') {
                    $state.go('app.compliance.declaration.details', {
                        declarationId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MROWORKREQUEST') {
                    $state.go('app.mro.workRequest.details', {
                        workRequestId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'MROMAINTENANCEPLAN') {
                    $state.go('app.mro.maintenancePlan.details', {
                        maintenancePlanId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                } else if (comment.type == 'MBOM') {
                    $state.go('app.mes.mbom.details', {
                        mbomId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                } else if (comment.type == 'MROSPAREPART') {
                    $state.go('app.mro.sparePart.details', {sparePartId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'MROMETER') {
                    $state.go('app.mro.meter.details', {meterId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'MROASSET') {
                    $state.go('app.mro.asset.details', {assetId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'MROWORKORDER') {
                    $state.go('app.mro.workOrder.details', {workOrderId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'REQUIREMENTDOCUMENTREVISION') {
                    $state.go('app.req.document.details', {reqId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'REQUIREMENTDOCUMENTTEMPLATE') {
                    $state.go('app.req.document.template.details', {
                        reqDocId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'REQUIREMENTTEMPLATE') {
                    $state.go('app.req.template.details', {
                        requirementId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'CUSTOMER') {
                    $state.go('app.customers.details', {customerId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'ITEMMCO') {
                    $state.go('app.changes.mco.details', {mcoId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'OEMPARTMCO') {
                    $state.go('app.changes.mco.details', {mcoId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'CUSTOMOBJECT') {
                    $state.go('app.customobjects.details', {customId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'PLMNPR') {
                    $state.go('app.nprs.details', {nprId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'SUPPLIERAUDIT') {
                    $state.go('app.pqm.supplierAudit.details', {
                        supplierAuditId: comment.parentObjectId,
                        tab: 'details.files'
                    });
                }
                else if (comment.type == 'PPAP') {
                    $state.go('app.pqm.ppap.details', {ppapId: comment.parentObjectId, tab: 'details.checklist'});
                }
                else if (comment.type == 'CUSTOMOBJECT') {
                    $state.go('app.customobjects.details', {customId: comment.parentObjectId, tab: 'details.files'});
                }
                else if (comment.type == 'DOCUMENT') {
                    $window.localStorage.setItem("document-folder", comment.parentObjectId);
                    $state.go('app.dm.all');
                }

                else if (comment.type == 'BOPREVISION') {
                    $state.go('app.mes.bop.details', {bopId: comment.parentObjectId, tab: 'details.files'});
                }

                else if (comment.type == 'BOPROUTEOPERATION') {
                    $state.go('app.mes.bop.planDetails', {
                        bopId: comment.parentId,
                        bopPlanId: comment.parentObjectId,
                        tab: 'details.files'
                    })
                }
            }

            (function () {
                $timeout(function () {
                    initTextArea();
                }, 2000);
                loadConversationsCount();
                loadComments(false);
                $rootScope.$on('app.home.conversations', closeSidePanel);
            })();
        }

        module.directive('conversationsWidget', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/desktop/modules/home/widgets/conversations/conversationsWidget.jsp',
                controller: ConversationsController,
                controllerAs: 'conversationsVm',
                bindToController: true
            }
        });
    }
);