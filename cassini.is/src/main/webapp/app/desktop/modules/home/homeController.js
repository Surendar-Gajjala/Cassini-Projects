define(
    [
        'app/desktop/modules/home/home.module',
        'app/desktop/modules/pm/project/new/newProjectDialogController',
        'app/desktop/modules/pm/project/new/newPortfolioController',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/shared/services/core/itemService',
        'app/shared/services/tm/taskService'
    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $window, $state, $sce, ObjectAttributeService, ItemService, TaskService, ProjectService, CommonService, ObjectTypeAttributeService, AttributeAttachmentService) {
            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Dashboard";
            $rootScope.viewInfo.description = "";
            $rootScope.setViewType('APP');

            var vm = this;
            vm.mode = null;
            vm.projects = [];
            vm.objectIds = [];
            vm.loadingProjects = true;
            vm.projectAttributes = [];
            var currencyMap = new Hashtable();
            var portFolioMap = new Hashtable();
            vm.loadingPortfolios = true;

            vm.showProjectAttributes = showProjectAttributes;
            vm.openProject = openProject;
            vm.showNewProjectDialog = showNewProjectDialog;
            vm.showNewPortfolio = showNewPortfolio;
            vm.showImage = showImage;
            vm.showRequiredImage = showRequiredImage;
            vm.lockProject = lockProject;
            vm.openAttachment = openAttachment;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.removeAttribute = removeAttribute;

            vm.pageable = {
                page: 0,
                size: 20
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.portfolios = angular.copy(pagedResults);

            function openProject(project) {
                $rootScope.setViewType('PROJECT');
                $rootScope.selectedProject = project;
                $rootScope.$broadcast('app.project', {project: project});
                /*if ($rootScope.hasPermission('permission.wbs.view')) {
                 $state.go('app.pm.project.works', {projectId: project.id});
                 } else {
                 $state.go('app.pm.project.home', {projectId: project.id});
                 }*/
                //$state.go('app.pm.project.home', {projectId: project.id});
                $state.go('app.pm.project.home', {projectId: project.id});
            }

            function showNewProjectDialog() {
                var options = {
                    title: 'New Project',
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/new/newProjectDialog.jsp',
                    controller: 'NewProjectController as newProjectVm',
                    resolve: 'app/desktop/modules/pm/project/new/newProjectDialogController',
                    width: 600,
                    buttons: [
                        {text: 'Create', broadcast: 'app.project.new'}
                    ],
                    callback: function (newProject) {
                        //loadPortfolios();
                        //$state.go('app.pm.project.wbs', {projectId: newProject.id})
                        $state.go('app.pm.project.home', {projectId: newProject.id})
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showNewPortfolio(portfolio, mode) {
                var title = mode == "NEW" ? 'New Portfolio' : 'Edit Portfolio';
                var options = {
                    title: title,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/new/newPortfolio.jsp',
                    controller: 'PortfolioController as newPortfolioVm',
                    resolve: 'app/desktop/modules/pm/project/new/newPortfolioController',
                    width: 600,
                    data: {
                        portFolioMap: portFolioMap,
                        portfolioObject: portfolio,
                        modeofObject: mode
                    },
                    buttons: [
                        {text: mode == 'NEW' ? 'Create' : 'Update', broadcast: 'app.portfolio.new'}
                    ],
                    callback: function () {
                        loadPortfolios();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadPortfolios() {
                vm.portfolios = [];
                vm.loadingPortfolios = true;
                ProjectService.getAllPageablePortfolios(vm.pageable).then(
                    function (data) {
                        vm.portfolios = data;
                        vm.loadingPortfolios = false;
                        angular.forEach(vm.portfolios.content, function (portfolio) {
                            portfolio.loading = true;
                            portfolio.projects = [];
                            vm.projectIds = [];
                            portFolioMap.put(portfolio.name, portfolio);
                            ProjectService.findProjectByPortfolio(portfolio.id).then(
                                function (projects) {
                                    portfolio.loading = false;
                                    portfolio.projects = projects;
                                    CommonService.getPersonReferences(portfolio.projects, 'projectOwner');
                                    loadProjects(projects);
                                }
                            )
                        });
                    })
            }

            function nextPage() {
                if (vm.portfolios.last != true) {
                    vm.pageable.page++;
                    loadPortfolios();
                }
            }

            function previousPage() {
                if (vm.portfolios.first != true) {
                    vm.pageable.page--;
                    loadPortfolios();
                }
            }

            function loadProjects(projects) {
                vm.projectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                $application.selectedProject = null;
                vm.loadingProjects = false;
                angular.forEach(projects, function (project) {
                    vm.projectIds.push(project.id);
                });
                angular.forEach(vm.projectAttributes, function (materialAttribute) {
                    if (materialAttribute.id != null && materialAttribute.id != "" && materialAttribute.id != 0) {
                        vm.attributeIds.push(materialAttribute.id);
                    }
                });
                angular.forEach(vm.requiredProjectAttributes, function (materialAttribute) {
                    if (materialAttribute.id != null && materialAttribute.id != "" && materialAttribute.id != 0) {
                        vm.requiredAttributeIds.push(materialAttribute.id);
                    }
                });
                if (vm.projectIds.length > 0 && vm.attributeIds.length > 0) {
                    ProjectService.getAttributesByProjectIdAndAttributeId(vm.projectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.projectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(projects, function (item) {
                                var attributes = [];

                                var projectAttributes = vm.selectedObjectAttributes[item.id];
                                if (projectAttributes != null && projectAttributes != undefined) {
                                    attributes = attributes.concat(projectAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.objectAttachments = data;
                                                        item[attributeName] = vm.objectAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }
                if (vm.projectIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    ProjectService.getAttributesByProjectIdAndAttributeId(vm.projectIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.selectedRequiredAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.requiredProjectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(projects, function (item) {
                                var attributes = [];

                                var projectAttributes = vm.selectedRequiredAttributes[item.id];
                                if (projectAttributes != null && projectAttributes != undefined) {
                                    attributes = attributes.concat(projectAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.objectAttachments = data;
                                                        item[attributeName] = vm.objectAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }
            }

            function showProjectAttributes() {
                var projectAttribute = angular.copy(vm.projectAttributes);
                var options = {
                    title: 'Project Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: projectAttribute,
                        attributesMode: 'PROJECT'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.projectAttributes = result;
                        $window.localStorage.setItem("projectAttributes", JSON.stringify(vm.projectAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributesFromShowAttributes();
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAttributesFromShowAttributes() {
                ProjectService.getProjects().then(
                    function (data) {
                        vm.projects = data;
                        CommonService.getPersonReferences(vm.projects.content, 'projectOwner');
                        loadProjects(vm.projects.content);
                        loadRequiredProjectAttributes();
                    });
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("projectAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function showImage(attribute) {
                var modal = document.getElementById('homeModal');
                var modalImg = document.getElementById('homeImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('myModal1');
                var modalImg = document.getElementById('img02');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function removeAttribute(att) {
                vm.projectAttributes.remove(att);
                $window.localStorage.setItem("projectAttributes", JSON.stringify(vm.projectAttributes));
            }

            function loadRequiredProjectAttributes() {
                ProjectService.getRequiredProjectAttributes("PROJECT").then(
                    function (data) {
                        vm.requiredProjectAttributes = data;
                        loadPortfolios();
                    }
                )
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function lockProject(project) {
                if (project.locked == false) {
                    project.locked = true;
                    ProjectService.updateProject(project).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Project locked successfully");
                        }
                    )
                } else {
                    project.locked = false;
                    ProjectService.updateProject(project).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Project unlocked successfully");
                        }
                    )
                }
            }

            function checkJson() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("projectAttributes"));
                } else {
                    var setAttributes = null;
                }

                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.objectIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("projectAttributes", "");
                                vm.projectAttributes = setAttributes
                            } else {
                                vm.projectAttributes = setAttributes;
                            }

                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                loadRequiredProjectAttributes();
                            }
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        loadRequiredProjectAttributes();
                    }
                }
            }

            function loadCurrencies() {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );
            }

            $scope.$on('$viewContentLoaded', function () {
                loadCurrencies();
                $application.homeLoaded = true;
                checkJson();
                $timeout(function () {
                    window.$("#appview").show();
                }, 500);
            });
        }
    }
)
;