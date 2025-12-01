var urlsPage = require('../../../../pages/urlsPage.js');

 //-------Check New Shift creation->without entering mandatory fields data----------
 describe('Check New Shift creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allShifts);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
    });

    it('Without Name', function () {
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

    });

    it('Without Start Time', function () {

        var name = element(by.model('newShiftVm.newShift.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 15000);
        name.sendKeys('shift 11');
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter start time');
        browser.sleep(5000);

    });

    it('Without End Time', function () {

        browser.executeScript("document.getElementsById('startTime').value='18:30'");
        browser.sleep(1500);

        // var startTime = element(by.model('newShiftVm.newShift.localStartTime'));
        // var EC = protractor.ExpectedConditions;
        // browser.wait(EC.presenceOf(startTime), 15000);
        // startTime.click();
        // startTime.sendKeys('18:30');
        // browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter end time');
        browser.sleep(8000);

    });


});

//-------Check New Shift creation->with entering mandatory fields data ----------
describe('Check New Shift creation->by entering mandatory fields data ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allShifts);
        browser.sleep(5000);
    });

    afterAll(function () {
        browser.get(urlsPage.allShifts);
        browser.sleep(5000);
    });

    it(' New Shift create Successfull Test Case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var name = element(by.model('newShiftVm.newShift.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 15000);
        name.sendKeys('shift 11');
        browser.sleep(1000);

        // browser.executeScript("document.getElementsById('startTime').value='18:30'");
        // browser.sleep(1500);

        // var startTimec = element(by.id('startTime'));
        // var EC = protractor.ExpectedConditions;
        // browser.wait(EC.presenceOf(startTime), 15000);
        
        var startTime = element(by.model('newShiftVm.newShift.localStartTime'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(startTime), 15000);
        startTime.click();
        startTime.sendKeys('18:30');
        browser.sleep(1500);

        var endTime = element(by.model("newShiftVm.newShift.localEndTime"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(endTime), 15000);
        endTime.click();
        endTime.sendKeys('20:30');
        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Shift created successfully');
        browser.sleep(8000);
    });

});