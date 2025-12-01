define(
    [
        'app/desktop/modules/shared/shared.module',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],

    function (module) {
        module.controller('NewCommentsController', NewCommentsController);

        function NewCommentsController($scope, $rootScope, $timeout, ItemService, $translate, $sce,
                                       CommonService, CommentsService, DialogService) {

            var vm = this;

            vm.data = $scope.data;
            vm.comments = [];

            var pageable = {
                page: 0,
                size: 25
            };

            vm.newComment = {
                objectType: vm.data.objectType,
                objectId: vm.data.objectId,
                comment: null
            };

            vm.showFilesDropZone = false;
            vm.placeholderText = "";
            vm.loading = true;

            var parsed = angular.element("<div></div>");
            $scope.deleteConversationTitle = parsed.html($translate.instant("DELETE_CONVERSATION")).html();
            $scope.deleteConversationMessage = parsed.html($translate.instant("P_C_DELETE_CONVERSATION")).html();
            $scope.conversationDeleted = parsed.html($translate.instant("CONVERSATION_DELETED")).html();


            function loadComments() {
                CommentsService.getRootComments(vm.data.objectType, vm.data.objectId, pageable).then(
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

                                                if (vm.data.updateCount === undefined || vm.data.updateCount) {
                                                    $rootScope.showComments(vm.newComment.objectType, vm.newComment.objectId, vm.comments.length);
                                                }
                                                if (vm.newComment.objectType == "PROJECT") {
                                                    $scope.callback(vm.comments.length);
                                                }

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
                                if (vm.data.updateCount === undefined || vm.data.updateCount) {
                                    $rootScope.showComments(vm.newComment.objectType, vm.newComment.objectId, vm.comments.length);
                                }
                                if (vm.newComment.objectType == "PROJECT") {
                                    $scope.callback(vm.comments.length);
                                }
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            vm.gotoCommentObjectDetails = gotoCommentObjectDetails;
            function gotoCommentObjectDetails(comment) {
                if (comment.objectType === 'ITEM') {
                    ItemService.getLatestRevision(comment.objectId).then(
                        function (data) {
                            $state.go('app.items.details', {itemId: data.id, tab: 'details.basic'});
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
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

                textarea.oninput = function () {
                    textarea.style.height = "";
                    textarea.style.height = Math.min(textarea.scrollHeight, limit) + "px";
                };
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
                                if (vm.data.updateCount === undefined || vm.data.updateCount) {
                                    $rootScope.showComments(vm.newComment.objectType, vm.newComment.objectId, vm.comments.length);
                                }
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

            (function () {
                $timeout(function () {
                    initTextArea();
                }, 2000);
                loadComments();
                loadFileSizePreference();
            })();
        }
    }
)
;