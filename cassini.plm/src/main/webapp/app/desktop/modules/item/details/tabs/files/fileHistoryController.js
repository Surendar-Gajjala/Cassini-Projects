define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/changeFileService',
        'app/shared/services/core/objectFileService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService'
    ],
    function (module) {
        module.controller('FileHistoryController', FileHistoryController);

        function FileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                       ItemFileService, CommonService, CommentsService, ObjectFileService, ChangeFileService) {
            var vm = this;

            vm.data = $scope.data;
            vm.fileMode = $scope.data.itemFileMode;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            vm.showComment = showComment;
            vm.showDownload = showDownload;
            vm.createComment = createComment;

            vm.emptyComment = {
                objectType: vm.data.itemFile.objectType,
                objectId: vm.data.itemFile.id,
                comment: null
            };

            var parsed = angular.element("<div></div>");
            var commentMsg = parsed.html($translate.instant("PLEASE_ENTER_COMMENT")).html();
            vm.enterComment = parsed.html($translate.instant("ENTER_COMMENT")).html();

            vm.newComment = angular.copy(vm.emptyComment);

            function showComment(fileVersion) {
                if (fileVersion.showComments == false) {
                    fileVersion.showComments = true
                } else {
                    fileVersion.showComments = false
                }

            }

            function showDownload(fileVersion) {
                if (fileVersion.showDownloads == false) {
                    fileVersion.showDownloads = true
                } else {
                    fileVersion.showDownloads = false
                }

            }

            function validateComment() {
                var valid = true;

                $scope.commentError = false;
                if (vm.newComment.comment == null || vm.newComment.comment.trim() == "") {
                    $rootScope.showWarningMessage(commentMsg);
                    valid = false;
                }
                return valid;
            }

            vm.hideComments = parsed.html($translate.instant("HIDE_COMMENTS")).html();
            vm.showComments = parsed.html($translate.instant("SHOW_COMMENT")).html();

            function createComment() {
                if (validateComment()) {
                    $rootScope.showBusyIndicator();
                    CommentsService.createComment(vm.newComment).then(
                        function (data) {
                            if (vm.data.itemFile.external && !$rootScope.loginPersonDetails.external) {
                                ObjectFileService.sendFileCommentNotification(vm.data.itemFile.object, vm.data.itemFile.id, vm.data.selectedFileType, data, "external").then(
                                    function (mail) {
                                        updateData(data);
                                    }
                                )
                            } else if ($rootScope.loginPersonDetails.external) {
                                ObjectFileService.sendFileCommentNotification(vm.data.itemFile.object, vm.data.itemFile.id, vm.data.selectedFileType, data, "qa").then(
                                    function (mail) {
                                        updateData(data);
                                    }
                                )
                            } else {
                                updateData(data);
                            }

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function updateData(data) {
                if (vm.fileVersions.length > 0) {
                    if (vm.fileVersions[0].comments == null) {
                        vm.fileVersions[0].comments = [];
                        vm.fileVersions[0].comments.unshift(data);
                        vm.fileVersions[0].showComments = true;
                    } else {
                        vm.fileVersions[0].comments.unshift(data);
                        vm.fileVersions[0].showComments = true;
                    }
                    vm.newComment = angular.copy(vm.emptyComment);
                } else {
                    vm.newComment = angular.copy(vm.emptyComment);
                    loadFileVersions();
                }
                $rootScope.hideBusyIndicator();
            }

            function loadFileVersions() {
                ObjectFileService.getFileVersionComments(vm.data.itemFile.id, vm.data.selectedFileType, vm.data.itemFile.objectType).then(
                    function (data) {
                        vm.fileVersions = data.objectFiles;
                        angular.forEach(vm.fileVersions, function (fileVersion) {
                            fileVersion.showComments = true;
                            fileVersion.showDownloads = true;
                        });
                        CommonService.getPersonReferences(vm.fileVersions, 'createdBy');
                        vm.fileName = vm.fileVersions[0].name
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                    format(window.location.protocol, window.location.host,
                    vm.data.itemId, vm.data.selectedFileType, file.id);
                $rootScope.downloadFileFromIframe(url);
                ObjectFileService.updateObjectFileDownloadHistory(vm.data.itemId, vm.data.selectedFileType, file.id).then(
                    function (data) {
                        loadFileVersions();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadFileVersions();
            })();
        }
    }
);