<div>
    <style scoped>
        #kister-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            /*  z-index: 1; /!* Sit on top *!/*/
            /*padding-top: 100px; /!* Location of the box *!/*/
            left: 0;
            top: 0;
            overflow: auto; /* Enable scroll if needed */
            margin-top: 50px;
        }

        #kister-model .closeImage1 {
            position: absolute;
            top: 1% !important;
            right: 1%;
            color: blanchedalmond;
            font-size: 30px;
            font-weight: bold;
            transition: 0.3s;
            border: 1px solid lightgrey;
            border-radius: 50%;
            background: #988282a6;
            padding: 5px;
        }

        #kister-model .closeImage1:hover,
        #kister-model .closeImage1:focus {
            color: white;
            text-decoration: none;
            cursor: pointer;
        }

        #closeImageId {
            display: none;
            margin-right: 0%;
        }

    </style>
    <div id="kister-model" class="modal">
        <span class="closeImage1" id="closeImageId">&times;</span>
        <iframe id="WebViewFrame" src="" <%--width="{{contwidth}}%"--%> width="100%" height="95%"></iframe>
    </div>
</div>