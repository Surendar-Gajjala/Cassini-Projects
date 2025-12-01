<div style="padding: 0 20px;">
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
            min-height: 75px;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 10px 10px 10px 20px;
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
            flex: 0.5;
            padding: 2px 10px;
            text-align: center;
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

        .plan-comments-container ul.timeline.no-comments:before {
            display: none;
        }

        .plan-comments-container ul.timeline {
            list-style-type: none;
            position: relative;
            width: 100% !important;
            height: 100% !important;
            margin-top: 0 !important;
            padding-top: 0;
        }

        .plan-comments-container ul.timeline:before {
            content: ' ';
            background: #d4d9df;
            display: inline-block;
            position: absolute;
            left: 29px;
            width: 2px;
            height: 100%;
            z-index: 400;
        }

        .plan-comments-container ul.timeline > li {
            padding: 0 0 0 20px;
            margin-bottom: 20px;
        }

        .plan-comments-container ul.timeline > li:before {
            content: ' ';
            display: inline-block;
            position: absolute;
            border-radius: 50%;
            border: 3px solid #1E88E5;
            left: 22px;
            width: 10px;
            height: 10px;
            z-index: 400;
            background-color: #1E88E5;
            margin-top: 5px;
        }

        .plan-comments-container ul.timeline li div.timeline-event {
            margin-left: 30px;
            margin-right: 20px;
        }

        .plan-comments-container ul.timeline li div.timeline-event .comment-user {
            color: #337ab7;
        }

        .plan-comments-container ul.timeline li div.timeline-event .comment-user i.fa-comments-o {
            font-size: 14px;
            margin-left: 5px;
            cursor: pointer;
            opacity: 0.6;
        }

        .plan-comments-container ul.timeline li div.timeline-event .comment-timestamp {
            font-size: 12px;
            text-align: left;
            opacity: 0.7;
            font-weight: 400;
        }

        .plan-comments-container ul.timeline li div.timeline-event .comment-message {

        }

        .plan-comments-container ul.timeline li div.timeline-event .float-right {
            float: right;
            margin-right: 20px;
        }

        .flex-box {
            display: flex;
        }

        .flex-box .flex-item {
            flex: 1;
            margin-right: 10px;
        }

        form.message-form {
            text-align: center;
            padding-top: 10px;
            /*position: sticky;*/
            top: 0;
            /*margin-bottom: 20px !important;*/
            background-color: var(--cassini-bg-color);
            z-index: 9999;
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
        }

        form.message-form i.chat-icon {
            position: absolute;
            margin-top: 12px !important;
            margin-left: 10px;
            color: #ccc;
            font-size: 16px;
            margin-bottom: 20px;
        }

        form.message-form i.send-icon {
            color: #a9a9a9;
            cursor: pointer;
            position: absolute;
            margin-left: -25px;
            margin-top: 12px;
        }

        form.message-form i.attachments-icon {
            margin-left: -45px;
            color: #a9a9a9;
            cursor: pointer;
            position: absolute;
            margin-top: 12px;
        }

        form.message-form textarea {
            border-radius: 3px;
            display: inline-block !important;
            padding-left: 30px !important;
            padding-right: 50px;
            /*width: 350px;*/
            margin-left: auto;
            margin-right: auto;
            background-color: rgb(241, 243, 244);
            resize: none;
            overflow: hidden;
            height: 33px;
            padding-top: 6px;
        }

        form.message-form textarea::placeholder {
            color: #a3a5a6;
            opacity: 1;
            font-size: 14px;
        }

        .comments-panel {
            height: 100%;
        }

        .comments-header {
            display: flex;
        }

        .comments-header > div {
            flex: 1;
        }

        .line-ht-40 {
            line-height: 40px;
        }

        .mr-10 {
            margin-right: 10px;
        }

        .mr-15 {
            margin-right: 15px;
        }

        .mr-20 {
            margin-right: 20px;
        }

        .plan-comments-container {
            height: 100%;
        }

        .plan-comments-container .widget-body {
            overflow-y: auto;
        }

        .cursor-pointer {
            cursor: pointer;
        }

        .comment-images-container {
            display: flex;
            height: 100%;
            flex-wrap: wrap;
        }

        .comment-images-container .comment-image {
            height: 50px;
            width: 50px;
            padding: 3px;
            cursor: pointer;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin: 3px;
        }

        .comment-images-container .comment-image:hover {
            border: 1px solid #1488CC;
        }

        .comment-image-previewer-sidepanel.comment-image-modal.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(45, 55, 72, 0.77); /* Black w/ opacity */
            z-index: 9999;
        }

        .comment-image-previewer-sidepanel.comment-image-modal .comment-image-content {
            margin: auto;
            display: block;
            height: calc(100% - 30px);
            width: 100%;
            border-radius: 7px !important;
        }

        .comment-image-previewer-sidepanel .image-content {
            height: 100%;
        }

        .comment-image-previewer-sidepanel .image-view {
            width: 100%;
        }

        .comment-image-previewer-sidepanel .comment-image-close {
            position: absolute;
            margin-left: -19px;
            margin-top: -19px;
            width: 38px;
            height: 38px;
            opacity: 0.6;
            background-color: #ddd;
            border-radius: 50%;
            cursor: pointer;
        }

        .comment-image-previewer-sidepanel .comment-image-close:hover {
            opacity: 0.9;
            border-radius: 50%;
            background-color: #ddd;
        }

        .comment-image-previewer-sidepanel .comment-image-close:before, .comment-image-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .comment-image-previewer-sidepanel .comment-image-close:before {
            transform: rotate(45deg) !important;
        }

        .comment-image-previewer-sidepanel .comment-image-close:after {
            transform: rotate(-45deg) !important;
        }

        .comments-file-dropzone {
            min-height: 150px;
            border: 1px dashed #b6b6b6;
            border-radius: 5px;
            margin: 20px 50px 0 50px;
            cursor: pointer;
        }

        .comments-file-dropzone .drop-files-label {
            font-style: italic;
            text-align: center;
            line-height: 140px
        }

        .files-preview {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-start;
        }

        .files-preview .dz-preview {
            flex: 1 0 0;
            white-space: nowrap;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin: 10px;
        }

        .files-preview .dz-preview .progress {
            height: 5px;
            margin-bottom: 0;
        }

        .comment-attachments-container {
            display: flex !important;
            flex-direction: row !important;
            flex-wrap: wrap !important;
        }

        .plan-comments-container .comment-attachment {
            padding: 1px 5px !important;
            border: 1px solid #ddd !important;
            border-radius: 3px !important;
            margin: 5px !important;
            cursor: pointer !important;
            background-color: #D7DDE8 !important; /* fallback for old browsers */

        }

        .comment-attachments-container .comment-attachment:hover {
            background: #0060df; /* fallback for old browsers */
            background: -webkit-linear-gradient(to top, #003eaa, #0060df); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to top, #003eaa, #0060df); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
            border: 0 solid #0060df !important;
            padding: 2px 6px !important;
            color: #fff !important;
        }

        .comment-attachments-container .comment-attachment .attachment-name {

        }

        .comment-attachments-container .comment-attachment .attachment-size {

        }

        .comments-panel .comments-body {
        }

        .comments-panel .comments-body i.fa-comments-o {
            font-size: 16px !important;
        }

        .no-conversations-container {
            text-align: center;
        }

        .no-conversations-container .no-conversations {
            margin-top: 100px;
        }

        .no-conversations-container .no-conversations img {
            width: 300px;
        }

        .no-conversations-container .no-conversations .no-conversations-message {
            font-size: 20px;
            font-weight: 400 !important;
        }

        .no-conversations-container .no-conversations .no-conversations-suggestion {
            font-size: 14px;
            font-weight: 300 !important;
        }

        .plan-comments-container .comments-file-dropzone i.la-close {
            margin-top: 0;
            padding: 5px;
            font-size: 14px;
            position: absolute;
            right: 65px;
            font-weight: 600;
        }

        .plan-comments-container .comments-file-dropzone i.la-close:hover {
            background-color: #ddd;
            border-radius: 50%;
            font-weight: 600;
        }

        .modal-open .modal {
            z-index: 10000 !important;
        }

        .plan-approve-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 100px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .plan-approve-model .planModel-content {
            margin: auto;
            display: block;
            height: 260px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        .plan-approve-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
            font-weight: bold;
            font-size: 22px;
        }

        .plan-approve-footer {
            padding: 10px;
            text-align: right;
            height: 50px;
        }

        .plan-approve-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }
    </style>
    <div>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <div class="plan-comments-container"
             ng-if="supplierPlanDetailsVm.supplierAuditPlan.status != 'APPROVED' && (!loginPersonDetails.external || (loginPersonDetails.external && sharedPermission == 'WRITE'))">
            <div class="comments-panel">
                <div class="comments-body">
                    <form class="message-form">
                        <i class="fa fa-comments-o chat-icon"></i>
                            <textarea rows="1" class="message-text form-control input-sm" id="messageText"
                                      placeholder="{{supplierPlanDetailsVm.placeholderText}}"
                                      ng-keypress="($event.charCode==13 && $event.ctrlKey == true)? supplierPlanDetailsVm.createComment() : return;"
                                      ng-model="supplierPlanDetailsVm.newComment.comment"></textarea>
                        <i class="fa fa-paperclip attachments-icon"
                           ng-click="supplierPlanDetailsVm.toggleCommentFiles()"></i>
                        <i class="fa fa-send-o send-icon" ng-click="supplierPlanDetailsVm.createComment()"></i>

                        <div id="commentFiles" class="comments-file-dropzone"
                             ng-click="supplierPlanDetailsVm.selectFiles()"
                             ng-if="supplierPlanDetailsVm.showFilesDropZone">
                            <i class="la la-close" title="Close"
                               ng-click="supplierPlanDetailsVm.toggleCommentFiles()"></i>

                            <div class="drop-files-label">{{"DROP_FILES_OR_CLICK" |
                                translate}}
                            </div>

                            <div id="fileUploadPreviews" class="files-preview">
                                <div class="dz-preview dz-file-preview" id="fileUploadTemplate"
                                     style="display: none">
                                    <div class="dz-details">
                                        <div class="dz-filename"><span data-dz-name></span></div>
                                        <div class="dz-size" data-dz-size></div>
                                    </div>
                                    <div class="dz-progress"><span class="dz-upload" data-dz-uploadprogress></span>
                                    </div>
                                    <div class="progress progress-striped active" style="display: none;">
                                        <div class="progress-bar"
                                             role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                             aria-valuemax="100" style="width: 100%">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row" style="padding-bottom: 20px !important;">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span style="font-weight: bold" translate>Planned Date</span> :
                </label>

                <div class="col-sm-7" style="margin-top: 10px;">
                    {{supplierPlanDetailsVm.supplierAuditPlan.plannedStartDate}}
                </div>

            </div>

            <div class="form-group">

                <label class="col-sm-4 control-label">
                    <span style="font-weight: bold" translate>Completed Date</span> :
                </label>

                <div class="col-sm-7" style="margin-top: 10px;">
                    {{supplierPlanDetailsVm.supplierAuditPlan.finishedDate}}
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span style="font-weight: bold" translate>Prepared By</span> : </label>

                <div class="col-sm-7" style="margin-top: 10px;">
                    {{supplierPlanDetailsVm.supplierAuditPlan.createdByObject.fullName}}
                </div>

            </div>
        </form>

        <%--<div class="responsive-table" style="padding-top: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="col-width-150" translate>NAME</th>
                    <th class="col-width-100" translate>STATUS</th>
                    <th translate>COMMENT</th>
                    <th class="text-center col-width-100" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="supplierPlanDetailsVm.reviewers.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <div class="message" translate>NO_USERS</div>
                        </div>
                    </td>

                </tr>

                <tr ng-repeat="reviewer in supplierPlanDetailsVm.reviewers">
                    <td class="col-width-150">
                        {{reviewer.login.person.fullName}}
                    </td>
                    <td class="col-width-100">
                        <object-status object="reviewer"></object-status>
                    </td>
                    <td>
                        {{reviewer.notes}}
                    </td>
                    <td class="text-center col-width-100">
                        <span ng-if="reviewer.showApproveButton || reviewer.showReviewButton" class="row-menu"
                              uib-dropdown dropdown-append-to-body
                              style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li style="cursor: pointer" ng-if="reviewer.showApproveButton"
                                    ng-click="supplierPlanDetailsVm.showSubmitDialog('Approve',reviewer)">
                                    <a translate>APPROVE</a>
                                </li>
                                <li ng-if="reviewer.showApproveButton"
                                    ng-click="supplierPlanDetailsVm.showSubmitDialog('Reject',reviewer)"
                                    style="cursor: pointer">
                                    <a translate>REJECT</a>
                                </li>
                                <li ng-if="reviewer.showReviewButton"
                                    ng-click="supplierPlanDetailsVm.showSubmitDialog('Review',reviewer)"
                                    style="cursor: pointer">
                                    <a translate>REVIEW</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>--%>
    </div>
    <div class="view-tb"
         ng-if="supplierPlanDetailsVm.supplierAuditPlan.status != 'APPROVED' && !loginPersonDetails.external"
         uib-dropdown
         dropdown-toggle uib-dropdown-toggle>
        <span>
            <span class="add-user-icon"><i class="las la-user-plus"></i></span>
            <span class="add-user-label" translate>Add Approvers</span>
        </span>
        <ul uib-dropdown-menu class="dropdown-menu" role="menu"
            style="z-index: 9999 !important;" ng-click="$event.stopPropagation()">
            <li>
                <div class="users-container">
                    <div class="users-search" ng-if="supplierPlanDetailsVm.logins.length > 0">
                        <input type="text" class="form-control" ng-model="supplierPlanDetailsVm.searchUsers">
                        <i class="la la-times"
                           ng-if="supplierPlanDetailsVm.searchUsers != null && supplierPlanDetailsVm.searchUsers.trim() !== ''"
                           ng-click="supplierPlanDetailsVm.searchUsers =  '';"></i>
                    </div>
                    <div class="no-users" ng-if="supplierPlanDetailsVm.logins.length == 0">
                        {{noUserSelect}}
                    </div>
                    <div class="user-cards">
                        <div class="user-card d-flex"
                             ng-repeat="login in supplierPlanDetailsVm.logins | filter: {person: {fullName: supplierPlanDetailsVm.searchUsers}}"
                             ng-click="supplierPlanDetailsVm.addReviewer(login)">
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
    <div class="help-text"
         ng-if="supplierPlanDetailsVm.supplierAuditPlan.status != 'APPROVED' && !loginPersonDetails.external">
        {{selectReviewerApproverMsg}}
    </div>
    <div class="reviewers-container">
        <div class="reviewer-card" ng-repeat="reviewer in supplierPlanDetailsVm.reviewers">
            <div style="display: flex;">
                <div class="reviewer-avatar">
                    <person-avatar person-id="reviewer.login.person.id" display="'normal'"
                                   ng-if="reviewer.login != null"></person-avatar>
                </div>
                <div class="reviewer-desc">
                    <div class="reviewer-name">{{reviewer.login.person.fullName}}</div>
                    <div class="reviewer-role">{{reviewer.login.person.defaultGroupObject.name}}</div>
                </div>
                <div class="reviewer-approval">
                    <div class="text-light">Status</div>
                    <div>
                        <object-status object="reviewer"></object-status>
                    </div>
                </div>
                <div class="reviewer-approval">
                    <div class="text-light">Approver</div>
                    <div ng-class="{'disabled':reviewer.status != 'NONE' || loginPersonDetails.external}">
                        <input type="checkbox" id="reviewer{{$index}}" switch="none" checked=""
                               ng-click="supplierPlanDetailsVm.updateReviewer(reviewer)"
                               ng-model="reviewer.approver">
                        <label for="reviewer{{$index}}" data-on-label="Yes" data-off-label="No"></label>
                    </div>
                    <div ng-class="{'disabled':reviewer.status != 'NONE' || loginPersonDetails.external}"
                         class="remove-approver"
                         ng-click="supplierPlanDetailsVm.deleteReviewer(reviewer)">
                        <i class="la la-times" title="Remove this reviewer"></i>
                    </div>
                </div>
            </div>
            <div style="display: flex;">
                <div class="reviewer-desc" style="padding-left: 65px;">
                    <div class="reviewer-name" translate>NOTES</div>
                    <div class="reviewer-role">{{reviewer.notes}}</div>
                </div>
                <div class="reviewer-approval" ng-if="supplierPlanDetailsVm.supplierAuditPlan.status != 'APPROVED'">
                    <div class="btn-group" ng-if="reviewer.approver && reviewer.showApproveButton">
                        <button class="btn btn-xs btn-success"
                                ng-disabled="supplierPlanDetailsVm.supplierAuditPlan.status != 'COMPLETED'"
                                ng-click="supplierPlanDetailsVm.showSubmitDialog('Approve',reviewer)" translate>APPROVE
                        </button>
                        <button class="btn btn-xs btn-danger"
                                ng-disabled="supplierPlanDetailsVm.supplierAuditPlan.status != 'COMPLETED'"
                                ng-click="supplierPlanDetailsVm.showSubmitDialog('Reject',reviewer)" translate>REJECT
                        </button>
                    </div>
                    <div class="btn-group" ng-if="!reviewer.approver && reviewer.showReviewButton">
                        <button class="btn btn-xs btn-primary"
                                ng-disabled="supplierPlanDetailsVm.supplierAuditPlan.status != 'COMPLETED'"
                                ng-click="supplierPlanDetailsVm.showSubmitDialog('Review',reviewer)" translate>REVIEW
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <div id="commentsContainer" class="plan-comments-container">
            <div class="comments-panel">
                <div id="commentsBody" class="comments-body">
                    <ul class="timeline" ng-class="{'no-comments': supplierPlanDetailsVm.comments.length == 0}">
                        <li ng-repeat="comment in supplierPlanDetailsVm.comments">
                            <div class="timeline-event">
                                <div class="comment-user">
                                    <span>{{comment.commentedBy.fullName}}</span>
                                    <i class="la la-trash" style="cursor: pointer"
                                       ng-click="deleteConversation(comment)" title="{{deleteConversationTitle}}"
                                       ng-if="comment.commentedBy.id == loginPersonDetails.person.id"></i>
                                </div>
                                <div class="comment-timestamp">{{comment.time}}</div>
                                <p class="comment-message" ng-bind-html="comment.comment"></p>

                                <div class="comment-images-container">
                                    <img class="comment-image"
                                         ng-repeat="image in comment.images"
                                         ng-click="supplierPlanDetailsVm.showImages(comment,image)"
                                         ng-src="api/col/comments/image/{{image.id}}"/>

                                </div>
                                <div class="comment-attachments-container">
                                    <div class="comment-attachment"
                                         ng-repeat="attachment in comment.attachments"
                                         ng-click="supplierPlanDetailsVm.downloadFile(attachment)">
                                        <div class="attachment-name">{{attachment.name}} [{{attachment.size | bytes}}]
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>

            <div id="comment-image-previewer-sidepanel"
                 class="comment-image-previewer-sidepanel comment-image-modal modal">
                <div class="comment-image-content">
                    <div class="image-content" style="display: flex;width: 100%;">
                        <div class="image-view">
                            <div id="commentImage{{image.id}}"
                                 ng-repeat="image in supplierPlanDetailsVm.selectedCommentImages"
                                 ng-show="image.showImage"
                                 style="width: 100%; height: 100%;display: table-cell;vertical-align: middle;text-align: center;">
                                <i class="fa fa-angle-left" ng-click="supplierPlanDetailsVm.showPreviousImage(image)"
                                   ng-if="supplierPlanDetailsVm.selectedCommentImages.length > 1"
                                   style="font-size: 50px;color: white;position: absolute;top: 45%;left: 5%;cursor: pointer;"></i>
                                <img ng-src="api/col/comments/image/{{image.id}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                <a href="" ng-click="supplierPlanDetailsVm.hideImagesView()"
                                   class="comment-image-close pull-right"
                                   style="display: inline-block"></a>
                                <i class="fa fa-angle-right" ng-click="supplierPlanDetailsVm.showNextImage(image)"
                                   ng-if="supplierPlanDetailsVm.selectedCommentImages.length > 1"
                                   style="font-size: 50px;color: white;position: absolute;top: 45%;right: 5%;cursor: pointer;"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="plan-approve-modal" class="plan-approve-model modal">
        <div class="planModel-content">
            <div class="plan-approve-header">
                <span>{{supplierPlanDetailsVm.approveType}}</span>
            </div>
            <div class="plan-approve-content">
                <p ng-if="supplierPlanDetailsVm.selectedReviewer.notes == null && supplierPlanDetailsVm.error != ''"
                   style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{supplierPlanDetailsVm.error}}
                </p>

                <div class="form-group">
                    <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                        <span translate>COMMENT</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <textarea type="req" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_COMMENT' | translate}}" rows="3"
                                  ng-model="supplierPlanDetailsVm.selectedReviewer.notes"></textarea>
                    </div>
                </div>
            </div>
            <div class="plan-approve-footer">
                <button class="btn btn-xs btn-default"
                        ng-click="supplierPlanDetailsVm.hideSubmitDialog()" translate>
                    CANCEL
                </button>
                <button class="btn btn-xs btn-success"
                        ng-click="supplierPlanDetailsVm.submitReview()" translate>
                    SUBMIT
                </button>
            </div>
        </div>
    </div>
</div>