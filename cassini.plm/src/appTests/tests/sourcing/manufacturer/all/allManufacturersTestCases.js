var urlsPage = require('../../../../pages/urlsPage');

describe('All Manufacturer Page Test case:',function(){

    it('Check Manufacturer tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var sourcingButton = element(by.id('oem'));
        browser.executeScript("arguments[0].scrollIntoView();", sourcingButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sourcingButton), 15000);
        sourcingButton.click();

        var manufacturersButton = element(by.id('manufacturers'));
        browser.executeScript("arguments[0].scrollIntoView();", manufacturersButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(manufacturersButton), 25000);
        manufacturersButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allManufacturers);
        browser.sleep(5000);
    });

    it('Check New Manufacturer Button Title', function () {
        //browser.get(urlsPage.allManufacturers); 
        var buttonTitle = element(by.id('newManufacturer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(buttonTitle), 15000);
        expect(buttonTitle.getAttribute('title')).toEqual('New Manufacturer');
        browser.sleep(3000);
    });

    it('New Manufacturer Button Text', function () {
        var button = element(by.id('newManufacturer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 15000);
        expect(button.getText()).toEqual('New Manufacturer');
        browser.sleep(3000);
    });

     //---------Check New Manufacturer button Click -> open new Manufacturer sidepanel-----------
     it('Check New Manufacturer button Click -> open new Manufacturer sidepanel ', function () {
        browser.sleep(2000);
        var newButton = element(by.id('newManufacturer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Manufacturer');
        browser.sleep(5000);

    });

     //---------Check New Manufacturer button Click -> close new Manufacturer sidepanel -----------
     it('Check close(X) button Click -> close new Manufacturer sidepanel ', function () {
        browser.sleep(2000);
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.id('newManufacturer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.isDisplayed(true));
        browser.sleep(5000);
    });

})


