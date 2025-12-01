var urlsPage = require('../../../../pages/urlsPage');

//-------Check New Supplier creation->without entering mandatory fields data----------
describe('Check New Supplier Part creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allSuppliers);
        browser.sleep(5000);
    });


    it('Without select Supplier Type', function () {
        var newButton = element(by.id('newSupplier'));
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
        expect(alertMessage).toEqual('Please select type');
        browser.sleep(5000);
    });

    it('Without Name', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteSupplierType = element(by.id('supplierTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteSupplierType), 15000);
        selecteSupplierType.click();
        browser.sleep(3000);

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
        browser.sleep(5000);


    });

});

//-------Check New Supplier creation->with entering mandatory fields data----------
describe('Check New Supplier creation->with entering mandatory fields data', function () {

    it('New Supplier  Creation Successfully Testing', function () {
        var newButtonClick = element(by.id('newSupplier'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Supplier');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteSupplierType = element(by.id('supplierTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteSupplierType), 15000);
        selecteSupplierType.click();
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.name').sendKeys('Supplier 125')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.description').sendKeys('Nut 1')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.address').sendKeys('description')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.city').sendKeys('description')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.country').sendKeys('India')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.postalCode').sendKeys('522213')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.phoneNumber').sendKeys('086352213')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.mobileNumber').sendKeys('9703395735')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.faxAddress').sendKeys('faxAddress')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.email').sendKeys('ashok@gmail.com')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSupplierVm.newSupplier.webSite').sendKeys('www.cassiniPLM.com')), 30000);
        browser.sleep(3000);


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(2500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Supplier created successfully');
        browser.sleep(5000);
    });

});

