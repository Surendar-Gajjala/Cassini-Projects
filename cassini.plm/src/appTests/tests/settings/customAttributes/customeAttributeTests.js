var urlsPage = require('../../../pages/urlsPage.js');

describe('Settings -> Custom Attributes :', function () {

    beforeAll(function () {
        browser.get(urlsPage.homeUrl);
        browser.sleep(5000);
    });

    //---------Select Custom Attributes-------
    it('Select Custom Attributes ', function () {
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

        var selectAttribute = element.all(by.id('settingsTree')).all(by.xpath('li/ul/li')).get(3);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAttribute), 15000);
        selectAttribute.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toEqual(urlsPage.customAttributesUrl);
        browser.sleep(10000);
    });


    //------------------Custom Attributes Header Text testCase---------------------
it("Header Text testCase ", function () {
        browser.sleep(1000);
        var header = element(by.css('h3.ng-scope'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(header), 15000);
        expect(header.getText()).toEqual('Custom Attributes');
        browser.sleep(10000);
    });

    //---------Check New Attribute Button Tooltip-----------
    it("Check New Attributes Button Tooltip", function () {
        browser.sleep(1000);
        var newAttributeButtonTitle = element(by.id('propAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newAttributeButtonTitle), 15000);
        expect(newAttributeButtonTitle.getAttribute('title')).toEqual('New Attribute');
        browser.sleep(8000);
    });

    //---------Check New Attribute Button Click-----------
    it("Check New Attribute Button Click", function () {
        browser.sleep(1000);
        var newAttributeButton = element(by.id('propAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newAttributeButton), 15000);
        newAttributeButton.click();
        browser.sleep(2000);
        expect(element(by.model('prop.newName')).isPresent()).toBe(true);
        browser.sleep(8000);
    });

    //---------Check New Attribute Button Click-----------
    it("Check New Attribute close Button Click", function () {
        var cancelChanges = element(by.css('[ng-click="propsVm.cancelChanges(prop);prop.editMode = false;"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelChanges), 15000);
        expect(cancelChanges.getAttribute('title')).toEqual('Cancel changes');
        browser.sleep(2000);
        cancelChanges.click();
        browser.sleep(2000);
        expect(element(by.model('prop.newName')).isPresent()).toBe(false);
        browser.sleep(8000);
    });


    describe('Create -> new attribute  without Entering mandatory fields', function () {

        beforeAll(function () {
            var newAttributeButton = element(by.id('propAddButton'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newAttributeButton), 15000);
            newAttributeButton.click();
        });

        it('Without Attribute name', function () {
            var saveAttribute = element(by.css('[ng-click="propsVm.applyChanges(prop)"]'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(saveAttribute), 15000);
            saveAttribute.click();
            browser.sleep(2000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Attribute name cannot be empty');
            browser.sleep(8000);
        });

        it('Without data type name', function () {

            var attributeName = element(by.model('prop.newName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(attributeName), 15000);
            attributeName.sendKeys('Test Attribute');
            browser.sleep(2000);
            var saveAttribute = element(by.css('[ng-click="propsVm.applyChanges(prop)"]'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(saveAttribute), 15000);
            saveAttribute.click();
            browser.sleep(2000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Attribute data type cannot be empty');
            browser.sleep(8000);

            var cancelChanges = element(by.css('[ng-click="propsVm.cancelChanges(prop);prop.editMode = false;"]'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(cancelChanges), 15000);
            expect(cancelChanges.getAttribute('title')).toEqual('Cancel changes');
            browser.sleep(2000);
            cancelChanges.click();
        });



    });


    //---------Check New attribute Button Click-----------
    it("Create New attribute", function () {

        var newAttributeButton = element(by.id('propAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newAttributeButton), 15000);
        newAttributeButton.click();

        var attributeName = element(by.model('prop.newName'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeName), 15000);
        attributeName.sendKeys('Test Attribute1');
        browser.sleep(2000);

        var select = element(by.id('dropdownMenuButton0'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(select), 15000);
        select.click();
        browser.sleep(2000);
        var selectDataType = element(by.id('dropdownMenu0')).all(by.xpath('li/a')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDataType), 15000);
        selectDataType.click();
        browser.sleep(4000);

        var saveAttribute = element(by.css('[ng-click="propsVm.applyChanges(prop)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveAttribute), 15000);
        saveAttribute.click();
        browser.sleep(2000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Attribute saved successfully');
        browser.sleep(8000);

    });





});
