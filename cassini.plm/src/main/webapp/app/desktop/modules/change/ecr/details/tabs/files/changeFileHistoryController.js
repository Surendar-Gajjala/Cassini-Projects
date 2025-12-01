define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/changeFileService',
        'app/shared/services/core/itemFileService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService'
    ],
    function (module) {
        module.controller('ChangeFileHistoryController', ChangeFileHistoryController);

        function ChangeFileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                             ChangeFileService, CommonService, ItemFileService, CommentsService) {
            var vm = this;

            vm.id = $scope.data.objectId;
            vm.file = $scope.data.file;
            vm.callback = $scope.callback;
            vm.fileMode = $scope.data.fileMode;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            vm.showComment = showComment;
            vm.showDownload = showDownload;
            vm.createComment = createComment;

            vm.emptyComment = {
                objectType: "FILE",
                objectId: vm.file.id,
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
                if (vm.newComment.comment == null || vm.newComment.comment.trim() == "") {
                    $rootScope.showWarningMessage(commentMsg);
                    valid = false;
                }
                return valid;
            }

            function createComment() {
                if (validateComment()) {
                    CommentsService.createComment(vm.newComment).then(
                        function (data) {
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

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.loadFileDownloadHistory = loadFileDownloadHistory;

            vm.clickOnVersionToDownload = parsed.html($translate.instant("CLICK_ON_VERSION_DOWNLOAD")).html();

            function loadFileVersions() {
                ChangeFileService.getAllFileVersions(vm.id, vm.file.id).then(
                    function (data) {
                        vm.fileVersions = data;
                        vm.fileName = vm.fileVersions[0].name;
                        CommonService.getPersonReferences(vm.fileVersions, 'createdBy');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );

                ChangeFileService.getFileVersionComments(vm.file.id, "FILE").then(
                    function (data) {
                        vm.fileVersions = data;
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
                var url = "{0}//{1}/api/changes/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    vm.id, file.id);
                $rootScope.downloadFileFromIframe(url);
                ChangeFileService.updateChangeFileDownloadHistory(vm.id, file.id).then(
                    function (data) {
                        loadFileVersions();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadFileDownloadHistory() {
                ItemFileService.getFileDownloadHistory(vm.id, vm.file.id).then(
                    function (data) {
                        vm.fileDownloadHistories = data;
                        CommonService.getPersonReferences(vm.fileDownloadHistories, 'person');
                        vm.fileName = vm.file.name;
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