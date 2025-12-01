var urlsPage = require('../../../../pages/urlsPage');

it('Check Substances tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 10000);
    sideManuButton.click();
    browser.sleep(2000);
    var complianceButton = element(by.id('compliance'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(complianceButton), 10000);
    complianceButton.click();
    browser.sleep(4000);
    
    var substancesButton = element(by.id('substances'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(substancesButton), 10000);
    substancesButton.click();
    browser.sleep(1000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allSubstances);
    browser.sleep(8000);
});


describe('All Substances Page Test Cases :', function () {

    beforeAll(function () {
        browser.get(urlsPage.allSubstances);
        browser.sleep(5000);
    })

    it('New Substance Button text and Title test case', function () {
        var newSubstance = element(by.id('newSubstance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newSubstance), 15000);
        expect(newSubstance.getText()).toEqual('New Substance');
        browser.sleep(2000);
        expect(newSubstance.getAttribute('title')).toEqual('New Substance');
    });


    it('New Substance Button click -> open new Substance creation Side panel test case', function () {
        var newSubstanceButton = element(by.id('newSubstance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newSubstanceButton), 10000);
        newSubstanceButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Substance');
        browser.sleep(5000);
    });


    it('Click Close(X) button -> close new Substance creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newSubstance').isDisplayed(true));
        browser.sleep(5000);
    });

    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
    });

    it('Attributes Button click-> open Attributes Side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
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

