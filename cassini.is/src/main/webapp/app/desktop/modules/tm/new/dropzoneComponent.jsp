<div ng-show="newTaskVm.showDropzone">
    <div id="actions" class="row">
        <div class="col-lg-7">
            <!-- The fileinput-button span is used to style the file input field as button -->
            <button class="btn btn-sm btn-default mr20" ng-click="newTaskVm.backToFiles()">
                <i class="fa fa-chevron-left"></i>
                <span>Go Back</span>
            </button>

            <span class="btn btn-sm btn-success fileinput-button">
                <i class="glyphicon glyphicon-plus"></i>
                <span>Add Files..</span>
            </span>
            <button type="submit" class="btn btn-sm btn-primary start">
                <i class="glyphicon glyphicon-upload"></i>
                <span>Start Upload</span>
            </button>
            <button type="reset" class="btn btn-sm btn-warning cancel">
                <i class="glyphicon glyphicon-ban-circle"></i>
                <span>Cancel Upload</span>
            </button>
        </div>

        <div class="col-lg-5">
            <!-- The global file processing state -->
            <span class="fileupload-process">
              <div id="total-progress" class="total-progress progress progress-striped active" role="progressbar"
                   aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
                  <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
              </div>
            </span>
        </div>

    </div>

    <form action="" id="dropzoneForm" method="post" enctype="multipart/form-data">
        <div id="documentsDropzoneContainer" class="documents-dropzone-container">
            <div id="documentsDropZone" style="display: table; width: 100%; height: 300px;">
                <div class="documents-dropzone">
                    <div style="width: 500px; display: inline-block; text-align: center">
                        <h3><i>You can drag and drop files here</i></h3>
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
                            <div class="progress-bar progress-bar-success" style="width:0%;"
                                 data-dz-uploadprogress></div>
                        </div>
                    </div>
                    <div style="width: 200px; text-align: center;">

                        <%--<button ng-hide="true" class="btn btn-sm btn-primary start">
                            <i class="glyphicon glyphicon-upload"></i>
                            <span>Start</span>
                        </button>
                        <button data-dz-remove class="btn btn-sm btn-warning cancel">
                            <i class="glyphicon glyphicon-ban-circle"></i>
                            <span>Cancel</span>
                        </button>--%>
                        <!--
                        <button data-dz-remove class="btn btn-danger delete">
                            <i class="glyphicon glyphicon-trash"></i>
                            <span>Delete</span>
                        </button>
                        -->
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>