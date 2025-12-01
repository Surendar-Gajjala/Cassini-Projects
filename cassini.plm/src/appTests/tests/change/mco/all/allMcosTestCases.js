var urlsPage = require('../../../../pages/urlsPage');


it('Check Mco tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var changesButton = element(by.id('changes'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(changesButton), 15000);
    changesButton.click();

    var mcoButton = element(by.id('mcos'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(mcoButton), 25000);
    mcoButton.click();
    browser.sleep(5000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allMcos);
    browser.sleep(8000);
});



// -----------------------Product Mco Testing ------------------
describe('All Mcos for Product Page Test cases', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMcos);
        browser.sleep(5000);
        var itemMcoType = element(by.id('itemMcoType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(itemMcoType), 15000);
        itemMcoType.click();
        browser.sleep(5000);
    });


    it('New Mco Button Text and Title Test case', function () {
        var newMcoButton = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newMcoButton), 10000);
        expect(newMcoButton.getText()).toEqual('New MCO');
        browser.sleep(2000);
        expect(newMcoButton.getAttribute('title')).toEqual('New MCO');
        browser.sleep(5000);
    });

    it('New Mco Button click -> open new Mco creation Side panel test case', function () {
        var newMcoButton = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newMcoButton), 10000);
        newMcoButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New MCO');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close new Mco creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newMcoButton').isDisplayed(true));
        browser.sleep(5000);
    });


    it('Attributes Button Title test case', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click -> open Attributes side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close Attributes Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });

});


// -----------------------Material Mco Testing ------------------
describe('All Mcos for Material Page Test Cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMcos);
        browser.sleep(5000);
        var materialType = element(by.id('manufacturerMcoType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(materialType), 15000);
        materialType.click();
        browser.sleep(5000);
    });


    it('New Mco Button Text and Title Test case', function () {
        var newMcoButton = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newMcoButton), 10000);
        expect(newMcoButton.getText()).toEqual('New MCO');
        browser.sleep(2000);
        expect(newMcoButton.getAttribute('title')).toEqual('New MCO');
        browser.sleep(5000);
    });

    it('New Mco Button click -> open new Mco creation Side panel test case', function () {
        var newMcoButton = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newMcoButton), 10000);
        newMcoButton.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('New MCO');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close new Mco creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(3000);
        
        expect($('#newMcoButton').isDisplayed(true));
        browser.sleep(5000);
    });


    it('Attributes Button Title test case', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click -> open Attributes side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close Attributes Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(3000);

        expect(closeRightSidePanel.isDisplayed(false));
        browser.sleep(5000);
    });

});


it('preferenPage Button Title', function () {
    var preferenPageButtonTitle = element(by.id('preferedPageButton'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
    expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
    browser.sleep(5000);
});

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferedPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });
