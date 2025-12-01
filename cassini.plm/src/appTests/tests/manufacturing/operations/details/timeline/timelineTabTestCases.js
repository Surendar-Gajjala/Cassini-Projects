var urlsPage = require('../../../../../pages/urlsPage.js');

describe('Timeline:',function(){
    it('Click on Timeline tab -> open the Timeline tab View successfully', function () {
        var timelineTab = element(by.css('[heading="Timeline"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(timelineTab), 15000);
        timelineTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'mes/operation/' + urlsPage.url_id + '?tab=details.timelineHistory');
    });

});