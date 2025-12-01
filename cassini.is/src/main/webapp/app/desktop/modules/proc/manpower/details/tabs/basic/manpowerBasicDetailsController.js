define(
    [
        'app/desktop/modules/proc/proc.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ManpowerBasicDetailsController', ManpowerBasicDetailsController);

        function ManpowerBasicDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce,
                                                CommonService, ItemService) {

            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Manpower Details";

            var vm = this;

            vm.manpowerId = $stateParams.manpowerId;
            vm.prjId = $stateParams.projectId;
            vm.manpower = null;
            vm.attributes = [];
            vm.changeImage = false;
            vm.personImage = null;
            vm.personImagePath = null;
            var updateImage = false;

            vm.change = change;
            vm.cancel = cancel;
            vm.showImage = showImage;
            $scope.setImage = setImage;
            vm.updateManpower = updateManpower;

            function loadManpower() {
                vm.loading = true;
                ItemService.getManpowerItem(vm.manpowerId).then(
                    function (data) {
                        vm.manpower = data;
                        vm.loading = false;
                        if (vm.manpower.person.image != null) {
                            vm.personImagePath = "api/is/items/" + vm.manpower.person.id + "/personImage/download?" + new Date().getTime();
                        }
                        $rootScope.viewInfo.title = "Man Power Details : " + vm.manpower.person.fullName;
                        $rootScope.viewInfo.description = "Type: {0}".format(vm.manpower.itemType.name);
                        CommonService.getPersonReferences([vm.manpower], 'createdBy');
                        CommonService.getPersonReferences([vm.manpower], 'modifiedBy');
                    }
                )
            }

            function validate() {
                var flag = true;
                if (vm.manpower.person.fullName == null || vm.manpower.person.fullName == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Manpower Name cannot be empty");
                }
                return flag;
            }

            function updateManpower() {
                if (validate()) {
                    var parts = vm.manpower.person.fullName.split(' ');

                    vm.manpower.person.firstName = parts.shift(); // removes the first item from the array
                    vm.manpower.person.lastName = parts.join(' ');
                    ItemService.updateManpower(vm.manpower).then(
                        function (data) {
                            if (vm.changeImage) {
                                /*savePersonImage();*/
                            }
                            loadManpower();
                            $rootScope.showSuccessMessage("Manpower updated successfully");
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            loadManpower();
                        }
                    );
                }
            }

            /*----------  To show large image when click on Image  ------------------*/

            function showImage() {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById("img03");

                modal.style.display = "block";
                modalImg.src = "api/is/items/" + vm.manpower.person.id + "/personImage/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function cancel() {
                vm.changeImage = false;
                updateImage = false;
                vm.personImagePath = "api/is/items/" + vm.manpower.person.id + "/personImage/download?" + new Date().getTime();
                vm.changeImage = false;
            }

            function change() {
                vm.changeImage = true;
            }

            function setImage(files) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#image1')
                        .attr('src', e.target.result)
                        .width(100)
                        .height(100);
                };
                updateImage = true;
                vm.personImage = files[0];
                reader.readAsDataURL(files[0]);
            }

            function validatePersonImage() {
                var valid = true;
                var typeIndex = vm.personImage.name.lastIndexOf(".");
                var imageType = vm.personImage.name.substring(typeIndex).toLowerCase();
                if (imageType == ".jpg" || imageType == ".jpeg" || imageType == ".png") {
                    if (vm.personImage.size >= 2097152) {
                        valid = false;
                        vm.personImage = null;
                        vm.personImagePath = null;
                        $rootScope.showWarningMessage("Please upload photo with in 1MB size");
                    }

                    if (vm.personImage.size < 2097152) {
                        valid = true;
                        vm.personImageType = imageType;
                    }
                } else {
                    valid = false;
                    vm.personImage = null;
                    vm.personImagePath = null;
                    $rootScope.showWarningMessage("Invalid File format");
                }

                return valid;
            }

            vm.savePersonImage = savePersonImage;
            function savePersonImage() {
                if (validatePersonImage()) {
                    ItemService.uploadImage(vm.manpower.person.id, vm.personImage).then(
                        function (data) {
                            vm.manpower.person.image = vm.personImage;
                            vm.personImagePath = "api/is/items/" + vm.manpower.person.id + "/personImage/download?" + new Date().getTime();
                            $rootScope.showSuccessMessage("Manpower updated successfully");
                            vm.changeImage = false;
                        }
                    )
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadManpower();
                }
                else {
                    return;
                }
            })();
        }
    }
);