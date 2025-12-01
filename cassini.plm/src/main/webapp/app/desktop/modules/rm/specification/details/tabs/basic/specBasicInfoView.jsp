<style scoped>
</style>
<div>
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-bind-html="selectedSpecification.type.name"></span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedSpecification.objectNumber}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NAME</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" e-style="width:250px" style="word-wrap: break-word;"
                           ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                           ng-if="hasPermission('admin','all') || ((specPermission != null && specPermission.editPermission == true) || hasPermission('pgcspecification','edit'))"
                           onaftersave="specBasicVm.updateSpecification()"
                           editable-text="selectedSpecification.name" ng-bind-html="selectedSpecification.name">
                        </a>


                        <span ng-if="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                              style="word-wrap: break-word;"
                              ng-bind-html="selectedSpecification.name"></span>


                        <span ng-if="!hasPermission('admin','all') && (specPermission == null || specPermission.editPermission == false) && !hasPermission('pgcspecification','edit')"
                              ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                              style="word-wrap: break-word;"
                        <%--ng-class="{disabled:selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'}"--%>
                              ng-bind-html="selectedSpecification.name"></span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DESCRIPTION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" e-style="width:250px" style="word-wrap: break-word;"
                           ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                           ng-if="hasPermission('admin','all') || ((specPermission != null && specPermission.editPermission == true) || hasPermission('pgcspecification','edit'))"
                           onaftersave="specBasicVm.updateSpecification()"
                           editable-text="selectedSpecification.description">
                            <span ng-bind-html="(selectedSpecification.description) ||'CLICK_TO_ENTER_DESCRIPTION' | translate"
                                  title="{{selectedSpecification.description}}"></span>
                        </a>

                       <span ng-if="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                             style="word-wrap: break-word;"
                             ng-bind-html="selectedSpecification.description"></span>

                        <span ng-if="!hasPermission('admin','all') && (specPermission == null || specPermission.editPermission == false) && !hasPermission('pgcspecification','edit')"
                              style="word-wrap: break-word;"
                              ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                              ng-bind-html="selectedSpecification.description"></span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ITEM_ALL_REVISION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedSpecification.revision}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ITEM_ALL_LIFECYCLE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedSpecification.lifecyclePhase.phase}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-bind-html="selectedSpecification.createdByObject.fullName"></span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedSpecification.createdDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-bind-html="selectedSpecification.modifiedByObject.fullName"></span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedSpecification.modifiedDate}}</span>
                    </div>
                </div>
                <basic-attribute-details-view object-type="SPECIFICATION"
                                              quality-type="SPECIFICATION"
                                              has-permission="hasPermission('pgcspecification','edit')"
                                              object-id="selectedSpecification.id"></basic-attribute-details-view>
            </div>
        </div>
    </div>
</div>