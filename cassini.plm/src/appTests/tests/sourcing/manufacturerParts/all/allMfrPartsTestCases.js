var urlsPage = require('../../../../pages/urlsPage');

describe('All ManufacturerParts Page Test Cases:', function () {

    it('Check ManufacturerParts tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var sourcingButton = element(by.id('oem'));
        browser.executeScript("arguments[0].scrollIntoView();", sourcingButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sourcingButton), 15000);
        sourcingButton.click();

        var manufacturerPartsButton = element(by.id('manufacturerParts'));
        browser.executeScript("arguments[0].scrollIntoView();", manufacturerPartsButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(manufacturerPartsButton), 25000);
        manufacturerPartsButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allManufacturerParts);
        browser.sleep(3000);
    });

    it('Check New Manufacturer Part Button Title', function () {
        browser.get(urlsPage.allManufacturerParts);
        browser.sleep(5000);
        var buttonTitle = element(by.id('newManufacturerPart'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(buttonTitle), 15000);
        expect(buttonTitle.getAttribute('title')).toEqual('Create MFR Parts');
        browser.sleep(3000);
    });

    it('New Manufacturer Part Button Text', function () {
        var button = element(by.id('newManufacturerPart'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 15000);
        expect(button.getText()).toEqual('New Manufacturer Part');
        browser.sleep(3000);
    });

    //---------Check New ManufacturerParts button Click -> open new Manufacturer Part sidepanel-----------
    it('Check New Manufacturer Part button Click -> open new Manufacturer Part sidepanel', function () {
        browser.sleep(2000);
        var newButton = element(by.id('newManufacturerPart'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Manufacturer Part');
        browser.sleep(5000);

    });

    //---------Check Close(X) button Click -> close new Manufacturer Part sidepanel-----------
    it('Check Close(X) button Click -> close new Manufacturer Part sidepanel ', function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(2000);

        var newButton = element(by.id('newManufacturerPart'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.isDisplayed(true));
        browser.sleep(5000);
    });

});


