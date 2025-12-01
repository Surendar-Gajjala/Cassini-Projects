<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group" ng-if="newContactVm.contactMode == 'NEW'">
                        <label class="col-sm-4 control-label">
                            <span translate>NEW_PERSON</span> : </label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isNewPerson" switch="none" checked=""
                                   ng-model="newContactVm.newContact.newPerson"
                                   ng-change="newContactVm.onChangePerson()">
                            <label for="isNewPerson" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="!newContactVm.newContact.newPerson && newContactVm.contactMode == 'NEW'">
                        <label class="col-sm-4 control-label">
                            <span translate>PERSON</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newContactVm.newContact.contact" theme="bootstrap"
                                       style="width:100%" on-select="newContactVm.newContact.person = $item">
                                <ui-select-match placeholder="Contact Person">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newContactVm.persons track by person.id">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <person-data person="newContactVm.newContact.person"
                                 disable="!newContactVm.newContact.newPerson && newContactVm.contactMode == 'NEW'"></person-data>

                    <div class="form-group" ng-if="newContactVm.contactMode == 'EDIT'">
                        <label class="col-sm-4 control-label">
                            <span translate>ACTIVE</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isActive" switch="none" checked=""
                                   ng-model="newContactVm.newContact.active">
                            <label for="isActive" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ROLE</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   placeholder="{{'ENTER_ROLE' | translate }}"
                                   ng-model="newContactVm.newContact.role">
                        </div>
                    </div>

                </form>

            </div>
            <br><br>
        </div>
    </div>
</div>

