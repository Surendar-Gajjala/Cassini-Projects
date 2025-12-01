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
        bottom: 0;
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

    .dropdown-content {
        position: fixed !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    .responsive-table .dropdown-content {
        margin-left: 78px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }


</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PROGRAM_TEMPLATES</span>

        <button class="btn btn-sm new-button" ng-click="allProgramTemplatesVm.showNewTemplate()" id="newTemplate"
                title="{{allProgramTemplatesVm.newTemplateTitle}}" ng-if="hasPermission('programtemplate','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_TEMPLATE' | translate}}</span>
        </button>

        <free-text-search on-clear="allProgramTemplatesVm.resetPage" search-term="allProgramTemplatesVm.searchText"
                          on-search="allProgramTemplatesVm.freeTextSearch"
                          filter-search="allProgramTemplatesVm.filterSearch"></free-text-search>

    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-300" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>CREATED_BY</th>
                    <th style="width: 150px" translate>CREATED_DATE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class="actions-column" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allProgramTemplatesVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_TEMPLATES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allProgramTemplatesVm.loading == false && allProgramTemplatesVm.programTemplates.content.length == 0">

                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Templates.png" alt="" class="image">

                            <div class="message" translate>NO_TEMPLATES</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="template in allProgramTemplatesVm.programTemplates.content">
                    <td id="name" class="col-width-200">
                        <a ng-click="allProgramTemplatesVm.showTemplate(template)"
                           title="{{allProgramTemplatesVm.openTemplateTitle}}">
                            <span ng-bind-html="template.name | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td id="description" class="col-width-300"><span
                            ng-bind-html="(template.description) | highlightText: freeTextQuery"
                            title="{{template.description}}"></span>
                    </td>
                    <td>{{template.createdByObject.fullName}}</td>
                    <td>{{template.createdDate}}</td>
                    <td>{{template.modifiedByObject.fullName}}</td>
                    <td>{{template.modifiedDate}}</td>
                    <td class="text-center">
                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="hasPermission('programtemplate','delete')"
                                    ng-disabled="template.percentComplete > 0"
                                    ng-click="allProgramTemplatesVm.deleteTemplate(template)"><a translate>DELETE_TEMPLATE</a>
                                </li>
                                <plugin-table-actions context="projectTemplate.all" object-value="template"></plugin-table-actions>
                            </ul>
                         </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allProgramTemplatesVm.programTemplates" pageable="allProgramTemplatesVm.pageable"
                          previous-page="allProgramTemplatesVm.previousPage"
                          next-page="allProgramTemplatesVm.nextPage" page-size="allProgramTemplatesVm.pageSize"></table-footer>
        </div>
    </div>
</div>
