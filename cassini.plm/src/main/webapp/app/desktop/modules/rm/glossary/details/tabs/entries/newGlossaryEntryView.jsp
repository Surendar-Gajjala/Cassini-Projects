<div style="position: relative;">
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset>
                        <uib-tab ng-repeat="glossaryEntryDetail in newGlossaryEntryVm.glossaryEntryDetails"
                                 heading="{{glossaryEntryDetail.languageName}}">
                            <div ng-if="glossaryEntryDetail.languageName != 'Attributes'">
                                <form class="form-horizontal">
                                    <div>
                                        <div class="form-group" style="margin: 0px;">
                                            <label class="col-sm-3 control-label" style="text-align: left;margin: 0px;">
                                                <span translate>NAME</span>
                                                <span ng-if="glossaryEntryDetail.language.defaultLanguage"
                                                      class="asterisk">*</span> : </label>
                                        </div>
                                        <div class="form-group" style="margin: 0px;">
                                            <div class="col-sm-12">
                                                <input type="text" class="form-control" name="title"
                                                       ng-model="glossaryEntryDetail.name" autofocus>
                                            </div>
                                        </div>
                                        <div class="form-group" style="margin: 0px;">
                                            <label class="col-sm-3 control-label" style="margin: 0px;text-align: left;">
                                                <span translate>DESCRIPTION</span>
                                                <span ng-if="glossaryEntryDetail.language.defaultLanguage"
                                                      class="asterisk">*</span> : </label>
                                        </div>
                                        <div class="form-group" style="margin: 0px;">
                                            <div class="col-sm-12">
                                                <textarea name="description" rows="7" class="form-control"
                                                          style="resize: none"
                                                          ng-model="glossaryEntryDetail.description">
                                                </textarea>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div ng-if="glossaryEntryDetail.languageName == 'Attributes'">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newGlossaryEntryVm.entryRequiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newGlossaryEntryVm.entryAttributes"></attributes-view>
                            </div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

