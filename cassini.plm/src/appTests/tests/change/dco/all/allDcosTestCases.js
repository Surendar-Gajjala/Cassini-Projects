var urlsPage = require('../../../../pages/urlsPage');

it('Check Dco tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 10000);
    sideManuButton.click();
    browser.sleep(2000);
    var changesButton = element(by.id('changes'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(changesButton), 10000);
    changesButton.click();

    var dcoButton = element(by.id('dcos'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(dcoButton), 10000);
    dcoButton.click();
    browser.sleep(1000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allDcos);
    browser.sleep(8000);
});


describe('Dco Testing', function () {

    beforeAll(function () {
        browser.get(urlsPage.allDcos);
        browser.sleep(5000);
    });

    it('New Dco Button Text and Title TestCase', function () {
        var button = element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 15000);
        expect(button.getText()).toEqual('New DCO');
        browser.sleep(2000);
        expect(button.getAttribute('title')).toEqual('New DCO');
        browser.sleep(5000);
    });

    it('New Dco Button Click -> open new Dco SidePanel TestCase', function () {
        var button = element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 15000);
        button.click();
        browser.sleep(2000);
        var newDcoSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newDcoSidePanelTitle.getText()).toBe('New DCO');
        browser.sleep(5000);
    });

    it('Click close (X) Button Click -> close new Dco SidePanel TestCase', function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(2000);

        expect($('#newButton').isDisplayed(true));
        browser.sleep(5000);
    });

    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
    });

    it('Attributes Button click -> open Attributes sidepanel', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
    });

    it('Click close (X) Button Click -> close Attributes SidePanel TestCase', function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(2000);

        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });

    it('preferenPage Button Title', function () {
        var preferenPageButtonTitle = element(by.id('preferedPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
        browser.sleep(5000);
    });

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferedPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });
});
