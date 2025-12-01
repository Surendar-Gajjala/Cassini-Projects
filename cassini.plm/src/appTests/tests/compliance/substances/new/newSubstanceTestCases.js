var urlsPage = require('../../../../pages/urlsPage');

describe('New Substance Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allSubstances);
        browser.sleep(6000);
    });

    it('Without select Substance Type -> create New Substance', function () {
        var newButtonClick = element(by.id('newSubstance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
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
        expect(alertMessage).toEqual('Please select type');
        browser.sleep(5000);
    });

    it('Without Name -> create New Substance', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteSubstanceType = element(by.id('complianceTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteSubstanceType), 15000);
        selecteSubstanceType.click();
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Name cannot be empty');
        browser.sleep(5000);
    });

    it('Without CAS Number -> create New Substance', function () {

        var substanceName = element(by.model('newSubstanceVm.newSubstance.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(substanceName), 10000);
        substanceName.sendKeys('Substance 25');
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(2000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter cas number');
        browser.sleep(8000);

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(3000);
    });

    it('New Substance  Created Successful Test case', function () {
        var newButtonClick = element(by.id('newSubstance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Substance');


        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteSubstanceType = element(by.id('complianceTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteSubstanceType), 15000);
        selecteSubstanceType.click();
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSubstanceVm.newSubstance.name').sendKeys('Substance 1')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSubstanceVm.newSubstance.casNumber').sendKeys('123456')), 30000);
        browser.sleep(3000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newSubstanceVm.newSubstance.description').sendKeys('description')), 30000);
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Substance created successfully');
        browser.sleep(8000);


    });

});