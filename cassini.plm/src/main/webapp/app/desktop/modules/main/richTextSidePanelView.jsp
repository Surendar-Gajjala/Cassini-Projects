<div style="position: relative;">
    <style scoped>
        div.sticky {
            position: sticky;
            top: 0;
            background-color: white;
            border-bottom: 1px solid #ece4e4;
            height: 50px;

        }

        .modal-header .close {
            float: right;
            font-size: 21px;
            font-weight: 700;
            line-height: 1;
            color: #000;
            text-shadow: 0 1px 0 #fff;
            filter: alpha(opacity=20);
            opacity: .2;
            margin-top: -3px !important;
        }

        .note-popover {
            z-index: 10000 !important;
        }

        .popover {
            width: 225px !important;
        }
    </style>


    <div class="row sticky">
        <button class="btn  btn-xs btn-warning" ng-if="richTextSidePanelVm.richTextShowFalg == false"
                style="margin-top: 7px;margin-left: 20px;height: 36px;"
                ng-click="richTextSidePanelVm.editRichText()">Edit
        </button>

        <button class="btn  btn-xs btn-success" ng-if="richTextSidePanelVm.richTextShowFalg == true"
                style="margin-top: 7px;margin-left: 20px;height: 36px;"
                ng-click="richTextSidePanelVm.saveRichText()">Save
        </button>
        <button class="btn btn-xs btn-danger" ng-if="richTextSidePanelVm.richTextShowFalg == true"
                style="margin-top: 7px;height: 36px;"
                ng-click="richTextSidePanelVm.cancelRichText()">Cancel
        </button>

    </div>
    <br/>

    <div style="overflow-y: auto; overflow-x: auto; padding: 0px;">
        <div class="row" style="margin: 0;" ng-show="richTextSidePanelVm.richTextShowFalg == false">
            <div class="" style="padding: 10px;">
                <span style="word-wrap: break-word;white-space: normal;"
                      ng-bind-html="richTextSidePanelVm.richTextObj.richTextValue">
                </span>
            </div>
        </div>

        <div class="row" style="margin: 0;" ng-show="richTextSidePanelVm.richTextShowFalg == true">
            <summernote
                    ng-model="richTextSidePanelVm.richTextObj.richTextValue"></summernote>

        </div>
    </div>
</div>


