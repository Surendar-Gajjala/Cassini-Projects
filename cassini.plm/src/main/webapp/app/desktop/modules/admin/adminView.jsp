<div>
    <style scoped>
        .view-toolbar .switch-toggle {
            max-width: 350px;
            margin-left: auto;
            margin-right: auto;
            margin-top: -32px;
        }

        .view-toolbar #freeTextSearchDirective {
            top: 6px !important;
            width: 200px !important;
        }

        .view-toolbar #freeTextSearchDirective #freeTextSearchInput {
            width: 195px;
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

        .module-dropdow {
            width: 250px;
            position: absolute;
            right: 230px;
            top: 10px;
        }
    </style>

    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>ADMIN</span>

            <button class="btn btn-sm new-button" ng-click="newUser()" title="{{ 'NEW_USER' | translate }}"
                    ng-if="adminVm.view === 'users'">
                <i class="las la-plus" aria-hidden="true"></i>
                <span translate>NEW_USER</span>
            </button>
            <button class="btn btn-sm new-button" ng-click="newRole()" title="{{ 'NEW_ROLE' | translate }}"
                    ng-if="adminVm.view === 'roles'"
                    ng-click="adminVm.showRoles()">
                <i class="las la-plus" aria-hidden="true"></i>
                <span translate>NEW_ROLE</span>
            </button>
            <button class="btn btn-sm" ng-click="adminVm.viewType='table';adminVm.setViewType('table');"
                    title="Table view"
                    ng-if="adminVm.viewType == 'cards' && (adminVm.view === 'roles' || adminVm.view === 'users')">
                <i class="las la-th"></i>
            </button>
            <button class="btn btn-sm" ng-click="adminVm.viewType='cards';adminVm.setViewType('cards');"
                    title="Card view"
                    ng-if="adminVm.viewType == 'table' && (adminVm.view === 'roles' || adminVm.view === 'users')">
                <i class="las la-border-all"></i>
            </button>
            <button class="btn btn-sm new-button" ng-click="newSecurityPermission()"
                    title="{{ 'NEW_PERMISSION' | translate }}"
                    ng-if="adminVm.view === 'permissions'">
                <i class="las la-plus" aria-hidden="true"></i>
                <span translate>NEW_PERMISSION</span>
            </button>

            <div class="switch-toggle switch-candy">
                <input id="users" name="adminViewType" type="radio" value="users"
                       ng-model="adminVm.view"
                       ng-click="adminVm.showUsers()">
                <label for="users" onclick="" translate>USERS</label>

                <input id="groups" name="adminViewType" type="radio" value="roles"
                       ng-model="adminVm.view"
                       ng-click="adminVm.showRoles()">
                <label for="groups" onclick="" translate>ROLES</label>

                <input id="permissions" name="adminViewType" type="radio"
                       ng-model="adminVm.view"
                       value="permissions"
                       ng-click="adminVm.showPermissions()">
                <label for="permissions" onclick="" translate>PERMISSIONS</label>
                <a href=""></a>
            </div>

            <div class="module-dropdow" ng-if="adminVm.view === 'permissions'">
                <div style="width: 200px;">
                    <ui-select ng-model="adminVm.module"
                               on-select="loadModulePermissions(adminVm.module)">
                        <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices repeat="type in moduleTypes | filter: $select.search">
                            <div ng-bind="type"></div>
                        </ui-select-choices>
                    </ui-select>

                </div>
                <div style="position: absolute;top: 0;margin-left: 205px;">
                    <button class="btn-danger btn-sm" ng-if="moduleSelected == true"
                            ng-click="adminVm.clearModule()" translate>CLEAR
                    </button>
                </div>
            </div>

            <free-text-search on-clear="adminVm.resetPage" search-term="adminVm.filters.searchQuery"
                              on-search="adminVm.freeTextSearch"></free-text-search>
        </div>

        <div ui-view class="view-content no-padding" style="overflow-y: auto;"></div>
    </div>
</div>
