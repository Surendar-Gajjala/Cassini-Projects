var urlsPage = require('../../../../pages/urlsPage.js');

//---------Check Project tab in the side navigation-------
it('Check Plants tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var projectManagementButton = element(by.id('projectManagement'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(projectManagementButton), 15000);
    projectManagementButton.click();

    var projectButton = element(by.id('projects'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(projectButton), 25000);
    projectButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allProjects);
    browser.sleep(8000);
});

describe('All Projects Page Test Cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allProjects);
        browser.sleep(5000);
    })

    it('Check New Project button Text and Title Test case ', function () {
        var newProjectButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newProjectButton), 15000);
        expect(newProjectButton.getText()).toEqual('New Project');
        browser.sleep(2000);
        expect(newProjectButton.getAttribute('title')).toEqual('New Project');
        browser.sleep(5000);
    });

    it('Check New Project button click -> open New Project side panel', function () {
        var newProjectButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newProjectButton), 15000);
        newProjectButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Project');
        browser.sleep(5000);
    });


    it('close (x) button click -> close new  Template sidepanel test case', function () {
        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

        expect($('.new-button').isDisplayed()).toBe(true);
        browser.sleep(5000);
    });


});

