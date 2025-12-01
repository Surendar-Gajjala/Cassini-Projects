var urlsPage = require('../../../../pages/urlsPage');

describe(' new Part Request Module',function(){
    beforeAll(function(){
        browser.get(urlsPage.allNewPartRequestsItems);
        browser.sleep(5000);
    });

    it('Check New part request  tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(1000);
        var itemsButton = element(by.id('items-menu'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(itemsButton), 15000);
        itemsButton.click();
        browser.sleep(2000);
        var newPartRequestItemsButton = element(by.id('newPartRequest'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newPartRequestItemsButton), 25000);
        newPartRequestItemsButton.click();
        browser.sleep(15000);
        expect(browser.getCurrentUrl()).toEqual(urlsPage.allNewPartRequestsItems);
        browser.sleep(5000);
    });


    it('new Part Request button click',function(){
        var newButtonClick = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(5000);
        expect(newSidePanelTitle.getText()).toBe('New Part Request');
        browser.sleep(8000);

    });

    it('closed side pannel when we click close button(x)',function() {
        var closeButton = element(by.id('rightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 10000);
        closeButton.click();
        browser.sleep(2000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Part Request');
        browser.sleep(8000);
    });

    //---------- Prefered Page  button Testing --------------

    /*it('preferenPage Button Title', function () {
     var preferenPageButtonTitle = element(by.id('preferedPage'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
     expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
     });

     it('preferenPage Button click', function () {
     var preferenPageButtonClick = element(by.id('preferedPage'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
     preferenPageButtonClick.click();
     browser.sleep(2000);
     });*/

});