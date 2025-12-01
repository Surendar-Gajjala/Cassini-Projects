<div class="col-md-12" style="height: 100%;">
    <div ng-if="changeTypeBasicVm.changeType != null && !newItemTypeCreating">
        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success min-width" ng-click="changeTypeBasicVm.onSave()"
                    ng-if="hasPermission('changetype','edit')" translate>SAVE
            </button>
        </div>
        <div class="">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>NAME</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-model="changeTypeBasicVm.changeType.name"
                                       ng-disabled="changeTypeBasicVm.changeTypeECOsExist == true || !hasPermission('changetype','edit')">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  ng-model="changeTypeBasicVm.changeType.description"
                                  ng-disabled="!hasPermission('changetype','edit')"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>AUTO_NUMBER_SOURCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="changeTypeBasicVm.changeType.autoNumberSource" theme="bootstrap"
                                           style="width:100%"
                                           <%--ng-disabled="changeTypeBasicVm.changeTypeECOsExist == true || !hasPermission('classification','edit')"--%>
                                           ng-disabled="!hasPermission('changetype','edit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                                    <ui-select-choices
                                            repeat="source in autoNumbers | filter: $select.search">
                                        <div ng-bind="source.name"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="changeTypeBasicVm.changeType.objectType == 'ECRTYPE' || changeTypeBasicVm.changeType.objectType == 'DCRTYPE'">
                            <label class="col-sm-4 control-label">
                                <span translate>CHANGE_REASON_TYPES</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="changeTypeBasicVm.changeType.changeReasonTypes" theme="bootstrap"
                                           style="width:100%"
                                           ng-disabled="!hasPermission('changetype','edit')">
                                    <ui-select-match placeholder="Select">
                                        {{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in listOfValues | filter: $select.search">
                                        <div title="{{lov.name}}">
                                            {{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>