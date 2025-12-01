<div class="view-container" fitcontent>
    <style scoped>
        .media-img {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin: 5px;
            background-color: #fff;
        }

        .mode-btn.mode-active {
            border: 3px solid red;
        }

        img {
            image-orientation: from-image;
        }
    </style>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-primary" ng-click="mediaVm.setMode('upload')">
            <i class="fa fa-upload" style="margin-right: 5px;"></i>Upload
        </button>

        <div class="btn-group">
            <button class='btn btn-sm btn-warning mode-btn' ng-class="{'mode-active': mediaVm.mode == 'photos'}"
                    ng-click="mediaVm.setMode('photos')">Photos</button>
            <button class='btn btn-sm btn-info mode-btn'  ng-class="{'mode-active': mediaVm.mode == 'videos'}"
                    ng-click="mediaVm.setMode('videos')">Videos</button>
            <button class='btn btn-sm btn-success mode-btn'  ng-class="{'mode-active': mediaVm.mode == 'documents'}"
                    ng-click="mediaVm.setMode('documents')">Documents</button>
        </div></div>
    <div class="view-content" style="overflow-y:auto;background-color:#eee">
        <div ng-if="mediaVm.mode == 'photos'">
            <table style="table-layout: fixed; width:100%;">
                <tbody>
                <tr ng-repeat="chunk in mediaVm.chunks">
                    <td ng-repeat="image in chunk">
                        <div class="media-img">
                            <img width="100%" height="200" ng-src="{{image}}" alt=""  style="cursor: pointer"
                                 ng-click="mediaVm.showPicturesDialog(image)">
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div ng-if="mediaVm.mode == 'videos'">

        </div>
        <div ng-if="mediaVm.mode == 'documents'">
            <table style="table-layout: fixed; width:100%;">
                <tbody>
                <tr>
                    <td>
                        <div class="media-img">
                            <a href="" ng-click="mediaVm.downloadFile('app/assets/docs/RRI_CANCELLATIONS.pdf')">RRI Train Cancellations</a>
                        </div>
                    </td><td>
                    <div class="media-img">
                        <a href="" ng-click="mediaVm.downloadFile('app/assets/docs/RRI_BZA_YARD.pdf')">RRI BZA Yard Diagram</a>
                    </div>
                </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div ng-if="mediaVm.mode == 'upload'">
            <%@include file="dropzoneComponent.jsp" %>
        </div>
    </div>
</div>
