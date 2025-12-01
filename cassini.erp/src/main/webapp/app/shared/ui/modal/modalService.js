/**
 * Created by reddy on 12/10/14.
 */
define(['app/app.modules'],
    function (app) {
        app.service('modalService', ['$modal',
            function ($modal) {

                var modalDefaults = {
                    backdrop: true,
                    keyboard: true,
                    modalFade: true
                };

                var modalOptions = {
                    closeButtonText: 'Close',
                    actionButtonText: 'OK',
                    headerText: 'Proceed?',
                    bodyText: 'Perform this action?'
                };

                this.showModal = function (defaults, options) {
                    if (!defaults) defaults = {};
                    defaults.backdrop = 'static';
                    return this.show(defaults, options);
                };

                this.show = function (defaults, options) {
                    //Create temp objects to work with since we're in a singleton service
                    var tempDefaults = {};
                    var tempOptions = {};

                    //Map angular-ui modal custom defaults to modal defaults defined in service
                    angular.extend(tempDefaults, modalDefaults, defaults);

                    //Map modal.html $scope custom properties to defaults defined in service
                    angular.extend(tempOptions, modalOptions, options);

                    if (!tempDefaults.controller) {
                        tempDefaults.controller = function ($scope, $modalInstance) {
                            $scope.modalOptions = tempOptions;
                            $scope.modalOptions.ok = function (result) {
                                $modalInstance.close(result);
                            };
                            $scope.modalOptions.close = function (result) {
                                $modalInstance.dismiss('cancel');
                            };
                        }
                    }

                    return $modal.open(tempDefaults).result;
                };

            }]);
    });
