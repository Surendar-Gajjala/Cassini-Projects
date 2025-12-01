var urlsPage = require('../../../../pages/urlsPage');

it('Check Specification tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 10000);
    sideManuButton.click();
    browser.sleep(2000);
    var complianceButton = element(by.id('compliance'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(complianceButton), 10000);
    complianceButton.click();

    var specificationsTab = element(by.id('specifications'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(specificationsTab), 10000);
    specificationsTab.click();
    browser.sleep(1000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allSpecifications);
    browser.sleep(8000);
});


describe('All Specifications Page Test Cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allSpecifications); 
        browser.sleep(5000);
    });

    it('New Specification Button text and Title Test case', function () {
        var newSpecification = element(by.id('newSpecification'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newSpecification), 15000);
        expect(newSpecification.getText()).toEqual('New Specification');
        browser.sleep(2000);
        expect(newSpecification.getAttribute('title')).toEqual('New Specification');
        browser.sleep(5000);
    });

    it('New Specification Button click -> open new Specification creation Side panel test case', function () {
        var newSpecification = element(by.id('newSpecification'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newSpecification), 10000);
        newSpecification.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Specification');
        browser.sleep(5000);
    });


    it('Click Close(X) button -> close new Specification creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newSpecification').isDisplayed(true));
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

