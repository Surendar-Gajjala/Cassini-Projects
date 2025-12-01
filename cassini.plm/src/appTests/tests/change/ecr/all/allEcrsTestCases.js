var urlsPage = require('../../../../pages/urlsPage');


     it('Check Ecr tab present in the side navigation Panel', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(2000);
        var chnagesButton = element(by.id('changes'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(chnagesButton), 15000);
        chnagesButton.click();
    
        var ecrButton = element(by.id('ecrs'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ecrButton), 25000);
        ecrButton.click();
        browser.sleep(1000);
        expect(browser.getCurrentUrl()).toEqual(urlsPage.allEcrs);
        browser.sleep(8000);
    });


describe('all Ecrs page test cases:', function () {

    beforeAll(function(){
        browser.get(urlsPage.allEcrs);
        browser.sleep(5000);
    });


    it('New Button text and Title test case', function () {
        var button = element(by.css('.view-toolbar')).element(by.id('newEcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 5000);
        expect(button.getText()).toEqual('New ECR');
        browser.sleep(2000);
        expect(button.getAttribute('title')).toEqual('New ECR');
        browser.sleep(8000);
    });

    it('new ECR button click open new ECR side panel', function () {
        var newButtonClick = element(by.id('newEcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 5000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New ECR');
        browser.sleep(8000);
    });

    it('close (x) button click -> close new ECR side panel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('#newEcrButton').isDisplayed()).toBe(true);
        browser.sleep(5000);
    });

    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click -> open Attributes sidepanel', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
        browser.sleep(8000);
    });

    it('close (x) button click -> close new Attributes sidepanel test case',function() {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(1500);
        expect($('#closeRightSidePanel').isDisplayed()).toBe(false);
        browser.sleep(5000);
    })

    it('preferenPage Button Title', function () {
        var preferenPageButtonTitle = element(by.id('preferedPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
    });

// it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferedPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });
});
