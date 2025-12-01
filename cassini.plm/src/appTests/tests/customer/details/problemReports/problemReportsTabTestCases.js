var urlsPage = require('../../../../pages/urlsPage.js');

describe('Customer Details -> Problem Reports Tab:', function () {

    it('Click on Problem Reports tab -> open the Problem Reports tab View successfully', function () {
        var problemReportsTab = element.all(by.id('problemReports')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(problemReportsTab), 15000);
        problemReportsTab.click();
        browser.sleep(1000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'customers/' + urlsPage.url_id + '?tab=details.problemReport');
        browser.sleep(5000);
    });
   

});
