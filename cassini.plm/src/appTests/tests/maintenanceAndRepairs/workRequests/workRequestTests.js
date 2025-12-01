var urlsPage = require('../../../pages/urlsPage.js');

describe('Work Request: ', function () {

    var workRequest_id;

    //---------Check Work Requests tab in the side navigation-------
    it('Check Work Requests tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var maintenanceAndRepairButton = element(by.id('maintenanceAndRepairs'));
        browser.executeScript("arguments[0].scrollIntoView();", maintenanceAndRepairButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(maintenanceAndRepairButton), 15000);
        maintenanceAndRepairButton.click();

        var sidePanelButton = element(by.id('workRequests'));
        browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelButton), 25000);
        sidePanelButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allWorkRequests)

    });

    //---------Check New Work Request button-----------
    it('Check New Work Request button', function () {
        browser.get(urlsPage.allWorkRequests);
        browser.sleep(2000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Work Request');
    });

    //---------Check New Work Request Button Text and Tooltip-----------
    it(" New Work Request Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allWorkRequests);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Work Request');
        expect(newButtonn.getAttribute('title')).toEqual('New Work Request');
    });

    // ----------Check Close(X) button on the New Work Request screen-----------
    it("Check Close(X) button on the New Work Request screen", function () {
        browser.get(urlsPage.allWorkRequests);
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
        expect(newButton.getText()).toEqual('New Work Request');

    });


    //-------Check New Work Request creation->without entering mandatory fields data----------
    describe('Check New Work Request creation->without entering mandatory fields data', function () {

        beforeAll(function () {
            browser.get(urlsPage.allWorkRequests);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });


        it('Without Type', function () {
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Work Request type cannot be empty');
            browser.sleep(5000);
        });

        it('Without select Asset', function () {

            var selectbuttonClick = element(by.id('Select'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectbuttonClick), 10000);
            selectbuttonClick.click();
            browser.sleep(1000);
            var selectWorkRequestType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectWorkRequestType), 15000);
            selectWorkRequestType.click();
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

            var selectAsset = element(by.model('newWorkRequestVm.workRequest.asset'));
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
            expect(alertMessage).toEqual('Name cannot be empty');
            browser.sleep(5000);
        });


        
        it('Without Requestor Field', function () {
            
            browser.sleep(1000);
            var name = element(by.model('newWorkRequestVm.workRequest.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 15000);
            name.sendKeys('Work Request 200');
            browser.sleep(1000);


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select requestor');
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });


        
    });

    //-------Check New Work Request creation->with entering mandatory fields data----------
    describe('Check New Work Request creation->by entering mandatory fields data', function () {

        beforeAll(function () {
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });

        it(' New Work Request create Successfull Test Case', function () {

            var selectbuttonClick = element(by.id('Select'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectbuttonClick), 10000);
            selectbuttonClick.click();
            browser.sleep(1000);
            var equipmentType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(equipmentType), 10000);
            equipmentType.click();
            browser.sleep(1000);

            var selectAsset = element(by.model('newWorkRequestVm.workRequest.asset'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();


            var name = element(by.model('newWorkRequestVm.workRequest.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 10000);
            name.sendKeys('Work Request 200');
            browser.sleep(1000);

            var selectAsset = element(by.model('newWorkRequestVm.workRequest.requestor'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 10000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 1000);
            createButton.click();
            browser.sleep(3500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Work Request created successfully');
            browser.sleep(5000);
        });

    });

});

