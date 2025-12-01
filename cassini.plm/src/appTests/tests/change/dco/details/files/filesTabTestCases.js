var urlsPage = require('../../../../../pages/urlsPage.js');
var changeFileDirectiveTestCases = require('../../../../../Directives/files/changeFilesDirectiveTestCases.js');

describe('DCO Details ->  Files Tab:', function () {

    it('Click on Files tab -> open the files tab View successfully', function () {
        var filesTab = element(by.id('files'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'changes/dco/' + urlsPage.url_id + '?tab=details.files');
        browser.sleep(5000);
    });
   
    changeFileDirectiveTestCases.filesTab();

});
