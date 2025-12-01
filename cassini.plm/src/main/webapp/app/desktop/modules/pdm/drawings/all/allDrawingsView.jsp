<div>
    <style scoped>

        .no-vaults-container {
            position: absolute;
            top: 45%;
            left: 50%;
            /* bring your own prefixes */
            transform: translate(-50%, -50%);
        }

        .no-vaults-container .vault-storage-img img {
            width: 400px
        }

        .no-vaults-container .vault-storage-message {
            font-size: 20px;
            font-weight: 300;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }
    </style>


    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>DRAWINGS</span>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
            <free-text-search on-clear="allDrawingsVm.resetPage" search-term="allDrawingsVm.searchText"
                              on-search="allDrawingsVm.freeTextSearch"
                              filter-search="allDrawingsVm.filterSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="padding: 10px;height: 230px;">

            <div class="no-vaults-container text-center" <%-- ng-if="allVaultsVm.vaults.length == 0"--%> >
                <div class="vault-storage-img">
                    <img src="app/assets/no_data_images/Drawings.png" alt="">
                </div>
                <div class="vault-storage-message">
                    <span translate>NO_DRAWINGS</span>
                </div>
            </div>

        </div>
    </div>
</div>