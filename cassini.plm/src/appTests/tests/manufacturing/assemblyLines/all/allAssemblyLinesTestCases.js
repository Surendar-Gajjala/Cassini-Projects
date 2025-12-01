var urlsPage = require('../../../../pages/urlsPage.js');

 //---------Check Assembly Lines tab in the side navigation-------
 it('Check Assembly Lines tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var assembliLineButton = element(by.id('assembliLines'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(assembliLineButton), 25000);
    assembliLineButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allAssembliLines)

});

describe('All Assembly Lines Page :', function () {

    beforeAll(function () {
        browser.get(urlsPage.allAssembliLines);
        browser.sleep(5000);
    });

    
    it(" New Assembly Line Button Text and Tooltip testCase ", function () {
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        browser.sleep(2000);
        expect(newButtonn.getText()).toEqual('New Assembly Line');
        expect(newButtonn.getAttribute('title')).toEqual('New Assembly Line');
        browser.sleep(5000);
    });

    it("Click New Assembly Line button -> Open New Assembly Line Side panel Test case", function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Assembly Line');
        browser.sleep(5000);

    });

    it("Check Close(X) button -> close New Assembly Line Sidepanel Test case", function () {
        
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);

    });

});