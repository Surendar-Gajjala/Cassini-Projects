var urlsPage = require('../../../../pages/urlsPage.js');

 //---------Check Equipments tab in the side navigation-------
 it('Check Equipments tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var sidePanelButton = element(by.id('equipments'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allEquipments);
    browser.sleep(6000);

});

describe('All Equipments Page Test Cases: ', function () {
    
    beforeAll(function () {
        browser.get(urlsPage.allEquipments);
        browser.sleep(5000);
    });

     //---------Check New Equipment Button Text and Tooltip-----------
    it("New Equipment Button Text and Tooltip testCase ", function () {        
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        browser.sleep(2000);
        expect(newButtonn.getText()).toEqual('New Equipment');
        expect(newButtonn.getAttribute('title')).toEqual('New Equipment');
        browser.sleep(5000);
    });

    //--------Check New Equipment button click -> open New Equipment Sidepanel Test case-----
    it('Check New Equipment button click -> open New Equipment Sidepanel Test case', function () {
        browser.get(urlsPage.allEquipments);
        browser.sleep(2000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Equipment');
        browser.sleep(5000);
    });

    // ----------Check Close(X) button -> close the New Equipment sidepanel test case-----------
    it("Check Close(X) button -> close the New Equipment sidepanel test case", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Equipment');
        browser.sleep(5000);

    });  
});

