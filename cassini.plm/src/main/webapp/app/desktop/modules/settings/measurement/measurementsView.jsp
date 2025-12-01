<div style="height: 100%;overflow: auto !important;">
    <style scoped>
        .measurement-left {
            width: 300px;
            border-right: 1px solid #eee;
            padding-right: 12px;
            padding-top: 10px;
        }

        .measurement-right {
            width: calc(100% - 300px);
        }

        .measurement {
            padding-left: 10px;
            cursor: pointer;
            height: 32px;
            line-height: 30px;
            border-bottom: 1px dotted #ddd;
        }

        .measurement:hover {
            background-color: #d6e1e0 !important;
        }

        .selected-measurement,.selected-measurement:hover {
            color: white;
            background-color: #0081c2 !important;
        }

        tr.base-unit td {
            font-weight: bolder !important;
            color: #337ab7 !important;
        }
    </style>

    <div style="display: flex;height: 100%;">
        <div class="measurement-left">
            <div class="measurement"
                 ng-class="{'selected-measurement': measurement.id == measurementsVm.selectedMeasurement.id}"
                 ng-repeat="measurement in measurementsVm.measurements"
                 ng-click="measurementsVm.selectMeasurement(measurement)">
                <span>{{measurement.name}}</span>
            </div>
        </div>
        <div class="measurement-right">
            <h4 class="text-center">Units of Measurement for {{measurementsVm.selectedMeasurement.name}}</h4>

            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="width: 30px; text-align: center" translate>Base</th>
                        <th style="" translate>Name</th>
                        <th translate>Symbol</th>
                        <th translate>Conversion Factor</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="measurementsVm.selectedMeasurement.measurementUnits.length == 0">
                        <td colspan="10"><span translate>No Measurement Units</span></td>
                    </tr>

                    <tr ng-repeat="unit in measurementsVm.selectedMeasurement.measurementUnits"
                        ng-class="{'base-unit': unit.baseUnit}">
                        <td style="width: 50px; text-align: center">
                            <i class="fa fa-check" ng-if="unit.baseUnit"></i>
                        </td>
                        <td>{{unit.name}}</td>
                        <td><span ng-bind-html="unit.symbol"></span>
                        </td>
                        <td>{{unit.conversionFactor}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
