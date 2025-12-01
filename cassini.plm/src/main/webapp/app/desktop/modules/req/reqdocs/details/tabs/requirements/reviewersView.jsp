<div style="padding: 20px;">
    <style scoped>
        .view-tb {
            padding: 10px 15px 5px 10px;
            display: inline-block;
            background-color: var(--cassini-form-contrast-color);
            border-radius: 3px;
        }

        .view-tb:hover {
            cursor: pointer;
            background-color: rgb(194, 223, 251);
        }

        .view-tb .add-user-icon {
            margin-right: 5px;
        }

        .view-tb .add-user-label {
            margin-top: -3px;
            float: right;
        }

        .view-tb .users-container {
            width: 300px;
            max-height: 400px;
            overflow-y: auto;
        }

        .view-tb .users-container .users-search {
            position: sticky;
            top: 0;
            padding: 10px 30px;
            background-color: var(--cassini-bg-color);
        }

        .view-tb .users-container .users-search i {
            float: right;
            margin-top: -23px;
            font-size: 14px;
            margin-right: 10px;
            cursor: pointer;
            z-index: 9999 !important;
            position: relative;
        }

        .view-tb .users-container .users-search i:hover {
        }

        .view-tb .users-container .users-search .form-control {
            padding: 5px 10px;
        }

        .view-tb .dropdown-menu {
            border: 0;
            box-shadow: rgba(0, 0, 0, 0.133) 0px 6.4px 14.4px 0px,
            rgba(0, 0, 0, 0.11) 0px 1.2px 3.6px 0px;
            padding: 0;
        }

        .view-tb .users-container .user-cards {
            display: flex;
            flex-direction: column;
        }

        .view-tb .users-container .user-cards .user-card {
            padding: 10px 20px;
        }

        .view-tb .users-container .user-cards .user-card:hover {
            background-color: var(--cassini-form-controls-color);
        }

        .view-tb .users-container .user-cards .user-card .user-image,
        .view-tb .users-container .user-cards .user-card .user-image img {
            width: 50px;
            height: 50px;
        }

        .view-tb .users-container .user-cards .user-card .user-name {
            font-size: 15px;
        }

        .view-tb .users-container .user-cards .user-card .user-role {
            font-weight: 300;
        }

        .reviewers-container {
            display: flex;
            flex-direction: column;
            margin-top: 30px;
        }

        .reviewers-container .reviewer-card {
            /* height: 75px; */
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 10px 10px 10px 20px;
            /* display: flex; */
            margin-bottom: 10px;
        }

        .reviewers-container .reviewer-card .reviewer-avatar {
            padding-top: 2px;
        }

        .reviewers-container .reviewer-card .reviewer-avatar .user-image,
        .reviewers-container .reviewer-card .reviewer-avatar .user-image img {
            width: 50px;
            height: 50px;
        }

        .reviewers-container .reviewer-card .reviewer-desc {
            flex: 1;
            padding-top: 5px;
        }

        .reviewers-container .reviewer-card .reviewer-desc .reviewer-name {
            font-size: 15px;
        }

        .reviewers-container .reviewer-card .reviewer-desc .reviewer-role {
            font-weight: 300;
        }

        .reviewers-container .reviewer-card .reviewer-approval {
            width: 100px;
            padding: 0 10px;
            text-align: center;
            padding-top: 2px;
        }

        .reviewers-container .reviewer-card .reviewer-approval input[switch] + label {
            margin-top: 0;
        }

        .reviewers-container .reviewer-card .reviewer-approval .remove-approver {
            float: right;
            margin-top: -67px;
            margin-right: -20px;
            display: none;
        }

        .reviewers-container .reviewer-card:hover .reviewer-approval .remove-approver {
            display: block;
            height: 20px;
            width: 20px;
            line-height: 18px;
            background-color: #ddd;
        }

        .reviewers-container .reviewer-card .reviewer-approval .remove-approver i {
            font-size: 12px;
            opacity: 0.6;
            cursor: pointer;
        }

        .reviewers-container .reviewer-card .reviewer-approval .remove-approver i:hover {
            opacity: 1;
        }

        .mt-5 {
            margin-top: 4px;
        }

        .mt-10 {
            margin-top: 10px;
        }

        .text-light {
            color: #707d91;
            font-weight: 300;
        }

        .users-container .no-users {
            padding: 20px 30px;
        }

        .help-text {
            opacity: 0.5;
            font-weight: 300;
            font-size: 12px;
        }
    </style>

    <div class="view-tb" uib-dropdown dropdown-toggle uib-dropdown-toggle>
        <span>
            <span class="add-user-icon"><i class="las la-user-plus"></i></span>
            <span class="add-user-label" translate>ADD_USER</span>
        </span>
        <ul uib-dropdown-menu class="dropdown-menu" role="menu"
            style="z-index: 9999 !important;" ng-click="$event.stopPropagation()">
            <li>
                <div class="users-container">
                    <div class="users-search" ng-if="reviewersVm.logins.content.length > 0">
                        <input type="text" class="form-control" ng-model="reviewersVm.searchUsers">
                        <i class="la la-times"
                           ng-if="reviewersVm.searchUsers != null && reviewersVm.searchUsers.trim() !== ''"
                           ng-click="reviewersVm.searchUsers =  '';"></i>
                    </div>
                    <div class="no-users" ng-if="reviewersVm.logins.content.length == 0">
                        {{noUserSelect}}
                    </div>
                    <div class="user-cards">
                        <div class="user-card d-flex"
                             ng-repeat="login in reviewersVm.logins.content | filter: {person: {fullName: reviewersVm.searchUsers}}"
                             ng-click="reviewersVm.addReviewer(login)">
                            <person-avatar person-id="login.person.id" display="'normal'"></person-avatar>
                            <div class="mt-5">
                                <div class="user-name">
                                    {{login.person.fullName}}
                                </div>
                                <div class="user-role">
                                    {{login.person.defaultGroupObject.name}}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
    <div class="help-text">
        {{selectReviewerApproverMsg}}
    </div>
    <div class="reviewers-container">
        <div class="reviewer-card" ng-repeat="reviewer in reviewersVm.requirement.reviewers">
            <div style="display:flex">
                <div class="reviewer-avatar">
                    <person-avatar person-id="reviewer.login.person.id" display="'normal'"
                        ng-if="reviewer.login != null"></person-avatar>
                </div>
                <div class="reviewer-desc">
                    <div class="reviewer-name">{{reviewer.login.person.fullName}}</div>
                    <div class="reviewer-role">{{reviewer.login.person.defaultGroupObject.name}}</div>
                </div>
                <div class="reviewer-approval">
                    <div class="text-light">Approver</div>
                    <div ng-if="reviewersVm.type == 'REQUIREMENTDOCUMENT' || reviewersVm.type == 'REQUIREMENT'"
                        ng-class="{'disabled':reviewer.status != 'NONE'}">
                        <input type="checkbox" id="reviewer{{$index}}" switch="none" checked=""
                            ng-click="reviewersVm.updateReviewer(reviewer)" ng-model="reviewer.approver">
                        <label for="reviewer{{$index}}" data-on-label="Yes" data-off-label="No"></label>
                    </div>
                    <div ng-if="reviewersVm.type == 'REQUIREMENTDOCUMENT' || reviewersVm.type == 'REQUIREMENT'"
                        ng-class="{'disabled':reviewer.status != 'NONE'}" class="remove-approver"
                        ng-click="reviewersVm.deleteReviewer(reviewer)">
                        <i class="la la-times" title="Remove this reviewer"></i>
                    </div>

                    <div
                        ng-if="reviewersVm.type == 'REQUIREMENTDOCUMENTTEMPLATE' || reviewersVm.type == 'REQUIREMENTTEMPLATE'">
                        <input type="checkbox" id="reviewer1{{$index}}" switch="none" checked=""
                            ng-click="reviewersVm.updateReviewer(reviewer)" ng-model="reviewer.approver">
                        <label for="reviewer1{{$index}}" data-on-label="Yes" data-off-label="No"></label>
                    </div>
                    <div ng-if="reviewersVm.type == 'REQUIREMENTDOCUMENTTEMPLATE' || reviewersVm.type == 'REQUIREMENTTEMPLATE'"
                        class="remove-approver" ng-click="reviewersVm.deleteReviewer(reviewer)">
                        <i class="la la-times" title="Remove this reviewer"></i>
                    </div>
                </div>
            </div>
            <div style="display: flex;">
                <div class="reviewer-desc">
                    <div class="reviewer-name" translate>NOTES</div>
                    <div class="reviewer-role">{{reviewer.notes}}</div>
                </div>
            </div>
        </div>
    </div>
</div>