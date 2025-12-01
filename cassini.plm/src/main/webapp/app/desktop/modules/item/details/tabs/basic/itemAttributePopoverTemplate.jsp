<div style="max-height: 220px;">
    <table class="table table-striped">
        <thead>
        <tr style="">
            <th style="" translate>LIST_OF_VALUES</th>
        </tr>
        <tr ng-if="attribute.attributeDef.configurableAttr.length == 0" style="border-top: 1px solid lightgrey;">
            <td colspan="10"
                style="width: 100px;background: #fcfffa !important;color: blue !important;text-align: left;" translate>
                NO_ATTRIBUTES
            </td>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="attr in attribute.attributeDef.configurableAttr">
            <td> {{attr}}</td>
        </tr>
        </tbody>
    </table>
</div>
