<div>
    <style scoped>
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

        .part-details-container {
            position: absolute;
            margin-left: 0;
            top: 51px;
            bottom: 0;
            left: 0;
            width: 100%;
            border-right: 1px solid #ddd;
            padding: 0;
        }

        .part-details-container ul.nav-tabs {
            border-radius: 0;
            border-top: 0;
            border-left: 0;
            border-right: 0;
        }

        .part-details-container ul.nav-tabs > li {
            min-width: 120px;
            text-align: center;
        }

        .part-details-container .tab-content {
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
                            {{partDetailsVm.part.name}}
                        </span>
                    </div>
                    <div class="display-cell" uib-dropdown dropdown-append-to-body>
                        <span class="version-label" uib-dropdown-toggle>
                            {{partDetailsVm.part.revision}}.{{partDetailsVm.part.version}}
                            <i class="fa fa-caret-down" style="margin-left: 5px;"></i>
                        </span>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu"
                            style="z-index: 9999 !important; min-width: 100px;max-height: 250px;overflow-y: auto;margin-left: 10px;">
                            <li ng-repeat="fileVersion in partDetailsVm.fileVersions">
                                <a href="" ng-click="partDetailsVm.showPartDetails(fileVersion);">{{fileVersion.attachedToRevision}}.{{fileVersion.version}}</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="path-label" ng-if="hasPermission('pdm_vault','all') || hasPermission('pdm_vault','view')">
                <span style="font-size: 20px;padding-right: 10px;font-weight: 300;"
                      ng-bind-html="partDetailsVm.breadCrumbs"></span>
            </div>
        </div>

        <div class="view-content no-padding">
            <div class="part-details-container">
                <div class="placeholder-container text-center"
                     ng-if="!partDetailsVm.loading && partDetailsVm.selectedItem == null">
                    <div class="placeholder-img">
                        <img src="app/assets/images/select-item.jpg" alt="">
                    </div>
                    <div class="placeholder-message">
                        Select an item to view details.
                    </div>
                </div>
                <uib-tabset active="partDetailsVm.activeTab">
                    <uib-tab heading="{{partDetailsVm.tabs.basic.heading}}" active="partDetailsVm.tabs.basic.active"
                             select="partDetailsVm.tabSelected(partDetailsVm.tabs.basic.id)">
                        <div ng-include="partDetailsVm.tabs.basic.template"
                             ng-controller="PartDetailsBasicController as basicVm"></div>
                    </uib-tab>

                    <uib-tab heading="{{partDetailsVm.tabs.drawing.heading}}"
                             active="partDetailsVm.tabs.drawing.active"
                             select="partDetailsVm.tabSelected(partDetailsVm.tabs.drawing.id)">
                        <div ng-include="partDetailsVm.tabs.drawing.template"
                             ng-controller="PartDrawingController as drawingVm"></div>
                    </uib-tab>

                    <uib-tab heading="{{partDetailsVm.tabs.visualization.heading}}"
                             active="partDetailsVm.tabs.visualization.active"
                             select="partDetailsVm.tabSelected(partDetailsVm.tabs.visualization.id)">
                        <div ng-include="partDetailsVm.tabs.visualization.template"
                             ng-controller="PartDetailsVisualizationController as visualizationVm"></div>
                    </uib-tab>

                    <uib-tab heading="{{partDetailsVm.tabs.timeline.heading}}"
                             active="partDetailsVm.tabs.timeline.active"
                             select="partDetailsVm.tabSelected(partDetailsVm.tabs.timeline.id)">
                        <div ng-include="partDetailsVm.tabs.timeline.template"
                             ng-controller="TimelineController as timelineVm"></div>
                    </uib-tab>
                </uib-tabset>
            </div>
        </div>
    </div>
</div>