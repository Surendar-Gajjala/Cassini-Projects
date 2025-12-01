var urlsPage = require('../../pages/urlsPage.js');

var relatedItemsDirective= function(){

   
    this.TestCases = function(){
    
        it('Click Plus(+) button -> open Select items Side panel Successfully', function () {
            var plusButton = element(by.css('[ng-click="showRelatedItems()"]'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(plusButton), 15000);
            plusButton.click();
            browser.sleep(3000);
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
            var plusButton = element(by.css('[ng-click="showRelatedItems()"]'));
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
            var plusButton = element(by.css('[ng-click="showRelatedItems()"]'));
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
    
            browser.sleep(4500);
    
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Related item(s) added successfully');
            browser.sleep(8000);
        });
    
        it('Click Add button -> with select more then one Item Test case', function () {
            var plusButton = element(by.css('[ng-click="showRelatedItems()"]'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(plusButton), 15000);
            plusButton.click();
            browser.sleep(3000);
    
            var selectItem1 = element.all(by.model('item.selected')).first();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectItem1), 15000);
            selectItem1.click();
            browser.sleep(3000);
    
            var selectItem2 = element.all(by.model('item.selected')).get(1);
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
            expect(alertMessage).toEqual('Related item(s) added successfully');
            browser.sleep(8000);
        });
    
        it('Click Remove Item button -> remove that Item from the table Successful', function () {
            var clickonActionButton = element.all(by.css('.fa-ellipsis-h')).first();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonActionButton), 15000);
            clickonActionButton.click();
            browser.sleep(1500);
            var clickonRemoveRelatedItemButton = element(by.css("[spellcheck='false'] > .dropdown-menu li a"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonRemoveRelatedItemButton), 15000);
            browser.sleep(2000);
            clickonRemoveRelatedItemButton.click();
            browser.sleep(5000);
        
            var conformationPopUpWindow = element(by.css('.modal-header')).element(by.tagName('h4'));
            browser.sleep(1500);
            expect(conformationPopUpWindow.getText()).toBe('Remove Item');
        
            var confirmOk = element(by.css("[ng-click='confirmVm.onOk()']"));
            browser.sleep(1500);
            expect(confirmOk.getText()).toBe('OK');
            confirmOk.click();
            browser.sleep(4500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Item removed successfully');
            browser.sleep(8000);
        });
       
    }

}
module.exports = new relatedItemsDirective();

