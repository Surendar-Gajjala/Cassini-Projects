var urlsPage = require('../../../../pages/urlsPage.js');

//---------Check Operations tab in the side navigation-------
it('Check Operations tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var sidePanelButton = element(by.id('operations'));
    browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allOperations);
    browser.sleep(6000);

});
describe('All Operations Page Test case: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allOperations);
        browser.sleep(5000);
    });

    //---------Check New Operation Button Text and Tooltip-----------
    it("New Operation Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allOperations);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Operation');
        expect(newButtonn.getAttribute('title')).toEqual('New Operation');
        browser.sleep(5000);
    });

    //--------Click New Operation button -> open New Operation Sidepanel Test case----------
    it('Click New Operation button -> open New Operation Sidepanel Test case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Operation');
        browser.sleep(5000);
    });

    // ---------Click Close(X) button -> close the New Operation Sidepanel-----------
    it("Click Close(X) button -> close the New Operation Sidepanel", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Operation');

    });


   

});

