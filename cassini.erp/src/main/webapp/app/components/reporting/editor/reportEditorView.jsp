<div>
    <div>
        <button class="btn btn-primary" ng-click="executeScript()">Run Report</button>
    </div>
    <br/>
    <div ng-if="showChart" style="width: 80%;border: 1px solid #EEE;padding: 10px;margin-bottom: 20px;">
        <div>
            <h5 style="font-size: 18px; text-align: center" class="text-muted">Customers by Region (Top 10)</h5>
        </div>
        <div
            bar-chart
            bar-data='chart.data'
            bar-x='key'
            bar-y='["number"]'
            bar-colors='["#7a92a3"]'
            bar-size='100'
            bar-labels='["Region"]'>
        </div>
    </div>
    <div class="report-editor">
        <div ui-ace="{
                theme:'xcode',
                mode: 'groovy',
                onLoad: aceLoaded,
                onChange: aceChanged
            }"
            ng-model="scriptText">
        </div>
    </div>
</div>