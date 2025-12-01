var urlsPage = require('../../../../pages/urlsPage');

describe('New NCR Sidepanel Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allNcrs);
        browser.sleep(5000);
    });

    it('without Select NCR Type -> create New NCR ', function () {
        var newNcrButton = element(by.id('newNcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newNcrButton), 15000);
        newNcrButton.click();
        browser.sleep(5000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select NCR type');
        browser.sleep(8000);
    });

    it('without Title -> create New NCR ', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteNCRType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteNCRType), 15000);
        selecteNCRType.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter title');
        browser.sleep(8000);
    });

    it('without Description -> create New NCR ', function () {

        var ncrTitle = element(by.model('newNcrVm.newNcr.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ncrTitle), 10000);
        ncrTitle.sendKeys('NCR 1');
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter description');
        browser.sleep(8000);
    });

    it('without select Quality Analyst -> create New NCR ', function () {

        var description = element(by.model('newNcrVm.newNcr.description'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(description), 10000);
        description.sendKeys('description');
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select quality analyst');
        browser.sleep(8000);
    });

   it('without select Workflow -> create New NCR ', function () {

        var selectQualityAnalyst = element(by.model('newNcrVm.newNcr.qualityAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAnalyst), 15000);
        selectQualityAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select workflow');
        browser.sleep(8000);
    });

    it('without select Defect Type -> create New NCR ', function () {

        var selectWorkflow = element(by.model('newNcrVm.newNcr.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkflow), 15000);
        selectWorkflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select defect type');
        browser.sleep(8000);
    });

    it('without select Severity -> create New NCR ', function () {

        var selectFailureType = element(by.model('newNcrVm.newNcr.failureType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFailureType), 15000);
        selectFailureType.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select severity');
        browser.sleep(8000);
    });

    it('without select Disposition -> create New NCR ', function () {

        var selectSeverity = element(by.model('newNcrVm.newNcr.severity'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSeverity), 15000);
        selectSeverity.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select disposition');
        browser.sleep(8000);

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
    });


    it('New NCR Creation Testing ', function () {
        var newButtonClick = element(by.id('newNcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New NCR');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteNCRType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteNCRType), 15000);
        selecteNCRType.click();

        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newNcrVm.newNcr.title').sendKeys('NCR 4')), 30000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newNcrVm.newNcr.description').sendKeys('description')), 30000);
        browser.sleep(2000);

        var selectQualityAnalyst = element(by.model('newNcrVm.newNcr.qualityAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAnalyst), 15000);
        selectQualityAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var selectWorkflow = element(by.model('newNcrVm.newNcr.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkflow), 15000);
        selectWorkflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var selectFailureType = element(by.model('newNcrVm.newNcr.failureType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFailureType), 15000);
        selectFailureType.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var selectSeverity = element(by.model('newNcrVm.newNcr.severity'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSeverity), 15000);
        selectSeverity.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var selectDisposition = element(by.model('newNcrVm.newNcr.disposition'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDisposition), 15000);
        selectDisposition.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(8000);

        browser.getCurrentUrl().then(function (url) {
            var ncr_id = url.split("pqm/ncr/")[1].replace("?tab=details.basic","");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl+'pqm/ncr/'+ncr_id +'?tab=details.basic');
        });

        browser.sleep(8000);

    });


});