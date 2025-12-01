<div>
    <style scoped>

        .view-details {

        }

        .view-details .view-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .view-details .view-header .view-title {
            font-size: 16px;
            font-weight: 600;
        }

        .view-details .view-header .view-summary {
            color: #707d91;
            font-weight: 300;
        }

    </style>

    <div class="view-details">
        <div class="view-header d-flex">
            <div style="flex: 1">
                <div class="view-title" translate>ROLE_INFORMATION_TITLE</div>
                <div class="view-summary" translate>VIEW_AND_UPDATE_TITLE</div>
            </div>
            <div class="header-buttons">
                <button class="btn btn-sm btn-new" ng-click="roleBasicVm.updateRole()" translate>SAVE</button>
            </div>
        </div>
        <div class="col-xs-12 col-md-10 col-md-offset-1 col-md-8 col-md-offset-1" style="margin-top: 10px;">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-md-4 control-label" for="name"><span translate>NAME</span><span class="asterisk"> *</span>
                        :</label>

                    <div class="col-md-7">
                        <input type="text"
                               ng-disabled="roleBasicVm.personGroup.name =='Administrator'"
                               class="form-control" id="name"
                               placeholder="{{ 'ENTER_NAME' | translate }}" ng-model="roleBasicVm.roleName">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label" for="description"><span translate>DESCRIPTION</span> :</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" id="description"
                               placeholder="{{ 'ENTER_DESCRIPTION_TITLE' | translate }}" ng-model="roleBasicVm.personGroup.description">
                    </div>

                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label"><span translate>PROFILE_LABEL</span>
                        <span class="asterisk">*</span>:</label>

                    <div class="col-md-7" style="border: 0;">
                        <ui-select ng-model="roleBasicVm.personGroup.profile"
                                   on-select="roleBasicVm.onSelectProfile($item)"
                                   theme="bootstrap"
                                   style="width: 100%;">
                            <ui-select-match placeholder="{{selectProfile}}">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices style="max-height: 191px;"
                                               repeat="profile in roleBasicVm.allProfiles | filter: $select.search">
                                {{profile.name}}
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label" for="isActive"><span translate>IS_ACTIVE</span> :</label>

                    <div class="col-md-6">
                        <input type="checkbox" id="isActive" switch="none" checked=""
                               ng-model="roleBasicVm.personGroup.isActive">
                        <%--<label for="isActive" data-on-label="Yes" data-off-label="No"></label>--%>
                        <label for="isActive" data-on-label="{{yes}}" data-off-label="{{no}}"></label>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-4 control-label" for="external"><span translate>EXTERNAL</span> :</label>

                    <div class="col-md-6">
                        <input type="checkbox" id="external" switch="none" checked=""
                               ng-model="roleBasicVm.personGroup.external"
                        <%--ng-disabled="!roleBasicVm.isGroupEdit"--%>>
                        <%--<label for="external" data-on-label="Yes" data-off-label="No"></label>--%>
                        <label for="external"  data-on-label="{{yes}}" data-off-label="{{no}}"></label>
                    </div>
                </div>
        </div>
        </form>
    </div>
</div>