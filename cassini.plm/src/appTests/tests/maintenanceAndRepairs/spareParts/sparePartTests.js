var urlsPage = require('../../../pages/urlsPage.js');

describe('SpareParts: ', function () {

    var sparePart_id;

    //---------Check SpareParts tab in the side navigation-------
    it('Check SpareParts tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var maintenanceAndRepairButton = element(by.id('maintenanceAndRepairs'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(maintenanceAndRepairButton), 15000);
        maintenanceAndRepairButton.click();

        var sidePanelButton = element(by.id('spareParts'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelButton), 25000);
        sidePanelButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allSpareParts)

    });

    //---------Check New Spare Part button-----------
    it('Check New Spare Part button', function () {
        browser.get(urlsPage.allSpareParts);
        browser.sleep(2000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Spare Part');
    });

     //---------Check New Spare Part Button Text and Tooltip-----------
    it(" New Spare Part Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allSpareParts);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Spare Part');
        expect(newButtonn.getAttribute('title')).toEqual('New Spare Part');
    });

    // ----------Check Close(X) button on the New Spare Part screen-----------
    it("Check Close(X) button on the New Spare Part screen", function () {
        browser.get(urlsPage.allSpareParts);
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
        expect(newButton.getText()).toEqual('New Spare Part');

    });


    //-------Check New Spare Part creation->without entering mandatory fields data----------
    describe('Check New Spare Part creation->without entering mandatory fields data', function () {

        beforeAll(function () {
            browser.get(urlsPage.allSpareParts);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });


        it('Without Spare Part Type', function () {
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


        it('Without Name', function () {

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

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });
       
    });

   //-------Check New Spare Part creation->with entering mandatory fields data----------
    describe('Check New Spare Part creation->by entering mandatory fields data', function () {

        beforeAll(function () {
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });

        it(' New Spare Part create Successfull Test Case', function () {

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

            browser.wait(EC.presenceOf(urlsPage.byModel('newSparePartVm.newSparePart.name').sendKeys('SparePart 128')), 35000);
            browser.sleep(1000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(3500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Spare part created successfully');
            browser.sleep(5000);
        });

    });

});

