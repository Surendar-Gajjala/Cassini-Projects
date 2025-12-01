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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PROGRAMS</span>

        <button class="btn btn-sm new-button" ng-if="hasPermission('program','create')"
                ng-click="allProgramsVm.newProgram()"
                title="{{'NEW_PROGRAM' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_PROGRAM' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm" ng-click="allProgramsVm.viewType='table';allProgramsVm.setViewType('table')"
                    title="Table view" ng-if="allProgramsVm.viewType == 'cards'">
                <i class="las la-th"></i>
            </button>
            <button class="btn btn-sm" ng-click="allProgramsVm.viewType='cards';allProgramsVm.setViewType('cards')"
                    title="Card view" ng-if="allProgramsVm.viewType == 'table'">
                <i class="las la-border-all"></i>
            </button>
            <button class="btn btn-sm btn-maroon" ng-if="allProgramsVm.viewType == 'table'"
                    ng-click="allProgramsVm.showTypeAttributes()" title="{{allProgramsVm.attributeTitle}}">
                <i class="fa fa-newspaper-o"></i>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>


        </div>

        <free-text-search on-clear="allProgramsVm.resetPage" search-term="allProgramsVm.searchText"
                          on-search="allProgramsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;padding: 10px;">
        <div ng-if="allProgramsVm.viewType == 'table'"
             ng-include="'app/desktop/modules/pm/program/all/programsTableView.jsp'"></div>
        <div ng-if="allProgramsVm.viewType == 'cards'"
             ng-include="'app/desktop/modules/pm/program/all/programsCardView.jsp'"></div>
        <div class="table-footer">
            <table-footer objects="allProgramsVm.programs" pageable="allProgramsVm.pageable"
                          previous-page="allProgramsVm.previousPage"
                          next-page="allProgramsVm.nextPage" page-size="allProgramsVm.pageSize"></table-footer>
        </div>
    </div>
</div>
