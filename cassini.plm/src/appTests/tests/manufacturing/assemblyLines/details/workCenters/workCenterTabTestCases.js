var urlsPage = require('../../../../../pages/urlsPage.js');

describe('Work Centers Tab:', function () {

    it('Click on Work Centers tab -> open the Work Centers tab View successfully', function () {
        var filesTab = element(by.css('[heading="Work Centers"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(filesTab), 15000);
        filesTab.click();
        expect(browser.getCurrentUrl()).toBe(urlsPage.baseUrl + 'mes/assemblyline/' + urlsPage.url_id + '?tab=details.workCenters');
        browser.sleep(5000);
    });

    it('Plus (+) button Tool tip Test case', function () {
        var plusButton = element(by.css('[ng-click="assemblyLineWorkCentersVm.addWorkCenters()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        browser.sleep(1000);
        expect(plusButton.getAttribute('title')).toEqual('Add work centers');
        browser.sleep(5000);
    });

    it('Click Plus(+) button -> open Add Work Centers Side panel Successfully', function () {
        var plusButton = element(by.css('[ng-click="assemblyLineWorkCentersVm.addWorkCenters()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        plusButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('Add Work Centers');
        browser.sleep(3000);
    });

    it('Click Close(X) button -> close Add Work Centers Side panel Successfully', function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });

    it('Click Add button -> Without select at least one Workcenter Test case', function () {
        var plusButton = element(by.css('[ng-click="assemblyLineWorkCentersVm.addWorkCenters()"]'));
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
        expect(alertMessage).toEqual('Select at least one work center');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(3000);

    });

    it('Click Add button -> with select at least one Workcenter Test case', function () {
        var plusButton = element(by.css('[ng-click="assemblyLineWorkCentersVm.addWorkCenters()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plusButton), 15000);
        plusButton.click();
        browser.sleep(3000);

        var selectWorkcenter = element.all(by.model('workCenter.selected')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWorkcenter), 15000);
        selectWorkcenter.click();
        browser.sleep(3000);

        var addButton = element(by.buttonText('Add'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(addButton), 10000);
        addButton.click();
        browser.sleep(2500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Work center(s) added successfully');
        browser.sleep(8000);
    });

    it('Click Remove workCenter button -> remove that Workcenter from the table Test case', function () {
        var clickonActionButton = element.all(by.css('.fa-ellipsis-h')).first();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonActionButton), 15000);
        clickonActionButton.click();
        browser.sleep(1500);
        var clickonRemoveButton = element.all(by.css(" [spellcheck='false'] > .dropdown-menu li"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickonRemoveButton), 15000);
        clickonRemoveButton.click();
        browser.sleep(2500);
    
        var removePopUpWindow = element(by.css('.modal-header')).element(by.tagName('h4'));
        browser.sleep(1500);
        expect(removePopUpWindow.getText()).toBe('Remove Work Center');
    
        var removeButtonYes = element(by.css("[ng-click='confirmVm.onOk()']"));
        browser.sleep(1500);
        expect(removeButtonYes.getText()).toBe('OK');
        removeButtonYes.click();
        browser.sleep(4500);
        var plantWarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
        expect(plantWarnMes).toEqual('Work center removed successfully');
        browser.sleep(8000);
    });


});
