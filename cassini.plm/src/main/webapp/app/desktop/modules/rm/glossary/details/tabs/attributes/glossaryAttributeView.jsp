<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }

    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage:hover,
    .img-model .closeimage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }

</style>

<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">
        <div ng-if="glossaryAttributesVm.loading == true" style="padding: 30px;">
            <div style="font-size: 15px;">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif"
                     class="mr5"><span translate>LOADING_ATTRIBUTES</span>
            </div>
        </div>
        <div ng-if="glossaryAttributesVm.glossaryProperties.length == 0">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Attribute.png" alt="" class="image">

                    <div class="message">{{ 'NO_ATTRIBUTES' | translate}} </div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
        </div>
        <div class="row" ng-if="glossaryAttributesVm.glossaryProperties.length != 0"
             ng-repeat="attribute in glossaryAttributesVm.glossaryProperties">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span ng-bind-html="attribute.attributeDef.name"></span>:
            </div>
            <div class="value col-xs-8 col-sm-9">

                <%----------------  String Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                    <a ng-if="attribute.editMode == false && attribute.value.stringValue != null" href=""
                       title="{{changeValue}}" ng-click="changeAttribute(attribute)"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'">
                        <span ng-bind-html="attribute.value.stringValue "
                              title="{{attribute.value.stringValue}}"></span>
                        {{attribute.value.stringValue.length > 20 ? '...' : ''}}
                        <i class="fa fa-pencil"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 7px"></i>
                    </a>
                    <a ng-if="attribute.editMode == false  && attribute.value.stringValue == null"
                       href="" ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 7px"></i>
                    </a>
                      <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                            ng-bind-html="attribute.value.stringValue"></span>
                </div>


                <%----------  longText Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'LONGTEXT'">
                    <a ng-if="attribute.editMode == false && attribute.value.longTextValue != null"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" title="{{changeValue}}"
                       ng-click="changeAttribute(attribute)">
                        <span ng-bind-html="attribute.value.longTextValue "
                              title="{{attribute.value.longTextValue}}"></span>
                        {{attribute.value.longTextValue.length > 20 ? '...' : ''}}
                        <i class="fa fa-pencil"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 7px"></i>
                    </a>
                    <a ng-if="attribute.editMode == false &&  attribute.value.longTextValue == null"
                       href="" ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 7px"></i> </a>

                    <div ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                         ng-bind-html="attribute.value.longTextValue">
                    </div>
                </div>

                <%--------------- RichTextValue --------------%>
                <div ng-if="attribute.attributeDef.dataType == 'RICHTEXT'">
                <span>
                   <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue == null"
                      href="" ng-click="glossaryAttributesVm.editRichText(attribute)">
                       <i class="fa fa-pencil" style="padding-left: 7px;"
                          title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                   </a>
                    <span ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null"
                          ng-bind-html="attribute.value.encodedRichTextValue">
                       </span>
                    <span>
                        <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null" href=""
                           ng-click="glossaryAttributesVm.editRichText(attribute)">
                            <i class="fa fa-pencil" style="padding-left: 7px;"
                               title="{{'CLICK_TO_EDIT_VALUE' | translate}}"></i>
                        </a>
                    </span>

                </span>

                    <div ng-show="attribute.editMode == true">
                        <summernote ng-model="attribute.value.richTextValue"></summernote>
                    </div>

                    <button ng-show="attribute.editMode == true" title="{{saveAttributeTitle}}"
                            ng-click="glossaryAttributesVm.saveRichText(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button ng-show="attribute.editMode == true" title="{{cancelChangesTitle}}"
                            ng-click="glossaryAttributesVm.cancelRichText(attribute)"><i class="fa fa-times"></i>
                    </button>


                </div>

                <%----------------  Integer Value  -------------------%>

                <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                    <a ng-if="attribute.editMode == false && hasPermission('glossary','edit') && attribute.value.integerValue != null"
                       href="" ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                        {{attribute.value.integerValue}}
                    <span><i
                            class="fa fa-pencil" style="padding-left: 7px;"
                            title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </span>
                    </a>
                    <a ng-if="attribute.editMode == false  && attribute.value.integerValue == null"
                       href="" title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                     <span> <i class="fa fa-pencil" style="padding-left: 7px;"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i></span>
                    </a>

                    <div ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'">
                        {{attribute.value.integerValue}}
                    </div>
                </div>


                <%----------------  HyperLink Attribute  -------------------%>


                <div id="intClick" ng-if="attribute.attributeDef.dataType == 'HYPERLINK'">
                    <a ng-if="attribute.editMode == false && hasPermission('glossary','edit')"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="showHyperLink(attribute.value.hyperLinkValue)">
                        {{attribute.value.hyperLinkValue}}
                    </a>

                    <a ng-if="attribute.editMode == false && hasPermission('glossary','edit')"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="changeAttribute(attribute)"
                       title="{{'CLICK_TO_SET_VALUE' | translate}}"><i class="fa fa-pencil"></i>
                    </a>
                    <span ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'">
                        {{attribute.value.hyperLinkValue}}
                    </span>
                </div>

                <%----------------  DOUBLE Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                    <a ng-if="attribute.editMode == false && hasPermission('glossary','edit') && attribute.value.doubleValue != null"
                       href="" title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                        {{attribute.value.doubleValue}}
                      <span>
                    <i class="fa fa-pencil" style="padding-left: 7px;"
                       title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </span>
                    </a>
                    <a ng-if="attribute.editMode == false && hasPermission('glossary','edit') && attribute.value.doubleValue == null"
                       href="" ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil" style="padding-left: 7px;"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                    </a>

                    <div ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'">
                        {{attribute.value.doubleValue}}
                    </div>


                </div>


                <%----------------  DATE Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'DATE' && attribute.editMode == false">
                <span>
                <a href=""
                   ng-if="attribute.editMode == false && hasPermission('glossary','edit') && attribute.value.dateValue != null"
                   title="{{changeDate}}" ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                   ng-click="changeAttribute(attribute)">
                    <span ng-if="currentLang == 'en'">{{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}</span>
                    <span ng-if="currentLang == 'de'">{{ (attribute.value.dateValuede | date:"dd.MM.yyyy")}}</span>
                 <span>
                    <i class="fa fa-pencil" style="padding-left: 7px;"
                       title="{{'CLICK_TO_SET_DATE' | translate}}"></i>
                        </span>
                </a>
                    </span>

                <span>
                <a ng-if="attribute.editMode == false &&  attribute.value.dateValue == null"
                   href="" ng-click="changeAttribute(attribute)">
                    <span>
                    <i class="fa fa-pencil" style="padding-left: 7px;"
                       title="{{'CLICK_TO_SET_DATE' | translate}}"></i>
                        </span>
                </a>
                </span>

                    <div ng-if="!hasPermission('glossary','edit')">
                        <span ng-if="currentLang == 'en'">{{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}</span>
                        <span ng-if="currentLang == 'de'">{{ (attribute.value.dateValuede | date:"dd.MM.yyyy")}}</span>
                    </div>
                </div>

                <%----------------  BOOLEAN Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                    <a href="" ng-if="attribute.editMode == false && hasPermission('glossary','edit')"
                       title="{{clickToChangeValue}}" ng-click="changeAttribute(attribute)">
                        {{attribute.value.booleanValue}}
                    </a>
                    <a href=""
                       ng-if="attribute.editMode == false && hasPermission('glossary','edit') && attribute.value.booleanValue == null"
                       title="{{clickToChangeValue}}" ng-click="changeAttribute(attribute)">
                     <span><a href=""><i style="padding-left: 7px" ng-click="changeAttribute(attribute)"
                                         ng-hide="attribute.editMode == true"
                                         title="{{'CLICK_TO_EDIT_VALUE' | translate}}"
                                         class="fa fa-pencil"></i></a></span>
                    </a>
                    <span ng-if="!hasPermission('glossary','edit')|| selectGlossaryLifeCyclePhase == 'Released'">{{attribute.value.booleanValue}}</span>
                </div>

                <%----------------  LIST Value  -------------------%>
                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false">
                    <%--    <span>
                             <a ng-if="hasPermission('glossary','edit')" title="{{clickToChangeValue}}"
                                editable-select="attribute.value.listValue"
                                href="" e-style="width:200px;color:#337ab7" editable-select="attribute.value.listValue"
                                e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                                onaftersave="glossaryAttributesVm.saveProperties(attribute)">
                                 <span ng-bind-html="attribute.value.listValue"></span>
                                 <i class="fa fa-pencil" style="padding-left: 7px;"
                                    title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                             </a>
                        </span>

                    <a ng-if="attribute.value.listValue != null && attribute.editMode == false"
                       ng-hide="attribute.editMode == true || attribute.value.deleteFlag == true"
                       title="{{'REMOVE_ATTRIBUTE_VALUE' | translate}}"
                       ng-click="glossaryAttributesVm.cancelListValue(attribute)">
                        <i class="fa fa-times"
                           style="margin-left: 5px;font-size: 14px;cursor:pointer;color:darkred"></i></a>

                    <span ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.listValue">
                    </span>--%>
                    <div style="display: flex">
                        <ui-select ng-show="attribute.editMode == true" ng-model="attribute.value.listValue"
                                   theme="bootstrap" style="width:30%">
                            <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                            <ui-select-choices
                                    repeat="value in attribute.attributeDef.lov.values">
                                <div ng-bind="value"></div>
                            </ui-select-choices>
                        </ui-select>

                    <span ng-show="attribute.editMode == false && attribute.value.listValue != null">
                                        {{attribute.value.listValue}}
                    </span>

                        <div ng-show="attribute.editMode == false">
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               ng-click="changeAttribute(attribute)"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>

                            <i ng-if="attribute.value.listValue != null && attribute.value.deleteFlag != true"
                               ng-hide="attribute.editMode == true"
                               title="{{'REMOVE_ATTRIBUTE_VALUE' | translate}}"
                               ng-click="glossaryAttributesVm.deleteAttribute(attribute)">
                                <i class="fa fa-times-circle"
                                   style="margin-left: 25px;font-size: 14px;cursor:pointer;"></i></i>
                        </div>

                    </div>
                    <div style="margin-left: 310px;margin-top: -20px">
                        <i class="fa fa-check-circle" style="padding: 4px;cursor: pointer"
                           ng-show="attribute.editMode == true"
                           title="{{saveAttributeTitle}}"
                           ng-click="glossaryAttributesVm.saveProperties(attribute)">
                        </i>
                        <i class="fa fa-times-circle" style="cursor: pointer"
                           ng-show="attribute.editMode == true"
                           title="{{cancelChangesTitle}}"
                           ng-click="glossaryAttributesVm.cancelListValue(attribute)">
                        </i>
                    </div>

              <span ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'"
                    ng-bind-html="attribute.value.listValue">
                    </span>
                </div>

                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true">
                    <div ng-if="attribute.value.listValue.length > 0" ng-hide="attribute.editMode == true"
                         ng-repeat="listValue in attribute.value.mlistValue">
                        <ul>
                            <li>{{listValue}}</li>
                        </ul>
                    </div>
                    <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length == 0"
                       href="" ng-click="glossaryAttributesVm.editListValue(attribute)">
                        <i title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           class="fa fa-pencil"></i>
                    </a>

                    <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length > 0"
                       href="" ng-click="glossaryAttributesVm.editListValue(attribute)">
                        <i title="{{'CLICK_TO_EDIT_VALUE' | translate}}"
                           class="fa fa-pencil"></i>
                    </a>


                    <div ng-if="attribute.editMode" style="width: 400px; display: inline-flex">
                        <div style="width: 350px">
                            <ui-select multiple ng-model="attribute.value.mlistValue" theme="bootstrap"
                                       close-on-select="false" title="Choose a List" remove-selected="true">
                                <ui-select-match placeholder="Select list...">{{$item}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in attribute.attributeDef.lov.values track by value">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                        <div style="width: 25px">
                            <i class="fa fa-check-circle"
                               ng-if="attribute.attributeDef.dataType == 'LIST'"
                               ng-click="glossaryAttributesVm.saveProperties(attribute)"
                               title="{{saveAttributeTitle}}"
                               style="padding: 8px;cursor:pointer;margin-left: -4px;"></i>
                        </div>
                        <div style="width: 25px;">
                            <i class="fa fa-times-circle"
                               ng-if="attribute.attributeDef.dataType == 'LIST'"
                               ng-click="glossaryAttributesVm.cancelListValue(attribute)" title="{{cancelChangesTitle}}"
                               style="padding: 8px;cursor:pointer;margin-left: -12px;"></i>
                        </div>
                    </div>
                </div>


                <div ng-if="attribute.editMode == true &&
            (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'INTEGER' ||
            attribute.attributeDef.dataType == 'LONGTEXT' || attribute.attributeDef.dataType == 'HYPERLINK' ||
             attribute.attributeDef.dataType == 'DOUBLE' || attribute.attributeDef.dataType == 'BOOLEAN' ||
             attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                    <input type="text" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'TEXT'"
                           placeholder="{{'ENTER_VALUE' | translate}}"
                           ng-model="attribute.value.stringValue" style="width:200px;">
                <textarea type="text" class="form-control" name="title"
                          ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                          placeholder="{{'ENTER_VALUE' | translate}}"
                          ng-model="attribute.value.longTextValue" style="width:320px;"></textarea>
                    <input type="number" class="form-control" name="title"
                           placeholder="{{'ENTER_NUMBER' | translate}}"
                           ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                           ng-model="attribute.value.integerValue" style="width:200px;">
                    <input type="text" class="form-control" name="title" valid-number
                           ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                           placeholder="{{'ENTER_NUMBER' | translate}}"
                           ng-model="attribute.value.doubleValue" style="width:200px;">
                    <input type="text" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                           placeholder="{{'ENTER_VALUE' | translate}}"
                           ng-model="attribute.value.hyperLinkValue" style="width:350px;">
                    <label class="radio-inline" ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                        <input type="checkbox"
                               ng-click="glossaryAttributesVm.saveProperties(attribute)"
                               ng-model="attribute.value.booleanValue">
                    </label>

                    <div class="input-group" ng-if="attribute.attributeDef.dataType == 'DATE'"
                         style="width: 200px;">
                        <input type="text" class="form-control" date-picker-edit
                               ng-model="attribute.value.dateValue"
                               name="attDate" placeholder="dd/mm/yyyy">
                                    <span class="input-group-addon">
                                        <i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                    <div ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'CURRENCY'"
                         ng-bind-html="attribute.value.encodedCurrencyType">
                    </div>
                    <input type="text" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'CURRENCY'"
                           ng-model="attribute.value.currencyValue"
                           style="width:185px;margin-top:-25px;margin-left:15px;">


                    <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'LONGTEXT' && attribute.attributeDef.dataType != 'HYPERLINK'"
                            class="btn btn-sm btn-primary"
                            type="button" title="{{saveAttributeTitle}}"
                            style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                            ng-click="glossaryAttributesVm.saveProperties(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'LONGTEXT' && attribute.attributeDef.dataType != 'HYPERLINK'"
                            class="btn btn-sm btn-default"
                            type="button" title="{{cancelChangesTitle}}"
                            style="margin-top:-65px;padding: 8px;width:33px;"
                            ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>

                    <i class="fa fa-times-circle" ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                       ng-click="glossaryAttributesVm.cancelChanges(attribute)" title="{{cancelChangesTitle}}"
                       style="padding: 8px;width:33px;cursor:pointer;"></i>

                    <button ng-if="attribute.attributeDef.dataType == 'LONGTEXT'" class="btn btn-sm btn-primary"
                            type="button" title="{{saveAttributeTitle}}"
                            style="margin-top:-65px;margin-left:325px;padding: 8px;width:33px;"
                            ng-click="glossaryAttributesVm.saveProperties(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.attributeDef.dataType == 'LONGTEXT'" class="btn btn-sm btn-default"
                            type="button" title="{{cancelChangesTitle}}"
                            style="margin-top:-65px;padding: 8px;width:33px;"
                            ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>

                    <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'" class="btn btn-sm btn-primary"
                            type="button" title="{{saveAttributeTitle}}"
                            style="margin-top:-65px;margin-left:353px;padding: 8px;width:33px;"
                            ng-click="glossaryAttributesVm.saveProperties(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'" class="btn btn-sm btn-default"
                            type="button" title="{{cancelChangesTitle}}"
                            style="margin-top:-65px;padding: 8px;width:33px;"
                            ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>

                </div>

                <%----------------  TIMESTAMP Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                    <a href="" title="{{changeTime}}"
                       ng-show="attribute.showTimestamp == false && hasPermission('glossary','edit')"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="glossaryAttributesVm.changeTimestamp(attribute)">
                        {{attribute.value.timestampValue}}
                        <i class="fa fa-pencil" style="padding-left: 7px;"
                           title="{{'CLICK_TO_SET_TIME' | translate}}"></i>
                    </a>

                    <span ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'">{{attribute.value.timestampValue}}</span>

                    <div ng-if="attribute.showTimestamp == true">
                        <div>{{attribute.value.timestampValue}}</div>
                        <div uib-timepicker
                             ng-model="attribute.timestampValue">
                        </div>

                        <button class="btn btn-xs btn-primary" type="button"
                                style="/*margin-top:-66px;margin-left:253px*/; margin-top: -194px;margin-left: 168px;"
                                ng-click="glossaryAttributesVm.saveTimeProperty(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-xs btn-default" type="button" style="margin-top:-194px;"
                                ng-click="glossaryAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%----------------  TIME Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                <span>
                <a href=""
                   ng-show="attribute.showTimeAttribute == false && attribute.value.timeValue != null"
                   title="{{changeTime}}" ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                   ng-click="glossaryAttributesVm.changeTime(attribute)">
                    {{attribute.value.timeValue}} <i class="fa fa-pencil" title="{{'CLICK_TO_SET_TIME' | translate}}"
                                                     style="padding-left: 7px;"></i>
                </a>
                  </span>
                <span>
                <a ng-if="attribute.showTimeAttribute == false && attribute.value.timeValue == null"
                   href="" ng-click="glossaryAttributesVm.changeTime(attribute)">
                    <i class="fa fa-pencil" title="{{'CLICK_TO_SET_TIME' | translate}}"
                       style="padding-left: 7px;"></i>
                </a>
                    </span>
                    <span ng-if="!hasPermission('manufacturer','edit')">{{attribute.value.timeValue}}</span>

                    <div ng-if="attribute.showTimeAttribute == true">

                        <input class="form-control" type="text" ng-model="attribute.timeValue" time-picker
                               style="width: 240px;">

                        <button class="btn btn-xs btn-primary" type="button"
                                style="margin-top:-66px;margin-left:253px;"
                                ng-click="glossaryAttributesVm.saveTimeProperty(attribute)"><i class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-xs btn-default" type="button" style="margin-top:-65px;"
                                ng-click="glossaryAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>


                <%----------------  IMAGE Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                    <a href="" ng-click="showAttributeImage(attribute)"
                       title="{{Clicktoshowlargeimage}}">
                        <img ng-if="attribute.value.imageValue != null"
                             ng-src="{{attribute.value.mfrImagePath}}"
                             style="height: 100px;width: 100px;margin-bottom: 5px;">
                    </a>
                    <%-- To show large image --%>
                    <%--<div id="myModal2" class="img-model modal">
                        <span class="closeImage">&times;</span>
                        <img class="modal-content" id="img03">
                    </div>--%>
                    <div id="item-thumbnail-basic{{attribute.attributeDef.id}}" class="item-thumbnail modal">
                        <div class="item-thumbnail-content">
                            <div class="thumbnail-content" style="display: flex;width: 100%;">
                                <div class="thumbnail-view"
                                     id="thumbnail-view-basic{{attribute.attributeDef.id}}">
                                    <div id="thumbnail-image-basic{{attribute.attributeDef.id}}"
                                         style="display: table-cell;vertical-align: middle;text-align: center;">
                                        <img ng-src="{{attribute.value.mfrImagePath}}"
                                             style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{attribute.attributeDef.id}}"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="inline">
                        <a href=""
                           ng-if="attribute.value.imageValue == null && hasPermission('glossary','edit')"
                           ng-click="glossaryAttributesVm.change(attribute)"
                           ng-hide="attribute.changeImage == true" title="{{addImage}}">
                            <i class="fa fa-pencil" title="{{'ADD_IMAGE' | translate}}" style="padding-left: 7px"></i>
                        </a>
                        <i ng-if="attribute.value.imageValue != null && hasPermission('glossary','edit')"
                           class="fa fa-pencil"
                           ng-click="glossaryAttributesVm.change(attribute)"
                           ng-hide="attribute.changeImage == true" title="{{changeImageMessage}}">
                        </i>
                        <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                               type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
                        <button class="btn btn-xs btn-primary"
                                ng-click="glossaryAttributesVm.saveImage(attribute)"
                                ng-show="attribute.changeImage == true" title="save image"
                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;"><i
                                class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="glossaryAttributesVm.cancel(attribute)" title="{{cancelChangesTitle}}"
                                ng-show="attribute.changeImage == true" style="margin-top:-62px;padding-top:8px;"><i
                                class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%------------------------  OBJECT Property  -------------------------%>

                <div class="row" ng-if="attribute.attributeDef.dataType == 'OBJECT'">

                    <a ng-if="attribute.value.refValue.objectType == 'ITEM'" href=""
                       ng-click="glossaryAttributesVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-bind-html="attribute.value.refValue.itemNumber">
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'ITEMREVISION'" href=""
                       ng-click="glossaryAttributesVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.itemMaster+" "+attribute.value.refValue.revision+"
                        "+attribute.value.refValue.lifeCyclePhase.phase}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'CHANGE'" href=""
                       ng-click="glossaryAttributesVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-bind-html="attribute.value.refValue.ecoNumber">
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION'" href=""
                       ng-click="glossaryAttributesVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-bind-html="attribute.value.refValue.name">
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURER'" href=""
                       ng-click="glossaryAttributesVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-bind-html="attribute.value.refValue.name">
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'PROJECT'" href=""
                       ng-click="glossaryAttributesVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-bind-html="attribute.value.refValue.name"></a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURERPART'" href=""
                       ng-click="glossaryAttributesVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-bind-html="attribute.value.refValue.partName">
                    </a>
                    <a href=""
                       ng-if="attribute.value.refValue == null && attribute.editMode == false"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       title="{{addAttribute}}"
                       ng-click="glossaryAttributesVm.showSelectionDialog(attribute.attributeDef.refType, attribute)">
                        <i class="fa fa-pencil" style="padding-left: 5px;color: #337ab7"
                           title="{{ADD_ATTRIBUTE | translate}}"></i>
                    </a>
                    <i ng-if="attribute.value.refValue != null && attribute.editMode == false"
                       class="fa fa-pencil"
                       title="{{changeAttributeTitle}}" style="margin-left:5px; color: #337ab7"
                       ng-click="glossaryAttributesVm.showSelectionDialog(attribute.attributeDef.refType, attribute)">
                    </i>
                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-primary"
                            title="{{saveAttributeTitle}}" style="margin-left: 5px;"
                            ng-click="glossaryAttributesVm.saveObject(attribute)">
                        <i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default"
                            title="{{cancelChangesTitle}}"
                            ng-click="cancelChanges(attribute)">
                        <i class="fa fa-times"></i>
                    </button>
                </div>

                <%----------------  ATTACHMENT Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                    <div ng-if="attribute.value.attachmentValues.length > 0"
                         ng-repeat="attachment in attribute.value.attachmentValues">
                        <ul style="margin-left:-30px;">
                            <li>
                                <a href="" ng-click="glossaryAttributesVm.openPropertyAttachment(attachment)"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:250px;color: #002451;"
                                   ng-bind-html="attachment.name">
                                </a>
                                <a href="" title="{{deleteAttachment}}"
                                   ng-show="selectGlossaryLifeCyclePhase != 'Released'"
                                   ng-if="hasPermission('glossary','delete')"
                                   ng-click="glossaryAttributesVm.deleteAttachments(attribute,attachment)">
                                    <i class="fa fa-times"
                                       style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <a href="">
                            <div ng-if="attribute.showAttachment == false && hasPermission('glossary','edit')"
                                 ng-click="glossaryAttributesVm.addAttachment(attribute)"
                                 ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                                 title="{{addAttachments}}">
                                <i class="fa fa-pencil" title="{{'ADD_ATTACHMENTS' | translate}}"
                                   style="padding-left: 7px"></i>

                            </div>
                        </a>

                        <div ng-show="attribute.showAttachment == true">
                            <input class="browse-control" name="file"
                                   multiple="true"
                                   type="file" ng-file-model="attribute.attachmentValues"
                                   style="width: 250px;"/>
                            <button class="btn btn-xs btn-primary" title="{{saveAttachmentsTitle}}"
                                    ng-click="glossaryAttributesVm.saveAttachments(attribute)"
                                    style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-xs btn-default" title="{{cancelChangesTitle}}"
                                    ng-click="glossaryAttributesVm.cancelAttachment(attribute)"
                                    style="margin-top:-62px;padding-top:8px;">
                                <i class="fa fa-times"></i>
                            </button>
                            <p ng-repeat="file in attribute.attachmentValues">
                                <span ng-bind-html="file.name"></span>
                            </p>
                        </div>
                    </div>
                </div>

                <%------  Currency Property  ------%>

                <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">

                    <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">

                        <div ng-if="attribute.value.encodedCurrencyType != null">
                    <span>
                        <a ng-if="attribute.editMode == false && hasPermission('glossary','edit')"
                           ng-hide="selectGlossaryLifeCyclePhase == 'Released'" title="{{changeCurrencyValueTitle}}"
                           ng-bind-html="attribute.value.encodedCurrencyType ">
                        </a>
                    </span>
                    <span>
                    <a href="" ng-if="attribute.editMode == false && hasPermission('glossary','edit')"
                       title="{{changeCurrencyValueTitle}}"
                       ng-click="changeAttribute(attribute)">
                        {{attribute.value.currencyValue}}
                        <i class="fa fa-pencil" style="padding-left: 7px"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>

                    </a>
                        </span>

                            <span ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'">{{attribute.value.currencyValue}}</span>
                        </div>


                        <%----------  For newly added 'CURRENCY' attribute after ECO Creation ---------------%>

                        <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                            <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                                <select class="form-control" ng-model="attribute.value.currencyType"
                                        ng-init="attribute.value.currencyType = glossaryAttributesVm.currencies[0].id"
                                        ng-options="currency.id as currency.name for currency in glossaryAttributesVm.currencies">
                                </select>
                            </div>
                            <a href=""
                               ng-if="attribute.changeCurrency == false && hasPermission('glossary','edit')"
                               ng-click="glossaryAttributesVm.changeCurrencyValue(attribute)"
                               ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                               title="{{changeCurrencyValueTitle}}">
                                {{attribute.value.currencyValue}}
                                <i class="fa fa-pencil" style="padding-left: 7px"
                                   title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>

                            </a>
                        <span ng-if="!hasPermission('glossary','edit') || selectGlossaryLifeCyclePhase == 'Released'">
                            {{attribute.value.currencyValue}}
                        </span>

                            <div ng-if="attribute.changeCurrency == true"
                                 style="width: 100px;margin-top: -40px;margin-left: 155px;">
                                <input class="form-control" name="currencyValue" type="number"
                                       placeholder="{{'ENTER_VALUE' | translate}}"
                                       ng-model="attribute.value.currencyValue"/>
                            </div>
                            <button ng-if="attribute.changeCurrency == true" class="btn btn-xs btn-primary"
                                    title="{{saveCurrencyTitle}}"
                                    ng-click="glossaryAttributesVm.saveProperties(attribute)"
                                    style="margin-top:-64px;margin-left: 260px;padding-top:10px;">
                                <i class="fa fa-check"></i>
                            </button>
                            <button ng-if="attribute.changeCurrency == true" class="btn btn-xs btn-default"
                                    title="{{cancelChangesTitle}}"
                                    ng-click="cancelChanges(attribute)"
                                    style="margin-top:-64px;padding-top:10px;">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>