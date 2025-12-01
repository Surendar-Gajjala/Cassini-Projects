<div class="view-container">
    <style scoped>
        img {
            image-orientation: from-image;
        }
    </style>

    <div class="modal-body" style="padding: 20px; height: 600px; background-color: #eef0f3;overflow-y: auto"
         tabindex="0"
         ng-keydown="picturesVm.keyPressed($event)">
        <div style="width:100%; height: 530px;">
            <img src="" alt="" height="520" width="100%" id="pictureHolder">
        </div>
        <div class="row">
            <div class="col-sm-4">
                <button class='btn btn-xs btn-default' ng-click='picturesVm.previous()'>
                    <i class="fa fa-chevron-left"></i>
                </button>
            </div>
            <div class="col-sm-4 text-center">
                <span>{{picturesVm.currentIndex+1}} of {{picturesVm.pictures.length}}</span>
            </div>
            <div class="col-sm-4 text-right">
                <button class='btn btn-xs btn-default' ng-click='picturesVm.next()'>
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-primary" ng-click="picturesVm.cancel()">Close</button>
        </div>
    </div>
</div>
