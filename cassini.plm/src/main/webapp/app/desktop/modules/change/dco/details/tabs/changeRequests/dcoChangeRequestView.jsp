<change-request-view object-id="dcoChangeRequestVm.dcoId" object-type="DCO"
                     can-add-request="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED' && !dco.startWorkflow"
                     can-delete-request="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED' && !dco.revisionsCreated && !dco.startWorkflow"
                     has-permission="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED' && !dco.startWorkflow">
</change-request-view>