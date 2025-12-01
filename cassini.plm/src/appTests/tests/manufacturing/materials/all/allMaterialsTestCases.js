var urlsPage = require('../../../../pages/urlsPage.js');

//---------Check Materials tab in the side navigation-------
it('Check Materials tab in the side navigation', function () {
       
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    browser.sleep(3000);

    var sidePanelButton = element(by.id('materials'));
    browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
    browser.sleep(3000);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(3000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allMaterials);
    browser.sleep(5000);

});

describe('Materials: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMaterials);
        browser.sleep(5000);
    });
    
    
     //---------Check New Material Button Text and Tooltip-----------
    it(" New Material Button Text and Tooltip testCase ", function () {
        browser.get(urlsPage.allMaterials);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Material');
        expect(newButtonn.getAttribute('title')).toEqual('New Material');
        browser.sleep(5000);

    });

    //---------Click New Material button -> open New Material Sidepanel Test case-----------
    it('Click New Material button -> open New Material Sidepanel Test case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Material');
        browser.sleep(5000);

    });

    // ----------Click Close(X) button -> close  the New Material Sidepanel screen-----------
    it("Click Close(X) button -> close  the New Material Sidepanel screen", function () {

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Material');

    });

  

});