var urlsPage = require('../../../../../pages/urlsPage.js');

it('Click on Check list tab -> open the Check list tab View successfully', function () {
    var checklist = element(by.id('checklist'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(checklist), 15000);
    checklist.click();
    expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'pqm/inspectionPlan/' + urlsPage.url_id + '?tab=details.checklist');
    browser.sleep(5000);
});

describe('Inspection Plan Details -> Check list Tab -> Section TestCases:',function(){

    it("click Add Section(+) button to display the New Section fields ",function(){
        var clickAddSection = element(by.css('[ng-click="planChecklistVm.addSection()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAddSection), 15000);
        clickAddSection.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var cancelButton= element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField,saveButton,cancelButton), 15000);
        expect(titleField.isDisplayed()).toBe(true);
        expect(saveButton.isDisplayed()).toBe(true);
        expect(cancelButton.isDisplayed()).toBe(true);
        browser.sleep(5000);
        cancelButton.click();
        browser.sleep(2000);
    });

    it("click New Section Save button Without entering Title Name ",function(){
        var clickAddSection = element(by.css('[ng-click="planChecklistVm.addSection()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAddSection), 15000);
        clickAddSection.click();
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter title');
        browser.sleep(5000);

        var onCancel = element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(onCancel), 15000);
        onCancel.click();
        browser.sleep(3000);
    });

    it("click New Section Save button With entering Title Name ",function(){
        var clickAddSection = element(by.css('[ng-click="planChecklistVm.addSection()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAddSection), 15000);
        clickAddSection.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.sendKeys('Sectionn 1');
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Section saved successfully');
        browser.sleep(5000);
    });

    it("click Edit Section -> Save button Without entering Title Name ",function(){
        var clickAction = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickEdit = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickEdit), 15000);
        clickEdit.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.clear();
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter title');
        browser.sleep(5000);

        var onCancel = element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(onCancel), 15000);
        onCancel.click();
        browser.sleep(3000);
    });

    it("click Edit Section -> Cancel button -> not update Title name Test case ",function(){
        var beforeTitleName = element(by.xpath("//div[@class='tab-pane ng-scope active']//tr[1]//span[@class='ng-binding ng-scope']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeTitleName), 15000);

        var clickAction = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickEdit = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickEdit), 15000);
        clickEdit.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.sendKeys('salmann');
        browser.sleep(1500);

        var onCancel = element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(onCancel), 15000);
        onCancel.click();
        browser.sleep(4000);
       
        var afterTitleName = element(by.xpath("//div[@class='tab-pane ng-scope active']//tr[1]//span[@class='ng-binding ng-scope']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterTitleName), 15000);

        expect(beforeTitleName.getText()).toEqual(afterTitleName.getText());
        browser.sleep(5000);
    });

    it("click Edit Section -> Save button With entering Title Name ",function(){
        var clickAction = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickEdit = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickEdit), 15000);
        clickEdit.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.sendKeys('sectiion 11');
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Section saved successfully');
        browser.sleep(5000);

    });

    it("click Delete Section Test case",function(){
        var clickAction = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickDelete = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.deleteChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickDelete), 15000);
        clickDelete.click();
        browser.sleep(1000);

        var deletePopUpWindow = element(by.css('.modal-header')).element(by.tagName('h4'));
        browser.sleep(1500);
        expect(deletePopUpWindow.getText()).toBe('Delete Section');
    
        var deleteButtonYes = element(by.css("[ng-click='confirmVm.onOk()']"));
        browser.sleep(1500);
        expect(deleteButtonYes.getText()).toBe('OK');
        deleteButtonYes.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Section deleted successfully');
        browser.sleep(5000);
    });

    it("click New Section Save button With entering Title Name ",function(){
        var clickAddSection = element(by.css('[ng-click="planChecklistVm.addSection()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAddSection), 15000);
        clickAddSection.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.sendKeys('Section 11');
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Section saved successfully');
        browser.sleep(5000);
    });

});

describe('Inspection Plan Details -> Check list Tab -> Checklist TastCases:',function(){

    it("click Add Checklist(+) button to display the Add Checklist fields ",function(){
        var clickAddChecklist = element.all(by.css('[ng-click="planChecklistVm.addChecklist(checklist)"]')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAddChecklist), 15000);
        clickAddChecklist.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title')); 
        var summaryField = element(by.model('checklist.summary'));
        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var cancelButton= element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField,summaryField,saveButton,cancelButton), 15000);
        expect(titleField.isDisplayed()).toBe(true);
        expect(summaryField.isDisplayed()).toBe(true);
        expect(saveButton.isDisplayed()).toBe(true);
        expect(cancelButton.isDisplayed()).toBe(true);
        browser.sleep(5000);
        cancelButton.click();
        browser.sleep(2000);
    });

    it("click Add Checklist Save button Without entering Title Name ",function(){
        var clickAddSection = element.all(by.css('[ng-click="planChecklistVm.addChecklist(checklist)"]')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAddSection), 15000);
        clickAddSection.click();
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter title');
        browser.sleep(5000);

        var onCancel = element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(onCancel), 15000);
        onCancel.click();
        browser.sleep(3000);
    });

    it("click Add Checklist Save button With entering Title Name ",function(){
        var clickAddSection = element.all(by.css('[ng-click="planChecklistVm.addChecklist(checklist)"]')).last();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAddSection), 15000);
        clickAddSection.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.sendKeys('Checklist 1');
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Checklist saved successfully');
        browser.sleep(5000);
    });

    it("click Edit Checklist -> Save button Without entering Title Name ",function(){
        var clickAction = element.all(by.css('.fa-ellipsis-h')).last();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickEdit = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickEdit), 15000);
        clickEdit.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.clear();
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter title');
        browser.sleep(5000);

        var onCancel = element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(onCancel), 15000);
        onCancel.click();
        browser.sleep(3000);
    });

    it("click Edit Check list -> Cancel button -> not update Title name Test case ",function(){
        var beforeTitleName = element.all(by.id('checklistTitle')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeTitleName), 15000);

        var clickAction = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickEdit = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickEdit), 15000);
        clickEdit.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.sendKeys('salmann');
        browser.sleep(1500);

        var onCancel = element(by.css('[ng-click="planChecklistVm.onCancel(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(onCancel), 15000);
        onCancel.click();
        browser.sleep(4000);
       
        var afterTitleName = element.all(by.id('checklistTitle')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterTitleName), 15000);

        expect(beforeTitleName.getText()).toEqual(afterTitleName.getText());
        browser.sleep(5000);
    });

    it("click Edit Checklist -> Save button With entering Title Name ",function() {
        var clickAction = element.all(by.css('.fa-ellipsis-h')).last();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickEdit = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.editChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickEdit), 15000);
        clickEdit.click();
        browser.sleep(1500);

        var titleField = element(by.model('checklist.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(titleField), 15000);
        titleField.sendKeys('Checklist 11');
        browser.sleep(1500);

        var saveButton = element(by.css('[ng-click="planChecklistVm.onOk(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 15000);
        saveButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Checklist saved successfully');
        browser.sleep(5000);

    });

    it("click Delete Checklist Test case",function(){
        var clickAction = element.all(by.css('.fa-ellipsis-h')).last();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickAction), 15000);
        clickAction.click();
        browser.sleep(1500);

        var clickDelete = element(by.css(' [spellcheck="false"] > .dropdown-menu [ng-click="planChecklistVm.deleteChecklist(checklist)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickDelete), 15000);
        clickDelete.click();
        browser.sleep(1000);

        var deletePopUpWindow = element(by.css('.modal-header')).element(by.tagName('h4'));
        browser.sleep(1500);
        expect(deletePopUpWindow.getText()).toBe('Delete Checklist');
    
        var deleteButtonYes = element(by.css("[ng-click='confirmVm.onOk()']"));
        browser.sleep(1500);
        expect(deleteButtonYes.getText()).toBe('OK');
        deleteButtonYes.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Checklist deleted successfully');
        browser.sleep(5000);
    });

});
