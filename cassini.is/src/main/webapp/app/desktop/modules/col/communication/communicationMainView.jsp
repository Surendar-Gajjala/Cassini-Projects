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

    .nav-tabs {
        border: 0;
        border-bottom: 1px solid #d8dbde;
    }


</style>
<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="item-details-tabs">
            <uib-tabset active="communicationVm.activeTab">
                <uib-tab heading="{{communicationVm.tabs.email.heading}}"
                         disable="!hasPermission('permission.communication.viewEmail')"
                         select="communicationVm.communicationTabActivated(communicationVm.tabs.email.id)">
                    <div ng-include="communicationVm.tabs.email.template"
                         ng-controller="EmailController as emailVm"
                         style="height: 100% !important; padding-top: 5px !important;"></div>
                </uib-tab>
                <uib-tab heading="{{communicationVm.tabs.messaging.heading}}"
                         disable="!(hasPermission('permission.communication.viewMessages') || hasPermission('permission.communication.newGroup') || hasPermission('permission.communication.editGroup') || hasPermission('permission.communication.writeMessage'))"
                         select="communicationVm.communicationTabActivated(communicationVm.tabs.messaging.id)">
                    <div ng-include="communicationVm.tabs.messaging.template"
                         ng-controller="MessagesController as messageVm"
                         style="height: 100% !important; padding-top: 5px !important;"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
</div>
