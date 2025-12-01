<div class="view-container" fitcontent>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="row" style="padding: 10px;">
            <div class="col-xs-12 col-sm-12 col-sm-offset-1 col-md-10 col-md-offset-1">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <span translate>TITLE</span><span class="asterisk">*</span>
                            <span> : </span>
                        </label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newFbVm.newFeedback.summary">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <span translate>DESCRIPTION</span>
                            <span> : </span>
                        </label>

                        <div class="col-sm-9">
                            <textarea id="meetingDescription" name="description" rows="10" class="form-control"
                                      style="resize: none"
                                      ng-model="newFbVm.newFeedback.description"></textarea>
                        </div>
                    </div>

                </form>
                <br>

                <div class="row">
                    <label class="col-sm-2 text-right">
                        <span style="position: absolute; left: -22px; top: 10px;" translate>ATTACHMENTS</span>
                        <span style="position: absolute; left: 65px; top: 10px;">: </span>
                    </label>

                    <div class="col-sm-8">
                        <p class="name" data-dz-name></p>
                    </div>

                    <div class="col-sm-10">
                        <%--  <div style="margin-bottom: 5px;">
                              <span class="btn btn-xs btn-success fileinput-button">
                                  <i class="glyphicon glyphicon-plus"></i>
                                  <span translate>ADD_FILES</span>
                              </span>
                          </div>--%>

                        <form action="" id="attachmentsForm" method="post" enctype="multipart/form-data">
                            <div class="attachments-dropzone">
                                <div style="font-style: italic; font-size: 12px;text-align: center;margin-bottom: 20px;margin-right: -109px">
                                    {{dragAndDropFilesTitle}}
                                    <a href=""
                                       ng-click="newFbVm.selectFeedBackFiles()">({{clickToAddFilesTitle}})</a>
                                </div>
                            </div>
                            <div id="attachmentsContainer" class="attachments-dropzone-container">
                                <div id="attachmentsDropZone">
                                    <div id="previews">
                                        <div id="template" ng-show="newFbVm.template">
                                            <div class="row"
                                                 style="border-bottom: 1px dotted lightgrey; margin-bottom: 10px;padding-bottom: 10px;">
                                                <div class="col-sm-7">
                                                    <p class="name" data-dz-name></p>
                                                    <small class="error text-danger" data-dz-errormessage></small>
                                                </div>
                                                <div class="col-sm-3">
                                                    <p class="size" data-dz-size></p>

                                                    <%-- <div class="progress progress-striped active" role="progressbar"
                                                          aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"
                                                          style="margin-bottom: 0">
                                                         <div class="progress-bar progress-bar-success" style="width:0%;"
                                                              data-dz-uploadprogress></div>
                                                     </div>--%>
                                                </div>
                                                <div class="col-sm-2" style="text-align: right">
                                                    <button data-dz-remove class="btn btn-xs btn-danger delete"
                                                            style="margin-top: 10px;">
                                                        <i class="glyphicon glyphicon-trash"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div ng-show="newFbVm.processing">
                                <span translate>CREATING_TICKET</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar"
                                         role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
