<div class="grid-form">
    <button class="btn btn-primary" ng-click="showProfile()">Show Profile</button>
    <div class="row"></div>

    <div class="row">
        <div class="col-sm-4 text-primary text-right">Name: </div>
        <div class="col-sm-8 property-value">{{salesRep.firstName}}</div>
    </div>

    <div class="row">
        <div class="col-sm-4 text-primary text-right">Status: </div>
        <div class="col-sm-8">{{salesRep.status}}</div>
    </div>

    <div class="row">
        <div class="col-sm-4 text-primary text-right">Date of Hire: </div>
        <div class="col-sm-8">{{salesRep.dateOfHire}}</div>
    </div>

    <div class="row">
        <div class="col-sm-4 text-primary text-right">Manager: </div>
        <div class="col-sm-8">{{salesRep.manager}}</div>
    </div>

    <div class="row">
        <div class="col-sm-4 text-primary text-right">Office Phone: </div>
        <div class="col-sm-8">{{salesRep.phoneOffice}}</div>
    </div>

    <div class="row">
        <div class="col-sm-4 text-primary text-right">Mobile Phone: </div>
        <div class="col-sm-8">{{salesRep.phoneMobile}}</div>
    </div>
</div>