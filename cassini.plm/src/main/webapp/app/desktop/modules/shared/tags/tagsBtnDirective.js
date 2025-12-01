define(
    [
        'app/desktop/modules/shared/shared.module'
    ],

    function (module) {
        module.directive('tagsBtn', TagsBtnDirective);

        function TagsBtnDirective($rootScope, $compile, $timeout, $translate) {
            return {
                templateUrl: 'app/desktop/modules/shared/tags/tagsBtn.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'objectType': '=',
                    'object': '=',
                    'tagsCount': '='
                },

                link: function (scope, elm, attr) {

                    var parsed = angular.element("<div></div>");
                    scope.showTags = showTags;
                    scope.addTagTitle = parsed.html($translate.instant("SHOW_TAG")).html();
                    var tagsTitle = parsed.html($translate.instant("TAGS")).html();

                    function showTags() {
                        var options = {
                            title: tagsTitle,
                            template: 'app/desktop/modules/shared/tags/objectTagsView.jsp',
                            controller: 'ObjectTagsController as objectTagsVm',
                            resolve: 'app/desktop/modules/shared/tags/objectTagsController',
                            width: 600,
                            showMask: true,
                            data: {
                                tagObjectType: scope.objectType,
                                tagObjectId: scope.object,
                                tagsCount: scope.tagsCount
                            },
                            callback: function (tagsCount) {
                                scope.tagsCount = tagsCount;
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }
                }
            }
        }
    }
)
;