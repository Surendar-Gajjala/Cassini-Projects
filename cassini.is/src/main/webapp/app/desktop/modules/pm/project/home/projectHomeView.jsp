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
        background-color: #fff;
    }


</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default min-width" ng-click="projectHomeVm.back()">Back</button>
            <button class="btn btn-sm btn-default min-width"
                    ng-if="projectHomeVm.tabs.basic.active && selectedProject.mailServerObject.mailServer == null && showUpdateButton && selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                    ng-click="projectHomeVm.update()">Update
            </button>
            <button class="btn btn-sm btn-default min-width" ng-click="saveAs()">SaveAs
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset>
                        <uib-tab heading="{{projectHomeVm.tabs.basic.heading}}"
                                 active="projectHomeVm.tabs.basic.active"
                                 select="projectHomeVm.taskDetailsTabActivated(projectHomeVm.tabs.basic.id)">
                            <div ng-include="projectHomeVm.tabs.basic.template"
                                 ng-controller="BasicController as basicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{projectHomeVm.tabs.tasks.heading}}"--%>
                        <%--ng-if="hasPermission( 'permission.home.Tasks')"--%>
                        <%--active="projectHomeVm.tabs.tasks.active"--%>
                        <%--select="projectHomeVm.taskDetailsTabActivated(projectHomeVm.tabs.tasks.id)">--%>
                        <%--<div ng-include="projectHomeVm.tabs.tasks.template"--%>
                        <%--ng-controller="TaskHomeController as tasksVm"></div>--%>
                        <%--</uib-tab>--%>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
