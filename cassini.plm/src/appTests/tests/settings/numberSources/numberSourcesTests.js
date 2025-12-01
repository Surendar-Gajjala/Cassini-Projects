 var urlsPage = require('../../../pages/urlsPage.js');

describe('Settings -> Number Sources: ', function () {

    //---------Check Admin tab in the side navigation-------
    it('Check Admin tab in the side navigation', function () {
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
        expect(browser.getCurrentUrl()).toBe(urlsPage.numberSourceUrl);
        browser.sleep(10000);
    });

    //---------Header Text testCase-----------
    it("Header Text testCase ", function () {

        browser.sleep(1000);
        var header = element(by.css('h3.ng-scope'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(header), 15000);
        expect(header.getText()).toEqual('Number Sources');
        browser.sleep(10000);
    });

    it(" New Auto Number Tooltip testCase ", function () {
        browser.sleep(1000);
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getAttribute('title')).toEqual('New AutoNumber');
        browser.sleep(10000);
    });

    it(" New Auto Number -> click : open input fields and close input fields Test case", function () {
        browser.sleep(1000);
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(5000);
        expect(element(by.css('.btn-success > .fa')).isPresent()).toBe(true);
        browser.sleep(10000);

        var saveButton = element(by.css('.btn-default > .fa'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(5000);
        expect(element(by.css('.btn-success > .fa')).isPresent()).toBe(false);
        browser.sleep(10000);
    });

});

describe('create new number source with out entering madatory fields', function () {

    it("Without name ", function () {
        browser.sleep(1000);
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var saveButton = element(by.css('.btn-success > .fa'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Name cannot be empty');
        browser.sleep(5000);

        var saveButton = element(by.css('.btn-default > .fa'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(5000);
    });
});

describe('create new number source with entering madatory fields', function () {

    it("create new number source successfuly", function () {
        browser.sleep(1000);
        var newButton = element(by.css('.addAutonumberButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 5000);
        newButton.click();
        browser.sleep(2000);
       
        var enterName = element(by.css('.ng-touched'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(enterName), 15000);
        enterName.sendKeys('Testing  Number source');
        browser.sleep(1000);
        enterName.sendKeys(protractor.Key.TAB);
        browser.sleep(3000);

        var saveButton = element(by.css('.btn-success > .fa'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 10000);
        saveButton.click();

        browser.sleep(1000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Number source created successfully');
        browser.sleep(8000);

    });
});
