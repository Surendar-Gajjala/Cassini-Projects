define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService'
    ],
    function (module) {
        module.controller('SpecificationFileHistoryController', SpecificationFileHistoryController);

        function SpecificationFileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                                    SpecificationsService, CommonService, CommentsService) {
            var vm = this;

            vm.data = $scope.data;
            vm.fileMode = $scope.data.specFileMode;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            vm.showComment = showComment;
            vm.showDownload = showDownload;
            vm.createComment = createComment;

            vm.emptyComment = {
                objectType: "FILE",
                objectId: vm.data.specFile.id,
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

            function createComment() {
                if (validateComment()) {
                    CommentsService.createComment(vm.newComment).then(
                        function (data) {

                            if (vm.fileVersions.length > 0) {
                                loadFileVersions();
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
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadFileVersions() {
                SpecificationsService.getAllFileVersionComments(vm.data.changeSpecId, vm.data.specFile.id, "FILE").then(
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
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/rm/specifications/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    vm.data.changeSpecId, file.id);
                //launchUrl(url);

                window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);
                SpecificationsService.updateFileDownloadHistory(vm.data.changeSpecId, file.id).then(
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