<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>NEW_PART_REQUEST_INFO</h4>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>


                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newNprVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newNprVm.newNpr.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUESTER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newNprVm.newNpr.requester" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{'SELECT_PERSONS' | translate}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person as person in newNprVm.persons | filter: $select.search">
                                    <div>{{person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span>:</label>

                        <div class="col-sm-7">
                            <textarea class="form-control" placeholder="{{'ENTER_ITEMDESCRIPTION' | translate}}"
                                      rows="3" style="resize: none"
                                      ng-model="newNprVm.newNpr.description"></textarea>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REASON_FOR_REQUEST</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" placeholder="{{'ENTER_REASON_FOR_REQUEST' | translate}}"
                                      rows="3" style="resize: none"
                                      ng-model="newNprVm.newNpr.reasonForRequest"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" placeholder="{{'ENTER_NOTES' | translate}}"
                                      rows="3" style="resize: none"
                                      ng-model="newNprVm.newNpr.notes"></textarea>
                        </div>
                    </div>

                    <div class="form-group" style="margin-bottom: 65px">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newNprVm.newNpr.workflow" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{'SELECT' | translate}}">{{$select.selected.name}} [
                                    Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newNprVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
