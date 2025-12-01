<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="newProjectVm.project.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description :</label>

                        <div class="col-sm-7">
                <textarea name="description" rows="5" class="form-control" style="resize: none"
                          ng-model="newProjectVm.project.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Portfolio :<span class="asterisk">*</span> </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectVm.project.portfolioObject" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Portfolio">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="portfolio in newProjectVm.portfolios | filter: $select.name.search |orderBy: 'name'">
                                    <div ng-bind-html="trustAsHtml((portfolio.name | highlight: $select.name.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">Project Owner :<span class="asterisk">*</span> </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectVm.project.owner" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Owner">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newProjectVm.persons | filter: $select.fullName.search |orderBy: 'fullName'">
                                    <div ng-bind-html="trustAsHtml((person.fullName | highlight: $select.fullName.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Planned Start Date:<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedStartDate" class="form-control" placeholder="dd/mm/yyyy"
                                   ng-model="newProjectVm.project.plannedStartDate" start-finish-date-picker>
                        </div>


                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Planned Finish Date:<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedFinishDate" class="form-control" placeholder="dd/mm/yyyy"
                                   ng-model="newProjectVm.project.plannedFinishDate" start-finish-date-picker>
                        </div>
                    </div>
                    <br>
                    <h4 ng-if="newProjectVm.newProjectAttributes.length > 0" class="section-title"
                        style="color: black;">Attributes</h4>
                    <br>

                    <attributes-view show-objects="selectObjectValues" class="control-label"
                                     attributes="newProjectVm.newProjectAttributes"></attributes-view>
                    <br>
                    <hr>
                    <h5>Mail Server Settings</h5>
                    <hr>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="bottom: 18px;">Mail Server :</label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectVm.objectMailSettings.mailServerObject" theme="bootstrap"
                                       style="width:100%; bottom: 15px"
                                       ng-change="newProjectVm.objectMailSettingsChanged(newProjectVm.objectMailSettings.mailServerObject)">
                                <ui-select-match placeholder="Select MailServer">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="mailServer in newProjectVm.mailServers | filter: $select.name.search |orderBy: 'name'">
                                    <div ng-bind-html="trustAsHtml((mailServer.name | highlight: $select.name.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div ng-if="newProjectVm.objectMailSettings.mailServerObject != null">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">Receiver User :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="receiverUser"
                                       ng-model="newProjectVm.objectMailSettings.receiverUser">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Receiver Email :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="receiverEmail"
                                       ng-model="newProjectVm.objectMailSettings.receiverEmail">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">Receiver Password :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="password" class="form-control" name="receiverPassword"
                                       id="receiverPassword"
                                       ng-model="newProjectVm.objectMailSettings.receiverPassword">
                                <span id="showPassword" class="fa fa-fw fa-eye"
                                      ng-click="newProjectVm.showReceiverPassword()"
                                      style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;
                              font-size: 18px;margin-right: 5px;color: black;"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Sender User :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="senderUser"
                                       ng-model="newProjectVm.objectMailSettings.senderUser">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label" for="mail">Sender Email :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="senderEmail" id="mail"
                                       ng-model="newProjectVm.objectMailSettings.senderEmail">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">Sender Password :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="password" class="form-control" name="senderPassword" id="senderPassword"
                                       ng-model="newProjectVm.objectMailSettings.senderPassword">
                                <span id="showPassword1" class="fa fa-fw fa-eye"
                                      ng-click="newProjectVm.showSenderPassword()"
                                      style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;
                              font-size: 18px;margin-right: 5px;color: black;"></span>
                            </div>
                        </div>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

