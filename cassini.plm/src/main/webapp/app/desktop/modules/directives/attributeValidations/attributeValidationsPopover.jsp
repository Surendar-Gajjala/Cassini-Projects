<div>
    <style scoped>
        .popover {
            max-height: 350px !important;
            min-width: 370px !important;
            overflow-y: auto;
            overflow-x: hidden;
            z-index: 9999;
        }

        .popover-title {
            display: block;
        }
    </style>
    <form class="form-horizontal">
        <div class="form-group" ng-repeat="validation in attribute.newValidations">
            <label class="col-sm-6 control-label">
                <span translate>{{validation.key}}</span> : </label>

            <div class="col-sm-6" style="margin-top: 10px !important;">
                <span ng-if="validation.type == 'text' || validation.type == 'number' || validation.type == 'date' || validation.type == 'select' || validation.type == 'time'">{{validation.value}}</span>
                <span ng-if="validation.type == 'multitext' || validation.type == 'multidate' || validation.type == 'multitime'">{{validation.value.value1}} - {{validation.value.value2}}</span>
                <i ng-if="validation.type == 'checkbox' && validation.value" class="fa fa-check"></i>
            </div>
        </div>
        <div class="form-group" ng-repeat="validation in attribute.attributeDef.newValidations">
            <label class="col-sm-6 control-label">
                <span translate>{{validation.key}}</span> : </label>

            <div class="col-sm-6" style="margin-top: 10px !important;">
                <span ng-if="validation.type == 'text' || validation.type == 'number' || validation.type == 'date' || validation.type == 'select' || validation.type == 'time'">{{validation.value}}</span>
                <span ng-if="validation.type == 'multitext' || validation.type == 'multidate' || validation.type == 'multitime'">{{validation.value.value1}} - {{validation.value.value2}}</span>
                <i ng-if="validation.type == 'checkbox' && validation.value" class="fa fa-check"></i>
            </div>
        </div>
    </form>
</div>