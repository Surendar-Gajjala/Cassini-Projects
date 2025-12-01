<div classs="row">
    <style scoped>
        .addBomGroupButton:hover {
            background-color: #0081c2 !important;
            border-color: #028af3;
            cursor: pointer;
        }

        .addBomGroupButton:hover i {
            color: #fff !important;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .ui-select-match .btn {
            line-height: 16px !important;
        }

        table .ui-select-choices {
            position: fixed !important;
        }
    </style>
    <div class="col-md-12" style="border-bottom: 1px solid #EEE;padding: 7px;margin-top: -15px;height: 40px;">
        <div>
            <button class='addBomGroupButton btn btn-sm btn-white'
                    title="New Type"
                    style="position: absolute;left: 10px;width: 40px;top: 0px;"
                    ng-click='bomGroupVm.addBomGroupType()'>
                <i class="fa fa-plus"
                   style="font-size: 20px;vertical-align: middle;color: #adadad;"></i>
            </button>
        </div>
        <div class="col-md-3" style="left: 40px;">
            <span>Displaying {{bomGroupVm.bomGroups.numberOfElements}} of {{bomGroupVm.bomGroups.totalElements}}</span>
        </div>
        <div class="col-md-6">

        </div>
        <div class="col-md-3 text-right">
            <span class="mr10">Page {{bomGroupVm.bomGroups.totalElements != 0 ? bomGroupVm.bomGroups.number+1:0}} of {{bomGroupVm.bomGroups.totalPages}}</span>
            <a href="" ng-click="bomGroupVm.previousPage()"
               ng-class="{'disabled': bomGroupVm.bomGroups.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
            <a href="" ng-click="bomGroupVm.nextPage()"
               ng-class="{'disabled': bomGroupVm.bomGroups.last}"><i class="fa fa-arrow-circle-right"></i></a>
        </div>
    </div>
    <div class="col-md-12">
        <div class="responsive-table">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="">Type</th>
                    <th style="width:40%;">Name</th>
                    <th style="width:10%;">Code</th>
                    <th style="width:10%; text-align: center">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="bomGroupVm.bomGroups.content.length == 0">
                    <td colspan="10"><span>No BOM Groups</span></td>
                </tr>

                <tr ng-repeat="bomGroup in bomGroupVm.bomGroups.content">
                    <td style="vertical-align: middle;display: flex;">
                        <bom-group-type ng-show="!bomGroup.editMode" object="bomGroup"></bom-group-type>
                        <span ng-show="!bomGroup.editMode && bomGroup.type == 'SECTION' && bomGroup.versity"
                              style="padding-left: 5px;"> ( Versity )</span>
                        <ui-select ng-show="bomGroup.editMode" ng-model="bomGroup.newType"
                                   theme="bootstrap" style="width:50%">
                            <ui-select-match placeholder="Select Type">
                                {{$select.selected}}
                            </ui-select-match>
                            <ui-select-null-choice></ui-select-null-choice>
                            <ui-select-choices
                                    repeat="type in bomGroupVm.bomGroupTypes | filter: $select.search">
                                <div ng-bind="type"></div>
                            </ui-select-choices>
                        </ui-select>
                        <span ng-show="bomGroup.editMode && bomGroup.newType == 'SECTION'" style="padding: 5px 10px;">Versity : </span>
                        <input ng-show="bomGroup.editMode && bomGroup.newType == 'SECTION'" type="checkbox"
                               ng-model="bomGroup.newVersity"
                               style="width: 20px;margin-top: 10px;"/>
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-show="!bomGroup.editMode">{{bomGroup.name}}</span>
                        <input placeholder="Enter Name" class="form-control" type="text"
                               ng-show="bomGroup.editMode" ng-model="bomGroup.newName">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-show="!bomGroup.editMode">{{bomGroup.code}}</span>
                        <input placeholder="Enter Code" class="form-control" type="text" maxlength="2"
                               ng-show="bomGroup.editMode" ng-model="bomGroup.newCode"
                               ng-enter="bomGroupVm.acceptChanges(bomGroup)"
                               ng-disabled="bomGroup.newType == null">
                    </td>

                    <td style="width:120px; text-align: center; vertical-align: middle;">
                        <div class="btn-group" ng-if="!bomGroup.editMode">
                            <button type="button" class="btn btn-xs btn-warning"
                                    ng-click="bomGroupVm.showEditMode(bomGroup)" title="Edit">
                                <i class="fa fa-edit"></i></span>
                            </button>
                            <button type="button" class="btn btn-xs btn-danger"
                                    ng-click="bomGroupVm.deleteBomGroupType(bomGroup)" title="Delete">
                                <i class="fa fa-trash"></i></span>
                            </button>
                        </div>


                        <div class="btn-group" style="margin-bottom: 0px;" ng-if="bomGroup.editMode">
                            <button type="button" class="btn btn-xs btn-success"
                                    ng-click="bomGroupVm.acceptChanges(bomGroup)" title="Save"><i
                                    class="fa fa-check"></i></button>
                            <button type="button" class="btn btn-xs btn-default"
                                    title="Cancel Changes"
                                    ng-click="bomGroupVm.hideEditMode(bomGroup);"><i class="fa fa-times"></i></button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

