<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }

    /* The Close Button */
    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }

    .item-number {
        display: inline-block;
    }

    .item-rev {
        font-size: 16px;
        font-weight: normal;
    }
</style>

<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{selectedGlossary.defaultDetail.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" ng-if="hasPermission('glossary','edit')"
                   ng-hide="external.external== true"
                   onaftersave="glossaryBasicVm.updateGlossary()"
                   editable-textarea="selectedGlossary.defaultDetail.description"
                   title="{{selectedGlossary.defaultDetail.description}}">
                    {{(selectedGlossary.defaultDetail.description ) || 'CLICK_TO_ENTER_DESCRIPTION' | translate}}
                </a>

                <p ng-if="hasPermission('glossary','edit') == false">
                    {{selectedGlossary.defaultDetail.description}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>REVISION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{selectedGlossary.revision}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>LIFE_CYCLE_PHASE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span><item-status item="selectedGlossary"></item-status></span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>RELEASED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{selectedGlossary.releasedDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{selectedGlossary.createdByObject.firstName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{selectedGlossary.createdDate}}</span>
            </div>
        </div>
    </div>
</div>
