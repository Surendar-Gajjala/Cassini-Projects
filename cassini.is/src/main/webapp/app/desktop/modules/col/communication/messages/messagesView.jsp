<style scoped>

    :root {
        --highlight-color: #ddeef8;
    }

    .messaging-window {
        margin-left: auto;
        margin-right: auto;
        border: 1px solid #ddd;
    }

    .messaging-window .left {
        border-right: 1px solid #ddd;
    }

    .messaging-window .left, .messaging-window .right {
        padding: 0;
        height: 302px;
    }

    .messaging-window .search-box {
        height: 50px;
        border-bottom: 1px solid #ddd;
        padding-left: 10px;
        padding-right: 10px;
        background-color: #eceff1;
    }

    .messaging-window .search-box input {
        float: left;
        width: calc(100% - 40px);
        height: 30px;
        padding: 0 10px;
        border: 1px solid #ddd;
        background-color: #fff;
        border-radius: 18px;
        font-family: 'Source Sans Pro', sans-serif;
        font-weight: 400;
        margin-top: 10px;
    }

    .messaging-window .search-box a.search {
        display: block;
        float: left;
        width: 30px;
        height: 30px;
        margin-left: 10px;
        border: 1px solid #778ca3;
        background-color: #778ca3;
        background-image: url("/app/assets/images/edit.png");
        background-repeat: no-repeat;
        background-position: top 6px left 8px;
        border-radius: 50%;
        margin-top: 10px;
    }

    .messaging-window .conversations {
        position: absolute;
        top: 50px;
        bottom: 0;
        left: 0;
        right: 0;
        overflow-y: auto;
    }

    .messaging-window .conversation .header {
        height: 50px;
        line-height: 50px;
        border-bottom: 1px solid #ddd;
        background-color: #eceff1;
        display: inline-flex;
        width: 100%;
    }

    .dropdown:hover .dropdown-menu {
        display: block;
    }

    .tab-content {
        border: none !important;
    }

    .nav-tabs .uib-tab, .nav-tabs .uib-tab .nav-link {
        border: none !important;
    }

    .messaging-window .conversations .group {
        height: 75px;
        line-height: 75px;
        border-bottom: 1px solid #ddd;
        padding: 10px;
    }

    .messaging-window .conversations .group .avatar {
        width: 40px;
        height: 40px;
        float: left;
        border: 0;
        border-radius: 50%;
        margin-top: 8px;
        text-align: center;
        font-size: 20px;
        font-weight: bolder;
        line-height: 40px;
        color: #fff;
    }

    .messaging-window .conversations .group .group-details {
        margin-left: 55px;
        line-height: 1;
        height: 55px;
        padding-top: 12px;
    }

    .messaging-window .conversations .group .groupEditIcon {
        margin-right: 10px;
        line-height: 1;
        height: 55px;
        padding-top: 12px;
    }

    .messaging-window .conversations .group .groupEditIcon {
        display: none;
    }

    .messaging-window .conversations .group:hover .groupEditIcon {
        display: block;
    }

    .messaging-window .conversations .group .group-details .group-name {
        font-size: 18px;
    }

    .messaging-window .conversations .group.active,
    .messaging-window .conversations .group:hover {
        background-color: var(--highlight-color);
        cursor: pointer;
    }

    .messaging-window .conversations .group:hover .avatar {
    }

    .messaging-window .conversation .messages {
        position: absolute;
        top: 50px;
        left: 0;
        right: 0;
        bottom: 50px;
        overflow-y: auto;
        padding: 20px;
    }

    .hasAttachments {
        bottom: 100px !important;
    }

    .hasNoAttachments {
        bottom: 50px !important;
    }

    .noAttachmentsHeight {
        height: 50px !important;
    }

    .attachmentsHeight {
        height: 100px !important;
    }

    .conversation .messages .bubble {
        font-size: 16px;
        position: relative;
        display: inline-block;
        clear: both;
        margin-bottom: 8px;
        padding: 0 14px;
        vertical-align: top;
        border-radius: 5px;
        max-width: 80%;
    }

    .conversation .messages .bubble:before {
        position: absolute;
        top: 19px;
        display: block;
        width: 8px;
        height: 6px;
        content: '\00a0';
        -webkit-transform: rotate(29deg) skew(-35deg);
        transform: rotate(29deg) skew(-35deg);
    }

    .conversation .messages .bubble.you {
        float: left;
        color: black;
        background-color: var(--highlight-color);
        align-self: flex-start;
        -webkit-animation-name: slideFromLeft;
        animation-name: slideFromLeft;
    }

    .conversation .messages .bubble.you:before {
        left: -3px;
        background-color: var(--highlight-color);
    }

    .conversation .messages .bubble.me {
        float: right;
        color: #1a1a1a;
        background-color: #eceff1;
        align-self: flex-end;
        -webkit-animation-name: slideFromRight;
        animation-name: slideFromRight;
    }

    .conversation .messages .bubble.me:before {
        right: -3px;
        background-color: #eceff1;
    }

    .conversation .message-input-section {
        position: absolute;
        height: 50px;
        width: 100%;
        background-color: #eceff1;
        bottom: 0;
        border-top: 1px solid #ddd;
        padding-left: 10px;
        padding-right: 10px;
    }

    .conversation .message-input-section .message-input {
        float: left;
        width: calc(100% - 75px) !important;
        height: 40px;
        padding: 0 10px;
        border: 1px solid #ddd;
        background-color: #fff;
        border-radius: 5px;
        font-family: 'Source Sans Pro', sans-serif;
        font-weight: 400;
        margin-top: 5px;
    }

    @keyframes slideFromLeft {
        0% {
            margin-left: -200px;
            opacity: 0;
        }
        100% {
            margin-left: 0;
            opacity: 1;
        }
    }

    @-webkit-keyframes slideFromLeft {
        0% {
            margin-left: -200px;
            opacity: 0;
        }
        100% {
            margin-left: 0;
            opacity: 1;
        }
    }

    @keyframes slideFromRight {
        0% {
            margin-right: -200px;
            opacity: 0;
        }
        100% {
            margin-right: 0;
            opacity: 1;
        }
    }

    @-webkit-keyframes slideFromRight {
        0% {
            margin-right: -200px;
            opacity: 0;
        }
        100% {
            margin-right: 0;
            opacity: 1;
        }
    }

    h1, h2, h3, h4, h5, h6 {
        margin: 0;
    }

    .new-group-panel {
        position: absolute;
        top: 0;
        bottom: 0;

    }

    .messaging-window, .messaging-window .row, .messaging-window .row .left, .messaging-window .row .right {
        height: 100% !important;
    }

    .messaging-window .members .group {
        height: 38px;
    }

    .messaging-window .members .group .avatar {
        width: 28px;
        height: 25px;
        float: left;
        border: 0;
        border-radius: 50%;
        margin-top: 7px;
        text-align: center;
        font-size: 15px;
        font-weight: bolder;
        line-height: 26px;
        color: #fff;
    }

    .messaging-window .members .group .group-details {
        line-height: 1;
        height: 28px;
        padding-top: 11px;
    }

    table .ui-select-choices {
        position: absolute !important;
        top: auto !important;
        left: auto !important;
        width: auto !important;
    }

    .img-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .img-model.modal1 {
        display: block; /* Hidden by default */
    }

    /* Modal Content (image) */
    .img-model .modal-content {
        margin: auto;
        display: block;
        height: 90%;
        width: 60%;
        /*max-width: 70%;*/
    }

    /* Caption of Modal Image */
    .img-model #caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        height: 150px;
    }

    /* Add Animation */
    .img-model .modal-content, #caption {
        -webkit-animation-name: zoom;
        -webkit-animation-duration: 0.6s;
        animation-name: zoom;
        animation-duration: 0.6s;
    }

    .img-model .closeImage10 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage10:hover,
    .img-model .closeImage10:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    ul li {
        display: inline;
    }

    .xxx:hover .image {
        opacity: 0.3;
    }

    .xxx:hover .text {
        opacity: 1;
    }

    .text {
        transition: .5s ease;
        opacity: 0;
        position: absolute;
        top: 75px !important;
        left: calc(30% + 60px);
        transform: translate(-50%, -50%);
        -ms-transform: translate(-50%, -50%);
        text-align: center;
    }

    .group .group-details .deleteMember {
        display: none;
    }

    .group .group-details:hover .deleteMember {
        display: inline-block;
        padding-left: 5px;
    }

</style>

<div class="messaging-window" style="width: 1000px;">
    <div class="row row-eq-height" style="margin: 0;">
        <div class="col-sm-4 left">
            <div class="search-box" ng-if="!messageVm.addGroup && !messageVm.groupEditMode">
                <input type="text" placeholder="Search"
                       ng-if="messageVm.groups.length > 0 || messageVm.criteria.searchQuery != '' "
                       ng-change="messageVm.loadGroups()"
                       ng-model="messageVm.criteria.searchQuery"/>
                <a href="javascript:;" class="search" ng-click="messageVm.newGroup()"
                   title="Add Group"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.communication.newGroup') || login.person.isProjectOwner)"></a>
            </div>
            <div ng-if="messageVm.addGroup || messageVm.groupEditMode"
                 style="height: 50px;border-bottom: 1px solid #ddd;padding: 10px;background-color: #eceff1;">
                <h3 ng-if="messageVm.addGroup" style="text-align: center">New Group</h3>

                <h3 ng-if="messageVm.groupEditMode" style="text-align: center">Edit Group</h3>
            </div>
            <div class="conversations" <%--when-scrolled="messageVm.loadGroups()"--%>>
                <div class="row" ng-show="messageVm.addGroup">
                    <input type="file" id="groupIcon" value="file" accept="image/*"
                           title="Click to add group icon" style="display:none;"/>
                    <label for="groupIcon" class="btn" ng-if="messageVm.groupIcon == null"
                           style="background-color: rgba(140, 140, 140, 0.85);border-radius: 50%;height: 120px;width: 120px;margin-left: 30%; margin-top: 10px;">
                        <i style="color: white;margin-top: 30px;"
                           class="fa fa-camera"></i>

                        <div ng-if="messageVm.groupIcon == null" style="color: white">ADD GROUP <br> ICON</div>
                    </label>
                    <label for="groupIcon" class="btn" ng-if="messageVm.groupIcon != null"
                           style="margin-left: 30%; margin-top: 10px;">
                        <img class="image" id="icon"
                             ng-src="{{messageVm.selectedGrp.groupIconSrc}}" height="120px" width="120px"
                             style="border-radius: 50%;"/>
                    </label>

                    <form style="padding: 20px;">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Title"
                                   ng-model="messageVm.newMessageGroup.name">
                        </div>
                        <div class="form-group">
                            <textarea type="text" rows="4" style="resize: vertical" class="form-control"
                                      ng-model="messageVm.newMessageGroup.description"
                                      placeholder="Description"></textarea>
                        </div>
                    </form>
                    <div style="padding: 17px;">
                        <button class="btn btn-success" style="border-radius: 20px;width: 49%"
                                ng-click="messageVm.createNewGrp()">
                            Create
                        </button>
                        <button class="btn btn-primary" style="border-radius: 20px;width: 49%"
                                ng-click="messageVm.cancelNewGrp()">Cancel
                        </button>
                    </div>
                </div>
                <div class="row" ng-show="messageVm.groupEditMode">
                    <label class="xxx" for="groupEditIcon"
                           ng-if="messageVm.selectedGrp.groupIcon != null && messageVm.groupIcon == null"
                           style="border-radius: 50%;margin-left: 30%; margin-top: 10px;">
                        <img class="image" id="image" ng-src="{{messageVm.selectedGrp.groupIconSrc}}" height="120px"
                             width="120px" style="border-radius: 50%;"/>

                        <div class="text">CLICK TO EDIT</div>
                    </label>
                    <label for="groupEditIcon" class="btn"
                           ng-if="messageVm.groupIcon == null && messageVm.selectedGrp.groupIcon == null"
                           style="background-color: rgba(140, 140, 140, 0.85);border-radius: 50%;height: 120px;width: 120px;margin-left: 30%; margin-top: 10px;">
                        <i style="color: white;margin-top: 30px;" class="fa fa-camera"></i>

                        <div style="color: white">ADD GROUP <br> ICON</div>
                    </label>
                    <label for="groupEditIcon" class="btn" ng-if="messageVm.groupIcon != null"
                           style="margin-left: 30%; margin-top: 10px;">
                        <img class="image" id="image1" ng-src="{{messageVm.selectedGrp.groupIconSrc}}"
                             height="120px" width="120px" style="border-radius: 50%;"/>
                    </label>
                    <input type="file" id="groupEditIcon" value="file" accept="image/*"
                           ng-model="messageVm.selectedGrp.groupIcon"
                           title="Click to edit group icon" style="display:none;"/>

                    <form style="padding: 20px;">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Title"
                                   ng-model="messageVm.selectedGrp.name">
                        </div>
                        <div class="form-group">
                            <textarea type="text" rows="4" style="resize: vertical" class="form-control"
                                      ng-model="messageVm.selectedGrp.description"
                                      placeholder="Description"></textarea>
                        </div>
                    </form>
                    <div style="padding: 17px;">
                        <button class="btn btn-success" style="border-radius: 20px;width: 49%"
                                ng-click="messageVm.updateGroup()">Update
                        </button>
                        <button class="btn btn-primary" style="border-radius: 20px;width: 49%"
                                ng-click="messageVm.cancelNewGrp()">Cancel
                        </button>
                    </div>
                </div>
                <div ng-if="messageVm.groups.length == 0 && !messageVm.addGroup && !messageVm.groupEditMode && !messageVm.loadingGroups"
                     style="text-align: center">No Groups
                </div>
                <div>
                    <div ng-repeat="group in messageVm.groups" class="group" ng-class="{'active': group.active}"
                         ng-if="!messageVm.addGroup && !messageVm.groupEditMode"
                         ng-click="messageVm.loadGrpMessages(group)">
                        <div ng-if="group.groupIcon == null" class="avatar" ng-style="group.color">
                            {{group.name.charAt(0)}}
                        </div>
                        <div ng-if="group.groupIcon != null" class="avatar">
                            <img ng-src="{{group.groupIconSrc}}" width="40px" height="40px" style="border-radius: 50%;">
                        </div>
                        <div class="groupEditIcon" style="float: right;" ng-click="messageVm.editGroup(group)"
                             ng-if="selectedProject.locked == false && (hasPermission('permission.communication.editGroup') || login.person.isProjectOwner)">
                            <i
                                    class="fa fa-edit" title="Click to edit details"></i></div>
                        <div class="group-details">
                            <span class="group-name"><span
                                    ng-bind-html="group.name | highlightText: messageVm.criteria.searchQuery"></span></span><br>
                            <span ng-bind-html="group.description | highlightText: messageVm.criteria.searchQuery"></span>
                        </div>
                    </div>
                    <div ng-if="messageVm.loadingGroups" style="text-align: center">
                        <img src="app/assets/images/loaders/loader5.gif" class="mr5">Loading Groups..
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-8 right">
            <div class="conversation">
                <div class="header">
                    <div class="members" style="margin-left: 20px;width: 30%" ng-if="!messageVm.groupEditMode">
                        <div class="avatar" ng-if="messageVm.selectedGrp != null">
                            <img ng-if="messageVm.selectedGrp.groupIconSrc != null"
                                 ng-src="{{messageVm.selectedGrp.groupIconSrc}}" width="40px" height="40px"
                                 style="border-radius: 50%;">
                            <span style="font-size: 20px;">{{messageVm.selectedGrp.name}}</span>
                        </div>
                    </div>
                    <div class=".messaging-window search-box" style="width: 40%;">
                        <h4 style="text-align: center;margin-top: 10px;" ng-show="messageVm.showInfo">Group Members</h4>
                        <input type="text" placeholder="Search"
                               ng-if="(messageVm.selectedGrp.messageList.length > 0 || messageVm.messageCriteria.searchQuery != '') && !messageVm.groupEditMode && !messageVm.showInfo && !messageVm.selectView"
                               ng-change="messageVm.loadMessages()" style="width: 250px;"
                               ng-model="messageVm.messageCriteria.searchQuery"/>
                        <input type="text" placeholder="Search" style="width: 250px;"
                               ng-show="messageVm.selectView" ng-model="searchText"/>
                    </div>
                    <div style="width: 30%;margin-right: 20px;text-align: right"
                         ng-if="messageVm.selectedGrp != null && !messageVm.groupEditMode">
                        <div class="dropdown">
                            <a class="dropdown-toggle" type="button" data-toggle="dropdown">
                                <span><i style="cursor: pointer" class="fa fa-ellipsis-v"></i></span></a>
                            <ul class="dropdown-menu dropdown-menu-right"
                                style="min-width: 100px !important;top: 30px !important;">
                                <li ng-click='messageVm.back()' ng-if="!messageVm.showInfo && !messageVm.selectView">
                                    <a class="dropdown-item">
                                        <span style="padding-left: 3px;">Details</span>
                                    </a></li>
                                <li ng-click='messageVm.deleteGroup()'
                                    ng-if="!messageVm.showInfo && !messageVm.selectView && (login.isAdmin || login.person.isProjectOwner)">
                                    <a class="dropdown-item">
                                        <span style="padding-left: 3px;">Delete Group</span>
                                    </a></li>
                                <li ng-click='messageVm.exitGroup()'
                                    ng-if="!messageVm.showInfo && !messageVm.selectView">
                                    <a class="dropdown-item">
                                        <span style="padding-left: 3px;">Exit Group</span>
                                    </a></li>
                                <li ng-click='messageVm.back()' ng-if="messageVm.showInfo || messageVm.selectView">
                                    <a class="dropdown-item">
                                        <span style="padding-left: 3px;">Back</span>
                                    </a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <%---   messages view   ---%>
                <div ng-if="messageVm.selectedGrp == null && messageVm.groups.length > 0 && !messageVm.addGroup && !messageVm.groupEditMode"
                     style="text-align: center">
                    Select group to view conversations
                </div>
                <div ng-if="!messageVm.showInfo && !messageVm.selectView && messageVm.selectedGrp != null && !messageVm.groupEditMode"
                     style="overflow-y: scroll">
                    <div ng-if="messageVm.selectedGrp.messageList.length == 0 && !messageVm.loadingGrpMessages"
                         style="text-align: center">
                        No Conversations
                    </div>
                    <div ng-if="messageVm.loadingGrpMessages" style="text-align: center">
                        <img src="app/assets/images/loaders/loader5.gif" class="mr5">Loading Conversations..
                    </div>
                    <div class="messages" when-scrolled='messageVm.loadMessages()' id="messages"
                         ng-class="{'hasAttachments' :messageVm.importFile.length > 0, 'hasNoAttachments' :messageVm.importFile.length == 0}">
                        <div class="bubble"
                             ng-class="{'you':message.createdBy != login.person.id, 'me': message.createdBy == login.person.id}"
                             ng-repeat="message in messageVm.selectedGrp.messageList">
                            <div style="font-size: 10px; text-align: left"
                                 ng-if="message.createdBy != login.person.id && (messageVm.selectedGrp.messageList[$index - 1].createdBy != message.createdBy)">
                                {{message.createdByObject.fullName}}
                            </div>
                            <div style="margin: 5px 5px;" ng-if="message.msgText != '' "><span
                                    ng-bind-html="message.msgText | highlightText: messageVm.messageCriteria.searchQuery">
                            </span>
                            </div>
                            <div ng-if="message.attachments.length > 0" ng-repeat="file in message.attachments">
                                <a href="" ng-click="messageVm.openAttachment(file)" style="font-size: 13px;"
                                   title="Click to download file">
                                    {{file.name}}
                                </a>

                                <div class="thmb thmb-prev img-thumbnail" ng-click="messageVm.showImage(file)"
                                     ng-if="file.extension == '.JPEG' || file.extension == '.jpeg' || file.extension == '.jpg' || file.extension == '.JPG' || file.extension == '.PNG' || file.extension == '.png' || file.extension == '.mp4' || file.extension == '.gif' ">
                                    <%--<span class="closeImage">&times;</span>--%>
                                    <img id="img{{$index}}" ng-src="api/col/attachments/{{file.id}}/download"
                                         class="img-responsive"
                                         ng-if="file.extension == '.JPEG' || file.extension == '.jpeg' || file.extension == '.jpg' || file.extension == '.JPG' || file.extension == '.PNG' || file.extension == '.png' || file.extension == '.gif' ">
                                    <video id="vid{{$index}}" controls
                                           class="img-responsive" ng-if="file.extension == '.mp4' " type="video/mp4">
                                        <source ng-src="{{messageVm.getVideo(file.id)}}" type="video/mp4">
                                    </video>

                                    <div class="image-info-panel">
                                        <div>{{file.createdDate}}</div>
                                    </div>
                                </div>
                            </div>
                            <div style="font-size: 10px; text-align: right;">{{message.createdDate}}</div>

                        </div>
                    </div>
                    <div id="myModal0" class="img-model modal">
                        <span class="closeImage10">&times;</span>
                        <img class="modal-content" id="img10">
                    </div>

                    <div class="message-input-section"
                         ng-if="selectedProject.locked == false && (hasPermission('permission.storeInventory.writeMessage') || login.person.isProjectOwner)"
                         ng-class="{'attachmentsHeight' :messageVm.importFile.length > 0, 'noAttachmentsHeight' :messageVm.importFile.length == 0}">
                        <div ng-if="messageVm.importFile.length > 0" style="height: 50px;overflow: auto;">
                            <ul ng-repeat="file in messageVm.importFile" style="display: inline !important;">
                                <li>{{file.name}}<span style="padding-left: 2px;"
                                                       ng-click="messageVm.removeAttachment(file)"><i
                                        class="fa fa-times-circle"></i></span></li>
                            </ul>
                        </div>
                        <input type="text" placeholder="Enter message" class="message-input"
                               ng-model="messageVm.newMessage.msgText" ng-enter="messageVm.postMessage()">

                        <div style="float: right;margin-top: 15px;margin-right: 20px;color: #9d9d9d;font-size: 18px;">
                            <input type="file" id="file" value="file" multiple style="display: none"
                                   ng-click="messageVm.uploadAttachment()"/>
                            <label for="file"> <i class="fa fa-paperclip" aria-hidden="true"
                                                  title="Upload Attachments"
                                                  style="cursor:pointer;margin-right: 5px;"></i></label>
                            <span ng-click="messageVm.postMessage()"><i class="fa fa-paper-plane" title="send"
                                                                        style="cursor:pointer;"></i></span>
                        </div>
                    </div>
                </div>

                <%--- group members view  ---%>
                <div ng-if="messageVm.showInfo">
                    <div style="position: absolute;top: 50px;bottom: 50px;left: 0;right: 0;overflow-y: auto;padding: 20px;">
                        <div class="members">
                            <div ng-repeat="person in messageVm.selectedGrp.msgGrpMembers" class="group">
                                <div class="avatar" style="background-color: lightslategray">
                                    {{person.personName.charAt(0)}}
                                </div>
                                <div class="group-details">
                                    <span style="margin-left: 7px;">{{person.personName}}</span>

                                    <div class="deleteMember" style="padding-left: 10px;cursor: pointer;"
                                         ng-if="login.person.isProjectOwner">
                                        <i class="fa fa-minus-circle"
                                           title="Delete Member"
                                           ng-click="messageVm.deleteGroupMember(person)"
                                           aria-hidden="true"></i>
                                        <br>
                                    </div>
                                </div>
                                <div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="message-input-section" style="height: 40px !important;">
                        <div style="float: right;margin-top: 6px;margin-left: 20px;color: #9d9d9d;">
                            <Button class="btn btn-success btn-xs" ng-click="messageVm.viewGroupMembers()">Add Members
                            </Button>
                        </div>
                    </div>
                </div>

                <%--- group members selection view  ---%>
                <div ng-show="messageVm.selectView">
                    <div style="position: absolute;top: 50px;bottom: 50px;left: 0;right: 0;overflow-y: auto;padding: 20px;">
                        <div class="members">
                            <table class="table table-striped">
                                <thead>
                                <th></th>
                                <th style="vertical-align: middle;">Name</th>
                                <th style="vertical-align: middle;">Phone</th>
                                <th style="vertical-align: middle;">Email</th>
                                </thead>
                                <tbody>
                                <tr ng-if="messageVm.loadingMembers == true">
                                    <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading ...
                            </span>
                                    </td>
                                </tr>
                                <tr ng-if="messageVm.loginPersons.length == 0 && !messageVm.loadingMembers">
                                    <td colspan="12" style="padding-left: 30px;">No Logins</td>
                                </tr>
                                <tr ng-repeat="login in messageVm.loginPersons | filter: {person: {fullName: searchText}}">
                                    <th style="width: 50px; text-align: center">
                                        <i class="fa fa-plus-circle" title="Add Member"
                                           ng-click="messageVm.addToGroup(login)" aria-hidden="true"></i>
                                    </th>
                                    <td style="vertical-align: middle;">
                                        {{login.person.fullName}}
                                    </td>
                                    <td style="vertical-align: middle;">
                                        {{login.person.phoneMobile}}
                                    </td>
                                    <td style="vertical-align: middle;">
                                        {{login.person.email}}
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="message-input-section" style="padding-left: 0 !important;padding-right: 0 !important;"
                         ng-if="messageVm.addedMembers.length > 0">
                        <ui-select multiple ng-model="messageVm.addedMembers" theme="bootstrap"
                                   style="background-color: #fff;height: 50px; overflow-y: auto"
                                   on-remove="messageVm.removePerson($item)">
                            <ui-select-match style="background-color: #fff;">
                                {{$item.person.fullName}}
                            </ui-select-match>
                            <ui-select-choices repeat="person in messageVm.addedMembers"
                                               style="position: relative;!important;">
                                <div ng-bind="person.person.fullName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

