<div class="col-md-12" style="height: 100%;">
    <style scoped>

    </style>

    <div ng-if="selectedClassificationType !=null || (itemTypeBasicVm.itemType != null && !newItemTypeCreating)" style="overflow-y: hidden;">
        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success" ng-click="itemTypeBasicVm.onSave()"
                    ng-if="hasPermission('itemtype','edit')" translate>SAVE
            </button>

        </div>
        <div class="">
            <%--<h4 class="section-title" translate>BASIC_INFO</h4>--%>

            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">


                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>NAME</span>
                                <span class="asterisk">*</span> :</label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-model="itemTypeBasicVm.itemType.name"
                                       ng-disabled="itemTypeBasicVm.itemTypeItemExist == true || !hasPermission('itemtype','edit')">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="itemTypeBasicVm.itemType.description"
                                      ng-disabled="!hasPermission('itemtype','edit')"></textarea>
                            </div>
                        </div>

                        <%--<div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>ITEM_CLASS</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="itemTypeBasicVm.itemType.itemClass" theme="bootstrap"
                                           style="width:100%" on-select="itemTypeBasicVm.onSelectItemClass($item)"
                                           ng-disabled="!hasPermission('permission.classification.edit')">
                                    <ui-select-match placeholder="Select Item Class">{{$select.selected.label}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="itemClass.value as itemClass in itemTypeBasicVm.itemClasses track by itemClass.value | filter: $select.search">
                                        <div>{{itemClass.label}}</div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>--%>

                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>AUTO_NUMBER_SOURCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="itemTypeBasicVm.itemType.itemNumberSource" theme="bootstrap"
                                           style="width:100%"
                                           title="{{itemTypeBasicVm.itemType.numberSource.name}}"
                                           ng-disabled="!hasPermission('itemtype','edit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="source in autoNumbers | filter: $select.search">
                                        <div title="{{source.name}}">{{source.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>REVISION_SEQUENCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="itemTypeBasicVm.itemType.revisionSequence" theme="bootstrap"
                                           style="width:100%"
                                           title="{{itemTypeBasicVm.itemType.revisionSequence.name}}"
                                           ng-disabled="!hasPermission('itemtype','edit') || itemTypeBasicVm.itemTypeItemExist">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in revSequences | filter: $select.search">
                                        <div title="{{lov.name}}">{{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="itemTypeBasicVm.itemType.lifecycle" theme="bootstrap"
                                           style="width:100%"
                                           title="{{itemTypeBasicVm.itemType.lifecycle.name}}"
                                           ng-disabled="!hasPermission('itemtype','edit') || itemTypeBasicVm.itemTypeItemExist"
                                           on-select="itemTypeBasicVm.onSelectLifecycle($item)">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lifecycle in lifecycles | filter: $select.search">
                                        <div title="{{lifecycle.name}}">{{lifecycle.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>IS_SOFTWARE_TYPE</span>: </label>

                            <div class="col-sm-7">
                                <input type="checkbox" id="software" switch="none" checked=""
                                       ng-model="itemTypeBasicVm.itemType.softwareType"
                                       ng-disabled="!hasPermission('itemtype','edit') ||
                                       itemTypeBasicVm.itemTypeItemExist ||
                                       itemTypeBasicVm.itemType.parentTypeObject.softwareType">
                                <label for="software" data-on-label="Yes" data-off-label="No"></label>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>REQUIRES_ECO_TO_CHANGE_LIFECYCLE</span>: </label>

                            <div class="col-sm-7">
                                <input type="checkbox" id="reqlc" switch="none" checked="" ng-model="itemTypeBasicVm.itemType.requiredEco"
                                       ng-disabled="!hasPermission('itemtype','edit') ||
                                           itemTypeBasicVm.itemTypeItemExist">
                                <label for="reqlc" data-on-label="Yes" data-off-label="No"></label>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label">
                                <span translate>DISPLAY_TABS</span>: </label>

                            <div class="col-sm-7" style="padding-left: 8px;">
                                <div class="tabs-checkboxes-container" style="padding: 10px;">
                                    <div ng-repeat="tab in itemTypeBasicVm.tabs" style="display: flex;">
                                        <input type="checkbox" class="form-control input-sm"
                                               style="box-shadow: none !important;width: 16px;margin: 0;height: 20px"
                                               ng-model="tab.selected"/>
                                        <label style="margin: 0 0 0 5px;">{{tab.label}}</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/>
                    </form>

                </div>
            </div>

        </div>
    </div>
</div>


