<style>
    .popover-content {
        padding: 5px !important;
    }
</style>
<div style="max-height: 240px;overflow-y: auto !important;">
    <table class="table table-striped">
        <thead>
        <tr style="">
            <th translate>CAS_NUMBER</th>
            <th translate>THRESHOLD_MASS</th>
            <th translate>DECLARED_MASS</th>
        </tr>
        <tr ng-if="specification.billOfSubstances.length == 0" style="border-top: 1px solid lightgrey;">
            <td colspan="10"
                style="width: 100px;background: #fcfffa !important;color: blue !important;text-align: left;" translate>
                NO_SUBSTANCES
            </td>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="billOfSubstance in specification.billOfSubstances">
            <td>{{billOfSubstance.casNumber}}</td>
            <td>{{billOfSubstance.specMass}} {{billOfSubstance.specMassUnitSymbol}}</td>
            <td>{{billOfSubstance.mass}} {{billOfSubstance.unitSymbol}}</td>
        </tr>
        </tbody>
    </table>
</div>
