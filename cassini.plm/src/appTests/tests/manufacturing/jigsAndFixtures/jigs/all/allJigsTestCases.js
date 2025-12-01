var urlsPage = require('../../../../../pages/urlsPage.js');

//---------Check Jigs tab in the side navigation-------
it('Check Jigs tab in the side navigation', function () {
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
    expect(browser.getCurrentUrl()).toBe(urlsPage.allJigsAndFixtures);
    browser.sleep(5000); 

});

describe('All Jigs Page Test cases: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allJigsAndFixtures);
        browser.sleep(5000);
    });

   

    //---------Check New Jig Button Text and Tooltip-----------
    it(" New Jig Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allJigsAndFixtures);
        var selectFixtureType = element(by.id('jigsType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFixtureType), 15000);
        selectFixtureType.click();
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Jig');
        expect(newButtonn.getAttribute('title')).toEqual('New Jig');
        browser.sleep(5000);

    });

     //---------Click New Jig button -> open New Jig  Side panel Test case-----------
     it('Click New Jig button -> open New Jig  Side panel Test case', function () {
       
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Jig');
        browser.sleep(5000);

    });

    // ----------Check Close(X) button -> close New Jig  Side panel Test case-----------
    it("Check Close(X) button -> close New Jig  Side panel Test case", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Jig');
        browser.sleep(5000);

    });


});

