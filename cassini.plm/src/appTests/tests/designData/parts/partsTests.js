var urlsPage = require('../../../pages/urlsPage.js');

describe('Design Data -> Parts:', function () {

    //---------Check Design Data Parts tab in the side navigation -------
    it('Check Parts tab in the side navigation', function () {
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
        var sidePanelPartsButton = element(by.id('parts'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelPartsButton), 25000);
        sidePanelPartsButton.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allParts);
        browser.sleep(9500);
    });

    //---------Page Heading test case-------
    it('Page Heading test case', function () {
        browser.get(urlsPage.allParts);
        browser.sleep(5000);
        var pageTitle = element(by.css('.view-toolbar')).element(by.tagName('span'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(pageTitle), 15000);
        expect(pageTitle.getText()).toBe('Parts');
        browser.sleep(6000);
    });

   
});