var urlsPage = require('../../../../../pages/urlsPage.js');

describe('Resources Tab:', function () {

    it('Click on Resources tab -> open the Resources tab View successfully', function () {
        var filesTab = element(by.css('[heading="Related Items"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'mes/machine/' + urlsPage.url_id + '?tab=details.relatedItem');
        browser.sleep(5000);
    });

  

});
