var urlsPage = require('../../../../pages/urlsPage');

describe('New PR Test Cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allProblemReports);
        browser.sleep(5000);
    });

    it('without select Reported By -> create New Problem Report', function () {
        var newProblemReportButton = element(by.id('newProblemReportButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newProblemReportButton), 15000);
        newProblemReportButton.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select reported by');
        browser.sleep(8000);
    });

    it('without Select Problem Report Type -> create New Problem Report', function () {
        var reportedBySelection = element(by.model('newProblemReportVm.newProblemReport.reportedBy'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(reportedBySelection), 20000);
        reportedBySelection.click();
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
        expect(alertMessage).toEqual('Please select problem report type');
        browser.sleep(8000);
    });

    it('without select Product -> create New Problem Report', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selectProblemReportType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProblemReportType), 15000);
        selectProblemReportType.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select product');
        browser.sleep(8000);
    });

    it('without  Problem Name -> create New Problem Report', function () {
        var selectProduct = element(by.model('newProblemReportVm.newProblemReport.product'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProduct), 20000);
        selectProduct.click();
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
        expect(alertMessage).toEqual('Please enter problem');
        browser.sleep(8000);
    });

    it('without  Description -> create New Problem Report', function () {

        var problemName = element(by.model('newProblemReportVm.newProblemReport.problem'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(problemName), 20000);
        problemName.sendKeys('problem');
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

    it('without select Quality Analyst -> create New Problem Report', function () {

        var problemDescription = element(by.model('newProblemReportVm.newProblemReport.description'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(problemDescription), 20000);
        problemDescription.sendKeys('Description');
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

    it('without select Workflow -> create New Problem Report', function () {

        var selectQualityAnalyst = element(by.model('newProblemReportVm.newProblemReport.qualityAnalyst'));
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

    it('without select Defect Type -> create New Problem Report', function () {

        var selectWorkflow = element(by.model('newProblemReportVm.newProblemReport.workflow'));
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

    it('without select Severity -> create New Problem Report', function () {

        var selectFailureType = element(by.model('newProblemReportVm.newProblemReport.failureType'));
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

    it('without select Disposition -> create New Problem Report', function () {

        var selectSeverity = element(by.model('newProblemReportVm.newProblemReport.severity'));
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


    it('New customer ProblemReport Creation Successfull Test case ', function () {
        browser.get(urlsPage.allProblemReports);
        browser.sleep(8000);
        var newButtonClick = element(by.id('newProblemReportButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Problem Report');

        var ReporterTypeSelect = element(by.css("[for='prTypeC']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ReporterTypeSelect), 20000);
        ReporterTypeSelect.click();
        browser.sleep(5000);
        var reportedBySelection = element(by.model('newProblemReportVm.newProblemReport.reportedBy'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(reportedBySelection), 20000);
        reportedBySelection.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selectProblemReportType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProblemReportType), 15000);
        selectProblemReportType.click();
        browser.sleep(2000);

        var selectProduct = element(by.model('newProblemReportVm.newProblemReport.product'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProduct), 20000);
        selectProduct.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        //    var selectInspection =  element(by.model('newProblemReportVm.newProblemReport.inspection'));
        //    var EC = protractor.ExpectedConditions;
        //    browser.wait(EC.presenceOf(selectInspection), 15000);
        //    selectInspection.click(); 
        //    element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.problem').sendKeys('customer problem 12')), 30000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.description').sendKeys('description')), 30000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.stepsToReproduce').sendKeys('stepsToReproduce')), 30000);

        browser.sleep(2000);

        var selectQualityAnalyst = element(by.model('newProblemReportVm.newProblemReport.qualityAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAnalyst), 15000);
        selectQualityAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(2000);

        var selectWorkflow = element(by.model('newProblemReportVm.newProblemReport.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkflow), 15000);
        selectWorkflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(2000);

        var selectFailureType = element(by.model('newProblemReportVm.newProblemReport.failureType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFailureType), 15000);
        selectFailureType.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(2000);

        var selectSeverity = element(by.model('newProblemReportVm.newProblemReport.severity'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSeverity), 15000);
        selectSeverity.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(2000);

        var selectDisposition = element(by.model('newProblemReportVm.newProblemReport.disposition'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDisposition), 15000);
        selectDisposition.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3500);

        browser.getCurrentUrl().then(function (url) {
            var problemReportCustomer_id = url.split("problemReport/")[1].replace("?tab=details.basic", "");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl + 'pqm/problemReport/' + problemReportCustomer_id + '?tab=details.basic');
        });
        browser.sleep(8000);
    });

    it('New Internal ProblemReport Creation Successfull Test case ', function () {
        browser.get(urlsPage.allProblemReports);
        browser.sleep(10000);
        var newButtonClick = element(by.id('newProblemReportButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Problem Report');

        var ReporterTypeSelect = element(by.css("[for='prTypeI']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ReporterTypeSelect), 20000);
        ReporterTypeSelect.click();
        browser.sleep(2000);
        var reportedBySelection = element(by.model('newProblemReportVm.newProblemReport.reportedBy'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(reportedBySelection), 20000);
        reportedBySelection.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(2000);

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        browser.sleep(2000);

        var selectProblemReportType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProblemReportType), 15000);
        selectProblemReportType.click();
        browser.sleep(2000);

        var selectProduct = element(by.model('newProblemReportVm.newProblemReport.product'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProduct), 20000);
        selectProduct.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        //    var selectInspection =  element(by.model('newProblemReportVm.newProblemReport.inspection'));
        //    var EC = protractor.ExpectedConditions;
        //    browser.wait(EC.presenceOf(selectInspection), 15000);
        //    selectInspection.click(); 
        //    element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.problem').sendKeys('Internal problem 12')), 30000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.description').sendKeys('description')), 30000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.stepsToReproduce').sendKeys('stepsToReproduce')), 30000);

        browser.sleep(1500);

        var selectQualityAnalyst = element(by.model('newProblemReportVm.newProblemReport.qualityAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAnalyst), 15000);
        selectQualityAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectWorkflow = element(by.model('newProblemReportVm.newProblemReport.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkflow), 15000);
        selectWorkflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectFailureType = element(by.model('newProblemReportVm.newProblemReport.failureType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFailureType), 15000);
        selectFailureType.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectSeverity = element(by.model('newProblemReportVm.newProblemReport.severity'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSeverity), 15000);
        selectSeverity.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectDisposition = element(by.model('newProblemReportVm.newProblemReport.disposition'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDisposition), 15000);
        selectDisposition.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function (url) {
            var problemReportInternal_id = url.split("problemReport/")[1].replace("?tab=details.basic", "");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl + 'pqm/problemReport/' + problemReportInternal_id + '?tab=details.basic');
        });
        browser.sleep(8000);
    });

    it('New Supplier ProblemReport Creation Successfull Test case ', function () {
        browser.get(urlsPage.allProblemReports);
        browser.sleep(10000);
        var newButtonClick = element(by.id('newProblemReportButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Problem Report');

        var ReporterTypeSelect = element(by.css("[for='prTypeS']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ReporterTypeSelect), 20000);
        ReporterTypeSelect.click();
        browser.sleep(2000);

        var reportedBySelection = element(by.model('newProblemReportVm.newProblemReport.reportedBy'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(reportedBySelection), 20000);
        reportedBySelection.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(2000);

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(2000);

        var selectProblemReportType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProblemReportType), 15000);
        selectProblemReportType.click();
        browser.sleep(2000);

        var selectProduct = element(by.model('newProblemReportVm.newProblemReport.product'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProduct), 20000);
        selectProduct.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        //    var selectInspection =  element(by.model('newProblemReportVm.newProblemReport.inspection'));
        //    var EC = protractor.ExpectedConditions;
        //    browser.wait(EC.presenceOf(selectInspection), 15000);
        //    selectInspection.click(); 
        //    element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.problem').sendKeys('Supplier problem 12')), 30000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.description').sendKeys('description')), 30000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newProblemReportVm.newProblemReport.stepsToReproduce').sendKeys('stepsToReproduce')), 30000);

        browser.sleep(1500);

        var selectQualityAnalyst = element(by.model('newProblemReportVm.newProblemReport.qualityAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectQualityAnalyst), 15000);
        selectQualityAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectWorkflow = element(by.model('newProblemReportVm.newProblemReport.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkflow), 15000);
        selectWorkflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectFailureType = element(by.model('newProblemReportVm.newProblemReport.failureType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectFailureType), 15000);
        selectFailureType.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectSeverity = element(by.model('newProblemReportVm.newProblemReport.severity'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSeverity), 15000);
        selectSeverity.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var selectDisposition = element(by.model('newProblemReportVm.newProblemReport.disposition'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDisposition), 15000);
        selectDisposition.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function (url) {
            var problemReportInternal_id = url.split("problemReport/")[1].replace("?tab=details.basic", "");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl + 'pqm/problemReport/' + problemReportInternal_id + '?tab=details.basic');
        });
        browser.sleep(8000);
    });
})