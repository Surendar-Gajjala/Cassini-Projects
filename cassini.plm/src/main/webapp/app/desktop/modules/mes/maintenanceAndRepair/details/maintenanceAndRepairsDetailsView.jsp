<div class="view-container" fitcontent>
  <style>
    .item-details-tabs .tab-content {
      padding: 0 !important;
    }

    #freeTextSearchDirective {
      top: 7px !important;
    }

    .tab-content {
      padding: 0px !important;
    }

    .tab-content .tab-pane {
      overflow: auto !important;
    }

    .tab-pane {
      position: relative;
    }

    .tab-content .tab-pane .responsive-table {
      height: 100%;
      position: absolute;
      overflow: auto !important;
      padding: 5px;
    }

    .tab-content .tab-pane .responsive-table table thead th {
      position: -webkit-sticky;
      position: sticky;
      top: -10px !important;
      z-index: 5;
    }

    .sticky-col {
      position: sticky !important;
      position: -webkit-sticky !important;
    }

    .sticky-actions-col {
      right: -10px !important;
    }

  </style>
  <div class="view-toolbar">
    <div class="row" style="margin: 0">
      <div class="btn-group">
        <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.maintenanceAndRepair.all')"
                title="{{'SHOW_ALL' | translate}}">
          <i class="fa fa-table" aria-hidden="true"></i>
        </button>

      </div>
      <button ng-if="maintenanceAndRepairDetailsVm.tabs.workflow.active"
              ng-show="!plant.startWorkflow"
              class="btn btn-sm btn-success" >
        <i class="fa fa-indent" aria-hidden="true" style=""></i>
      </button>

      <%--<button ng-if="maintenanceAndRepairDetailsVm.tabs.files.active && hasPermission('change','dcr','edit') && hasFiles == true"
              title="{{downloadTitle}}"
              class="btn btn-sm btn-success" >
        <i class="fa fa-download" aria-hidden="true" style=""></i>
      </button>--%>
      <button class="btn btn-default btn-sm"
              ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
              ng-click="copyChangeFilesToClipBoard()" title="{{copyFileToClipboard}}">
        <i class="fa fa-copy" style="font-size: 16px;"></i>
      </button>
      <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
        <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="false">
          <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
          <li ng-click="clearAndCopyChangeFilesToClipBoard()"><a href=""
                                                                 translate>CLEAR_AND_ADD_FILES</a>
          </li>
          <li ng-click="copyChangeFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
            ({{clipBoardObjectFiles.length}})</a></li>
        </ul>
      </div>

      <div class="pull-right">

      </div>
    </div>
    <free-text-search ng-if="maintenanceAndRepairDetailsVm.tabs.files.active" on-clear="maintenanceAndRepairDetailsVm.onClear"
                      on-search="maintenanceAndRepairDetailsVm.freeTextSearch"></free-text-search>
  </div>
  <div class="view-content no-padding" style="overflow-y: hidden;">
    <div class="row">
      <div class="col-sm-12" style="padding: 10px;">
        <div class="item-details-tabs">
          <uib-tabset active="maintenanceAndRepairDetailsVm.active">
            <uib-tab heading="{{maintenanceAndRepairDetailsVm.tabs.basic.heading}}"
                     active="maintenanceAndRepairDetailsVm.tabs.basic.active"
                     select="maintenanceAndRepairDetailsVm.tabActivated(maintenanceAndRepairDetailsVm.tabs.basic.id)">
              <div ng-include="maintenanceAndRepairDetailsVm.tabs.basic.template"
                   ng-controller="MaintenanceAndRepairBasicInfoController as maintenanceAndRepairBasicVm"></div>
            </uib-tab>
            <uib-tab heading="{{maintenanceAndRepairDetailsVm.tabs.attributes.heading}}"
                     active="maintenanceAndRepairDetailsVm.tabs.attributes.active"
                     select="maintenanceAndRepairDetailsVm.tabActivated(maintenanceAndRepairDetailsVm.tabs.attributes.id)">
              <div ng-include="maintenanceAndRepairDetailsVm.tabs.attributes.template"
                   ng-controller="MaintenanceAndRepairAttributesController as maintenanceAndRepairAttributesVm"></div>
            </uib-tab>
            <uib-tab id="files" heading="{{maintenanceAndRepairDetailsVm.tabs.files.heading}}"
                     active="maintenanceAndRepairDetailsVm.tabs.files.active"
                     select="maintenanceAndRepairDetailsVm.tabActivated(maintenanceAndRepairDetailsVm.tabs.files.id)">
              <div ng-include="maintenanceAndRepairDetailsVm.tabs.files.template"
                   ng-controller="MaintenanceAndRepairFilesController as maintenanceAndRepairFilesVm"></div>
            </uib-tab>
            <plugin-tabs tabs="maintenanceAndRepairDetailsVm.tabs" custom-tabs="maintenanceAndRepairDetailsVm.customTabs"
                         object-value="maintenanceAndRepairDetailsVm.dco" tab-id="maintenanceAndRepairDetailsVm.tabId" active="maintenanceAndRepairDetailsVm.active"></plugin-tabs>
            <uib-tab id="" heading="{{maintenanceAndRepairDetailsVm.tabs.timelineHistory.heading}}"
                     active="maintenanceAndRepairDetailsVm.tabs.timelineHistory.active"
                     select="maintenanceAndRepairDetailsVm.tabActivated(maintenanceAndRepairDetailsVm.tabs.timelineHistory.id)">
              <div ng-include="maintenanceAndRepairDetailsVm.tabs.timelineHistory.template"
                   ng-controller="MaintenanceAndRepairTimeLineController as maintenanceAndRepairTimeLineVm"></div>
            </uib-tab>

          </uib-tabset>
        </div>
      </div>
    </div>
  </div>
</div>
