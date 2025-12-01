<div>
    <style scoped>
        .view-content {
            overflow-y: auto;
        }

        .vaults-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(400px, max-content));
            grid-gap: 20px;
            justify-content: center;
            padding: 30px;
            margin: 0 100px;
        }

        .vaults-container .vault-container {
            flex: 1;
            display: flex;
            border: 1px solid #ddd;
            border-radius: 5px;
            height: 125px;
            max-width: 400px;
            min-width: 350px;
            margin: 20px;
            padding: 15px 15px 10px 15px;
            cursor: pointer;
            box-shadow: 0 3px 6px 0 rgba(0, 0, 0, 0.2);
            background-image: var(--cassini-linear-gradient);
        }

        .vaults-container .vault-container:last-child {
        }

        .vaults-container .vault-container:hover {
            background: #0060df; /* fallback for old browsers */
            background: -webkit-linear-gradient(to top, #003eaa, #0060df); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to top, #003eaa, #0060df); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
            border: 1px solid #0060df;
            color: #fff;
        }

        .vaults-container .vault-container .vault-icon {
            flex: 1;
            max-width: 60px;
        }

        .vaults-container .vault-container .vault-icon img {
            width: 60px;
        }

        .vaults-container .vault-container .vault-icon i {
            font-size: 50px;
            margin-top: -5px;
            color: var(--cassini-font-color);
            opacity: 0.9;
        }

        .vaults-container .vault-container:hover .vault-icon i {
            color: #fff;
        }

        .vaults-container .vault-container .vault-body {
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        .vaults-container .vault-container .vault-body .vault-name {
            font-size: 16px;
            font-weight: 600;
            flex: 1;
            white-space: nowrap;
        }

        .vaults-container .vault-container .vault-body .vault-description {
            flex: 2;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
            line-height: 24px;
        }

        .vaults-container .vault-container .vault-body .vault-footer {
            flex: 1;
            color: #7b8a90;
            display: flex;
            font-size: 12px;
            justify-content: end;
        }

        .vaults-container .vault-container:hover .vault-body .vault-footer {
            color: #fff;
        }

        .vaults-container .vault-container .vault-body .vault-icons {
            display: flex;
            justify-content: end;
        }

        .vaults-container .vault-container .vault-body .vault-icons .dropdown-menu {
            left: calc(100% - 15px);
        }

        .vaults-container .vault-container .vault-body .vault-icons i {
            margin-left: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .vaults-container .vault-container .vault-body .vault-footer div {
            flex: 1;
        }

        div.flex-row {
            display: flex;
            flex-direction: row;
            align-items: flex-start;
            align-content: flex-start
        }

        div.flex-row > div {
            flex: 1;
            align-self: flex-start
        }

        div.metrics-container {
        }

        div.metrics-counts-label {
            max-width: 20px;
            min-height: 20px;
            min-width: 20px;
            max-height: 20px;
            text-align: center;
            line-height: 20px;
            border-radius: 50%;
            color: #fff;
            font-size: 10px;
        }

        div.metrics-counts {
            max-width: 20px;
            min-height: 20px;
            min-width: 20px;
            max-height: 20px;
            line-height: 20px;
            text-align: left;
            font-size: 12px;
            margin-left: 5px;
        }

        div.flex-start {
            justify-content: start;
        }

        div.flex-center {
            justify-content: center;
        }

        div.flex-end {
            justify-content: end;
        }

        div.assemblies {
            background-color: #54a0ff;
        }

        div.parts {
            background-color: #1dd1a1;
        }

        div.drawings {
            background-color: #ff6b6b;
        }

        div.commits {
            background-color: #ffa502;
        }

        .no-vaults-container {
            position: absolute;
            top: 45%;
            left: 50%;
            /* bring your own prefixes */
            transform: translate(-50%, -50%);
        }

        .no-vaults-container .vault-storage-img img {
            width: 400px
        }

        .no-vaults-container .vault-storage-message {
            font-size: 20px;
            font-weight: 300;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }
    </style>

    <div class="view-container" fitcontent ng-show="vaultId === null">
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>VAULTS</span>
            <button class="btn btn-sm new-button"
                    ng-if="hasPermission('pdm_vault','create') || hasPermission('pdmobject','all')"
                    ng-click="allVaultsVm.newVault()" title="{{'NEW_VAULT' | translate}}">
                <i class="las la-plus"></i>
                <span>New Vault</span>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
            <free-text-search on-clear="allVaultsVm.resetPage" search-term="allVaultsVm.searchText"
                              on-search="allVaultsVm.freeTextSearch"
                              filter-search="allVaultsVm.filterSearch"></free-text-search>
        </div>

        <div class="view-content no-padding">
            <div class="vaults-container">
                <div class="no-vaults-container text-center" ng-if="allVaultsVm.vaults.length == 0">
                    <div class="vault-storage-img">
                        <img src="app/assets/no_data_images/cloud-storage.png" alt="">
                    </div>
                    <div class="vault-storage-message">
                        {{'NO_DATA_MESSAGE_IN_VAULTS ' | translate }}
                    </div>
                </div>
                <div ng-repeat="vault in allVaultsVm.vaults" class="vault-container"
                     ng-click="allVaultsVm.showVaultDetails($event, vault.id)">
                    <div class="vault-icon">
                        <i class="lab la-buffer"></i>
                        <%--<img src="app/assets/images/cloud-storage.png" alt="">--%>
                    </div>
                    <div class="vault-body">
                        <div class="flex-row">
                            <div class="vault-name">
                                <span ng-bind-html="vault.name | highlightText: freeTextQuery"></span>
                            </div>
                            <div class="vault-icons text-right mr5 dropdown">
                                <%--<i class="fa fa-edit" title="Edit"></i>--%>
                                <span class="dropdown-toggle" data-toggle="dropdown">
                                    <i class="fa fa-gear" title="Settings"></i>
                                </span>
                                <ul class="dropdown-menu dropdown-menu-right">
                                    <li><a href="" ng-click="allVaultsVm.edit(vault)" translate>EDIT</a></li>
                                    <li><a href="" ng-click="allVaultsVm.deleteVault(vault)" translate>DELETE</a></li>
                                    <%--<li><a href="" ng-click="">Settings</a></li>--%>
                                </ul>
                            </div>
                        </div>
                        <div class="vault-description">
                            <span ng-bind-html="vault.description | highlightText: freeTextQuery"></span>
                        </div>
                        <div class="vault-footer">
                            <div class="metrics-container flex-row flex-start" title="Assemblies">
                                <div class="metrics-counts-label assemblies">A</div>
                                <div class="metrics-counts">{{vault.assemblasCount}}</div>
                            </div>
                            <div class="metrics-container flex-row flex-start" title="Parts">
                                <div class="metrics-counts-label parts">P</div>
                                <div class="metrics-counts">{{vault.partsCount}}</div>
                            </div>
                            <div class="metrics-container flex-row flex-start" title="Drawings">
                                <div class="metrics-counts-label drawings">D</div>
                                <div class="metrics-counts">{{vault.drawingsCount}}</div>
                            </div>
                            <%--<div class="metrics-container flex-row flex-end" title="Commits">
                                <div class="metrics-counts-label commits">C</div>
                                <div class="metrics-counts">{{vault.commitsCount}}</div>
                            </div>--%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div ui-view ng-show="vaultId !== null"></div>
</div>