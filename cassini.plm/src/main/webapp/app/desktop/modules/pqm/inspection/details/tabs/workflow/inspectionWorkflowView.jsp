<object-workflow object-type="{{inspection.objectType}}" can-start-workflow="true"
                 can-finish-workflow="inspectionCounts.pendingChecklists == 0" object="inspection"
                 permission="hasPermission('inspection','edit') && !inspection.released && inspection.statusType != 'REJECTED'"></object-workflow>
