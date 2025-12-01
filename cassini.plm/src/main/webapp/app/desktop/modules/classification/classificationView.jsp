<div class="view-container" fitcontent>

    <style scoped>
        .change-node {
            background: transparent url("app/assets/images/admin_edit.gif") no-repeat !important;
            height: 16px;
        }

        .mfr-node {
            background: transparent url("app/assets/images/mfr.png") no-repeat !important;
            height: 16px;
        }

        .requirements-node {
            background: transparent url("app/assets/images/requirements.png") no-repeat !important;
            height: 16px;
        }

        .req-node {
            background: transparent url("app/assets/images/req.png") no-repeat !important;
            height: 16px;
        }

        .spec-node {
            background: transparent url("app/assets/images/document.png") no-repeat !important;
            height: 16px;
        }

        .mfrpart-node {
            background: transparent url("app/assets/images/mfrpart.png") no-repeat !important;
            height: 16px;
        }

        .workflow-node {
            background: transparent url("app/assets/images/workflow-node.png") no-repeat !important;
            height: 16px;
        }

        .quality-node {
            background: transparent url("app/assets/images/quality-node.png") no-repeat !important;
            height: 16px;
        }

        .custom-node {
            background: transparent url("app/assets/images/custom.png") no-repeat !important;
            height: 16px;
        }

        .customtype-node {
            background: transparent url("app/assets/images/preferences.png") no-repeat !important;
            height: 16px;
        }

        .bomItem-node {
            background: transparent url("app/assets/images/service.png") no-repeat !important;
            height: 16px;
        }

        .plant-node {
            background: transparent url("app/assets/images/plant.png") no-repeat !important;
            height: 16px;
        }

        .workCenter-node {
            background: transparent url("app/assets/images/workcenter.png") no-repeat !important;
            height: 16px;
        }

        .machine-node {
            background: transparent url("app/assets/images/machine.png") no-repeat !important;
            height: 16px;
        }

        .resource-node {
            background: transparent url("app/assets/images/industry-robot.png") no-repeat !important;
            height: 16px;
        }

        .compliance-node {
            background: transparent url("app/assets/images/compliance-certificate.png") no-repeat !important;
            height: 16px;
        }

        .production-node {
            background: transparent url("app/assets/images/production-order.png") no-repeat !important;
            height: 16px;
        }
        .mbom-node {
            background: transparent url("app/assets/images/sitemap.png") no-repeat !important;
            height: 16px;
        }
        .bop-node {
            background: transparent url("app/assets/images/flaticon.png") no-repeat !important;
            height: 16px;
        }


        .service-node {
            background: transparent url("app/assets/images/service.png") no-repeat !important;
            height: 16px;
        }

        .jigs-node {
            background: transparent url("app/assets/images/jigs.png") no-repeat !important;
            height: 16px;
        }

        .material-node {
            background: transparent url("app/assets/images/material1.png") no-repeat !important;
            height: 16px;
        }

        .tool-node {
            background: transparent url("app/assets/images/tools.png") no-repeat !important;
            height: 16px;
        }

        .operation-node {
            background: transparent url("app/assets/images/operation.png") no-repeat !important;
            height: 16px;
        }

        .manpower-node {
            background: transparent url("app/assets/images/group.png") no-repeat !important;
            height: 16px;
        }

        .oem-node {
            background: transparent url("app/assets/images/oem2.png") no-repeat !important;
            height: 16px;
        }

        .sourcing-node {
            background: transparent url("app/assets/images/supply-chain.png") no-repeat !important;
            height: 16px;
        }

        .equipment-node {
            background: transparent url("app/assets/images/wrfw.png") no-repeat !important;
            height: 16px;
        }

        .instrument-node {
            background: transparent url("app/assets/images/preferences.png") no-repeat !important;
            height: 16px;
        }

        .project-node {
            background: transparent url("app/assets/images/book1.png") no-repeat !important;
            height: 16px;
        }

        .sparepart-node {
            background: transparent url("app/assets/images/bom-inclusions.png") no-repeat !important;
            height: 16px;
        }

        .workRequest-node {
            background: transparent url("app/assets/images/prefs1.png") no-repeat !important;
            height: 16px;
        }

        .workOrder-node {
            background: transparent url("app/assets/images/document.png") no-repeat !important;
            height: 16px;
        }

        .mroObject-node {
            background: transparent url("app/assets/images/maintenanceAndRepair.png") no-repeat !important;
            height: 16px;
        }

        .asset-node {
            background: transparent url("app/assets/images/milestoneIcon.png") no-repeat !important;
            height: 16px;
        }

        .meter-node {
            background: transparent url("app/assets/images/measure.png") no-repeat !important;
            height: 16px;
        }

        .supplierType-node {
            background: transparent url("app/assets/images/book1.png") no-repeat !important;
            height: 16px;
        }

        .substance-node {
            background: transparent url("app/assets/images/production.png") no-repeat !important;
            height: 16px;
        }

        .subGroup-node {
            background: transparent url("app/assets/images/manual.png") no-repeat !important;
            height: 16px;
        }

        .specification-node {
            background: transparent url("app/assets/images/box.png") no-repeat !important;
            height: 16px;
        }

        .declaration-node {
            background: transparent url("app/assets/images/lovs.png") no-repeat !important;
            height: 16px;
        }

        .assemblyLine-node {
            background: transparent url("app/assets/images/milestoneIcon.png") no-repeat !important;
            height: 16px;
        }

        .miscellaneous-node {
            background: transparent url("app/assets/images/miscellaneous.png") no-repeat !important;
            height: 16px;
        }

        .masterData-node {
            background: transparent url("app/assets/images/database.png") no-repeat !important;
            height: 16px;
        }

        .toggleColor1 {
            color: #A8A2A2
        }

        .toggleColor2 {
            color: white;
        }

        .my-search {
            border: 0;
            outline: 0;
            background: transparent;
            box-shadow: 0px 0px 0px #d3d3d3;
            border-bottom: 1px solid #d3d3d3;
            margin: 5px 0px;
            width: 210px;
            text-align: center;
        }

        .search-form {
            border-radius: 3px;
            background-color: #eaeaea;
            border: 1px solid #fff;
        }

        .search-form:focus {
            box-shadow: none;
            border: 1px solid #c5cfd5;
        }

        i.clear-search {
            margin-left: -24px;
            color: #aab4b7;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: 12px;
        }

        #classificationContainer .classification-tree {
            position: absolute;
            left: 10px;
            right: 10px;
            top: 100px;
            bottom: 0;
            overflow-y: auto;
        }

        #classificationContainer .search-input i.fa-search {
            position: absolute;
            margin-top: 13px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
            font-size: 12px;
        }

        #classificationContainer .search-input .search-form {
            padding-left: 25px;
            padding-right: 25px;
        }

        .no-type-container {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
        }

        .no-type-container > .no-type {
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            position: absolute;
            width: 100%;
        }

        .no-type-container > .no-type > img {
            width: 300px;
        }

        .no-type-container > .no-type > .no-type-message {
            font-size: 20px;
            font-weight: 400 !important;
        }

        .ng-hide.ng-hide-animate {
            display: none !important;
        }

        .switch {
            position: relative;
            display: inline-block;
            width: 60px;
            height: 34px;
        }

        .switch input {
            opacity: 0;
            width: 0;
            height: 0;
        }

        .switch .slider {
            position: absolute;
            cursor: pointer;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: #ccc;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch .slider:before {
            position: absolute;
            content: "";
            height: 26px;
            width: 26px;
            left: 4px;
            bottom: 4px;
            background-color: white;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch input:checked + .slider {
            background-color: #2196F3;
        }

        .switch input:focus + .slider {
            box-shadow: 0 0 1px #2196F3;
        }

        .switch input:checked + .slider:before {
            -webkit-transform: translateX(26px);
            -ms-transform: translateX(26px);
            transform: translateX(26px);
        }

        /* Rounded sliders */
        .switch .slider.round {
            border-radius: 34px;
        }

        .switch .slider.round:before {
            border-radius: 50%;
        }

        table .ui-select-choices {
            position: absolute !important;
            top: auto !important;
            left: auto !important;
            width: 100% !important;
            bottom: auto;
        }

    </style>

    <div class="view-content  no-padding" style="overflow-y: auto;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="margin-top: -25px">
                <li><a tabindex="-1" id="addType" href="" ng-click="classVm.addType()">
                    <span translate="ADD_TYPE"></span></a></li>
                <li><a tabindex="-1" id="deleteType" href="" ng-click="classVm.deleteType()">
                    <span translate>DELETE_TYPE</span></a>
                </li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
                      translate>CLASSIFICATION_TITLE</span>

                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div class="search-input" style="height: 30px;margin: 10px 0 20px 0;">
                        <i class="fa fa-search"></i>
                        <input type="search" class="form-control input-sm search-form"
                               placeholder={{searchTitle}}
                               ng-model="classVm.searchValue" ng-change="classVm.searchTree()">
                        <i class="las la-times-circle clear-search"
                           ng-show="classVm.searchValue.length > 0"
                           ng-click="classVm.searchValue = '';classVm.searchTree()"></i>
                    </div>
                    <ul id="classificationTree" class="easyui-tree classification-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:300px;overflow: hidden">
                <%--  <div class="no-type-container" ng-if="classVm.loading">
                      <div class="no-type">
                          <img src="app/assets/images/fast-loading.png" alt="">

                          <div class="no-type-message" translate>LOADING_DATA</div>
                          <div class="no-type-suggestion" translate>DATA_RETRIEVED_MESSAGE</div>
                      </div>
                  </div>--%>

                <div class="no-type-container"
                     ng-if="!classVm.loading && classVm.objectTypeAttributes == false && (classVm.type == null || classVm.type == undefined || type == null || type == undefined)
                     || classVm.selectedData.attributes.nodeType == 'CUSTOMNODE' || classVm.selectedData.attributes.nodeType == 'SOURCING' || classVm.selectedData.attributes.nodeType == 'MCONODE'
                     || classVm.selectedData.attributes.nodeType == 'WORKORDERNODE'  || classVm.selectedData.attributes.nodeType == 'MASTERDATANODE'|| (classVm.selectedData.attributes.nodeType == 'PMOBJECTTYPE' && classVm.selectedData.attributes.root == true)">
                    <div class="no-type">
                        <img src="app/assets/images/click.png" alt="">

                        <div class="no-type-message" translate>NO_TYPE_SELECTED</div>
                        <div class="no-type-suggestion" translate>SELECTED_TYPE_MESSAGE</div>
                    </div>

                </div>

                    <div ng-if="!classVm.loading && classVm.objectTypeAttributes == true && (classVm.selectedData.attributes.nodeType != 'CUSTOMNODE' || classVm.selectedData.attributes.nodeType != 'SOURCING' || classVm.selectedData.attributes.nodeType != 'PMOBJECTTYPE'
                || classVm.selectedData.attributes.nodeType != 'WORKORDERNODE' || classVm.selectedData.attributes.nodeType != 'MASTERDATANODE' || classVm.selectedData.attributes.nodeType != 'MCONODE')">
                    <div ng-include="'app/desktop/modules/classification/details/objectTypeAttributesView.jsp'"
                         ng-controller="ObjectTypeAttributesController as objectTypeAttributeVm"></div>
                </div>

                <div ng-hide="!classVm.loading && classVm.objectTypeAttributes == false && (classVm.type == null || classVm.type == undefined || type == null || type == undefined)">
                    <div ng-if="!classVm.loading && (classVm.type == 'ITEMTYPE')"
                         ng-include="'app/desktop/modules/classification/details/itemTypeDetail.jsp'"
                         ng-controller="ItemTypeDetailController as itemTypesVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'CUSTOMOBJECTTYPE')"
                         ng-include="'app/desktop/modules/classification/details/customObjectTypeDetail.jsp'"
                         ng-controller="CustomObjectTypeDetailController as customTypesVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'CHANGETYPE')"
                         ng-include="'app/desktop/modules/classification/details/changeTypeDetailView.jsp'"
                         ng-controller="ChangeTypeDetailController as changeTypesVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'MANUFACTURERTYPE')"
                         ng-include="'app/desktop/modules/classification/details/mfrTypeDetailView.jsp'"
                         ng-controller="MfrTypeDetailController as mfrTypesVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'WORKFLOWTYPE')"
                         ng-include="'app/desktop/modules/classification/details/workflowTypeDetailView.jsp'"
                         ng-controller="WorkflowTypeDetailController as workflowTypesVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'MANUFACTURERPARTTYPE')"
                         ng-include="'app/desktop/modules/classification/details/mfrPartTypeDetailView.jsp'"
                         ng-controller="MfrPartTypeDetailController as mfrPartTypesVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'TOOLTYPE' || classVm.type == 'EQUIPMENTTYPE' || classVm.type == 'INSTRUMENTTYPE' || classVm.type == 'JIGFIXTURETYPE'
                     ||classVm.type == 'MATERIALTYPE' || classVm.type == 'PLANTTYPE' || classVm.type == 'MACHINETYPE' || classVm.type == 'MBOMTYPE' || classVm.type == 'BOPTYPE' || classVm.type == 'PRODUCTIONORDERTYPE' ||
                     classVm.type == 'WORKCENTERTYPE' || classVm.type == 'OPERATIONTYPE'|| classVm.type == 'MANPOWERTYPE' || classVm.type == 'ASSETTYPE'
                     || classVm.type == 'SUPPLIERTYPE'|| classVm.type == 'SPAREPARTTYPE' || classVm.type == 'WORKREQUESTTYPE' || classVm.type == 'WORKORDERTYPE'
                     || classVm.type == 'METERTYPE' || classVm.type == 'ASSEMBLYLINETYPE')"
                         ng-include="'app/desktop/modules/classification/details/mesObjectTypeDetailsView.jsp'"
                         ng-controller="MESObjectTypeDetailsController as mesObjectTypeVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'PGCSUBSTANCETYPE' || classVm.type == 'PGCSUBSTANCEGROUPTYPE' ||
                     classVm.type == 'PGCSPECIFICATIONTYPE' || classVm.type == 'PGCDECLARATIONTYPE')"
                         ng-include="'app/desktop/modules/classification/details/pgcObjectTypeDetailsView.jsp'"
                         ng-controller="PGCObjectTypeDetailsController as pgcObjectTypeVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type =='SPECIFICATIONTYPE')"
                         ng-include="'app/desktop/modules/classification/details/requirementTypeDetailView.jsp'"
                         ng-controller="requirementTypeDetailController as reqTypesVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'QUALITY_TYPE')" style="height: 100%"
                         ng-include="'app/desktop/modules/classification/details/qualityTypeDetailsView.jsp'"
                         ng-controller="QualityTypeDetailsController as qualityTypeVm"></div>
                    <div ng-if="!classVm.loading && (classVm.type == 'PMOBJECTTYPE' || classVm.type == 'REQUIREMENTTYPE' || classVm.type == 'REQUIREMENTDOCUMENTTYPE')"
                         ng-include="'app/desktop/modules/classification/details/pmObjectTypeDetailsView.jsp'"
                         ng-controller="PMObjectTypeDetailsController as pmObjectTypeVm"></div>
                </div>

            </div>
        </div>
    </div>
</div>
