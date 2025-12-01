var urlsPage = require('../../../pages/urlsPage.js');

describe('Maintenance Plan: ', function () {

    var maintenancePlan_id;

    //---------Check Maintenance Plan tab in the side navigation-------
    it('Check Maintenance Plan tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var maintenanceAndRepairButton = element(by.id('maintenanceAndRepairs'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(maintenanceAndRepairButton), 15000);
        maintenanceAndRepairButton.click();

        var sidePanelButton = element(by.id('maintenancePlans'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelButton), 25000);
        sidePanelButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allMaintenancePlans)

    });

    //---------Check New Maintenance Plan button-----------
    it('Check New Maintenance Plan button', function () {
        browser.get(urlsPage.allMaintenancePlans);
        browser.sleep(2000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Maintenance Plan');
    });

    //---------Check New Maintenance Plan Button Text and Tooltip-----------
    it(" New Maintenance Plan Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allMaintenancePlans);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Maintenance Plan');
        expect(newButtonn.getAttribute('title')).toEqual('New Maintenance Plan');
    });

    // ----------Check Close(X) button on the New Maintenance Plan screen-----------
    it("Check Close(X) button on the New Maintenance Plan screen", function () {
        browser.get(urlsPage.allMaintenancePlans);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Maintenance Plan');

    });


    //-------Check New Maintenance Plan creation->without entering mandatory fields data----------
    describe('Check New Maintenance Plan creation->without entering mandatory fields data', function () {

        beforeAll(function () {
            browser.get(urlsPage.allMaintenancePlans);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });


        it('Without Asset Type', function () {
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select asset type');
            browser.sleep(5000);
        });

        it('Without select Asset', function () {

            var selectbuttonClick = element(by.id('Select'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectbuttonClick), 10000);
            selectbuttonClick.click();
            browser.sleep(1000);
            var selectAssertType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAssertType), 15000);
            selectAssertType.click();
            browser.sleep(1000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select asset');
            browser.sleep(5000);
        });


        it('Without Name', function () {

            var selectAsset = element(by.model('newMaintenancePlanVm.newMaintenancePlan.asset'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please enter name');
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

        
    });

    //-------Check New Maintenance Plan creation->with entering mandatory fields data----------
    describe('Check New Maintenance Plan creation->by entering mandatory fields data', function () {

        beforeAll(function () {
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });

        it(' New Maintenance Plan create Successfull Test Case', function () {

            var selectbuttonClick = element(by.id('Select'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectbuttonClick), 10000);
            selectbuttonClick.click();
            browser.sleep(1000);
            var equipmentType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(equipmentType), 15000);
            equipmentType.click();
            browser.sleep(1000);

            var selectAsset = element(by.model('newMaintenancePlanVm.newMaintenancePlan.asset'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();


            browser.wait(EC.presenceOf(urlsPage.byModel('newMaintenancePlanVm.newMaintenancePlan.name').sendKeys('maintenancePlan 198')), 35000);
            browser.sleep(1000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(3500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Maintenance plan created successfully');
            browser.sleep(5000);
        });

    });

});

