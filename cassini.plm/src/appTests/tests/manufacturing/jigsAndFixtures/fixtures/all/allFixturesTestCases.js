var urlsPage = require('../../../../../pages/urlsPage.js');

 //---------Check Fixtures tab in the side navigation-------
 it('Check Fixtures tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var sidePanelButton = element(by.id('jigsAndFixtures'));
    browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allJigsAndFixtures)

});

describe('Fixtures: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allJigsAndFixtures);
        browser.sleep(5000);
    });
    
    //---------Check New Fixture Button Text and Tooltip-----------
    it(" New Fixture Button Text and Tooltip testCase ", function () {
        var selectFixtureType = element(by.id('fixtureType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFixtureType), 15000);
        selectFixtureType.click();
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Fixture');
        expect(newButtonn.getAttribute('title')).toEqual('New Fixture');
        browser.sleep(5000);
    });

    //---------Click New Fixture button -> open New Fixture Sidepanel Test case----------
    it('Click New Fixture button -> open New Fixture Sidepanel Test case', function () {
        var selectFixtureType = element(by.id('fixtureType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFixtureType), 15000);
        selectFixtureType.click();
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Fixture');
        browser.sleep(5000);
    });

    // ----------Check Close(X) button on the New Fixture screen-----------
    it("Check Close(X) button on the New Fixture screen", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Fixture');
        browser.sleep(5000);
    });
});

