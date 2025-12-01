<div>
    <style scoped>
        .programs-container {
            display: grid;
            grid-template-columns: auto auto;
            grid-gap: 20px;
            width: 100% !important;
            padding: 50px 200px;
            overflow-y: auto;
        }

        @media screen and (max-width: 1375px) {
            .programs-container {
                padding: 50px 100px;
            }
        }

        .programs-container .program-card {
            position: relative;
            height: 385px;
            max-width: 650px;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 20px;
            cursor: pointer;
        }

        .programs-container .program-card:hover {
            /*box-shadow: 0 7px 14px rgba(147, 148, 150, 0.25),
            0 5px 5px rgba(177, 177, 179, 0.22);
            transition: all .2s ease;
            transform: translate3D(0, -1px, 0) scale(1.01);*/
        }

        .programs-container .program-card .program-header {

        }

        .programs-container .program-card .program-header .project-name {
            font-size: 18px;
            font-weight: 600;
            height: 1.5em;
            overflow: hidden;
        }

        .programs-container .program-card .program-header .project-description {
            color: #707d91;
            height: 1.5em;
            overflow: hidden;
        }

        .programs-container .program-card .project-details {
            margin-top: 20px;
        }

        .programs-container .program-card .project-details .project-props {

        }

        .programs-container .program-card .project-details .project-props div {
            inline-size: 120px;
            overflow-wrap: break-word;
        }

        .programs-container .program-card .project-details .project-props div:last-child {
            margin-right: 0;
        }

        .programs-container .program-card .project-details .prop-name {
            color: #707d91;
            font-weight: 300;
        }

        .programs-container .program-card .project-details .prop-value {
            font-weight: 400;
        }

        .programs-container .program-card .project-details .prop-value.btn {
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

        .programs-container .program-card .project-details .prop-value {
            font-weight: 600;
        }

        .programs-container .program-card .project-details .prop-value.btn:hover {
        }

        .programs-container .program-card .project-footer {
            border-top: 1px solid #eee !important;
        }

        .programs-container .program-card .project-footer div {
            line-height: 64px !important;
        }

        .programs-container .program-card .project-footer i {
            color: #707d91;
        }

        .programs-container .program-card .project-footer .footer-label {
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

        .program-card .project-members {
            height: 100px;
            margin-top: 30px;
        }

        .program-card .project-members .member-avatar {
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

        .program-card .project-members .member-avatar:hover {
            z-index: 100 !important;
        }

        .program-card .project-members .more-members {
            background-color: #f7ecec;
            color: #ed55b2;
        }

        .program-card .project-members .no-members {
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

        .program-card .program-header .program-card-menu {
            float: right;
            margin-top: -40px;
            font-size: 18px;
            padding: 3px 5px;
            border-radius: 3px;
        }

        .program-card .program-header .program-card-menu .dropdown-menu {
            background-color: #fff;
        }

        .program-card .program-header .program-card-menu:hover {
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
    <div class="no-data" ng-if="allProgramsVm.loading == false && allProgramsVm.programs.content.length == 0">
        <span ng-if="allProgramsVm.filters.searchQuery == null || allProgramsVm.filters.searchQuery == ''">
            <img src="app/assets/no_data_images/Project.png" alt="" class="image">
        </span>

        <span ng-if="allProgramsVm.filters.searchQuery != null && allProgramsVm.filters.searchQuery != ''">
            <img src="app/assets/no_data_images/searching.png" alt="" class="image">
        </span>

        <div class="message">
            <span ng-if="allProgramsVm.filters.searchQuery == null || allProgramsVm.filters.searchQuery == ''">
                {{ 'NO_PROGRAMS' | translate}}
            </span>
            <span ng-if="allProgramsVm.filters.searchQuery != null && allProgramsVm.filters.searchQuery != ''">
                {{ 'NO_SEARCH_RESULTS' | translate}}
            </span>
        </div>
        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
    </div>
    <div class="programs-container">
        <div class="program-card" ng-repeat="program in allProgramsVm.programs.content">
            <div class="program-header">
                <div class="project-name">
                    <span ng-bind-html="program.name | highlightText: freeTextQuery | limitTo: 50"
                          title="{{program.name}}"></span>
                </div>
                <div class="project-description">
                    <span title="{{program.description.length > 20 ? program.description : ' '}}">{{program.description | limitTo:20}} {{program.description.length > 20 ? '...' : ' '}}</span>
                </div>
                <div class="program-card-menu" uib-dropdown>
                    <i class="las la-ellipsis-h" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <%--<li>
                            <a ng-click="allProgramsVm.editProject(program)" translate>EDIT_PROJECTS</a>
                        </li>--%>
                        <li>
                            <a ng-click="showPrintOptions(program.id,'PROGRAM')" translate>PREVIEW_AND_PRINT</a>
                        </li>
                        <li title="{{program.started || program.percentComplete > 0 ? completedProjectCannotDeleteProgram:''}}">
                            <a ng-click="allProgramsVm.deleteProgram(program)"
                          ng-class="{'disabled':program.started || program.percentComplete > 0}" translate>DELETE_PROGRAM</a>
                        </li>
                        <plugin-table-actions context="program.all" object-value="program"></plugin-table-actions>
                    </ul>
                </div>
            </div>
            <div class="project-details">
                <div class="project-progress d-flex flex-column">
                    <div class="progress-label" translate>PROGRAM_PROGRESS</div>
                    <div class=" d-flex">
                        <div class="flex progress progress-xs">
                            <div class="progress-bar bg-warning" role="progressbar"
                                 style="width: {{program.percentComplete}}%;"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                        <div class="progress-percent">{{program.percent}}</div>
                    </div>
                    <div ng-show="program.status != null" class="prop-name"><span translate>STATUS </span>:
                        <span class="prop-value">{{program.status}}</span>
                    </div>
                </div>
            </div>
            <div class="project-members d-flex">
                <div class="flex" style="max-width: 150px;">
                    <div class="d-flex flex flex-column">
                        <div class="text-light" translate>PROGRAM_MANAGER</div>
                        <div class="manager-avatar"
                             title="{{program.managerFullName}}" ng-click="allProgramsVm.showUserDetails(user)">
                            <span ng-if="!program.hasManagerImage">{{program.managerWord}}</span>
                            <img ng-if="program.hasManagerImage" ng-src="{{program.managerImage}}"
                                 style="height: 50px;width: 50px;border-radius: 50%">
                        </div>
                    </div>
                </div>
                <div class="flex">
                    <div class="text-light" translate>RESOURCES</div>
                    <div style="display: flex">
                        <div class="member-avatar" title="{{projectMember.fullName}}"
                             ng-if="$index < 10"
                             ng-repeat="projectMember in program.projectMembers"
                             ng-click="allProgramsVm.showUserDetails(projectMember)">
                            <span ng-if="!projectMember.hasImage">{{projectMember.imageWord}}</span>
                            <img ng-if="projectMember.hasImage" ng-src="{{projectMember.personImage}}"
                                 style="height: 50px;width: 50px;border-radius: 50%">
                        </div>
                        <div class="member-avatar more-members" ng-if="program.projectMembers.length > 10">
                            {{program.projectMembers.length - 10}}+
                        </div>
                        <div class="member-avatar no-members d-flex align-items-center"
                             ng-if="program.projectMembers.length == 0">
                            {{'NO' | translate}} <br/> {{'RESOURCE' | translate}}
                        </div>
                    </div>
                </div>
            </div>
            <div class="project-footer d-flex">
                <div class="flex">
                    <div class="flex-inline mr-10" title="Projects">
                        <i class="las la-tasks"></i>
                        <span class="footer-label"
                              ui-sref="app.pm.program.details({programId:program.id,tab:'details.project'})">
                            {{program.projects}}</span>
                    </div>
                    <div class="flex-inline mr-10" title="{{allProgramsVm.conversations}}">
                        <i class="las la-comments"></i>
                        <span class="footer-label"
                              ng-click="allProgramsVm.showComments(program)">{{program.comments}}</span>
                    </div>
                    <%--<div class="flex-inline mr-10" title="{{allProgramsVm.detailsShareTitle}}">
                        <i class="las la-share" ng-click="shareProject(program)" style=""></i>
                        <span class="footer-label" title="{{allProgramsVm.share}}"></span>
                    </div>
                    <div class="flex-inline">
                        <span ng-click="allProgramsVm.subscribeProject(program)">
                            <i ng-if="!program.isSubscribed" class="la la-bell"
                               title="{{'SUBSCRIBE_TITLE' | translate }}"></i>
                            <i ng-if="program.isSubscribed" class="la la-bell-slash"
                               title="{{'UN_SUBSCRIBE_TITLE' | translate }}"></i>
                        </span>
                    </div>--%>
                </div>
                <div class="text-right">
                    <button class="btn btn-sm btn-details" ng-click="allProgramsVm.showProgram(program)"
                            translate>
                        DETAILS
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>