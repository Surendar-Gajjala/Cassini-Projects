var urlsPage = require('../../../../pages/urlsPage.js');

describe('Design Data -> Vaults:', function () {

    //---------Check Design Data Vaults tab in the side navigation -------
    it('Check Vaults tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(1000);
        var designDataButton = element(by.id('designData'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(designDataButton), 15000);
        designDataButton.click();
        browser.sleep(5000);
        var sidePanelvaultsButton = element(by.id('vaults'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelvaultsButton), 25000);
        sidePanelvaultsButton.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allVaults);
        browser.sleep(9500);
    });

    //---------Page Heading test case-------
    it('Page Heading test case', function () {
        browser.get(urlsPage.allVaults);
        browser.sleep(5000);
        var pageTitle = element(by.css('.view-toolbar')).element(by.tagName('span'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(pageTitle), 15000);
        expect(pageTitle.getText()).toBe('Vaults');
        browser.sleep(6000);
    });

    //---------New Vault Button Text and Tooltip testCase----------
    it("New Vault Button Text and Tooltip testCase ", function () {
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Vault');
        expect(newButtonn.getAttribute('title')).toEqual('New Vault');
        browser.sleep(7000);
    });

    //---------Check Close(X) button on the New Vault screen-----------
    it("Check Close(X) button on the New Vault screen", function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Vault');
        expect(newButtonn.getAttribute('title')).toEqual('New Vault');

    });

    //---------Check New Vault button-----------
    it('Check New Vault button click', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Vault');
        browser.sleep(8000);
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });

});

 