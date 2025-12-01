<style scoped>

    .image {
        padding: 10px !important;
    }

    .img-thumbnail {
        padding: 0 !important;
    }

    .img-responsive {
        height: 250px;
        width: 100%;
    }

    .modal1 {
        width: 360px;
        height: 200px;
        padding: 20px;
        border: 1px solid black;
        background-color: white;
        position: fixed;
        left: 10px;
        top: 10px;
    }

    /*.img-model .closeImage {
        position: absolute;
        top: 1%;
        left: 56%;
        color: #5e5e5e;
        font-weight: bold;
        transition: 0.3s;
        background-color: #fff;
        border-radius: 50%;
        height: 35px;
        width: 35px;
        text-align: center;
        font-size: 40px;
    }*/

    .img-model .closeImage {
        position: absolute;
        top: 9%;
        left: 58%;
        color: #5e5e5e;
        font-weight: bold;
        transition: 0.3s;
        background-color: #fff;
        border-radius: 50%;
        height: 44px;
        width: 45px;
        text-align: center;
        font-size: 45px;
    }

    .img-model .leftArrow {
        position: absolute;
        top: 50%;
        left: 2%;
        color: white;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .rightArrow {
        position: absolute;
        top: 50%;
        right: 2%;
        color: white;
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

    .img-model .leftArrow:hover,
    .img-model .leftArrow:focus {
        color: white;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .rightArrow:hover,
    .img-model .rightArrow:focus {
        color: white;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model.modal1 {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 50px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    /* Modal Content (image) */
    /*.img-model .modal1-content {
        margin: auto;
        display: block;
        height: auto;
        width: auto;
        max-width: 70%;
        max-height: 85%;
        /*max-width: 70%;*/

    .img-model .modal1-content {
        margin: auto;
        display: block;
        height: auto;
        width: auto;
        max-width: 70%;
        max-height: 85%;
        /* max-width: 70%; */
        margin-top: 80px;
        margin-left: 485px;
    }

    /*/!* Caption of Modal Image *!/
    .img-model #caption {
        margin: auto;
        display: block;
        /!*width: 80%;
        max-width: 700px;*!/
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        /!*height: 150px;*!/
    }*/

    .image-info-panel {
        position: relative;
        margin-top: -30px;
        height: 30px;
        background-color: rgba(0, 0, 0, 0.5);
        line-height: 30px;
        color: #fff;
        padding-left: 10px;
        padding-right: 10px;
        display: flex;
    }

    /*.img-model .print {
         position: absolute;
         top: 1%;
         left: 48%;
         color: #5e5e5e;
         font-size: 25px;
         font-weight: bold;
         transition: 0.3s;
         background-color: #fff;
         border-radius: 50%;
         height: 35px;
         width: 35px;
         line-height: 37px;
         padding-left: 5px;
         z-index: -1;
     }*/

    .img-model .print {
        position: absolute;
        top: 9%;
        left: 42%;
        color: #5e5e5e;
        font-size: 29px;
        font-weight: bold;
        transition: 0.3s;
        background-color: #fff;
        border-radius: 50%;
        height: 44px;
        width: 39px;
        line-height: 41px;
        padding-left: 5px;
        z-index: -1;
    }

    .img-model .uploadBy {
        bottom: 5%;
        color: black;
        font-size: 14px;
        background-color: #fff;
        left: 35%;
        width: 30%;
        position: absolute;
        padding: 5px;
    }

    /*.img-model .rotate {
        position: absolute;
        top: 1%;
        left: 52%;
        color: #5e5e5e;
        font-size: 25px;
        font-weight: bold;
        transition: 0.3s;
        background-color: #fff;
        border-radius: 50%;
        height: 35px;
        width: 35px;
        line-height: 37px;
        padding-left: 5px;
        z-index: -1;
    }*/

    .img-model .rotate {
        position: absolute;
        top: 9%;
        left: 50%;
        color: #5e5e5e;
        font-size: 26px;
        font-weight: bold;
        transition: 0.3s;
        background-color: #fff;
        border-radius: 50%;
        height: 45px;
        width: 43px;
        line-height: 43px;
        padding-left: 8px;
        z-index: -1;
    }

</style>

<div id="taskMediaDropzoneForm">
    <div style="font-style: italic; text-align: center;margin-bottom: 10px;margin-top: 10px;"
         ng-hide="selectedProject.locked || !(login.person.isProjectOwner || hasPermission('permission.tasks.uploadMedia') || login.person.isTaskOwner)">
        Drag and Drop Files here or <a href="" ng-click="taskMediaVm.selectFiles()">(Click here to
        upload
        files)</a>
    </div>
    <div id="taskMediaContainer" class="row" ng-repeat="media in taskMediaVm.taskMedia">
        <div class="col-sm-3 image" ng-repeat="file in media">
            <div class="thmb thmb-prev img-thumbnail" style="width: 100%;">
                <%--<span class="closeImage">&times;</span>--%>
                <img id="img{{$index}}"
                     ng-src="api/col/media/{{file.id}}/bytes"
                     class="img-responsive" ng-if="file.mediaType == 'IMAGE' "
                     ng-click="setTurtle(file)">
                <video id="vid{{$index}}" controls
                       class="img-responsive" ng-if="file.mediaType == 'VIDEO' "
                       type="video/mp4" ng-click="setTurtle(file)">
                    <source ng-src="{{file.url}}" type="video/mp4">
                </video>
                <div class="image-info-panel">
                    <div style="width: 95%;">{{file.createdDate}}</div>
                    <div title="Delete" style="cursor: pointer" ng-click="deleteFile(file)"
                         ng-if="(login.person.isProjectOwner || (file.createdBy == login.person.id))"><i
                            class="fa fa-trash"></i></div>
                </div>
            </div>

            <div id="myModal2" class="img-model modal1">
                <span class="closeImage" title="Close Preview"><i class="fa fa-times"
                                                                  style="font-size: 35px;"></i></span>
                <span class="rotate" ng-click="rotateImage()" title=" Click to rotate image"><i
                        class="fa fa-repeat"></i></span>
                <a href="" class="leftArrow" ng-click="prev()"><i class="fa fa-arrow-circle-left"></i>
                </a>

                <span id="taskImg"><img class="modal1-content" id="img03"></span>
                <a href="" class="rightArrow" ng-click="next()"><i class="fa fa-arrow-circle-right"></i></a>

                <div ng-if="taskMediaVm.selectedImage.location.uploadFrom != 'null' " class="uploadBy">Uploaded from :
                    {{taskMediaVm.selectedImage.location.uploadFrom}}
                </div>
                <a href="" class="print" title="Print" ng-click="print()"><i class="fa fa-print"></i></a>
            </div>
        </div>
    </div>
</div>