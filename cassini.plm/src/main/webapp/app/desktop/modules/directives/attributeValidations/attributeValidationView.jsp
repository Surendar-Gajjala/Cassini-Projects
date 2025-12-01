<div style="position: relative;">
    <style scoped>
        .attributes-validations {

        }

        .attributes-validations .form-control {
            padding: 7px;
        }
    </style>
    <div class="attributes-validations" style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group" ng-repeat="validation in attributeValidationVm.attributeValidations">
                        <label class="col-sm-6 control-label">
                            <span translate>{{validation.key}}</span>
                            <%--<span class="asterisk">*</span>--%> : </label>

                        <div class="col-sm-5">
                            <input class="form-control" type="text" ng-if="validation.type == 'text'"
                                   ng-model="validation.value"/>
                            <input class="form-control" type="number" ng-if="validation.type == 'number'"
                                   ng-model="validation.value"/>
                            <input class="form-control" type="text" ng-if="validation.type == 'date'"
                                   ng-model="validation.value" date-picker/>
                            <input class="form-control" type="text" name="timeValidation"
                                   ng-if="validation.type == 'time'"
                                   ng-model="validation.value" time-picker/>
                            <input class="form-control" type="checkbox" ng-if="validation.type == 'checkbox'"
                                   ng-disabled="!checkValidation(validation)" style="width: 25px;margin-top: 15px;"
                                   ng-model="validation.value"/>
                            <ui-select ng-model="validation.value" theme="bootstrap"
                                       style="width:100%" ng-if="validation.type == 'select'">
                                <ui-select-match placeholder="Select format">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices repeat="value in validation.values">
                                    <div>{{value}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </form>
                <br><br><br>
            </div>
        </div>
    </div>
</div>
