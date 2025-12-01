
var urlsPage = require('../../../../../pages/urlsPage.js');

describe('Inspection Plan Details -> Timeline:', function () {

    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });

    it('Click on Timeline tab -> open the Timeline tab View successfully', function () {
        var timelineTab = element(by.css('[heading="Timeline"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(timelineTab), 15000);
        timelineTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'pqm/inspectionPlan/' + urlsPage.url_id + '?tab=details.timelineHistory');
        browser.sleep(8000);
    });

});