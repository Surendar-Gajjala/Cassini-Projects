<div style="position: relative;">
    <style scoped>
        .comments-panel {
            /*padding: 10px;*/
            background-color: #fff;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
            margin-bottom: 65px;
        }

        .comment-container {
            margin: 10px;
            border-bottom: 1px solid lightgrey;
            width: 80%;
            box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.22);
        }

        .comment-container:last-child {
            margin-bottom: 80px !important;
        }

        .comment-container:last-child {
            margin-bottom: 0;
        }

        .comment-info {
            display: flex;
            flex-direction: row;
            padding: 10px;
        }

        .comment-user {
            flex: 1;
        }

        .comment-user i.la {
            font-size: 14px;
        }

        .comment-user h5 {
            margin: 0;
            color: #16a085;
            line-height: 22px;
        }

        .comment-timestamp {
            flex: 1;
            text-align: right;
            color: grey;
        }

        .comment-text {
            margin-top: 0;
            padding: 0 10px 10px;
        }

        .comment-input {
            position: fixed;
            bottom: 30px;
            height: 65px;
            width: 600px;
            padding: 10px;
            background-color: #eee;
            border-top: 1px solid #ddd;
        }

        .comment-textarea {
            resize: none !important;
            min-height: 41px !important;
            position: absolute !important;
            bottom: 0 !important;
            left: 0 !important;
            /*margin-right: 41px !important;*/
            width: calc(100% - 105px) !important;
            border-right: 0;
        }

        .left-float {
            float: left;
        }

        .right-float {
            float: right;
        }

        .login-person {
            color: #0390fd !important;
        }

        #rightSidePanelContent {
            padding-bottom: 40px;
        }

        .fileContainer {
            background: #fff;
            border-radius: .3em;
            float: left;
            padding: 7px 10px;
            height: 41px;
            text-align: center;
            margin-left: -42px;
            width: 41px;
            margin-bottom: 0 !important;
            border: 1px solid #ccc;
            border-left: 0;
        }

        .fileContainer [type=file] {
            cursor: inherit;
            display: block;
            font-size: 999px;
            filter: alpha(opacity=0);
            opacity: 0;
            position: absolute;
            right: 0;
            text-align: right;
            top: 0;
        }

        .dropup-menu {
            position: absolute;
            bottom: 100%;
            left: auto;
            display: none;
            min-width: 160px;
            padding: 5px 0;
            margin: 2px 0 0;
            text-align: left;
            top: auto;
            right: 62px;
        }

        .open > .dropup-menu {
            display: flex;
        }

        .files-view.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 50px; /* Location of the box */
            left: auto;
            top: 50px;
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .files-view .files-content {
            margin: auto;
            display: block;
            /*height: 94%;*/
            width: 90%;
            background-color: white;
            border-radius: 7px !important;
        }

        .file-close {
            position: absolute;
            right: 9px;
            top: 8px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .file-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .file-close:before, .file-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .file-close:before {
            transform: rotate(45deg) !important;
        }

        .file-close:after {
            transform: rotate(-45deg) !important;
        }

        #file-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
            position: relative;
        }

        .configuration-header {
            font-weight: bold;
            font-size: 22px;
            display: inline-block;
            left: 44%;
            top: 13px;
        }

        #file-footer {
            border-top: 1px solid lightgrey;
            padding: 5px;
            height: 50px;
            width: 100%;
        }

        .comment-attachment {
            padding: 3px;
            border: 1px solid lightgrey;
            width: fit-content;
            background-color: #f5f9f5bd;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.16), 0 1px 2px rgba(0, 0, 0, 0.22);
            border-radius: 3px;
            cursor: pointer;
        }

        .comment-files .attachment-name {
            padding: 5px;
            background-color: #e6e1e1;
            color: #00253f;
        }

        .comment-image.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(45, 55, 72, 0.77); /* Black w/ opacity */
        }

        .comment-image .comment-image-content {
            margin: auto;
            display: block;
            height: calc(100% - 30px);
            width: 100%;
            border-radius: 7px !important;
        }

        .image-content {
            height: 100%;
        }

        .image-view {
            width: 100%;
        }

        .comment-image-close {
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

        .comment-image-close:hover {
            opacity: 0.9;
            border-radius: 50%;
            background-color: #ddd;
        }

        .comment-image-close:before, .comment-image-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .comment-image-close:before {
            transform: rotate(45deg) !important;
        }

        .comment-image-close:after {
            transform: rotate(-45deg) !important;
        }

        .comments-panel .attachments-container {
            display: flex;
            flex-wrap: wrap;
        }

        .comments-panel .attachments-container > div {
            flex-grow: 1;
        }

    </style>


    <div style="padding: 20px;font-style: italic" ng-if="commentsVm.comments.length == 0">
        <span translate>NO_COMMENTS</span>
    </div>
    <div class="comments-panel" ng-if="commentsVm.comments.length > 0">
        <div class="comment-container" ng-repeat="comment in commentsVm.comments"
             ng-class="{'left-float': loginPersonDetails.person.id != comment.commentedBy.id,'right-float':loginPersonDetails.person.id == comment.commentedBy.id}">
            <div class="comment-info"
                 ng-class="{'bg-success':loginPersonDetails.person.id == comment.commentedBy.id, 'bg-info':loginPersonDetails.person.id != comment.commentedBy.id}">
                <div class="comment-user">
                    <h5 ng-class="{'login-person':loginPersonDetails.person.id != comment.commentedBy.id}">
                        {{comment.commentedBy.fullName}}</h5>
                </div>
                <div class="comment-timestamp">
                    <small <%--class="text-muted"--%>>{{comment.commentedDate}}</small>
                </div>
            </div>
            <div class="comment-text">
                <span style="white-space: normal;word-wrap: break-word;">
                     <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                            -webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                         <div>
                             <span style="display:block;overflow:auto !important;color: #00253f;padding: 5px;"
                                   ng-bind-html="comment.comment"></span>
                         </div>
                         <div style="overflow: auto;">
                             <div style="display: flex;" ng-repeat="imageFile in comment.imageFiles">
                                 <div ng-repeat="image in imageFile">
                                     <div class="comment-attachment" style="margin-left: 5px;padding: 0">
                                         <img ng-src="api/col/comments/image/{{image.id}}"
                                              title="Preview {{image.fileName}}"
                                              ng-click="commentsVm.showImages(comment,image)"
                                              style="height: 100px;width: 104px;padding: 3px;cursor: pointer;"/><br>
                                     </div>
                                 </div>
                             </div>
                         </div>
                         <div style="overflow: auto;padding: 5px 0">
                             <div style="display: flex;" ng-repeat="videoFile in comment.videoFiles">
                                 <div ng-repeat="video in videoFile">
                                     <div class="comment-attachment" style="margin-left: 5px;padding: 0">
                                         <video style="width: 215px;height: 150px;padding: 3px;cursor: pointer;"
                                                title="{{video.fileName}}"
                                                type="video/{{video.extension}}" controls>
                                             <source ng-src="{{video.url}}" type="video/{{video.extension}}">
                                         </video>
                                     </div>
                                 </div>
                             </div>
                         </div>
                         <div class="attachments-container">
                             <div ng-repeat="attachment in comment.attachments" style="padding: 5px;">
                                 <div class="comment-attachment">
                                     <div class="attachment-name" title="Preview {{attachment.name}}"
                                          ng-click="commentsVm.filePreview(attachment)">
                                         <i class="{{getIcon(attachment.name)}}"
                                            style="padding-right: 5px;font-size: 16px;"
                                            ng-style="{{getIconColor(attachment.name)}}"></i>
                                         <span>{{attachment.name}}</span>
                                     </div>
                                 </div>
                             </div>
                         </div>
                     </div>
                </span>
            </div>
        </div>
    </div>
    <div class="comment-input">
        <div class="input-group">
            <textarea id="commentTextbox" class="form-control comment-textarea"
                      ng-keyup="commentsVm.onKeyUp();"
                      rows="1" style="resize: none;"
                      ng-model="commentsVm.newComment.comment"></textarea>
            <span class="input-group-btn">
                <label class="fileContainer" title="Attach Files">
                    <span class="fa fa-paperclip" style="font-size: 18px;vertical-align: bottom;"></span>
                    <input type="file" id="images" value="file" multiple="true" style="width: 1px;"/>
                </label>
                <button type="button" class="btn btn-lightblue"
                        ng-click="commentsVm.createComment()" translate>SEND
                </button>
            </span>
        </div>
    </div>
</div>

<div id="comment-files" class="files-view modal">
    <div class="files-content">
        <div id="file-header">
            <span class="configuration-header">Files</span>
            <a href="" ng-click="commentsVm.hideFilesView()" class="file-close pull-right"
               style="display: inline-block"></a>
        </div>
        <div id="file-content" style="overflow-y: auto;">
            <div style="width: 96%;margin:20px auto;">
                <textarea class="form-control" rows="3" style="resize: none" placeholder="Enter Comment"
                          ng-model="commentsVm.newComment.comment"></textarea>
            </div>
            <h4 style="margin: 5px 0 0 10px">Images : </h4>

            <div style="width: 96%;padding: 5px 0;overflow-x: auto;margin: auto;">
                <div style="display: flex;" ng-repeat="photo in commentsVm.newComment.photos">
                    <div ng-repeat="image in photo">
                        <div class="comment-attachment" style="margin-left: 5px;padding: 0">
                            <img ng-src="{{image.fileValue}}" ng-if="image.attachmentType == 'image'"
                                 style="height: 100px;width: 100px;padding: 3px;cursor: pointer;"/><br>
                            <%--<video style="width: 100px;height: 100px;" ng-if="image.attachmentType == 'video'"
                                   type="video/{{image.extension}}" controls>
                                <source ng-src="{{image.fileValue}}" type="video/{{image.extension}}">
                            </video>--%>
                        </div>
                    </div>
                </div>
            </div>
            <h4 style="margin: 5px 0 0 10px">Files : </h4>

            <div style="width: 96%;padding: 5px 0;margin: auto;">
                <div ng-repeat="file in commentsVm.newComment.attachmentFiles" style="padding: 3px">
                    {{file.name}}
                </div>
            </div>
        </div>
        <div id="file-footer" style="background-color: #edeeef">
            <button class="btn btn-sm btn-primary" style="float: right"
                    ng-click="commentsVm.sendImages()">
                <span translate>SEND</span>
            </button>
        </div>
    </div>
</div>

<div id="comment-image" class="comment-image modal">
    <div class="comment-image-content">
        <div class="image-content" style="display: flex;width: 100%;">
            <div class="image-view">
                <div id="{{image.id}}" ng-repeat="image in commentsVm.selectedCommentImages"
                     ng-show="image.showImage"
                     style="display: table-cell;vertical-align: middle;text-align: center;">
                    <i class="fa fa-angle-left" ng-click="showPreviousImage(image)"
                       ng-if="commentsVm.selectedCommentImages.length > 1"
                       style="font-size: 50px;color: white;position: absolute;top: 45%;left: 5%;cursor: pointer;"></i>
                    <img ng-src="api/col/comments/image/{{image.id}}"
                         style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                    <a href="" ng-click="hideImagesView()"
                       class="comment-image-close pull-right"
                       style="display: inline-block"></a>
                    <i class="fa fa-angle-right" ng-click="showNextImage(image)"
                       ng-if="commentsVm.selectedCommentImages.length > 1"
                       style="font-size: 50px;color: white;position: absolute;top: 45%;right: 5%;cursor: pointer;"></i>
                </div>
            </div>
        </div>
    </div>
</div>

<%--<div style="display: table-cell;vertical-align: middle;text-align: center;">
    <img ng-src="{{comment.attachmentValue}}"
         style="height: auto;width: auto;max-width: 100%;max-height: 100%"/>
</div>--%>
