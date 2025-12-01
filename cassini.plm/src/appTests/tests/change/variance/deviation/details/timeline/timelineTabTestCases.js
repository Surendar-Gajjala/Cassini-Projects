
var urlsPage = require('../../../../../../pages/urlsPage.js');

describe('Deviation Details ->  Timeline:', function () {

    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });

    it('Click on Timeline tab -> open the Timeline tab View successfully', function () {
        var timelineTab = element(by.css('[heading="Timeline"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(timelineTab), 15000);
        timelineTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'changes/variance/' + urlsPage.url_id + '?tab=details.timeLine');
        browser.sleep(8000);
    });

});