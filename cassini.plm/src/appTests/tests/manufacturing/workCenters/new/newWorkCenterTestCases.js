var urlsPage = require('../../../../pages/urlsPage.js');

 //-------Check New Work Center creation->without entering mandatory fields data----------
 describe('Check New Work Center creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allWorkCenters);
        browser.sleep(5000);
    });


    it('With Out select Plant', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Please select plant');
        browser.sleep(5000);
    });

    it('With Out WorkCenter Type', function () {

        var selectPlant = element(by.model('newWorkCenterVm.workCenter.plant'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectPlant), 15000);
        selectPlant.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Please select type');
        browser.sleep(5000);
    });

    it('With Out Name', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectManufacturerType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectManufacturerType), 15000);
        selectManufacturerType.click();
        browser.sleep(1000);


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Please enter name');
        browser.sleep(5000);
    });

    it('With Out Asset Type ', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectManufacturerType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectManufacturerType), 15000);
        selectManufacturerType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newWorkCenterVm.workCenter.name').sendKeys('WorkCenter 27')), 35000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Please select asset type');
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
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Please enter asset name');
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

        browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 2020')), 35000);

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
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Please select atleast one meter');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

    });

});

//-------Check New Work Center creation->with entering mandatory fields data----------
describe('Check New Work Center creation->by entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allWorkCenters);
        browser.sleep(5000);
    });

    afterAll(function () {
        browser.get(urlsPage.allWorkCenters);
        browser.sleep(5000);
    });

    it(' New Work Center create Successfull Test Case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(1000);

        var selectPlant = element(by.model('newWorkCenterVm.workCenter.plant'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectPlant), 15000);
        selectPlant.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectManufacturerType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectManufacturerType), 15000);
        selectManufacturerType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newWorkCenterVm.workCenter.name').sendKeys('WorkCenter 273')), 35000);

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

        browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 2026')), 35000);

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
        browser.sleep(1800);
        var workCenterSuccessMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(workCenterSuccessMes), 5000, "Text is not something I've expected");
        expect(workCenterSuccessMes).toEqual('Work center created successfully');
        browser.sleep(8000);
    });

});