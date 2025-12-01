//var newPartRequestPage = require('../../../../pages/newPartRequestPage');
var urlsPage = require('../../../../pages/urlsPage');

describe('New Newpartrequest Test Cases', function () {
    beforeAll(function(){
        browser.get(urlsPage.allNewPartRequestsItems);
        browser.sleep(10000);
    });

    it('with out Description -> create new NewPartRequest', function () {
        var newButtonClick = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('  Please enter description');
        browser.sleep(8000);

    });

    it('with out Reason for Request -> create new NewPartRequest', function () {
        var Description = element(by.model('newNprVm.newNpr.description'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(Description), 15000);
        Description.sendKeys('Description is the pattern of narrative development that aims to make vivid a place, object, character, or group');
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual(' Please enter reason for request');
        browser.sleep(8000);

    });
    it('creation of item with mandatory fields',function(){
        browser.get(urlsPage.allNewPartRequestsItems);
        browser.sleep(8000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var Description =  element(by.model('newNprVm.newNpr.description'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(Description), 15000);
        Description.sendKeys('Description is the pattern of narrative development that aims to make vivid a place, object, character, or group');

        var reasonForRequest =  element(by.model('newNprVm.newNpr.reasonForRequest'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(reasonForRequest), 15000);
        reasonForRequest.sendKeys('not required');

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();


        browser.sleep(8000);

        browser.getCurrentUrl().then(function (url) {
            var partRequest_id = url.split("nprs/")[1].replace("?tab=details.basic","");
            expect(browser.getCurrentUrl())
                .toEqual('http://localhost:8085/#/app/' + 'nprs/'+partRequest_id+'?tab=details.basic');
        });
        browser.sleep(10000);

    });
});