var urlsPage = require('../../../../pages/urlsPage');

describe('New Product InspectionPlan Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allInspectionPlan);
        browser.sleep(5000);
        var selectProduct = element(by.id('productType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProduct), 15000);
        selectProduct.click();
        browser.sleep(5000); 
    });

    it('without Select Plan Type   -> create New Product Inspection Plan',function(){
        var newInspectionPlanButton = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionPlanButton), 10000);
        newInspectionPlanButton.click();
        browser.sleep(5000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select plan type');
        browser.sleep(8000);
    });

    it('without Select Product -> create New Product Inspection Plan',function(){

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(2000);
        var selectInspectionPlanType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectInspectionPlanType), 15000);
        selectInspectionPlanType.click();

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

    it('without Name -> create  New Product Inspection Plan',function(){
        
        var productSelect = element(by.model('newInspectionPlanVm.newInspectionPlan.product'));
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
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(8000);
    });

    it('without Select Workflow -> create New Product Inspection Plan',function(){

        var inspectionPlanName =  element(by.model('newInspectionPlanVm.newInspectionPlan.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(inspectionPlanName), 20000);
        inspectionPlanName.sendKeys('Inspection plan 3'); 
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

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(5000);
    });

    it('New Product InspectionPlan Created Successful Test case ', function () {
        var newButtonClick = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Product Inspection Plan');
        browser.sleep(2000);
        
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(2000);

        var selectInspectionPlanType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectInspectionPlanType), 15000);
        selectInspectionPlanType.click();
        browser.sleep(3000);

        var productSelect = element(by.model('newInspectionPlanVm.newInspectionPlan.product'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(productSelect), 20000);
        productSelect.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newInspectionPlanVm.newInspectionPlan.name').sendKeys('Product Inspection plan 133')), 25000);
        browser.sleep(2000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newInspectionPlanVm.newInspectionPlan.description').sendKeys('description')), 30000);
        browser.sleep(3000);

        var workflowSelect = element(by.model('newInspectionPlanVm.newInspectionPlan.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(workflowSelect), 15000);
        workflowSelect.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newInspectionPlanVm.newInspectionPlan.notes').sendKeys('notes')), 30000);
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function (url) {
            var inspectionPlanProduct_id = url.split("pqm/inspectionPlan/")[1].replace("?tab=details.basic","");
            browser.sleep(1000);
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl+'pqm/inspectionPlan/'+inspectionPlanProduct_id+'?tab=details.basic');
        });

        browser.sleep(10000);
    });

});

describe('New Material InspectionPlan Test cases:', function () {
    beforeAll(function () {
        browser.get(urlsPage.allInspectionPlan);
        browser.sleep(8000);
        var selectMaterial = element(by.id('materialType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMaterial), 15000);
        selectMaterial.click();
        browser.sleep(5000);
       
    });
    
    it('without Select Plan Type -> create New Product Inspection Plan',function(){
        var newInspectionPlanButton = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionPlanButton), 10000);
        newInspectionPlanButton.click();
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select plan type');
        browser.sleep(8000);
    });

    it('without Select Material -> create New Product Inspection Plan',function(){

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(2000);
        var selectInspectionPlanType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectInspectionPlanType), 15000);
        selectInspectionPlanType.click();

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

    it('without Name -> create  New Product Inspection Plan',function(){
        
        var materialSelect = element(by.model('newInspectionPlanVm.newInspectionPlan.material'));
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
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(8000);
    });

    it('without Select Workflow -> create New Product Inspection Plan',function(){

        var inspectionPlanName =  element(by.model('newInspectionPlanVm.newInspectionPlan.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(inspectionPlanName), 20000);
        inspectionPlanName.sendKeys('Inspection plan 3'); 
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

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(5000);
    });

    it('New Material InspectionPlan Created Successful Test case', function () {
        var newButtonClick = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Material Inspection Plan');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selectInspectionPlanType = element(by.id('qualityTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectInspectionPlanType), 15000);
        selectInspectionPlanType.click();

        browser.sleep(3000);

        var materialSelect = element(by.model('newInspectionPlanVm.newInspectionPlan.material'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(materialSelect), 20000);
        materialSelect.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newInspectionPlanVm.newInspectionPlan.name').sendKeys('Material Inspection plan 133')), 25000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newInspectionPlanVm.newInspectionPlan.description').sendKeys('description')), 30000);

        browser.sleep(2000);

        var workflowSelect = element(by.model('newInspectionPlanVm.newInspectionPlan.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(workflowSelect), 15000);
        workflowSelect.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        browser.wait(EC.presenceOf(urlsPage.byModel('newInspectionPlanVm.newInspectionPlan.notes').sendKeys('notes')), 30000);


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function (url) {
            var inspectionPlanMaterial_id = url.split("pqm/inspectionPlan/")[1].replace("?tab=details.basic","");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl + 'pqm/inspectionPlan/'+inspectionPlanMaterial_id+'?tab=details.basic');
        });
        browser.sleep(10000);
    });

});