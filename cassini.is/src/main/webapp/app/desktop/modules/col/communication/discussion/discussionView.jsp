<div style="height: 100%">
    <style scoped>
        .discussion-groups {
            position: absolute;
            top: 57px;
            bottom: 0px;
            left: 0;
            right: 0;
            overflow-y: auto;
            border: 1px solid #ddd;
        }

        .panel-title {
            font-family: 'LatoRegular', 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, sans-serif;
            font-size: 16px;
        }

        .full-height {
            height: 100%;
        }

        .new-group-form {
            padding: 10px;
        }

        .new-group-form textarea {
            resize: none;
        }

        .group-members .ui-select-match-close {
            font-size: 14px;
            margin-top: 5px;
        }

        .group-item {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            cursor: pointer;
        }

        .group-item .group-name {
            font-size: 16px;
        }

        .group-item.selected {
            background-color: #eef2f3;
        }

        .group-item .group-description {
            font-size: 14px;
        }

        .group-item span {
            color: #959799;
        }

        .group-item .pull-right {
            padding-top: 0px;
        }

        .discussion-input-container {
            position: absolute;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 65px;
            border: 1px solid #ddd;
            background-color: #eef2f3;
            padding: 10px;
        }

        .group-messages {
            position: absolute;
            top: 57px;
            left: 0;
            bottom: 65px;
            right: 0;
            overflow-y: auto;
            font-size: 15px;
        }

        .group-messages .discussion {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        .group-messages .discussion .discussion-info {
            font-size: 12px;
        }

        .group-messages .discussion:nth-child(even) {
            background-color: #eef2f3;
            border-right: 1px solid #ddd;
            border-left: 1px solid #ddd;
        }

        .group-messages .discussion .avatar {
            background: url(/app/assets/images/user-grey.png) no-repeat center center;
            height: 40px;
            background-size: 40px 40px;
        }

    </style>

    <div class="row full-height">
        <div class="col-sm-12 full-height">
            <div class="col-sm-4 full-height" style="padding:0">
                <div class="panel panel-default full-height" style="border: 1px solid #d7d7d7;">
                    <div class="panel-heading">
                        <div class="panel-btns">
                            <a href="" title="New Group" ng-click="discussionVm.addNewDiscussionGroup()"><i
                                    class="fa fa-plus"></i></a>
                        </div>
                        <h3 class="panel-title">Discussion Groups</h3>
                    </div>
                    <div class="panel-body discussion-groups">
                        <div ng-if="discussionVm.newMode == false && discussionVm.discussionGroups.length == 0"
                             style="padding: 10px;">
                            No discussions
                        </div>
                        <div ng-if="discussionVm.newMode == true" class="new-group-form">
                            <h4 class="text-center text-primary">New Discussion</h4>

                            <div class="form-group">
                                <label class="control-label" for="groupName">Name</label>
                                <input type="text" class="form-control" id="groupName"
                                       ng-model="discussionVm.newDiscussionGroup.name">
                            </div>
                            <div class="form-group">
                                <label class="control-label" for="groupDescription">Description</label>
                                <textarea name="" class="form-control" id="groupDescription" rows="5"
                                          ng-model="discussionVm.newDiscussionGroup.description"></textarea>
                            </div>
                            <%-- <div class="form-group">
                                 <label class="control-label">Members</label>
                                 <ui-select class="group-members" multiple ng-model="discussionVm.newGroup.persons" theme="bootstrap" style="width:100%">
                                     <ui-select-match placeholder="Select">{{$item.firstName}}</ui-select-match>
                                     <ui-select-choices repeat="person in discussionVm.persons | filter: $select.search">
                                         <div ng-bind="person.firstName | highlight: $select.search"></div>
                                     </ui-select-choices>
                                 </ui-select>
                             </div>--%>
                            <div class="text-center">
                                <button class='btn btn-xs btn-default mr10' ng-click='discussionVm.cancelNew()'>Cancel
                                </button>
                                <button class='btn btn-xs btn-success' ng-click='discussionVm.createDiscussionGroup()'>
                                    Create
                                </button>
                            </div>
                        </div>
                        <div ng-if="discussionVm.newMode == false">
                            <div class="group-item"
                                 ng-class="{'selected': group.selected}"
                                 ng-click="discussionVm.selectGroup(group)"
                                 ng-repeat="group in discussionVm.discussionGroups">
                                <div class="pull-right">
                                    <div>
                                        <span title="Messages" class="mr10"><i class="fa fa-comment mr5"></i>{{group.messagesCount}}</span>
                                        <span title="Members"><i
                                                class="fa fa-user mr5"></i>{{group.activeUsersCount}}</span>
                                    </div>
                                    <div style="text-align: right;">
                                        <span title="Edit" class="mr10"><i class="fa fa-pencil mr5"></i></span>
                                        <span title="Remove"><i class="fa fa-remove"></i></span>
                                    </div>
                                </div>
                                <div>
                                    <div class="group-name text-primary">
                                        {{group.name}}
                                    </div>
                                    <div title="{{group.description}}" class="group-description">
                                        {{group.description | limitTo: 15}}{{group.description.length > 15 ? '...' :
                                        ''}}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-8 full-height" style="padding:0">
                <div class="panel panel-default full-height" style="border: 1px solid #d7d7d7;">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            Group Discussions
                            <span ng-if="discussionVm.selectedGroup != null">
                                (<span style="color: #337ab7">{{discussionVm.selectedGroup.name}}</span>)
                            </span>
                        </h3>
                    </div>
                    <div class="panel-body" style="border-width: 0px;">

                        <div ng-if="discussionVm.selectedGroup.selected==true">
                            <discussions group="discussionVm.selectedGroup" idd="discussionVm.projectId"></discussions>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>