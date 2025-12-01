<object-workflow object-type="NCR" can-start-workflow="ncrDetailsCount.problemItems > 0"
                 can-finish-workflow="true" object="ncr"
                 permission="hasPermission('ncr','edit') && !ncr.released && ncr.statusType != 'REJECTED'"></object-workflow>