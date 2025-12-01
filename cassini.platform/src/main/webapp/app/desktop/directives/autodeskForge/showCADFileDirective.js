/**
 * Created by Nageshreddy on 17-05-2019.
 */
define(
    [
        'app/desktop/desktop.app'
    ],

    function (module) {
        module.directive('showForgeFile', ['$compile', '$timeout', '$application', '$rootScope', '$translate', '$sce', function ($compile, $timeout, $application, $rootScope, $translate, $sce) {
            function link(scope, element, attrs) {
                scope.registerCallBack({
                    callback: function (file) {
                        var modal = document.getElementById('forgeModel');

                        modal.style.display = "block";
                        scope.fileUrl = "app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/forgeView.html?url=" + $application.forgeToken + "&urn=" + file.urn;
                        var span = document.getElementsByClassName("closeImage1")[0];
                        span.onclick = function () {
                            modal.style.display = "none";
                        }
                        $('#forgeFrame').attr('src', scope.fileUrl);
                    }
                });
            }

            return {
                templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/showCADFileView.jsp',
                restrict: 'E',
                replace: false,
                /*controller: function ($scope) {
                 var span = document.getElementsByClassName("closeImage1")[0];
                 span.onclick = function () {
                 modal.style.display = "none";
                 }
                 },*/
                scope: {
                    registerCallBack: '&'
                }, link: link
            };
        }])
        ;
    }
)
;
