var urlsPage = require('../../../../pages/urlsPage.js');
 
 //-------Check New Machine creation->without entering mandatory fields data----------
 describe('Check New Machine creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMachines);
        browser.sleep(5000); 
    });


    it('With Out Machine Type', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(1000); 

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

    it('With Out select Work Center', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var MachineType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(MachineType), 15000);
        MachineType.click();
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select work center');
        browser.sleep(5000);
    });

    it('With Out Name', function () {

        var selectWorkCenter = element(by.model('newMachineVm.newMachine.workCenter'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkCenter), 15000);
        selectWorkCenter.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

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

    it('With Out Asset Type ', function () {
     
        element(by.model('newMachineVm.newMachine.name')).sendKeys('Machine 27');
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select asset type');
        browser.sleep(5000);
    });

    it('With Out Asset Name ', function () {

        var selectbuttonClick = element(by.id('selectAssetType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectAssertType = element(by.id('mroObjectTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAssertType), 15000);
        selectAssertType.click();
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter asset name');
        browser.sleep(8000);
    });

    it('With Out Select Meter ', function () {

        var selectbuttonClick = element(by.id('selectAssetType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectAssertType = element(by.id('mroObjectTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAssertType), 15000);
        selectAssertType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 2021')), 35000);

        var clickMeteredToggle = element(by.css("[for='isMetered']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickMeteredToggle), 15000);
        clickMeteredToggle.click();
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select atleast one meter');
        browser.sleep(8000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

    });

});

//-------Check New Machine creation->with entering mandatory fields data----------
describe('Check New Machine creation->by entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMachines);
        browser.sleep(5000); 
    });

    afterAll(function () {
        browser.get(urlsPage.allMachines);
        browser.sleep(5000);
    });

    it(' New Machine create Successfull Test Case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(1000);

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var MachineType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(MachineType), 15000);
        MachineType.click();
        browser.sleep(1000);

        var selectWorkCenter = element(by.model('newMachineVm.newMachine.workCenter'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkCenter), 15000);
        selectWorkCenter.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newMachineVm.newMachine.name').sendKeys('Machine 270207')), 35000);

        var selectbuttonClick = element(by.id('selectAssetType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(1000);
        var selectAssertType = element(by.id('mroObjectTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectAssertType), 15000);
        selectAssertType.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('asset.name').sendKeys('Assert 2021001')), 35000);

        var clickMeteredToggle = element(by.css("[for='isMetered']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickMeteredToggle), 15000);
        clickMeteredToggle.click();
        browser.sleep(1000);

        var selectMeter = element(by.model('asset.meters'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMeter), 15000);
        selectMeter.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(4500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Machine created successfully');
        browser.sleep(8000);
    });

});
