<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group" ng-if="actionType == 'home'">
                        <label class="col-sm-4 control-label">
                            <span translate>TYPE</span>: </label>

                        <div class="col-sm-7">

                            <div class="switch-toggle switch-candy">
                                <input id="prTypeC" name="view" type="radio" checked
                                       ng-click="selectJigFixtureType('JIG', $event)">
                                <label for="prTypeC" onclick="" translate>JIG</label>

                                <input id="prTypeI" name="view" type="radio"
                                       ng-click="selectJigFixtureType('FIXTURE', $event)">
                                <label for="prTypeI" onclick="" translate="">FIXTURE</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>JIGS_TYPE_TITLE</span>
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
                                                    on-select-type="newJigsAndFixturesVm.onSelectType"
                                                    object-type="JIFSFIXTURETYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newJigsAndFixturesVm.type.name" readonly>

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
                                            ng-click="newJigsAndFixturesVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newJigsAndFixturesVm.newJigAndFix.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newJigsAndFixturesVm.newJigAndFix.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newJigsAndFixturesVm.newJigAndFix.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>IMAGE_TITLE</span> :</label>

                        <div class="col-sm-7">
                            <input id="imageId" type="file" class="form-control" accept="image/*"
                                   ng-file-model="newJigsAndFixturesVm.newJigAndFix.imageFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUIRESMAINTENANCE</span>:</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="requiresMaintenance" switch="none" checked=""
                                   ng-model="newJigsAndFixturesVm.newJigAndFix.requiresMaintenance">
                            <label for="requiresMaintenance" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>


                </form>
                <div ng-show="newJigsAndFixturesVm.newJigAndFix.requiresMaintenance">
                    <mes-asset asset="newJigsAndFixturesVm.newAsset"></mes-asset>
                </div>
                <mes-mfr-data update-type="'creation'" has-permission="true"
                              manufacturer-data="newJigsAndFixturesVm.newJigAndFix.manufacturerData"></mes-mfr-data>

                <attributes-view show-objects="selectObjectValues"
                                 ng-if="newJigsAndFixturesVm.attributes.length > 0"
                                 attributes="newJigsAndFixturesVm.attributes"></attributes-view>
            </div>
        </div>
    </div>
</div>
