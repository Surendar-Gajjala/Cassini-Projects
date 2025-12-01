var urlsPage = require('../../../../pages/urlsPage');


describe('Items Module',function(){
    beforeAll(function(){
        browser.get(urlsPage.allItems);
        browser.sleep(5000);
    });

    it('Check Item tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(1000);
        var itemsButton = element(by.id('items-menu'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(itemsButton), 15000);
        itemsButton.click();
        var allItemsButton = element(by.id('all-items'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(allItemsButton), 25000);
        allItemsButton.click();
        browser.sleep(15000);
        expect(browser.getCurrentUrl()).toEqual(urlsPage.allItems);
        browser.sleep(5000);

    });


    it('new Item button click',function(){
        var newButtonClick = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(5000);
        expect(newSidePanelTitle.getText()).toBe('New Item');
        browser.sleep(5000);

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
        expect(newButton.getText()).toEqual('New Item');
        browser.sleep(8000);
    });

    // ----------- Search icon ---------------------

    /* it('Search icon Title', function () {
     browser.get('http://localhost:8085/#/app/items/all');
     browser.sleep(8000);
     var searchTitle = element(by.id('searchIcon'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(searchTitle), 10000);
     expect(searchTitle.getAttribute('title')).toEqual('Search');
     browser.sleep(5000);
     });

     it('Search icon Click', function () {
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(searchClick), 10000);
     searchClick.click();
     browser.sleep(2000);
     var searchSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
     expect(searchSidePanelTitle.getText()).toBe('allItems Search');
     browser.sleep(5000);
     });

     // ---------- Saved Search button Testing --------------

     it('savedSearch Button click', function () {
     browser.get('http://localhost:8085/#/app/items/all');
     browser.sleep(8000);
     var savedSearchButtonClick = element(by.id('savedSearch'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(savedSearchButtonClick), 10000);
     savedSearchButtonClick.click();
     browser.sleep(2000);
     var savedSearchSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
     expect(savedSearchSidePanelTitle.getText()).toBe('Saved Searches');
     browser.sleep(5000);
     });
     it('Attributes Button Title', function () {
     var savedSearchButtonTitle = element(by.id('savedSearch'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(savedSearchButtonTitle), 10000);
     expect(savedSearchButtonTitle.getAttribute('title')).toEqual('Saved Searches');
     browser.sleep(5000);
     });

     //---------- folder button Testing --------------

     it('dropdown folder click', function () {
     browser.get('http://localhost:8085/#/app/items/all');
     browser.sleep(8000);
     var folderClick = element(by.id('dropdownButton'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(folderClick), 5000);
     folderClick.click();
     browser.sleep(5000);
     });

     //---------- Attribute button Testing --------------

     it('Attributes Button click', function () {
     browser.get('http://localhost:8085/#/app/items/all');
     browser.sleep(8000);
     var attributeClick = element(by.id('attributes'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(attributeClick), 10000);
     attributeClick.click();
     browser.sleep(5000);
     var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
     expect(attributeSidePanelTitle.getText()).toBe('Attributes');
     browser.sleep(5000);
     });

     it('Attributes Button Title', function () {
     var attributeButtonTitle = element(by.id('attributes'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
     expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Item Attributes');
     browser.sleep(5000);
     });

     //---------- Export Items button Testing --------------

     it('dropdown Export Items click', function () {
     var exportItemsClick = element(by.css('itemsVm.exportItems(csv)'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(exportItemsClick), 5000);
     exportItemsClick.click();
     browser.sleep(5000);
     });

     //---------- Master Details View button Testing --------------

     it('masterDetailsView Button click', function () {
     var attributeClick = element(by.css('itemsVm.shareSelectedItems()'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(masterDetailsViewClick), 10000);
     masterDetailsViewClick.click();
     browser.sleep(5000);
     });

     //---------- Prefered Page  button Testing --------------

     it('preferenPage Button Title', function () {
     var preferenPageButtonTitle = element(by.id('preferedPage'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
     expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
     browser.sleep(5000);
     });

     xit('preferenPage Button click', function () {
     var preferenPageButtonClick = element(by.id('preferedPage'));
     var EC = protractor.ExpectedConditions;
     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
     preferenPageButtonClick.click();
     browser.sleep(5000);
     });*/

});