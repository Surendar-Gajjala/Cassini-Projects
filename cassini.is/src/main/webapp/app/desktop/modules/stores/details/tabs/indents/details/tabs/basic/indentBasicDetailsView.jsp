<div ng-if="indentBasicDetailsVm.loading == true" style="padding: 30px;">
    <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading...
                </span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Indent Number: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{indentBasicDetailsVm.indent.indentNumber}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Project: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{indentBasicDetailsVm.indent.project.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created Date: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{indentBasicDetailsVm.indent.raisedDate}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{indentBasicDetailsVm.indent.raisedBy}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Status: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span style="color: white" class="label" ng-class="{
                                    'label-success': indentBasicDetailsVm.indent.status == 'NEW',
                                    'label-info': indentBasicDetailsVm.indent.status == 'APPROVED'}">{{indentBasicDetailsVm.indent.status}}
                        </span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Approved By: </span>
            </div>

            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#"
                   ng-if="indentBasicDetailsVm.indent.status != 'APPROVED' && hasPermission('permission.indents.editIndent')"
                   editable-text="indentBasicDetailsVm.indent.approvedBy"
                   onaftersave="indentBasicDetailsVm.update()">
                    {{indentBasicDetailsVm.indent.approvedBy || 'Click to enter approvedBy'}}</a>

                <p ng-if="indentBasicDetailsVm.indent.status == 'APPROVED' || !hasPermission('permission.indents.editIndent')">
                    {{indentBasicDetailsVm.indent.approvedBy}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Notes: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#"
                   ng-if="indentBasicDetailsVm.indent.status != 'APPROVED'  && hasPermission('permission.indents.editIndent')"
                   editable-text="indentBasicDetailsVm.indent.notes"
                   onaftersave="indentBasicDetailsVm.update()">
                    {{indentBasicDetailsVm.indent.notes || 'Click to enter notes'}}</a>

                <p ng-if="indentBasicDetailsVm.indent.status == 'APPROVED' || !hasPermission('permission.indents.editIndent')">
                    {{indentBasicDetailsVm.indent.notes}}</p>
            </div>
        </div>

        <attributes-details-view attribute-id="indentId" attribute-type="CUSTOM_INDENT"
                                 has-permission="hasPermission('permission.indents.editIndent')"></attributes-details-view>

    </div>
</div>
