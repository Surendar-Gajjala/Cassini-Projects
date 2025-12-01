<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }


    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CHANGE_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;"
                                             ng-if="varianceType == 'Deviation'">
                                            <change-type-tree on-select-type="newVarianceVm.onSelectType"
                                                              change-type="Deviation"></change-type-tree>
                                        </div>
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;"
                                             ng-if="varianceType == 'Waiver'">
                                            <change-type-tree on-select-type="newVarianceVm.onSelectType"
                                                              change-type="Waiver"></change-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newVarianceVm.newVariance.changeType.name" readonly>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newVarianceVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newVarianceVm.newVariance.varianceNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span ng-if="varianceType == 'Deviation'" translate>DEVIATION_FOR</span>
                            <span ng-if="varianceType == 'Waiver'" translate>WAIVER_FOR</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="">

                            <div class="switch-toggle switch-candy">
                                <input id="prTypeC" name="varFor" type="radio" value="ITEMS" checked
                                       ng-model="newVarianceVm.newVariance.varianceFor">
                                <label for="prTypeC" onclick="" translate>ITEMS_ALL_TITLE</label>

                                <input id="prTypeI" name="varFor" type="radio" value="MATERIALS"
                                       ng-model="newVarianceVm.newVariance.varianceFor">
                                <label for="prTypeI" onclick="" translate="">MATERIALS</label>
                                <a></a>
                            </div>
                            <%--
                            <div class="form-check"
                                 style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" value="ITEMS" name="gridRadios"
                                           id="gridRadios3"
                                           ng-model="newVarianceVm.newVariance.varianceFor"
                                           checked><span style="padding: 2px;margin-left: 5px;"
                                                         translate>ITEMS_ALL_TITLE</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" value="MATERIALS" name="gridRadios"
                                           id="gridRadios4"
                                           ng-model="newVarianceVm.newVariance.varianceFor"><span
                                        style="padding: 2px;margin-left: 5px;" translate>MATERIALS</span>
                                </label>
                            </div>--%>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{(varianceType == 'Deviation' ? 'ENTER_DEVIATION_TITLE' : 'ENTER_WAIVER_TITLE') | translate}}"
                                   ng-model="newVarianceVm.newVariance.title">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newVarianceVm.newVariance.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label" ng-if="varianceType == 'Deviation'">
                            <span translate>REASON_FOR_DEVIATION</span>
                            <span class="asterisk">*</span> : </label>
                        <label class="col-sm-4 control-label" ng-if="varianceType == 'Waiver'">
                            <span translate>REASON_FOR_WAIVER</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea style="resize: none;" class="form-control" rows="3" name="ReasonForVariance"
                                      placeholder="{{(varianceType == 'Deviation' ? 'ENTER_REASON_FOR_DEVIATION' : 'ENTER_REASON_FOR_WAIVER') | translate}}"
                                      ng-model="newVarianceVm.newVariance.reasonForVariance"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CURRENT_REQUIREMENT</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                             <textarea style="resize: none;" class="form-control" rows="3" name="CurrentRequirement"
                                       placeholder="{{'ENTER_CURRENT_REQUIREMENT' | translate}}"
                                       ng-model="newVarianceVm.newVariance.currentRequirement"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUIREMENT_DEVIATION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                             <textarea style="resize: none;" class="form-control" rows="3" name="RequirementDeviation"
                                       placeholder="{{'ENTER_REQUIREMENT_DEVIATION' | translate}}"
                                       ng-model="newVarianceVm.newVariance.requirementDeviation"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ORIGINATOR</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newVarianceVm.newVariance.originator" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newVarianceVm.select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newVarianceVm.persons | filter: $select.fullName.search | orderBy:'fullName'">
                                    <div ng-bind-html="trustAsHtml((person.fullName | highlight: $select.fullName.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="varianceType == 'Deviation'">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_TYPE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <div class="switch-toggle switch-candy">
                                <input id="effQ" name="effType" type="radio" value="QUANTITY" checked
                                       ng-model="newVarianceVm.newVariance.effectivityType">
                                <label for="effQ" onclick="" translate>QUANTITY</label>

                                <input id="effD" name="effType" type="radio" value="DURATION"
                                       ng-model="newVarianceVm.newVariance.effectivityType">
                                <label for="effD" onclick="" translate="">DURATION</label>
                                <a></a>
                            </div>

                            <%--
                            <div class="form-check" style="padding: 5px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" value="QUANTITY" name="gridRadios"
                                           id="gridRadios1"
                                           ng-model="newVarianceVm.newVariance.effectivityType"
                                           checked><span style="padding: 2px;margin-left: 5px;"
                                                         translate>QUANTITY</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" value="DURATION" name="gridRadios"
                                           id="gridRadios2"
                                           ng-model="newVarianceVm.newVariance.effectivityType"><span
                                        style="padding: 2px;margin-left: 5px;" translate>DURATION</span>
                                </label>
                            </div>
                            --%>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newVarianceVm.newVariance.effectivityType == 'DURATION'">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_FROM</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{effectiveFromPlaceholder}}"
                                   type="text" name="top-left" ng-model="newVarianceVm.newVariance.effectiveFrom"
                                   start-finish-date-picker/>
                            <i class="fa fa-times" ng-if="newVarianceVm.newVariance.effectiveFrom != null"
                               style="float: right;margin-top: -26px;margin-right: 10px;cursor: pointer;"
                               ng-click="newVarianceVm.newVariance.effectiveFrom = null"></i>
                        </div>
                    </div>


                    <div class="form-group" ng-if="newVarianceVm.newVariance.effectivityType == 'DURATION'">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_TO</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{effectiveToPlaceholder}}"
                                   type="text" name="top-left" ng-model="newVarianceVm.newVariance.effectiveTo"
                                   start-finish-date-picker/>
                            <i class="fa fa-times" ng-if="newVarianceVm.newVariance.effectiveTo != null"
                               style="float: right;margin-top: -26px;margin-right: 10px;cursor: pointer;"
                               ng-click="newVarianceVm.newVariance.effectiveTo = null"></i>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES_TITLE</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_NOTES' | translate}}"
                                      ng-model="newVarianceVm.newVariance.notes"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newVarianceVm.newVariance.workflowDefinition"
                                       on-select="newVarianceVm.onSelectWorkflow()" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder={{newVarianceVm.select}}>{{$select.selected.name}}
                                    [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow in newVarianceVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newVarianceVm.ecoRequiredProperties.length > 0"
                                     attributes="newVarianceVm.ecoRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newVarianceVm.ecoProperties.length > 0"
                                     attributes="newVarianceVm.ecoProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newVarianceVm.attributes.length > 0"
                                     attributes="newVarianceVm.attributes"></attributes-view>

                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>
