<object-workflow object-type="PROBLEMREPORT" can-start-workflow="problemReportDetailsCount.problemItems > 0"
                 can-finish-workflow="true" object="problemReport"
                 permission="hasPermission('problemreport','edit') && !problemReport.released && problemReport.statusType != 'REJECTED'"></object-workflow>