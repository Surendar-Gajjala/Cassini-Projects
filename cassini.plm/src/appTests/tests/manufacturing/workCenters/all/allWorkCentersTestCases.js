var urlsPage = require('../../../../pages/urlsPage.js');

//---------Check WorkCenters tab in the side navigation-------
it('Check WorkCenters tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var sidePanelButton = element(by.id('workCenters'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allWorkCenters);
    browser.sleep(5000);
});

describe('All WorkCenters Page Test cases: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allWorkCenters);
        browser.sleep(5000);
    });
    //---------Check New Work Center Button Text and Tooltip-----------
    it(" New Work Center Button Text and Tooltip testCase ", function () {

        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Work Center');
        expect(newButtonn.getAttribute('title')).toEqual('New Work Center');
        browser.sleep(5000);
    });

    //---------Click New Work Center button  -> open new Workcenter sidepanel test case-----------
    it('Click New Work Center button  -> open new Workcenter sidepanel test case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Work Center');
        browser.sleep(5000);
    });

    // ----------Check Close(X) button on the New Work Center screen-----------
    it("Check Close(X) button on the New Work Center screen", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Work Center');
        browser.sleep(5000);

    });

});

