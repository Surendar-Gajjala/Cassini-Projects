<style>
  .view-content {
    position: relative;
  }

  .view-content .responsive-table {
    padding: 10px;
    position: absolute;
    bottom: 40px;
    top: 0;
    overflow: auto;
  }

  .view-content .responsive-table table thead th {
    position: -webkit-sticky;
    position: sticky;
    top: -10px;
    z-index: 5;
  }

  .view-content .table-footer {
    padding: 0 10px 0 10px;
    position: absolute;
    bottom: 0px !important;
    height: 40px;
    width: 100%;
    border-top: 1px solid #D3D7DB;
    display: table;
  }

  .view-content .table-footer > div {
    display: table-row;
    line-height: 30px;
  }

  .view-content .table-footer > div h5 {
    margin: 0;
  }

  .view-content .table-footer > div > div {
    display: table-cell;
    vertical-align: middle;
  }

  .view-content .table-footer > div > div > i {
    font-size: 16px;
  }

  a.disabled {
    cursor: not-allowed;
    color: lightgrey;
  }

  .sticky-col {
    position: sticky !important;
    position: -webkit-sticky !important;
  }

  .sticky-actions-col {
    right: -10px !important;
  }

  #freeTextSearchDirective {
    top: 7px !important;
  }

  .attributeTooltip {
    position: relative;
    display: inline-block;
  }

  .attributeTooltip .attributeTooltiptext {
    visibility: hidden;
    width: 200px;
    background-color: #7BB7EB;
    color: #141f9f;
    text-align: left;
    border-radius: 6px;
    padding: 5px 0;
    position: absolute;
    z-index: 1;
    top: -5px;
    bottom: auto;
    right: 100%;
    opacity: 0;
    transition: opacity 1s;
  }

  .attributeTooltip .attributeTooltiptext::after {
    content: "";
    position: absolute;
    top: 25%;
    left: 102%;
    margin-left: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: transparent transparent transparent #7BB7EB;
  }

  .attributeTooltip:hover .attributeTooltiptext {
    visibility: visible;
    opacity: 1;
  }
</style>
<div class="view-container" fitcontent>
  <div class="view-toolbar">
    <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>MAINTENANCEANDREPAIRS</span>

    <button class="btn btn-sm new-button" ng-click="allMaintenanceAndRepairVm.newMaintenanceAndRepair()"
            title="{{'NEW_MAINTENANCE_AND_REPAIRS' | translate}}" ng-if="hasPermission('machine','create')">
      <i class="las la-plus" aria-hidden="true"></i>
      <span>{{ 'NEW_MAINTENANCE_AND_REPAIRS' | translate }}</span>
    </button>

    <div class="btn-group">
      <button class="btn btn-sm btn-success" ng-click="allMaintenanceAndRepairVm.showTypeAttributes()"
              title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
        <i class="fa fa-newspaper-o" style=""></i>
      </button>
    </div>
    <free-text-search on-clear="allMaintenanceAndRepairVm.resetPage" search-term="allMaintenanceAndRepairVm.searchText"
                      on-search="allMaintenanceAndRepairVm.freeTextSearch"
                      filter-search="allMaintenanceAndRepairVm.filterSearch"></free-text-search>
  </div>
  <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
    <div class="responsive-table" style="padding: 10px;">
      <table class="table table-striped highlight-row">
        <thead>
        <tr>
          <th style="width: 150px" translate>NUMBER</th>
          <th style="width: 150px" translate>TYPE</th>
          <th style="width: 150px" translate>NAME</th>
          <th style="width: 150px" translate>DESCRIPTION</th>
          <th style="width: 150px" translate>MODIFIED_BY</th>
          <th style="width: 150px" translate>MODIFIED_DATE</th>
          <th class='added-column'
              style="width: 150px;z-index: auto !important;"
              ng-repeat="selectedAttribute in allMaintenanceAndRepairVm.selectedAttributes">
            {{selectedAttribute.name}}
            <i class="fa fa-times-circle"
               ng-click="allMaintenanceAndRepairVm.removeAttribute(selectedAttribute)"
               title={{allMaintenanceAndRepairVm.RemoveColumnTitle}}></i>
          </th>
          <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
              translate>ACTIONS
          </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="allMaintenanceAndRepairVm.loading == true">
          <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_MACHINES</span>
                        </span>
          </td>
        </tr>
        <tr ng-repeat="maintenanceAndRepair in allMaintenanceAndRepairVm.maintenanceAndRepairs">
          <td style="width: 150px;">
            <a href="" ng-click="allMaintenanceAndRepairVm.showMaintenanceAndRepair(maintenanceAndRepair)"
               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
              <span ng-bind-html="maintenanceAndRepair.number | highlightText: freeTextQuery"></span>
            </a>
          </td>
          <td>{{maintenanceAndRepair.type}}</td>
          <td>{{maintenanceAndRepair.name}}</td>
          <td style="width: 150px;" title="{{maintenanceAndRepair.description}}">
            <span ng-bind-html="maintenanceAndRepair.description  | highlightText: freeTextQuery"></span>
          </td>
          <td>{{maintenanceAndRepair.modifiedByObject.firstName}}</td>
          <td>{{maintenanceAndRepair.modifiedDate}}</td>
          <td class="text-center actions-col sticky-col sticky-actions-col">
            <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
              <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
              <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                  style="z-index: 9999 !important;">
                <li>
                </li>
              </ul>
            </span>
          </td>
        </tr>


        </tbody>
      </table>
    </div>
    <div class="table-footer">
      <table-footer objects="allMaintenanceAndRepairVm.maintenanceAndRepairs" pageable="allMaintenanceAndRepairVm.pageable"
                    previous-page="allMaintenanceAndRepairVm.previousPage" next-page="allMaintenanceAndRepairVm.nextPage"></table-footer>
    </div>
  </div>
</div>
