var urlsPage = require('../../../../../pages/urlsPage');

describe('New Material Inspection Test cases:',function(){

    beforeAll(function () {
        browser.get(urlsPage.allInspections);
        browser.sleep(5000);
        var selectMaterial = element(by.id('materialType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMaterial), 15000);
        selectMaterial.click();
        browser.sleep(3000);
       
    });

    it('without Select Manufacturer Part Type -> create new Material Inspection',function(){
        var newButtonClick =element(by.id('newInspectionButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Select Manufacturer Part Type');
        browser.sleep(8000);
    });

    it('without Select Material -> create new Material Inspection',function(){

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
    
        var selectInspectionType =  element(by.id('manufacturerPartTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectInspectionType), 15000);
        selectInspectionType.click();
        browser.sleep(5000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select material');
        browser.sleep(8000);
    });

    it('without Select Inspection Plan -> create new Material Inspection',function(){
        
        var materialSelect =  element(by.model('newInspectionVm.newInspection.material'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(materialSelect), 20000);
        materialSelect.click(); 
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

    it('without Select Assigned To  -> create new Material Inspection',function(){

        var inspectionPlanSelected =  element(by.id('selectInspectionPlan'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(inspectionPlanSelected), 20000);
        inspectionPlanSelected.click(); 
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

    it('without Select Workflow -> create new Material Inspection',function(){

        var assignToSelect =  element(by.model('newInspectionVm.newInspection.assignedTo'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(assignToSelect), 20000);
        assignToSelect.click(); 
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
        expect(alertMessage).toEqual('Please select workflow');
        browser.sleep(8000);

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(5000);
    });

    it('New Material Inspection Creation successful test case ', function () {
        browser.get(urlsPage.allInspections);
        browser.sleep(8000);
        var selectMaterial = element(by.id('materialType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMaterial), 15000);
        selectMaterial.click();
        browser.sleep(3000);
        
        var newButtonClick =element(by.id('newInspectionButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Material Inspection');
    
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
    
        var selectInspectionType =  element(by.id('manufacturerPartTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectInspectionType), 15000);
        selectInspectionType.click();
    
    
        var materialSelect =  element(by.model('newInspectionVm.newInspection.material'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(materialSelect), 20000);
        materialSelect.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click(); 
    
        var inspectionPlanSelected =  element(by.id('selectInspectionPlan'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(inspectionPlanSelected), 20000);
        inspectionPlanSelected.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
    
        
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

