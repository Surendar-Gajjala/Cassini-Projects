<div>
    <style scoped>
        .home-dashboard-container {
            background-color: var(--cassini-theme-dark-color);
            padding-top: 50px;
            height: 100%;
        }

        .home-dashboard-container .view-content {
            padding: 50px;
            overflow-y: auto;
        }

        .home-dashboard-container .greeting-container {
            margin-bottom: 50px;
        }

        .home-dashboard-container .greeting-container .date-string {
            font-size: 16px;
            color: rgba(255, 255, 255, 0.4);
            margin-top: 5px;
            font-weight: 300;
        }

        .home-dashboard-container .home-greeting {
            margin: 0;
            font-weight: 300 !important;
            font-size: 30px;
            color: rgba(255, 255, 255, 0.8);
        }

        .home-dashboard-container .home-widget-container {
            display: grid;
            grid-template-columns: auto auto;
            grid-gap: 30px;
            justify-content: left;
        }

        .home-dashboard-container .home-widget-container.counts-container {
            grid-template-columns: auto auto auto auto;
        }

        .home-dashboard-container .home-widget-container .home-widget {
            height: 150px;
            width: 150px;
            text-align: center;
            color: rgba(255, 255, 255, 0.8);
            background-color: var(--cassini-theme-light-color);
            border-radius: 5px;
            padding: 10px;
            position: relative;
            border: 2px solid var(--cassini-theme-light-color);
        }

        .home-dashboard-container .home-widget-container .home-widget:hover {
            -webkit-box-shadow: 0 0px 7px var(--cassini-theme-widget-hover-color);
            box-shadow: 0 0px 7px var(--cassini-theme-widget-hover-color);
            cursor: pointer;
            border-radius: 5px;
            border: 2px solid var(--cassini-theme-widget-hover-color);

            transition: all .2s ease;
            transform: translate3D(0, -1px, 0) scale(1.01);
        }

        .home-dashboard-container .home-widget-container .home-widget .home-widget-icon {
            height: 75px;
            font-size: 25px;
            padding-top: 10px;
        }

        .home-dashboard-container .home-widget-container .home-widget .home-widget-icon i {
            height: 50px;
            font-size: 60px;
        }

        .home-dashboard-container .home-widget-container .home-widget .home-widget-label {
            font-size: 18px;
            font-weight: 300;
            line-height: 40px;
        }

        .home-dashboard-container .home-widget-container .home-widget .home-badge {
            position: absolute;
            top: -10px;
            right: -10px;
            border-radius: 50%;
            background-color: #f01059;
            color: white;
            height: 32px;
            width: 32px;
            text-align: center;
            line-height: 27px;
            border: 2px solid #fff;
        }

        .pd-l-50 {
            padding-left: 50px;
        }

        .home-widget .count-label {
            line-height: 70px;
            font-size: 34px;
            color: rgb(58, 181, 164) !important;
        }


        @media screen and (max-width: 1375px) {
            .home-dashboard-container .home-greeting {
                font-size: 24px;
            }

            .home-dashboard-container .greeting-container .date-string {
                font-size: 14px;
            }

            .home-dashboard-container .home-widget-container {
                grid-template-columns: auto auto;
                grid-gap: 20px;
                justify-content: left;
            }

            .home-dashboard-container .home-widget-container .home-widget {
                height: 100px;
                width: 120px;
            }

            .home-dashboard-container .home-widget-container .home-widget .home-widget-icon i {
                font-size: 36px;
            }

            .home-dashboard-container .home-widget-container .home-widget .home-widget-label {
                font-size: 14px;
            }

            .home-dashboard-container .home-widget-container .home-widget .home-widget-icon {
                height: 45px;
            }

            .home-dashboard-container .home-widget-container .home-widget .home-badge {
                height: 24px;
                width: 24px;
                font-size: 12px;
                line-height: 20px;
            }

            .home-widget .count-label {
                line-height: 30px;
                font-size: 40px;
            }
        }

        .fs-24 {
            font-size: 24px !important;
        }

        .fs-28 {
            font-size: 28px !important;
        }

        .fs-34 {
            font-size: 34px !important;
        }

        .home-widget-icon .flaticon-marketing8:before, .home-widget-icon .flaticon-marketing8:after {
            font-size: 36px !important;
        }

        .widget-btn-circle {
            width: 30px;
            height: 30px;
            padding: 6px 0px;
            border-radius: 15px;
            text-align: center;
            font-size: 12px;
            line-height: 1.42857;
            outline: none !important;
        }

        .widget-container {
            margin: 0 133px 0px 0px !important;
        }

        .add-widgets {
            position: absolute;
            background: transparent;
            border: none;
            color: rgba(255, 255, 255, 0.8);
            padding-left: 0;
            margin-top: -50px;
        }

        .add-widgets:hover {
            background: transparent !important;
            border: none;
            color: rgba(255, 255, 255, 0.8);
        }

        .lh-20 {
            line-height: 20px !important;
        }

        .adjust-name {
            line-height: 1.5em !important;
            overflow: hidden;
            height: 3em;
        }

        .pt-5 {
            padding-top: 5px !important;
        }
    </style>

    <div class="home-dashboard-container" ng-controller="NewObjectController as newObjectVm">
        <%--<div class="widget-container pull-right">

        </div>--%>
        <div class="container">
            <div class="greeting-container">
                <h3 class="home-greeting">{{homeDashboardVm.greeting}}!</h3>

                <div class="date-string">{{homeDashboardVm.dateString}}</div>
            </div>
            <div style="padding: 10px 0 30px 0;height: calc(100% - 260px);">
                <div class="row">
                    <div class="col-sm-5 home-widget-container no-padding">
                        <div class="home-widget" ng-click="homeDashboardVm.showMyTasks()">
                            <div class="home-badge" ng-if="homeDashboardVm.userTasksCount > 0">
                                {{homeDashboardVm.userTasksCount}}
                            </div>
                            <div class="home-widget-icon">
                                <i class="las la-business-time"></i>
                            </div>
                            <div class="home-widget-label">
                                My Tasks
                            </div>
                        </div>
                        <div class="home-widget" ng-click="homeDashboardVm.showConversations()">
                            <div class="home-badge" ng-if="homeDashboardVm.conversationsCount > 0">
                                {{homeDashboardVm.conversationsCount}}
                            </div>
                            <div class="home-widget-icon">
                                <i class="las la-comments"></i>
                            </div>
                            <div class="home-widget-label" translate>
                                CONVERSATIONS
                            </div>
                        </div>
                        <div class="home-widget"
                             ng-if="hasPermission('savedsearch','view')"
                             ng-click="homeDashboardVm.showSavedSearches()">
                            <div class="home-badge" ng-if="savedSearchesCount > 0">
                                {{savedSearchesCount}}
                            </div>
                            <div class="home-widget-icon">
                                <i class="las la-search"></i>
                            </div>
                            <div class="home-widget-label" translate>
                                SEARCHES
                            </div>
                        </div>
                        <div class="home-widget"
                             ng-if="hasPermission('settings','view') && isProfileMenu('settings.menu')"
                             ng-click="homeDashboardVm.gotoSettings()">
                            <div class="home-widget-icon">
                                <i class="las la-tools"></i>
                            </div>
                            <div class="home-widget-label" translate>
                                SETTINGS_TITLE
                            </div>
                        </div>
                        <div class="home-widget" ng-click="homeDashboardVm.gotoClassification()"
                             ng-show="hasPermission('classification','view') && isProfileMenu('classification.menu')">
                            <div class="home-widget-icon">
                                <i class="las la-sitemap"></i>
                            </div>
                            <div class="home-widget-label" translate>
                                CLASSIFICATION_TITLE
                            </div>
                        </div>
                        <div class="home-widget" ng-click="homeDashboardVm.gotoAdmin()"
                             ng-if="loginPersonDetails.isAdmin">
                            <div class="home-widget-icon">
                                <i class="las la-users-cog"></i>
                            </div>
                            <div class="home-widget-label" translate>
                                ADMIN
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-7">
                        <style scoped>
                            .slideshow-container {
                                max-width: 1000px;
                                position: relative;
                                margin: auto;
                            }

                            /* Hide the images by default */
                            .mySlides {
                                display: none;
                            }

                            .slide-dot {
                                cursor: pointer;
                                height: 13px;
                                width: 13px;
                                margin: 0 2px;
                                background-color: #bbb;
                                border-radius: 50%;
                                display: inline-block;
                                transition: background-color 0.6s ease;
                            }

                            .dot-active, .slide-dot:hover {
                                background-color: #717171;
                            }
                        </style>
                        <div class="slideshow-container">
                            <button title="Add home widget" class="add-widgets" ng-if="widgets.length > 0"
                                    ng-click="homeDashboardVm.addWidgets()">
                                <i class="la la-plus"></i>
                                <span>Add Widgets</span>
                            </button>
                            <div class="mySlides" ng-repeat="homeWidget in homeDashboardVm.homeWidgets">
                                <div class="home-widget-container counts-container">
                                    <div class="home-widget" ng-click="newObjectVm.navigateWidget(widget)"
                                         ng-repeat="widget in homeWidget">
                                        <div class="home-widget-icon pt-5">
                                            <span class="count-label"
                                                  ng-if="widget.privilege == 'view'">
                                                {{homeDashboardVm.getObjectCount(widget)}}</span>
                                            <i class="las {{homeDashboardVm.getObjectIcon(widget)}}"
                                               ng-if="widget.privilege == 'create'"></i>
                                        </div>
                                        <div class="home-widget-label adjust-name" title="{{widget.name}}">
                                            {{widget.name}}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br>

                        <div style="width: 690px;text-align: center;" ng-show="homeDashboardVm.homeWidgets.length > 1">
                            <span class="slide-dot" ng-repeat="homeWidget in homeDashboardVm.homeWidgets"
                                  ng-click="currentSlide($index + 1)"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>