var urlsPage = require('../../../pages/urlsPage.js');

describe('Meters: ', function () {

    var meter_id;

    //---------Check Meters tab in the side navigation-------
    it('Check Meters tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var maintenanceAndRepairButton = element(by.id('maintenanceAndRepairs'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(maintenanceAndRepairButton), 15000);
        maintenanceAndRepairButton.click();

        var sidePanelButton = element(by.id('meters'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelButton), 25000);
        sidePanelButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allMeters)

    });

    //---------Check New Meter button-----------
    it('Check New Meter button', function () {
        browser.get(urlsPage.allMeters);
        browser.sleep(2000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Meter');
    });

     //---------Check New Meter Button Text and Tooltip-----------
    it(" New Meter Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allMeters);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Meter');
        expect(newButtonn.getAttribute('title')).toEqual('New Meter');
    });

    // ----------Check Close(X) button on the New Meter screen-----------
    it("Check Close(X) button on the New Meter screen", function () {
        browser.get(urlsPage.allMeters);
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
        expect(newButton.getText()).toEqual('New Meter');

    });


    //-------Check New Meter creation->without entering mandatory fields data----------
    describe('Check New Meter creation->without entering mandatory fields data', function () {

        beforeAll(function () {
            browser.get(urlsPage.allMeters);
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
            expect(alertMessage).toEqual('Please select type');
            browser.sleep(5000);
        });


        it('Without Meter Name', function () {

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
        });

        it('Without QOM ', function () {

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
         
            element(by.model('newMeterVm.newMeter.name')).sendKeys('Meter 27');
            browser.sleep(2000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select QOM');
            browser.sleep(5000);
        });

        it('Without UOM ', function () {

            var selectQom = element(by.model('newMeterVm.newMeter.qomObject'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectQom), 15000);
            selectQom.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select UOM');
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

       
    });

   //-------Check New Meter creation->with entering mandatory fields data----------
    describe('Check New Meter creation->by entering mandatory fields data', function () {

        beforeAll(function () {
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });

        it(' New Meter create Successfull Test Case', function () {

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

            browser.wait(EC.presenceOf(urlsPage.byModel('newMeterVm.newMeter.name').sendKeys('Meter 28')), 35000);
            browser.sleep(1000);

            var selectQom = element(by.model('newMeterVm.newMeter.qomObject'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectQom), 15000);
            selectQom.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();
            browser.sleep(1000);

            var selectUom = element(by.model('newMeterVm.newMeter.uomObject'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectUom), 15000);
            selectUom.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();
            browser.sleep(1000);
           
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(3500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Meter added successfully');
            browser.sleep(5000);
        });

    });

});

