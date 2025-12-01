<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROJECT_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                     <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                      ng-disabled="editProjectVm.project.type.name">
                                        <span translate id="select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div
                                                style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <project-management-type-tree
                                                    on-select-type="editProjectVm.onSelectType"
                                                    object-type="PROJECT"></project-management-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="editProjectVm.project.type.name" readonly>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" ng-model="editProjectVm.project.name"
                                   ng-disabled="editProjectVm.projectData.percentComplete > 0">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                <textarea name="description" rows="3" class="form-control" style="resize: none"
                          ng-model="editProjectVm.project.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROJECT_MANAGER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="editProjectVm.project.projectManager" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select">{{$select.selected.firstName}}</ui-select-match>
                                <ui-select-choices
                                        repeat="person in editProjectVm.persons | filter: $select.firstName.search">
                                    <div ng-bind-html="trustAsHtml((person.firstName | highlight: $select.firstName.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANNED_START_DATE</span>
                            <span class="asterisk">*</span>:</label>

                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="text" id="plannedStartDate" class="form-control" placeholder="dd/mm/yyyy"
                                       ng-disabled="editProjectVm.projectData.percentComplete > 0"
                                       ng-model="editProjectVm.project.plannedStartDate" date-picker-edit>
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANNED_FINISH_DATE</span>
                            <span class="asterisk">*</span>:</label>

                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="text" id="plannedFinishDate" class="form-control" placeholder="dd/mm/yyyy"
                                       ng-disabled="editProjectVm.projectData.percentComplete > 0"
                                       ng-model="editProjectVm.project.plannedFinishDate" start-finish-date-picker>
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>Select Template</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="editProjectVm.selectedTemplate" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select template">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="template in editProjectVm.templates">
                                    <div ng-bind-html="trustAsHtml((template.name))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>--%>
                    <div ng-show="editProjectVm.creating" ng-cloak>
                        <span>Creating project...</span>

                        <div class="progress progress-striped active">
                            <div class="progress-bar"
                                 role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 100%">
                            </div>
                        </div>
                        <br>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

