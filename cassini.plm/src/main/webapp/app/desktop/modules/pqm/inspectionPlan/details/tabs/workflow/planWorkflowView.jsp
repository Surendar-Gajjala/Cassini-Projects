<object-workflow object-type="INSPECTIONPLANREVISION" can-start-workflow="inspectionDetailsCount.checklists > 0"
                 can-finish-workflow="true" object="inspectionPlanRevision"
                 permission="hasPermission('inspectionplan','edit') && !inspectionPlanRevision.released && !inspectionPlanRevision.rejected"></object-workflow>