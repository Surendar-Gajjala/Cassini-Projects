define(
    [
        'app/desktop/modules/shared/shared.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/tagsService'
    ],
    function (module) {
        module.controller('ObjectTagsController', ObjectTagsController);

        function ObjectTagsController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies,
                                      TagsService) {

            var parsed = angular.element("<div></div>");
            var tagAddedMsg = parsed.html($translate.instant("TAG_ADDED_MSG")).html();
            var tagDeletedMsg = parsed.html($translate.instant("TAG_DELETED_MSG")).html();
            var pleaseEnterTag = parsed.html($translate.instant("P_E_TAG")).html();
            $scope.addTagTitle = parsed.html($translate.instant("CLICK_TO_ADD_TAG")).html();
            $scope.deleteTagTitle = parsed.html($translate.instant("DELETE_TAG")).html();
            var vm = this;
            vm.tag = null;
            vm.objectTags = [];
            vm.newTag = {
                id: null,
                label: null,
                objectType: $scope.data.tagObjectType,
                object: $scope.data.tagObjectId
            };

            vm.createTag = createTag;
            vm.deleteTag = deleteTag;

            function createTag() {
                if (vm.newTag.label != null && vm.newTag.label != "" && vm.newTag.label != undefined) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    TagsService.createTag(vm.newTag).then(
                        function (data) {
                            vm.objectTags.unshift(data);
                            $scope.callback(vm.objectTags.length);
                            vm.newTag = {
                                id: null,
                                label: null,
                                objectType: $scope.data.tagObjectType,
                                object: $scope.data.tagObjectId
                            };
                            $rootScope.showSuccessMessage(tagAddedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage(pleaseEnterTag);
                }
            }

            function deleteTag(tag) {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                TagsService.deleteTag(tag.id).then(
                    function (data) {
                        vm.objectTags.splice(vm.objectTags.indexOf(tag), 1);
                        $scope.callback(vm.objectTags.length);
                        $rootScope.showSuccessMessage(tagDeletedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function close() {
                $rootScope.hideSidePanel();
            }

            function loadObjectTags() {
                TagsService.getAllObjectTags($scope.data.tagObjectId).then(
                    function (data) {
                        vm.objectTags = data;
                    }
                )
            }

            (function () {
                loadObjectTags();
                $rootScope.$on('app.object.tags.add', close);
            })();
        }
    }
)
;