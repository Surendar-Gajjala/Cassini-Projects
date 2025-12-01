var urlsPage = require('../../../../pages/urlsPage');

describe('All Suppliers Page Test cases:', function () {
    
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

        var suppliersButton = element(by.id('suppliers'));
        browser.executeScript("arguments[0].scrollIntoView();", suppliersButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(suppliersButton), 25000);
        suppliersButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allSuppliers);
        browser.sleep(5000);
    });

    it('Check New Supplier Button Title', function () {
        browser.get(urlsPage.allSuppliers); 
        browser.sleep(5000);
        var buttonTitle = element(by.id('newSupplier'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(buttonTitle), 15000);
        expect(buttonTitle.getAttribute('title')).toEqual('New Supplier');
        browser.sleep(3000);
    });

    it('New Supplier Button Text', function () {
        var button = element(by.id('newSupplier'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 15000);
        expect(button.getText()).toEqual('New Supplier');
        browser.sleep(3000);
    });

     //---------Check New Supplier button Click -> open new Supplier sidepanel -----------
     it('Check New Supplier button Click -> open new Supplier sidepanel ', function () {
        browser.sleep(2000);
        var newButton = element(by.id('newSupplier'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Supplier');
        browser.sleep(5000);
    });

    //---------Check Close(X) button Click -> close new Supplier sidepanel -----------
    it('Check Close(X) button Click -> close new Supplier sidepanel ', function () {

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(2000);
        var newButton = element(by.id('newSupplier'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.isDisplayed(true));
        browser.sleep(5000);
    });

});

