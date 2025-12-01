<style>
    .added-column {
        text-align: left;
        width: 150px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
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

    /* The Close Button */
    .img-model .closeImage,
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .project-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

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

    .modal-header {
        padding: 9px;
        border-bottom: 1px solid #e5e5e5;
    }

    .close {
        float: right;
        font-size: 21px;
        font-weight: 700;
        line-height: 1;
        color: #000;
        text-shadow: 0 1px 0 #fff;
        filter: alpha(opacity=20);
        opacity: .2;
    }

    .btn-group .btn + .btn, .btn-group .btn + .btn-group, .btn-group .btn-group + .btn, .btn-group .btn-group + .btn-group {
        margin-left: -2px;
    }

    .responsive-table .dropdown-content {
        margin-left: -78px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .center {
        display: block;
        margin-left: auto;
        margin-right: auto;
        margin-top: 4%;
        width: 300px;
    }

    .no-conversations .no-conversations-message {
        font-size: 20px;
        font-weight: 300 !important;
        text-align: center;
    }


</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PROJECTS</span>

        <button class="btn btn-sm new-button" ng-if="hasPermission('project','create')"
                ng-click="allProjectsVm.showNewProjectDialog()"
                title="{{'NEW_PROJECT' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'PROJECT' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm" ng-click="allProjectsVm.viewType='table';allProjectsVm.setViewType('table')"
                    title="Table view" ng-if="allProjectsVm.viewType == 'cards'">
                <i class="las la-th"></i>
            </button>
            <button class="btn btn-sm" ng-click="allProjectsVm.viewType='cards';allProjectsVm.setViewType('cards')"
                    title="Card view" ng-if="allProjectsVm.viewType == 'table'">
                <i class="las la-border-all"></i>
            </button>
            <button class="btn btn-sm btn-maroon" ng-if="allProjectsVm.viewType == 'table'"
                    ng-click="allProjectsVm.showTypeAttributes()" title="{{allProjectsVm.attributeTitle}}">
                <i class="fa fa-newspaper-o"></i>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>

        </div>

        <%--<p ng-show="searchModeType == true;"
           style="color: #0390fd;margin-top: -28px;margin-left: 700px; font-size: 16px;" translate>ALL_VIEW_ALERT</p>--%>


        <%--<input class="form-control" type="file" ng-file-model="file">--%>

        <free-text-search on-clear="allProjectsVm.resetPage" search-term="allProjectsVm.searchText"
                          on-search="allProjectsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;padding: 10px;">
        <div ng-if="allProjectsVm.viewType == 'table'"
             ng-include="'app/desktop/modules/pm/project/all/projectsTableView.jsp'"></div>
        <div ng-if="allProjectsVm.viewType == 'cards'"
             ng-include="'app/desktop/modules/pm/project/all/projectsCardView.jsp'"></div>
        <div class="table-footer">
            <table-footer objects="allProjectsVm.projects" pageable="allProjectsVm.pageable"
                          previous-page="allProjectsVm.previousPage"
                          next-page="allProjectsVm.nextPage" page-size="allProjectsVm.pageSize"></table-footer>
            <%--<div style="height: 8px;">
                <h5 style="padding: 10px 0;margin: 0;">
                    <span style="padding-right: 5px" > {{ 'TOTAL_NUMBER_OF_PROJECTS' | translate }} : {{allProjectsVm.projects.numberOfElements}} </span>
                </h5>
            </div>--%>
        </div>
    </div>
</div>
