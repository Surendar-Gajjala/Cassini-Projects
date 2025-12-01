<div>
    <style scoped>
        .projects-container {
            display: grid;
            grid-template-columns: auto auto;
            grid-gap: 20px;
            width: 100% !important;
            padding: 50px 200px;
            overflow-y: auto;
        }

        @media screen and (max-width: 1375px) {
            .projects-container {
                padding: 50px 100px;
            }
        }

        .projects-container .project-card {
            position: relative;
            height: 385px;
            max-width: 650px;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 20px;
            cursor: pointer;
        }

        .projects-container .project-card:hover {
            /*box-shadow: 0 7px 14px rgba(147, 148, 150, 0.25),
            0 5px 5px rgba(177, 177, 179, 0.22);
            transition: all .2s ease;
            transform: translate3D(0, -1px, 0) scale(1.01);*/
        }

        .projects-container .project-card .project-header {

        }

        .projects-container .project-card .project-header .project-name {
            font-size: 18px;
            font-weight: 600;
            height: 1.5em;
            overflow: hidden;
        }

        .projects-container .project-card .project-header .project-description {
            color: #707d91;
            height: 1.5em;
            overflow: hidden;
        }

        .projects-container .project-card .project-details {
            margin-top: 20px;
        }

        .projects-container .project-card .project-details .project-props {

        }

        .projects-container .project-card .project-details .project-props div {
            inline-size: 120px;
            overflow-wrap: break-word;
        }

        .projects-container .project-card .project-details .project-props div:last-child {
            margin-right: 0;
        }

        .projects-container .project-card .project-details .prop-name {
            color: #707d91;
            font-weight: 300;
        }

        .projects-container .project-card .project-details .prop-value {
            font-weight: 400;
        }

        .projects-container .project-card .project-details .prop-value.btn {
            text-align: left;
        }

        .project-progress {
            margin-bottom: 10px;
        }

        .project-progress .progress-label {
            font-weight: 300;
            color: #707d91;
        }

        .project-progress .progress-percent {
            width: 30px;
            margin-left: 20px;
            margin-top: -8px;
            font-weight: 400;
            text-align: right;
        }

        .projects-container .project-card .project-details .prop-value {
            font-weight: 600;
        }

        .projects-container .project-card .project-details .prop-value.btn:hover {
        }

        .projects-container .project-card .project-footer {
            border-top: 1px solid #eee !important;
        }

        .projects-container .project-card .project-footer div {
            line-height: 64px !important;
        }

        .projects-container .project-card .project-footer i {
            color: #707d91;
        }

        .projects-container .project-card .project-footer .footer-label {
            color: #3699ff;
        }

        .progress.progress-xs {
            height: 8px;
        }

        .btn-details {
            background-color: #3699ff;
            border-color: #3699ff;
            min-width: 80px;
            color: #fff;
        }

        .project-card .project-members {
            height: 100px;
            margin-top: 30px;
        }

        .project-card .project-members .member-avatar {
            position: relative;
            border-radius: 50%;
            margin-left: -25px;
            z-index: 1;
            height: 50px;
            width: 50px;
            background-color: #d1d3e0;
            color: #181c32;
            display: inline-flex;
            border: 2px solid #fff;
            align-items: center;
            justify-content: center;
        }

        .manager-avatar {
            position: relative;
            border-radius: 50%;
            z-index: 1;
            height: 50px;
            width: 50px;
            background-color: #d1d3e0;
            color: #181c32;
            display: inline-flex;
            border: 2px solid #fff;
            align-items: center;
            justify-content: center;
        }

        .member-avatar:first-child {
            margin-left: -2px !important;
        }

        .project-card .project-members .member-avatar:hover {
            z-index: 100 !important;
        }

        .project-card .project-members .more-members {
            background-color: #f7ecec;
            color: #ed55b2;
        }

        .project-card .project-members .no-members {
            font-size: 12px;
            line-height: 12px;
            text-align: center;
            border: 1px solid #e1f0ff;
        }

        .align-items-center {
            -webkit-box-align: center !important;
            -ms-flex-align: center !important;
            align-items: center !important;
        }

        .project-card .project-header .project-card-menu {
            float: right;
            margin-top: -40px;
            font-size: 18px;
            padding: 3px 5px;
            border-radius: 3px;
        }

        .project-card .project-header .project-card-menu .dropdown-menu {
            background-color: #fff;
        }

        .project-card .project-header .project-card-menu:hover {
            background-color: #d5d9df;
        }

        .btn-details:hover {
            background-color: #187de4;
            border-color: #187de4;
            color: #fff;
        }

        .flex-2 {
            flex: 2;
        }

        .flex-inline {
            display: inline-block;
        }

        .mr-10 {
            margin-right: 20px;
        }

        .text-light {
            color: #707d91;
            font-weight: 300;
        }

    </style>
    <div class="no-data" ng-if="allProjectsVm.loading == false && allProjectsVm.projects.content.length == 0">
        <span ng-if="allProjectsVm.projectFilter.searchQuery == null || allProjectsVm.projectFilter.searchQuery == ''">
            <img src="app/assets/no_data_images/Project.png" alt="" class="image">
        </span>

        <span ng-if="allProjectsVm.projectFilter.searchQuery != null && allProjectsVm.projectFilter.searchQuery != ''">
            <img src="app/assets/no_data_images/searching.png" alt="" class="image">
        </span>

        <div class="message">
            <span ng-if="allProjectsVm.projectFilter.searchQuery == null || allProjectsVm.projectFilter.searchQuery == ''">
                {{ 'NO_PROJECTS' | translate}}
            </span>
            <span ng-if="allProjectsVm.projectFilter.searchQuery != null && allProjectsVm.projectFilter.searchQuery != ''">
                {{ 'NO_SEARCH_RESULTS' | translate}}
            </span>
        </div>
        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
    </div>
    <div class="projects-container">
        <div class="project-card" ng-repeat="project in allProjectsVm.projects.content">
            <div class="project-header">
                <div class="project-name">
                    <span ng-bind-html="project.name | highlightText: freeTextQuery | limitTo: 50"
                          title="{{project.name}}"></span>
                </div>
                <div class="project-description">
                    <span title="{{project.description.length > 20 ? project.description : ' '}}">{{project.description | limitTo:20}} {{project.description.length > 20 ? '...' : ' '}}</span>
                </div>
                <div class="project-card-menu" uib-dropdown>
                    <i class="las la-ellipsis-h" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li title="{{project.percentComplete == 100 ? completedProjectCannotBeEdited:''}}">
                            <a ng-click="allProjectsVm.editProject(project)" 
                            ng-class="{'disabled':project.percentComplete == 100}" translate>EDIT_PROJECTS</a>
                        </li>
                        <li>
                            <a ng-click="showPrintOptions(project.id,'PROJECT')" translate>PREVIEW_AND_PRINT</a>
                        </li>
                        <li  title="{{project.percentComplete > 0 ? startedProjectCannotBeDeleted:''}}">
                        <a ng-click="allProjectsVm.deleteProject(project)"
                         ng-class="{'disabled':project.percentComplete > 0}" translate>DELETE_PROJECT_TITLE</a>
                        </li>
                        <plugin-table-actions context="project.all" object-value="project"></plugin-table-actions>
                    </ul>
                </div>
            </div>
            <div class="project-details">
                <div class="project-progress d-flex flex-column">
                    <div class="progress-label" translate>PROJECT_PROGRESS</div>
                    <div class=" d-flex">
                        <div class="flex progress progress-xs">
                            <div class="progress-bar bg-warning" role="progressbar"
                                 style="width: {{project.percentComplete}}%;"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                        <div class="progress-percent">{{project.percentComplete}}%</div>
                    </div>
                    <div ng-show="project.status != null" class="prop-name"><span translate>STATUS </span>:
                        <span class="prop-value">{{project.status}}</span>
                    </div>
                </div>
                <div class="project-props d-flex flex-stretch">
                    <div class="d-flex flex flex-column">
                        <div class="prop-name" translate>PLANNED_START</div>
                        <div class="prop-value">{{project.plannedStartDate || 'Not Set'}}</div>
                    </div>
                    <div class="d-flex flex flex-column">
                        <div class="prop-name" translate>PLANNED_FINISH</div>
                        <div class="prop-value">{{project.plannedFinishDate || 'Not Set'}}</div>
                    </div>
                    <div class="d-flex flex flex-column">
                        <div class="prop-name" translate>ACTUAL_START</div>
                        <div class="prop-value">{{project.actualStartDate || '-'}}</div>
                    </div>
                    <div class="d-flex flex flex-column">
                        <div class="prop-name" translate>ACTUAL_FINISH</div>
                        <div class="prop-value">{{project.actualFinishDate || '-'}}</div>
                    </div>
                </div>
            </div>
            <div class="project-members d-flex">
                <div class="flex" style="max-width: 150px;">
                    <div class="d-flex flex flex-column">
                        <div class="text-light" translate>PROJECT_MANAGER</div>
                        <div class="manager-avatar"
                             title="{{project.managerFullName}}" ng-click="allRolesVm.showUserDetails(user)">
                            <span ng-if="!project.hasManagerImage">{{project.managerWord}}</span>
                            <img ng-if="project.hasManagerImage" ng-src="{{project.managerImage}}"
                                 style="height: 50px;width: 50px;border-radius: 50%">
                        </div>
                    </div>
                </div>
                <div class="flex">
                    <div class="text-light" translate>PROJECT_TEAM</div>
                    <div style="display: flex">
                        <div class="member-avatar" title="{{projectMember.fullName}}"
                             ng-if="$index < 10"
                             ng-repeat="projectMember in project.projectMembers"
                             ng-click="allProjectsVm.showUserDetails(projectMember)">
                            <span ng-if="!projectMember.hasImage">{{projectMember.imageWord}}</span>
                            <img ng-if="projectMember.hasImage" ng-src="{{projectMember.personImage}}"
                                 style="height: 50px;width: 50px;border-radius: 50%">
                        </div>
                        <div class="member-avatar more-members" ng-if="project.projectMembers.length > 10">
                            {{project.projectMembers.length - 10}}+
                        </div>
                        <div class="member-avatar no-members d-flex align-items-center"
                             ng-if="project.projectMembers.length == 0">
                            {{'NO' | translate}} <br/> {{'TEAM' | translate}}
                        </div>
                    </div>
                </div>
            </div>
            <div class="project-footer d-flex">
                <div class="flex">
                    <div class="flex-inline mr-10" title="{{allProjectsVm.tasks}}">
                        <i class="las la-tasks"></i>
                        <span class="footer-label"
                              ui-sref="app.pm.project.details({projectId:project.id,tab:'details.plan'})">
                            {{project.tasks}}</span>
                    </div>
                    <div class="flex-inline mr-10" title="{{allProjectsVm.deliverables}}">
                        <i class="las la-cogs"></i>
                        <span class="footer-label"
                              ui-sref="app.pm.project.details({projectId:project.id,tab:'details.deliverables'})">
                            {{project.deliverables}}</span>
                    </div>
                    <div class="flex-inline mr-10" title="{{allProjectsVm.conversations}}"
                         ng-show="project.showConversationCount">
                        <i class="las la-comments"></i>
                        <span class="footer-label"
                              ng-click="allProjectsVm.showComments(project)">{{project.comments}}</span>
                    </div>
                    <div class="flex-inline mr-10" title="{{allProjectsVm.detailsShareTitle}}">
                        <i class="las la-share" ng-click="shareProject(project)" style=""></i>
                        <span class="footer-label" title="{{allProjectsVm.share}}"></span>
                    </div>
                    <div class="flex-inline">
                        <span ng-click="allProjectsVm.subscribeProject(project)">
                            <i ng-if="!project.isSubscribed" class="la la-bell"
                               title="{{'SUBSCRIBE_TITLE' | translate }}"></i>
                            <i ng-if="project.isSubscribed" class="la la-bell-slash"
                               title="{{'UN_SUBSCRIBE_TITLE' | translate }}"></i>
                        </span>
                    </div>
                </div>
                <div class="text-right">
                    <button class="btn btn-sm btn-details" ng-click="allProjectsVm.openProjectDetails(project)"
                            translate>
                        DETAILS
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>