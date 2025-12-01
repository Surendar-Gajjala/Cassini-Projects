<style>
    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
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

    .img-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    /* Modal Content (image) */
    .img-model .modal-content {
        margin: auto;
        display: block;
        height: 90%;
        width: 60%;
        /*max-width: 70%;*/
    }

    /* Caption of Modal Image */
    .img-model #caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        height: 150px;
    }

    /* Add Animation */
    .img-model .modal-content, #caption {
        -webkit-animation-name: zoom;
        -webkit-animation-duration: 0.6s;
        animation-name: zoom;
        animation-duration: 0.6s;
    }

    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }
</style>

<div ng-if="siteBasicVm.loading == true" style="padding: 30px;">
    <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading Site details..
                </span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;" ng-if="siteBasicVm.loading == false">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Name: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && hasPermission('permission.sites.edit') == true" href="#"
                   editable-text="siteBasicVm.site.name"
                   onaftersave="siteBasicVm.updateSite()">
                    {{siteBasicVm.site.name}}
                </a>

                <p ng-if="selectedProject.locked == false && hasPermission('permission.sites.edit') == false">
                    {{siteBasicVm.site.name}}</p>

                <p ng-if="selectedProject.locked == true"> {{siteBasicVm.site.name}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Description: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && hasPermission('permission.sites.edit') == true" href="#"
                   editable-text="siteBasicVm.site.description"
                   onaftersave="siteBasicVm.updateSite()">
                    {{siteBasicVm.site.description || 'Click to enter description'}}</a>

                <p ng-if="selectedProject.locked == false && hasPermission('permission.sites.edit') == false">
                    {{siteBasicVm.site.description}}</p>

                <p ng-if="selectedProject.locked == true">{{siteBasicVm.site.description}}</p>
            </div>
        </div>

        <attributes-details-view attribute-id="siteId" attribute-type="SITE"
                                 has-permission="(hasPermission('permission.sites.edit') || login.person.isProjectOwner)"></attributes-details-view>
    </div>
</div>




