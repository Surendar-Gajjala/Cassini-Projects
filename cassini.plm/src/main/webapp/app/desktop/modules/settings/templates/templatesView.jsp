<div style="width: calc(100% - 10px);height: calc(100% - 50px);display: flex;position: absolute;">
    <style scoped>
        .split-left {
            width: 300px;
            border-right: 1px solid #eee;
            padding-right: 12px;
            padding-top: 10px;
        }

        .split-right {
            width: calc(100% - 300px);
            height: 100%;
            overflow-x: hidden !important;
            padding-left: 20px;
            overflow-y: auto;
        }

        .active-tab {
            color: white;
            background-color: darkgrey;
        }

        .template {
            padding-left: 10px;
            cursor: pointer;
            height: 32px;
            line-height: 30px;
            border-bottom: 1px dotted #ddd;
        }

        .template:hover {
            background-color: #d6e1e0 !important;
        }

        .selected-template,.selected-template:hover {
            color: white;
            background-color: #0081c2 !important;
        }
    </style>
    <div class="split-left">
        <div class="template"
             ng-class="{'selected-template': pageName === 'shareobjectmail.html'}"
             ng-click="tempsVm.loadPage('shareobjectmail.html')">
            <span translate>SHARE_OBJECT</span>
        </div>

        <div class="template"
             ng-class="{'selected-template': pageName === 'sharedProjectObject.html'}"
             ng-click="tempsVm.loadPage('sharedProjectObject.html')">
            <span translate>SHARE_PROJECT</span>
        </div>

        <div class="template"
             ng-class="{'selected-template': pageName === 'workflowApprover.html'}"
             ng-click="tempsVm.loadPage('workflowApprover.html')">
            <span translate>ECO_CHANGES</span>
        </div>

        <div class="template"
             ng-class="{'selected-template': pageName === 'subscribeNotification.html'}"
             ng-click="tempsVm.loadPage('subscribeNotification.html')">
            <span translate>SUBSCRIPTION</span>
        </div>

        <div class="template"
             ng-class="{'selected-template': pageName === 'subscribeSpecificationNotification.html'}"
             ng-click="tempsVm.loadPage('subscribeSpecificationNotification.html')">
            <span translate>SPECIFICATION_SUBSCRIPTION</span>
        </div>

        <div class="template"
             ng-class="{'selected-template': pageName === 'requirementTemplate.html'}"
             ng-click="tempsVm.loadPage('requirementTemplate.html')">
            <span translate>REQUIREMENT_SUBSCRIPTION</span>
        </div>
    </div>
    <div id="preferencesPane" class="split-right">
        <div ng-if="tempsVm.status == true">
            <div ng-if="pageName === 'shareobjectmail.html'">
                <div
                     ng-include="'app/desktop/modules/settings/templates/newScriptView.jsp'"
                     ng-controller='NewTemplateController as newTemplateVm'>
                </div>
            </div>

            <div ng-if="pageName === 'sharedProjectObject.html'">
                <div
                     ng-include="'app/desktop/modules/settings/templates/newScriptView.jsp'"
                     ng-controller='NewTemplateController as newTemplateVm'>
                </div>
            </div>

            <div ng-if="pageName === 'workflowApprover.html'">
                <div
                     ng-include="'app/desktop/modules/settings/templates/newScriptView.jsp'"
                     ng-controller='NewTemplateController as newTemplateVm'>
                </div>
            </div>

            <div ng-if="pageName === 'subscribeNotification.html'">
                <div
                     ng-include="'app/desktop/modules/settings/templates/newScriptView.jsp'"
                     ng-controller='NewTemplateController as newTemplateVm'>
                </div>
            </div>

            <div ng-if="pageName === 'subscribeSpecificationNotification.html'">
                <div
                     ng-include="'app/desktop/modules/settings/templates/newScriptView.jsp'"
                     ng-controller='NewTemplateController as newTemplateVm'>
                </div>
            </div>

            <div ng-if="pageName === 'requirementTemplate.html'">
                <div
                     ng-include="'app/desktop/modules/settings/templates/newScriptView.jsp'"
                     ng-controller='NewTemplateController as newTemplateVm'>
                </div>
            </div>
        </div>
    </div>
</div>