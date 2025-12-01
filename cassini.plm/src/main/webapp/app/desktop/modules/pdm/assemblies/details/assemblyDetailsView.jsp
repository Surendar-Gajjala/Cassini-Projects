<div>
    <style scoped>
        .assembly-bom-container {
            position: absolute;
            top: 51px;
            bottom: 0;
            left: 0;
            width: 40%;
            border-right: 1px solid #ddd;
            overflow-y: auto;
        }

        .assembly-bom-container .responsive-table {
            /*height: 100%;*/
        }

        .assembly-bom-container .bom-table {

        }

        .assembly-bom-container .bom-table .parent-row {
            font-weight: bold;
        }

        .assembly-bom-container .bom-table .level-1 {
            padding-left: 20px !important;
        }

        .assembly-bom-container .bom-table .level-2 {
            padding-left: 40px !important;
        }

        .assembly-bom-container .bom-table .level-3 {
            padding-left: 65px !important;
        }

        .assembly-bom-container .bom-table .level-4 {
            padding-left: 80px !important;
        }

        .assembly-bom-container .bom-table .level-5 {
            padding-left: 100px !important;
        }

        .assembly-bom-container .bom-table .level-6 {
            padding-left: 120px !important;
        }

        .assembly-bom-container .bom-table .level-7 {
            padding-left: 140px !important;
        }

        .assembly-bom-container .bom-table .level-8 {
            padding-left: 160px !important;
        }

        .assembly-bom-container .bom-table .level-9 {
            padding-left: 180px !important;
        }

        .assembly-bom-container .bom-table .level-10 {
            padding-left: 200px !important;
        }

        .assembly-bom-container .bom-table .bom-name-col {
            max-width: 500px !important;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
        }

        .assembly-bom-container .bom-table .bom-name-col .bomitem-icon {
            width: 16px;
            height: 16px;
        }

        .assembly-bom-container .bom-table .bom-revision-col {
            width: 100px;
            text-align: center;
        }

        .assembly-bom-container .bom-table .bom-revision-col {
            width: 150px;
        }

        .assembly-bom-container .bom-table .bom-quantity-col {
            width: 100px;
            text-align: center;
        }

        .assembly-bom-container .bom-table .expand-collapse {
            cursor: pointer;
        }

        .assembly-bom-container .bom-table .part-name {
            margin-left: 13px
        }

        .assembly-bom-container .bom-table tbody tr.selected td {
            background-image: linear-gradient(to bottom, #3498db, #2980b9) !important;
            color: #fff !important;
        }

        .assembly-bom-container .bom-table tbody tr.selected td:last-child {
            border-right: 1px solid #ddd;
        }

        .mr-5 {
            margin-right: 2px;
        }

        .mr-2 {
            margin-right: 2px;
        }

        .view-toolbar .name-label {
            position: relative;
            top: 2px;
        }

        .view-toolbar .version-label {
            margin-left: 10px;
            background-color: #0682f0;
            padding: 5px 10px;
            /*border-radius: 3px;*/
            color: #fff;
            font-size: 12px;
            position: relative;
        }

        .display-table {
            display: inline-table;
        }

        .display-inline-table {
            display: inline-table;
        }

        .display-row {
            display: table-row;
        }

        .display-cell {
            display: table-cell;
        }

        .view-toolbar .view-header {
            font-weight: 600;
            font-size: 20px;
            padding-right: 10px;
            border-right: 1px solid #ddd;
            margin-top: 3px;
        }

        .assembly-details-container {
            position: absolute;
            margin-left: 40%;
            top: 51px;
            bottom: 0;
            left: 0;
            width: 60%;
            border-right: 1px solid #ddd;
            padding: 0;
        }

        .assembly-details-container ul.nav-tabs {
            border-radius: 0;
            border-top: 0;
            border-left: 0;
            border-right: 0;
        }

        .assembly-details-container ul.nav-tabs > li {
            min-width: 120px;
            text-align: center;
        }

        .assembly-details-container .tab-content {
            border: 0;
            overflow-y: auto;
            padding: 0;
        }

        .view-toolbar .path-label {
            position: absolute;
            right: 0;
            top: 13px;
        }

        .placeholder-container {
            position: absolute;
            top: 45%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .placeholder-container .placeholder-img img {
            width: 300px
        }

        .placeholder-container .placeholder-message {
            font-size: 20px;
            font-weight: 300;
        }

    </style>

    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <div class="display-table view-header">
                <div class="display-row">
                    <div class="display-cell">
                        <span class="name-label">
                            {{assemblyDetailsVm.assembly.name}}
                        </span>
                    </div>
                    <div class="display-cell" uib-dropdown dropdown-append-to-body>
                        <span class="version-label" uib-dropdown-toggle>
                            {{assemblyDetailsVm.assembly.revision}}.{{assemblyDetailsVm.assembly.version}}
                            <i class="fa fa-caret-down" style="margin-left: 5px;"></i>
                        </span>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu"
                            style="z-index: 9999 !important; min-width: 100px;max-height: 250px;overflow-y: auto;margin-left: 10px;">
                            <li ng-repeat="fileVersion in assemblyDetailsVm.fileVersions">
                                <a href="" ui-sref="app.pdm.assemblies.details({assemblyId: fileVersion.attachedTo})">{{fileVersion.attachedToRevision}}.{{fileVersion.version}}</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="display-table">
                <button class="display-cell btn btn-sm"
                        title="Auto generate part numbers"
                        ng-click="assemblyDetailsVm.assignPartNumbersToAssembly();">
                    <i class="las la-stream"></i>
                </button>

                <!--
                <button class="display-cell btn btn-sm"
                        title="Create EBOM"
                        ng-click="assemblyDetailsVm.createEBOM();">
                    <i class="las la-project-diagram"></i>
                </button>
                -->
            </div>

            <div class="path-label" ng-if="hasPermission('pdm_vault','all') || hasPermission('pdm_vault','view')">
                <span style="font-size: 20px;padding-right: 10px;font-weight: 300;"
                      ng-bind-html="assemblyDetailsVm.breadCrumbs"></span>
            </div>
        </div>

        <div class="view-content no-padding">
            <div class="assembly-bom-container">
                <div class="responsive-table">
                    <table class="table table-striped table-condensed bom-table highlight-row">
                        <thead>
                        <th class="bom-name-col">
                            <i class="mr-5 fa fa-caret-right expand-collapse"
                               ng-if="!assemblyDetailsVm.expanded"
                               ng-click="assemblyDetailsVm.toggleAllItems()"></i>
                            <i class="mr-2 fa fa-caret-down expand-collapse" ng-if="assemblyDetailsVm.expanded"
                               ng-click="assemblyDetailsVm.toggleAllItems()"></i>
                            <span>Name</span>
                        </th>
                        <th class="bom-revision-pn">Part Number</th>
                        <th class="bom-revision-col">Revision.Version</th>
                        <th class="bom-quantity-col">Quantity</th>
                        </thead>
                        <tbody>
                        <tr class="parent-row"
                            ng-class="{'selected': assemblyDetailsVm.assembly.selected}"
                            ng-click="assemblyDetailsVm.loadSelectedItem(assemblyDetailsVm.assembly)">
                            <td class="bom-name-col level-0">
                                <span><img class="bomitem-icon" src="app/assets/images/sldasm.png" alt=""></span>
                                <span>{{assemblyDetailsVm.assembly.name}}</span>
                            </td>
                            <td class="bom-revision-pn">
                                <a href=""
                                   ui-sref="app.items.details({itemId: assemblyDetailsVm.assembly.master.plmItem.latestRevision})"
                                   ng-click="$event.stopPropagation()">
                                    {{assemblyDetailsVm.assembly.master.plmItem.itemNumber}}
                                </a>
                            </td>
                            <td class="bom-revision-col">
                                {{assemblyDetailsVm.assembly.revision}}.{{assemblyDetailsVm.assembly.version}}
                            </td>
                            <td class="bom-quantity-col">1</td>
                        </tr>

                        <tr ng-repeat="bomItem in assemblyDetailsVm.bomItems"
                            ng-class="{'selected': bomItem.selected}"
                            ng-click="assemblyDetailsVm.loadSelectedItem(bomItem)">
                            <td class="bom-name-col level-{{bomItem.level}}">
                                    <span ng-if="bomItem['@type'] == 'AssemblyDTO'">
                                        <i class="mr-5 fa fa-caret-right expand-collapse"
                                           ng-if="!bomItem.expanded"
                                           ng-click="assemblyDetailsVm.toggleBomItem($event, bomItem)"></i>
                                        <i class="mr-2 fa fa-caret-down expand-collapse" ng-if="bomItem.expanded"
                                           ng-click="assemblyDetailsVm.toggleBomItem($event, bomItem)"></i>

                                        <span><img class="bomitem-icon" src="app/assets/images/sldasm.png"
                                                   alt=""></span>
                                        <span>{{bomItem.assembly.name}}</span>
                                    </span>
                                    <span class="part-name" ng-if="bomItem['@type'] == 'PartDTO'">
                                        <span><img class="bomitem-icon" src="app/assets/images/sldprt.png"
                                                   alt=""></span>
                                        <span>{{bomItem.part.name}}</span>
                                    </span>
                            </td>
                            <td class="bom-revision-pn">
                                    <span ng-if="bomItem['@type'] == 'AssemblyDTO'">
                                        <a href=""
                                           ui-sref="app.items.details({itemId: bomItem.assembly.master.plmItem.latestRevision})"
                                           ng-click="$event.stopPropagation()">
                                            {{bomItem.assembly.master.plmItem.itemNumber}}
                                        </a>
                                    </span>
                                    <span ng-if="bomItem['@type'] == 'PartDTO'">
                                        <a href=""
                                           ui-sref="app.items.details({itemId: bomItem.part.master.plmItem.latestRevision})"
                                           ng-click="$event.stopPropagation()">
                                            {{bomItem.part.master.plmItem.itemNumber}}
                                        </a>
                                    </span>
                            </td>
                            <td class="bom-revision-col">
                                <span ng-if="bomItem['@type'] == 'AssemblyDTO'">{{bomItem.assembly.revision}}.{{bomItem.assembly.version}}</span>
                                <span ng-if="bomItem['@type'] == 'PartDTO'">{{bomItem.part.revision}}.{{bomItem.part.version}}</span>
                            </td>
                            <td class="bom-quantity-col">{{bomItem.quantity}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="assembly-details-container">
                <div class="placeholder-container text-center"
                     ng-if="!assemblyDetailsVm.loading && assemblyDetailsVm.selectedItem == null">
                    <div class="placeholder-img">
                        <img src="app/assets/images/select-item.jpg" alt="">
                    </div>
                    <div class="placeholder-message">
                        Select an item to view details.
                    </div>
                </div>
                <uib-tabset active="assemblyDetailsVm.activeTab">
                    <uib-tab heading="{{assemblyDetailsVm.tabs.basic.heading}}"
                             active="assemblyDetailsVm.tabs.basic.active"
                             select="assemblyDetailsVm.tabSelected(assemblyDetailsVm.tabs.basic.id)">
                        <div ng-include="assemblyDetailsVm.tabs.basic.template"
                             ng-controller="AssemblyDetailsBasicController as basicVm"></div>
                    </uib-tab>

                    <uib-tab heading="{{assemblyDetailsVm.tabs.drawing.heading}}"
                             active="assemblyDetailsVm.tabs.drawing.active"
                             select="assemblyDetailsVm.tabSelected(assemblyDetailsVm.tabs.drawing.id)">
                        <div ng-include="assemblyDetailsVm.tabs.drawing.template"
                             ng-controller="AssemblyDrawingController as drawingVm"></div>
                    </uib-tab>

                    <uib-tab heading="{{assemblyDetailsVm.tabs.visualization.heading}}"
                             active="assemblyDetailsVm.tabs.visualization.active"
                             select="assemblyDetailsVm.tabSelected(assemblyDetailsVm.tabs.visualization.id)">
                        <div ng-include="assemblyDetailsVm.tabs.visualization.template"
                             ng-controller="AssemblyDetailsVisualizationController as visualizationVm"></div>
                    </uib-tab>

                    <uib-tab heading="{{assemblyDetailsVm.tabs.timeline.heading}}"
                             active="assemblyDetailsVm.tabs.timeline.active"
                             select="assemblyDetailsVm.tabSelected(assemblyDetailsVm.tabs.timeline.id)">
                        <div ng-include="assemblyDetailsVm.tabs.timeline.template"
                             ng-controller="TimelineController as timelineVm"></div>
                    </uib-tab>
                </uib-tabset>
            </div>
        </div>
    </div>
</div>