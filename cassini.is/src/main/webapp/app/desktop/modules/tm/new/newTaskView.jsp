<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name :<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title" ng-model="newTaskVm.newTask.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description :</label>

                        <div class="col-sm-8">
                            <textarea id="meetingDescription" name="description" rows="2" class="form-control"
                                      style="resize: none"
                                      ng-model="newTaskVm.newTask.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Site:<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <ui-select class="required-field" ng-model="newTaskVm.newTask.siteObject"
                                       on-select="newTaskVm.changeMode()"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Site">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices
                                        repeat="site in newTaskVm.sites | filter: $select.search | orderBy: 'name'">
                                    <div ng-bind="site.name | highlight: $select.name.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div ng-if="newTaskVm.mode == 'NEW'">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">Site Name <span class="asterisk">*</span>: </label>

                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="type" placeholder="Enter Site Name"
                                       ng-model="newTaskVm.site.name">
                            </div>
                        </div>

                        <div class="form-group" style="margin-bottom: 15px;">
                            <label class="col-sm-4 control-label">Site Description : </label>

                            <div class="col-sm-8">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  placeholder="Enter Site Description"
                                  ng-model="newTaskVm.site.description"></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">WBS :<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <div class="input-group">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                            ng-click="$event.stopPropagation();">
                                        Select WBS<span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <wbs-tree on-select-wbs="newTaskVm.onSelectWbs"></wbs-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newTaskVm.wbs.name" readonly/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Workflow</span>
                            <%--<span class="asterisk">*</span>--%> : </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="newTaskVm.newTask.workflowDefinition"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="selectWorkflow">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices repeat="workflow in newTaskVm.workflows | filter: $select.search">
                                    <div ng-bind="workflow.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" style="margin-bottom: 15px;">
                        <label class="col-sm-4 control-label">Sub Contract : </label>

                        <div class="col-sm-8">
                            <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   style="margin-top: 15px;"
                                   ng-model="newTaskVm.newTask.subContract">
                        </div>
                    </div>

                    <div class="form-group" ng-if="!newTaskVm.newTask.subContract">
                        <label class="col-sm-4 control-label">Assigned To :</label>

                        <div class="col-sm-8">
                            <ui-select class="required-field" ng-model="newTaskVm.newTask.personObject"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Person">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newTaskVm.persons | filter: $select.search | orderBy : 'fullName'">
                                    <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Unit of Work :<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newTaskVm.newTask.unitOfWork">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Total Units :<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <input type="number" class="form-control" name="title"
                                   ng-model="newTaskVm.newTask.tempTotalUnits" placeholder="Total units">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Inspection Person :</label>

                        <div class="col-sm-8">
                            <ui-select class="required-field" ng-model="newTaskVm.newTask.inspectionPerson"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Person">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newTaskVm.persons | filter: $select.search | orderBy: 'fullName'">
                                    <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Planned Start Date :<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <input id="plannedStartDate" type="text" class="form-control" start-date-picker
                                   ng-model="newTaskVm.newTask.plannedStartDate" placeholder="dd/mm/yyyy">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Planned Finish Date :<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <input id="plannedFinishDate" type="text" class="form-control" start-finish-date-picker
                                   ng-model="newTaskVm.newTask.plannedFinishDate" placeholder="dd/mm/yyyy">
                        </div>
                    </div>

                    <h4 ng-if="newTaskVm.newTaskAttributes.length > 0" class="section-title" style="color: black;">
                        Attributes</h4>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newTaskVm.newTaskAttributes"></attributes-view>
                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>
