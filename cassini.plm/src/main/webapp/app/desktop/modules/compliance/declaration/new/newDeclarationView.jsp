<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DECLARATION_TYPE</span>
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
                                            <compliance-type-tree
                                                    on-select-type="newDeclarationVm.onSelectType"
                                                    object-type="PGCDECLARATIONTYPE"></compliance-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newDeclarationVm.selectedDeclarationType.name" readonly>

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
                                            ng-click="newDeclarationVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newDeclarationVm.newDeclaration.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newDeclarationVm.newDeclaration.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newDeclarationVm.newDeclaration.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SUPPLIER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDeclarationVm.newDeclaration.supplier"
                                       on-select="newDeclarationVm.loadContacts($item.id)"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectSupplier}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="supplier.id as supplier in newDeclarationVm.suppliers | filter: $select.search">
                                    <div>{{supplier.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CONTACT</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDeclarationVm.newDeclaration.contact" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectContact}}">
                                    {{$select.selected.person.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="contact.id as contact in newDeclarationVm.contacts | filter: $select.search">
                                    <div>{{contact.person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newDeclarationVm.customAttributes.length > 0"
                                     attributes="newDeclarationVm.customAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newDeclarationVm.attributes.length > 0"
                                     attributes="newDeclarationVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
