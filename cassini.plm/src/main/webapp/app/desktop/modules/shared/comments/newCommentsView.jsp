<div style="position: relative;height: 100%;">
    <style scoped>
        .comments-container ul.timeline.no-comments:before {
            display: none;
        }

        .comments-container ul.timeline {
            list-style-type: none;
            position: relative;
            width: 100% !important;
            height: 100% !important;
            margin-top: 0 !important;
            padding-top: 0;
        }

        .comments-container ul.timeline:before {
            content: ' ';
            background: #d4d9df;
            display: inline-block;
            position: absolute;
            left: 29px;
            width: 2px;
            height: 100%;
            z-index: 400;
        }

        .comments-container ul.timeline > li {
            padding: 0 0 0 20px;
            margin-bottom: 20px;
        }

        .comments-container ul.timeline > li:before {
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

        .comments-container ul.timeline li div.timeline-event {
            margin-left: 30px;
            margin-right: 20px;
        }

        .comments-container ul.timeline li div.timeline-event .comment-user {
            color: #337ab7;
        }

        .comments-container ul.timeline li div.timeline-event .comment-user i.fa-comments-o {
            font-size: 14px;
            margin-left: 5px;
            cursor: pointer;
            opacity: 0.6;
        }

        .comments-container ul.timeline li div.timeline-event .comment-timestamp {
            font-size: 12px;
            text-align: left;
            opacity: 0.7;
            font-weight: 400;
        }

        .comments-container ul.timeline li div.timeline-event .comment-message {

        }

        .comments-container ul.timeline li div.timeline-event .float-right {
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
            position: sticky;
            top: 0;
            margin-bottom: 20px !important;
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
            width: 350px;
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

        .comments-container {
            height: 100%;
        }

        .comments-container .widget-body {
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

        .comments-container .comment-attachment {
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

        .comments-container .comments-file-dropzone i.la-close {
            margin-top: 5px;
            padding: 5px;
            font-size: 14px;
            position: absolute;
            right: 56px;
            font-weight: 600;
        }

        .comments-container .comments-file-dropzone i.la-close:hover {
            background-color: #ddd;
            border-radius: 50%;
            font-weight: 600;
        }

        .modal-open .modal {
            z-index: 10000 !important;
        }
    </style>

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <div id="commentsContainer" class="comments-container">
        <div class="comments-panel">
            <div id="commentsBody" class="comments-body">
                <ul class="timeline" ng-class="{'no-comments': commentsVm.comments.length == 0}">
                    <form class="message-form">
                        <i class="fa fa-comments-o chat-icon"></i>
                        <%--<input type="text" class="form-control input-sm"
                               placeholder="{{commentsVm.placeholderText}}"
                               ng-model="commentsVm.newComment.comment"
                               ng-enter="commentsVm.createComment()">--%>
                        <textarea rows="1" class="message-text form-control input-sm" id="messageText"
                                  placeholder="{{commentsVm.placeholderText}}"
                                  ng-keypress="($event.charCode==13 && $event.ctrlKey == true)? commentsVm.createComment() : return;"
                                  ng-model="commentsVm.newComment.comment"></textarea>
                        <i class="fa fa-paperclip attachments-icon" ng-click="commentsVm.toggleCommentFiles()"></i>
                        <i class="fa fa-send-o send-icon" ng-click="commentsVm.createComment()"></i>

                        <div id="commentFiles" class="comments-file-dropzone" ng-click="commentsVm.selectFiles()"
                             ng-if="commentsVm.showFilesDropZone">
                            <i class="la la-close" title="Close" ng-click="commentsVm.toggleCommentFiles()"></i>

                            <div class="drop-files-label">{{"DROP_FILES_OR_CLICK" |
                                translate}}
                            </div>

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


                    <div class="conversations-loading" ng-if="conversationsVm.loading">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader17.gif"
                             class="mr5"><span translate>LOADING_CONVERSATIONS</span>
                    </div>

                    <div class="no-conversations-container"
                         ng-if="!commentsVm.loading && commentsVm.comments.length == 0">
                        <div class="no-conversations">
                            <img src="app/assets/images/plm-conversations.jpg" alt="">

                            <div class="no-conversations-message">Type something and start a conversation.</div>
                            <div class="no-conversations-suggestion">This will start a discussion thread on this
                                object.
                            </div>
                        </div>
                    </div>

                    <li ng-repeat="comment in commentsVm.comments">
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
                                     ng-click="commentsVm.showImages(comment,image)"
                                     ng-src="api/col/comments/image/{{image.id}}"/>

                            </div>
                            <div class="comment-attachments-container">
                                <div class="comment-attachment"
                                     ng-repeat="attachment in comment.attachments"
                                     ng-click="commentsVm.downloadFile(attachment)">
                                    <div class="attachment-name">{{attachment.name}} [{{attachment.size | bytes}}]</div>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>

        <div id="comment-image-previewer-sidepanel" class="comment-image-previewer-sidepanel comment-image-modal modal">
            <div class="comment-image-content">
                <div class="image-content" style="display: flex;width: 100%;">
                    <div class="image-view">
                        <div id="commentImage{{image.id}}" ng-repeat="image in commentsVm.selectedCommentImages"
                             ng-show="image.showImage"
                             style="width: 100%; height: 100%;display: table-cell;vertical-align: middle;text-align: center;">
                            <i class="fa fa-angle-left" ng-click="commentsVm.showPreviousImage(image)"
                               ng-if="commentsVm.selectedCommentImages.length > 1"
                               style="font-size: 50px;color: white;position: absolute;top: 45%;left: 5%;cursor: pointer;"></i>
                            <img ng-src="api/col/comments/image/{{image.id}}"
                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                            <a href="" ng-click="commentsVm.hideImagesView()"
                               class="comment-image-close pull-right"
                               style="display: inline-block"></a>
                            <i class="fa fa-angle-right" ng-click="commentsVm.showNextImage(image)"
                               ng-if="commentsVm.selectedCommentImages.length > 1"
                               style="font-size: 50px;color: white;position: absolute;top: 45%;right: 5%;cursor: pointer;"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>