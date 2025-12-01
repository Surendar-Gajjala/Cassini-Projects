var urlsPage = require('../../../../pages/urlsPage.js');


//-------Check New Instrument creation->without entering mandatory fields data----------
describe('Check New Instrument creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allInstruments);
        browser.sleep(5000);
    });


    it('With Out Instrument Type', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

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


    it('With Out Instrument Name', function () {

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

    it('With Out Asset Type ', function () {

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

        browser.wait(EC.presenceOf(urlsPage.byModel('newInstrumentVm.newInstrument.name').sendKeys('Instrument 27')), 35000);
        browser.sleep(2000);

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

    it('With Out Asset Name ', function () {

        var selectbuttonClick = element(by.id('selectAssetType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectAssertType = element(by.id('mroObjectTypeTree')).element(by.xpath('li/ul/li/div'));
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
        expect(alertMessage).toEqual('Please enter asset name');
        browser.sleep(5000);
    });

    it('With Out Select Meter ', function () {

        var selectbuttonClick = element(by.id('selectAssetType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectAssertType = element(by.id('mroObjectTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAssertType), 15000);
        selectAssertType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 2022')), 35000);

        var clickMeteredToggle = element(by.css("[for='isMetered']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickMeteredToggle), 15000);
        clickMeteredToggle.click();
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select atleast one meter');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

    });

});

//-------Check New Instrument creation->with entering mandatory fields data----------
describe('Check New Instrument creation->by entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allInstruments);
        browser.sleep(5000);
    });

    afterAll(function () {
        browser.get(urlsPage.allInstruments);
        browser.sleep(5000);
    });

    it(' New Instrument create Successfull Test Case', function () {

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(5000);

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

        browser.wait(EC.presenceOf(urlsPage.byModel('newInstrumentVm.newInstrument.name').sendKeys('Instrument 211720')), 35000);

        var selectbuttonClick = element(by.id('selectAssetType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectAssertType = element(by.id('mroObjectTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAssertType), 15000);
        selectAssertType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 222120')), 35000);

        var clickMeteredToggle = element(by.css("[for='isMetered']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickMeteredToggle), 15000);
        clickMeteredToggle.click();
        browser.sleep(1000);

        var selectMeter = element(by.model('asset.meters'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMeter), 15000);
        selectMeter.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Instrument created successfully');
        browser.sleep(8000);
    });

});