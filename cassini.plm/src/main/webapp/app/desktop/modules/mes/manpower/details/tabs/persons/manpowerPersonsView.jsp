<div class="view-container" fitcontent>
    <style>
        .added-column {
            text-align: left;
            width: 150px;
        }

        .added-column i {
            display: none;
            cursor: pointer;
            margin-left: 5px;
        }

        .added-column:hover i {
            display: inline-block;
        }

        .attributeTooltip {
            position: relative;
            display: inline-block;
        }

        .attributeTooltip .attributeTooltiptext {
            visibility: hidden;
            width: 200px;
            background-color: #7BB7EB;
            color: #141f9f;
            text-align: left;
            border-radius: 6px;
            padding: 5px 0;
            position: absolute;
            z-index: 1;
            top: -5px;
            bottom: auto;
            right: 100%;
            opacity: 0;
            transition: opacity 1s;
        }

        .attributeTooltip .attributeTooltiptext::after {
            content: "";
            position: absolute;
            top: 25%;
            left: 102%;
            margin-left: -5px;
            border-width: 5px;
            border-style: solid;
            border-color: transparent transparent transparent #7BB7EB;
        }

        .attributeTooltip:hover .attributeTooltiptext {
            visibility: visible;
            opacity: 1;
        }

        /* The Close Button */
        .img-model .closeImage11 {
            position: absolute;
            top: 50px;
            right: 50px;

            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage11:hover,
        .img-model .closeImage11:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .responsive-table .dropdown-content {
            margin-left: 30px !important;
        }


    </style>
    <div class='responsive-table' style="padding: 10px">
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>

                <th style="width: 30px;">
                    <i ng-if="(hasPermission('manpower','edit') && !loginPersonDetails.external) || (loginPersonDetails.external && sharedPermission == 'WRITE' && hasPermission('manpower','edit'))"  class="la la-plus" ng-click="manpowerPersonsVm.addContact()"
                       style="padding-left: 3px;cursor: pointer;" title="{{'ADD_PERSON' | translate }}"></i>
                </th>
                <th style="width: 200px;" translate>NAME</th>
                <th style="width: 200px;" translate>PHONE_NUMBER</th>
                <th translate>EMAIL</th>
                <th style="width: 100px;" translate>ACTIVE</th>
                <th style="text-align: center" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="manpowerPersonsVm.loading == true">
                <td colspan="10"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5">
                    <span translate>LOADING_PERSONS</span>
                </td>
            </tr>

            <tr ng-if="manpowerPersonsVm.loading == false && manpowerPersonsVm.manpowersContacts.length == 0">
                <td colspan="10" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Manufacturers.png" alt="" class="image">

                        <div class="message">{{ 'NO_PERSONS' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>

            <tr ng-if="manpowerPersonsVm.manpowersContacts.length > 0"
                ng-repeat="manpowersContact in manpowerPersonsVm.manpowersContacts">
                <td></td>
                <td>{{manpowersContact.person.fullName}}</td>
                <td>{{manpowersContact.person.phoneMobile}}</td>
                <td>{{manpowersContact.person.email}}</td>
                <td style="width: 100px;">
                    <span>
                          <input type="checkbox" id="{{manpowersContact.id}}" switch="none" checked=""
                                 ng-model="manpowersContact.active" ng-change="manpowerPersonsVm.updateManpowerContact(manpowersContact)">
                        <label for="{{manpowersContact.id}}" data-on-label="Yes" data-off-label="No" style="margin: 0 !important"></label>
                    </span>
                </td>
                <td class="text-center">
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                          ng-if="(hasPermission('manpower','edit') && !loginPersonDetails.external) || (loginPersonDetails.external && sharedPermission == 'WRITE' && hasPermission('manpower','edit'))">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                            <%--    <li ng-click="manpowerPersonsVm.editContact(manpowersContact)">
                                    <a href="" translate>EDIT</a>
                                </li>--%>
                                <li title="{{ manpowersContact.usedContact == true ? cannotDeleteAddedDeclaration : ''}}">
                                    <a ng-class="{'disabled': manpowersContact.usedContact == true}"
                                       ng-click="manpowerPersonsVm.deleteManpowerContact(manpowersContact)"
                                       translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="manpower.person" object-value="manpowersContact"></plugin-table-actions>
                            </ul>
                    </span>

                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>