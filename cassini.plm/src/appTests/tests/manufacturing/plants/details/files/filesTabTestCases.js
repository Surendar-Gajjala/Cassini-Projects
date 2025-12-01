var urlsPage = require('../../../../../pages/urlsPage.js');
var fileDirectiveTestCases = require('../../../../../Directives/files/fileDirectiveTestCases.js');

describe('Files:', function () {

    it('Click on Files tab -> open the files tab View successfully', function () {
        var filesTab = element(by.id('files'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'mes/plant/' + urlsPage.url_id + '?tab=details.files');
        browser.sleep(5000);
    });
    
    fileDirectiveTestCases.filesTab();

});
