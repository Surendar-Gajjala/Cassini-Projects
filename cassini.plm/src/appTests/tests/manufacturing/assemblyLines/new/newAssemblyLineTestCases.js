var urlsPage = require('../../../../pages/urlsPage.js');

describe('Check New Assembly Line creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allAssembliLines);
        browser.sleep(5000);
    });


    it('With Out select plant -> create New Assembly', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select plant');
        browser.sleep(5000);
    });

    it('With Out Assembly Line Type -> create New Assembly', function () {

        var selectPlant = element(by.model('newAssemblyLineVm.newAssemblyLine.plant'));
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
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select type');
        browser.sleep(5000);
    });

    it('With Out Name -> create New Assembly', function () {

        var selectbuttonClick = element(by.id('select'));
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
        expect(plantWarnMes).toEqual('Name cannot be empty');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });

});


describe('Check New Assembly Line creation->with entering mandatory fields data', function () {

    beforeAll(function () {
         browser.get(urlsPage.allAssembliLines);
         browser.sleep(5000);
    });

    afterAll(function () {
        browser.get(urlsPage.allAssembliLines);
        browser.sleep(5000);
    });


    it('new Assembly Created Successful Test case:', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);

        var selectPlant = element(by.model('newAssemblyLineVm.newAssemblyLine.plant'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectPlant), 15000);
        selectPlant.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var selectbuttonClick = element(by.id('select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectManufacturerType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectManufacturerType), 15000);
        selectManufacturerType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newAssemblyLineVm.newAssemblyLine.name').sendKeys('AssemblyLine 27')), 35000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Assembly line created successfully');
        browser.sleep(8000);
    });

});