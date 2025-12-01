<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="editVarianceVm.variance.title">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>:
                            <span class="asterisk">*</span> : </label>


                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="editVarianceVm.variance.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REASON_FOR_VARIANCE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="ReasonForVariance"
                                   ng-model="editVarianceVm.variance.reasonForVariance">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CURRENT_REQUIREMENT</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="CurrentRequirement"
                                   ng-model="editVarianceVm.variance.currentRequirement">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUIREMENT_DEVIATION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="RequirementDeviation"
                                   ng-model="editVarianceVm.variance.requirementDeviation">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ORIGINATOR</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="editVarianceVm.variance.originatorObject" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{editVarianceVm.select}}">{{$select.selected.firstName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in editVarianceVm.persons | filter: $select.firstName.search">
                                    <div ng-bind-html="trustAsHtml((person.firstName | highlight: $select.firstName.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="varianceType == 'Deviation'">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_TYPE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <div class="form-check" style="padding: 5px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" value="QUANTITY" name="gridRadios" id="gridRadios1"
                                           ng-model="editVarianceVm.variance.effectivityType"
                                           checked><span style="padding: 2px;margin-left: 5px;" translate>QUANTITY</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" value="DURATION" name="gridRadios" id="gridRadios2"
                                           ng-model="editVarianceVm.variance.effectivityType"><span
                                        style="padding: 2px;margin-left: 5px;" translate>DURATION</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group" ng-if="editVarianceVm.variance.effectivityType == 'DURATION'">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_FROM</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{effectiveFromPlaceholder}}"
                                   type="text" name="top-left" ng-model="editVarianceVm.variance.effectiveFrom" future-date-picker/>
                            <i class="fa fa-times" ng-if="newItemVm.newItem.fromDate != null"
                               style="float: right;margin-top: -26px;margin-right: 10px;cursor: pointer;"
                               ng-click="editVarianceVm.variance.effectiveTo = null"></i>
                        </div>
                    </div>


                    <div class="form-group" ng-if="editVarianceVm.variance.effectivityType == 'DURATION'">
                        <label class="col-sm-4 control-label">
                            <span translate>EFFECTIVE_TO</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{effectiveToPlaceholder}}"
                                   type="text" name="top-left" ng-model="editVarianceVm.variance.effectiveTo" future-date-picker/>
                            <i class="fa fa-times" ng-if="newItemVm.newItem.toDate != null"
                               style="float: right;margin-top: -26px;margin-right: 10px;cursor: pointer;"
                               ng-click="editVarianceVm.variance.effectiveTo = null"></i>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="editVarianceVm.variance.notes"></textarea>
                        </div>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

