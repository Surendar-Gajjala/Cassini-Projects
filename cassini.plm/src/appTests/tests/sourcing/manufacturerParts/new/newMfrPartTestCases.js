var urlsPage = require('../../../../pages/urlsPage');

//-------Check New Manufacturer creation->without entering mandatory fields data----------
describe('Check New Manufacturer Part creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allManufacturerParts); 
        browser.sleep(5000);
    });


    it('Without  select Manufacturer', function () {
        var newButton = element(by.id('newManufacturerPart'));
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
        expect(alertMessage).toEqual('Please select manufacturer');
        browser.sleep(5000);
    });

    it('Without select Manufacturer Part type', function () {

        var selectManufacturer  =  element(by.model('newManufacturerPartVm.newManufacturepart.manufacturer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectManufacturer), 15000);
        selectManufacturer.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manufacturer part type cannot be empty');
        browser.sleep(5000);

    });


    it('Without Part Number ', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteManufacturerType =  element(by.id('manufacturerPartTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteManufacturerType), 15000);
        selecteManufacturerType.click(); 
        browser.sleep(3000);
       
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manufacturer part number cannot be empty');
        browser.sleep(5000);

    });


    it('Without Part Name', function () {

        var partNumber =  element(by.model('newManufacturerPartVm.newManufacturepart.partNumber'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(partNumber), 15000);
        partNumber.sendKeys('part002'); 
       
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manufacturer part name cannot be empty');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });
    
});

//-------Check New ManufacturerParts creation->with entering mandatory fields data----------
describe('Check New ManufacturerParts creation->with entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allManufacturerParts); 
        browser.sleep(5000);
    });

    it('New Manufacturer Part  Creation successfully', function () {
        var newButtonClick =element(by.id('newManufacturerPart'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Manufacturer Part');


        var selectManufacturer  =  element(by.model('newManufacturerPartVm.newManufacturepart.manufacturer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectManufacturer), 15000);
        selectManufacturer.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteManufacturerType =  element(by.id('manufacturerPartTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteManufacturerType), 15000);
        selecteManufacturerType.click(); 
        browser.sleep(3000);
        
        browser.wait(EC.presenceOf(urlsPage.byModel('newManufacturerPartVm.newManufacturepart.partNumber').sendKeys('part00012')), 30000);

       browser.wait(EC.presenceOf(urlsPage.byModel('newManufacturerPartVm.newManufacturepart.partName').sendKeys('Nut 11')), 30000);

       browser.wait(EC.presenceOf(urlsPage.byModel('newManufacturerPartVm.newManufacturepart.description').sendKeys('description')), 30000);
       
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(2500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manufacturer part created successfully');
        browser.sleep(8000);
    });

});
