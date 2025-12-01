var urlsPage = require('../../../../../pages/urlsPage');

it('Check Waiver tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var changesButton = element(by.id('changes'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(changesButton), 15000);
    changesButton.click();

    var waiverButton = element(by.id('waivers'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(waiverButton), 25000);
    waiverButton.click();
    browser.sleep(1000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allWaiver);
    browser.sleep(8000);
});

describe('All Waiver Page Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allWaiver);
        browser.sleep(5000);
    });

    it('new Waiver Button Text and Title test case', function () {
        var button = element(by.id('newVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 15000);
        expect(button.getText()).toEqual('New Waiver');
        browser.sleep(2000);
        expect(button.getAttribute('title')).toEqual('New Waiver');
        browser.sleep(5000);
    });

    
    it('New Waiver Button click -> open new Waiver creation Side panel test case', function () {
        var newVariance = element(by.id('newVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newVariance), 10000);
        newVariance.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Waiver');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close new Waiver creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newVariance').isDisplayed(true));
        browser.sleep(5000);
    });


    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
    });

    it('Attributes Button click', function () {
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

    it('preferenPage Button Title', function () {
        var preferenPageButtonTitle = element(by.id('preferedPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
    });

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferedPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });

});
