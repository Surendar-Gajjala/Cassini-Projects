<uib-tab ng-repeat="customTab in customTabs track by $index"
         id="{{customTab.id}}"
         class="custom-tab"
         heading="{{customTab.heading}}"
<%--active="customTab.active"--%>
         select="tabActivated(customTab.id)">
    <div ng-include="customTab.template" style="height:69vh !important;"
         dynamic-ctrl="customTab.controller"></div>
</uib-tab>
