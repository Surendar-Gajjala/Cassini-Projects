<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>BASIC_INFO</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span translate id="select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <classification-tree
                                                    ng-if="itemsMode == null || itemsMode == '' || itemsMode == undefined"
                                                    on-select-type="newItemVm.onSelectType"></classification-tree>
                                            <item-class-tree item-class="{{itemsMode}}"
                                                             ng-if="itemsMode != null && itemsMode != '' && itemsMode != undefined"
                                                             on-select-type="newItemVm.onSelectType"></item-class-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newItemVm.newItem.itemType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-show="newItemVm.newItem.itemType != null && newItemVm.newItem.itemType.itemClass != 'DOCUMENT'">
                        <label class="col-sm-4 control-label">
                            <span translate>Configuration</span>: </label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="normalItem" name="configType" type="radio" checked
                                       ng-click="newItemVm.selectConfiguration('None', $event)">
                                <label for="normalItem" onclick="" translate>NORMAL</label>

                                <input id="configurationItem" name="configType" type="radio"
                                       ng-click="newItemVm.selectConfiguration('Configurable', $event)">
                                <label for="configurationItem" onclick="" translate>CONFIGURABLE</label>

                                <input id="configuredItem" name="configType" type="radio"
                                       ng-click="newItemVm.selectConfiguration('Configured', $event)">
                                <label for="configuredItem" onclick="" translate>CONFIGURED</label>
                                <a href=""></a>
                            </div>
                            <%--
                            <div class="form-check"
                                 style="border: 1px solid #ddd;padding:8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="gridRadios" id="normalItem"
                                           ng-click="newItemVm.selectConfiguration('None', $event)"
                                           checked><span style="padding: 2px;margin-left: 5px;" translate>NORMAL</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="gridRadios"
                                           id="configurationItem"
                                           ng-click="newItemVm.selectConfiguration('Configurable', $event)"><span
                                        style="padding: 2px;margin-left: 5px;" translate>CONFIGURABLE</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="gridRadios" id="configuredItem"
                                           ng-click="newItemVm.selectConfiguration('Configured', $event)"><span
                                        style="padding: 2px;margin-left: 5px;" translate>CONFIGURED</span>
                                </label>
                            </div>
                            --%>
                        </div>
                    </div>


                    <div class="form-group" ng-show="newItemVm.newItem.configured">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_INSTANCE_OF</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newItemVm.newItem.instanceItem" theme="bootstrap"
                                       style="width:100%" on-select="newItemVm.selectItem()">
                                <ui-select-match placeholder="{{newItemVm.selectInstance}}">
                                    {{$select.selected.itemNumber}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="item in newItemVm.itemInstances | filter: $select.itemNumber.search">
                                    <div ng-bind-html="trustAsHtml((item.itemNumber | highlight: $select.itemNumber.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-show="newItemVm.newItem.configured && newItemVm.newItem.instanceItem != null && newItemVm.newItem.instanceItem.hasBom">
                        <label class="col-sm-4 control-label">
                            <span translate>BOM_CONFIGURATION</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newItemVm.newItem.bomConfig" theme="bootstrap"
                                       style="width:100%" on-select="newItemVm.selectBomConfiguration()">
                                <ui-select-match placeholder="{{selectBomConfiguration}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bomConfiguration in newItemVm.bomConfigurations | filter: $select.name.search">
                                    <div ng-bind-html="trustAsHtml((bomConfiguration.name | highlight: $select.name.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newItemVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newItemVm.newItem.itemNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-hide="newItemVm.newItem.itemType.itemClass == 'DOCUMENT' || itemDetailsMode == 'documents' || itemDetailsMode == 'products'">
                        <label class="col-sm-4 control-label">
                            <span translate>MAKE_OR_BUY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">

                            <div class="switch-toggle switch-candy">
                                <input id="prTypeC" name="view" type="radio" checked
                                       ng-click="newItemVm.selectMakeOrBuy('make', $event)"
                                       ng-disabled="newItemVm.newItem.itemType == null || newItemVm.newItem.itemType.itemClass == 'PRODUCT'">
                                <label for="prTypeC" onclick="" translate>MAKE</label>

                                <input id="prTypeI" name="view" type="radio"
                                       ng-click="newItemVm.selectMakeOrBuy('buy', $event)"
                                       ng-disabled="newItemVm.newItem.itemType == null || newItemVm.newItem.itemType.itemClass == 'PRODUCT'">
                                <label for="prTypeI" onclick="" translate="">BUY</label>
                                <a href=""></a>
                            </div>

                            <%--
                            <div class="form-check"
                                 style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="makeOrBuy"
                                           id="make" ng-click="newItemVm.selectMakeOrBuy('make', $event)"
                                           ng-disabled="newItemVm.newItem.itemType == null || newItemVm.newItem.itemType.itemClass == 'PRODUCT'">
                                    <span style="padding: 2px;margin-left: 5px;"
                                          translate>MAKE</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="makeOrBuy"
                                           id="buy" ng-click="newItemVm.selectMakeOrBuy('buy', $event)"
                                           ng-disabled="newItemVm.newItem.itemType == null || newItemVm.newItem.itemType.itemClass == 'PRODUCT'"><span
                                        style="padding: 2px;margin-left: 5px;" translate>BUY</span>
                                </label>
                            </div>--%>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_ITEMNAME' | translate}}"
                                   name="title" ng-model="newItemVm.newItem.itemName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" placeholder="{{'ENTER_ITEMDESCRIPTION' | translate}}"
                                      rows="3" style="resize: none"
                                      ng-model="newItemVm.newItem.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>UNITS</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" ng-model="newItemVm.newItem.units">
                        </div>
                    </div>

                    <div class="form-group"
                         ng-show="newItemVm.newItem.itemType != null && newItemVm.newItem.itemType.softwareType">
                        <label class="col-sm-4 control-label">
                            <span>GitHub Repository</span>: </label>

                        <div class="col-sm-7">
                            <ui-select theme="bootstrap"
                                       style="width:100%"
                                       ng-model="newItemVm.selectedGitHubRepo"
                                       on-select="newItemVm.setItemRepository($item)">
                                <ui-select-match placeholder="Select repository">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="repo in newItemVm.githubRepos | filter: $select.search">
                                    <div ng-bind="repo.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>THUMBNAIL</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" id="filename" ng-if="newItemVm.imageView"
                                   type="file" ng-file-model="newItemVm.itemImage.thumbnail"/>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-show="!newItemVm.newItem.itemType.requiredEco && newItemVm.newItem.itemType != null">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newItemVm.newItem.workflowDefinition" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder=Select>{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices repeat="workflow in newItemVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_FROM</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{effectiveFromPlaceholder}}"
                                   type="text" name="top-left" ng-model="newItemVm.newItem.fromDate"
                                   start-finish-date-picker/>
                            <i class="fa fa-times" ng-if="newItemVm.newItem.fromDate != null"
                               style="float: right;margin-top: -26px;margin-right: 10px;cursor: pointer;"
                               ng-click="newItemVm.newItem.fromDate = null"></i>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_TO</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{effectiveToPlaceholder}}"
                                   type="text" name="top-left" ng-model="newItemVm.newItem.toDate"
                                   start-finish-date-picker/>
                            <i class="fa fa-times" ng-if="newItemVm.newItem.toDate != null"
                               style="float: right;margin-top: -26px;margin-right: 10px;cursor: pointer;"
                               ng-click="newItemVm.newItem.toDate = null"></i>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="newItemVm.newItem.itemType.itemClass == 'PART' && newItemVm.newItem.makeOrBuy == 'BUY'">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUIRES_COMPLIANCE</span>: </label>

                        <div class="col-sm-7" style="margin-top: 6px;">
                            <label class="switch-light switch-candy" onclick=""
                                   style="max-width: 150px;cursor: pointer">
                                <input type="checkbox" ng-model="newItemVm.newItem.requireCompliance">
                                    <span>
                                        <span translate>NO</span>
                                        <span translate>YES</span>
                                        <a href=""></a>
                                    </span>
                            </label>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newItemVm.newItemAttributes.length > 0"
                                     attributes="newItemVm.newItemAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newItemVm.newItemRevisionAttributes.length > 0"
                                     attributes="newItemVm.newItemRevisionAttributes"></attributes-view>
                </form>
                <%--<br>
                <h4 ng-if="newItemVm.requiredAttributes.length > 0 || newItemVm.attributes.length > 0"
                    ng-hide="newItemVm.newItem.configured"
                    class="section-title" style="" translate>ATTRIBUTES
                </h4>
                <h4 ng-show="newItemVm.newItem.configured && newItemVm.configuredAttributes.length > 0"
                    class="section-title" style="" translate>CONFIGURABLE_ATTRIBUTES
                </h4>
                <br>--%>

                <div ng-hide="newItemVm.newItem.configured || newItemVm.newItem.configurable">
                    <form class="form-horizontal" ng-if="newItemVm.newItem.itemType != null">
                        <attributes-view show-objects="selectObjectValues" ng-if="newItemVm.attributes.length > 0"
                                         attributes="newItemVm.attributes"></attributes-view>
                    </form>
                </div>
                <div ng-show="newItemVm.newItem.configured">
                    <form class="form-horizontal" ng-if="newItemVm.newItem.itemType != null">
                        <attributes-view show-objects="selectObjectValues" object="newItemVm.newItem"
                                         ng-if="newItemVm.configuredAttributes.length > 0"
                                         attributes="newItemVm.configuredAttributes"></attributes-view>
                    </form>
                </div>
                <div ng-show="newItemVm.newItem.configurable">
                    <form class="form-horizontal" ng-if="newItemVm.newItem.itemType != null">
                        <attributes-view show-objects="selectObjectValues" object="newItemVm.newItem"
                                         ng-if="newItemVm.configurableAttributes.length > 0"
                                         attributes="newItemVm.configurableAttributes"></attributes-view>
                        <br>
                        <attributes-view show-objects="selectObjectValues" object="newItemVm.newItem"
                                         ng-if="newItemVm.nonConfigurableAttributes.length > 0"
                                         attributes="newItemVm.nonConfigurableAttributes"></attributes-view>
                    </form>
                </div>
                <br><br><br>
            </div>
        </div>
    </div>
</div>
