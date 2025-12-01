var urlsPage = require('../../../../pages/urlsPage');

it('Check NCR tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var manufacturingButton = element(by.id('quality'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var plantButton = element(by.id('ncrs'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(plantButton), 25000);
    plantButton.click();
    browser.sleep(3000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allNcrs);
    browser.sleep(5000);
});


describe('All NCRs Page Test Cases : ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allNcrs);
        browser.sleep(5000);
    });

    it('New NCR Button Text and Title Test case ', function () {
        var newNcrButton = element(by.id('newNcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newNcrButton), 15000);
        expect(newNcrButton.getText()).toEqual('New NCR');
        browser.sleep(2000);
        expect(newNcrButton.getAttribute('title')).toEqual('New NCR');
        browser.sleep(5000);
    });


    it('New NCR Button click -> open new NCR creation Side panel test case', function () {
        var newNcrButton = element(by.id('newNcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newNcrButton), 10000);
        newNcrButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New NCR');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close new NCR creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newNcrButton').isDisplayed(true));
        browser.sleep(5000);
    });


    it('Attributes Button Title', function () {
        browser.get(urlsPage.allNcrs);
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click -> opne Attribute side panel', function () {
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

    it('Click Close(X) button -> close new NCR creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });


    it('preferenPage Button Title', function () {
        browser.get(urlsPage.allNcrs);
        var preferenPageButtonTitle = element(by.id('preferredPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
    });

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferredPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });


});
