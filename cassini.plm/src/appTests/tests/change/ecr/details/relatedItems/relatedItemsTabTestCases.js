var urlsPage = require('../../../../../pages/urlsPage.js');
var relatedItemsDirective = require('../../../../../Directives/relatedItems/relatedItemsDirectiveTestCases.js');

describe('ECR Details -> Related Items tab:', function () {
    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });
    
    it('Click on Related Items tab -> open the  Related Items tab View successfully', function () {
        
        var filesTab = element(by.id('relatedItems'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'changes/ecr/' + urlsPage.url_id + '?tab=details.relatedItems');
        browser.sleep(5000);
    });
   
    relatedItemsDirective.TestCases();

});
