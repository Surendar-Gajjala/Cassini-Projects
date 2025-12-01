<div>
    <style scoped>
        .view-content {
            position: relative;
        }

        .view-content .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 0px;
            top: 0;
            overflow: auto;
        }

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .select-item td {
            background-color: #ddd !important;
            /*color: white !important;*/
        }

        .split-pane-component .responsive-table {
            padding: 0;
            position: absolute;
            bottom: 5px;
            top: 0;
            overflow: auto;
        }

        .split-pane-component .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            /*background-color: #fff;*/
        }

        .split-pane-component .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px !important;
            height: 40px;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .split-pane-component .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .split-pane-component .table-footer > div h5 {
            margin: 0;
        }

        .split-pane-component .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .split-pane-component .table-footer > div > div > i {
            font-size: 16px;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

        .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
            /*background-color: #fff;*/
        }

        .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
            background-color: #d6e1e0;
        }

        #freeTextSearchDirective ::-webkit-input-placeholder {
            /**/
            font-weight: bold;
        }

        #freeTextSearchDirective :-moz-placeholder {
            /**/
            font-weight: bold;
        }

        i.activeView {
            text-decoration: none;
            font-weight: bold;
            color: #467aff;
            font-size: 16px !important;

        }

        #classificationContainer .search-input i.fa-search {
            position: absolute;
            margin-top: 13px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
            font-size: 12px;
        }

        #classificationContainer .search-input .search-form {
            padding-left: 25px;
            padding-right: 25px;
        }

        .search-form {
            border-radius: 3px;
            background-color: #eaeaea;
            border: 1px solid #fff;
        }

        .search-form:focus {
            box-shadow: none;
            border: 1px solid #c5cfd5;
        }

        #classificationContainer .search-input .search-form {
            padding-left: 25px;
            padding-right: 25px;
        }

        i.clear-search {
            margin-left: -24px;
            color: #aab4b7;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: 8px;
        }

        .bom-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .bom-model .configurationEditor-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .dm-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .dm-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
            height: calc(100% - 100px);
        }

        .dm-bottom {
            padding: 5px;
            border-top: 1px solid lightgrey;
            height: 50px;
        }

        .config-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .config-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close:before, .config-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .config-close:before {
            transform: rotate(45deg) !important;
        }

        .config-close:after {
            transform: rotate(-45deg) !important;
        }

        .configuration-header {
            font-weight: bold;
            font-size: 22px;
            display: inline-block;
            margin-top: 7px;
        }

        .dm-content table th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }
        .config-close-btn {
            position: absolute;
            right: 50px;
            top: 7px;
            width: 32px;
            height: 32px;
            opacity: 0.3;
        }
        .item-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

        .config-close-btn:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close-btn:before, .config-close-btn:after {
            position: absolute;
            top: 9px;
            left: 15px;
            content: ' ';
            height: 15px;
            width: 2px;
            background-color: #333;
        }

        .config-close-btn:before {
            transform: rotate(45deg) !important;
        }

        .config-close-btn:after {
            transform: rotate(-45deg) !important;
        }
        #configuration-error {
            display: none;
            padding: 11px !important;
            margin-bottom: 0 !important;
            width: 100%;
            height: 50px;
            float: left;
            position: relative;
        }
        .user-node {
            background: transparent url("app/assets/images/user1.png") no-repeat !important;
            height: 16px;
        }
        .users-node {
            background: transparent url("app/assets/images/users.png") no-repeat !important;
            height: 16px;
        }

    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">Sharing Objects</span>
         
            <button class="btn btn-sm btn-info" ng-if="allSharedToVm.person != null && allSharedToVm.sharedCounts.item > 0 "
                ng-class="{'activeView':allSharedToVm.itemsView == true}" ng-click='allSharedToVm.sharedAllItems()'
                title="Items">
                 <i ng-class="{'activeView':itemsView == true}" class="fa fa-cogs" style=""></i>
              </button>
              <button class="btn btn-sm btn-info" ng-if="allSharedToVm.person != null && allSharedToVm.sharedMfrs > 0"
                ng-class="{'activeView':allSharedToVm.mfrView == true}" ng-click='allSharedToVm.sharedMfr()'
                title="Manufacturer">
                <i ng-class="{'activeView':mfrView == true}" class="fa flaticon-office42" style=""></i>
                </button>
                <button class="btn btn-sm btn-info" ng-if="allSharedToVm.person != null && allSharedToVm.sharedCounts.mfrPart > 0"
                    ng-class="{'activeView':allSharedToVm.mfrPartView == true}" ng-click='allSharedToVm.sharedMfrPart()'
                    title="Manufacturer Parts">
                <i ng-class="{'activeView':mfrPartView == true}" class="fa fa-cubes" style=""></i>
            </button>
            <button class="btn btn-sm btn-info" ng-if="allSharedToVm.person != null && allSharedToVm.sharedCounts.program > 0"
                     ng-click='allSharedToVm.sharedProgramObjects()'
                  title="Programs">
                  <i ng-class="{'activeView':programsView == true}" class="fa fa-calendar-check-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-info" ng-if="allSharedToVm.person != null && allSharedToVm.sharedCounts.supplier > 0"
                ng-class="{'activeView':allSharedToVm.supplierView == true}" ng-click='allSharedToVm.sharedSupplier()'
                title="Suppliers">
               <i ng-class="{'activeView':supplierView == true}" class="fa flaticon-office42" style=""></i>
            </button>
            <button class="btn btn-sm btn-info" ng-if="allSharedToVm.person != null && allSharedToVm.sharedCounts.folder > 0">
                <span class="item-notification" ng-class="{'activeView':allSharedToVm.folderView == true}"
                      ng-click='allSharedToVm.sharedFolder()'
                      title="Folders">
                    <i ng-class="{'activeView':folderView == true}" class="fa fa-folder-open-o" style=""></i>
                </span>
            </button>
    
            <button class="btn btn-sm btn-info" ng-if="allSharedToVm.person != null && allSharedToVm.sharedCounts.declaration > 0">
                <a class="item-notification" ng-class="{'activeView':allSharedToVm.declarationView == true}"
                   ng-click='allSharedToVm.sharedDeclaration()' title="Declarations">
                    <i class="fa fa-newspaper-o" style=""></i>
                </a>
            </button>
    
            <button class="btn btn-sm btn-info"
                    ng-if="allSharedToVm.person != null && allSharedToVm.sharedCounts.project > 0 || allSharedToVm.sharedCounts.activity > 0 || allSharedToVm.sharedCounts.task > 0">
                <a class="item-notification" ng-class="{'activeView':allSharedToVm.projectView == true}"
                   ng-click='allSharedToVm.sharedProjectsObjects()' title="Projects">
                    <i ng-class="{'activeView':projectView == true}" class="fa fa-calendar" style=""></i>
                </a>
            </button>

            <div class="btn-group">
               
            </div>
            
        </div>
        <div class="view-content no-padding">
            <div class="split-pane fixed-left">
                <div class="split-pane-component split-left-pane"
                     style="padding: 0;min-width: 250px;max-width: 250px;overflow: auto;">
                    <div id="classificationContainer" class="classification-pane" data-toggle="context"
                         data-target="#context-menu">
                        <div class="search-input" style="height: 30px;margin: 10px 10px 20px 10px;width: 230px;">
                            <i class="fa fa-search"></i>
                            <input type="search" class="form-control input-sm search-form"
                                   placeholder={{searchTitle}}
                                   ng-model="allSharedToVm.searchValue"
                                   ng-change="allSharedToVm.searchTree()">
                            <i class="las la-times-circle clear-search"
                               ng-show="allSharedToVm.searchValue.length > 0"
                               ng-click="allSharedToVm.searchValue = '';allSharedToVm.searchTree()"></i>
                        </div>
                        <ul id="sharingObjectTree" class="easyui-tree classification-tree">
                        </ul>
                    </div>
                </div>
                <div class="split-pane-divider" style="left: 250px;z-index: 1;">
                </div>
                <div class="split-pane-component split-right-pane noselect"
                     style="left:250px;padding: 0;overflow-y: hidden;">
                     
                     <div
                     ng-include="'app/desktop/modules/sharing/all/sharedView.jsp'"
                     ng-controller="SharedController as sharedVm"></div>
                     
                </div>
            </div>
        </div>
    </div>
</div>