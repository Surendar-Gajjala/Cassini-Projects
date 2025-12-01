<div style="position: relative;">
    <div class="row">
        <form class="form-horizontal">
            <div class="col-sm-12">
                <div class="col-md-4" style="text-align: right;margin-top: 5px;font-size: 18px;">
                    {{newGlossaryVm.supportedLanguages}} :
                </div>
                <div class="col-md-8">
                    <table class="table table-striped" style="box-shadow: none !important;">
                        <tbody>
                        <tr ng-repeat="language in newGlossaryVm.languages">
                            <td style="background-color: white;border: none;width: 25px;">
                                <input type="checkbox" name="licence" ng-model="language.selected"
                                       title="{{newGlossaryVm.selectLanguage}}"
                                       ng-disabled="(language.defaultLanguage && language.selected) || language.disableLanguage || language.editMode"
                                       style="height:15px; width:15px; vertical-align: middle;margin: 0px;"
                                       ng-click="newGlossaryVm.selectLanguageType(language)"/>
                            </td>
                            <td style="text-align: left;background-color: white;border: none;">
                                <span ng-if="!language.editMode">{{language.language}}</span>
                                <span ng-if="language.defaultLanguage && !language.editMode">({{newGlossaryVm.defaultTitle}})</span>
                                <span ng-if="!language.defaultLanguage && !language.editMode">
                                    <i class="fa fa-bookmark" title="{{'SET_DEFAULT_LANGUAGE' | translate}}"
                                       style="font-size: 16px;cursor: pointer;color: green;"
                                       ng-click="newGlossaryVm.setDefaultLanguage(language)"></i>
                                </span>
                                <span ng-if="newGlossaryVm.mode == 'NEW'">
                                    <i class="fa fa-pencil-square-o"
                                       ng-if="!language.editMode && !language.defaultLanguage"
                                       ng-click="newGlossaryVm.editLanguage(language)"
                                       style="cursor: pointer;margin: 0 2px;"
                                       title="{{'CLICK_TO_EDIT' | translate}}"></i>
                                    <%--<i class="fa fa-trash-o" ng-if="!language.editMode && !language.defaultLanguage"
                                       ng-click="newGlossaryVm.deleteLanguage(language)"
                                       style="cursor: pointer;font-size: 15px;" title="Click to delete"></i>--%>
                                </span>
                                <span ng-if="language.editMode" class="input-group mb15"
                                      style="width: 250px;margin-bottom: 0px;">
                                    <input type="text" class="form-control"
                                           placeholder="{{'ENTER_LANGUAGES' | translate}}"
                                           ng-model="language.language"
                                           style="width: 50%;padding:3px !important;margin-top: 4px;"/>
                                    <i class="btn btn-sm fa fa-check-square" title="{{'SAVE' | translate}}"
                                       style="font-size: 20px;cursor: pointer;padding-right: 2px;"
                                       ng-click="newGlossaryVm.updateLanguage(language)"></i>
                                    <i class="btn btn-sm fa fa-times-circle"
                                       style="font-size: 20px;cursor: pointer;padding-left: 2px;"
                                       title="{{'CANCEL' | translate}}"
                                       ng-click="newGlossaryVm.cancelLanguageChanges(language)"></i>
                                </span>

                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: left;background-color: white;border: none;">
                                <i class="la la-plus" style="font-size: 20px;cursor: pointer;"
                                   title="{{newGlossaryVm.createNewLanguage}}"
                                   ng-if="newGlossaryVm.newLanguage == false"
                                   ng-click="newGlossaryVm.createNew()"></i>
                            </td>
                            <td style="text-align: left;background-color: white;border: none;">
                            <span>
                                <span ng-if="newGlossaryVm.newLanguage == true" class="input-group mb15"
                                      style="width: 250px;margin-bottom: 0px;">
                                    <input type="text" class="form-control"
                                           placeholder="{{'ENTER_LANGUAGES' | translate}}"
                                           ng-model="newGlossaryVm.language.language"
                                           style="width: 50%;padding:3px !important;margin-top: 4px;"/>
                                    <i class="btn btn-sm fa fa-check-circle" title="{{'SAVE' | translate}}"
                                       style="font-size: 20px;cursor: pointer;padding-right: 2px;"
                                       ng-click="newGlossaryVm.createLanguage()"></i>
                                    <i class="btn btn-sm fa fa-times-circle"
                                       style="font-size: 20px;cursor: pointer;padding-left: 2px;"
                                       title="{{'CANCEL' | translate}}"
                                       ng-click="newGlossaryVm.cancelChanges()"></i>
                                </span>
                            </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-md-12" ng-if="newGlossaryVm.mode == 'NEW'">
                <h5><span>{{newGlossaryVm.note}} : </span>
                    <span style="color: red">{{newGlossaryVm.byDefault}} {{defaultLanguageName}} {{newGlossaryVm.defaultLanguageTitle}}
                        {{newGlossaryVm.clickOn}} <i class="fa fa-bookmark" style="font-size: 16px;color: green;"></i>
                        {{newGlossaryVm.toChange}}</span>
                </h5>
            </div>
            <div id="glossaryLanguagesList" class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset>
                        <uib-tab ng-repeat="glossaryLanguage in newGlossaryVm.glossaryLanguages"
                                 heading="{{glossaryLanguage.languageName}}">
                            <div ng-show="glossaryLanguage.languageName != 'Attributes'">
                                <form class="form-horizontal">
                                    <div>
                                        <div class="form-group" style="margin: 0px;">
                                            <label class="col-sm-3 control-label" style="margin: 0px;text-align: left;">
                                                <span translate>NAME</span>
                                                <span class="asterisk">*</span> : </label>
                                        </div>
                                        <div class="form-group" style="margin: 0px;">
                                            <div class="col-sm-12">
                                                <input type="text" class="form-control" name="title" placeholder="{{'ENTER_TERMINOLOGY_NAME' | translate}}"
                                                       ng-model="glossaryLanguage.name" autofocus/>
                                            </div>
                                        </div>

                                        <div class="form-group" style="margin: 0px;">
                                            <label class="col-sm-3 control-label" style="margin: 0px;text-align: left;">
                                                <span translate>DESCRIPTION</span>: </label>
                                        </div>
                                        <div class="form-group" style="margin: 0px;">
                                            <div class="col-sm-12">
                                                <textarea name="description" rows="3" class="form-control"
                                                          style="resize: none" placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                                          ng-model="glossaryLanguage.description">
                                                 </textarea>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div ng-show="glossaryLanguage.languageName == 'Attributes'">
                                <div ng-if='newGlossaryVm.mode == "NEW"'>
                                    <attributes-view show-objects="selectObjectValues"
                                                     attributes="newGlossaryVm.glossaryRequiredAttributes"></attributes-view>
                                    <br>
                                    <attributes-view show-objects="selectObjectValues"
                                                     attributes="newGlossaryVm.glossaryAttributes"></attributes-view>
                                </div>
                                <div ng-if='newGlossaryVm.mode == "EDIT"'>
                                    <%@include file="attributeDetailsView.jsp" %>
                                </div>
                            </div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </form>
    </div>
</div>

