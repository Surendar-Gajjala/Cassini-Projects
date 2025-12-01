<div>
    <style scoped>
        .widget-messaging ul li {
            padding: 0px !important;
        }

        .workflows-widget .tab-content, .workflows-widget .nav-tabs {
            border: 0 !important;
            border-radius: 0 !important;
        }

        .workflows-widget .nav-tabs {
            border-bottom: 1px solid #ddd !important;
        }
    </style>
    <div class="panel panel-default panel-alt widget-messaging workflows-widget">
        <div class="panel-heading" style="">

            <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
                <span style="font-weight: bold;" translate>Workflows</span>
            </div>
            <div class="pull-right text-center" style="margin-top: 10px; padding: 2px;">
            <span ng-if="workflowWigVm.loading == false"><small>
                {{workflowWigVm.workflows.totalElements}}
                <span translate>Workflows</span>
            </small></span>

                <div class="btn-group">
                    <button class="btn btn-xs btn-default"
                            ng-click="workflowWigVm.previousPage()"
                            ng-disabled="workflowWigVm.workflows.first">
                        <i class="fa fa-chevron-left"></i>
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="workflowWigVm.nextPage()"
                            ng-disabled="workflowWigVm.workflows.last">
                        <i class="fa fa-chevron-right"></i>
                    </button>
                </div>
            </div>
        </div>

        <div class="panel-body" style="overflow: hidden !important;">
            <style scoped>
                .tab-content {
                    padding: 0px !important;
                }

                .tab-content .tab-pane {
                    overflow: auto !important;
                }

                .tab-pane {
                    position: relative;
                }

                .tab-content .tab-pane .responsive-table {
                    height: 100%;
                    position: absolute;
                    overflow: auto !important;
                    padding: 5px;
                }

                .tab-content .tab-pane .responsive-table table thead th {
                    position: -webkit-sticky;
                    position: sticky;
                    top: -10px !important;
                    z-index: 5;
                }

                .ui-tabs-scrollable > .spacer > div:first-child > .nav-tabs {
                    border-bottom: 1px solid #ddd;
                }

                .ui-tabs-scrollable > .nav-button {
                    height: 43px;
                }
            </style>
            <div class="widget-panel" style="max-height: 400px; min-height: 400px;">
                <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                    <uib-tabset>
                        <uib-tab ng-show="workflowWigVm.workflowTabDetails" id="Items" heading="Items" active="workflowWigVm.tabs.items.active"
                                 select="workflowWigVm.workflowWidTabActivated(workflowWigVm.tabs.items.id)"
                                 style="">
                            <div ng-include="workflowWigVm.tabs.items.template"></div>
                        </uib-tab>

                        <uib-tab ng-show="workflowWigVm.workflowTabDetails.ecos >0" id="Changes" heading="Changes"
                                 active="workflowWigVm.tabs.changes.active"
                                 select="workflowWigVm.workflowWidTabActivated(workflowWigVm.tabs.changes.id)"
                                 style="">
                            <div ng-include="workflowWigVm.tabs.changes.template"></div>
                        </uib-tab>

                        <uib-tab ng-show="workflowWigVm.workflowTabDetails.quality >0" id="Changes" heading="Quality"
                                 active="workflowWigVm.tabs.quality.active"
                                 select="workflowWigVm.workflowWidTabActivated(workflowWigVm.tabs.quality.id)"
                                 style="">
                            <div ng-include="workflowWigVm.tabs.changes.template"></div>
                        </uib-tab>

                        <uib-tab ng-show="workflowWigVm.workflowTabDetails.manufacturers >0" id="Manufacturers" heading="Manufacturers"
                                 active="workflowWigVm.tabs.Manufacturers.active"
                                 select="workflowWigVm.workflowWidTabActivated(workflowWigVm.tabs.Manufacturers.id)"
                                 style="">
                            <div ng-include="workflowWigVm.tabs.Manufacturers.template"></div>
                        </uib-tab>

                        <uib-tab ng-show="workflowWigVm.workflowTabDetails.manufacturerParts >0" id="ManufacturerParts" heading="Manufacturer Parts"
                                 active="workflowWigVm.tabs.ManufacturerParts.active"
                                 select="workflowWigVm.workflowWidTabActivated(workflowWigVm.tabs.ManufacturerParts.id)"
                                 style="">
                            <div ng-include="workflowWigVm.tabs.ManufacturerParts.template"></div>
                        </uib-tab>

                        <uib-tab ng-show="workflowWigVm.workflowTabDetails.projects >0" id="Project" heading="Projects"
                                 active="workflowWigVm.tabs.projects.active"
                                 select="workflowWigVm.workflowWidTabActivated(workflowWigVm.tabs.projects.id)"
                                 style="">
                            <div ng-include="workflowWigVm.tabs.projects.template"></div>
                        </uib-tab>

                        <uib-tab ng-show="workflowWigVm.workflowTabDetails.requirements >0" id="Requirements" heading="Requirements"
                                 active="workflowWigVm.tabs.requirements.active"
                                 select="workflowWigVm.workflowWidTabActivated(workflowWigVm.tabs.requirements.id)"
                                 style="">
                            <div ng-include="workflowWigVm.tabs.requirements.template"></div>
                        </uib-tab>
                    </uib-tabset>
                </scrollable-tabset>
            </div>
        </div>
    </div>
</div>
