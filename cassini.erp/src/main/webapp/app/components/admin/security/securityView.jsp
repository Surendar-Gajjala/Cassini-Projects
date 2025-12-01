<tabset>
    <tab ng-repeat="tab in tabs" heading="{{tab.heading}}" active="tab.active" select="setTabActive(tab)">
        <div ui-view="{{tab.view}}"></div>
    </tab>
</tabset>