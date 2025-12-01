var urlsPage = require('../../../../../pages/urlsPage.js');
var affectedItemsDirective = require('../../../../../Directives/affectedItems/affectedItemsDirectiveTestCases.js');

describe('ECR Details -> Affected Items tab:', function () {
    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });

    it('Click on Affected Items tab -> open the  Affected Items tab View successfully', function () {
        browser.sleep(5000);
        var filesTab = element(by.id('affectedItems'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'changes/ecr/' + urlsPage.url_id + '?tab=details.affectedItems');
        browser.sleep(5000);
    });
   
    affectedItemsDirective.TestCases();
    
});
