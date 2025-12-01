<div>
    <style>

        .tab-content {
            padding: 10px !important;
        }
    </style>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset>
                        <uib-tab ng-repeat="glossaryEntryDetail in entrySelectionVm.glossaryEntryDetails"
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
                                                       ng-model="glossaryEntryDetail.name">
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
                                                 attributes="entrySelectionVm.entryRequiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="entrySelectionVm.entryAttributes"></attributes-view>
                            </div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

<%--
<div style="position: relative">

    &lt;%&ndash;<div>
        <div class="col-sm-6" style="text-align: center;font-size: 20px;">
            <input type="radio" ng-click="entrySelectionVm.showEntries()" name="entries" checked> All Entries
        </div>
        <div class="col-sm-6" style="font-size: 20px;" ng-if=" hasPermission('entry','create')">
            <input type="radio" ng-click="entrySelectionVm.showNewEntry()" name="entries">New Entry
        </div>
    </div>
    <hr>
    <hr>&ndash;%&gt;
    <div ng-if="entrySelectionVm.newEntry == true">
        <div style="overflow-y: auto; overflow-x: hidden;padding: 20px;">
            <div class="row">
                <form class="form-horizontal">
                    <div ng-repeat="glossaryEntryDetail in entrySelectionVm.glossaryEntryDetails">
                        <h3 style="margin: 0px;">{{glossaryEntryDetail.language.language}}</h3>

                        <div class="form-group" style="margin: 0px;">
                            <label class="col-sm-3 control-label" style="text-align: left;margin: 0px;">
                                <span translate>NAME</span>
                                <span class="asterisk">*</span> : </label>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <div class="col-sm-12">
                                <input type="text" class="form-control" name="title"
                                       ng-model="glossaryEntryDetail.name">
                            </div>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <label class="col-sm-3 control-label" style="margin: 0px;text-align: left;">
                                <span translate>DESCRIPTION</span>
                                <span class="asterisk">*</span> : </label>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <div class="col-sm-12">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="glossaryEntryDetail.description">
                            </textarea>
                            </div>
                        </div>
                        <hr style="border-color: lightgrey !important;">
                    </div>
                    <hr>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="entrySelectionVm.entryRequiredAttributes"></attributes-view>
                    <br>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="entrySelectionVm.entryAttributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
    <div ng-if="entrySelectionVm.allEntries == true" class="col-md-12"
         style="padding:0px; height: auto;overflow: auto;">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 80px; text-align: center">
                    <input name="itemSelected" ng-value="true" type="checkbox"
                           ng-model="entrySelectionVm.selectAllCheck"
                           ng-click="entrySelectionVm.selectAll()">
                </th>
                <th translate>NAME</th>
                <th translate>DESCRIPTION</th>
                <th translate>VERSION</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="entrySelectionVm.glossaryEntries.content.length == 0">
                <td colspan="15" translate>NO_ENTRIES</td>
            </tr>
            <tr ng-if="entrySelectionVm.glossaryEntries.content.length > 0"
                ng-repeat="entry in entrySelectionVm.glossaryEntries.content">
                <th style="width: 80px; text-align: center">
                    <input name="entry" type="checkbox" ng-model="entry.selected"
                           ng-click="entrySelectionVm.selectCheck(entry)">
                </th>

                <td style="vertical-align: middle;">
                    {{entry.defaultDetail.name}}
                </td>
                <td style="vertical-align: middle;">
                    {{entry.defaultDetail.description}}
                </td>
                <td style="vertical-align: middle;">
                    {{entry.version}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<br>
<br>
</div>
--%>
