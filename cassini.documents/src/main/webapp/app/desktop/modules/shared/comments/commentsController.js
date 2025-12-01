define (
    [
        'app/desktop/modules/shared/shared.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],

    function(module) {
        module.controller('CommentsController', CommentsController);

        function CommentsController($scope, $rootScope, $timeout, CommentsService) {

            var vm = this;

            vm.data = $scope.data;

            vm.comments = [];
            vm.emptyComment = {
                objectType: vm.data.objectType,
                objectId: vm.data.objectId,
                comment: ""
            };

            vm.newComment = angular.copy(vm.emptyComment);

            vm.createComment = createComment;
            vm.onKeyUp = onKeyUp;

            function loadComments() {
                var pageable = {
                    page: 0,
                    size: 1000
                };
                CommentsService.getRootComments(vm.data.objectType, vm.data.objectId, pageable).then(
                    function(data) {
                        vm.comments = data.content;
                    }
                )
            }

            function createComment() {
                if(validateComment()) {
                    CommentsService.createComment(vm.newComment).then(
                        function(data) {
                            vm.comments.unshift(data);
                            vm.newComment = angular.copy(vm.emptyComment);
                            $rootScope.$broadcast('comment.added');
                            $timeout(function() {
                                onKeyUp();
                            }, 300);
                        }
                    )
                }
            }

            function validateComment() {
                var valid = true;

                $scope.commentError = false;
                if(vm.newComment.comment == null || vm.newComment.comment.trim() == "") {
                    valid = false;
                }
                return valid;
            }

            function onKeyUp() {
                var el = document.getElementById("commentTextbox");
                el.style.cssText = 'height:auto; padding:0';
                // for box-sizing other than "content-box" use:
                // el.style.cssText = '-moz-box-sizing:content-box';
                el.style.cssText = 'height:' + el.scrollHeight + 'px';
            }

            (function() {
                loadComments();
            })();
        }
    }
);