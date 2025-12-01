<style scoped>
    .tab-content {
        padding-left: 10px !important;
        padding-right: 10px !important;
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
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="issueDetailsVm.back()">Back</button>

                <button ng-show="issueDetailsVm.issue.task == null && issueDetailsVm.tabs.basic.active && selectedProject.locked == false && (hasPermission('permission.issues.assign') || login.person.isProjectOwner)"
                        class="btn btn-sm btn-success min-width" ng-click="issueDetailsVm.newTask()"> Assign to
                </button>
            </div>
            <div ng-show="issueDetailsVm.tabs.parts.active">
                <free-text-search on-clear="issueDetailsVm.resetPage"
                                  on-search="issueDetailsVm.freeTextSearch"></free-text-search>
            </div>
            <h4 style="text-align:right;float: right;margin: 0;padding-right: 10px;vertical-align: middle;width: 500px;max-width:500px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">
                Problem Details : {{viewInfo.title}}</h4>
        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="storeVm.activeTab">
                        <uib-tab heading="{{issueDetailsVm.tabs.basic.heading}}"
                                 select="issueDetailsVm.problemDetailsTabActivated(issueDetailsVm.tabs.basic.id)">
                            <div ng-include="issueDetailsVm.tabs.basic.template"
                                 ng-controller="ProblemBasicController as problemBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="media"
                                 heading="{{issueDetailsVm.tabs.media.heading}}"
                                 select="issueDetailsVm.problemDetailsTabActivated(issueDetailsVm.tabs.media.id)">
                            <div ng-include="issueDetailsVm.tabs.media.template"
                                 ng-controller="ProblemMediaController as problemMediaVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
