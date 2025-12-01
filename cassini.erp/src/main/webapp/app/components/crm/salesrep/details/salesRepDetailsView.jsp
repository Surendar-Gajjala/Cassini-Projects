<br/>
<div class="row">
    <div class="col-xs-12 col-sm-12 col-md-2 styled-panel">

        <div>
            <img src="app/assets/images/user-alt-256.png" class="thumbnail img-responsive" alt="" style="margin: 0 auto;"/>
        </div>

        <div class="text-center" ><a href="" ng-click="showProfile()">Show Full Profile</a></div>
        <br/>
        <div class="grid-form">
            <div class="row">
                <div class="col-sm-6 text-primary text-right">Status: </div>
                <div class="col-sm-6">{{salesRep.status}}</div>
            </div>

            <div class="row">
                <div class="col-sm-6 text-primary text-right">Date of Hire: </div>
                <div class="col-sm-6">{{salesRep.dateOfHire}}</div>
            </div>

            <div class="row">
                <div class="col-sm-6 text-primary text-right">Manager: </div>
                <div class="col-sm-6">{{salesRep.manager}}</div>
            </div>

            <div class="row">
                <div class="col-sm-6 text-primary text-right">Office Phone: </div>
                <div class="col-sm-6">{{salesRep.phoneOffice}}</div>
            </div>

            <div class="row">
                <div class="col-sm-6 text-primary text-right">Mobile Phone: </div>
                <div class="col-sm-6">{{salesRep.phoneMobile}}</div>
            </div>
        </div>

    </div>

    <div class="col-xs-12 col-sm-12 col-md-10">
        <h2 class="profile-name">{{salesRep.firstName}}</h2>
        <br/>
        <tabset>
            <tab ng-repeat="tab in tabs" heading="{{tab.heading}}" active="tab.active" select="setTabActive(tab)">
                <div ui-view="{{tab.view}}"></div>
            </tab>
        </tabset>
    </div>
</div>