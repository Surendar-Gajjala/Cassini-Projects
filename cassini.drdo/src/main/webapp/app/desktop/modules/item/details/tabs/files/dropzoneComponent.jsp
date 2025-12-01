<div id="actions" class="row">
    <div class="col-lg-7">
        <!-- The fileinput-button span is used to style the file input field as button -->
        <button class="btn btn-sm btn-default mr20" ng-click="itemFilesVm.backToFiles()">
            <span>Cancel</span>
        </button>

            <span class="btn btn-sm btn-success fileinput-button">
                <i class="glyphicon glyphicon-plus"></i>
                <span>Add files...</span>
            </span>
        <button type="submit" id="primaryBtn" style="display: none;" class="btn btn-sm btn-primary start btn-margin">
            <i class="glyphicon glyphicon-upload"></i>
            <span>Start upload</span>
        </button>
        <button type="reset" id="warningBtn" style="display: none;" class="btn btn-sm btn-warning cancel btn-margin">
            <i class="glyphicon glyphicon-ban-circle"></i>
            <span>Cancel upload</span>
        </button>
    </div>

    <div class="col-lg-5">
        <!-- The global file processing state -->
        <span class="fileupload-process">
          <div id="total-progress" class="total-progress progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
              <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
          </div>
        </span>
    </div>

</div>

<form action="" id="dropzoneForm" method="post" enctype="multipart/form-data" >
    <div id="filesDropzoneContainer" class="files-dropzone-container">
        <div id="filesDropZone" style="display: table; width: 100%; height: 300px;">
            <div class="files-dropzone">
                <div style="width: 500px; display: inline-block; text-align: center">
                    <h3 ><i>You can drag and drop files here!</i></h3>
                </div>
            </div>
        </div>

        <div class="table table-striped table-bordered" class="files previews" id="previews">
            <div id="template" class="file-row">
                <!-- This is used as the file preview template -->
                <div style="width:10px">
                    <span class="preview"><img data-dz-thumbnail/></span>
                </div>
                <div>
                    <p class="name" data-dz-name></p>
                    <strong class="error text-danger" data-dz-errormessage></strong>
                </div>
                <div>
                    <p class="size" data-dz-size></p>
                    <div class="progress progress-striped active" role="progressbar"
                         aria-valuemin="0" aria-valuemax="100" aria-valuenow="0" style="margin-bottom: 0">
                        <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
                    </div>
                </div>
                <div style="width: 200px; text-align: center;">
                    <button ng-hide="true" type="submit" class="btn btn-sm btn-primary start">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span>Start</span>
                    </button>
                    <button data-dz-remove class="btn btn-sm btn-warning cancel">
                        <i class="glyphicon glyphicon-ban-circle"></i>
                        <span>Cancel</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>