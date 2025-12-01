<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANT_TYPE_TITLE</span>
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
                                            <manufacturing-type-tree
                                                    on-select-type="newPlantVm.onSelectType"
                                                    object-type="PLANTTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newPlantVm.selectedPlantType.name" readonly>

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
                                            ng-click="newPlantVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newPlantVm.newPlant.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}" ng-model="newPlantVm.newPlant.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newPlantVm.newPlant.description"></textarea>
                        </div>
                    </div>


                    <div class=" form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ADDRESS</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_ADDRESS' | translate}}"
                                      ng-model="newPlantVm.newPlant.address"></textarea>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>CITY</span>
                            : </label>

                        <div class="col-sm-7">
                            <input type="text" placeholder="{{'ENTER_CITY' | translate}}" class="form-control"
                                   name="title" ng-model="newPlantVm.newPlant.city">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>COUNTRY</span>
                            : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_COUNTRY' | translate}}"
                                   name="title" ng-model="newPlantVm.newPlant.country">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>POSTAL_CODE</span>
                            : </label>

                        <div class="col-sm-7">
                            <input type="text" placeholder="{{'ENTER_POSTAL_CODE' | translate}}" class="form-control"
                                   name="title"
                                   ng-model="newPlantVm.newPlant.postalCode">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_PHONE_NUMBER' | translate}}"
                                   ng-model="newPlantVm.newPlant.phoneNumber"
                                    >
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MOBILE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_MOBILE_NUMBER' | translate}}"
                                   ng-model="newPlantVm.newPlant.mobileNumber"
                                    >
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>FAX_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_FAX_NUMBER' | translate}}"
                                   ng-model="newPlantVm.newPlant.faxAddress"
                                    >
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EMAIL</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_EMAIL' | translate}}"
                                   ng-model="newPlantVm.newPlant.email">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANT_MANAGER</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newPlantVm.newPlant.plantPerson" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{'ENTER_PLANT_MANAGER' | translate}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newPlantVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea placeholder="{{'ENTER_NOTES' | translate}}" class="form-control" rows="3"
                                      style="resize: none"
                                      ng-model="newPlantVm.newPlant.notes"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>REQUIRESMAINTENANCE</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="requiresMaintenance" switch="none" checked=""
                                   ng-model="newPlantVm.newPlant.requiresMaintenance">
                            <label for="requiresMaintenance" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>

                    <div>
                        <mes-asset asset="newPlantVm.newAsset"
                                   ng-show="newPlantVm.newPlant.requiresMaintenance"></mes-asset>
                    </div>

                    <attributes-view show-objects="selectObjectValues" ng-if="newPlantVm.attributes.length > 0"
                                     attributes="newPlantVm.attributes"></attributes-view>
                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>
