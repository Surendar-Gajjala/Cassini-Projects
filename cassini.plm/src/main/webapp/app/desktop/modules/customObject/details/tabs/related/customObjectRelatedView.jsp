<div>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 30px;">
                    <i class="la la-plus" title="Add related objects"
                       style="cursor: pointer" ng-click="customObjectRelatedVm.addRelatedObjects()"></i>
                </th>
                <th style="width: 200px;" translate>NUMBER</th>
                <th class="col-width-250" translate>NAME</th>
                <th style="width: 200px;" translate>TYPE</th>
                <th style="width: 200px;" translate>DESCRIPTION</th>
                <th style="width: 200px;" translate>NOTES</th>
                <th style="width: 100px;text-align: center">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="customObjectRelatedVm.saveAll()"
                       ng-if="customObjectRelatedVm.selectedRelatedObjects.length > 1"
                       title="Save"
                       style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="customObjectRelatedVm.removeAll()" title="Remove"
                       ng-if="customObjectRelatedVm.selectedRelatedObjects.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="customObjectRelatedVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_OBJECTS</span>
                </td>
            </tr>
            <tr ng-if="customObjectRelatedVm.loading == false && customObjectRelatedVm.relatedCustomObjects.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Substance.png" alt="" class="image">

                        <div class="message" translate>NO_OBJECTS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-if="customObjectRelatedVm.relatedCustomObjects.length > 0"
                ng-repeat="relatedCustomObject in customObjectRelatedVm.relatedCustomObjects">
                <td></td>
                <td>
                    <a href=""
                       ng-click="customObjectRelatedVm.showCustomObject(relatedCustomObject)">{{relatedCustomObject.related.number}}</a>
                </td>

                <td class="col-width-250">{{relatedCustomObject.related.name}}</td>
                <td>{{relatedCustomObject.related.type.name}}</td>
                <td class="col-width-250">
                    {{relatedCustomObject.related.description}}
                </td>

                <td>
                    <span ng-if="relatedCustomObject.editMode == false">
                        {{relatedCustomObject.notes}}
                    </span>
                    <span ng-if="relatedCustomObject.editMode == true">
                       <form>
                           <input type="text" class="form-control" style="width: 150px;"
                                  ng-model="relatedCustomObject.notes"/>
                       </form>
                    </span>
                </td>

                <td class="text-center">
                     <span class="btn-group"
                           ng-if="relatedCustomObject.editMode == true"
                           style="margin: -1px">
                    <i title="{{'SAVE' | translate}}"
                       ng-click="customObjectRelatedVm.createRelatedCustomObject(relatedCustomObject)"
                       class="la la-check">
                    </i>
                    <i ng-show="customObjectRelatedVm.itemFlag == true"
                       title="{{cancelChangesTitle}}"
                       ng-click="customObjectRelatedVm.onCancel(relatedCustomObject)"
                       class="la la-times">
                    </i>
                     <i ng-show="customObjectRelatedVm.itemFlag == false"
                        title="{{cancelChangesTitle}}"
                        ng-click="customObjectRelatedVm.cancelChanges(relatedCustomObject)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" ng-hide="relatedCustomObject.editMode == true" uib-dropdown
                      dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li>
                            <a href="" ng-click="customObjectRelatedVm.editRelatedCustomObject(relatedCustomObject)"
                               translate>EDIT_OBJECT</a>
                        </li>
                        <li>
                            <a href=""
                               ng-click="customObjectRelatedVm.deleteCustomObject(relatedCustomObject)"
                               translate>REMOVE_OBJECT</a>
                        </li>
                        <plugin-table-actions context="custom.related" object-value="relatedCustomObject"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>