<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <project-management-type-tree
                                                    on-select-type="newReqDocumentVm.onSelectType"
                                                    object-type="REQUIREMENTDOCUMENTTYPE"></project-management-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newReqDocumentVm.type.name" readonly>

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
                                            ng-click="newReqDocumentVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newReqDocumentVm.newReqDocument.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}"
                                   ng-model="newReqDocumentVm.newReqDocument.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newReqDocumentVm.newReqDocument.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DOCUMENT_OWNER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newReqDocumentVm.newReqDocument.personObject" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{'SELECT_PERSONS' | translate}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newReqDocumentVm.persons | filter: $select.search">
                                    <div>{{person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SELECT_TEMPLATE</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newReqDocumentVm.newReqDocument.template" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectTemplate}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="template.id as template in newReqDocumentVm.templates | filter: $select.search">
                                    <div>{{template.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group" ng-if="newReqDocumentVm.newReqDocument.template != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_DOCUMENT_REVIEWER</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newReqDocumentVm.newReqDocument.documentReviewer">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newReqDocumentVm.newReqDocument.template != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_REQUIREMENT_REVIEWER</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newReqDocumentVm.newReqDocument.requirementReviewer">
                        </div>
                    </div>
                    


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newReqDocumentVm.newReqDocument.workflowDefinition"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowTitle}}>
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices style="height: 120px"
                                                   repeat="workflow.id as workflow in newReqDocumentVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>




                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newReqDocumentVm.reqDocAttributes.length > 0"
                                     attributes="newReqDocumentVm.reqDocAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newReqDocumentVm.attributes.length > 0"
                                     attributes="newReqDocumentVm.attributes"></attributes-view>
                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>