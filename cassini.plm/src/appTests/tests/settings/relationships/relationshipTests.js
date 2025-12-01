var urlsPage = require('../../../pages/urlsPage.js');

describe('Settings -> Relationships: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.homeUrl);
        browser.sleep(5000);
    });

    //---------Check Admin tab in the side navigation-------
    it('Check Admin tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(3000);
        var settingsButton = element(by.id('settings'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(settingsButton), 15000);
        settingsButton.click();
        browser.sleep(5000);

        var selectAttribute = element.all(by.id('settingsTree')).all(by.xpath('li/ul/li')).get(4);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAttribute), 15000);
        selectAttribute.click();
        browser.sleep(5000);

        expect(browser.getCurrentUrl()).toBe(urlsPage.relationshipsUrl);
        browser.sleep(10000);
    });

    //---------Header Text testCase-----------
    it("Header Text testCase ", function () {

        browser.sleep(1000);
        var header = element(by.css('h3.ng-scope'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(header), 15000);
        expect(header.getText()).toEqual('Relationships');
        browser.sleep(10000);
    });

    it(" New Relationship Tooltip testCase ", function () {
        browser.sleep(1000);
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getAttribute('title')).toEqual('New Relationship');
        browser.sleep(10000);
    });

    it(" New Relationship -> click : open right side panel ", function () {
        browser.sleep(1000);
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(5000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Relationship');
        browser.sleep(8000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });

    // ----------Check Close(X) button on the New Equipment screen-----------
    it("Check Close(X) button on the New Relationship screen", function () {
       
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getAttribute('title')).toEqual('New Relationship');

    });

});

describe('create new Relationship with out entering madatory fields', function () {

    beforeAll(function(){
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);
    });

    it("Without Relationship Name ", function () {
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(8000);

    });

    it("Without First Type", function () { 
        var relationshipName = element(by.model('newRelationshipVm.relationship.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(relationshipName), 10000);
        relationshipName.sendKeys('Assembly -> Part');
        browser.sleep(5000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select first type');
        browser.sleep(8000);
    });

    it("Without Second Type", function () { 
        
        var selectbuttonClick = element(by.id('selectFirstType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();


        var selectFirstType =  element(by.id('classificationTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFirstType), 15000);
        selectFirstType.click();


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select second type');
        browser.sleep(8000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

    });


});

describe('create new Relationship  with entering madatory fields', function () {

    beforeAll(function(){
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);
    });

    it("create new Relationship successfuly", function () {
      
        var relationshipName = element(by.model('newRelationshipVm.relationship.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(relationshipName), 10000);
        relationshipName.sendKeys('Assembly -> Part');
        browser.sleep(5000);

        var selectbuttonClick = element(by.id('selectFirstType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();
        browser.sleep(2000);
        var selectFirstType =  element(by.id('classificationTree')).element(by.xpath('li/ul/li[2]/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFirstType), 15000);
        selectFirstType.click();

        var selectSecondbuttonClick = element(by.id('selectSecondType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSecondbuttonClick), 5000);
        selectSecondbuttonClick.click();
        browser.sleep(2000);
        var selectSecondType =  element(by.id('relationClassificationTree')).element(by.xpath('li/ul/li[3]/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSecondType), 15000);
        selectSecondType.click();
        browser.sleep(4000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Relationship created successfully');
        browser.sleep(8000);

    });
});
