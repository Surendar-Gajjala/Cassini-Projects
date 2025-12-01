var urlsPage = require('../../../../pages/urlsPage.js');

  //-------Check New Material creation->without entering mandatory fields data----------
  describe('Check New Material creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMaterials);
        browser.sleep(5000); 
    });


    it('Without Material Type', function () {
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
        expect(alertMessage).toEqual('Material type cannot be empty');
        browser.sleep(5000);
    });


    it('Without Material Name', function () {

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
        expect(alertMessage).toEqual('Material name cannot be empty');
        browser.sleep(5000);
    });

    it('Without QOM  ', function () {

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
     
        browser.wait(EC.presenceOf(urlsPage.byModel('newMaterialVm.newMaterial.name').sendKeys('Material 27')), 35000);
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Material QOM cannot be empty');
        browser.sleep(5000);
    });

    it('Without UOM ', function () {

        var selectQom = element(by.model('newMaterialVm.newMaterial.qomObject'));
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
        expect(alertMessage).toEqual('Material UOM cannot be empty');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });

  

});

//-------Check New Material creation->with entering mandatory fields data----------
describe('Check New Material creation->by entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMaterials);
        browser.sleep(5000); 
    });
    afterAll(function () {
        browser.get(urlsPage.allMaterials);
        browser.sleep(5000);
    });

    it(' New Material create Successfull Test Case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

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
     
        browser.wait(EC.presenceOf(urlsPage.byModel('newMaterialVm.newMaterial.name').sendKeys('Material 310')), 35000);
        browser.sleep(2000);

        var selectQom = element(by.model('newMaterialVm.newMaterial.qomObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQom), 15000);
        selectQom.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);
        var selectQom = element(by.model('newMaterialVm.newMaterial.uomObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQom), 15000);
        selectQom.click();
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
        expect(alertMessage).toEqual('Material created successfully');
        browser.sleep(8000);
    });

});