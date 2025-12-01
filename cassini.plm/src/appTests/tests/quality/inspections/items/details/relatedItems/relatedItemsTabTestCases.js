var urlsPage = require('../../../../../../pages/urlsPage.js');
var relatedItemsDirective = require('../../../../../../Directives/relatedItems/relatedItemsDirectiveTestCases.js');

describe('Item Inspection Details -> Related Items Tab',function(){

    it('Click on Related Items tab -> open the Related Items tab View successfully', function () {
        var relatedItemsTab = element(by.id('relatedItems'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(relatedItemsTab), 15000);
        relatedItemsTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'pqm/inspections/' + urlsPage.url_id + '?tab=details.relatedItem');
        browser.sleep(5000);
    });

    relatedItemsDirective.TestCases();

});