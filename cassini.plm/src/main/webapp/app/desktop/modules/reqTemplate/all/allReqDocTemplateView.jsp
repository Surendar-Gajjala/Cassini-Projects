<style scoped>
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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
              translate>REQ_DOC_TEMPLATES</span>

        <button class="btn btn-sm new-button" ng-click="reqDocTemplateVm.newReqDocTemplate()" id="newReqDocTemplate"
                title="{{'NEW_REQ_DOC' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_TEMPLATE_TITLE' | translate}}</span>
        </button>


        <free-text-search on-clear="reqDocTemplateVm.resetPage" search-term="reqDocTemplateVm.searchText"
                          on-search="reqDocTemplateVm.freeTextSearch"
                          filter-search="reqDocTemplateVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-200" translate>TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>CREATED_BY</th>
                    <th style="width: 150px" translate>CREATED_DATE</th>

                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="reqDocTemplateVm.loading == true">
                    <td colspan="25">
            <span style="font-size: 15px;">
              <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                   class="mr5">
              <span translate>LOADING_REQ_DOC_TEMPLATES</span>
            </span>
                    </td>
                </tr>

                <tr ng-if="reqDocTemplateVm.loading == false && reqDocTemplateVm.reqDocTemplates.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Documents.png" alt="" class="image">

                            <div class="message" >{{ 'NO_REQ_DOC_TEMPLATES' | translate }}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-if="reqDocTemplateVm.loading == false && reqDocTemplateVm.reqDocTemplates.content.length > 0"
                    ng-repeat="template in reqDocTemplateVm.reqDocTemplates.content">

                    <td id="name" class="col-width-200">
                        <a ng-click="reqDocTemplateVm.showReqDocTemplate(template)"
                           title="{{reqDocTemplateVm.openTemplateTitle}}">
                            <span ng-bind-html="template.name | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="template.type.name"></span>
                    </td>
                    <td id="description" class="col-width-300"><span
                            ng-bind-html="(template.description) | highlightText: freeTextQuery"
                            title="{{template.description}}"></span>
                    </td>
                    <td>{{template.createdByObject.fullName}}</td>
                    <td>{{template.createdDate}}</td>
                    <td class="text-center">
                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="hasPermission('template','delete')"
                                    ng-click="reqDocTemplateVm.deleteReqDocTemplate(template)"><a translate>DELETE_TEMPLATE</a>
                                </li>
                                <plugin-table-actions context="reqDoc.all" object-value="template"></plugin-table-actions>
                            </ul>
                         </span>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="reqDocTemplateVm.reqDocTemplates" pageable="reqDocTemplateVm.pageable"
                          page-size="reqDocTemplateVm.pageSize"
                          previous-page="reqDocTemplateVm.previousPage"
                          next-page="reqDocTemplateVm.nextPage"></table-footer>
        </div>
    </div>
</div>

