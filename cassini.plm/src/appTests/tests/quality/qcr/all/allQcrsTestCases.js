var urlsPage = require('../../../../pages/urlsPage');

it('Check QCR tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);

    var manufacturingButton = element(by.id('quality'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();
    browser.sleep(2000);
    var plantButton = element(by.id('qcrs'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(plantButton), 25000);
    plantButton.click();
    browser.sleep(3000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allQcrs);
    browser.sleep(5000);
});

describe('QCR Testing ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allQcrs);
        browser.sleep(5000);
    });

    it('New QCR Button Text and Title Test case', function () {
        var newQcrButton = element(by.id('newQcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newQcrButton), 15000);
        expect(newQcrButton.getText()).toEqual('New QCR');
        browser.sleep(2000);
        expect(newQcrButton.getAttribute('title')).toEqual('New QCR');
        browser.sleep(5000);
    });

   
    it('New QCR Button click -> open New QCR creation Side panel test case', function () {
        var newQcrButton = element(by.id('newQcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newQcrButton), 10000);
        newQcrButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New QCR');
        browser.sleep(5000);
    });


    it('Click Close(X) button -> close New QCR creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newQcrButton').isDisplayed(true));
        browser.sleep(5000);
    });


    it('Attributes Button Title', function () {
        browser.get(urlsPage.allQcrs);
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click -> open Attributes Side panel test case', function () {
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
        browser.get(urlsPage.allQcrs);
        var preferenPageButtonTitle = element(by.id('preferredPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
        browser.sleep(5000);
    });

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferredPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });

});
