var urlsPage = require('../../../../pages/urlsPage');


it('Check Eco tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var changesButton = element(by.id('changes'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(changesButton), 15000);
    changesButton.click();

    var ecoButton = element(by.id('ecos'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(ecoButton), 25000);
    ecoButton.click();
    browser.sleep(1000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allEcos);
    browser.sleep(8000);
});

describe('All ECOs page Testing', function () {

    beforeAll(function(){
        browser.get(urlsPage.allEcos);
        browser.sleep(5000);
    });


    it('Page Title', function () {
        var title = element(by.css('.view-toolbar')).element(by.tagName('span'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(title), 8000);
        expect(title.getText()).toEqual('ECOs');
        browser.sleep(8000);
    });

    // ----------- New Button Text and Title Test case---------------------

    it('New Button Text and Title Test case', function () {
        var button = element(by.css('.view-toolbar')).element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 8000);
        expect(button.getText()).toEqual('New ECO');
        browser.sleep(3000);
        expect(button.getAttribute('title')).toEqual('New ECO');
        browser.sleep(8000);
    });


    it('new ECO button click open new Eco side panel', function () {
        var newButtonClick = element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New ECO');
    });

    it('close (x) button click -> close new Eco side panel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('#newButton').isDisplayed()).toBe(true);
        browser.sleep(5000);
    });

});


    // ----------- Search icon ---------------------

    // it('Search icon Title', function () {
    //     var searchTitle = element(by.id('searchIcon'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(searchTitle), 10000);
    //     expect(searchTitle.getAttribute('title')).toEqual('Search');
    // });

    // it('Search icon Click', function () {
    //     var searchClick = element(by.id('searchIcon'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(searchClick), 10000);
    //     searchClick.click();
    //     browser.sleep(2000);
    //     var searchSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
    //     expect(searchSidePanelTitle.getText()).toBe('ECOs Search');
    // });


    //---------- folder button Testing --------------
    // it('dropdown folder click', function () {

    //     var folderClick = element(by.id('dropdownButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(folderClick), 5000);
    //     folderClick.click();
    //     browser.sleep(2000);


    // });

    //---------- Attribute button Testing --------------
    // it('Attributes Button click', function () {
    //     var attributeClick = element(by.id('attributes'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(attributeClick), 10000);
    //     attributeClick.click();
    //     browser.sleep(2000);
    //     var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
    //     expect(attributeSidePanelTitle.getText()).toBe('Attributes');
    // });

    // it('Attributes Button Title', function () {
    //     var attributeButtonTitle = element(by.id('attributes'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
    //     expect(attributeButtonTitle.getAttribute('title')).toEqual('Show ECO Attributes');
    // });


    // ---------- Saved Search button Testing --------------
    // it('savedSearch Button click', function () {
    //     var savedSearchButtonClick = element(by.id('savedSearch'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(savedSearchButtonClick), 10000);
    //     savedSearchButtonClick.click();
    //     browser.sleep(2000);
    //     var savedSearchSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
    //     expect(savedSearchSidePanelTitle.getText()).toBe('Saved Searches');
    // });

    // it('Attributes Button Title', function () {
    //     var savedSearchButtonTitle = element(by.id('savedSearch'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(savedSearchButtonTitle), 10000);
    //     expect(savedSearchButtonTitle.getAttribute('title')).toEqual('Saved Searches');
    // });

    //---------- Prefered Page  button Testing --------------
    // it('preferenPage Button Title', function () {
    //     var preferenPageButtonTitle = element(by.id('preferedPage'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
    //     expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
    // });

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferedPage'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });




    //---------- Table Header Testing --------------
    // it('All ECO Table header Testing', function () {

    //     let headerRow = element(by.id('theadRow')).all(by.tagName('th'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(headerRow), 15000);
    //     expect(headerRow.get(1).getText()).toEqual('ECO Number');
    //     expect(headerRow.get(2).getText()).toEqual('ECO Type');
    //     expect(headerRow.get(3).getText()).toEqual('Title');
    //     expect(headerRow.get(4).getText()).toEqual('Description');
    //     expect(headerRow.get(5).getText()).toEqual('Reason for Change');
    //     expect(headerRow.get(6).getText()).toEqual('Status');
    //     expect(headerRow.get(7).getText()).toEqual('Created Date');
    //     expect(headerRow.get(8).getText()).toEqual('Released/Rejected Date');
    //     expect(headerRow.get(9).getText()).toEqual('Workflow');
    //     expect(headerRow.get(10).getText()).toEqual('Change Analyst');
    //     expect(headerRow.get(11).getText()).toEqual('Actions');

    // });




