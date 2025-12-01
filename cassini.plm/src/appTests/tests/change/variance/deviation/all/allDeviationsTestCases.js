var urlsPage = require('../../../../../pages/urlsPage');

     it('Check Deviation tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(2000);
        var changesButton = element(by.id('changes'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changesButton), 15000);
        changesButton.click();
    
        var deviationButton = element(by.id('deviations'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(deviationButton), 15000);
        deviationButton.click();
        browser.sleep(1000);
        expect(browser.getCurrentUrl()).toEqual(urlsPage.allDeviation);
        browser.sleep(5000);
    });


describe('all Deviation Page Test Cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allDeviation);
        browser.sleep(5000);
    });

    it('Button Text and Title test case', function () {
        var newVariance = element(by.id('newVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newVariance), 15000);
        expect(newVariance.getAttribute('title')).toEqual('New Deviation');
        expect(newVariance.getText()).toEqual('New Deviation');

    });

    it('New Deviation Button click -> open new Deviation creation Side panel test case', function () {
        var newVariance = element(by.id('newVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newVariance), 10000);
        newVariance.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Deviation');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close new Deviation creation Side panel test case', function () {
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

