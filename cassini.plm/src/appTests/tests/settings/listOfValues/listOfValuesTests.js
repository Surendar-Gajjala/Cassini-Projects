var urlsPage = require('../../../pages/urlsPage.js');

describe('Settings -> List of Values :', function () {

    beforeAll(function () {
        browser.get(urlsPage.homeUrl);
        browser.sleep(5000);
    });

    //---------Select List of Values-------
    it('Select List of Values ', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(3000);
        var settingsButton = element(by.id('settings'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(settingsButton), 15000);
        settingsButton.click();
        browser.sleep(5000);

        var selectAttribute = element.all(by.id('settingsTree')).all(by.xpath('li/ul/li')).get(2);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAttribute), 15000);
        selectAttribute.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toEqual(urlsPage.lovsUrl);
        browser.sleep(10000);
    });


    //------------------List of Values Header Text testCase---------------------
it("Header Text testCase ", function () {
        browser.sleep(1000);
        var header = element(by.css('h3.ng-scope'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(header), 15000);
        expect(header.getText()).toEqual('List of Values');
        browser.sleep(10000);
    });

    //---------Check New List of Values Button Tooltip-----------
    it("Check New List of Values Button Tooltip", function () {
        browser.sleep(1000);
        var newAttributeButtonTitle = element(by.id('lovAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newAttributeButtonTitle), 15000);
        expect(newAttributeButtonTitle.getAttribute('title')).toEqual('New List of Values');
        browser.sleep(8000);
    });

    //---------Check New List of Values Button Click-----------
    it("Check New List of Values Button Click", function () {
        browser.sleep(1000);
        var newAttributeButton = element(by.id('lovAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newAttributeButton), 15000);
        newAttributeButton.click();
        browser.sleep(2000);
        expect(element(by.model('lov.newName')).isPresent()).toBe(true);
        browser.sleep(8000);
    });

    //---------Check New List of Values Button Click-----------
    it("Check New List of Values close Button Click", function () {
        var cancelChanges = element(by.css('[ng-click="lovsVm.cancelLovChange(lov);lov.editTitle = false;"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelChanges), 15000);
        expect(cancelChanges.getAttribute('title')).toEqual('Cancel changes');
        browser.sleep(2000);
        cancelChanges.click();
        browser.sleep(2000);
        expect(element(by.model('lov.newName')).isPresent()).toBe(false);
        browser.sleep(8000);
    });


    //---------Check New LoV Button Click-----------
    it("Create New LoV", function () {

        var newAttributeButton = element(by.id('lovAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newAttributeButton), 15000);
        newAttributeButton.click();

        var attributeName = element(by.model('lov.newName'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeName), 15000);
        attributeName.sendKeys('Test Lov 1');
        browser.sleep(2000);

        var saveAttribute = element(by.css('[ng-click="lov.editTitle = false;lovsVm.applyChanges(lov)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveAttribute), 15000);
        saveAttribute.click();
        browser.sleep(2000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('LOV saved successfully');
        browser.sleep(8000);

    });





});
