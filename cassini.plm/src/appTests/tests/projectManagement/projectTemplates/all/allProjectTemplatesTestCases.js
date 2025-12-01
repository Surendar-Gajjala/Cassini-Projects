var urlsPage = require('../../../../pages/urlsPage');

it('Check Project Templates tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var projectManagement = element(by.id('projectManagement'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(projectManagement), 15000);
    projectManagement.click();
    browser.sleep(2000);

    var projectTemplates = element(by.id('projectTemplates'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(projectTemplates), 25000);
    projectTemplates.click();
    browser.sleep(5000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allProjectTemplates);
    browser.sleep(8000);

});

describe('all Project Templates  page Test cases ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allProjectTemplates);
        browser.sleep(5000);
    })

    it('New Template Button text and Title Test cases', function () {
        var newTemplate = element(by.id('newTemplate'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newTemplate), 15000);
        expect(newTemplate.getText()).toEqual('New Template');
        browser.sleep(2000);
        expect(newTemplate.getAttribute('title')).toEqual('New Template');
        browser.sleep(5000);
    });
    
    it('new Template button click -> open New Template  sidepanel Test case', function () {
        var newTemplate = element(by.id('newTemplate'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newTemplate), 5000);
        newTemplate.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Template');
        browser.sleep(8000);
    });

    it('close (x) button click -> close new  Template sidepanel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('#newTemplate').isDisplayed()).toBe(true);
        browser.sleep(5000);
    });



});

