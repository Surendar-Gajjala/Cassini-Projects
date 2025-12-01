<div>
    <style>

        .dropzone-container {
            height: 500px;
            width: 500px;
            border-radius: 50%;
            background-color: var(--cassini-form-contrast-color);
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            margin: auto;
            cursor: pointer;
        }

        .dropzone-container h4 {
            line-height: 500px;
            text-align: center;
        }

        @media screen and (max-width: 660px) {
            .dropzone-container {
                height: 300px;
                width: 300px;
            }

            .dropzone-container h4 {
                line-height: 300px;
            }

        }

    </style>
    <div class="view-container" fitcontent>
        <div class="view-content nopadding" style="overflow-y: auto;">
            <div class="dropzone-container" onclick="$('#importFile').click();">
                <h4 style="font-weight: 300 !important;font-size: 24px;">Click here to select file to import</h4>
            </div>
            <input type="file" id="importFile" value="file" onchange="angular.element(this).scope().importData()"
                   style="display: none">
        </div>
    </div>
</div>