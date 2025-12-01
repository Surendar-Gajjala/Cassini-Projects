var urlsPage = require('../../../../pages/urlsPage');

describe('New Qcr Side panel Test cases:', function () {
    beforeAll(function () {
        browser.get(urlsPage.allQcrs);
        browser.sleep(5000);
    });

    it('without select QCR Type -> create New QCR', function () {
        var newQcrButton = element(by.id('newQcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newQcrButton), 15000);
        newQcrButton.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select QCR type');
        browser.sleep(8000);
    });

    it('without select QCR For -> create New QCR', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteQCRType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteQCRType), 15000);
        selecteQCRType.click();
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select QCR for');
        browser.sleep(8000);
    });

    it('without Title -> create New QCR', function () {
        var selectQCRFor = element(by.model('newQcrVm.newQcr.qcrFor'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQCRFor), 15000);
        selectQCRFor.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(3000);

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

    it('without Description  -> create New QCR', function () {

        var title = element(by.model('newQcrVm.newQcr.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(title), 15000);
        title.sendKeys('QCR 1');
        browser.sleep(3000);

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

    it('without Select Quality Administrator -> create New QCR', function () {

        var description = element(by.model('newQcrVm.newQcr.description'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(description), 15000);
        description.sendKeys('description');
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select quality administrator');
        browser.sleep(8000);
    });

    it('without Select Workflow  -> create New QCR', function () {
        var selectQualityAdministrator = element(by.model('newQcrVm.newQcr.qualityAdministrator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAdministrator), 15000);
        selectQualityAdministrator.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(3000);

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

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
    });

    it('New QCR Creation successful Test case here " Qcr For = NCR" ', function () {
        var newButtonClick = element(by.id('newQcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New QCR');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteQCRType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteQCRType), 15000);
        selecteQCRType.click();

        var selectQCRFor = element(by.model('newQcrVm.newQcr.qcrFor'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQCRFor), 15000);
        selectQCRFor.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.wait(EC.presenceOf(urlsPage.byModel('newQcrVm.newQcr.title').sendKeys('QCR 3')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newQcrVm.newQcr.description').sendKeys('description')), 30000);

        var selectQualityAdministrator = element(by.model('newQcrVm.newQcr.qualityAdministrator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAdministrator), 15000);
        selectQualityAdministrator.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var selectWorkflow = element(by.model('newQcrVm.newQcr.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkflow), 15000);
        selectWorkflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(5000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(4000);

        browser.getCurrentUrl().then(function (url) {
            var qcr_id = url.split("pqm/qcr/")[1].replace("?tab=details.basic","");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl+'pqm/qcr/'+qcr_id+'?tab=details.basic');
        });
        browser.sleep(10000);
    });

    it('New QCR Creation successfull Test case here Qcr For = PR ', function () {
        browser.get(urlsPage.allQcrs);
        browser.sleep(8000);
        var newButtonClick = element(by.id('newQcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New QCR');
        browser.sleep(2000);

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteQCRType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteQCRType), 15000);
        selecteQCRType.click();
        browser.sleep(2000);

        var selectQCRFor = element(by.model('newQcrVm.newQcr.qcrFor'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQCRFor), 15000);
        selectQCRFor.click();
        element.all(by.xpath("//div[.='NCR']")).click();
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newQcrVm.newQcr.title').sendKeys('QCR 4')), 30000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newQcrVm.newQcr.description').sendKeys('description')), 30000);
        browser.sleep(2000);

        var selectQualityAdministrator = element(by.model('newQcrVm.newQcr.qualityAdministrator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAdministrator), 15000);
        selectQualityAdministrator.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var selectWorkflow = element(by.model('newQcrVm.newQcr.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkflow), 15000);
        selectWorkflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function (url) {
            var qcr_id = url.split("pqm/qcr/")[1].replace("?tab=details.basic","");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl+'pqm/qcr/'+qcr_id+'?tab=details.basic');
        });
        browser.sleep(8000);
    });
})