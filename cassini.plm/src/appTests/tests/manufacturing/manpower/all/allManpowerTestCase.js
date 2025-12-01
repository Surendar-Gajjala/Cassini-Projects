var urlsPage = require('../../../../pages/urlsPage.js');

  //---------Check Manpower tab in the side navigation-------
  it('Check Manpower tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var sidePanelButton = element(by.id('manpower'));
    browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allManpower);
    browser.sleep(5000);
});


describe('All Manpower Page Test cases: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(5000);
    });
  
    //---------Check New Manpower Button Text and Tooltip-----------
    it("New Manpower Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Manpower');
        expect(newButtonn.getAttribute('title')).toEqual('New Manpower');
        browser.sleep(5000);
    });

     //---------Click New Manpower button -> open New Manpower Sidepanel-----------
     it('Click New Manpower button -> open New Manpower Sidepanel', function () {
       
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Manpower');
        browser.sleep(5000);
    });

    // ----------Check Close(X) button on the New Manpower screen-----------
    it("Check Close(X) button on the New Manpower screen", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Manpower');
        browser.sleep(5000);

    });

});

