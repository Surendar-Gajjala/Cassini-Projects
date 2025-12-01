<a ng-if="object.configurable == true" title="{{configurableItem}}" class="fa fa-cog"
   aria-hidden="true"></a>
<a ng-if="object.configured == true" title="{{configuredItem}}" class="fa fa-cogs"
   aria-hidden="true"></a>
<a class="icon fa fa-sitemap" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{object.latestRevisionObject.hasBom ? showBomTitle : objectHasBom}}"
   ng-click="showItemBom(object)"
   ng-if="object.latestRevisionObject.hasBom || object.hasBom">
</a>
<a class="icon fa fa-handshake-o" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showChangesTitle}}"
   ng-click="showItemChanges(object)"
   ng-if="object.hasChanges">
</a>
<a class="icon fa fa-deviantart" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showVarianceTitle}}"
   ng-click="showItemVariance(object)"
   ng-if="object.hasVariance">
</a>
<a class="icon fa fa-certificate" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showQualityTitle}}"
   ng-click="showItemQuality(object)"
   ng-if="object.hasQuality">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showFileTitle}}"
   ng-if="object.latestRevisionObject.hasFiles && object.itemFiles.length > 0 && object.objectType == 'ITEM'"
   uib-popover-template="itemFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.itemNumber}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showObjectFileTitle}}"
   ng-if="object.itemFiles.length > 0 && object.objectType == 'CUSTOMOBJECT'"
   uib-popover-template="itemFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.number}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showBopFiles}}"
   ng-if="object.itemFiles.length > 0 && object.objectType == 'BOPREVISION'"
   uib-popover-template="bopFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.number}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="fa fa-cubes" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showPartTitle}}"
   ng-click="showItemMfrParts(object)"
   ng-if="object.hasMfrParts && object.makeOrBuy == 'BUY'">
</a>
<a class="la la-bell" style="padding-right: 4px;"
   title="{{showSubscribers}}"
   ng-if="object.subscribes.length > 0"
   uib-popover-template="subscribesPopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.itemNumber}}) {{Subscribes}}"
   popover-trigger="'outsideClick'">
</a>
<i class="fa fa-lock" title="{{lockedBy}} {{object.lockedByName}}"
   style="font-size: 16px;margin-left: 2px;padding-right: 4px;color: #d9534f;"
   ng-if="object.lockObject"></i>
<a class="fa fa-object-ungroup" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{hasAlternateParts}}"
   ng-click="showItemRelatedItems(object)"
   ng-if="object.hasAlternates">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showInspectionReports}}"
   ng-if="object.inspectionReportFiles.length > 0 && object.objectType == 'MANUFACTURERPART'"
   uib-popover-template="mfrPartsInspectionReportPopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.partNumber}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>


<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showMbomFiles}}" 
   ng-if="object.mbomFiles.length > 0 && object.objectType == 'MBOM'"
   uib-popover-template="mbomFilesPopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.number}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>

<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showOperationFiles}}" 
   ng-if="object.operationFiles.length > 0 && object.objectType == 'OPERATION'"
   uib-popover-template="operationFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>

<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showShiftFiles}}" 
   ng-if="object.shiftFiles.length > 0 && object.objectType == 'SHIFT'"
   uib-popover-template="shiftFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>

<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showMaterialFiles}}" 
   ng-if="object.materialFiles.length > 0 && object.objectType == 'MATERIAL'"
   uib-popover-template="materialFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>

<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showJigFixtureFiles}}" 
   ng-if=" object.jigsFixtureFiles.length > 0 && object.objectType == 'JIGFIXTURE'"
   uib-popover-template="jigFixtureFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>

<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showPlantFiles}}" 
   ng-if=" object.plantFiles.length > 0 && object.objectType == 'PLANT'"
   uib-popover-template="plantFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showToolFiles}}" 
   ng-if="object.toolFiles.length > 0 && object.objectType == 'TOOL'"
   uib-popover-template="toolFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showInstrumentFiles}}" 
   ng-if="object.instrumentFiles.length > 0 && object.objectType == 'INSTRUMENT'"
   uib-popover-template="instrumentFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showEquipmentFiles}}" 
   ng-if="object.equipmentFiles.length > 0 && object.objectType == 'EQUIPMENT'"
   uib-popover-template="equipmentFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showMachineFiles}}" 
   ng-if="object.machineFiles.length > 0 && object.objectType == 'MACHINE'"
   uib-popover-template="machineFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showWorkCenterFiles}}" 
   ng-if=" object.workcenterFiles.length > 0 && object.objectType == 'WORKCENTER'"
   uib-popover-template="workCenterFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
<a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
   title="{{showAssemblyLineFiles}}" 
   ng-if="object.assemblyLineFiles.length > 0 && object.objectType == 'ASSEMBLYLINE'"
   uib-popover-template="assemblyLineFilePopover.templateUrl"
   popover-append-to-body="true"
   popover-popup-delay="50"
   popover-placement="right"
   popover-title="({{object.name}}) {{FILES}}"
   popover-trigger="'outsideClick'">
</a>
