<p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'Created Date' && objectAttribute.name != 'createdBy' && objectAttribute.name != 'Modified By' && objectAttribute.name != 'Created By' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                                      && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
   ng-init="attrName = objectAttribute.id">
    <a href=""
       ng-if="objectAttribute.refType == 'ITEM' && object[attrName].objectType == 'ITEM'"
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].itemNumber}}
    </a>
    <a href=""
       ng-if="objectAttribute.refType == 'ITEMREVISION'"
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">
        {{object[attrName].itemMaster+" "+object[attrName].revision+" "+
        object[attrName].lifeCyclePhase.phase}}
    </a>
    <a href=""
       ng-if="objectAttribute.refType == 'MANUFACTURER'"
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].name}}
    </a>
    <a href=""
       ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].partNumber}}
    </a>
    <a href=""
       ng-if="objectAttribute.refType == 'WORKFLOW'"
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].name}}
    </a>
    <a href=""
       ng-if="objectAttribute.refType == 'PROJECT'"
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].name}}
    </a>

    <span ng-if="objectAttribute.refType == 'CHANGE'" href="">
        <a ng-if="object[attrName].changeType == 'ECO'"
           ng-click="showAttributeDetails(object[attrName])"
           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
            {{object[attrName].ecoNumber}}
        </a>
        <a ng-if="object[attrName].changeType == 'DCO'"
           ng-click="showAttributeDetails(object[attrName])"
           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
            {{object[attrName].dcoNumber}}
        </a>
        <a ng-if="object[attrName].changeType == 'ECR' || object[attrName].changeType == 'DCR'"
           ng-click="showAttributeDetails(object[attrName])"
           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
            {{object[attrName].crNumber}}
        </a>
        <a ng-if="object[attrName].changeType == 'DEVIATION' || object[attrName].changeType == 'WAIVER'"
           ng-click="showAttributeDetails(object[attrName])"
           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
            {{object[attrName].varianceNumber}}
        </a>
    </span>

    <a ng-if="object[attrName].objectType == 'OEMPARTMCO'||  object[attrName].objectType == 'ITEMMCO'"
       href=""
       ng-click="showAttributeDetails(object[attrName])"
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
        {{object[attrName].mcoNumber}}

    </a>

    <a ng-if="objectAttribute.refType == 'MESOBJECT' && (object[attrName].objectType == 'PLANT' || object[attrName].objectType == 'ASSEMBLYLINE' ||
              object[attrName].objectType == 'WORKCENTER' || object[attrName].objectType == 'MACHINE' ||
              object[attrName].objectType == 'JIGFIXTURE' || object[attrName].objectType == 'MANPOWER' ||
              object[attrName].objectType == 'INSTRUMENT' || object[attrName].objectType == 'EQUIPMENT' ||
              object[attrName].objectType == 'PRODUCTIONORDER' || object[attrName].objectType == 'OPERATION' ||
              object[attrName].objectType == 'MATERIAL' || object[attrName].objectType == 'TOOL')"
       href=""
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].number}}
    </a>

    <a ng-if="objectAttribute.refType == 'MROOBJECT' && (object[attrName].objectType == 'MROASSET' || object[attrName].objectType == 'MROMETER' ||
                                object[attrName].objectType == 'MROSPAREPART' || object[attrName].objectType == 'MROWORKREQUEST' || object[attrName].objectType == 'MROWORKORDER')"
       href=""
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].number}}
    </a>
    <a ng-if=" objectAttribute.refType == 'REQUIREMENT' && object[attrName].objectType == 'REQUIREMENT'"
       href=""
       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
       ng-click="showAttributeDetails(object[attrName])">{{object[attrName].number}}
    </a> <a
        ng-if=" objectAttribute.refType == 'REQUIREMENTDOCUMENT' && object[attrName].objectType == 'REQUIREMENTDOCUMENT'"
        href=""
        title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
        ng-click="showAttributeDetails(object[attrName])">{{object[attrName].number}}
</a> <a ng-if=" objectAttribute.refType == 'CUSTOMOBJECT' && object[attrName].objectType == 'CUSTOMOBJECT'"
        href=""
        title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
        ng-click="showAttributeDetails(object[attrName])">{{object[attrName].number}}
</a>


    <a href="#" ng-if="objectAttribute.refType == 'PERSON'">
        {{object[attrName].firstName}}
    </a>
</p>

<div class="attributeTooltip"
     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'Created Date' && objectAttribute.name != 'createdBy' && objectAttribute.name != 'Modified By' && objectAttribute.name != 'Created By' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
     ng-init="attrName = objectAttribute.id">
    <p>
        <a ng-if="object[attrName].length > 0" href="">
            {{object[attrName].length}} Attachments
        </a>
    </p>

    <div class="attributeTooltiptext">
        <ul>
            <li ng-repeat="attachment in object[attrName]">
                <a href="" ng-click="openAttachment(attachment)"
                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                    {{attachment.name}}
                </a>
            </li>
        </ul>
    </div>
</div>

<div class="attributeTooltip"
     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'Created Date' && objectAttribute.name != 'createdBy' && objectAttribute.name != 'Modified By' && objectAttribute.name != 'Created By'
            && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'
            && objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple == true"
     ng-init="attrName = objectAttribute.id">
    <p>
        <a ng-if="object[attrName].length > 0" href="">
            {{object[attrName].length}} Values
        </a>
    </p>

    <div class="attributeTooltiptext">
        <ul>
            <li ng-repeat="listValue in object[attrName]">
                <a href=""
                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                    {{listValue}}
                </a>
            </li>
        </ul>
    </div>
</div>

<div class="attributeTooltip"
     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'Created Date' && objectAttribute.name != 'createdBy' && objectAttribute.name != 'Modified By' && objectAttribute.name != 'Created By'
            && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'
            && objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple != true"
     ng-init="attrName = objectAttribute.id">
    <p>
        <span title="{{object[attrName]}}"
              ng-if="object[attrName] != null && object[attrName] != '' && object[attrName] != undefined">{{object[attrName] | limitTo: 5 }}{{object[attrName].length > 6 ? '...' : ''}}</span>
        <span title="{{objectAttribute.defaultListValue}}" ng-if="(object[attrName] == null || object[attrName] == '' || object[attrName] == undefined)
                     && (objectAttribute.objectType == object.itemType.objectType && objectAttribute.itemType == object.itemType.id)">
            {{objectAttribute.defaultListValue | limitTo: 5 }}{{objectAttribute.defaultListValue.length > 6 ? '...' : ''}}</span>
        <span title="{{objectAttribute.defaultListValue}}" ng-if="(object[attrName] == null || object[attrName] == '' || object[attrName] == undefined)
                     && (objectAttribute.objectType == 'ITEM' || objectAttribute.objectType == 'ITEMREVISION')">
            {{objectAttribute.defaultListValue | limitTo: 5 }}{{objectAttribute.defaultListValue.length > 6 ? '...' : ''}}</span>
    </p>
</div>

<div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'Created Date' && objectAttribute.name != 'createdBy' && objectAttribute.name != 'Modified By' && objectAttribute.name != 'Created By' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
     ng-init="attrName = objectAttribute.id">
    <a href="" ng-click="showThumbnailImage(objectAttribute)"
       title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
        <img ng-if="object[attrName] != null"
             ng-src="{{object[attrName]}}"
             style="height: 30px;width: 40px;margin-bottom: 5px;">
    </a>

    <div id="item-thumbnail{{objectAttribute.id}}{{objectAttribute.id}}" class="item-thumbnail modal">
        <div class="item-thumbnail-content">
            <div class="thumbnail-content" style="display: flex;width: 100%;">
                <div class="thumbnail-view" id="thumbnail-view{{objectAttribute.id}}{{objectAttribute.id}}">
                    <div id="thumbnail-image{{objectAttribute.id}}{{objectAttribute.id}}"
                         style="display: table-cell;vertical-align: middle;text-align: center;">
                        <img ng-src="{{object[attrName]}}"
                             style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                        <span class="thumbnail-close"
                              id="thumbnail-close{{objectAttribute.id}}{{objectAttribute.id}}"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'Created Date' && objectAttribute.name != 'Modified By' && objectAttribute.name != 'Created By' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
          && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'&& objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'LONGTEXT' && objectAttribute.dataType != 'HYPERLINK'
          && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT' && objectAttribute.dataType != 'TEXT' && objectAttribute.dataType != 'BOOLEAN'"
   ng-init="attrName = objectAttribute.id">
    {{object[attrName]}}
</p>

<div ng-if="objectAttribute.dataType == 'BOOLEAN'"
     ng-init="attrName = objectAttribute.id">
    <p ng-if="object[attrName] == true">{{object[attrName]}}</p>

    <p ng-if="object[attrName] == false || object[attrName] == null">false</p>
</div>

<p ng-if="objectAttribute.dataType == 'LONGTEXT'"
   ng-init="attrName = objectAttribute.id" title="{{object[attrName]}}">
    {{object[attrName] | limitTo: 20 }}{{object[attrName].length > 21 ? '...' : ''}}
</p>

<p ng-if="objectAttribute.dataType == 'TEXT'" class="col-width-200"
   ng-init="attrName = objectAttribute.id">
   <span title="{{object[attrName]}}"
         ng-if="object[attrName] != null && object[attrName] != '' && object[attrName] != undefined">{{object[attrName]}}</span>
    <span title="{{objectAttribute.defaultTextValue}}" ng-if="(object[attrName] == null || object[attrName] == '' || object[attrName] == undefined)
                 && (objectAttribute.objectType == object.itemType.objectType && objectAttribute.itemType == object.itemType.id)">
        {{objectAttribute.defaultTextValue}}</span>
    <span title="{{objectAttribute.defaultTextValue}}" ng-if="(object[attrName] == null || object[attrName] == '' || object[attrName] == undefined)
                 && (objectAttribute.objectType == 'ITEM' || objectAttribute.objectType == 'ITEMREVISION')">
        {{objectAttribute.defaultTextValue}}</span>
</p>

<p ng-if="objectAttribute.dataType == 'HYPERLINK'"
   ng-init="attrName = objectAttribute.id" class="col-width-250">
    <a href=""
       ng-click="showHyperLinkValue(object[attrName])">{{object[attrName]}}</a>
</p>


<span ng-init="currencyType = objectAttribute.id+'type'"
      ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="object[currencyType]">
</span>
<span ng-init="attrName = objectAttribute.id"
      ng-if="objectAttribute.dataType == 'CURRENCY'">{{object[attrName]}}</span>

<span ng-init="attrName = objectAttribute.id"
      ng-if="objectAttribute.dataType == 'RICHTEXT'">
    <a href=""
       ng-if="object[attrName] != null && object[attrName] != undefined && object[attrName] != ''" data-toggle="modal"
       ng-click="showRichText(object[attrName],objectAttribute,object)">Click to show RichText</a>
</span>

<p ng-if="objectAttribute.name == 'Modified By'">{{object.modifiedByObject.fullName || object.modifiedByName}}</p>

<p ng-if="objectAttribute.name == 'Created By'">
    <span ng-if="object.createdByObject != null">{{object.createdByObject.fullName}}</span>
    <span ng-if="object.createdByObject == null && object.createdBy != null">{{object.createdByName}}</span>
    <span ng-if="object.createdBy != null">{{object.createdBy}}</span>
</p>

<p ng-if="objectAttribute.name == 'Created Date'">{{object.createdDate}}</p>
