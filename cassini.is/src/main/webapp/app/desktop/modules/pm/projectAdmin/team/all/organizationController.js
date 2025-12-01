define(['app/desktop/modules/pm/pm.module',
        'app/desktop/modules/pm/projectAdmin/team/new/personDialogueController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService'

    ],
    function (module) {
        module.controller('OrganizationController', OrganizationController);

        function OrganizationController($scope, $rootScope, $timeout, DialogService, ProjectService,
                                        CommonService, $state, $stateParams) {

            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Team";

            var vm = this;

            vm.loading = true;
            $rootScope.showFlag = false;
            vm.showValues = true;
            vm.personRole = null;
            vm.person = null;

            vm.persons = [];
            vm.projectRoles = [];
            vm.personAddButton = false;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "rowId",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.projectPersons = angular.copy(pagedResults);

            vm.personData = {
                spid: null,
                node: null,
                name: null,
                parentId: null,
                pic: null
            };

            function loadOrgChart(persons) {
                var marker = "<marker id='markerArrow' markerWidth='12' markerHeight='12' refX='12' refY='6' orient='auto'><path d='M0,0 L12,6 L0,12 L0,0' style='fill: #000000;' /> </marker>";
                //console.log(vm.projectPersons.content);
                var orgchart = new getOrgChart(document.getElementById("people"), {
                    embededDefinitions: marker,
                    photoFields: ["pic"],
                    clickNodeEvent: clickHandler,
                    removeNodeEvent: removeNodeEvent,
                    dataSource: persons
                });

                console.log(orgchart.dataSource);

            }

            function clickHandler(sender, args) {
                var options = {
                    title: 'New Organization',
                    showMask: true,
                    template: 'app/desktop/modules/pm/projectAdmin/team/all/projectPersonView.jsp',
                    controller: 'ProjectPersonController as projectPersonVm',
                    resolve: 'app/desktop/modules/pm/projectAdmin/team/all/projectPersonController',
                    width: 600,
                    data: {
                        node: args
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.projectPerson.new'}
                    ],
                    callback: function (person) {
                        loadProjectPersons();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeNodeEvent(sender, args) {
                var options = {
                    title: "Remove Node or Person",
                    message: "Are you sure want to delete ",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectService.deletePersonNode($stateParams.projectId, args.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("deeeeeell");
                            }
                        )
                    }
                });

            }

            /*$('.get-btn').click(function() {
             var d = $(this).data('btn-action');
             alert(d);
             } );*/

            /*   $(document).on('click', 'g[class^="get-btn"]', function (sender, args) {
             dataSource.push(args);
             var d = $(this).data('btn-action');
             dataSource.push(args);
             createDataSources();
             });*/

            var dataObject = [];
            vm.savePersonDetails = savePersonDetails;
            function savePersonDetails() {
                if (vm.personData != null) {
                    dataObject.push(vm.personData);
                    /* angular.forEach(vm.chartPersons, function (chartPerson) {
                     if (vm.personData.node != chartPerson.node) {

                     }
                     })*/
                    vm.personData = {
                        id: null,
                        node: null,
                        personName: null,
                        parent: null
                    }
                    var modal = document.getElementById("sectionsMenu");
                    modal.style.display = "none";
                }
            }

            vm.savePersonObject = savePersonObject;
            function savePersonObject() {
                ProjectService.createOrgChart($stateParams.projectId, dataObject).then(
                    function (data) {

                    }
                )
            }

            function loadProjectPersons() {
                ProjectService.getProjectPersonObjects($stateParams.projectId).then(
                    function (data) {
                        vm.projectPersons = data;
                        vm.chartPersons = [];
                        var empty = {
                            node: null,
                            parent: null,
                            Name: null,
                            Title: null,
                            pic: null,
                            nodeName: null,
                            person: null,
                            projectRole: null,
                            role: null
                        };
                        if (vm.projectPersons.length == 0) {
                            var newPerson = angular.copy(empty);
                            newPerson.node = 1;
                            newPerson.parent = null;
                            newPerson.Name = name;
                            newPerson.Title = null;
                            newPerson.pic = "app/assets/images/user-grey.png";
                            vm.chartPersons.push(newPerson);
                        }
                        angular.forEach(vm.projectPersons, function (person) {
                            person.editMode = false;
                            var newPerson = angular.copy(empty);
                            newPerson.node = person.node;
                            newPerson.parent = person.parentId;
                            if (person.projectPerson == null) {
                                newPerson.Name = person.nodeName;
                                newPerson.nodeName = person.nodeName;
                            }
                            if (person.projectPerson != null) {
                                newPerson.Name = person.projectPerson.fullName;
                                newPerson.person = person.projectPerson;
                                newPerson.pic = "api/is/items/" + person.projectPerson.id + "/personImage/download?" + new Date().getTime();
                            }
                            if (person.projectRole != null) {
                                newPerson.Title = person.projectRole.role;
                                newPerson.projectRole = person.projectRole;
                            }
                            newPerson.id = person.rowId;
                            newPerson.role = person.role;
                            vm.chartPersons.push(newPerson);
                        });

                        $timeout(function () {
                            loadOrgChart(vm.chartPersons);
                        }, 1000)
                    }
                )

            }

            (function () {
                if ($application.homeLoaded == true) {

                    $('a[title="GetOrgChart jquery plugin"]').hide();
                    loadProjectPersons();
                    $scope.$on('app.team.person', savePersonObject)
                }
            })();

        }
    }
)
;