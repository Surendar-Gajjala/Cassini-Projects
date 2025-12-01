var urlsPage = require('../../../../pages/urlsPage');

//---------Check ProblemReport tab in the side navigation-------

    it('Check ProblemReport tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(2000);
        var manufacturingButton = element(by.id('quality'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(manufacturingButton), 15000);
        manufacturingButton.click();
        browser.sleep(2000);
        var plantButton = element(by.id('problemReports'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantButton), 25000);
        plantButton.click();
        browser.sleep(3000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allProblemReports)
        browser.sleep(8000);
    });



describe('All Problem Reports Page Test cases', function () {
    
    beforeAll(function () {
        browser.get(urlsPage.allProblemReports);
        browser.sleep(5000);
    });

    it('New PR Button Text and Title test cases', function () {
        var newProblemReportButton = element(by.id('newProblemReportButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newProblemReportButton), 15000);
        expect(newProblemReportButton.getText()).toEqual('New PR');
        browser.sleep(2000);
        expect(newProblemReportButton.getAttribute('title')).toEqual('New PR');
        browser.sleep(8000);
    });

    it('New PR Button click -> open New Problem Report creation Side panel test case', function () {
        var newProblemReportButton = element(by.id('newProblemReportButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newProblemReportButton), 10000);
        newProblemReportButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Problem Report');
        browser.sleep(5000);
    });


    it('Click Close(X) button -> close New Problem Report creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newProblemReportButton').isDisplayed(true));
        browser.sleep(5000);
    });
    
    it('Attributes Button Title', function () {
        browser.get(urlsPage.allProblemReports);
        browser.sleep(3000);
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });
    
    it('Attributes Button click -> open Attributes side panel Test case', function () {
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

    it('preferenPage Button Title', function () {
        var preferenPageButtonTitle = element(by.id('preferredPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
    });
   
    
});



