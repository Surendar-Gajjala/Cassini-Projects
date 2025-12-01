define(
    [
        'app/desktop/modules/media/media.module'
    ],
    function (module) {
        module.controller('PicturesController', PicturesController);

        function PicturesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance,
                                pictures, pictureIndex) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.next = next;
            vm.previous = previous;
            vm.cancel = cancel;


            vm.pictures = pictures;
            vm.currentIndex = pictureIndex;


            function previous() {
                if(vm.currentIndex == 0) {
                    vm.currentIndex = pictures.length - 1;
                }
                else {
                    vm.currentIndex--;
                }

                updateImage();
            }

            function next() {
                if(vm.currentIndex == pictures.length - 1) {
                    vm.currentIndex = 0;
                }
                else {
                    vm.currentIndex++;
                }

                updateImage();
            }

            function updateImage() {
                var src = pictures[vm.currentIndex];
                $('#pictureHolder').attr("src", src);
            }


            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }


            vm.keyPressed = function($event){
                if ($event.keyCode == 38) {} //up arrow
                else if ($event.keyCode == 39){ next(); } //right arrow
                else if ($event.keyCode == 40){} //down arrow
                else if ($event.keyCode == 37){ previous(); } //left
            };


            (function () {
                $uibModalInstance.opened.then(function() {
                    $timeout(function() {
                        updateImage();
                    });
                });
            })();
        }
    }
);