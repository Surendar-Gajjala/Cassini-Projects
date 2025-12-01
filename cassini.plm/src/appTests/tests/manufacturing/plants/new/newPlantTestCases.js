var urlsPage = require('../../../../pages/urlsPage.js');

 //-------Check New Plant creation->without entering mandatory fields data----------
describe('Check New Plant creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allPlants);
        browser.sleep(5000);

        var newPlantButton = element(by.id('newPlant'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newPlantButton), 15000);
        newPlantButton.click();
        browser.sleep(2000);

    });

    it('With Out plant Type ', function () {
      
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

    it('With Out plant Name ', function () {

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

    it('With Out plant Manager ', function () {

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

        browser.wait(EC.presenceOf(urlsPage.byModel('newPlantVm.newPlant.name').sendKeys('Plant 27')), 35000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Please select plant manager');
        browser.sleep(5000);
    });

    it('With Out Asset Type ', function () {

        var SelectPlantManager = element(by.model('newPlantVm.newPlant.plantPerson'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(SelectPlantManager), 15000);
        SelectPlantManager.click();
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

        browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 222')), 35000);

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

it('Check New Plant creation->by entering mandatory fields data', function () {

    browser.get(urlsPage.allPlants);
    browser.sleep(5000);

    var newPlantButton = element(by.id('newPlant'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(newPlantButton), 15000);
    newPlantButton.click();
    browser.sleep(2000);

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

    browser.wait(EC.presenceOf(urlsPage.byModel('newPlantVm.newPlant.name').sendKeys('Plant 211337')), 35000);

    var SelectPlantManager = element(by.model('newPlantVm.newPlant.plantPerson'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(SelectPlantManager), 15000);
    SelectPlantManager.click();
    browser.sleep(1000);
    element.all(by.css('.ui-select-choices-row-inner div')).first().click();

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

    browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 1023')), 35000);

    var clickMeteredToggle = element(by.css("[for='isMetered']"));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(clickMeteredToggle), 15000);
    clickMeteredToggle.click();
    browser.sleep(1000);

    var SelectPlantManager = element(by.model('asset.meters'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(SelectPlantManager), 15000);
    SelectPlantManager.click();
    browser.sleep(1000);
    element.all(by.css('.ui-select-choices-row-inner div')).first().click();
    browser.sleep(1000);

    var createButton = element(by.buttonText('Create'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(createButton), 10000);
    createButton.click();
    browser.sleep(1800);
    var plantSuccessMes = element(by.id('alertMessage')).getText();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(plantSuccessMes), 5000, "Text is not something I've expected");
    expect(plantSuccessMes).toEqual('Plant created successfully');
    browser.sleep(8000);

    browser.get(urlsPage.allPlants);
    browser.sleep(5000);
});