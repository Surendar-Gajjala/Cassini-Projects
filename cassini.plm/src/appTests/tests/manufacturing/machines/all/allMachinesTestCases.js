var urlsPage = require('../../../../pages/urlsPage.js');

 //---------Check Machines tab in the side navigation-------
 it('Check Machines tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var sidePanelButton = element(by.id('machines'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allMachines);
    browser.sleep(5000);

}); 

describe('All Machines Page Test Cases: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMachines);
        browser.sleep(5000);
    });
   

     //---------Check New Machine Button Text and Tooltip-----------
    it(" New Machine Button Text and Tooltip testCase ", function () {
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Machine');
        expect(newButtonn.getAttribute('title')).toEqual('New Machine');
        browser.sleep(5000);

    });

     //---------Check New Machine button-----------
     it('Click New Machine button -> open New Machine Sidepanel Test cases ', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Machine');
        browser.sleep(5000);

    });

    // ----------Check Close(X) button on the New Machine screen-----------
    it("Check Close(X) button on the New Machine screen", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Machine');

    });


   
});

