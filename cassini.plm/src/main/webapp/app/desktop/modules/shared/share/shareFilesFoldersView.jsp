<div>
    <style scoped>
        .ui-select-multiple.ui-select-bootstrap .ui-select-match-item > span {
        }

        .ui-select-multiple.ui-select-bootstrap .ui-select-match .close {
            font-size: 21px !important;
            line-height: 18px !important;
            opacity: 0.8;
            font-weight: 400;
        }

        .ml8 {
            margin-left: 8px;
        }
    </style>


    <div>
        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal" ng-if="shareFilesFoldersVm.fileShareType == 'PERSON'">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERSONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">

                                    <ui-select ng-show="shareFilesFoldersVm.users.length > 0" multiple
                                               ng-model="shareFilesFoldersVm.sharedToObjects" theme="bootstrap"
                                               checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareFilesFoldersVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareFilesFoldersVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareFilesFoldersVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareFilesFoldersVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareFilesFoldersVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareFilesFoldersVm.selectPermission}}">
                                            {{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareFilesFoldersVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareFilesFoldersVm.processing" ng-cloak>
                                <span translate>PROGRAM_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>

                        <form class="form-horizontal" ng-if="shareFilesFoldersVm.fileShareType == 'PROJECT'">
                            <div class='responsive-table'>
                                <table class='table table-striped highlight-row'>
                                    <thead>
                                    <tr>
                                        <th style="width: 20px;">
                                            <%--<input type="checkbox" ng-model="shareFilesFoldersVm.selectAllCheck"
                                                   ng-change="shareFilesFoldersVm.selectAll()" class="form-control"/>--%>
                                        </th>
                                        <th class="col-width-500" translate>NAME</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-if="shareFilesFoldersVm.loading == true">
                                        <td colspan="14"><img
                                                src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                                class="mr5"><span translate>LOADING_PROJECTS</span>
                                        </td>
                                    </tr>
                                    <tr ng-if="shareFilesFoldersVm.loading == false && shareFilesFoldersVm.programProjects.length == 0">
                                        <td colspan="12"
                                            style="background-color: #f9fbfe  !important;color: unset !important;">
                                            <div class="no-data">
                                                <img src="app/assets/no_data_images/ManufacturerParts.png" alt=""
                                                     class="image">

                                                <div class="message" translate>NO_PROJECTS</div>
                                                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;"
                                                     translate>
                                                    NO_PERMISSION_MESSAGE
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr ng-repeat="programProject in shareFilesFoldersVm.programProjects">
                                        <td style="width: 20px;text-align: center">
                                            <input type="checkbox" ng-model="programProject.selected"
                                                   ng-hide="programProject.project == shareFilesFoldersVm.objectId || programProject.objectType == 'GROUP'"
                                                   ng-change="shareFilesFoldersVm.selectCheck(programProject)"
                                                   class="form-control"/>
                                        </td>
                                        <td class="col-width-500">
                                            <span class="level{{programProject.level}}">
                                                <i ng-if="programProject.children.length > 0"
                                                   ng-hide="programProject.project != null && programProject.project == shareFilesFoldersVm.objectId"
                                                   class="fa"
                                                   style="cursor: pointer;margin-right: 0;"
                                                   ng-class="{'fa-caret-right': (programProject.expanded == false || programProject.expanded == null || programProject.expanded == undefined),
                                                              'fa-caret-down': programProject.expanded == true}"
                                                   ng-click="shareFilesFoldersVm.toggleNode(programProject)">
                                                </i>
                                                 <i class="mr5 fa" title="{{ExpandCollapse}}"
                                                    ng-if="programProject.objectType == 'FILE'"
                                                    style="cursor: pointer !important;color: limegreen !important;"
                                                    ng-class="{'fa-folder': (programProject.expanded == false || programProject.expanded == null || programProject.expanded == undefined),
                                                               'fa-folder-open': programProject.expanded == true,
                                                               'ml8':programProject.children.length == 0}">
                                                 </i>
                                                <span ng-if="programProject.objectType == 'GROUP' || programProject.objectType == 'PROJECT'"
                                                      ng-bind-html="programProject.name"
                                                      ng-class="{'ml8':programProject.children.length == 0 || programProject.project == shareFilesFoldersVm.objectId}"></span>
                                                <span ng-if="programProject.objectType == 'FILE'">{{programProject.name}}</span>
                                            </span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


</div>