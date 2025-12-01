<object-workflow object-type="MCO"
                 can-start-workflow="(mcoDetailsCount.affectedItems > 0 && mcoDetailsCount.replacementPartsExist && mco.mcoType.mcoType == 'OEMPARTMCO') || (mcoDetailsCount.affectedItems > 0 && mco.mcoType.mcoType == 'ITEMMCO')"
                 can-finish-workflow="true" object="mco"
                 permission="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'"></object-workflow>