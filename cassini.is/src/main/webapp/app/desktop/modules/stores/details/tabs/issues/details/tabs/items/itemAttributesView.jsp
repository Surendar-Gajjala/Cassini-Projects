<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">
        <span ng-if="itemAttributesVm.itemAttributes.length == 0 || itemAttributesVm.attributes.length == 0">
            <td colspan="11">No Attributes</td>
        </span>
        <br>

        <div>
            <form class="form-horizontal">
                <attributes-view show-objects="selectObjectValues"
                                 attributes="itemAttributesVm.requiredAttributes"></attributes-view>
                <br>
                <attributes-view show-objects="selectObjectValues"
                                 attributes="itemAttributesVm.attributes"></attributes-view>
                <br>
                <br>
            </form>
        </div>

    </div>
</div>
