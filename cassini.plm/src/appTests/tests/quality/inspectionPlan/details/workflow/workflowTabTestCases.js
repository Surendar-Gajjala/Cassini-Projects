var urlsPage = require('../../../../../pages/urlsPage.js');

describe('Inspection Plan Details -> Workflow tab:', function () {

    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });

    it('Click on Workflow tab -> open the  Workflow tab View successfully', function () {
        var filesTab = element(by.css('[heading="Workflow"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'pqm/inspectionPlan/' + urlsPage.url_id + '?tab=details.workflow');
        browser.sleep(5000);
    });
   

});
