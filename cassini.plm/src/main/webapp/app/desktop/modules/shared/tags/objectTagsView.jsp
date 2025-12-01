<div class="row" style="padding: 20px;">
    <style scoped>
        .tags-container {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            column-gap: 5px;
            row-gap: 5px;
        }

        .tags-container .tag-card {
            position: relative;
            height: auto;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 10px;
            cursor: pointer;
            border: 1px solid lightgrey;
        }

        .tags-container .tag-card:hover {
            box-shadow: 0 7px 14px rgba(147, 148, 150, 0.25),
            0 5px 5px rgba(177, 177, 179, 0.22);
            transition: all .2s ease;
            transform: translate3D(0, -1px, 0) scale(1.01);
        }

        .tag-card .card-view .tag-name {
            font-weight: 600 !important;
        }

        .tag-card .delete-tag-icon {
            font-size: 22px;
            color: red;
            display: none;
            padding-left: 10px;
        }

        .tag-card:hover .delete-tag-icon {
            display: block;
        }

    </style>
    <div style="display: flex;margin-bottom: 5px;">
        <input type="text" placeholder="{{'ADD_TAG' | translate}}" ng-model="objectTagsVm.newTag.label"
               class="form-control" ng-enter="objectTagsVm.createTag()" style="margin-right: 10px;"/>
        <button class="btn btn-sm btn-primary" ng-click="objectTagsVm.createTag()" title="{{addTagTitle}}">
            <i class="la la-plus"></i>
        </button>
    </div>
    <div class="tags-container">
        <div class="tag-card" ng-repeat="tag in objectTagsVm.objectTags">
            <div class="card-view">
                <div class="d-flex align-items-center">
                    <div class="d-flex">
                        <div class="tag-name">
                            <span ng-bind-html="tag.label"></span>
                        </div>
                        <i class="la la-times delete-tag-icon" title="{{deleteTagTitle}}"
                           ng-click="objectTagsVm.deleteTag(tag)"></i>
                    </div>
                </div>
            </div>
        </div>
        <div ng-if="objectTagsVm.noResults == true">
            <div class="no-data">
                <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                <div class="message">{{ 'NO_SEARCH_RESULT_FOUND' | translate}}</div>
                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE
                </div>
            </div>
        </div>
    </div>
</div>

