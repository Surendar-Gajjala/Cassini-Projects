<div style="position: relative;" class="body">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
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
                            <span translate>SPECIFICATION_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="padding-left: 5px;padding-right: 0;">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <specification-tree
                                                    on-select-type="newSpecificationVm.onSelectType"></specification-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newSpecificationVm.newSpecification.type.name" readonly>

                            </div>
                        </div>
                        <div class="form-group">

                            <label class="col-sm-4 control-label">
                                <span translate>NUMBER</span>
                                <span class="asterisk">*</span> :
                            </label>

                            <div class="col-sm-7">
                                <div class="input-group">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newSpecificationVm.autoNumber()" translate>AUTO
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newSpecificationVm.newSpecification.objectNumber">
                                </div>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>NAME</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       placeholder="{{'ENTER_SPECIFICATION_NAME' | translate}}"
                                       ng-model="newSpecificationVm.newSpecification.name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>DESCRIPTION</span>: </label>

                            <div class="col-sm-7">
                                      <textarea class="form-control" rows="3" style="resize: none"
                                                placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                                ng-model="newSpecificationVm.newSpecification.description"></textarea>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>WORKFLOW</span>: </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="newSpecificationVm.newSpecification.workflowDefinition"
                                           theme="bootstrap"
                                           style="width:100%">
                                    <ui-select-match placeholder=Select>{{$select.selected.name}} [ Revision :
                                        {{$select.selected.revision}} ]
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="workflow in newSpecificationVm.workflows | filter: $select.search">
                                        <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newSpecificationVm.specRequiredProperties"></attributes-view>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newSpecificationVm.specProperties"></attributes-view>

                        <attributes-view show-objects="selectObjectValues"
                                         ng-if="newSpecificationVm.newSpecification.type != null"
                                         attributes="newSpecificationVm.requiredAttributes"></attributes-view>
                        <attributes-view show-objects="selectObjectValues"
                                         ng-if="newSpecificationVm.newSpecification.type != null"
                                         attributes="newSpecificationVm.attributes"></attributes-view>
                        <br>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
