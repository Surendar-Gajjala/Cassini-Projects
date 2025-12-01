<div class="view-container">
    <div style="padding: 20px; max-height: 500px;">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label"><span translate>ECO_NUMBER</span> : </label>

                <div class="col-sm-9">
                    <input type="text" class="form-control" name="ecoNumber"
                           placeholder="{{ecoSearchDialogueVm.numberTitle}}"
                           ng-model="ecoSearchDialogueVm.filters.ecoNumber"
                           ng-enter="ecoSearchDialogueVm.search()" style="width: 270px;">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span translate>TITLE</span> : </label>

                <div class="col-sm-9">
                    <input type="text" class="form-control" name="title" placeholder="{{ecoSearchDialogueVm.title}}"
                           ng-model="ecoSearchDialogueVm.filters.title"
                           ng-enter="ecoSearchDialogueVm.search()" style="width: 270px;">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span translate>DESCRIPTION</span> : </label>

                <div class="col-sm-9">
                    <input type="text" class="form-control" name="description"
                           placeholder="{{ecoSearchDialogueVm.descriptionTitle}}"
                           ng-model="ecoSearchDialogueVm.filters.description"
                           ng-enter="ecoSearchDialogueVm.search()" style="width: 270px;">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><span translate>STATUS</span> :</label>

                <div class="col-sm-9">
                    <ui-select ng-model="ecoSearchDialogueVm.filters.statusType" theme="bootstrap" style="width:270px;">
                        <ui-select-match placeholder="{{ecoSearchDialogueVm.selectTitle}}">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="status in ['NORMAL', 'RELEASED', 'REJECTED'] | filter: $select.search">
                            <div ng-bind="status"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span translate>OWNER</span> : </label>

                <div class="col-sm-9">
                    <ui-select ng-model="ecoSearchDialogueVm.filters.ecoOwnerObject" theme="bootstrap"
                               style="width:270px;">
                        <ui-select-match placeholder="{{ecoSearchDialogueVm.selectTitle1}}">
                            {{$select.selected.firstName}}
                        </ui-select-match>
                        <ui-select-choices repeat="person in ecoSearchDialogueVm.persons | filter: $select.search">
                            <div ng-bind="person.firstName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>
    </div>
    <br>
    <br>
</div>
<%--<div id="appSidePanelButtonsPanel" class='buttons-panel' style="display: none">
    <button class="btn btn-sm btn-success pull-right"
            ng-click="ecoSearchDialogueVm.search()">Search
    </button>--%>
</div>
