var urlsPage = require('../../../pages/urlsPage.js');

describe('Dashboards -> Items',function(){


      //---------Check Dashboards Items tab in the side navigation -------
      it('Check Dashboards Items tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var dashboardButton = element(by.id('dashboards'));
        browser.executeScript("arguments[0].scrollIntoView();", dashboardButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(dashboardButton), 15000);
        dashboardButton.click();
        browser.sleep(5000);

        var sidePanelButton = element(by.id('dashboardItems'));
        browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelButton), 25000);
        sidePanelButton.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allDashboardItems);
        browser.sleep(8000);
    });

     //---------Page Heading test case-------
     it('Page Heading test case', function () {
        browser.get(urlsPage.allDashboardItems);
        browser.sleep(5000);
        var pageHeading = element(by.css('.view-toolbar')).element(by.tagName('span'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(pageHeading), 15000);
        browser.sleep(2000);    
        expect(pageHeading.getText()).toBe('Items Dashboard');
        browser.sleep(6000);
    });

});