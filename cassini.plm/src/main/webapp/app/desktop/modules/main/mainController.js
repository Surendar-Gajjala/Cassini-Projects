define(
    [
        'app/desktop/modules/main/main.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/themeSwitcherController',
        'app/desktop/modules/main/plmDirectives',
        'app/desktop/modules/main/searchBarController',
        'app/desktop/modules/main/quickAccessController',
        'app/desktop/modules/main/commandCenterController',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/filters/filters',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/forgeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/shared/comments/commentsButtonDirective',
        'app/desktop/modules/shared/comments/commentsBtnDirective',
        'app/desktop/modules/shared/tags/tagsBtnDirective',
        'app/desktop/modules/shared/tags/tagsButtonDirective',
        'app/desktop/directives/no-results/noResultsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/select/objectSelectionController',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/pluginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/appDetailsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService',
        'app/assets/js/d3.min',
        'app/assets/js/select2.min',
        'app/shared/services/core/recentlyVisitedService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/specificationsService',
        'app/desktop/modules/securityPermission/securityPermissionService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/desktop/modules/main/print/printController',
        'app/desktop/modules/main/common/commonController',
        'app/desktop/modules/main/security/objectrightsController',
        'app/desktop/directives/person-avatar/personAvatarDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService',
        'app/shared/services/core/documentService'
    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $q, $rootScope, $http, $timeout, $interval, $state, $location, $application, $translate, $window,
                                LoginService, ForgeService, DialogService, CommonService, ObjectAttributeService, DocumentService, SecurityPermissionService,
                                ItemService, PluginService, CustomObjectTypeService, AppDetailsService, $sce, PersonGroupService, PreferenceService, LicenseService) {
            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home',
                description: "",
                showDetails: true
            };

            $rootScope.breadCrumb = {
                project: "",
                activity: "",
                task: ""
            };

            $rootScope.mainLoaded = false;
            $rootScope.$application = $application;

            $application.urlParams = getQueryParams();

            $rootScope.currentLang = $translate.proposedLanguage() || $translate.use();

            moment.tz.setDefault("Asia/Kolkata");
            window.moment = moment;

            var vm = this;
            vm.$application = $application;

            vm.viewInfo = $rootScope.viewInfo;
            vm.showHomePage = showHomePage;
            $rootScope.helpDefaultView = "app/help/Overview/Overview.html";
            vm.currentVariant = {
                weight: null,
                price: null,
                deliveryDate: null
            };

            vm.comments = {
                show: false,
                objectType: null,
                objectId: null,
                commentCount: 0
            };

            vm.tags = {
                show: false,
                objectType: null,
                object: null,
                tagsCount: 0
            };

            $rootScope.clipBoardDeliverables = {
                itemIds: [],
                specIds: [],
                requirementIds: [],
                glossaryIds: []
            };
            $rootScope.showGantt = false;

            function showHomePage() {
                $state.go('app.home');
                $rootScope.hideSidePanel('left');
            }

            $rootScope.notification = {
                class: 'fa-check',
                type: "alert-success",
                message: "",
                undo: false,
                undoType: null
            };

            vm.user = {
                name: ""
            };

            var sessionCheckPromise = null;
            var sessionCheckErrorCount = 0;
            var permissionsMap = new Hashtable();

            vm.logout = logout;
            vm.feedback = feedback;
            vm.about = about;
            vm.showProfile = showProfile;
            vm.showFoldersTree = showFoldersTree;
            vm.stopPropagation = stopPropagation;
            vm.systemInfo = systemInfo;
            vm.currentState = $state.current.name;
            vm.lifecyclePhases = [];
            vm.sidenavExtensions = [];
            var parsed = angular.element("<div></div>");
            var logHistory = parsed.html($translate.instant('LOG_HISTORY_TITLE')).html();
            var accessLog = parsed.html($translate.instant('ACCESS_LOG')).html();
            $rootScope.noPermission = parsed.html($translate.instant('NO_PERMISSION_PERFORM')).html();

            var foldersTitle = $translate.instant("FOLDERS_TITLE");

            function showFoldersTree() {
                hideSideNavbar();
                var options = {
                    side: 'right',
                    title: foldersTitle,
                    template: 'app/desktop/modules/home/widgets/folders/foldersWidgetView.jsp',
                    controller: 'FoldersWidgetController as foldersVm',
                    resolve: 'app/desktop/modules/home/widgets/folders/foldersWidgetController',
                    width: 450,
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.systemInformation = null;

            function stopPropagation(event) {
                event.stopPropagation();
            }

            var usedSpace = parsed.html($translate.instant("DATA_USED_SPACE")).html();
            var avalableSpace = parsed.html($translate.instant("DATA_AVAILABLE_SPACE")).html();
            var totalSpace = parsed.html($translate.instant("DATA_TOTAL_SPACE")).html();

            function systemInfo() {
                $rootScope.showBusyIndicator();
                $state.go('app.about.main');
                $rootScope.hideBusyIndicator();
            }

            $rootScope.closeModal = closeModal;
            function closeModal() {
                $('#myModal').modal('hide');
            }

            vm.userPreferences = null;
            $rootScope.applicationDateSelectFormat = "DD/MM/YYYY";
            $rootScope.applicationDateSelectFormatDivider = "/";
            $rootScope.ganttDateFormat = "%d/%m/%Y";
            $rootScope.ganttDateSelectFormat = "%d %M";
            function loadUserPreference(loginId) {

                LoginService.getUserPreference(loginId).then(
                    function (data) {
                        vm.userPreferences = data;
                        if (vm.userPreferences != null) {
                            if (vm.userPreferences.userTheme != null && vm.userPreferences.userTheme != "") {
                                $rootScope.switchTheme(vm.userPreferences.userTheme, 'refresh');
                            }
                            if ($rootScope.loginPersonDetails.person.defaultGroup == 1 && vm.userPreferences.userTheme == null) {
                                vm.userPreferences.userTheme = "Default";
                            }
                            if (vm.userPreferences.preferredPage != null && vm.userPreferences.preferredPage != "") {
                                $window.location.href = vm.userPreferences.preferredPage;
                            }
                            if (vm.userPreferences.userDateFormat == null || vm.userPreferences.userDateFormat == "") {
                                $rootScope.applicationDateFormat = vm.systemDateFormat;
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    $rootScope.applicationDateSelectFormat = "DD MM YYYY";
                                    $rootScope.applicationFromDateFormat = "DD MM YYYY, HH:mm:ss";
                                    $rootScope.applicationDateSelectFormatDivider = " ";
                                    $rootScope.ganttDateFormat = "%d %m %Y";
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    $rootScope.applicationDateSelectFormat = "DD.MM.YYYY";
                                    $rootScope.applicationFromDateFormat = "DD.MM.YYYY, HH:mm:ss";
                                    $rootScope.applicationDateSelectFormatDivider = ".";
                                    $rootScope.ganttDateFormat = "%d.%m.%Y";
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    $rootScope.applicationDateSelectFormat = "MM-DD-YYYY";
                                    $rootScope.applicationFromDateFormat = "MM-DD-YYYY, HH:mm:ss";
                                    $rootScope.applicationDateSelectFormatDivider = "-";
                                    $rootScope.ganttDateFormat = "%m-%d-%Y";
                                    $rootScope.ganttDateSelectFormat = "%M %d";
                                } else {
                                    $rootScope.applicationDateSelectFormat = "DD/MM/YYYY";
                                    $rootScope.applicationDateSelectFormatDivider = "/";
                                    $rootScope.ganttDateFormat = "%d/%m/%Y";
                                    $rootScope.ganttDateSelectFormat = "%d %M";
                                }
                            } else {
                                $rootScope.applicationDateFormat = vm.userPreferences.userDateFormat;
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    $rootScope.applicationDateSelectFormat = "DD MM YYYY";
                                    $rootScope.applicationFromDateFormat = "DD MM YYYY, HH:mm:ss";
                                    $rootScope.applicationDateSelectFormatDivider = " ";
                                    $rootScope.ganttDateFormat = "%d %m %Y";
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    $rootScope.applicationDateSelectFormat = "DD.MM.YYYY";
                                    $rootScope.applicationFromDateFormat = "DD.MM.YYYY, HH:mm:ss";
                                    $rootScope.applicationDateSelectFormatDivider = ".";
                                    $rootScope.ganttDateFormat = "%d.%m.%Y";
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    $rootScope.applicationDateSelectFormat = "MM-DD-YYYY";
                                    $rootScope.applicationFromDateFormat = "MM-DD-YYYY, HH:mm:ss";
                                    $rootScope.applicationDateSelectFormatDivider = "-";
                                    $rootScope.ganttDateFormat = "%m-%d-%Y";
                                    $rootScope.ganttDateSelectFormat = "%M %d";
                                } else {
                                    $rootScope.applicationDateSelectFormat = "DD/MM/YYYY";
                                    $rootScope.applicationDateSelectFormatDivider = "/";
                                    $rootScope.ganttDateFormat = "%d/%m/%Y";
                                    $rootScope.ganttDateSelectFormat = "%d %M";
                                }
                            }
                        }
                        else {
                            $rootScope.switchTheme("slategrey", "refresh");
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.getDeviations = getDeviations;
            function getDeviations() {
                $rootScope.varianceType = "Deviation";
                hideSideNavbar();
            }

            vm.getWaivers = getWaivers;
            function getWaivers() {
                $rootScope.varianceType = "Waiver";
                hideSideNavbar();
            }

            vm.loadItems = loadItems;
            function loadItems() {
                $rootScope.allItemsLoad = true;
                hideSideNavbar();
            }

            vm.loadEcos = loadEcos;
            function loadEcos() {
                $rootScope.allEcosLoad = true;
                hideSideNavbar();
            }

            $rootScope.applicationDateFormat = "dd/MM/yyyy, HH:mm:ss";
            $rootScope.applicationFromDateFormat = "DD/MM/YYYY, HH:mm:ss";
            var preferredPageSaved = parsed.html($translate.instant("PREFERRED_PAGE_UPDATED_MESSAGE")).html();
            var licenceExpireToday = parsed.html($translate.instant("YOUR_LICENCE_EXPIRE_TODAY")).html();
            var licenceExpiryMsg = parsed.html($translate.instant("LICENCE_EXPIRY_MSG")).html();
            var licenceExpiredMsg = parsed.html($translate.instant("LICENCE_EXPIRED_MSG")).html();
            var licenceLastExpiredMsg = parsed.html($translate.instant("LICENCE_LAST_EXPIRY_MSG")).html();
            $rootScope.savePreferredPage = savePreferredPage;
            function savePreferredPage() {
                vm.userPreferences.preferredPage = window.location.href;
                vm.userPreferences.login = $rootScope.loginPersonDetails.id;
                LoginService.savePreferredPage(vm.userPreferences).then(
                    function (data) {
                        $rootScope.showSuccessMessage(preferredPageSaved);
                    }, function (error) {
                        console.log(error);
                    }
                );
            }


            function loadDMPermissions() {
                $rootScope.isDmPermissions = false;
                DocumentService.getDocumentFolderPermissions().then(
                    function (data) {
                        vm.dmPermissions = data;
                        if (vm.dmPermissions.length > 0) {
                            angular.forEach(vm.dmPermissions, function (permission) {
                                if (JSON.parse(permission.actions).length > 0 && !$rootScope.isDmPermissions) $rootScope.isDmPermissions = true;
                            });
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            $rootScope.personInfo = null;
            $rootScope.localStorageLogin = null;
            $rootScope.defaultProfile = null;
            $rootScope.widgets = [];
            $rootScope.backupWidgets = [];
            function checkSession() {
                $rootScope.widgets = [];
                $rootScope.backupWidgets = [];
                window.$application.session = null;
                window.$application.login = null;
                window.$application.loadProfileImage = true;
                window.$application.foldersData = null;
                window.$application.defaultRevisionConfiguration = null;
                window.$application.defaultValuesPreferences = new Hashtable;
                $rootScope.personInfo = null;
                window.$application.clipboard = {
                    items: [],
                    files: [],
                    deliverables: {
                        itemIds: [],
                        specIds: [],
                        glossaryIds: [],
                        requirementIds: []
                    }
                };
                $rootScope.loginPersonDetails = null;
                $rootScope.localStorageLogin = null;
                LoginService.current().then(
                    function (session) {
                        $application.validateLicense = $rootScope.validateLicense = true;
                        if ((session == null || session == "") || !$application.validateLicense) {
                            $state.go('login');
                            $rootScope.sessionStatus = false;
                        }
                        else {
                            window.$application.session = session;
                            window.$application.login = session.login;
                            window.$application.loadProfileImage = true;
                            vm.user = session.login;

                            $window.localStorage.setItem('token', session.accessToken);
                            $window.localStorage.setItem('refreshToken', session.refreshToken);
                            $rootScope.personInfo = session.login.person;
                            $rootScope.isAdmin = false;
                            $rootScope.personGroups1 = session.login.groups;
                            $rootScope.loginPersonDetails = session.login;
                            angular.forEach($rootScope.personGroups1, function (group) {
                                if ($rootScope.loginPersonDetails.person.defaultGroup == group.groupId) {
                                    $rootScope.loginPersonDetails.defaultGroup = group;
                                }
                            });
                            $rootScope.loginPersonDetails.defaultGroup.groupSecurityPermissions = $rootScope.loginPersonDetails.groupSecurityPermissions;
                            if ($rootScope.loginPersonDetails.loginSecurityPermissions.length > 0) {
                                angular.forEach($rootScope.loginPersonDetails.loginSecurityPermissions, function (loginSecurityPermission) {
                                    $rootScope.loginPersonDetails.defaultGroup.groupSecurityPermissions.push(loginSecurityPermission);
                                });
                            }
                            if (session.login.isAdmin == true) {
                                isAdminWidgets();
                            }
                            else {
                                addHomeWidgets(session.login.defaultGroup.groupSecurityPermissions);
                            }
                            $rootScope.sessionStatus = true;
                            $window.localStorage.setItem('validateLicense', true);
                            PersonGroupService.getPersonGrpById($rootScope.loginPersonDetails.person.defaultGroup).then(
                                function (data) {
                                    if (data.groupId == 1) {
                                        $rootScope.isAdmin = true;
                                    }
                                    $rootScope.defaultProfile = data.profile;
                                    loadProfilesData();

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );

                            loadCustomObjectTypes();
                            initMain();
                            loadSessionTime();
                            loadPreferences();
                            loadPasswordStrength();
                            getCurrencies();
                            loadUserPreference(session.login.id);
                            hidePreloader();
                            buildMaps(session);
                            checkForgeEnable();
                            getFileSystemPath();
                            loadDefaultValuePreferences();
                            if (!$rootScope.hasPermission('document', 'view')) loadDMPermissions();
                            // localStorage.clear();
                            localStorage.setItem("local_storage_login", JSON.stringify(session));
                            if (localStorage.getItem('local_storage_login') != null) {
                                $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                            }

                            CommonService.initialize();
                            loadPlugins();
                            loadViewPermissions();
                            //Checks If Person has Email Or Not
                            if (vm.user.person.email == null || vm.user.person.email == "" || !vm.user.person.emailVerified) {
                                LoginService.logout().then(
                                    function (success) {
                                        $http.defaults.headers.common['Authorization'] = undefined;
                                        $http.defaults.headers.common['X-Refresh-Token'] = undefined;
                                        $window.localStorage.removeItem('token');
                                        $window.localStorage.removeItem('refreshToken');
                                        $rootScope.sessionStatus = false;
                                        $state.go('login', {}, {reload: true});
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                );
                            }
                            //Person has Email Redirect to HomePage
                            else {

                                if ($application.homeLoaded == false) {
                                    $timeout(function () {
                                        if (vm.userPreferences != null) {
                                            if (vm.userPreferences.userTheme == null || vm.userPreferences.userTheme == "") {
                                                changeTheme(vm.systemTheme);

                                            }
                                            else {
                                                changeTheme(vm.userPreferences.userTheme);

                                            }
                                        } else {
                                            changeTheme(vm.systemTheme);
                                        }
                                        //$state.go('app.home');
                                        $rootScope.sessionStatus = true;
                                    }, 1000)

                                } else {
                                    changeTheme(vm.systemTheme);
                                }
                            }
                            generateKistersToken();
                            if ($rootScope.loginPersonDetails.isSuperUser) {
                                loadLicenseDetails();
                            }
                        }
                    }, function (error) {
                        $state.go('login', {}, {reload: true});
                    }
                );
            }

            function loadLicenseDetails() {
                LicenseService.getLicense().then(
                    function (data) {
                        vm.licenceDetails = data;
                        var gracePeriod = vm.licenceDetails.gracePeriod;
                        if (vm.licenceDetails.gracePeriod == null || vm.licenceDetails.gracePeriod == 0) {
                            gracePeriod = 10;
                        }
                        var today = moment(new Date());
                        var todayStr = today.format($rootScope.applicationDateSelectFormat);
                        var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                        var expiryDate = moment(vm.licenceDetails.expirationDate, $rootScope.applicationDateSelectFormat);
                        if (expiryDate.isSame(todayDate) || todayDate.isBefore(expiryDate)) {
                            var diffTime = Math.abs(expiryDate - todayDate);
                            var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                            if (diffDays !== null && diffDays !== "" && diffDays !== undefined && diffDays <= gracePeriod) {
                                if (diffDays == 0) {
                                    $rootScope.showInfoMessage(licenceExpireToday)
                                } else {
                                    $rootScope.showInfoMessage(licenceExpiryMsg.format(diffDays))
                                }
                            }
                        } else {
                            var addedGracePeriod = expiryDate.add(gracePeriod, 'days').format($rootScope.applicationDateSelectFormat);
                            var gracePeriodDate = moment(addedGracePeriod, $rootScope.applicationDateSelectFormat);
                            var diffTime = Math.abs(gracePeriodDate - todayDate);
                            var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                            if (diffDays !== null && diffDays !== "" && diffDays !== undefined) {
                                if (diffDays > 0) {
                                    $rootScope.showWarningMessage(licenceExpiredMsg.format(diffDays))
                                } else {
                                    $rootScope.showWarningMessage(licenceLastExpiredMsg);
                                }
                            }
                        }
                        window.$application.expiryMsgShown = true;
                    }
                )
            }

            function isAdminWidgets() {
                SecurityPermissionService.getAllSecurityPermissionsByObjectType("homewidget").then(
                    function (data) {
                        $rootScope.widgets = data;
                        $rootScope.backupWidgets = data;
                    }
                );
                /*$http.get('app/desktop/modules/main/widgets.json').success(function (data) {
                 $rootScope.widgets = data;
                 $rootScope.backupWidgets = data;
                 });*/
            }

            $rootScope.addHomeWidgets = addHomeWidgets;
            function addHomeWidgets(widgetPermissions) {
                $rootScope.widgets = [];
                $rootScope.backupWidgets = [];
                angular.forEach(widgetPermissions, function (widget) {
                    if (widget.objectType != null && widget.objectType != "") {
                        if (widget.objectType == "homewidget") {
                            widget.checked = false;
                            $rootScope.widgets.push(widget);
                            $rootScope.backupWidgets.push(widget);
                        }

                    }
                });

            }

            var modelSelect = document.getElementById('modelSelect');
            $rootScope.kisterToken = "";
            $rootScope.file_system_path = "";
            function generateKistersToken() {
                var array = new Uint32Array(10);
                var crypto = prefixCrypto(window);
                crypto.getRandomValues(array);
                token = array[0];
                $rootScope.kisterToken = token;
                //console.log($rootScope.kisterToken);
            }

            function getFileSystemPath() {
                AppDetailsService.getFileSystemPath().then(
                    function (data) {
                        $rootScope.file_system_path = data;
                    }
                )
            }

            function prefixCrypto(win) {
                return win.crypto || win.msCrypto;
            }

            $scope.toggleAdmin = function () {
                $('#user-info').toggle();
                $('#user-switch-roles').hide();
                $rootScope.hideSidePanel('right');
            };


            $scope.toggleRole = function () {
                $('#user-switch-roles').toggle();
                $('#user-info').hide();
                $rootScope.hideSidePanel('right');
            };

            $(document).click(function () {
                $(".uib-dropdown-menu").hide();
            });

            function feedback() {
                $state.go('app.help.feedback');
            }

            function about() {
                $state.go('app.about.main');
            }

            vm.help = help;
            function help() {
                $state.go('app.help.main')
            }

            function showProfile(login) {
                //$state.go('app.admin.logindetails', {loginId: login.id});
                $state.go('app.userdetails.activity', {userId: login.person.id});
            }


            function logout() {
                DialogService.confirmLogout(function (yes) {
                    if (yes == true) {
                        LoginService.logout().then(
                            function (success) {
                                $interval.cancel(sessionCheckPromise);
                                $http.defaults.headers.common['Authorization'] = undefined;
                                $http.defaults.headers.common['X-Refresh-Token'] = undefined;
                                $window.localStorage.removeItem('token');
                                $window.localStorage.removeItem('refreshToken');
                                $rootScope.$broadcast("app.notifications.logout");
                                $rootScope.sharedUrl = null;
                                $rootScope.sessionStatus = false;
                                $rootScope.companyImage = null;
                                var lang = $window.localStorage.getItem("language");
                                $translate.use(lang);
                                $translate.refresh();
                                $rootScope.widgets = [];
                                $rootScope.backupWidgets = [];
                                $window.localStorage.setItem('validateLicense', false);
                                $state.go('login', {}, {reload: true});
                            },

                            function (error) {
                                $interval.cancel(sessionCheckPromise);
                                console.error(error);
                            }
                        );
                    }
                });
            }


            function buildMaps(session) {
                var permissions = session.login.permissionsMap;
                angular.forEach(permissions, function (value, key) {
                    buildPermissionsMap(value);
                });

            }

            var profileNavigation = [];
            $rootScope.loadProfilesData = loadProfilesData;
            function loadProfilesData() {
                profileNavigation = [];
                if ($rootScope.defaultProfile != null) {
                    profileNavigation = $rootScope.defaultProfile.profileData;
                    angular.forEach(profileNavigation, function (nav) {
                        var nav1 = nav.split('.')[0];
                        nav1 = nav1 + ".menu";
                        if (profileNavigation.indexOf(nav1) == -1) {
                            profileNavigation.push(nav1);
                        }
                    })
                }
            }

            function buildPermissionsMap(permissions) {
                angular.forEach(permissions, function (permission) {
                    permissionsMap.put(permission.id, permission);
                });
            }

            function getPermission(permission) {
                var has = false;
                if (permissionsMap.get('permission.admin.all') != null) {
                    has = true;
                }
                var p = permissionsMap.get(permission);
                if (p != null && p != undefined) {
                    has = true;
                }

                if (!has && permission.endsWith('.*')) {
                    var keys = permissionsMap.keys();
                    for (var i = 0; i < keys.length; i++) {
                        var p = keys[i];
                        permission = permission.replace('.*', '');
                        if (p.startsWith(permission)) {
                            has = true;
                            break;
                        }
                    }
                }

                return has;
            }

            function getCurrencies() {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        $application.currencies = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            /*$rootScope.hasPermission = function (permission) {
             var has = false;
             if (permission != null && permission != undefined) {
             if (getPermission(permission)) {
             has = true;
             }
             }
             return has;
             };*/

            $rootScope.isProfileMenu = function (menuId) {
                if ($rootScope.defaultProfile != null) {
                    if (profileNavigation.indexOf(menuId) != -1) {
                        return true;
                    } else {
                        return false;
                    }
                } else if ($rootScope.loginPersonDetails != null && $rootScope.loginPersonDetails.isAdmin == true) {
                    return true;
                } else if ($rootScope.defaultProfile == null) {
                    return false;
                }
            };

            $rootScope.closeNotification = function () {
                hideNotificationPanel();
                $rootScope.notification.type = null;
                $rootScope.notification.message = null;
                $rootScope.notification.undo = false;
                $rootScope.notification.undoType = null;
            };

            $rootScope.showNotification = function (message, type, undo, undoType) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                if (undo == true) {
                    $rootScope.notification.undo = true;
                }

                if (undoType != undefined) {
                    $rootScope.notification.undoType = undoType;
                }
                showNotificationPanel();
            };

            $rootScope.showSuccessMessage = function (message, undo, undoType) {
                $rootScope.notification.class = 'fa-check';
                $rootScope.showNotification(message, 'alert-success', undo, undoType);
                var time = 5000;
                if (undo == true) {
                    time = 11000;
                }
                $timeout(function () {
                    $rootScope.closeNotification();
                }, time);
            };

            $rootScope.showErrorMessage = function (message) {
                $rootScope.notification.class = 'fa-ban';
                $rootScope.showNotification(message, 'alert-danger');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000);
            };

            $rootScope.showWarningMessage = function (message) {
                $rootScope.notification.message = message;
                $rootScope.notification.class = 'fa-warning';
                $rootScope.showNotificationWarning(message, 'alert-warning');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000);
            };

            $rootScope.showNotificationWarning = function (message, type, undo, undoType) {
                $rootScope.notification.type = type;
                if (undo == true) {
                    $rootScope.notification.undo = true;
                }

                if (undoType != undefined) {
                    $rootScope.notification.undoType = undoType;
                }
                $timeout(function () {
                    showNotificationPanel();
                }, 500);
            };

            $rootScope.showInfoMessage = function (message) {
                $rootScope.notification.class = 'fa-info-circle';
                $rootScope.showNotification(message, 'alert-info');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000);
            };

            $rootScope.undo = function () {
                if ($rootScope.notification.undoType == "SPECIFICATION") {
                    $rootScope.undoCopiedSpecFiles();
                } else if ($rootScope.notification.undoType == "REQUIREMENT") {
                    $rootScope.undoCopiedReqFiles();
                } else if ($rootScope.notification.undoType == "OBJECT") {
                    $rootScope.undoCopiedObjectFiles();
                } else if ($rootScope.notification.undoType == "CHANGE") {
                    $rootScope.undoCopiedItemFiles();
                } else if ($rootScope.notification.undoType == "PROJECTDELIVERABLE") {
                    $rootScope.undoProjectDeliverables();
                } else if ($rootScope.notification.undoType == "ACTIVITYDELIVERABLE") {
                    $rootScope.undoActivityDeliverables();
                } else if ($rootScope.notification.undoType == "TASKDELIVERABLE") {
                    $rootScope.undoTaskDeliverables();
                } else if ($rootScope.notification.undoType == "BOM") {
                    $rootScope.undoCopiedBomItems();
                }
            };


            $rootScope.showBusyIndicator = function (parent) {
                var w = null;
                var h = null;
                if (parent != null && parent != undefined) {
                    var pos = $(parent).offset();
                    w = $(parent).outerWidth();
                    h = $(parent).outerHeight();

                    $('#busy-indicator').css({top: pos.top, left: pos.left, width: w, height: h})
                }
                else {
                    w = $(window).outerWidth();
                    h = $(window).outerHeight();
                    $('#busy-indicator').css({top: 0, left: 0, width: w, height: h})
                }
                $('#busy-indicator').show();
            };

            $rootScope.hideBusyIndicator = function () {
                $('#busy-indicator').hide();
            };

            $rootScope.showComments = function (objectType, objectId, commentCount) {
                vm.comments.show = true;
                vm.comments.objectType = objectType;
                vm.comments.objectId = objectId;
                vm.comments.commentCount = commentCount;
            };

            $rootScope.showTags = function (objectType, object, tagsCount) {
                vm.tags.show = true;
                vm.tags.objectType = objectType;
                vm.tags.object = object;
                vm.tags.tagsCount = tagsCount;
            };

            $rootScope.setLifecyclePhases = function (phases) {
                vm.lifecyclePhases = phases;
            };

            $rootScope.version = null;
            $rootScope.date = null;
            $http.get('about.json').success(function (data) {
                $rootScope.version = data.version;
                $rootScope.date = data.date;
            });

            $rootScope.sessionTimeout = function () {
                if ($rootScope.sessionStatus == true) {
                    LoginService.logout().then(
                        function (data) {

                        }
                    );
                    $state.go('login');

                }
            };

            function initSessionCheck() {
                sessionCheckPromise = $interval(function () {
                    LoginService.current().then(
                        function (session) {
                            sessionCheckErrorCount = 0;
                            if (session == null || session == "") {
                                if (sessionCheckPromise != null) {
                                    $interval.cancel(sessionCheckPromise);
                                }
                                $state.go('login');
                            }
                        },
                        function (error) {
                            sessionCheckErrorCount++;
                            if (sessionCheckErrorCount == 3 && sessionCheckPromise != null) {
                                $interval.cancel(sessionCheckPromise);
                                $state.go('login');
                            }
                        }
                    );
                }, 10 * 60 * 1000);
            }

            vm.breadcrumbView = breadcrumbView;

            var passwordStrength = /^[\s\S]{8,32}$/,
                upper = /[A-Z]/,
                lower = /[a-z]/,
                number = /[0-9]/,
                special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;
            $rootScope.loadPasswordStrength = loadPasswordStrength;
            function loadPasswordStrength() {
                var context = 'SYSTEM';
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        angular.forEach(data, function (prop) {
                            if (prop.preferenceKey == 'SYSTEM.PASSWORD') {
                                $rootScope.passwordProperties = JSON.parse(prop.jsonValue);
                            }
                        });
                    }, function (error) {
                        console.log(error);
                    }
                )
            }

            vm.passwordMinLength = parsed.html($translate.instant("PASSWORD_MINIMUM_LENGTH")).html();
            vm.passwordNumbersOnly = parsed.html($translate.instant("PASSWORD_NUMBERS_ONLY")).html();
            vm.passwordNumbersAndSpecialChar = parsed.html($translate.instant("PASSWORD_NUMBERS_AND_SPECIAL_CHARACTERS")).html();
            vm.passwordCaseSensitivie = parsed.html($translate.instant("PASSWORD_CASE_SENSITIVIE")).html();
            vm.password = parsed.html($translate.instant("PASSWORD")).html();
            var characters = parsed.html($translate.instant("CHARACTERS")).html();
            $rootScope.clickToShowDetails = parsed.html($translate.instant("CLICK_TO_SHOW_DETAILS")).html();

            $rootScope.newPasswordInfo = '';
            $rootScope.loadPasswordMessage = loadPasswordMessage;
            function loadPasswordMessage() {
                $rootScope.newPasswordInfo = '';
                $rootScope.passwordInformation = "";
                if ($rootScope.passwordProperties.minLen != null) {
                    $rootScope.passwordInformation += "<br>";
                    $rootScope.passwordInformation += "\u2022 " + vm.passwordMinLength + $rootScope.passwordProperties.minLen + ' ' + characters;
                }
                if ($rootScope.passwordProperties.specialChar != null) {
                    if ($rootScope.passwordProperties.specialChar == 'Nb') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordNumbersOnly;
                    }
                    if ($rootScope.passwordProperties.specialChar == 'Nbs') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordNumbersAndSpecialChar;
                    }
                }
                if ($rootScope.passwordProperties.cases != null) {
                    if ($rootScope.passwordProperties.cases == 'Yes') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordCaseSensitivie;
                    }
                }
                $rootScope.newPasswordInfo = $sce.trustAsHtml($rootScope.passwordInformation)
            }

            $rootScope.passwordStrengthValid = passwordStrengthValid;
            $rootScope.validPassword = true;
            function passwordStrengthValid() {
                $rootScope.validPassword = false;
                var password = document.getElementsByName('password')[0].value;
                var meter = document.getElementById('password-strength-meter');
                var score = null;
                if ($rootScope.passwordProperties != null) {
                    $rootScope.loadPasswordMessage();
                    var minLength = parseInt($rootScope.passwordProperties.minLen);
                    var specialChar = $rootScope.passwordProperties.specialChar;
                    var cases = $rootScope.passwordProperties.cases;
                    score = evalPasswordScore(minLength, specialChar, cases, password);
                } else {
                    score = 4;
                    $rootScope.passwordInformation = vm.password;
                }
                if (score == 4) {
                    $rootScope.validPassword = true;
                }
                if (meter != null) meter.value = score;
            }

            vm.passwordScore = null;
            function evalPasswordScore(minLength, specialChar, cases, password) {
                var len = password.length;
                var score = 0;
                if (minLength == null && (specialChar == null || specialChar == 'No') && (cases == null || cases == 'No')) {
                    score = 4;
                } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'No' || cases == null)) {
                    if (len >= minLength) score = 4;
                } else if (minLength != null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password) && special.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password)) {
                        score += 1;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 1;
                    }
                } else if (minLength != null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password) && special.test(password)) {
                        score += 1;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 1;
                    }
                } else if (minLength == null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
                    if (number.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
                    if (number.test(password) && special.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
                    if (upper.test(password) && lower.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
                    if (number.test(password)) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                } else if (minLength == null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
                    if (number.test(password) && special.test(password)) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                }
                vm.passwordScore = score;
                return score;
            }

            function breadcrumbView(breadcrumb) {
                if (breadcrumb.objectType == "PROJECT") {
                    $state.go('app.pm.project.details', {projectId: breadcrumb.id})
                }
                if (breadcrumb.objectType == "PROJECTACTIVITY") {
                    $state.go('app.pm.project.activity.details', {activityId: breadcrumb.id})
                }

            }


            function getQueryParams() {
                var search = $window.location.search.substring(1);

                if (search != null && search != "")
                    return JSON.parse('{"' + decodeURI(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}');
                else
                    return {};
            }

            function initSideNavigation() {
                // Toggle Left Menu
                $('#sideNavigation .nav-parent > a').on('click', function () {
                    var parent = jQuery(this).parent();
                    var sub = parent.find('> ul');

                    // Dropdown works only when leftpanel is not collapsed
                    if (!jQuery('body').hasClass('leftpanel-collapsed')) {
                        if (sub.is(':visible')) {
                            sub.slideUp(200, function () {
                                parent.removeClass('nav-active');
                                jQuery('.mainpanel').css({height: ''});
                            });
                        } else {
                            closeVisibleSubMenu();
                            parent.addClass('nav-active');
                            sub.slideDown(200, function () {
                            });
                        }
                    }
                    return false;
                });
            }

            function closeVisibleSubMenu() {
                jQuery('#sideNavigation .nav-parent').each(function () {
                    var t = jQuery(this);
                    if (t.hasClass('nav-active')) {
                        t.find('> ul').slideUp(200, function () {
                            t.removeClass('nav-active');
                        });
                    }
                });
            }

            $scope.toggleSideNav = function (event) {
                $rootScope.hideSidePanel('left');

                var sideNavigation = $('#sideNavigation');
                if (sideNavigation.hasClass('visible')) {
                    sideNavigation.animate({"left": "-290px"}, "500").removeClass('visible');
                } else {
                    sideNavigation.animate({"left": "0px"}, "500").addClass('visible');
                }

                var navShortcutsPanel = $('#navShortcutsPanel');
                if (navShortcutsPanel.hasClass('visible')) {
                    navShortcutsPanel.animate({"left": "-290px"}, "500").removeClass('visible');
                } else {
                    navShortcutsPanel.animate({"left": "0px"}, "500").addClass('visible');
                }

                if (event != null && event != undefined) {
                    event.stopPropagation();
                }
            };

            function hideSideNavbar() {
                var sideNavigation = $('#sideNavigation');
                if (sideNavigation.hasClass('visible')) {
                    sideNavigation.animate({"left": "-290px"}, "500").removeClass('visible');
                }

                var navShortcutsPanel = $('#navShortcutsPanel');
                if (navShortcutsPanel.hasClass('visible')) {
                    navShortcutsPanel.animate({"left": "-290px"}, "500").removeClass('visible');
                }
            }

            function openLaunchpad() {
                var launchpad = $("#launchpad");
                launchpad.addClass("shown start");
                launchpad.find("nav").addClass("scale-down");
            }

            function closeLaunchpad() {
                var launchpad = $("#launchpad");
                var content = launchpad.find(".content"),
                    nav = content.find("nav");

                if (content.is(e.target) || nav.is(e.target)) {
                    close();
                }

                launchpad
                    .removeClass("start")
                    .addClass("end");
                launchpad.find("nav")
                    .removeClass("scale-down")
                    .addClass("scale-up");
                setTimeout(function () {
                    launchpad.removeClass("shown end");
                    launchpad.find("nav").removeClass("scale-up");
                }, 350);
            }

            //Double Shift key press event
            $('body').unbind().dbKeypress(16, {
                eventType: 'keyup',
                callback: function () {
                    if (!$rootScope.loginPersonDetails.external) {
                        $scope.toggleSideNav();
                    }

                }
            });

            function initEvents() {
                $('body').on('click', function () {
                    hideSideNavbar();
                });

                $('#sideNavigation .nav-parent  a').on('click', function (event) {
                    event.stopPropagation();
                });

                //ESC key press event
                $(document).on('keyup', function (evt) {
                    if (evt.keyCode == 27) {
                        hideSideNavbar();
                    }
                });
            }

            $rootScope.AppForgeEnable = false;
            function checkForgeEnable() {
                ForgeService.isForgeActive().then(
                    function (data) {
                        $rootScope.AppForgeEnable = data;
                    }, function (e) {
                        console.log(e.message);
                    }
                );
            }

            function loadSessionTime() {
                CommonService.getSessionTime().then(
                    function (data) {
                        $rootScope.sessionTime = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function updateBomSequence() {
                ItemService.updateBomSequence().then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            $rootScope.formats = ["dd/MM/yyyy, HH:mm:ss", "dd MM yyyy HH:mm:ss", "dd.MM.yyyy, HH:mm:ss", "MM-dd-yyyy, HH:mm:ss"];
            vm.systemTheme = null;
            vm.systemDateFormat = null;
            function loadPreferences() {
                CommonService.getPreferences().then(
                    function (data) {
                        angular.forEach(data, function (item) {
                            if (item.preferenceKey == "SYSTEM.LOGO") {
                                if (item.customLogo != null) {
                                    $rootScope.companyImage = "api/common/persons/" + item.id + "/customImageAttribute/download?" + new Date().getTime();
                                }
                            } else if (item.preferenceKey == "SYSTEM.THEME") {
                                vm.systemTheme = item.stringValue;
                                //changeTheme(item.stringValue);
                            } else if (item.preferenceKey == "SYSTEM.DATE.FORMAT") {
                                vm.systemDateFormat = item.stringValue;
                            } else if (item.preferenceKey == "SYSTEM.DEFAULT.REVISION.CONFIGURATION") {
                                if (item.jsonValue != null && item.jsonValue != "") {
                                    window.$application.defaultRevisionConfiguration = JSON.parse(item.jsonValue);
                                }
                            }
                        })

                    }
                );
                PreferenceService.getPreferenceByKey("SYSTEM.DEFAULT.REVISION.CONFIGURATION").then(
                    function (data) {
                        if (data != "" && data != null && data.jsonValue != null && data.jsonValue != "") {
                            window.$application.defaultRevisionConfiguration = JSON.parse(data.jsonValue);
                        }
                    }
                );
                loadHolidays();
            }

            function loadDefaultValuePreferences() {
                PreferenceService.getPreferencesByContext("DEFAULT").then(
                    function (data) {
                        vm.defaultPreferences = data;
                        angular.forEach(vm.defaultPreferences, function (preference) {
                            window.$application.defaultValuesPreferences.put(preference.preferenceKey, preference)
                        })
                    }
                )
            }

            $rootScope.loadHolidays = loadHolidays;
            function loadHolidays() {
                loadWorkingDays();
                loadHolidaysList();
            }

            function loadWorkingDays() {
                var key = "APPLICATION.WORKING_DAYS";
                PreferenceService.getPreferenceByKey(key).then(
                    function (data) {
                        if (data != "") $rootScope.workingDays = data.integerValue;
                    }
                )
            }

            function loadHolidaysList() {
                $rootScope.holidays = [];
                var key = "APPLICATION.HOLIDAY_LIST";
                PreferenceService.getPreferenceByKey(key).then(
                    function (data) {
                        if (data != "") {
                            $rootScope.holidaysList = JSON.parse(data.jsonValue);
                            angular.forEach($rootScope.holidaysList, function (holiday) {
                                $rootScope.holidays.push(holiday.date);
                            });
                        }
                    }
                )
            }

            $rootScope.noSundayOrHolidays = noSundayOrHolidays;
            function noSundayOrHolidays(date) {
                var day = date.getDay();
                if (day != 0) {
                    var d = date.getDate();
                    d = d < 10 ? '0' + d : '' + d;
                    var m = getMonth(date);
                    var y = date.getFullYear();
                    for (var i = 0; i < $rootScope.holidays.length; i++) {
                        var datePickerDate = (d) + '/' + m + '/' + y;
                        if ($.inArray(datePickerDate, $rootScope.holidays) != -1) {
                            return [false];
                        }
                    }
                    return [true];
                } else {
                    return [day != 0, ''];
                }
            }

            $rootScope.noWeekEndsOrHolidays = noWeekEndsOrHolidays;
            function noWeekEndsOrHolidays(date) {
                var day = date.getDay();
                if (day != 0 && day != 6) {
                    var d = date.getDate();
                    d = d < 10 ? '0' + d : '' + d;
                    var m = getMonth(date);
                    var y = date.getFullYear();
                    for (var i = 0; i < $rootScope.holidays.length; i++) {
                        var datePickerDate = (d) + '/' + m + '/' + y;
                        if ($.inArray(datePickerDate, $rootScope.holidays) != -1) {
                            return [false];
                        }
                    }
                    return [true];
                } else {
                    return [day != 0 && day != 6, ''];
                }
            }

            $rootScope.noHolidays = noHolidays;
            function noHolidays(date) {
                var d = date.getDate();
                d = d < 10 ? '0' + d : '' + d;
                var m = getMonth(date);
                var y = date.getFullYear();
                for (var i = 0; i < $rootScope.holidays.length; i++) {
                    var datePickerDate = (d) + '/' + m + '/' + y;
                    if ($.inArray(datePickerDate, $rootScope.holidays) != -1) {
                        return [false];
                    }
                }
                return [true];
            }

            function getMonth(date) {
                var month = date.getMonth() + 1;
                return month < 10 ? '0' + month : '' + month; // ('' + month) for string result
            }

            function changeTheme(el) {
                /*var theme = el;
                 if (theme != null) {
                 if (theme == "Default") {
                 $('#' + el).css('display', 'none');
                 $("#footer").css('background-color', "var(--cassini-theme-dark-color)");
                 $(".headerbar").css('background-color', "var(--cassini-theme-dark-color)");
                 } else {
                 $('#' + el).css('display', 'block');
                 $("#footer").css('background-color', el);
                 $(".headerbar").css('background-color', el);
                 }
                 } else {
                 $('#' + el).css('display', 'none');
                 $("#footer").css('background-color', "var(--cassini-theme-dark-color)");
                 $(".headerbar").css('background-color', "var(--cassini-theme-dark-color)var(--cassini-theme-dark-color)");
                 }*/
            }

            vm.switchTheme = switchTheme;
            function switchTheme() {
                $('#themeSwitcher').slideDown({
                    start: function () {
                        $(this).css({
                            display: "grid"
                        })
                    }
                });
                $rootScope.loadUserPreference($rootScope.loginPersonDetails.id);
            }

            var appNotification = null;

            function initNotificationPanel() {
                appNotification = $('#appNotification');
                $(window).resize(positionNotificationPanel);

                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        hideNotificationPanel();
                    }
                });
            }

            function showNotificationPanel() {
                positionNotificationPanel();
                appNotification.show();
                appNotification.removeClass('zoomOut');
                appNotification.addClass('zoomIn');
            }

            function positionNotificationPanel() {
                var pos = $('#headerbar').position();
                if (pos != null) {
                    var height = $('#headerbar').outerHeight();
                    var width = $('#workspace').outerWidth();

                    var left = pos.left;
                    var top = pos.top + height;
                    appNotification.css({top: 0, left: left, width: width, height: 50, position: 'absolute'});
                }
            }

            function hideNotificationPanel() {
                positionNotificationPanel();
                appNotification.removeClass('zoomIn');
                appNotification.addClass('zoomOut');
                appNotification.hide();
            }

            function loadPlugins() {
                var deferred = $q.defer();

                $application.plugins = [];
                PluginService.getPlugins().then(
                    function (data) {
                        if (data != null && data !== "" && data.length > 0) {
                            angular.forEach(data, function (plugin) {
                                if (plugin.enabled) {
                                    plugin.extensions = null;
                                    PluginService.loadExtensions(plugin).then(
                                        function (extensions) {
                                            plugin.extensions = extensions;
                                        }
                                    );

                                    $application.plugins.push(plugin);
                                }
                            });


                            var promises = [];
                            var pluginScripts = [];

                            angular.forEach(data, function (plugin) {
                                if (plugin.enabled) {
                                    var promise = PluginService.loadExtensions(plugin);
                                    promises.push(promise);
                                    pluginScripts.push('plugins/{0}/plugin'.format(plugin.directory));
                                }
                            });

                            if (promises.length > 0) {
                                Promise.all(promises).then(
                                    function (extensions) {
                                        var sideNavs = [];
                                        angular.forEach(extensions, function (extension) {
                                            if (extension.sidenav != undefined && extension.sidenav.navgroups != undefined) {
                                                angular.forEach(extension.sidenav.navgroups, function (sideNav) {
                                                    sideNavs.push(sideNav);
                                                });
                                            }
                                        });
                                        vm.sidenavExtensions = sideNavs;
                                        require(pluginScripts, function () {
                                            deferred.resolve();
                                        });
                                    },
                                    function (error) {
                                        deferred.reject(error);
                                    }
                                )
                            }
                            else {
                                deferred.resolve();
                            }
                        }
                        else {
                            deferred.resolve();
                        }
                    },
                    function (error) {
                        deferred.reject(error);
                    }
                );

                var promise = deferred.promise;
                promise.then(
                    function (data) {
                        $timeout(function () {
                            initSideNavigation();
                            $rootScope.$broadcast("app.plugins.loaded");
                        }, 100);
                    },
                    function (error) {
                        $timeout(function () {
                            initSideNavigation();
                        }, 100);
                    }
                )
            }

            function loadViewPermissions() {
                $rootScope.lockItemPermission = $rootScope.hasPermission('item', 'lock');
                $rootScope.adminPermission = $rootScope.hasPermission('admin', 'all');
                $rootScope.copyItemPermission = $rootScope.hasPermission('item', 'copy');
                $rootScope.printItemPermission = $rootScope.hasPermission('item', 'print');
                $rootScope.deleteItemPermission = $rootScope.hasPermission('item', 'delete');
                $rootScope.viewItemPermission = $rootScope.hasPermission('item', 'view');
                $rootScope.createItemPermission = $rootScope.hasPermission('item', 'create');
                $rootScope.editItemPermission = $rootScope.hasPermission('item', 'edit');
                $rootScope.adminPermission = $rootScope.hasPermission('admin', 'all');
                $rootScope.demoteItemPermission = $rootScope.hasPermission('item', 'demote');
                $rootScope.promoteItemPermission = $rootScope.hasPermission('item', 'promote');
                $rootScope.changeECOViewPermission = $rootScope.hasPermission('change', 'eco', 'view');
                $rootScope.changeMCOViewPermission = $rootScope.hasPermission('change', 'mco', 'view');
                $rootScope.changeViewPermission = $rootScope.hasPermission('change', 'view');
                $rootScope.mfrViewPermission = $rootScope.hasPermission('manufacturer', 'view');
                $rootScope.prViewPermission = $rootScope.hasPermission('problemreport', 'view');
                $rootScope.qcrViewPermission = $rootScope.hasPermission('qcr', 'view');
                $rootScope.relatedItemCreatePermission = $rootScope.hasPermission('relateditem', 'create');
                $rootScope.relatedItemDeletePermission = $rootScope.hasPermission('relateditem', 'delete');
            }

            vm.showLogHistory = showLogHistory;
            function showLogHistory() {
                var options = {
                    title: logHistory,
                    template: 'app/desktop/modules/activity/logHistoryView.jsp',
                    controller: 'LogHistoryController as logHistoryVm',
                    resolve: 'app/desktop/modules/activity/logHistoryController',
                    width: 700,
                    showMask: true,
                    buttons: [],
                    callback: function (result) {

                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.onClickAddWidgets = onClickAddWidgets;
            function onClickAddWidgets() {
                $rootScope.$broadcast("app.widgets.add");
            }

            function initObjectDetailsRouteParams(toState, toParams) {
                if (toState.name === 'app.items.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.changes.eco.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.changes.ecr.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.changes.dcr.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.changes.dco.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.changes.mco.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.changes.variance.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pqm.inspectionPlan.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pqm.inspection.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pqm.ncr.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pqm.qcr.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pqm.pr.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pqm.ppap.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pqm.supplierAudit.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.mfr.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.mfr.supplier.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.mfr.mfrparts.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.rm.requirements.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.rm.glossary.details' && toParams.tab === undefined) {
                    toParams.tab = "details.entries";
                } else if (toState.name === 'app.templates.details' && toParams.tab === undefined) {
                    toParams.tab = "details.plan";
                } else if (toState.name === 'app.templates.activity.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.templates.task.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.rm.specifications.details' && toParams.tab === undefined) {
                    toParams.tab = "details.sections";
                } else if (toState.name === 'app.pm.project.details' && toParams.tab === undefined) {
                    toParams.tab = "details.plan";
                } else if (toState.name === 'app.pm.project.activity.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pm.project.activity.task.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.mes.bop.planDetails' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.mes.bop.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.mes.mbom.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pm.program.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                } else if (toState.name === 'app.pm.programtemplate.details' && toParams.tab === undefined) {
                    toParams.tab = "details.basic";
                }
            }

            function initMain() {
                if (!$rootScope.mainLoaded && $application.login != null) {
                    $rootScope.mainLoaded = true;
                    $rootScope.$broadcast('mainLoaded');
                    initNotificationPanel();
                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams) {
                            if (window.$application != null && window.$application.login != null && window.$application.login.id == null) {
                                event.preventDefault();
                                return;
                            }
                            if (!(toState.name == "app.items.all" || toState.name == "app.items.details")) {
                                $rootScope.itemSimpleSearch = null;
                                $rootScope.itemAdvanceSearch = null;
                            } else if (toParams.itemMode != undefined) {
                                if ($rootScope.presentMode != toParams.itemMode) {
                                    $rootScope.itemSimpleSearch = null;
                                    $rootScope.itemAdvanceSearch = null;
                                }
                                $rootScope.presentMode = toParams.itemMode;
                            }

                            $('body').removeClass('dark-mode');
                            initObjectDetailsRouteParams(toState, toParams);

                            $('#search-results-mask-panel').hide();
                            $('#search-results-container').hide();
                            hideSideNavbar();
                            vm.comments.show = false;
                            vm.tags.show = false;
                            vm.viewInfo.description = "";
                            $rootScope.breadCrumb.activity = "";
                            $rootScope.breadCrumb.project = "";
                            $rootScope.breadCrumb.task = "";
                            vm.currentState = toState.name;
                            if (vm.currentState == "app.admin.usersettings") {
                                $rootScope.viewInfo.showDetails = true;
                            }
                            vm.lifecyclePhases = [];
                            $rootScope.searchModeType = false;
                            hideNotificationPanel();
                            $rootScope.hideBusyIndicator();

                            $timeout(function () {
                                positionNotificationPanel();
                            }, 500);
                            var height = $(window).height();
                            $('#contentpanel').height(height - 80);
                        }
                    );

                    if (sessionCheckPromise == null) {
                        initSessionCheck();
                    }

                    $timeout(function () {
                        $('#headerbar').click(function (event) {
                            event.stopPropagation();
                            event.preventDefault();
                        });
                    }, 1000);
                }
            }

            $rootScope.showAll = showAll;
            function showAll(route) {
                $state.go(route);
            }

            $rootScope.customObjectTypes = [];
            $rootScope.loadCustomObjectTypes = loadCustomObjectTypes;
            function loadCustomObjectTypes() {
                $rootScope.customObjectTypes = [];
                CustomObjectTypeService.getNavigationCustomObjectTypes().then(
                    function (data) {
                        $rootScope.customObjectTypes = data;
                    }
                )
            }

            vm.preserveCustomObjectParam = preserveCustomObjectParam;
            function preserveCustomObjectParam(routeParam) {
                $window.localStorage.setItem("route-param", routeParam);

            }

            /* ----- Import individual Objects -------*/
            vm.importIndividualObjects = importIndividualObjects;
            function importIndividualObjects() {
                hideSideNavbar();
                var options = {
                    side: 'right',
                    title: "Data Importer",
                    template: 'app/desktop/modules/exim/import/individualImportView.jsp',
                    controller: 'IndividualImportController as individualImportVm',
                    resolve: 'app/desktop/modules/exim/import/individualImportController',
                    width: 600,
                    buttons: [
                        {text: "Import", broadcast: 'app.exim.individual.mapping'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.switchRole = switchRole;
            function switchRole(group) {
                $rootScope.loginPersonDetails.defaultGroup = group;
                PersonGroupService.getPersonGrpById($rootScope.loginPersonDetails.defaultGroup.groupId).then(
                    function (data) {
                        $rootScope.defaultProfile = data.profile;
                        loadProfilesData();
                        if (vm.currentState != 'app.home') {
                            addHomeWidgets(group.groupSecurityPermissions);
                            $state.go('app.home');
                        } else {
                            addHomeWidgets(group.groupSecurityPermissions);
                            $timeout(function () {
                                $rootScope.sortWidgets($rootScope.widgets);
                            }, 500);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            $rootScope.downloadFileFromIframe = downloadFileFromIframe;
            function downloadFileFromIframe(url) {
                document.getElementById('download_file_iframe').src = url;
            }

            (function () {
                // loadAppSettingsDetails();
                loadPasswordStrength();
                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }
                $rootScope.dragAndDropFilesTitle = parsed.html($translate.instant("DRAG_DROP_FILE")).html();
                $rootScope.dragAndDropFilesTitle = $rootScope.dragAndDropFilesTitle.replace("&amp;", "&");
                initEvents();

                $rootScope.sharedUrl = $location.url();
                checkSession();
                $timeout(function () {
                    window.$("#appview").show();
                    $rootScope.hideBusyIndicator();
                }, 500);
            })();
        }
    }
)
;
