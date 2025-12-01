var urlsPage = require('../../../../pages/urlsPage.js');

 //-------Check New Operation creation->without entering mandatory fields data----------
 describe('Check New Operation creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allOperations);
        browser.sleep(5000);  
    });

    it('Without Operations Type', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select type');
        browser.sleep(5000);
    });


    it('Without Name', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectType), 15000);
        selectType.click();
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });

});

//-------Check New Operation creation->with entering mandatory fields data ----------
describe('Check New Operation creation Successful by entering mandatory fields data ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allOperations);
        browser.sleep(5000); 
    });

    afterAll(function () {
        browser.get(urlsPage.allOperations);
        browser.sleep(5000);
    });


    it(' New Operation create Successfull Test Case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectType), 15000);
        selectType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newOperationVm.newOperation.name').sendKeys('Operations 061')), 35000);
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Operation created successfully');
        browser.sleep(8000);
    });

});

