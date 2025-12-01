<div style="display:flex; flex-direction:column;margin-bottom: 20px;">
    <style scoped>
        .conversations-container ul.timeline.no-conversations:before {
            display: none;
        }

        .conversations-container ul.timeline {
            list-style-type: none;
            position: relative;
            width: 100% !important;
            margin-top: 0 !important;
        }

        .conversations-container ul.timeline:before {
            content: ' ';
            background: #d4d9df;
            display: inline-block;
            position: absolute;
            left: 29px;
            width: 2px;
            height: 100%;
            z-index: 400;
        }

        .conversations-container ul.timeline > li {
            padding: 0 0 20px 20px;
            /*margin-bottom: 20px;*/
        }

        .conversations-container > .conversations-panel > .widget-body > ul.timeline:first-child {
            padding-top: 50px;
        }

        .conversations-container ul.timeline > li:nth-child(2) {
            margin-top: 20px;
        }

        .conversations-container ul.timeline > li:before {
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

        .conversations-container ul.timeline > li.markAsRead {
            font-weight: 600;
            /*padding-top: 10px;
            cursor: pointer;
            background-color: #ddf7dd;
            border-bottom: 1px solid lightcyan;*/
        }

        .conversations-container ul.timeline > li.markAsRead:before {
            border: 3px solid green !important;
            background-color: green !important;
        }

        .conversations-container ul.timeline > li.markAllAsRead:before {
            border: none !important;
            background-color: transparent !important;
        }

        .conversations-container ul.timeline li div.timeline-event {
            margin-left: 30px;
            margin-right: 20px;
        }

        .conversations-container ul.timeline li div.timeline-event .comment-user {
            color: #337ab7;
        }

        .comment-user i.la {
            font-size: 14px;
        }

        .conversations-container ul.timeline li div.timeline-event .comment-user i.fa-comments-o {
            font-size: 14px;
            margin-left: 5px;
            cursor: pointer;
            font-weight: 400;
        }

        .conversations-container ul.timeline li div.timeline-event .comment-timestamp {
            font-weight: 400;
            font-size: 12px;
            text-align: left;
        }

        .conversations-container ul.timeline li div.timeline-event .comment-message {
            /*white-space: pre !important;*/
        }

        .conversations-container ul.timeline li div.timeline-event .float-right {
            float: right;
            margin-right: 20px;
        }

        .conversations-panel .comment-user i.la-trash {
            position: absolute;
            margin-top: 2px;
            margin-left: 2px;
            visibility: hidden;
        }

        .conversations-panel .timeline-event:hover .comment-user i.la-trash {
            position: absolute;
            margin-top: 2px;
            margin-left: 2px;
            visibility: visible;
        }

        .conversations-panel .comment-user span.mark-as-read {
            position: absolute;
            margin-top: 2px;
            margin-left: 2px;
            visibility: hidden;
        }

        .conversations-panel .timeline-event:hover .comment-user span.mark-as-read {
            visibility: visible;
            cursor: pointer;
            color: initial;
            margin-left: 35px;
            font-weight: normal;
        }

        .conversations-panel .comment-user i.la-pencil {
            position: absolute;
            margin-top: 2px;
            margin-left: 2px;
            visibility: hidden;
        }

        .conversations-panel .timeline-event:hover .comment-user i.la-pencil {
            position: absolute;
            margin-top: 2px;
            margin-left: 2px;
            visibility: visible;
        }

        .flex-box {
            display: flex;
        }

        .flex-box .flex-item {
            flex: 1;
            margin-right: 10px;
        }

        .conversations-container form.message-form {
            margin-top: -10px;
            text-align: center;
            padding-top: 5px;
            position: fixed;
            top: 109px;
            margin-bottom: 30px !important;
            background-color: #f9fbfe;
            width: 700px;
            z-index: 9998;
            border-bottom: 1px solid #eee;
            padding-bottom: 5px;
        }

        .conversations-container form.message-form i.chat-icon {
            position: absolute;
            margin-top: 12px;
            margin-left: -20px;
            color: var(--cassini-font-color);
            font-size: 16px;
            margin-bottom: 20px;
            display: inline;
            left: 178px;
            opacity: 0.6;
        }

        .conversations-container form.message-form i.send-icon {
            color: var(--cassini-font-color);
            cursor: pointer;
            position: absolute;
            margin-left: 155px;
            top: 18px;
            display: inline-block;
            opacity: 0.6;
        }

        i.send-icon.disabled {
            cursor: not-allowed !important;
            pointer-events: none !important;
        }

        .conversations-container form.message-form i.attachments-icon {
            color: var(--cassini-font-color);
            cursor: pointer;
            display: inline;
            position: absolute;
            margin-left: 140px;
            top: 18px;
            opacity: 0.6;
        }

        .conversations-container form.message-form .message-text {
            width: 100%;
            resize: none;
            padding-left: 30px;
            background-color: rgb(241, 243, 244);
            overflow: hidden;
            height: 35px;
            padding-top: 6px;
            padding-right: 50px;
        }

        .conversations-container form.message-form input::placeholder {
            opacity: 0.5;
            font-size: 14px;
        }

        .conversations-container form.message-form i.search-icon {
            position: absolute;
            margin-top: 13px;
            margin-left: 10px;
            color: var(--cassini-font-color);
            font-size: 12px;
            margin-bottom: 20px;
            opacity: 0.6;
        }

        .conversations-container form.message-form .message-search-container i.clear-search {
            position: absolute;
            top: 17px;
            right: 25px;
            cursor: pointer;
            color: var(--cassini-font-color);
            opacity: 0.6;
            font-size: 14px;
        }

        .conversations-container form.message-form input.message-search {
            border-radius: 3px;
            margin-left: auto;
            margin-right: auto;
            background-color: rgb(241, 243, 244);
            display: inline;
            max-width: 150px;
            padding: 5px 30px 5px 27px;
        }

        form.message-form .message-search-container {
            padding-right: 15px;
            text-align: right;
        }

        .conversations-panel {
            height: 100%;
        }

        .conversations-header {
            display: flex;
        }

        .conversations-header > div {
            flex: 1;
        }

        .conversations-container {
            height: 100%;
            max-height: 1555px;
        }

        .conversations-container .widget-body {
            overflow-y: auto;
        }

        .conversations-container .comment-images-container {
            display: flex;
            height: 100%;
            flex-wrap: wrap;
        }

        .conversations-container .comment-images-container .comment-image {
            height: 50px;
            width: 50px;
            padding: 3px;
            cursor: pointer;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin: 3px;
        }

        .conversations-container .comment-images-container .comment-image:hover {
            border: 1px solid #1488CC;
        }

        .conversations-container .comment-image.modal {
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

        .conversations-container .comment-image .comment-image-content {
            margin: auto;
            display: block;
            height: calc(100% - 30px);
            width: 100%;
            border-radius: 7px !important;
        }

        .conversations-container .image-content {
            height: 100%;
        }

        .conversations-container .image-view {
            width: 100%;
        }

        .conversations-container .comment-image-close {
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

        .conversations-container .comment-image-close:hover {
            opacity: 0.9;
            border-radius: 50%;
            background-color: #ddd;
        }

        .conversations-container .comment-image-close:before, .comment-image-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .conversations-container .comment-image-close:before {
            transform: rotate(45deg) !important;
        }

        .conversations-container .comment-image-close:after {
            transform: rotate(-45deg) !important;
        }

        .conversations-container .comments-file-dropzone {
            min-height: 150px;
            border: 1px dashed #b6b6b6;
            border-radius: 5px;
            margin: 10px 20px 20px 30px;
            cursor: pointer;
        }

        .conversations-container .comments-file-dropzone i.la-close {
            margin-top: 5px;
            padding: 5px;
            font-size: 14px;
            position: absolute;
            right: 28px;
        }

        .conversations-container .comments-file-dropzone i.la-close:hover {
            background-color: #ddd;
            border-radius: 50%;
            font-weight: 600;
        }

        .conversations-container .comments-file-dropzone .drop-files-label {
            font-style: italic;
            text-align: center;
            line-height: 140px
        }

        .conversations-container .files-preview {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-start;
        }

        .conversations-container .files-preview .dz-preview {
            flex: 1 0 0;
            white-space: nowrap;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin: 10px;
        }

        .conversations-container .files-preview .dz-preview .progress {
            height: 5px;
            margin-bottom: 0;
        }

        .conversations-container .comment-attachments-container {
            display: flex !important;
            flex-direction: row !important;
            flex-wrap: wrap !important;
        }

        .conversations-container .comment-attachment {
            padding: 1px 5px !important;
            border: 1px solid #ddd !important;
            border-radius: 3px !important;
            margin: 5px !important;
            cursor: pointer !important;
            background-color: #D7DDE8 !important; /* fallback for old browsers */

        }

        .conversations-container .comment-attachments-container .comment-attachment:hover {
            background: #0060df; /* fallback for old browsers */
            background: -webkit-linear-gradient(to top, #003eaa, #0060df); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to top, #003eaa, #0060df); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
            border: 0 solid #0060df !important;
            padding: 2px 6px !important;
            color: #fff !important;
        }

        .conversations-container .comment-attachments-container .comment-attachment .attachment-name {

        }

        .conversations-container .comment-attachments-container .comment-attachment .attachment-size {

        }

        .conversations-container .timeline .conversations-loading {
            padding-left: 20px;
        }

        .conversations-container .timeline .conversations-loading img {
            opacity: 0.5;
            margin-right: 5px;
        }

        .conversations-container .message-form .pagenated-results {
            padding-left: 20px;
            display: table-cell;
            vertical-align: middle;
            height: 40px;
            line-height: 20px;
            font-weight: 300;
        }

        .comment-message .highlighted {
            background-color: #08f80c8f;
            padding: 3px;
        }

        .conversations-container .no-conversations-container {
            text-align: center;
        }

        .conversations-container .no-conversations-container .no-conversations {
            margin-top: 100px;
        }

        .conversations-container .no-conversations-container .no-conversations img {
            width: 400px;
        }

        .conversations-container .no-conversations-container .no-conversations .no-conversations-message {
            font-size: 20px;
            font-weight: 300 !important;
        }

        .conversations-container .no-reults-container {
            position: absolute;
            top: 300px;
            bottom: 0;
            left: 0;
            right: 0;
        }

        .conversations-container .no-reults-container > .no-results {
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            position: absolute;
            width: 100%;
        }

        .conversations-container .no-reults-container > .no-results > img {
            width: 300px;
        }

        .conversations-container .no-reults-container > .no-results > .no-results-message {
            font-size: 20px;
            font-weight: 400 !important;
        }

        .conversations-container .no-reults-container > .no-results > .no-results-suggestion {
            font-size: 14px;
            font-weight: 300 !important;
        }

        .conversations-container .conversations-panel .message-form .row-container {
            display: flex;
        }

        .conversations-container .conversations-panel .message-form .row-container .pagenated-results-container {
            flex: 1;
            max-width: 150px;
        }

        .conversations-container .conversations-panel .message-form .row-container .message-search-container {
            flex: 1;
            max-width: 175px;
            margin-left: 10px;
        }

        .conversations-container .conversations-panel .message-form .row-container .message-text-container {
            flex: 1;
        }

        .modal-open .modal {
            z-index: 10000 !important;
        }

        .ml-25 {
            margin-left: 20px !important;
        }

        .show-all-unread {
            color: #16325C;
            cursor: pointer;
            font-size: 14px;
        }

        .label-default {
            color: #fff !important;
        }
    </style>

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <div id="conversationsContainer" class="conversations-container">
        <div class="conversations-panel home-widget">
            <div id="conversationsBody" class="widget-body">
                <ul class="timeline" ng-class="{'no-conversations': conversationsVm.comments.length == 0}">
                    <form class="message-form">
                        <div class="row-container" style="margin:0">
                            <div class="pagenated-results-container" style="padding: 0;text-align: left">
                                <span class="pagenated-results" style=""
                                      ng-if="!conversationsVm.loading && conversationsVm.comments.length > 0">
                                    Showing
                                    {{conversationsVm.comments.length}}
                                    of
                                    {{conversationsVm.pagenatedResults.totalElements}}
                                </span>
                                <span class="pagenated-results" style=""
                                      ng-if="!conversationsVm.loading && conversationsVm.comments.length == 0">
                                    Showing 0 of 0
                                </span>
                                <span class="pagenated-results" style=""
                                      ng-if="conversationsVm.loading">
                                    Loading...
                                </span>
                            </div>
                            <div class="message-text-container" style="padding: 0;">
                                <i class="fa fa-comments-o chat-icon"></i>
                                <textarea rows="1" class="message-text form-control input-sm" id="messageText"
                                          placeholder="{{startConversation}}"
                                          ng-keypress="($event.charCode==13 && $event.ctrlKey == true)? conversationsVm.createComment() : return;"
                                          ng-model="conversationsVm.newComment.comment"></textarea>
                                <i class="fa fa-paperclip attachments-icon" title="Add attachments"
                                   ng-click="conversationsVm.toggleCommentFiles()"></i>
                                <i class="fa fa-send-o send-icon" title="Submit"
                                   ng-click="conversationsVm.createComment()"
                                   ng-class="{'disabled':(conversationsVm.newComment.comment == null || conversationsVm.newComment.comment == '') && conversationsVm.commentFiles.length == 0}"></i>
                            </div>
                            <div class="message-search-container">
                                <i class="fa fa-search search-icon"></i>
                                <input type="text" class="message-search form-control input-sm"
                                       placeholder="{{search}}"
                                       ng-model="conversationsVm.searchQuery"
                                       ng-model-options="{ debounce: 500 }"
                                       ng-change="conversationsVm.comments=[];conversationsVm.searchComments()"
                                       ng-enter="conversationsVm.searchComments()">
                                <i class="la la-times clear-search" title="{{clearSearch}}"
                                   ng-show="conversationsVm.searchQuery.length > 0"
                                   ng-click="conversationsVm.comments=[];conversationsVm.searchQuery = '';conversationsVm.clearSearch()"></i>
                            </div>
                        </div>
                        <div id="commentFiles" class="comments-file-dropzone" ng-click="selectFiles()"
                             ng-if="conversationsVm.showFilesDropZone">
                            <i class="la la-close" title="Close" ng-click="conversationsVm.toggleCommentFiles()"></i>

                            <div class="drop-files-label">{{"DROP_FILES_OR_CLICK" | translate}}</div>

                            <div id="fileUploadPreviews" class="files-preview">
                                <div class="dz-preview dz-file-preview" id="fileUploadTemplate" style="display: none">
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


                    <div class="conversations-loading" ng-if="conversationsVm.loading" style="margin-top: 10px">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader17.gif"
                             class="mr5"><span translate>LOADING_CONVERSATIONS</span>
                    </div>

                    <div class="no-conversations-container"
                         ng-if="!conversationsVm.loading && conversationsVm.comments.length == 0 && !conversationsVm.searchMode">
                        <div class="no-conversations">
                            <img src="app/assets/images/plm-conversations.jpg" alt="">

                            <div class="no-conversations-message">{{conversationMessage}}</div>
                        </div>
                    </div>

                    <div class="no-reults-container"
                         ng-if="!conversationsVm.loading && conversationsVm.comments.length == 0 && conversationsVm.searchMode">
                        <div class="no-results">
                            <img src="app/assets/images/no-results-found.png" alt="">

                            <div class="no-results-message">Ooops...We did not find anything that matches this search
                                :(
                            </div>
                            <div class="no-results-suggestion">Try searching for something else or check for spelling
                                mistakes.
                            </div>
                        </div>
                    </div>
                    <li class="markAllAsRead" style="padding-left: 41px;"
                        ng-if="conversationsVm.unreadMessagesCount > 0">
                        <span ng-click="conversationsVm.showAllUnreadMessages()" class="label show-all-unread"
                              ng-class="{'label-default':conversationsVm.unreadMessages}">Show all unread
                        </span>
                        <i ng-if="conversationsVm.unreadMessages == true"
                           class="la la-times-circle" style="cursor: pointer;"
                           ng-click="conversationsVm.unreadMessages = false;conversationsVm.loadComments(false)"></i>
                        <span>| </span>
                        <span style="cursor: pointer;" ng-click="conversationsVm.updateConversationAsRead()">Mark all as read</span>
                        <span class="badge badge-primary">{{conversationsVm.unreadMessagesCount}}</span>
                    </li>
                    <li ng-repeat="comment in conversationsVm.comments"
                        ng-class="{'markAsRead':!comment.read}">
                        <div class="timeline-event">
                            <div class="flex-box">
                                <div class="comment-user flex-item" style="flex: 1;">
                                    <span>{{comment.commentedBy.fullName}}</span>
                                    <i class="la la-pencil" style="cursor: pointer"
                                       ng-click="editConversation(comment)" title="{{editConversationTitle}}"
                                       ng-if="comment.commentedBy.id == loginPersonDetails.person.id && (comment.editMode == false || comment.editMode == undefined)"></i>
                                    <i class="la la-trash" style="cursor: pointer"
                                       ng-class="{'ml-25':comment.commentedBy.id == loginPersonDetails.person.id}"
                                       ng-click="deleteConversation(comment)" title="{{deleteConversationTitle}}"
                                       ng-if="loginPersonDetails.isAdmin && (comment.editMode == false || comment.editMode == undefined)"></i>
                                    <span class="mark-as-read"
                                          ng-click="conversationsVm.updateUserReadComment(comment)"
                                          ng-if="!comment.read">Mark as Read</span>
                                    <i class="la la-check" style="cursor: pointer"
                                       ng-click="updateComment(comment)" title="{{updateConversationTitle}}"
                                       ng-if="comment.editMode"></i>
                                    <i class="la la-times" style="cursor: pointer;"
                                       ng-click="cancelChanges(comment)" title="{{cancelChangesTitle}}"
                                       ng-if="comment.editMode"></i>
                                    <%--<i class="fa fa-comments-o" title="{{'SHOW_CONVERSATION' | translate}}"
                                       ng-if="comment.objectId != null"
                                       ng-click="conversationsVm.showComments(comment.objectType, comment.objectId)"></i>--%>
                                </div>
                                <div class="flex-item text-center" ng-switch on="comment.objectType">
                                    <span class="label label-primary"
                                          ng-switch-when="ITEM">{{comment.objectType}}</span>
                                    <span ng-switch-when="CHANGE" ng-switch on="comment.type">
                                        <span class="label label-lightblue" ng-switch-when="ECO">{{comment.type}}</span>
                                        <span class="label label-flat-indigo"
                                              ng-switch-when="MCO">{{comment.type}}</span>
                                        <span class="label label-flat-purple"
                                              ng-switch-when="DCO">{{comment.type}}</span>
                                        <span class="label label-flat-olive"
                                              ng-switch-when="ECR">{{comment.type}}</span>
                                        <span class="label label-flat-olive"
                                              ng-switch-when="DCR">{{comment.type}}</span>
                                        <span class="label label-flat-brown"
                                              ng-switch-when="DEVIATION">{{comment.type}}</span>
                                        <span class="label label-flat-brown"
                                              ng-switch-when="WAIVER">{{comment.type}}</span>
                                        <span class="label label-flat-brown"
                                              ng-switch-when="DOCUMENT">{{comment.type}}</span>

                                        <span class="label label-flat-brown"
                                              ng-switch-when="OBJECTDOCUMENT">{{comment.objectType}}</span>
                                    </span>
                                    <span class="label label-flat-dimgray"
                                          ng-switch-when="QCR">{{comment.objectType}}</span>
                                    <span class="label label-flat-slategray"
                                          ng-switch-when="NCR">{{comment.objectType}}</span>
                                    <span class="label label-flat-khaki" ng-switch-when="PROBLEMREPORT">{{comment.objectType}}</span>
                                    <span class="label label-flat-orange" ng-switch-when="INSPECTIONPLANREVISION">{{comment.objectType}}</span>
                                    <span class="label label-flat-darkblue" ng-switch-when="ITEMINSPECTION">{{comment.objectType}}</span>
                                    <span class="label label-flat-darkblue" ng-switch-when="MATERIALINSPECTION">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="FILE">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="OBJECTDOCUMENT">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="MFRPARTINSPECTIONREPORT">{{comment.objectType}}</span>
                                      <span class="label label-success"
                                            ng-switch-when="PPAPCHECKLIST">{{comment.objectType}}</span>
                                    <span class="label label-info"
                                          ng-switch-when="MANUFACTURER">{{comment.objectType}}</span>
                                    <span class="label label-primary" ng-switch-when="MANUFACTURERPART">{{comment.objectType}}</span>
                                    <span class="label label-info"
                                          ng-switch-when="PGCSPECIFICATION">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="REQUIREMENT">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="DOCUMENT">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="PROJECT">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="PROGRAMFILE">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="PROGRAM">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="PLANT">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="PROJECTACTIVITY">{{comment.objectType}}</span>
                                    <span class="label label-primary"
                                          ng-switch-when="PROJECTTASK">{{comment.objectType}}</span>
                                     <span class="label label-flat-indigo"
                                           ng-switch-when="ASSEMBLYLINE">{{comment.objectType}}</span>
                                    <span class="label label-primary"
                                          ng-switch-when="WORKCENTER">{{comment.objectType}}</span>
                                    <span class="label label-flat-indigo"
                                          ng-switch-when="MACHINE">{{comment.objectType}}</span>
                                    <span class="label label-flat-khaki"
                                          ng-switch-when="EQUIPMENT">{{comment.objectType}}</span>
                                    <span class="label label-flat-slategray"
                                          ng-switch-when="INSTRUMENT">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="TOOL">{{comment.objectType}}</span>
                                    <span class="label label-danger"
                                          ng-switch-when="JIGFIXTURE">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="MATERIAL">{{comment.objectType}}</span>
                                    <span class="label label-lightblue"
                                          ng-switch-when="MANPOWER">{{comment.objectType}}</span>
                                    <span class="label label-info"
                                          ng-switch-when="SHIFT">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="OPERATION">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MFRSUPPLIER">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="BOP">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MBOM">{{comment.objectType}}</span>
                                     <span class="label label-warning"
                                           ng-switch-when="PGCSUBSTANCE">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="PGCDECLARATION">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MROWORKREQUEST">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MROMAINTENANCEPLAN">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MROSPAREPART">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MROMETER">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="PLMNPR">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MROASSET">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="MROWORKORDER">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="REQUIREMENTDOCUMENTREVISION">{{comment.objectType}}</span>
                                     <span class="label label-warning"
                                           ng-switch-when="CUSTOMER">{{comment.objectType}}</span>
                                     <span class="label label-flat-indigo"
                                           ng-switch-when="ITEMMCO">{{comment.objectType}}</span>
                                    <span class="label label-flat-indigo"
                                          ng-switch-when="OEMPARTMCO">{{comment.objectType}}</span>
                                    <span class="label label-warning"
                                          ng-switch-when="CUSTOMOBJECT">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="REQUIREMENTDOCUMENTTEMPLATE">{{comment.objectType}}</span>
                                    <span class="label label-success"
                                          ng-switch-when="SUPPLIERAUDITPLAN">{{comment.objectType}}</span>
                                    <span class="label label-flat-indigo"
                                          ng-switch-when="SUPPLIERAUDIT">{{comment.objectType}}</span>
                                    <span class="label label-flat-khaki"
                                          ng-switch-when="PPAP">{{comment.objectType}}</span>
                                    <span class="label label-info"
                                          ng-switch-when="REQUIREMENTTEMPLATE">{{comment.objectType}}</span>
                                </div>
                                <div class="flex-item text-right" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                     ng-switch on="comment.objectType">
                                    <a href="" ng-click="conversationsVm.gotoCommentObjectDetails(comment)"
                                       ng-switch-when="ITEM">{{comment.number}}</a>
                                    <a href="" ng-switch-when="CHANGE" ng-switch on="comment.type"
                                       ng-click="conversationsVm.gotoCommentObjectDetails(comment)">
                                        <span>{{comment.number}}</span>
                                    </a>
                                    <a href="" ui-sref="app.pqm.qcr.details({qcrId:comment.objectId})"
                                       ng-switch-when="QCR">{{comment.number}}</a>
                                    <a href="" ui-sref="app.pqm.ncr.details({ncrId:comment.objectId})"
                                       ng-switch-when="NCR">{{comment.number}}</a>
                                    <a href="" ui-sref="app.pqm.pr.details({problemReportId:comment.objectId})"
                                       ng-switch-when="PROBLEMREPORT">{{comment.number}}</a>
                                    <a href="" ui-sref="app.pqm.inspectionPlan.details({planId: comment.objectId})"
                                       ng-switch-when="INSPECTIONPLANREVISION">{{comment.number}}</a>
                                    <a href="" ui-sref="app.pqm.inspection.details({inspectionId: comment.objectId})"
                                       ng-switch-when="ITEMINSPECTION">{{comment.number}}</a>
                                    <a href="" ui-sref="app.pqm.inspection.details({inspectionId: comment.objectId})"
                                       ng-switch-when="MATERIALINSPECTION">{{comment.number}}</a>
                                    <a href="" ui-sref="app.mfr.details({manufacturerId: comment.objectId})"
                                       ng-switch-when="MANUFACTURER">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mfr.mfrparts.details({mfrId: comment.parentObjectId,manufacturePartId:comment.objectId})"
                                       ng-switch-when="MANUFACTURERPART">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.compliance.specification.details({specificationId: comment.objectId})"
                                       ng-switch-when="PGCSPECIFICATION">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.plant.details({plantId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="PLANT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.assemblyline.details({assemblyLineId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="ASSEMBLYLINE">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.workcenter.details({workcenterId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="WORKCENTER">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.machine.details({machineId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MACHINE">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.equipment.details({equipmentId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="EQUIPMENT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.instrument.details({instrumentId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="INSTRUMENT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.tool.details({toolId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="TOOL">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.jigsAndFixtures.details({jigsFixId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="JIGFIXTURE">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.material.details({materialId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MATERIAL">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.manpower.details({manpowerId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MANPOWER">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.shift.details({shiftId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="SHIFT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.masterData.operation.details({operationId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="OPERATION">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.req.requirements.details({requirementId: comment.parentObjectId,tab: 'details.basic'})"
                                       ng-switch-when="REQUIREMENT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pm.project.details({projectId: comment.objectId,tab: 'details.plan'})"
                                       ng-switch-when="PROJECT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pm.program.details({programId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="PROGRAM">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pm.project.activity.details({activityId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="PROJECTACTIVITY">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pm.project.activity.task.details({activityId: comment.parentObjectId,taskId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="PROJECTTASK">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mfr.supplier.details({supplierId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MFRSUPPLIER">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.bop.details({bopId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="BOP">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.compliance.substance.details({substanceId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="PGCSUBSTANCE">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.compliance.declaration.details({declarationId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="PGCDECLARATION">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mro.workRequest.details({workRequestId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MROWORKREQUEST">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mro.maintenancePlan.details({maintenancePlanId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MROMAINTENANCEPLAN">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mro.sparePart.details({sparePartId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MROSPAREPART">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mro.meter.details({meterId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MROMETER">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mro.asset.details({assetId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MROASSET">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mro.workOrder.details({workOrderId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="MROWORKORDER">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.req.document.details({reqId: comment.objectId,tab: 'details.requirements'})"
                                       ng-switch-when="REQUIREMENTDOCUMENTREVISION">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.req.document.template.details({reqDocId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="REQUIREMENTDOCUMENTTEMPLATE">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.req.template.details({requirementId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="REQUIREMENTTEMPLATE">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.customers.details({customerId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="CUSTOMER">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.changes.mco.details({mcoId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="ITEMMCO">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.changes.mco.details({mcoId: comment.objectId,tab: 'details.basic'})"
                                       ng-switch-when="OEMPARTMCO">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.customobjects.details({customId: comment.objectId, tab: 'details.basic'})"
                                       ng-switch-when="CUSTOMOBJECT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.nprs.details({nprId: comment.objectId, tab: 'details.basic'})"
                                       ng-switch-when="PLMNPR">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pqm.supplierAudit.details({supplierAuditId: comment.parentObjectId, tab: 'details.plan'})"
                                       ng-switch-when="SUPPLIERAUDITPLAN">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pqm.supplierAudit.details({supplierAuditId: comment.objectId, tab: 'details.basic'})"
                                       ng-switch-when="SUPPLIERAUDIT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pqm.supplierAudit.details({supplierAuditId: comment.objectId, tab: 'details.basic'})"
                                       ng-switch-when="SUPPLIERAUDIT">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.pqm.ppap.details({ppapId: comment.objectId, tab: 'details.basic'})"
                                       ng-switch-when="PPAP">{{comment.number}}</a>
                                    <a href=""
                                       ui-sref="app.mes.mbom.details({mbomId: comment.objectId, tab: 'details.basic'})"
                                       ng-switch-when="MBOM">{{comment.number}}</a>
                                    <a href="" ng-switch-when="FILE" ng-click="conversationsVm.loadFileTab(comment)">
                                        {{comment.number}}</a>
                                    <a href="" ng-switch-when="OBJECTDOCUMENT"
                                       ng-click="conversationsVm.loadFileTab(comment)">
                                        {{comment.number}}</a>
                                    <a href="" ng-switch-when="MFRPARTINSPECTIONREPORT"
                                       ng-click="conversationsVm.loadFileTab(comment)">
                                        {{comment.number}}</a>
                                    <a href="" ng-switch-when="PPAPCHECKLIST"
                                       ng-click="conversationsVm.loadFileTab(comment)">
                                        {{comment.number}}</a>
                                    <a href="" ng-switch-when="PROGRAMFILE"
                                       ng-click="conversationsVm.loadFileTab(comment)">
                                        {{comment.number}}</a>
                                    <a href="" ng-click="conversationsVm.loadFileTab(comment)"

                                       ng-switch-when="DOCUMENT">{{comment.number}}</a>
                                </div>
                            </div>
                            <div class="comment-timestamp">{{comment.time}}
                            </div>
                            <p class="comment-message" ng-if="!comment.editMode"
                               ng-bind-html="comment.commentHtml | highlightText: conversationsVm.searchQuery">
                            </p>
                            <textarea rows="3" class="form-control" ng-model="comment.comment" style="resize: none;"
                                      ng-if="comment.editMode"></textarea>

                            <%--<div id="{{comment.id}}commentFiles" class="comments-file-dropzone"
                                 ng-click="selectEditFiles(comment)" ng-if="comment.editMode">
                                <div class="drop-files-label">{{"DROP_FILES_OR_CLICK" | translate}}</div>

                                <div id="{{comment.id}}fileUploadPreviews" class="files-preview">
                                    <div class="dz-preview dz-file-preview" id="{{comment.id}}fileUploadTemplate"
                                         style="display: none">
                                        <div class="dz-details">
                                            <div class="dz-filename"><span data-dz-name></span></div>
                                            <div class="dz-size" data-dz-size></div>
                                        </div>
                                        <div class="dz-progress"><span class="dz-upload"
                                                                       data-dz-uploadprogress></span>
                                        </div>
                                        <div class="progress progress-striped active" style="display: none;">
                                            <div class="progress-bar"
                                                 role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                                 aria-valuemax="100" style="width: 100%">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>--%>
                            <div class="comment-images-container">
                                <img class="comment-image"
                                     ng-repeat="image in comment.images"
                                     ng-click="conversationsVm.showImages(comment,image)"
                                     ng-src="api/col/comments/image/{{image.id}}"/>

                            </div>
                            <div class="comment-attachments-container">
                                <div class="comment-attachment"
                                     ng-repeat="attachment in comment.attachments"
                                     ng-click="conversationsVm.downloadFile(attachment)">
                                    <div class="attachment-name">{{attachment.name}} [{{attachment.size | bytes}}]</div>
                                </div>
                            </div>
                        </div>
                    </li>
                    <div style="text-align: center;margin-bottom: 20px;"
                         ng-if="conversationsVm.comments.length > 0 && !conversationsVm.pagenatedResults.last">
                        <a href="" ng-click="conversationsVm.loadMore()"><h5>&#x2193;Load more</h5></a>
                    </div>
                </ul>
            </div>
        </div>

        <div id="comment-image-previewer" class="comment-image modal">
            <div class="comment-image-content">
                <div class="image-content" style="display: flex;width: 100%;">
                    <div class="image-view">
                        <div id="{{image.id}}" ng-repeat="image in conversationsVm.selectedCommentImages"
                             ng-show="image.showImage"
                             style="width: 100%; height: 100%;display: table-cell;vertical-align: middle;text-align: center;">
                            <i class="fa fa-angle-left" ng-click="showPreviousImage(image)"
                               ng-if="conversationsVm.selectedCommentImages.length > 1"
                               style="font-size: 50px;color: white;position: absolute;top: 45%;left: 5%;cursor: pointer;"></i>
                            <img ng-src="api/col/comments/image/{{image.id}}"
                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                            <a href="" ng-click="hideImagesView()"
                               class="comment-image-close pull-right"
                               style="display: inline-block"></a>
                            <i class="fa fa-angle-right" ng-click="showNextImage(image)"
                               ng-if="conversationsVm.selectedCommentImages.length > 1"
                               style="font-size: 50px;color: white;position: absolute;top: 45%;right: 5%;cursor: pointer;"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>