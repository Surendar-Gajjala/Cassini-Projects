<div style="padding-top: 10px;">
    <style scoped>
        #editor {
            position: absolute;
            left: 320px;
            bottom: 0;
            top: 50px;
            right: 10px;
        }
    </style>
    <button
            class="btn btn-sm btn-success"
            style="margin-bottom: 10px;"
            ng-click="newTemplateVm.update()" translate>UPDATE
    </button>
    <pre id="editor" style="" ng-model="newTemplateVm.script"></pre>
</div>