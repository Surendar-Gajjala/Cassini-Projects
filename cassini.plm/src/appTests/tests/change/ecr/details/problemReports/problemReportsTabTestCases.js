var urlsPage = require('../../../../../pages/urlsPage.js');

describe('ECR Details -> Problem Reports tab:', function () {
    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });
    
    it('Click on Problem Reports tab -> open the  Problem Reports tab View successfully', function () {
        
        var filesTab = element(by.css('[heading="Problem Reports"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'changes/ecr/' + urlsPage.url_id + '?tab=details.problemReports');
        browser.sleep(5000);
    });
   

    it('Click Plus(+) button -> open Add Problem Reports Side panel Successfully', function () {
        var plusButton = element(by.css('[ng-click="ecrProblemReportsVm.addProblemReports()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        browser.sleep(1000);
        expect(plusButton.getAttribute('title')).toBe('Add Problem Reports');
        browser.sleep(1000);
        plusButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('Add Problem Reports');
        browser.sleep(3000);
    });

    it('Click Close(X) button -> close Add Problem Reports Side panel Successfully', function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });

    it('Click Add button -> Without select at least one Problem Reports Test case', function () {
        var plusButton = element(by.css('[ng-click="ecrProblemReportsVm.addProblemReports()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        plusButton.click();
        browser.sleep(3000);

        var addButton = element(by.buttonText('Add'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(addButton), 10000);
        addButton.click();
        browser.sleep(2500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Select at least one problem report');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);

    });

    it('Click Add button -> with select at least one Problem Report Test case', function () {
        var plusButton = element(by.css('[ng-click="ecrProblemReportsVm.addProblemReports()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        plusButton.click();
        browser.sleep(3000);

        var selectItemcenter = element.all(by.model('problemReport.selected')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectItemcenter), 15000);
        selectItemcenter.click();
        browser.sleep(3000);

        var addButton = element(by.buttonText('Add'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(addButton), 10000);
        addButton.click();

        browser.sleep(4500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Problem reports added successfully');
        browser.sleep(8000);
    });

    xit('Click Add button -> with select more then one Problem report Test case', function () {
        var plusButton = element(by.css('[ng-click="ecrProblemReportsVm.addProblemReports()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        plusButton.click();
        browser.sleep(3000);

        var selectItem1 = element.all(by.model('problemReport.selected')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectItem1), 15000);
        selectItem1.click();
        browser.sleep(3000);

        var selectItem2 = element.all(by.model('problemReport.selected')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectItem2), 15000);
        selectItem2.click();
        browser.sleep(3000);

        var addButton = element(by.buttonText('Add'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(addButton), 10000);
        addButton.click();
        browser.sleep(4500); 

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Problem reports added successfully');
        browser.sleep(8000);
    });


    it('Click Remove problem report button -> remove that Problem report from the table Successful', function () {
        var clickonActionButton = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonActionButton), 15000);
        clickonActionButton.click();
        browser.sleep(1500);
        var clickonRemoveProblemReportButton = element.all(by.css('[spellcheck="false"] > .dropdown-menu [ng-click="ecrProblemReportsVm.deleteProblemReport(problemReport)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonRemoveProblemReportButton), 15000);
        browser.sleep(2000);
        clickonRemoveProblemReportButton.click();
        browser.sleep(5000);
    
        var conformationPopUpWindow = element(by.css('.modal-header')).element(by.tagName('h4'));
        browser.sleep(1500);
        expect(conformationPopUpWindow.getText()).toBe('Remove Problem Report');
    
        var confirmOk = element(by.css("[ng-click='confirmVm.onOk()']"));
        browser.sleep(1500);
        expect(confirmOk.getText()).toBe('OK');
        confirmOk.click();
        browser.sleep(4500);
        var alertMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMes), 5000, "Text is not something I've expected");
        expect(alertMes).toEqual('Problem report removed successfully');
        browser.sleep(8000);
    });



});
