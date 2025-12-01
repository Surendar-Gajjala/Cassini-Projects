<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Manpower Type :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">

                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        Select <span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 380px;height: 200px;">
                                            <manpower-tree
                                                    on-select-type="newManpowerVm.onSelectType"></manpower-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newManpowerVm.newManpower.itemType.name"
                                       readonly>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">ManPowerNumber : <span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">

                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newManpowerVm.autoNumber()">Auto
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newManpowerVm.newManpower.itemNumber">
                            </div>
                        </div>
                    </div>


                    <div class="form-group" style="text-align: center">
                        <label class="radio-inline" style="margin-left: 30px"><input type="radio" name="optradio"
                                                                                     id="radio1"
                                                                                     ng-click="newManpowerVm.existingPerson('EXIST')">Existing</label>
                        <label class="radio-inline" style="margin-left: 50px"><input type="radio" name="optradio"
                                                                                     id="radio2"
                                                                                     ng-click="newManpowerVm.newPerson('NEW')">New</label>

                    </div>
                    <div class="form-group" ng-show="newManpowerVm.mode == 'EXIST'">
                        <label class="col-sm-4 control-label">Person :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <ui-select class="required-field" ng-model="newManpowerVm.manpower"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Person">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newManpowerVm.persons | filter: $select.search |orderBy: 'fullName'">
                                    <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div ng-show="newManpowerVm.mode == 'NEW'">
                        <div class="form-group">

                            <label class="col-sm-4 control-label">First Name :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-model="newManpowerVm.newManpower.firstName">
                            </div>
                        </div>
                        <div class="form-group">

                            <label class="col-sm-4 control-label">Last Name :</label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-model="newManpowerVm.newManpower.lastName">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Phone :</label>

                            <%-- <div class="col-sm-7" ng-form name="mobile">
                                 <div class="input-group mb15" style="width: 100%;margin-bottom: 0px;">
                                     <input type="text"
                                            style="width: 20%;border-radius: 3px 0px 0px 3px;padding: 10px;"
                                            class="form-control" &lt;%&ndash;value="+91"&ndash;%&gt; readonly/>
                                     <input type="text" class="form-control" ng-pattern="/^\+?\d{10}$/" &lt;%&ndash;id="phoneNumber"&ndash;%&gt;
                                            style="width: 80%;"
                                            maxlength="10"
                                            ng-model="newManpowerVm.newManpower.phoneMobile"
                                            name="mobileNumber" />
                                 </div>
                             </div>--%>
                            <div class="col-sm-7">
                                <input type="text" class="form-control" maxlength="10" name="mobileNumber"
                                       ng-pattern="/^\+?\d{10}$/"
                                       ng-model="newManpowerVm.newManpower.phoneMobile">
                            </div>
                        </div>

                        <div class="form-group">

                            <label class="col-sm-4 control-label">Email :</label>

                            <div class="col-sm-7">
                                <input type="email" class="form-control" name="title"
                                       ng-model="newManpowerVm.newManpower.email">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">Photo :
                                <%--<span class="asterisk">*</span>--%>
                            </label>

                            <div class="col-sm-7">
                                <label class=""
                                       style="margin-bottom: 0px !important;padding-top: 5px;width: 100%;">
                                    Click to Upload Photo <br> (Limit size to 1MB in JPEG or JPG or PNG format)
                                    <input type="file" id="image1" value="file" accept="image/*"
                                           ng-model="newManpowerVm.personImage" style="display: none"/>

                                    <div ng-show="newManpowerVm.personImagePath == null">
                                        <img id="personImage" src="app/assets/images/user-placeholder.png"
                                             style="width: 100px;height: 100px;cursor: pointer"/>
                                    </div>

                                    <div ng-show="newManpowerVm.personImagePath != null">
                                        <img id="changeImage" ng-src="{{newManpowerVm.personImagePath}}"
                                             style="width: 100px;height: 100px;cursor: pointer"/>
                                    </div>
                                </label>
                            </div>
                        </div>


                    </div>
                    <br>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newManpowerVm.newManpower.description"></textarea>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newManpowerVm.newManpowerAttributes"></attributes-view>
                </form>

                <br>
                <h4 ng-if="newManpowerVm.requiredAttributes.length > 0 || newManpowerVm.attributes.length > 0"
                    class="section-title" style="color: black;">ATTRIBUTES
                </h4>
                <br>

                <div>
                    <form class="form-horizontal" ng-if="newManpowerVm.newManpower.itemType != null">
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newManpowerVm.requiredAttributes"></attributes-view>
                        <br>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newManpowerVm.attributes"></attributes-view>
                    </form>
                </div>
                <br><br>
            </div>
        </div>
    </div>
</div>
