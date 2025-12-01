<style>
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
    span.closeimage {
        position: absolute !important;
        top: 50px !important;
        right: 50px !important;

        font-size: 40px !important;
        font-weight: bold !important;
        transition: 0.3s !important;
        cursor: pointer !important;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb !important;
        text-decoration: none !important;
        cursor: pointer !important;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb !important;
        text-decoration: none !important;
        cursor: pointer !important;
    }
</style>
<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px"
                   onaftersave="templateTaskBasicVm.updateTask(templateTaskBasicVm.task)"
                   editable-text="templateTaskBasicVm.task.name">{{templateTaskBasicVm.task.name}}</a>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" ng-if="hasPermission('template','edit')"
                   onaftersave="templateTaskBasicVm.updateTask(templateTaskBasicVm.task)"
                   editable-textarea="templateTaskBasicVm.task.description">
                    <span ng-bind-html="templateTaskBasicVm.task.description || 'Click to enter description'"></span>
                </a>
                <span ng-if="!hasPermission('template','edit')"
                      ng-bind-html="templateTaskBasicVm.task.description"></span>
            </div>
        </div>
    </div>
</div>
