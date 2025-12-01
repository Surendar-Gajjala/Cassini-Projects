define(
    [
        'app/desktop/modules/col/col.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/emailMessageService'
    ],
    function (module) {
        module.controller('EmailController', EmailController);

        function EmailController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce, $application,
                                 EmailMessageService) {

            if ($application.homeLoaded === false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-comments";
            $rootScope.viewInfo.title = "Communication";
            var vm = this;

            vm.messages = [];
            vm.loadingMessages = false;
            var stopLoading = false;
            vm.selectedMessage = {id: null, selected: false, message: "", recipients: []};

            vm.showMessage = showMessage;
            vm.downloadAttachment = downloadAttachment;
            vm.loadEmailMessages = loadEmailMessages;
            var pageable = {
                page: 1,
                size: 15,
                sort: {
                    field: "createdDate",
                    order: "ASC"

                }
            };

            function loadEmailMessages() {
                if (!vm.loadingMessages) {
                    vm.loadingMessages = true;
                    EmailMessageService.getObjectEmail($stateParams.projectId, pageable).then(
                        function (data) {
                            vm.loadingMessages = false;
                            angular.forEach(data, function (message) {
                                vm.messages.push(message);
                            });
                            if (vm.messages.length > 0 && vm.messages[0].totalMessages == vm.messages.length) {
                                stopLoading = true;
                            }
                            pageable.page++;
                            vm.selectedMessage.selected = false;
                            angular.forEach(vm.messages, function (message) {
                                message.selected = false;
                            });

                            $rootScope.hideBusyIndicator();
                            if (vm.messages.length > 0) {
                                vm.selectedMessage = vm.messages[0];
                                vm.selectedMessage.selected = true;
                            }
                        },
                        function (error) {
                            vm.loadingMessages = false;
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function showMessage(message) {
                vm.selectedMessage.selected = false;
                vm.selectedMessage = message;
                vm.selectedMessage.selected = true;
            }

            function downloadAttachment(messageId, fileName) {
                var url = "{0}//{1}/api/col/email/{2}/{3}?fileName={4}".
                    format(window.location.protocol, window.location.host,
                    $stateParams.projectId, messageId, encodeURI(fileName));
                launchUrl(url);
            }

            module.directive("loadNext", function () {
                return {

                    restrict: 'A',
                    link: function (scope, elem, attrs) {

                        // we get a list of elements of size 1 and need the first element
                        var raw = elem[0];

                        // we load more elements when scrolled past a limit
                        elem.bind("scroll", function () {
                            if (!vm.loadingMessages && !stopLoading) {
                                if (((raw.scrollTop + raw.offsetHeight + 5) >= raw.scrollHeight)) {
                                    scope.$apply(attrs.loadNext);
                                }
                            }
                        });
                    }
                }
            });

            (function () {
                loadEmailMessages();
            })();

        }

        module.filter('toTrusted', ['$sce', function ($sce) {
            return function (text) {
                return $sce.trustAsHtml(text);
            };
        }]);
    }
);