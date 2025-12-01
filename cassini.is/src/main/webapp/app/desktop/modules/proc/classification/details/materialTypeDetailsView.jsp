<style>
    .responsive-table .dropdown-content {
        display: none;
        position: absolute;
        background-color: #fff !important;
        min-width: 100px;
        box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
        z-index: 1 !important;
        border-radius: 5px;
        text-align: left;
        color: black !important;
        margin-top: -45px;

    }

    .responsive-table .dropdown-content a {
        text-decoration: none;
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content a:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    .responsive-table .dropdown-content i:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    .responsive-table .dropdown:hover .dropdown-content {
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content {
        margin-left: -35px !important;
    }
</style>


<div ng-if="materialTypeVm.materialType == null || materialTypeVm.materialType == undefined" style="padding: 10px">
    <span>No Material Type selected to see/edit details</span>
</div>
<div ng-if="materialTypeVm.materialType != null" <%--style="overflow-y: auto;overflow-x: hidden"--%>>
    <%-- <div style="width: 100px">
         <button class="btn btn-xs btn-success min-width" ng-click="materialTypeVm.onSave()">Save</button>
     </div>
     <hr> --%>
    <div class="typeInfo">
        <h4 class="section-title">Basic Info</h4>

        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name:<span class="asterisk">*</span></label>

                        <div class="col-sm-7" ng-if="hasPermission('permission.masterdata.editClassification')==false"
                             style="margin-top: 3px;padding-top: 7px;">{{materialTypeVm.materialType.name}}
                        </div>
                        <div class="col-sm-7" ng-if="hasPermission('permission.masterdata.editClassification')==true">
                            <input type="text" class="form-control" name="title"
                                   ng-model="materialTypeVm.materialType.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description: </label>

                        <div class="col-sm-7" ng-if="hasPermission('permission.masterdata.editClassification')==false"
                             style="margin-top: 3px;padding-top: 7px;">{{materialTypeVm.materialType.description}}
                        </div>
                        <div class="col-sm-7" ng-if="hasPermission('permission.masterdata.editClassification')==true">
                            <textarea name="description" rows="5" class="form-control" style="resize: none"
                                      ng-model="materialTypeVm.materialType.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Auto Number Source:<span class="asterisk">*</span>
                        </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="materialTypeVm.materialType.materialNumberSource" theme="bootstrap"
                                       ng-disabled="hasPermission('permission.masterdata.editClassification')==false"
                                       style="width:100%">
                                <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices
                                        repeat="source in materialTypeVm.autoNumbers | filter: $select.search">
                                    <div ng-bind="source.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </form>

            </div>
        </div>

        <br>
        <h4 class="section-title" ng-show="materialTypeVm.attributesShow">Attributes</h4>
        <br>

        <div class="row" ng-show="materialTypeVm.attributesShow">
            <div style="margin-left: 10px;">
                <div>
                    <button class="btn btn-xs btn-primary" ng-click="materialTypeVm.addAttribute()">New Attribute
                    </button>
                    <span style="margin-left: 249px; color: red;">{{materialTypeVm.error}}</span>
                </div>
                <div class="responsive-table">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="width: 200px">Name<span class="asterisk">*</span></th>
                            <th>Description</th>
                            <th style="width: 120px">Data Type<span class="asterisk">*</span></th>
                            <th style="width: 100px; text-align: center">Is Required</th>
                            <th style="width: 150px;" class="actions-column">Actions</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-if="materialTypeVm.materialType.attributes.length == 0">
                            <td colspan="7">No attributes are available to view</td>
                        </tr>
                        <tr ng-repeat="attribute in materialTypeVm.materialType.attributes">
                            <td style="width: 200px">
                                <span ng-if="attribute.showValues == true">{{attribute.name}}</span>
                                <input ng-if="attribute.editMode == true" type=" text" class="form-control input-sm"
                                       ng-model="attribute.newName">
                            </td>
                            <td>
                                <span ng-if="attribute.showValues == true">{{attribute.description}}</span>
                                <input ng-if="attribute.editMode == true" type="text" class="form-control input-sm"
                                       ng-model="attribute.newDescription">
                            </td>
                            <td style="width: 120px">
                                <span ng-if="attribute.showValues == true">
                                    <span>{{attribute.dataType}}</span>
                                    <span ng-if="attribute.dataType == 'OBJECT'" style="margin-left: 5px;">( {{attribute.refType}} )</span>
                                    <span ng-if="attribute.dataType == 'LIST'" style="margin-left: 5px;">( {{attribute.lov.name}} )</span>
                                </span>
                                <select ng-if="attribute.editMode == true" class="form-control input-sm"
                                        ng-model="attribute.newDataType"
                                        ng-options="dataType for dataType in materialTypeVm.dataTypes">
                                </select>
                                <select ng-if="attribute.editMode == true && attribute.newDataType == 'OBJECT'"
                                        class="form-control input-sm"
                                        ng-model="attribute.newRefType"
                                        ng-options="refType for refType in materialTypeVm.refTypes">
                                </select>
                                <select ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'"
                                        class="form-control input-sm"
                                        ng-model="attribute.newLov"
                                        ng-options="lov.name for lov in materialTypeVm.lovs">
                                </select>
                            </td>
                            <td style="width: 100px; text-align: center">
                                <input type="checkbox" class="form-control input-sm" ng-model="attribute.required"
                                       ng-disabled="attribute.editMode == false">
                            </td>

                            <td class="text-center">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="materialTypeVm.applyChanges(attribute)">
                                            <a class="dropdown-item" title="Save" type="button"
                                               ng-if="attribute.editMode == true">
                                                <span style="padding-left: 3px;">Save</span>
                                            </a></li>
                                        <li ng-click="materialTypeVm.cancelChanges(attribute)">
                                            <a class="dropdown-item" type="button"
                                               ng-if="attribute.editMode == true"
                                               title="Cancel">
                                                <span style="padding-left: 3px;">Cancel</span>
                                            </a>
                                        </li>
                                        <li ng-click="materialTypeVm.editAttribute(attribute)"
                                            ng-if="attribute.showValues == true">
                                            <a title="Edit" type="button"
                                               ng-disabled="attribute.itemType != materialTypeVm.materialType.id"
                                               class="dropdown-item">
                                                <span style="padding-left: 3px;">Edit</span>
                                            </a>
                                        </li>
                                        <li ng-click="materialTypeVm.deleteAttribute(attribute)"
                                            ng-if="attribute.showValues == true">
                                            <a title="Delete"
                                               type="button"
                                               ng-disabled="attribute.itemType != materialTypeVm.materialType.id"
                                               class="dropdown-item">
                                                <span style="padding-left: 3px;">Delete</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>