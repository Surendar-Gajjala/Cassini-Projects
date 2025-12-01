define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemFileService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService'
    ],
    function (module) {
        module.controller('FileHistoryController', FileHistoryController);

        function FileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       ItemFileService, CommonService, CommentsService) {
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
                objectType: "FILE",
                objectId: vm.data.itemFile.id,
                comment: null
            };

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

                        }
                    )
                }
            }

            function loadFileVersions() {
                ItemFileService.getAllFileVersionComments(vm.data.itemFile.id, "FILE").then(
                    function (data) {
                        vm.fileVersions = data;
                        angular.forEach(vm.fileVersions, function (fileVersion) {
                            fileVersion.showComments = false;
                            fileVersion.showDownloads = false;
                        });
                        CommonService.getPersonReferences(vm.fileVersions, 'createdBy');
                        vm.fileName = vm.fileVersions[0].name
                    }
                );
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/drdo/items/{2}/files/{3}/preview".
                    format(window.location.protocol, window.location.host,
                    vm.data.itemId, file.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                /*window.open(url);
                 $timeout(function () {
                 window.close();
                 }, 2000);*/
                ItemFileService.updateFileDownloadHistory(vm.data.itemId, file.id).then(
                    function (data) {

                    }
                )
            }

            (function () {
                loadFileVersions();
            })();
        }
    }
);