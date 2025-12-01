<style scoped>
    .user-image {
        width: 75px;
        height: 75px;
        border-radius: 50%;
        background-color: #e1f0ff;
        color: #3699ff;
        margin-right: 15px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
    }

    .user-image img {
        width: 75px;
        height: 75px;
        border-radius: 50%;
    }

    .small-user-image {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background-color: #e1f0ff;
        color: #3699ff;
        margin-right: 15px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
    }

    .small-user-image img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
    }
</style>

<div class="user-image" ng-if="display == 'normal'">
    <span ng-if="!person.hasImage">{{person.imageWord}}</span>
    <img ng-if="person.hasImage" ng-src="{{person.personImage}}">
</div>
<div class="small-user-image" ng-if="display == 'small'">
    <span ng-if="!person.hasImage">{{person.imageWord}}</span>
    <img ng-if="person.hasImage" ng-src="{{person.personImage}}">
</div>