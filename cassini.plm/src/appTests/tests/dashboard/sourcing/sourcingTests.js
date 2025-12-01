var urlsPage = require('../../../pages/urlsPage.js');

describe('Dashboards -> Sourcing', function () {

  // beforeAll(function(){
  //     browser.get(urlsPage.homeUrl);
  //     browser.sleep(5000);
  //     browser.refresh();
  //     browser.sleep(2000);
  // });

  //---------Check Dashboards Sourcing tab in the side navigation -------
  it('Check Dashboards Sourcing tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(3000);
    var dashboardButton = element(by.id('dashboards'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(dashboardButton), 15000);
    dashboardButton.click();
    browser.sleep(3000);
    var sidePanelButton = element(by.id('dashboardSourcing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sidePanelButton), 25000);
    sidePanelButton.click();
    browser.sleep(5000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allDashboardSourcings);
    browser.sleep(9500);
  });

  //---------Page Heading test case-------
  it('Page Heading test case', function () {
    browser.get(urlsPage.allDashboardSourcings);
    browser.sleep(5000);
    var pageHeading = element(by.css('.view-toolbar')).element(by.tagName('span'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(pageHeading), 15000);
    browser.sleep(2000);
    expect(pageHeading.getText()).toBe('Sourcing Dashboard');
    browser.sleep(6000);
  });

});