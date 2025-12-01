define(
    [
        'app/desktop/modules/main/main.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {

        module.directive('personAvatar', PersonAvatarDirective);

        function PersonAvatarDirective($rootScope, $translate, $timeout,
                                       CommonService) {

            return {
                templateUrl: 'app/desktop/directives/person-avatar/personAvatarDirective.jsp',
                restrict: 'E',
                scope: {
                    personId: "=",
                    editPicture: "=",
                    display: "="
                },

                link: function ($scope, element, attrs) {
                    $scope.person = null;

                    function loadPerson() {
                        CommonService.getPerson($scope.personId).then(
                            function (person) {
                                $scope.person = person;

                                var firstLetter = "";
                                var lastLetter = "";
                                if (person.firstName != null && person.firstName != "") {
                                    firstLetter = person.firstName.substring(0, 1).toUpperCase();
                                }
                                if (person.lastName != null && person.lastName != "") {
                                    lastLetter = person.lastName.substring(0, 1).toUpperCase();
                                }
                                person.imageWord = firstLetter + "" + lastLetter;
                                if (person.hasImage) {
                                    person.personImage = "api/common/persons/" + person.id + "/image/download?" + new Date().getTime();
                                }
                            }
                        )
                    }

                    (function () {
                        loadPerson();
                    })();
                }
            }
        }
    }
);