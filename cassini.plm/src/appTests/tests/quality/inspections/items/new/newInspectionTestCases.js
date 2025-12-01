var urlsPage = require('../../../../../pages/urlsPage');

describe('New Item Inspection Test cases:',function(){

    beforeAll(function () {
        browser.get(urlsPage.allInspections);
        browser.sleep(5000);
        var selectProduct = element(by.id('productType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProduct), 15000);
        selectProduct.click();
        browser.sleep(5000);
       
    });

    it('without Select Product  -> create new Item Inspection',function(){
        var newInspectionButton = element(by.id('newInspectionButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionButton), 15000);
        newInspectionButton.click();
        browser.sleep(5000);

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

    it('without Select Inspection Plan -> create new Item Inspection',function(){

        var productSelect =  element(by.model('newInspectionVm.newInspection.item'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(productSelect), 20000);
        productSelect.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(5000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select inspection plan');
        browser.sleep(8000);
    });

    it('without Assigned To -> create new Item Inspection',function(){
        browser.sleep(5000);
        var inspectionPlanSelect =  element(by.model('newInspectionVm.newInspection.inspectionPlan'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(inspectionPlanSelect), 20000);
        inspectionPlanSelect.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(5000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select assigned to');
        browser.sleep(8000);
    });

    it('without Select Workflow -> create new Item Inspection',function(){

        var assignToSelect =  element(by.model('newInspectionVm.newInspection.assignedTo'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(assignToSelect), 20000);
        assignToSelect.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

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
        browser.sleep(5000);
    });

    it('New Item Inspection Creation successful test case', function () {
        var newButtonClick =element(by.id('newInspectionButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Item Inspection');


        var productSelect =  element(by.model('newInspectionVm.newInspection.item'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(productSelect), 20000);
        productSelect.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var inspectionPlanSelect =  element(by.model('newInspectionVm.newInspection.inspectionPlan'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(inspectionPlanSelect), 20000);
        inspectionPlanSelect.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(5000);

        var assignToSelect =  element(by.model('newInspectionVm.newInspection.assignedTo'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(assignToSelect), 20000);
        assignToSelect.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


       var workflowSelect =  element(by.model('newInspectionVm.newInspection.workflow'));
       var EC = protractor.ExpectedConditions;
       browser.wait(EC.presenceOf(workflowSelect), 15000);
       workflowSelect.click(); 
       element.all(by.css('.ui-select-choices-row-inner div')).first().click();

       browser.wait(EC.presenceOf(urlsPage.byModel('newInspectionVm.newInspection.notes').sendKeys('notes')), 30000);


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

       browser.getCurrentUrl().then(function(url){
       var inspection_id=url.split("inspections/")[1].replace("?tab=details.basic","");
        expect(browser.getCurrentUrl())
              .toEqual(urlsPage.baseUrl+'pqm/inspections/'+inspection_id+'?tab=details.basic');

      });

      browser.sleep(10000);
    });
});


