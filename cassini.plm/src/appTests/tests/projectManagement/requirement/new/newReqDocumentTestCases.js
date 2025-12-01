var urlsPage = require('../../../../pages/urlsPage');

describe('New Requirement Document Test cases:', function () {


    beforeAll(function () {
        browser.get(urlsPage.allReqDocuments);
        browser.sleep(5000);
    });

    it('without select Type -> create New Requirement Document ', function () {
        var newButtonClick = element(by.id('newReqDocument'));
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
        browser.sleep(8000);
    });


    it('without Name  -> create New Requirement c ', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteDocumentsType = element(by.id('projectManagementTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteDocumentsType), 15000);
        selecteDocumentsType.click();
        browser.sleep(4000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Name cannot be empty');
        browser.sleep(8000);
    });

    it('without Name  -> create New Requirement Document ', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteDocumentsType = element(by.id('projectManagementTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteDocumentsType), 15000);
        selecteDocumentsType.click();
        browser.sleep(4000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Name cannot be empty');
        browser.sleep(8000);

        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);
    });

    it('New Documents Creation Testing', function () {

        var newButtonClick = element(by.id('newReqDocument'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Requirement Document');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteDocumentsType = element(by.id('projectManagementTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteDocumentsType), 15000);
        selecteDocumentsType.click();
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newReqDocumentVm.newReqDocument.name').sendKeys('Requirement Document 1')), 30000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newReqDocumentVm.newReqDocument.description').sendKeys('description')), 30000);
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Requirement document created successfully');
        browser.sleep(10000);
    });
});