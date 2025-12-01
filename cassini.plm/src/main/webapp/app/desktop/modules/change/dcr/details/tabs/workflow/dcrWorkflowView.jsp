<object-workflow object-type="DCR" can-start-workflow="affectedItems > 0"
                 can-finish-workflow="true" object="dcr"
                 permission="(hasPermission('change','dcr','edit') || hasPermission('change','edit')) && !dcr.isApproved && dcr.statusType != 'REJECTED'"></object-workflow>
