var urlsPage = require('../../../../../pages/urlsPage.js');

describe('DCO Details -> Affected Items tab:', function () {
    beforeAll(function () {
        browser.refresh();
        browser.sleep(8000);
    });

    it('Click on Affected Items tab -> open the  Affected Items tab View successfully', function () {
        browser.sleep(5000);
        var filesTab = element(by.id('affectedItems'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'changes/dco/' + urlsPage.url_id + '?tab=details.affectedItems');
        browser.sleep(5000);
    });
   

    it('Click Plus(+) button -> open Select items Side panel Successfully', function () {
        var plusButton = element(by.css('[ng-click="dcoAffectedItemsVm.showDcoItems()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        expect(plusButton.getAttribute('title')).toBe('Add Affected Items');
        plusButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('Select items');
        browser.sleep(3000);
    });

    it('Click Close(X) button -> close Select items Side panel Successfully', function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });

    it('Click Add button -> Without select at least one Item Test case', function () {
        var plusButton = element(by.css('[ng-click="dcoAffectedItemsVm.showDcoItems()"]'));
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
        expect(alertMessage).toEqual('Please select at least one item');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);

    });

    it('Click Add button -> with select at least one Item Test case', function () {
        var plusButton = element(by.css('[ng-click="dcoAffectedItemsVm.showDcoItems()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        plusButton.click();
        browser.sleep(3000);

        var selectItemcenter = element.all(by.model('item.selected')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectItemcenter), 15000);
        selectItemcenter.click();
        browser.sleep(3000);

        var addButton = element(by.buttonText('Add'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(addButton), 10000);
        addButton.click();
        browser.sleep(2500); 

        var saveButton = element(by.css('button .fa-check'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(saveButton), 10000);
        saveButton.click();
        browser.sleep(4500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Item(s) added successfully');
        browser.sleep(8000);
    });

  

    it('Click Edit Item button -> Edit option enable successful', function () {
        var clickonActionButton = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonActionButton), 15000);
        clickonActionButton.click();
        browser.sleep(1500);
        var clickonEditItemButton = element(by.css("[spellcheck='false'] > .dropdown-menu [ng-click='dcoAffectedItemsVm.editItem(item)']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonEditItemButton), 15000);
        clickonEditItemButton.click();
        browser.sleep(5000);
    
        var editNotesField = element(by.model("item.notes"));
        var updateButton = element(by.css('[ng-click="dcoAffectedItemsVm.updateItem(item)"]'));
        var cancelButton = element(by.css('[ng-click="dcoAffectedItemsVm.cancelChanges(item)"]'));
        browser.sleep(3000);
        expect(editNotesField.isDisplayed(true));
        expect(updateButton.isDisplayed(true));
        expect(cancelButton.isDisplayed(true));
        cancelButton.click();
        browser.sleep(5000);
    });

   
    it('Click save button -> Update the Item successfully', function () {
        var clickonActionButton = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonActionButton), 15000);
        clickonActionButton.click();
        browser.sleep(1500);
        var clickonEditItemButton = element(by.css("[spellcheck='false'] > .dropdown-menu [ng-click='dcoAffectedItemsVm.editItem(item)']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonEditItemButton), 15000);
        clickonEditItemButton.click();
        browser.sleep(5000);
    
        var editNotesField = element(by.model("item.notes"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editNotesField), 15000);
        editNotesField.sendKeys('notes');
        browser.sleep(2000);

        var updateButton = element(by.css('[ng-click="dcoAffectedItemsVm.updateItem(item)"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(updateButton), 15000);
        updateButton.click();
        browser.sleep(4000);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Item updated successfully');
        browser.sleep(8000);
       
    });


    it('Click Remove Item button -> remove that Item from the table Successful', function () {
        var clickonActionButton = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonActionButton), 15000);
        clickonActionButton.click();
        browser.sleep(1500);
        var clickonRemoveItemButton = element.all(by.css("[spellcheck='false'] > .dropdown-menu [ng-click='dcoAffectedItemsVm.deleteItem(item)']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonRemoveItemButton), 15000);
        browser.sleep(2000);
        clickonRemoveItemButton.click();
        browser.sleep(5000);
    
        var conformationPopUpWindow = element(by.css('.modal-header')).element(by.tagName('h4'));
        browser.sleep(1500);
        expect(conformationPopUpWindow.getText()).toBe('Remove Item');
    
        var confirmOk = element(by.css("[ng-click='confirmVm.onOk()']"));
        browser.sleep(1500);
        expect(confirmOk.getText()).toBe('OK');
        confirmOk.click();
        browser.sleep(4500);
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Item removed successfully');
        browser.sleep(8000);
    });

});
