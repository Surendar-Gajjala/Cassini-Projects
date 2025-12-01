define(
    [
        'app/desktop/modules/shared/shared.module',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],

    function (module) {
        module.controller('CommentsController', CommentsController);

        function CommentsController($scope, $rootScope, $timeout, CommentsService, ItemService, $translate, $sce) {

            var vm = this;
            var parse = angular.element("<div></div>");

            vm.data = $scope.data;

            vm.comments = [];
            vm.emptyComment = {
                objectType: vm.data.objectType,
                objectId: vm.data.objectId,
                comment: "",
                commentFiles: [],
                imageFiles: [],
                attachmentFiles: []
            };

            vm.newComment = angular.copy(vm.emptyComment);

            vm.emptyImage = {
                attachmentType: null,
                extension: null,
                fileValue: null
            };

            vm.createComment = createComment;
            vm.onKeyUp = onKeyUp;

            vm.userMap = new Hashtable();

            function getRandomColor() {
                var letters = '0123456789ABCDEF';
                var color = '#';
                for (var i = 0; i < 6; i++) {
                    color += letters[Math.floor(Math.random() * 16)];
                }
                return color;
            }

            function randDarkColor() {
                var lum = -0.25;
                var hex = String('#' + Math.random().toString(16).slice(2, 8).toUpperCase()).replace(/[^0-9a-f]/gi, '');
                if (hex.length < 6) {
                    hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
                }
                var rgb = "#",
                    c, i;
                for (i = 0; i < 3; i++) {
                    c = parseInt(hex.substr(i * 2, 2), 16);
                    c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
                    rgb += ("00" + c).substr(c.length);
                }
                return rgb;
            }

            function loadComments() {
                var pageable = {
                    page: 0,
                    size: 1000
                };
                CommentsService.getRootComments(vm.data.objectType, vm.data.objectId, pageable).then(
                    function (data) {
                        vm.comments = data.content;
                        angular.forEach(vm.comments, function (comment) {
                            comment.comment = $sce.trustAsHtml(comment.comment.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            comment.imageFiles = [[]];
                            var rows = Math.ceil(comment.images.length / 4);
                            var count = -1;
                            for (var i = 0; i < rows; i++) {
                                comment.imageFiles[i] = [];

                                for (var j = 0; j < 4; j++) {
                                    count++;
                                    if (comment.images[count] !== null && comment.images[count] !== undefined) {
                                        comment.imageFiles[i][j] = comment.images[count];
                                    }
                                }
                            }

                            comment.videoFiles = [[]];
                            var videoRows = Math.ceil(comment.videos.length / 2);
                            var videoCount = -1;
                            for (var i = 0; i < videoRows; i++) {
                                comment.videoFiles[i] = [];

                                for (var j = 0; j < 2; j++) {
                                    videoCount++;
                                    if (comment.videos[videoCount] !== null && comment.videos[videoCount] !== undefined) {
                                        if (comment.videos[videoCount].mediaType == 'VIDEO') {
                                            var url = "api/col/comments/image/" + comment.videos[videoCount].id;
                                            comment.videos[videoCount].url = $sce.trustAsResourceUrl(url);
                                            URL.revokeObjectURL(comment.videos[videoCount].url);
                                        }
                                        comment.videoFiles[i][j] = comment.videos[videoCount];
                                    }
                                }
                            }
                        });
                        $timeout(function () {
                            loadImages();
                        }, 2000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var commentValidation = parse.html($translate.instant("COMMENT_VALIDATION")).html();

            function createComment() {
                if (validateComment()) {
                    CommentsService.createComment(vm.newComment).then(
                        function (data) {
                            vm.comments.unshift(data);
                            vm.newComment = angular.copy(vm.emptyComment);
                            $rootScope.$broadcast('comment.added');
                            $timeout(function () {
                                onKeyUp();
                            }, 300);
                            loadCommentCount();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );

                } else {
                    $rootScope.showErrorMessage(commentValidation);
                }
            }

            function loadCommentCount() {
                CommentsService.getAllCommentsCount(vm.data.objectType, vm.data.objectId).then(
                    function (data) {
                        $rootScope.showComments(vm.data.objectType, vm.data.objectId, data);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function validateComment() {
                var valid = true;

                $scope.commentError = false;
                if (vm.newComment.comment == null || vm.newComment.comment.trim() == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Comment");
                }
                return valid;
            }

            vm.imageComments = [];
            function loadImages() {
                document.getElementById("images").onchange = function () {
                    var file = document.getElementById("images");


                    if (file.files.length > 0) {
                        vm.newComment.imageFiles = [];
                        vm.newComment.attachmentFiles = [];
                        vm.newComment.commentFiles = [];
                        var fileCount = 0;
                        angular.forEach(file.files, function (attachment) {
                            var type = attachment.type.split("/");
                            if (type[0] == "image"/* || type[0] == "video"*/) {
                                var empty = angular.copy(vm.emptyImage);
                                var reader = new FileReader();
                                reader.readAsDataURL(attachment);
                                reader.onload = function () {
                                    empty.fileValue = reader.result;
                                    empty.attachmentType = type[0];
                                    empty.extension = type[1];
                                    vm.newComment.imageFiles.push(empty);
                                    fileCount++;
                                    if (fileCount == file.files.length) {
                                        vm.newComment.photos = [[]];
                                        var rows = Math.ceil(vm.newComment.imageFiles.length / 4);
                                        var count = -1;
                                        for (var i = 0; i < rows; i++) {
                                            vm.newComment.photos[i] = [];

                                            for (var j = 0; j < 4; j++) {
                                                count++;
                                                if (vm.newComment.imageFiles[count] !== null && vm.newComment.imageFiles[count] !== undefined) {
                                                    vm.newComment.photos[i][j] = vm.newComment.imageFiles[count];
                                                }
                                            }
                                        }
                                    }
                                    $scope.$evalAsync();
                                }
                            } else {
                                fileCount++;
                                vm.newComment.attachmentFiles.push(attachment);
                            }
                            vm.newComment.commentFiles.push(attachment);
                        })
                        showFilesView();
                    }
                };
            }

            function showFilesView() {
                var modal = document.getElementById("comment-files");
                modal.style.display = "block";
                var sidePanelHeight = $('#rightSidePanel').outerHeight();
                var sidePanelContentHeight = $('#rightSidePanelContent').outerHeight();
                var sidePanelWidth = $('#rightSidePanel').outerWidth();
                var fileHeader = $('#file-header').outerHeight();
                var fileFooter = $('#file-footer').outerHeight();
                $('#comment-files').height(sidePanelHeight);
                $('#comment-files').width(sidePanelWidth);
                $('.files-content').height(sidePanelContentHeight);
                var filesContent = $('.files-content').outerHeight();
                var filesContentWidth = $('.files-content').outerWidth();
                $('#file-content').height(filesContent - (fileHeader + fileFooter));
                $('#file-content').width(filesContentWidth);

                $scope.$evalAsync();

            }

            vm.hideFilesView = hideFilesView;
            function hideFilesView() {
                var modal = document.getElementById("comment-files");
                modal.style.display = "none";
                vm.newComment.attachmentFiles = [];
                vm.newComment.imageFiles = [];
                vm.newComment.commentFiles = [];

            }

            function resizeFilesView() {
                if (vm.newComment.commentFiles.length > 0) {
                    var sidePanelHeight = $('#rightSidePanel').outerHeight();
                    var sidePanelContentHeight = $('#rightSidePanelContent').outerHeight();
                    var sidePanelWidth = $('#rightSidePanel').outerWidth();
                    var fileHeader = $('#file-header').outerHeight();
                    var fileFooter = $('#file-footer').outerHeight();
                    $('#comment-files').height(sidePanelHeight);
                    $('#comment-files').width(sidePanelWidth);
                    $('.files-content').height(sidePanelContentHeight);
                    var filesContent = $('.files-content').outerHeight();
                    var filesContentWidth = $('.files-content').outerWidth();
                    $('#file-content').height(filesContent - (fileHeader + fileFooter));
                    $('#file-content').width(filesContentWidth);
                    $scope.$evalAsync();
                }

                var commentImageModal = document.getElementById("comment-image");
                if (commentImageModal != null) {
                    $timeout(function () {
                        $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                        $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                    }, 100)
                }
            }

            vm.sendImages = sendImages;
            function sendImages() {
                if (validateComment()) {
                    $rootScope.showBusyIndicator($('#comment-files'));
                    CommentsService.createComment(vm.newComment).then(
                        function (data) {
                            vm.newComment.id = data.id;
                            vm.newComment.commentedDate = data.commentedDate;
                            vm.newComment.commentedBy = data.commentedBy;
                            vm.comments.unshift(vm.newComment);
                            CommentsService.uploadCommentFiles(vm.newComment.id, vm.newComment.commentFiles).then(
                                function (data) {
                                    vm.newComment.images = data.images;
                                    vm.newComment.attachments = data.attachments;
                                    vm.newComment.videos = data.videos;
                                    vm.newComment.imageFiles = [[]];
                                    var rows = Math.ceil(vm.newComment.images.length / 4);
                                    var count = -1;
                                    for (var i = 0; i < rows; i++) {
                                        vm.newComment.imageFiles[i] = [];

                                        for (var j = 0; j < 4; j++) {
                                            count++;
                                            if (vm.newComment.images[count] !== null && vm.newComment.images[count] !== undefined) {
                                                vm.newComment.imageFiles[i][j] = vm.newComment.images[count];
                                            }
                                        }
                                    }

                                    vm.newComment.videoFiles = [[]];
                                    var videoRows = Math.ceil(vm.newComment.videos.length / 2);
                                    var videoCount = -1;
                                    for (var i = 0; i < videoRows; i++) {
                                        vm.newComment.videoFiles[i] = [];

                                        for (var j = 0; j < 2; j++) {
                                            videoCount++;
                                            if (vm.newComment.videos[videoCount] !== null && vm.newComment.videos[videoCount] !== undefined) {
                                                if (vm.newComment.videos[videoCount].mediaType == 'VIDEO') {
                                                    var url = "api/col/comments/image/" + vm.newComment.videos[videoCount].id;
                                                    vm.newComment.videos[videoCount].url = $sce.trustAsResourceUrl(url);
                                                    URL.revokeObjectURL(vm.newComment.videos[videoCount].url);
                                                }
                                                vm.newComment.videoFiles[i][j] = vm.newComment.videos[videoCount];
                                            }
                                        }
                                    }
                                    vm.newComment = angular.copy(vm.emptyComment);
                                    $scope.$evalAsync();
                                    $rootScope.hideBusyIndicator();
                                    loadCommentCount();
                                    hideFilesView();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                }
            }

            vm.filePreview = filePreview;
            function filePreview(file) {
                var fileId = file.id;
                var url = "{0}//{1}/api/col/attachments/{2}/preview".
                    format(window.location.protocol, window.location.host, fileId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            vm.imagePreview = imagePreview;
            function imagePreview(file) {
                var fileId = file.id;
                var url = "{0}//{1}/api/col/media/{2}/preview".
                    format(window.location.protocol, window.location.host, fileId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.fileName;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            vm.selectedCommentImages = [];
            vm.selectedImage = null;
            vm.showImages = showImages;
            function showImages(comment, image) {
                vm.selectedCommentImages = [];
                vm.selectedImage = image;
                vm.selectedCommentImages = comment.images;
                angular.forEach(vm.selectedCommentImages, function (commentImage) {
                    if (image.id == commentImage.id) {
                        commentImage.showImage = true;
                    } else {
                        commentImage.showImage = false;
                    }
                })
                var modal = document.getElementById("comment-image");
                modal.style.display = "block";

                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                }, 200)
            }

            $scope.showPreviousImage = showPreviousImage;
            function showPreviousImage(image) {
                var index = vm.selectedCommentImages.indexOf(image);
                vm.selectedImage = null;
                if (index == 0) {
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
                }, 100)
            }

            $scope.showNextImage = showNextImage;
            function showNextImage(image) {
                var index = vm.selectedCommentImages.indexOf(image);
                vm.selectedImage = null;
                if (index == 0 || index < (vm.selectedCommentImages.length - 1)) {
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
                }, 100)
            }

            $scope.hideImagesView = hideImagesView;
            function hideImagesView() {
                var modal = document.getElementById("comment-image");
                modal.style.display = "none";
            }

            function onKeyUp() {
                var el = document.getElementById("commentTextbox");
                el.style.cssText = 'height:auto; padding:0';
                // for box-sizing other than "content-box" use:
                // el.style.cssText = '-moz-box-sizing:content-box';
                el.style.cssText = 'height:' + el.scrollHeight + 'px';
            }

            (function () {
                loadComments();
                $(window).resize(resizeFilesView);
            })();
        }
    }
);