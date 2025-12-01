<object-workflow object-type="QCR" can-start-workflow="qcrDetailCount.problemItems > 0 && qcrDetailCount.capaPass"
                 can-finish-workflow="true" object="qcr"
                 permission="hasPermission('qcr','edit') && !qcr.released && qcr.statusType != 'REJECTED'"></object-workflow>