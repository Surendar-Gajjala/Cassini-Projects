<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="feedbackVm.createFeedback()">Create</button>
    </div>

    <div class="view-content">
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Title: </label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title" ng-model="feedbackVm.newFeedback.summary">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description: </label>
                        <div class="col-sm-8">
                            <textarea id="meetingDescription" name="description" rows="10" class="form-control" style="resize: none"
                                      ng-model="feedbackVm.newFeedback.description"></textarea>
                        </div>
                    </div>

                  </form>

                <div class="row" style="margin-top: 10px;">
                    <label class="col-sm-4 text-right">Attachments: </label>
                    <div class="col-sm-8">
                        <div style="margin-bottom: 5px;">
                            <span class="btn btn-xs btn-success fileinput-button">
                                <i class="glyphicon glyphicon-plus"></i>
                                <span>Add files...</span>
                            </span>
                        </div>
                        <form action="" id="attachmentsForm" method="post" enctype="multipart/form-data" >
                            <div id="attachmentsContainer" class="attachments-dropzone-container">
                                <div id="attachmentsDropZone" style="display: table; width: 100%;">
                                    <div class="attachments-dropzone">
                                        <div style="width: 100%; min-height: 50px; padding-top: 15px; display: inline-block; text-align: center">
                                            <span ><i>You can drag and drop files here!</i></span>
                                        </div>
                                    </div>
                                </div>

                                <div id="previews">
                                    <div id="template">
                                        <div class="row" style="border-bottom: 1px dotted lightgrey; margin-bottom: 10px;padding-bottom: 10px;">
                                            <div class="col-sm-7">
                                                <p class="name" data-dz-name></p>
                                                <small class="error text-danger" data-dz-errormessage></small>
                                            </div>
                                            <div class="col-sm-3">
                                                <p class="size" data-dz-size></p>
                                                <div class="progress progress-striped active" role="progressbar"
                                                     aria-valuemin="0" aria-valuemax="100" aria-valuenow="0" style="margin-bottom: 0">
                                                    <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
                                                </div>
                                            </div>
                                            <div class="col-sm-2" style="text-align: right">
                                                <button data-dz-remove class="btn btn-xs btn-danger delete" style="margin-top: 10px;">
                                                    <i class="glyphicon glyphicon-trash"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
