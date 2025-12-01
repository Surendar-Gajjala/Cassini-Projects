
var urlsPage = require('../../../../pages/urlsPage.js');

describe('Customer Details -> Timeline Tab:', function () {

    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });

    it('Click on Timeline tab -> open the Timeline tab View successfully', function () {
        var timelineTab = element(by.css('[heading="Timeline"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(timelineTab), 15000);
        timelineTab.click();
        browser.sleep(1000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'customers/' + urlsPage.url_id + '?tab=details.timelineHistory');
        browser.sleep(8000);
    });

});