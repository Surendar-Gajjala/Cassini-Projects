<div>
    <style scoped>

        #sideNavigation {
            position: fixed;
            top: 50px;
            bottom: 30px;
            width: 290px;
            z-index: 9999;
            left: -300px;
            background-color: var(--cassini-theme-light-color);
            border-right: 1px solid var(--cassini-theme-light-color);
            content: '';
            display: block;
            overflow-y: auto;
            font-weight: 300;
        }

        #sideNavigation .sidebartitle {
            color: #fff !important;
            opacity: 0.7;
        }

        #sideNavigation .nav-bracket .children > li .fa {
            opacity: 0.7 !important;
        }

        #sideNavigation .nav-bracket > li.nav-active > a,
        #sideNavigation .nav-bracket > li > a:hover,
        #sideNavigation .nav-bracket > li.nav-parent > a:hover,
        #sideNavigation .nav-bracket > li.nav-parent > a:active,
        #sideNavigation .nav-bracket > li.nav-parent > a:focus {
            background-color: var(--cassini-theme-dark-color) !important;
        }

        #sideNavigation .nav-bracket .children {
            background-color: var(--cassini-theme-lighter-color);
            border-bottom: 0;
        }

        #sideNavigation .nav-bracket > li > a {
            border-bottom: none !important;
            color: #fff !important;
            opacity: 0.7;
        }

        #sideNavigation .nav-bracket .children > li > a {
            color: #fff !important;
            opacity: 0.7;
        }

        #sideNavigation .nav-parent > ul > li > a:hover {
            background-color: var(--cassini-theme-more-lighter-color);
        }

        #sideNavigation .leftpanelinner {
            margin-bottom: 40px;
        }

        #sideNavigation span {
            display: contents !important;
        }

        #sideNavigation h5 {
            padding-left: 10px;
        }

        #sideNavigation .flaticon-office42::before {
            font-size: 16px;
        }

        .nav-shortcuts-panel {
            position: fixed;
            bottom: 30px !important;
            height: 40px;
            background-color: var(--cassini-theme-lighter-color);
            line-height: 30px;
            display: flex;
            width: 290px;
            z-index: 9999;
            left: -300px;
            justify-content: center;
            align-items: center;
            border-top: 1px solid var(--cassini-theme-lighter-color);
        }

        .nav-shortcuts-panel > a {
            color: #fff;
            opacity: 0.7;
            text-align: center;
            width: 30px;
            height: 30px;
            margin-left: 20px;
            margin-right: 20px;
        }

        .nav-shortcuts-panel > a:hover {
            background-color: var(--cassini-theme-dark-color);
            border-radius: 3px;
            border: 1px solid var(--cassini-theme-dark-color);
        }
    </style>
    <div id="sideNavigation" class="">
        <div class="leftpanelinner">
            <h5 class="sidebartitle text-center" style="padding-left: 0;">Navigation</h5>
            <ul class="nav nav-pills nav-stacked nav-bracket">
                <li id="nav-home">
                    <a ui-sref="app.home">
                        <i class="fa fa-home" style="font-size: 15px;"></i>
                        <span class="hidden-xs hidden-sm" translate="HOME_TITLE"></span>
                    </a>
                </li>

                <li class="nav-parent" id="dashboards"
                    ng-show="checkDashboardPermission('view') && isProfileMenu('dashboard.menu')">
                    <a href=""><i class="fa fa-bar-chart nav-icon-font"></i> <span
                            translate>DASHBOARDS</span></a>
                    <ul class="children" style="">
                        <li id="dashboardItems"
                            ng-show="hasPermission('dashboard', 'item', 'view') && isProfileMenu('dashboard.items')">
                            <a ui-sref="app.dashboard.items">
                                <span class="hidden-xs hidden-sm" translate>ITEMS_ALL_TITLE</span>
                            </a>
                        </li>
                        <li id="dashboardChanges"
                            ng-show="hasPermission('dashboard', 'change', 'view') && isProfileMenu('dashboard.changes')">
                            <a ui-sref="app.dashboard.changes">
                                <span class="hidden-xs hidden-sm" translate>CHANGES_TITLE</span>
                            </a>
                        </li>
                        <li id="dashboardQuality"
                            ng-show="hasPermission('dashboard', 'quality', 'view') && isProfileMenu('dashboard.quality')">
                            <a ui-sref="app.dashboard.quality">
                                <span class="hidden-xs hidden-sm" translate>QUALITY</span>
                            </a>
                        </li>
                        <li id="dashboardProjects"
                            ng-show="hasPermission('dashboard', 'project', 'view') && isProfileMenu('dashboard.project')">
                            <a ui-sref="app.dashboard.projects">
                                <span class="hidden-xs hidden-sm" translate>NO_OF_PROJECTS</span>
                            </a>
                        </li>
                        <li id="dashboardWorkflows"
                            ng-show="hasPermission('dashboard', 'workflow', 'view') && isProfileMenu('dashboard.workflow')">
                            <a ui-sref="app.dashboard.workflows">
                                <span class="hidden-xs hidden-sm" translate>NO_OF_WORKFLOWS</span>
                            </a>
                        </li>
                        <li id="dashboardSourcing"
                            ng-show="hasPermission('dashboard', 'sourcing', 'view') && isProfileMenu('dashboard.sourcing')">
                            <a ui-sref="app.dashboard.oems">
                                <span class="hidden-xs hidden-sm" translate>SOURCING</span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-parent" id="designData"
                    ng-show="checkPDMPermission('view') && isProfileMenu('designData.menu')">
                    <a href=""><i class="fa fa-database nav-icon-font"></i> <span
                            translate>DESIGN_DATA</span></a>
                    <ul class="children" style="">
                        <li id="vaults"
                            ng-show="isProfileMenu('designData.vault') && (hasPermission('pdm_vault', 'view') || hasPermission('pdmobject', 'all'))">
                            <a ui-sref="app.pdm.vaults">
                                <span class="hidden-xs hidden-sm" translate>VAULTS</span>
                            </a>
                        </li>
                        <li id="assemblies"
                            ng-show="isProfileMenu('designData.assemblies') && (hasPermission('pdm_assembly', 'view') || hasPermission('pdmobject', 'all'))">
                            <a ui-sref="app.pdm.files({fileType: 'ASSEMBLY'})">
                                <span class="hidden-xs hidden-sm" translate>ASSEMBLIES</span>
                            </a>
                        </li>
                        <li id="parts"
                            ng-show="isProfileMenu('designData.parts') && (hasPermission('pdm_part', 'view') || hasPermission('pdmobject', 'all'))">
                            <a ui-sref="app.pdm.files({fileType: 'PART'})">
                                <span class="hidden-xs hidden-sm" translate>PARTS</span>
                            </a>
                        </li>
                        <li id="drawings"
                            ng-show="isProfileMenu('designData.drawings') && (hasPermission('pdm_drawing', 'view') || hasPermission('pdmobject', 'all'))">
                            <a ui-sref="app.pdm.files({fileType: 'DRAWING'})">
                                <span class="hidden-xs hidden-sm" translate>DRAWINGS</span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-parent"
                    ng-show="hasPermission('item','view') && isProfileMenu('item.menu')">
                    <a href="" id="items-menu"><i class="fa fa-cogs nav-icon-font"></i> <span
                            translate>ITEMS_ALL_TITLE</span></a>
                    <ul class="children" style="">
                        <li ng-show="isProfileMenu('item.items')">
                            <a id="all-items" ui-sref="app.items.all({itemMode:'all'})" ng-click="mainVm.loadItems()">
                                <span class="hidden-xs hidden-sm" translate>ALL_ITEMS</span>
                            </a>
                        </li>
                        <li ng-show="isProfileMenu('item.products')">
                            <a id="product-items" ui-sref="app.items.all({itemMode:'products'})"
                               ng-click="mainVm.loadItems()">
                                <span class="hidden-xs hidden-sm" translate>PRODUCTS</span>
                            </a>
                        </li>
                        <li id="assemblie-items" ng-show="isProfileMenu('item.assemblies')">
                            <a ui-sref="app.items.all({itemMode:'assemblies'})" ng-click="mainVm.loadItems()">
                                <span class="hidden-xs hidden-sm" translate>ASSEMBLIES</span>
                            </a>
                        </li>
                        <li id="partitems" ng-show="isProfileMenu('item.parts')">
                            <a ui-sref="app.items.all({itemMode:'parts'})" ng-click="mainVm.loadItems()">
                                <span class="hidden-xs hidden-sm" translate>PARTS</span>
                            </a>
                        </li>
                        <li id="document-items" ng-show="isProfileMenu('item.documents')">
                            <a ui-sref="app.items.all({itemMode:'documents'})" ng-click="mainVm.loadItems()">
                                <span class="hidden-xs hidden-sm" translate>DOCUMENTS</span>
                            </a>
                        </li>
                        <!--
                        <li>
                            <a ui-sref="app.items.all({itemMode:'others'})">
                                <span class="hidden-xs hidden-sm">Others</span>
                            </a>
                        </li>
                        -->
                        <li id="newPartRequest">
                            <a ng-show="isProfileMenu('item.newPartRequests') && hasPermission('plmnpr','view')"
                               id="newPart" ui-sref="app.nprs.all">
                                <span class="hidden-xs hidden-sm" translate>NEW_PART_REQUESTS</span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-parent"
                    ng-show="customObjectTypes.length > 0">
                    <a href="" id="items-menu1"><i class="fa fa-copyright nav-icon-font"></i> <span
                            translate>CUSTOM_OBJECT_TITLE</span></a>
                    <ul class="children" style="">
                        <li>
                            <a ng-repeat="customObject in customObjectTypes"
                               ui-sref="app.customobjects.all({typeName:customObject.name})"
                               ng-click="mainVm.preserveCustomObjectParam(customObject.name)">
                                <span class="hidden-xs hidden-sm" translate>{{customObject.name}}</span>
                            </a>
                        </li>
                    </ul>
                </li>


                <li class="nav-parent" id="changes"
                    ng-show="checkChangePermission('view') && isProfileMenu('change.menu')">
                    <a href=""><i class="fa fa-ils nav-icon-font"></i> <span
                            translate>ITEM_DETAILS_TAB_CHANGES</span></a>
                    <ul class="children" style="">
                        <li id="ecrs"
                            ng-if="(hasPermission('change','ecr','view') || hasPermission('change','view')) && isProfileMenu('change.ecrs')">
                            <a ui-sref="app.changes.ecr.all">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>ENGINEERING_CHANGE_REQUESTS</span>
                            </a>
                        </li>
                        <li id="ecos"
                            ng-if="(hasPermission('change','eco','view') || hasPermission('change','view')) && isProfileMenu('change.ecos')">
                            <a ui-sref="app.changes.eco.all" ng-click="mainVm.loadEcos()">
                                <%--<i class="fa fa-calendar" style="font-size: 15px"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>ENGINEERING_CHANGE_ORDERS</span>
                            </a>
                        </li>
                        <li id="dcrs"
                            ng-if="(hasPermission('change','dcr','view') || hasPermission('change','view')) && isProfileMenu('change.dcrs')">
                            <a ui-sref="app.changes.dcr.all">
                                <%--<i class="fa fa-calendar" style="font-size: 15px"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>DOCUMENT_CHANGE_REQUESTS</span>
                            </a>
                        </li>
                        <li id="dcos"
                            ng-if="(hasPermission('change','dco','view') || hasPermission('change','view')) && isProfileMenu('change.dcos')">
                            <a ui-sref="app.changes.dco.all">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>DOCUMENT_CHANGE_ORDERS</span>
                            </a>
                        </li>
                        <li id="mcos"
                            ng-if="(hasPermission('change','mco','view') || hasPermission('change','view')) && isProfileMenu('change.mcos')">
                            <a ui-sref="app.changes.mco.all">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>MANUFACTURING_CHANGE_ORDERS</span>
                            </a>
                        </li>
                        <li ng-if="(hasPermission('change','deviation','view') || hasPermission('change','waiver','view')
                            || hasPermission('change','view')) &&
                        (isProfileMenu('change.variances-deviations') || isProfileMenu('change.variances-waivers'))">
                            <a>
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                    <span class="hidden-xs hidden-sm"
                                          style="font-size: 14px;font-weight: 600;opacity: 1"
                                          translate>VARIANCES</span>
                            </a>
                        </li>

                        <li id="deviations"
                            ng-if="(hasPermission('change','deviation','view') || hasPermission('change','view')) && isProfileMenu('change.variances-deviations')">
                            <a ui-sref="app.changes.variance.all({varianceMode:'deviation'})"
                               ng-click="mainVm.getDeviations()"
                               style="padding-left: 50px;">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>DEVIATIONS</span>
                            </a>
                        </li>
                        <li id="waivers"
                            ng-if="(hasPermission('change','waiver','view') || hasPermission('change','view')) && isProfileMenu('change.variances-waivers')">
                            <a ui-sref="app.changes.variance.all({varianceMode:'waiver'})"
                               ng-click="mainVm.getWaivers()"
                               style="padding-left: 50px;">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>WAIVERS</span>
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="nav-parent" id="quality"
                    ng-show="(hasPermission('inspectionplan','view') || hasPermission('inspection','view') || hasPermission('problemreport','view')
                    || hasPermission('inspectionplan','view') || hasPermission('ncr','view') || hasPermission('qcr','view')) && isProfileMenu('quality.menu')">
                    <a href=""><i class="fa flaticon-prize3 nav-icon-font"></i> <span translate>QUALITY</span></a>
                    <ul class="children" style="">
                        <li id="inspectionPlans"
                            ng-if="hasPermission('inspectionplan','view') && isProfileMenu('quality.inspectionPlan')">
                            <a ui-sref="app.pqm.inspectionPlan.all">
                                <%--<i class="fa fa-calendar" style="font-size: 15px"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>INSPECTION_PLANS</span>
                            </a>
                        </li>
                        <li id="inspections"
                            ng-if="hasPermission('inspection','view') && isProfileMenu('quality.inspections')">
                            <a ui-sref="app.pqm.inspection.all">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>INSPECTIONS</span>
                            </a>
                        </li>
                        <li id="problemReports"
                            ng-if="hasPermission('problemreport','view') && isProfileMenu('quality.prs')">
                            <a ui-sref="app.pqm.pr.all">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>PROBLEM_REPORTS</span>
                            </a>
                        </li>
                        <li id="ncrs" ng-if="hasPermission('ncr','view') && isProfileMenu('quality.ncrs')">
                            <a ui-sref="app.pqm.ncr.all">
                                <%--<i class="fa fa-calendar" style="font-size: 15px"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>NON_CONFORMANCE_REPORTS</span>
                            </a>
                        </li>
                        <li id="qcrs" ng-if="hasPermission('qcr','view') && isProfileMenu('quality.qcrs')">
                            <a ui-sref="app.pqm.qcr.all">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>QUALITY_CHANGE_REQUESTS</span>
                            </a>
                        </li>


                        <li id="ppap">
                            <a ui-sref="app.pqm.ppap.all" ng-if="hasPermission('ppap','view') ">
                                <%--<i class="fa fa-wpforms" style="font-size: 15px;"></i>--%>
                                <span class="hidden-xs hidden-sm" translate>PPAP</span>
                            </a>
                        </li>


                        <li id="supplierAudit">
                            <a ui-sref="app.pqm.supplierAudit.all" ng-if="hasPermission('supplieraudit','view')">
                                <span class="hidden-xs hidden-sm" translate>SUPPLIER_AUDIT</span>
                            </a>
                        </li>


                    </ul>
                </li>

                <li class="nav-parent" id="projectManagement"
                    ng-show="(hasPermission('requirement','view') || hasPermission('project','view') || hasPermission('template','view') || hasPermission('program','view')) && (isProfileMenu('project-management.menu') || isProfileMenu('project-management.requirement-doc'))">
                    <a href=""><i class="fa fa-calendar"></i> <span translate="PROJ_MANAGEMENT"></span></a>
                    <ul class="children" style="">
                        <li id="programs">
                            <a ui-sref="app.pm.program.all"
                               ng-if="hasPermission('program','view') && isProfileMenu('project-management.programs')">
                                <span class="hidden-xs hidden-sm" translate="PROGRAMS"></span>
                            </a>
                        </li>
                        <li id="programs">
                            <a ui-sref="app.pm.programtemplate.all"
                               ng-if="hasPermission('programtemplate','view') && isProfileMenu('project-management.programs-temp')">
                                <span class="hidden-xs hidden-sm" translate="PROGRAM_TEMPLATES"></span>
                            </a>
                        </li>
                        <li id="projects">
                            <a ui-sref="app.pm.project.all"
                               ng-if="hasPermission('project','view') && isProfileMenu('project-management.projects')">
                                <span class="hidden-xs hidden-sm" translate="PROJECTS"></span>
                            </a>
                        </li>
                        <li id="projectTemplates">
                            <a ui-sref="app.templates.all"
                               ng-if="hasPermission('template','view') && isProfileMenu('project-management.projects-temp')">
                                <span class="hidden-xs hidden-sm" translate="PROJECT_TEMPLATES"></span>
                            </a>
                        </li>

                        <li id="requirements">
                            <a ui-sref="app.req.document.all"
                               ng-if="hasPermission('requirement','view') && isProfileMenu('project-management.requirements')">
                                <span class="hidden-xs hidden-sm" translate="REQUIREMENTS"></span>
                            </a>
                        </li>
                        <li id="requirementTemplates">
                            <a ui-sref="app.req.document.template.all"
                               ng-if="hasPermission('template','view') && isProfileMenu('project-management.requirements-temp')">
                                <span class="hidden-xs hidden-sm" translate="REQUIREMENT_TEMPLATE"></span>
                            </a>
                        </li>
                    </ul>
                </li>

                <%--
                <li class="nav-parent"
                    ng-show="hasPermission('terminology','view') || hasPermission('pgcspecification','*') || hasPermission('requirement','*')">
                    <a href=""><i class="fa fa-wpforms"></i> <span translate="REQUIREMENT_TAB"></span></a>
                    <ul class="children" style="">
                        <li>
                            <a ui-sref="app.rm.specifications.all"
                               ng-show="hasPermission('pgcspecification','*') || hasPermission('requirement','*')">
                                <i class="fa fa-wpforms" style="font-size: 15px"></i>
                                <span class="hidden-xs hidden-sm" translate="SPECIFICATIONS"></span>
                            </a>
                        </li>
                        <li>
                            <a ui-sref="app.rm.glossary.all"
                               ng-show="hasPermission('terminology','all') || hasPermission('terminology','view')">
                                <i class="fa fa-book" style="font-size: 15px;"></i>
                                <span class="hidden-xs hidden-sm" translate="TERMINOLOGY"></span>
                            </a>
                        </li>
                    </ul>
                </li>

                --%>
                <li class="nav-parent" id="manufacturing" ng-show="(hasPermission('plant','view') || hasPermission('workcenter','view')
                        || hasPermission('machine','view') || hasPermission('equipment','view')
                        || hasPermission('instrument','view') || hasPermission('tool','view')
                        || hasPermission('jigfixture','view') || hasPermission('material','view')
                        || hasPermission('manpower','view') || hasPermission('shift','view') || hasPermission('bop','view')
                        || hasPermission('operation','view') || hasPermission('productionorder','view') || hasPermission('mbom','view'))
                        || hasPermission('assemblyline','view') || hasPermission('serviceorder','view') && isProfileMenu('manufacturing.menu')">
                    <a href=""><i class="fa fa-university nav-icon-font"></i> <span
                            translate>MANUFACTURING</span></a>
                    <ul class="children" style="">
                        <li id="mes-masterData">
                            <a ui-sref="app.mes.masterData">
                                <span class="hidden-xs hidden-sm" translate>MASTER_DATA</span>
                            </a>
                        </li>
                        <%--<li id="plantType"
                            ng-show="isProfileMenu('manufacturing.plants') && hasPermission('plant','view')">
                            <a ui-sref="app.mes.masterData.plant.all">
                                <span class="hidden-xs hidden-sm" translate>PLANT_TYPE</span>
                            </a>
                        </li>
                        <li id="assembliLines"
                            ng-show="isProfileMenu('manufacturing.assemblyLines') && hasPermission('assemblyline','view')">
                            <a ui-sref="app.mes.masterData.assemblyline.all">
                                <span class="hidden-xs hidden-sm" translate>ASSEMBLY_LINES</span>
                            </a>
                        </li>
                        <li id="workCenters"
                            ng-show="isProfileMenu('manufacturing.work-centers') && hasPermission('workcenter','view')">
                            <a ui-sref="app.mes.masterData.workcenter.all">
                                <span class="hidden-xs hidden-sm" translate>WORK_CENTER</span>
                            </a>
                        </li>
                        <li id="machines"
                            ng-show="isProfileMenu('manufacturing.machines') && hasPermission('machine','view')">
                            <a ui-sref="app.mes.masterData.machine.all">
                                <span class="hidden-xs hidden-sm" translate>MACHINE_TITLE</span>
                            </a>
                        </li>
                        <li id="equipments"
                            ng-show="isProfileMenu('manufacturing.equipments') && hasPermission('equipment','view')">
                            <a ui-sref="app.mes.masterData.equipment.all">
                                <span class="hidden-xs hidden-sm" translate>EQUIPMENTS</span>
                            </a>
                        </li>
                        <li id="instruments"
                            ng-show="isProfileMenu('manufacturing.instruments') && hasPermission('instrument','view')">
                            <a ui-sref="app.mes.masterData.instrument.all">
                                <span class="hidden-xs hidden-sm" translate>INSTRUMENTS</span>
                            </a>
                        </li>
                        <li id="toolType"
                            ng-show="isProfileMenu('manufacturing.tools') && hasPermission('tool','view')">
                            <a ui-sref="app.mes.masterData.tool.all">
                                <span class="hidden-xs hidden-sm" translate>TOOLS</span>
                            </a>
                        </li>
                        <li id="jigsAndFixtures"
                            ng-show="isProfileMenu('manufacturing.jigs-fixtures') && hasPermission('jigfixture','view')">
                            <a ui-sref="app.mes.masterData.jigsAndFixtures.all">
                                <span class="hidden-xs hidden-sm" translate>JIGS_FIXTURES</span>
                            </a>
                        </li>
                        <li id="materials"
                            ng-show="isProfileMenu('manufacturing.materials') && hasPermission('material','view')">
                            <a ui-sref="app.mes.masterData.material.all">
                                <span class="hidden-xs hidden-sm" translate>MATERIALS</span>
                            </a>
                        </li>
                        <li id="manpower"
                            ng-show="isProfileMenu('manufacturing.manpower') && hasPermission('manpower','view')">
                            <a ui-sref="app.mes.masterData.manpower.all">
                                <span class="hidden-xs hidden-sm" translate>MANPOWER</span>
                            </a>
                        </li>
                        <li id="shifts"
                            ng-show="isProfileMenu('manufacturing.shifts') && hasPermission('shift','view')">
                            <a ui-sref="app.mes.masterData.shift.all">
                                <span class="hidden-xs hidden-sm" translate>SHIFTS</span>
                            </a>
                        </li>
                        <li id="operations"
                            ng-show="isProfileMenu('manufacturing.operations') && hasPermission('operation','view')">
                            <a ui-sref="app.mes.masterData.operation.all">
                                <span class="hidden-xs hidden-sm" translate>OPERATIONS</span>
                            </a>
                        </li>--%>
                        <li id="mbom" ng-show="isProfileMenu('manufacturing.mboms') && hasPermission('mbom','view')">
                            <a ui-sref="app.mes.mbom.all">
                                <span class="hidden-xs hidden-sm" translate>MBOMS</span>
                            </a>
                        </li>
                        <li id="bop" ng-show="isProfileMenu('manufacturing.bop') && hasPermission('bop','view')">
                            <a ui-sref="app.mes.bop.all">
                                <span class="hidden-xs hidden-sm" translate>BOP</span>
                            </a>
                        </li>
                        <li id="productionOrder"
                            ng-show="isProfileMenu('manufacturing.production-orders') && hasPermission('productionorder','view')">
                            <a ui-sref="app.mes.productionOrder.all">
                                <span class="hidden-xs hidden-sm" translate>PRODUCTION_ORDER_TITLE</span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-parent" id="maintenanceAndRepairs" ng-show="(hasPermission('mroasset','view') || hasPermission('mrometer','view')
                                        || hasPermission('mrosparepart','view') || hasPermission('mromaintenanceplan','view')
                                        || hasPermission('mroworkrequest','view') || hasPermission('mroworkorder','view'))
                            && isProfileMenu('maintenance-repairs.menu')">
                    <a href=""><i class="fa fa-wrench nav-icon-font"></i> <span
                            translate="MAINTENANCEANDREPAIRS"></span></a>
                    <ul class="children" style="">
                        <li id="assets"
                            ng-show="isProfileMenu('maintenance-repairs.assets') && hasPermission('mroasset','view')">
                            <a ui-sref="app.mro.asset.all" style="">
                                <span class="hidden-xs hidden-sm" translate>ASSETS</span>
                            </a>
                        </li>

                        <li id="meters"
                            ng-show="isProfileMenu('maintenance-repairs.meters') && hasPermission('mrometer','view')">
                            <a ui-sref="app.mro.meter.all" style="">
                                <span class="hidden-xs hidden-sm" translate>METERS</span>
                            </a>
                        </li>

                        <li id="spareParts"
                            ng-show="isProfileMenu('maintenance-repairs.spare-parts') && hasPermission('mrosparepart','view')">
                            <a ui-sref="app.mro.sparePart.all" style="">
                                <span class="hidden-xs hidden-sm" translate>SPARE_PARTS</span>
                            </a>
                        </li>
                        <li id="maintenancePlans"
                            ng-show="isProfileMenu('maintenance-repairs.maintenance-plans') && hasPermission('mromaintenanceplan','view')">
                            <a ui-sref="app.mro.maintenancePlan.all" style="">
                                <span class="hidden-xs hidden-sm" translate>MAINTENANCE_PLANS</span>
                            </a>
                        </li>
                        <li id="workRequests"
                            ng-show="isProfileMenu('maintenance-repairs.work-requests') && hasPermission('mroworkrequest','view')">
                            <a ui-sref="app.mro.workRequest.all" style="">
                                <span class="hidden-xs hidden-sm" translate>WORK_REQUESTS</span>
                            </a>
                        </li>
                        <li id="workOrders"
                            ng-show="isProfileMenu('maintenance-repairs.work-orders') && hasPermission('mroworkorder','view')">
                            <a ui-sref="app.mro.workOrder.all" style="">
                                <span class="hidden-xs hidden-sm" translate>WORK_ORDERS</span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-parent" id="oem"
                    ng-show="(hasPermission('manufacturer','view') || hasPermission('manufacturerpart','view')
                                   || hasPermission('mfrsupplier','view')) && isProfileMenu('sourcing.menu')">
                    <a href=""><i class="fa flaticon-office42"></i> <span translate="OEM"></span></a>
                    <ul class="children" style="">
                        <li id="manufacturers"
                            ng-show="isProfileMenu('sourcing.manufacturers') && hasPermission('manufacturer','view')">
                            <a ui-sref="app.mfr.all">
                                <span class="hidden-xs hidden-sm" translate="MANUFACTURERS_TITLE"></span>
                            </a>
                        </li>
                        <li id="manufacturerParts"
                            ng-show="isProfileMenu('sourcing.manufacturers-parts') && hasPermission('manufacturerpart','view')">
                            <a ui-sref="app.mfr.mfrparts.all">
                                <span class="hidden-xs hidden-sm" translate="MANUFACTURER_PARTS"></span>
                            </a>
                        </li>
                        <li id="suppliers"
                            ng-show="isProfileMenu('sourcing.suppliers') && hasPermission('mfrsupplier','view')">
                            <a ui-sref="app.mfr.supplier.all">
                                <span class="hidden-xs hidden-sm" translate>SUPPLIERS</span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-parent" id="compliance" ng-show="(hasPermission('pgcsubstance','view') || hasPermission('pgcspecification','view')
                                || hasPermission('pgcdeclaration','view')) && isProfileMenu('compliance.menu')">
                    <a href=""><i class="fa fa-book nav-icon-font"></i> <span
                            translate>COMPLIANCE</span></a>
                    <ul class="children" style="">
                        <li id="substances"
                            ng-show="isProfileMenu('compliance.substances') && hasPermission('pgcsubstance','view')">
                            <a ui-sref="app.compliance.substance.all" style="">
                                <span class="hidden-xs hidden-sm" translate>SUBSTANCES</span>
                            </a>
                        </li>

                        <li id="specifications"
                            ng-show="isProfileMenu('compliance.specifications') && hasPermission('pgcspecification','view')">
                            <a ui-sref="app.compliance.specification.all" style="">
                                <span class="hidden-xs hidden-sm" translate>SPECIFICATIONS</span>
                            </a>
                        </li>

                        <li id="declarations"
                            ng-show="isProfileMenu('compliance.declarations') && hasPermission('pgcdeclaration','view')">
                            <a ui-sref="app.compliance.declaration.all" style="">
                                <span class="hidden-xs hidden-sm" translate>DECLARATIONS</span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-parent"
                    ng-show="(hasPermission('plmworkflowdefinition','view') || hasPermission('plmworkflow','view')) && (isProfileMenu('workflows.menu') || isProfileMenu('workflows.instances'))">
                    <a href=""><i class="fa flaticon-plan2 nav-icon-font"></i> <span
                            translate="WORKFLOW_ALL_TITLE"></span></a>
                    <ul class="children" style="">
                        <li ng-show="hasPermission('plmworkflowdefinition','view') && isProfileMenu('workflows.templates')">
                            <a ui-sref="app.workflow.all">
                                <span class="hidden-xs hidden-sm" translate="WORKFLOW_TEMPLATES"></span>
                            </a>
                        </li>
                        <li ng-show="hasPermission('plmworkflow','view') && isProfileMenu('workflows.instances')">
                            <a ui-sref="app.workflow.instances">
                                <span class="hidden-xs hidden-sm" translate="WORKFLOW_INSTANCES"></span>
                            </a>
                        </li>
                    </ul>
                </li>

                <li ng-show="hasPermission('document','view') || isDmPermissions">
                    <a ui-sref="app.dm.all">
                        <i class="fa fa-file nav-icon-font"
                           style="font-size: 16px !important;margin-right: 10px;"></i>
                        <span class="hidden-xs hidden-sm" translate>Document Management</span>
                    </a>
                </li>

                <li>
                    <a id="customers" ui-sref="app.customers.all"
                       ng-show="hasPermission('customer','view') && isProfileMenu('customers.menu')">
                        <i class="fa flaticon-key4 nav-icon-font"
                           style="font-size: 16px !important;margin-right: 10px;"></i>
                        <span class="hidden-xs hidden-sm" translate="CUSTOMERS_TITLE"></span>
                    </a>
                </li>
                <!--
                <li>
                    <a ui-sref="app.suppliers.all" ng-show="hasPermission('supplier','view')">
                        <i class="las la-shipping-fast nav-icon-font"
                           style="font-size: 16px !important;margin-right: 10px;"></i>
                        <span class="hidden-xs hidden-sm" translate="SUPPLIERS_TITLE"></span>
                    </a>
                </li>
                -->

                <li class="nav-parent"
                    ng-show="isProfileMenu('import-export.menu') && (hasPermission('all','import') || hasPermission('all','import'))">
                    <a href=""><i class="fa fa-download"></i> <span translate="IMPORT_EXPORT"></span></a>
                    <ul class="children" style="">
                        <li ng-show="hasPermission('all','import')">
                            <a ng-click="mainVm.importIndividualObjects()">
                                <i class="fa fa-download" style="font-size: 15px"></i>
                                <span class="hidden-xs hidden-sm" translate="IMPORT"></span>
                            </a>
                        </li>
                        <li ng-show="isProfileMenu('import-export.import') && hasPermission('all','import')">
                            <a ui-sref="app.exim.import">
                                <i class="fa fa-download" style="font-size: 15px"></i>
                                <span class="hidden-xs hidden-sm" translate="BOM_IMPORT_SIDE_NAV"></span>
                            </a>
                        </li>
                        <li ng-show="isProfileMenu('import-export.export') && hasPermission('all','import')">
                            <a ui-sref="app.exim.export">
                                <i class="fa fa-upload" style="font-size: 15px;"></i>
                                <span class="hidden-xs hidden-sm" translate="EXPORT"></span>
                            </a>
                        </li>
                    </ul>
                </li>

                <!-- Sidenav Extensions -->
                <li class="nav-parent" ng-repeat="sidenav in mainVm.sidenavExtensions">
                    <a href="">
                        <i class="fa {{sidenav.icon}}"></i>
                        <span>{{sidenav.name}}</span>
                    </a>
                    <ul class="children" style="">
                        <li ng-repeat="item in sidenav.items">
                            <a ng-if="item.routeparam !=null && item.routeparam !=''"
                               ui-sref="{{item.route}}({typeName:item.routeparam})"
                               ng-click="mainVm.preserveCustomObjectParam(item.routeparam)">
                                <i class="fa {{item.icon}}" style="font-size: 15px"></i>
                                <span class="hidden-xs hidden-sm">{{item.name}}</span>
                            </a>
                            <a ng-if="item.routeparam ==null || item.routeparam =='' || item.routeparam ==undefined"
                               ui-sref="{{item.route}}">
                                <i class="fa {{item.icon}}" style="font-size: 15px"></i>
                                <span class="hidden-xs hidden-sm">{{item.name}}</span>
                            </a>


                        </li>
                    </ul>
                </li>

                <li id="nav-class">
                    <a ui-sref="app.classification"
                       ng-show="hasPermission('classification','view') && isProfileMenu('classification.menu')">
                        <i class="fa flaticon-marketing8 nav-icon-font" style="font-size: 15px;"></i>
                        <span class="hidden-xs hidden-sm" translate="CLASSIFICATION_TITLE"></span>
                    </a>
                </li>

                <li id="nav-admin">
                    <a ui-sref="app.newadmin.users" class=""
                       ng-if="(hasPermission('admin','all') || hasPermission('admin','view')) && isProfileMenu('admin.menu')">
                        <i class="las la-users-cog nav-icon-font"
                           style="font-size: 15px;margin-right: 10px !important;"></i>
                        <span class="hidden-xs hidden-sm" translate="ADMIN"></span>
                    </a>
                </li>
            </ul>
        </div>
        <div id="navShortcutsPanel" class="nav-shortcuts-panel"
             ng-show="isProfileMenu('settings.menu') || isProfileMenu('sharing.menu') || isProfileMenu('folders.menu')
                || hasPermission('folder', 'view')">
            <a id="settings" ui-sref="app.settings" class=""
               ng-if="hasPermission('settings','all') && isProfileMenu('settings.menu')"
               title="{{'SETTINGS_TITLE' | translate}}">
                <i class="las la-tools nav-icon-font" style="font-size: 15px;"></i>
            </a>
            <a id="sharing" ui-sref="app.sharing.all"
               ng-show="hasPermission('sharing','all') && isProfileMenu('sharing.menu')"
               title="{{'SHARING' | translate}}">
                <i class="las la-share nav-icon-font" style="font-size: 15px;"></i>
            </a>
            <a id="folders" href="" ng-click="mainVm.showFoldersTree()"
               ng-if="!mainVm.user.external && isProfileMenu('folders.menu') && hasPermission('folder','view')"
               title="{{'FOLDERS_TITLE' | translate}}">
                <i class="fa fa-folder-open" style="font-size: 15px;"></i>
            </a>
        </div>
    </div>

</div>