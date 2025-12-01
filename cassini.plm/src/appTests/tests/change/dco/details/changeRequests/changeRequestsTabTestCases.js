var urlsPage = require('../../../../../pages/urlsPage.js');
var changeRequestsDirectiveTestCases = require('../../../../../Directives/changeRequests/changeRequestViewDirectiveTestCases.js');

describe('DCO Details -> Change Requests tab:', function () {
    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });
    
    it('Click on Change Requests tab -> open the  Change Requests tab View successfully', function () {
        
        var filesTab = element(by.id('changeRequests'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'changes/dco/' + urlsPage.url_id + '?tab=details.changeRequest');
        browser.sleep(5000);
    });
   
    changeRequestsDirectiveTestCases.changeRequests();


});
