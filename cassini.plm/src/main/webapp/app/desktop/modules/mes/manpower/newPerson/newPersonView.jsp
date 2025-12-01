<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group" ng-if="newPersonVm.contactMode == 'NEW'">
                        <label class="col-sm-4 control-label">
                            <span translate>NEW_PERSON</span> : </label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isNewPerson" switch="none" checked=""
                                   ng-model="newPersonVm.newManpower.newPerson"
                                   ng-change="newPersonVm.onChangePerson()">
                            <label for="isNewPerson" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="!newPersonVm.newManpower.newPerson && newPersonVm.contactMode == 'NEW'">
                        <label class="col-sm-4 control-label">
                            <span translate>PERSON</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newPersonVm.newManpower.contact" theme="bootstrap"
                                       style="width:100%" on-select="newPersonVm.newManpower.person = $item">
                                <ui-select-match placeholder="Contact Person">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newPersonVm.persons track by person.id">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <person-data person="newPersonVm.newManpower.person"
                                 disable="!newPersonVm.newManpower.newPerson && newPersonVm.contactMode == 'NEW'"></person-data>

                    <div class="form-group" ng-if="newPersonVm.contactMode == 'EDIT'">
                        <label class="col-sm-4 control-label">
                            <span translate>ACTIVE</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isActive" switch="none" checked=""
                                   ng-model="newPersonVm.newManpower.active">
                            <label for="isActive" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>

                    

                </form>

            </div>
            <br><br>
        </div>
    </div>
</div>

