var urlsPage = require('../../../../pages/urlsPage');

it('Check Requirement tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var projectManagement = element(by.id('projectManagement'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(projectManagement), 15000);
    projectManagement.click();

    var requirements = element(by.id('requirements'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(requirements), 25000);
    requirements.click();
    browser.sleep(5000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allReqDocuments);
    browser.sleep(8000);

});


describe('All Requirement Documents Page Test cases ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allReqDocuments);
        browser.sleep(5000);
    });

    it('New Document Button Text and Title Test case', function () {
        var newReqDocument = element(by.id('newReqDocument'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newReqDocument), 15000);
        expect(newReqDocument.getText()).toEqual('New Document');
        browser.sleep(2000);
        expect(newReqDocument.getAttribute('title')).toEqual('New Document');
        browser.sleep(5000);
    });


    it('new Document button click -> open New Requirement Document sidepanel Test case', function () {
        var newReqDocument = element(by.id('newReqDocument'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newReqDocument), 5000);
        newReqDocument.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Requirement Document');
        browser.sleep(8000);
    });

    it('close (x) button click -> close new Requirement Document sidepanel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('#newReqDocument').isDisplayed()).toBe(true);
        browser.sleep(5000);
    });

    it('Attributes Button Title', function () {
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

    it('close (x) button click -> close Attributes sidepanel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('#closeRightSidePanel').isDisplayed()).toBe(false);
        browser.sleep(5000);
    });

    it('preferenPage Button Title', function () {
        var preferenPageButtonTitle = element(by.id('preferredPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
        browser.sleep(5000);
    });

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferredPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });

});

