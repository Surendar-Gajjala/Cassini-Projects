<style>
    #rightSidePanelContent {
        overflow: hidden !important;
    }
</style>
<div style="position: relative;">
    <div style="padding: 20px; height: 500px;">
        <div class="col-md-12" style="margin-top: -13px; padding: 10px;" ng-hide="projectPersonVm.hideRadioButton">
            <div class="col-md-6" style="text-align: right;font-size: 20px;">
                <input type="radio" ng-click="projectPersonVm.showNode()" name="node"
                       checked> Add Function
            </div>
            <div class="col-md-6" style="font-size: 20px;">
                <input type="radio" ng-click="projectPersonVm.showPerson()" name="node"> Add Person
            </div>
            <br>
            <hr>
        </div>
        <div class="row" style="margin-right: 48px;">
            <form class="form-horizontal">
                <div ng-if="projectPersonVm.person">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Person :<span class="asterisk">*</span> </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="projectPersonVm.personData.person" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Owner">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in projectPersonVm.persons | filter: $select.fullName.search">
                                    <div ng-bind-html="trustAsHtml((person.fullName | highlight: $select.fullName.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Person Role :<span class="asterisk">*</span> </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="projectPersonVm.personData.projectRole" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Owner">{{$select.selected.role}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="projectRole in projectPersonVm.projectRoles | filter: $select.search">
                                    <div ng-bind-html="trustAsHtml((projectRole.role | highlight: $select.role.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </div>

                <div ng-if="projectPersonVm.node">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>Function Name</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="projectPersonVm.personData.nodeName">
                        </div>
                    </div>

                </div>

            </form>
            <br>
        </div>
    </div>
</div>

