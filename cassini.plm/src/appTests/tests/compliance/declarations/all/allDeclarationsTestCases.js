var urlsPage = require('../../../../pages/urlsPage');

it('Check Declarations tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 10000);
    sideManuButton.click();
    browser.sleep(2000);

    var complianceButton = element(by.id('compliance'));
    browser.executeScript("arguments[0].scrollIntoView();", complianceButton);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(complianceButton), 10000);
    complianceButton.click();
    browser.sleep(2000);

    var declarationsTab = element(by.id('declarations'));
    browser.executeScript("arguments[0].scrollIntoView();", declarationsTab);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(declarationsTab), 10000);
    declarationsTab.click();
    browser.sleep(3000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allDeclarations);
    browser.sleep(8000);
});


describe('All Declarations Page  Test cases:', function () {
    beforeAll(function () {
        browser.get(urlsPage.allDeclarations);
        browser.sleep(5000);
    });

    it('New Declaration Button text and Title test case', function () {
        var newDeclaration = element(by.id('newDeclaration'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newDeclaration), 15000);
        expect(newDeclaration.getText()).toEqual('New Declaration');
        browser.sleep(2000);
        expect(newDeclaration.getAttribute('title')).toEqual('New Declaration');
    });

    it('New Declaration Button click -> open new Declaration creation Side panel test case', function () {
        var newDeclaration = element(by.id('newDeclaration'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newDeclaration), 10000);
        newDeclaration.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Declaration');
        browser.sleep(5000);
    });


    it('Click Close(X) button -> close new Declaration creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newDeclaration').isDisplayed(true));
        browser.sleep(5000);
    });

    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click-> open Attributes Side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close Attributes Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });

});

