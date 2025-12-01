<div ng-if="selectedClassificationType.objectType == 'REQUIREMENTTYPE'">
    <timeline-view object-type="REQUIREMENTTYPE" object-id="selectedClassificationType.id"
                   has-permission="hasPermission('admin','all')"></timeline-view>
</div>
<div ng-if="selectedClassificationType.objectType == 'SPECIFICATIONTYPE'">
    <timeline-view object-type="SPECIFICATIONTYPE" object-id="selectedClassificationType.id"
                   has-permission="hasPermission('admin','all')"></timeline-view>
</div>
