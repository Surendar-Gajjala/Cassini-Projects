var urlsPage = require('../../../../../../pages/urlsPage.js');

describe('Material Inspection Details -> Check list Tab:', function () {

    it('Click on Check list tab -> open the Check list tab View successfully', function () {
        var checklist = element(by.id('checklist'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(checklist), 15000);
        checklist.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'pqm/inspections/' + urlsPage.url_id + '?tab=details.checklist');
        browser.sleep(5000);
    });

    it("click On caret(expand all)Button => expand All check lists  fields ", function () {
        var clickExpandAll = element(by.css('[ng-click="inspectionChecklistVm.expandAllSections()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickExpandAll), 15000);
        clickExpandAll.click();
        browser.sleep(2500);

        var ActionsColumnButtons = element.all(by.css('.fa-ellipsis-h')).first();
        browser.sleep(2000);

        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ActionsColumnButtons), 15000);
        expect(ActionsColumnButtons.isDisplayed()).toBe(true);
        browser.sleep(5000);
    });

    it("click on Edit button -> Edit fields display successfully ", function () {
        var ActionsColumnButtons = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ActionsColumnButtons), 15000);
        ActionsColumnButtons.click();
        browser.sleep(2500);

        var EditButtonClick = element(by.css('[spellcheck="false"] > .dropdown-menu [ng-click="inspectionChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(EditButtonClick), 15000);
        EditButtonClick.click();
        browser.sleep(2500);


        var assignedToField = element(by.model('checklist.assignedTo'));
        var resultField = element(by.model('checklist.result'));
        var notesField = element(by.model('checklist.notes'));

        var saveButton = element(by.css('[ng-click="inspectionChecklistVm.updateChecklist(checklist)"]'));
        var cancelButton = element(by.css('[ng-click="inspectionChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(assignedToField, resultField, notesField, saveButton, cancelButton), 15000);
        expect(assignedToField.isDisplayed(true)).toBe(true);
        expect(resultField.isDisplayed(true)).toBe(true);
        expect(notesField.isDisplayed(true)).toBe(true);
        expect(saveButton.isDisplayed(true)).toBe(true);
        expect(cancelButton.isDisplayed(true)).toBe(true);
        browser.sleep(5000);
        cancelButton.click();
        browser.sleep(3000)
    });

    it("update Checklist without AssignTo field ", function () {
        var ActionsColumnButtons = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ActionsColumnButtons), 15000);
        ActionsColumnButtons.click();
        browser.sleep(2500);

        var EditButtonClick = element(by.css('[spellcheck="false"] > .dropdown-menu [ng-click="inspectionChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(EditButtonClick), 15000);
        EditButtonClick.click();
        browser.sleep(2500);

        var saveButton = element(by.css('[ng-click="inspectionChecklistVm.updateChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Assigned To cannot be empty');
        browser.sleep(8000);
        var cancelButton = element(by.css('[ng-click="inspectionChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelButton), 15000);
        cancelButton.click();
        browser.sleep(4500);
    });

    it("click Update button -> update Checklist successful test case ", function () {
        var ActionsColumnButtons = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ActionsColumnButtons), 15000);
        ActionsColumnButtons.click();
        browser.sleep(2500);

        var EditButtonClick = element(by.css('[spellcheck="false"] > .dropdown-menu [ng-click="inspectionChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(EditButtonClick), 15000);
        EditButtonClick.click();
        browser.sleep(2500);

        var assignedTo = element(by.model('checklist.assignedTo'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(assignedTo), 15000);
        assignedTo.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2500);

        var result = element(by.model('checklist.result'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(result), 15000);
        result.click();
        element.all(by.css('.ui-select-choices-row-inner div')).get(1).click();
        browser.sleep(2500);

        var notes = element(by.model('checklist.notes'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(notes), 15000);
        notes.sendKeys('Notes');
        browser.sleep(2500);

        var saveButton = element(by.css('[ng-click="inspectionChecklistVm.updateChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4500);
    });

    it("click Cancel button -> cancel the update Checklist data successful test case ", function () {   

        var beforeAssignToField = element.all(by.id('checklistAssignTo')).get(0);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeAssignToField), 15000);


        var beforeResultField = element.all(by.id('checklistResult')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeResultField), 15000);

        var beforeNotesField = element.all(by.id('notesField')).get(0);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNotesField), 15000);
        

        var ActionsColumnButtons = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ActionsColumnButtons), 15000);
        ActionsColumnButtons.click();
        browser.sleep(2500);

        var EditButtonClick = element(by.css('[spellcheck="false"] > .dropdown-menu [ng-click="inspectionChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(EditButtonClick), 15000);
        EditButtonClick.click();
        browser.sleep(2500);

        var assignedTo = element(by.model('checklist.assignedTo'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(assignedTo), 15000);
        assignedTo.click();
        element.all(by.css('.ui-select-choices-row-inner div')).get(1).click();
        browser.sleep(2500);

        var result = element(by.model('checklist.result'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(result), 15000);
        result.click();
        element.all(by.css('.ui-select-choices-row-inner div')).get(2).click();
        browser.sleep(2500);

        var notes = element(by.model('checklist.notes'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(notes), 15000);
        notes.sendKeys('notes field');
        browser.sleep(2500);

        var cancelButton = element(by.css('[ng-click="inspectionChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelButton), 15000);
        cancelButton.click();
        browser.sleep(2500);

        var afterAssignToField = element.all(by.id('checklistAssignTo')).get(0);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterAssignToField), 15000);

        var afterResultField = element.all(by.id('checklistResult')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterResultField), 15000);

        var afterNotesField = element.all(by.id('notesField')).get(0);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNotesField), 15000);

        expect(beforeAssignToField.getText()).toEqual(afterAssignToField.getText());
        browser.sleep(5000);
        expect(beforeResultField.getText()).toEqual(afterResultField.getText());
        browser.sleep(5000);
        expect(beforeNotesField.getText()).toEqual(afterNotesField.getText());
        browser.sleep(5000);
        
    });

});
