define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/desktop/directives/person-avatar/personAvatarDirective',
        'app/shared/services/core/supplierAuditService'
    ],
    function (module) {
        module.controller('SupplierPlanDetailsController', SupplierPlanDetailsController);

        function SupplierPlanDetailsController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, $translate, SupplierAuditService,
                                               CommonService, LoginService, PersonGroupService, DialogService, CommentsService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            $scope.noUserSelect = parsed.html($translate.instant("NO_USER_SELECT_MSG")).html();
            $scope.selectReviewerApproverMsg = parsed.html($translate.instant("SELECT_REVIEWER_APPROVER_MSG")).html();

            vm.logins = [];

            vm.searchUsers = '';
            vm.supplierAuditPlan = $scope.data.supplierAuditPlan;

            var mapUsers = new Hashtable();

            function loadReviewers() {
                SupplierAuditService.getPlanReviewersAndApprovers(vm.supplierAuditPlan.id).then(
                    function (data) {
                        vm.reviewers = data;
                        CommonService.getPersonReferences([vm.supplierAuditPlan], 'createdBy');
                        loadUsers();
                    }
                )
            }

            function loadSupplierAuditPlan() {
                SupplierAuditService.getSupplierAuditPlan(vm.supplierAuditPlan.supplierAudit, vm.supplierAuditPlan).then(
                    function (data) {
                        vm.supplierAuditPlan = data;
                        CommonService.getPersonReferences([vm.supplierAuditPlan], 'createdBy');
                    }
                )
            }

            function loadUsers() {
                $rootScope.showBusyIndicator();
                LoginService.getInternalActiveLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            mapUsers.put(login.person.id, login);
                        });
                        vm.logins = data;
                        var revLogins = updateReviewerRefs(vm.logins);
                        $timeout(function () {
                            initTextArea();
                        }, 2000);
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
                vm.approver = false;
                vm.reviewer = false;
                angular.forEach(vm.reviewers, function (reviewer) {
                    reviewer.editMode = false;
                    var login = mapUsers.get(reviewer.reviewer);
                    if (login != null) {
                        reviewer.login = login;
                        logins.splice(logins.indexOf(login), 1);
                        mapUsers.remove(reviewer.reviewer);
                        revLogins.push(login);
                    }
                    reviewer.showApproveButton = false;
                    reviewer.showReviewButton = false;
                    if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                        reviewer.showApproveButton = true;
                    } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                        reviewer.showReviewButton = true;
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
                reviewer.plan = vm.supplierAuditPlan.id;
                SupplierAuditService.addPlanReviewer(vm.supplierAuditPlan.id, reviewer).then(
                    function (data) {
                        data.login = login;
                        vm.reviewers.push(data);
                        angular.forEach(vm.reviewers, function (reviewer) {
                            reviewer.showApproveButton = false;
                            reviewer.showReviewButton = false;
                            if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                reviewer.showApproveButton = true;
                            } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                reviewer.showReviewButton = true;
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
                SupplierAuditService.updatePlanReviewer(vm.supplierAuditPlan.id, reviewer).then(
                    function (data) {
                        vm.approver = false;
                        vm.reviewer = false;
                        angular.forEach(vm.reviewers, function (reviewer) {
                            reviewer.showApproveButton = false;
                            reviewer.showReviewButton = false;
                            if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                reviewer.showApproveButton = true;
                            } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                reviewer.showReviewButton = true;
                            }
                        })
                        $rootScope.hideBusyIndicator();
                        $rootScope.loadPlan();
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
                SupplierAuditService.deletePlanReviewer(vm.supplierAuditPlan.id, reviewer).then(
                    function (data) {
                        vm.reviewers.splice(vm.reviewers.indexOf(reviewer), 1);
                        vm.logins.push(reviewer.login);
                        $rootScope.hideBusyIndicator();
                        $rootScope.loadPlan();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.comments = [];

            var pageable = {
                page: 0,
                size: 25
            };

            vm.newComment = {
                objectType: "SUPPLIERAUDITPLAN",
                objectId: vm.supplierAuditPlan.id,
                comment: null
            };

            vm.showFilesDropZone = false;
            vm.placeholderText = "";

            $scope.deleteConversationTitle = parsed.html($translate.instant("DELETE_CONVERSATION")).html();
            $scope.deleteConversationMessage = parsed.html($translate.instant("P_C_DELETE_CONVERSATION")).html();
            $scope.conversationDeleted = parsed.html($translate.instant("CONVERSATION_DELETED")).html();


            function loadComments() {
                $rootScope.showBusyIndicator();
                CommentsService.getRootComments("SUPPLIERAUDITPLAN", vm.supplierAuditPlan.id, pageable).then(
                    function (data) {
                        vm.comments = data.content;
                        angular.forEach(vm.comments, function (comment) {
                            comment.time = moment(comment.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                            comment.comment = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        });

                        if (vm.comments.length == 0) {
                            vm.placeholderText = "Start a conversation";
                        }
                        else {
                            vm.placeholderText = "Add a message";
                        }
                        loadReviewers();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function validateComment() {
                var valid = true;

                if (vm.commentFiles.length == 0 && (vm.newComment.comment == null || vm.newComment.comment == "" || vm.newComment.comment == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter comment");

                }

                return valid;
            }

            vm.createComment = createComment;
            function createComment() {
                if (validateComment()) {
                    if (vm.commentFiles.length > 0 && vm.newComment.comment == null) {
                        vm.newComment.comment = "";
                    }
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    CommentsService.createComment(vm.newComment).then(
                        function (comment) {
                            comment.time = moment(comment.commentedDate, 'DD/MM/YYYY, HH:mm').fromNow();
                            comment.comment = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            if (vm.commentFiles.length > 0) {
                                $(".progress").show();
                                CommentsService.uploadCommentFiles(comment.id, vm.commentFiles).then(
                                    function (data) {
                                        CommentsService.getComment(comment.id).then(
                                            function (data) {
                                                vm.comments.unshift(data);
                                                vm.newComment.comment = null;
                                                toggleCommentFiles();
                                                $("#commentsBody").animate({scrollTop: 0}, "fast");

                                                var textarea = document.getElementById("messageText");
                                                textarea.style.height = "";
                                                textarea.style.height = "34px";
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
                                $("#commentsBody").animate({scrollTop: 0}, "fast");

                                var textarea = document.getElementById("messageText");
                                textarea.style.height = "";
                                textarea.style.height = "34px";
                                $rootScope.hideBusyIndicator();
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.selectedCommentImages = [];
            vm.selectedImage = null;
            vm.showImages = showImages;
            function showImages(comment, image) {
                vm.selectedCommentImages = [];
                vm.selectedImage = image;
                Array.prototype.push.apply(vm.selectedCommentImages, comment.images);
                angular.forEach(vm.selectedCommentImages, function (commentImage) {
                    if (image.id === commentImage.id) {
                        commentImage.showImage = true;
                    } else {
                        commentImage.showImage = false;
                    }
                });
                var modal = document.getElementById("comment-image-previewer-sidepanel");
                modal.style.display = "block";

                $timeout(function () {
                    $("#commentImage" + vm.selectedImage.id).width($('.comment-image-previewer-sidepanel .image-view').outerWidth());
                    $("#commentImage" + vm.selectedImage.id).height($('.comment-image-previewer-sidepanel .image-view').outerHeight());
                });
            }

            vm.showPreviousImage = showPreviousImage;
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
                    $("#commentImage" + vm.selectedImage.id).width($('.comment-image-previewer-sidepanel .image-view').outerWidth());
                    $("#commentImage" + vm.selectedImage.id).height($('.comment-image-previewer-sidepanel .image-view').outerHeight());
                });
            }

            vm.showNextImage = showNextImage;
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
                    $("#commentImage" + vm.selectedImage.id).width($('.comment-image-previewer-sidepanel .image-view').outerWidth());
                    $("#commentImage" + vm.selectedImage.id).height($('.comment-image-previewer-sidepanel .image-view').outerHeight());
                });
            }

            vm.hideImagesView = hideImagesView;
            function hideImagesView() {
                var modal = document.getElementById("comment-image-previewer-sidepanel");
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
                    maxFilesize: vm.fileConfig.fileSize,
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
                });
            }

            vm.selectFiles = selectFiles;
            function selectFiles() {
                $('#commentFiles')[0].click();
            }

            function loadFileSizePreference() {
                vm.fileConfig = {};
                vm.fileConfig.fileTypeError = false;
                vm.fileConfig.fileSizeError = false;
                var context = 'APPLICATION';
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        if (data.length != 0) {
                            vm.configs = data;
                            angular.forEach(vm.configs, function (config) {
                                if (config.preferenceKey == "APPLICATION.FILESIZE") {
                                    vm.fileConfig.fileSize = config.integerValue;
                                }
                            });
                        } else {
                            vm.fileConfig.fileSize = 2000;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            }

            vm.downloadFile = downloadFile;
            function downloadFile(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                window.open(url);
            }

            function initTextArea() {
                var textarea = document.getElementById("messageText");
                var limit = 100; //height limit
                if (textarea != null) {
                    textarea.oninput = function () {
                        textarea.style.height = "";
                        textarea.style.height = Math.min(textarea.scrollHeight, limit) + "px";
                    };
                }
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

            vm.showSubmitDialog = showSubmitDialog;
            function showSubmitDialog(type, reviewer) {
                vm.approveType = type;
                vm.selectedReviewer = reviewer;
                vm.error = "";
                $timeout(function () {
                    $('#plan-approve-modal').css("display", "block");
                }, 500);
            }

            vm.hideSubmitDialog = hideSubmitDialog;
            function hideSubmitDialog() {
                $('#plan-approve-modal').css("display", "none");
                vm.selectedReviewer.notes = null;
                vm.selectedReviewer = null;
                vm.error = "";
            }

            var enterApproveComment = parsed.html($translate.instant("PLEASE_ENTER_APPROVE_COMMENT")).html();
            var enterRejectComment = parsed.html($translate.instant("PLEASE_ENTER_REJECT_COMMENT")).html();
            var enterReviewComment = parsed.html($translate.instant("PLEASE_ENTER_REVIEW_COMMENT")).html();
            var documentApprovedMsg = parsed.html($translate.instant("PLAN_APPROVED_MSG")).html();
            var documentRejectedMsg = parsed.html($translate.instant("PLAN_REJECTED_MSG")).html();
            var documentReviewedMsg = parsed.html($translate.instant("PLAN_REVIEWED_MSG")).html();

            vm.submitReview = submitReview;
            function submitReview() {
                var successMsg;
                if (vm.selectedReviewer.notes == null || vm.selectedReviewer.notes == '') {
                    if (vm.approveType == "Approve") {
                        vm.error = enterApproveComment;
                    } else if (vm.approveType == "Reject") {
                        vm.error = enterRejectComment;
                    } else {
                        vm.error = enterReviewComment;
                    }
                } else {
                    if (vm.approveType == "Approve") {
                        successMsg = documentApprovedMsg;
                        vm.selectedReviewer.status = "APPROVED";
                    } else if (vm.approveType == "Reject") {
                        successMsg = documentRejectedMsg;
                        vm.selectedReviewer.status = "REJECTED";
                    } else {
                        successMsg = documentReviewedMsg;
                        vm.selectedReviewer.status = "REVIEWED";
                    }
                    SupplierAuditService.submitPlanReview(vm.supplierAuditPlan.id, vm.selectedReviewer).then(
                        function (data) {
                            loadSupplierAuditPlan();
                            loadReviewers();
                            $rootScope.loadPlan();
                            vm.hideSubmitDialog();
                            vm.approver = false;
                            vm.reviewer = false;
                            angular.forEach(vm.reviewers, function (reviewer) {
                                reviewer.showApproveButton = false;
                                reviewer.showReviewButton = false;
                                if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                    reviewer.showApproveButton = true;
                                } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                    reviewer.showApproveButton = true;
                                }
                            });
                            $rootScope.loadSupplierAudit();
                            $rootScope.showSuccessMessage(successMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                loadComments();
                loadFileSizePreference();
            })();
        }
    }
)
;