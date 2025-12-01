<change-request-view object-id="ecoChangeRequestVm.ecoId" object-type="ECO"
                     can-add-request="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && !eco.released && eco.statusType != 'REJECTED' && !eco.startWorkflow"
                     can-delete-request="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && !eco.released && eco.statusType != 'REJECTED' && !eco.revisionsCreated && !eco.startWorkflow"
                     has-permission="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && !eco.released && eco.statusType != 'REJECTED' && !eco.revisionsCreated && !eco.startWorkflow">
</change-request-view>