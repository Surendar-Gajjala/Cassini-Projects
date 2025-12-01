<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MANPOWER_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="Select" translate>SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <manufacturing-type-tree
                                                    on-select-type="newManpowerVm.onSelectType"
                                                    object-type="MANPOWERTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newManpowerVm.selectedManpowerType.name" readonly>
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
                                            ng-click="newManpowerVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newManpowerVm.newManpower.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newManpowerVm.newManpower.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newManpowerVm.newManpower.description"></textarea>
                        </div>
                    </div>
                    <!-- <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NEW_PERSON</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isExistPerson" switch="none" checked=""
                                   ng-model="newManpowerVm.newManpower.newPerson"
                                   ng-change="newManpowerVm.onChangePerson()">
                            <label for="isExistPerson" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group" ng-if="!newManpowerVm.newManpower.newPerson">
                        <label class="col-sm-4 control-label">
                            <span translate>PERSON</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newManpowerVm.newManpower.person" theme="bootstrap"
                                       style="width:100%" on-select="newManpowerVm.newManpower.personDetails = $item">
                                <ui-select-match placeholder="{{selectPersons}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newManpowerVm.persons | filter: $select.search">
                                    <div>{{person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div> -->
                    <!-- <person-data person="newManpowerVm.newManpower.personDetails"
                                 disable="!newManpowerVm.newManpower.newPerson"></person-data> -->
                    <attributes-view show-objects="selectObjectValues" ng-if="newManpowerVm.attributes.length > 0"
                                     attributes="newManpowerVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
