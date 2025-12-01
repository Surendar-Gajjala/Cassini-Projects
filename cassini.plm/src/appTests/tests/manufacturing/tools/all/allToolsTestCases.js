var urlsPage = require('../../../../pages/urlsPage.js');

//---------Check Tools tab in the side navigation-------
it('Check Tools tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();
    browser.sleep(3000);

    var sidePanelButton = element(by.id('toolType'));
    browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
    browser.sleep(3000);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allTools);
    browser.sleep(5000);
});

describe('All Tools Page Test cases: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allTools);
        browser.sleep(5000);
    });

    //---------Check New Tool Button Text and Tooltip-----------
   it(" New Tool Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allTools);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Tool');
        expect(newButtonn.getAttribute('title')).toEqual('New Tool');
    });

     //---------Click New Tool button -> open New Tool Sidepanel Test case-----------
     it('Click New Tool button -> open New Tool Sidepanel Test case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Tool');
    });

    // ----------Check Close(X) button on the New Tool screen-----------
    it("Check Close(X) button on the New Tool screen", function () {
       
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Tool');

    });

   

});

