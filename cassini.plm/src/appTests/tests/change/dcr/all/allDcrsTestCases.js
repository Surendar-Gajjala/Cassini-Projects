var urlsPage = require('../../../../pages/urlsPage');

it('Check Dcr tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var changesButton = element(by.id('changes'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(changesButton), 15000);
    changesButton.click();

    var dcrButton = element(by.id('dcrs'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(dcrButton), 25000);
    dcrButton.click();
    browser.sleep(1000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allDcrs);
    browser.sleep(8000);
});


describe('All DCR Page TestCases :', function () {

    beforeAll(function(){
        browser.get(urlsPage.allDcrs);
        browser.sleep(5000);
    });

    it('New Button Text and Title TestCase', function () {
        var button = element(by.id('newDcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 10000);
        expect(button.getText()).toEqual('New DCR');
        browser.sleep(2000);
        expect(button.getAttribute('title')).toEqual('New DCR');
        browser.sleep(5000);
    });

    it('new DCR button click open new DCR sidepanel', function () {
        var newButtonClick = element(by.id('newDcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 5000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New DCR');
        browser.sleep(8000);
    });

    it('close (x) button click -> close new DCR sidepanel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('#newDcrButton').isDisplayed()).toBe(true);
        browser.sleep(5000);
    });


    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click-> open new DCR side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
        browser.sleep(5000);
    });

    it('close (x) button click -> close new DCR side panel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('#closeRightSidePanel').isDisplayed()).toBe(false);
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
