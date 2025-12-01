<div>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;">
                    <i class="la la-plus" ng-click="supplier4mChangeVm.createCustomObject()"
                       ng-if="supplier4mChangeVm.customObjectType != null"></i>
                </th>
                <th class="col-width-100" translate>NUMBER</th>
                <th class="col-width-150" translate>NAME</th>
                <th class="col-width-150" translate>TYPE</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th translate>CREATED_DATE</th>
                <th translate>CREATED_BY</th>
                <th translate>MODIFIED_DATE</th>
                <th translate>MODIFIED_BY</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="supplier4mChangeVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_4MCHANGE</span>
                </td>
            </tr>
            <tr ng-if="supplier4mChangeVm.loading == false && supplier4mChangeVm.customObjects.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message" translate>NO_4MCHANGE</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="object in supplier4mChangeVm.customObjects">
                <td></td>
                <td class="col-width-100">
                    <a href=""
                       ng-click="supplier4mChangeVm.showObjectDetails(object)">{{object.number}}</a>
                </td>
                <td class="col-width-200">{{object.name}}</td>
                <td class="col-width-200">{{object.type.name}}</td>
                <td class="col-width-250">{{object.description}}</td>
                <td style="width: 155px">
                    {{object.createdDate}}
                </td>
                <td style="width: 155px">
                    {{object.createdByObject.firstName}}
                </td>
                <td style="width: 155px">
                    {{object.modifiedDate}}
                </td>
                <td style="width: 155px">
                    {{object.modifiedByObject.firstName}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>