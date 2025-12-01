<div style="padding: 5px">
    <h4 class="section-title" translate>MASTER_ATTRIBUTES</h4>
</div>
<object-attribute-details-view object-type="ITEMTYPE"
                                has-permission="hasPermission('item','edit') && !itemRevision.released && !itemRevision.rejected && lockedObjectPermission"
                               object-type-id="item.itemType.id" show-attributes="true"
                               object-id="item.id"></object-attribute-details-view>
<div style="padding: 5px">
    <h4 class="section-title" translate>REVISION_ATTRIBUTES</h4>
</div>
<object-attribute-details-view object-type="ITEMREVISION"
                                has-permission="hasPermission('item','edit') && !itemRevision.released && !itemRevision.rejected && lockedObjectPermission"
                               object-type-id="item.itemType.id" show-attributes="true"
                               object-id="itemRevision.id"></object-attribute-details-view>