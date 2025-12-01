var urlsPage = require('../../../../pages/urlsPage');

describe('New Manufacturer Test cases:',function(){

    beforeAll(function () {
        browser.get(urlsPage.allManufacturers);
        browser.sleep(5000);
    });


    it('Without  select Manufacturer Type -> create new Manufacturer', function () {
        var newButton = element(by.id('newManufacturer'));
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
        expect(alertMessage).toEqual('Manufacturer type cannot be empty');
        browser.sleep(5000);
    });

    it('Without enter Manufacturer Name -> create new Manufacturer', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectType = element(by.id('manufacturerTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectType), 15000);
        selectType.click();
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manufacturer name cannot be empty');
        browser.sleep(8000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);
    });

    //-------New Manufacturer Creation successfull Test Case----------
    it('New Manufacturer Creation successfull Test Case', function () {
        var newButtonClick =element(by.id('newManufacturer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Manufacturer');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteManufacturerType =  element(by.id('manufacturerTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteManufacturerType), 15000);
        selecteManufacturerType.click();
        browser.sleep(3000);
        
       browser.wait(EC.presenceOf(urlsPage.byModel('newMfrVm.newManufacture.name').sendKeys('Ashok layland 123')), 30000);

       browser.wait(EC.presenceOf(urlsPage.byModel('newMfrVm.newManufacture.description').sendKeys('description')), 30000);
       
       browser.wait(EC.presenceOf(urlsPage.byModel('newMfrVm.newManufacture.phoneNumber').sendKeys('9876543210')), 30000);

       browser.wait(EC.presenceOf(urlsPage.byModel('newMfrVm.newManufacture.contactPerson').sendKeys('Ashok')), 30000);

  
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manufacturer created successfully');
        browser.sleep(8000);
    });
});