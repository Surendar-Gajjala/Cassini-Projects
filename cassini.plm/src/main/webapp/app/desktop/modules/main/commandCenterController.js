define(
    [
        'app/desktop/modules/main/main.module'
    ],

    function (module) {
        module.controller('CommandCenterController', CommandCenterController);

        function CommandCenterController($scope, $rootScope, $timeout, $interval, $state, $location, $application, $translate, $window) {

            var vm = this;

            var animationShowCls = "animated bounceIn";

            vm.close = close;

            function close() {
                $('#commandCenter').removeClass(animationShowCls);
                $('#commandCenter').hide();
            }

            function initEvents() {
                $('body').on('click', function () {
                    close();
                });

                //Double Ctrl key press event
                $(document).on('keydown', function (e) {
                    if (e.altKey && e.keyCode == 67) {
                        var commandCenterPanel = $('#commandCenter');
                        commandCenterPanel.show();
                        commandCenterPanel.outerWidth();
                        commandCenterPanel.addClass(animationShowCls);
                    }
                });

                //ESC key press event
                $(document).on('keyup', function (evt) {
                    if (evt.keyCode == 27) {
                        close();
                    }
                });
            }

            (function () {
                initEvents();
            })();
        }
    }
);