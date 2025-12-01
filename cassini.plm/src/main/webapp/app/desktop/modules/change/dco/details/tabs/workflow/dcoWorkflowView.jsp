<object-workflow object-type="DCO" can-start-workflow="affectedItems > 0"
                 can-finish-workflow="true" object="dco"
                 permission="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED'"></object-workflow>
