var urlsPage = require('../../../../../pages/urlsPage');

describe('New Waiver Test cases:',function(){

    beforeAll(function () {
        browser.get(urlsPage.allWaiver);
        browser.sleep(5000);
        var newDeviationButton = element(by.id('newVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newDeviationButton), 10000);
        newDeviationButton.click();
        browser.sleep(5000);
    });

    it('without change type -> create new Waiver',function(){
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select type');
        browser.sleep(5000);
    });

    it('without Title -> create new Waiver',function(){
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selectDeviationType =  element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDeviationType), 15000);
        selectDeviationType.click();
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Title cannot be empty');
        browser.sleep(8000);
    });

    it('without Description -> create new Waiver',function(){
        var WaiverTitle = element(by.model('newVarianceVm.newVariance.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(WaiverTitle), 10000);
        WaiverTitle.sendKeys('Deviation 1');
        
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

    it('without Reason for Waiver -> create new Waiver',function(){
        var description = element(by.model('newVarianceVm.newVariance.description'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(description), 10000);
        description.sendKeys('description');
        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Reason for change cannot be empty');
        browser.sleep(8000);
    });

    it('without Current Requirement -> create new Waiver',function(){
        var reasonForVariance = element(by.model('newVarianceVm.newVariance.reasonForVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(reasonForVariance), 10000);
        reasonForVariance.sendKeys('reasonForVariance');
        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Current requirement cannot be empty');
        browser.sleep(8000);
    });

    it('without Requirement Deviation -> create new Waiver',function(){
        var currentRequirement = element(by.model('newVarianceVm.newVariance.currentRequirement'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(currentRequirement), 10000);
        currentRequirement.sendKeys('reasonForVariance');
        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Requirement cannot be empty');
        browser.sleep(8000);
    });

    it('without Originator -> create new Waiver',function(){
        var requirementDeviation = element(by.model('newVarianceVm.newVariance.requirementDeviation'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(requirementDeviation), 10000);
        requirementDeviation.sendKeys('requirementDeviation');
        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select originator');
        browser.sleep(8000);
    });

    it('without Workflow -> create new Waiver',function(){
        var originator =  element(by.model('newVarianceVm.newVariance.originator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(originator), 15000);
        originator.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(1500);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Workflow cannot be empty');
        browser.sleep(8000);

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
    });

    it('New Waiver Creation Testing here Waiver For = "Items"', function () {
        var newButtonClick = element(by.id('newVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Waiver');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();


        var selectWaiverType =  element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWaiverType), 15000);
        selectWaiverType.click();

        var waiverFor =  element(by.css("[for='prTypeC']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(waiverFor), 15000);
        waiverFor.click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.title').sendKeys('Waiver 1')), 15000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.description').sendKeys('description')), 15000);


        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.reasonForVariance').sendKeys('reasonForVariance')), 15000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.currentRequirement').sendKeys('currentRequirement')), 15000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.requirementDeviation').sendKeys('requirementDeviation')), 15000);


        var originator =  element(by.model('newVarianceVm.newVariance.originator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(originator), 15000);
        originator.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        
        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.notes').sendKeys('notes')), 15000);


       var workflow =  element(by.model('newVarianceVm.newVariance.workflowDefinition'));
       var EC = protractor.ExpectedConditions;
       browser.wait(EC.presenceOf(workflow), 15000);
       workflow.click(); 
       element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

       browser.getCurrentUrl().then(function(url){
       var waiver_id=url.split("variance/")[1].replace("?tab=details.basic","");
        expect(browser.getCurrentUrl())
              .toEqual(urlsPage.baseUrl+'changes/variance/'+waiver_id+'?tab=details.basic');

      });
      browser.sleep(8000);
    });

    it('New Waiver Creation Testing here Waiver For = "material" ', function () {
        browser.get(urlsPage.allWaiver);
        browser.sleep(5000);
        var newButtonClick = element(by.id('newVariance'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Waiver');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selectWaiverType =  element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectWaiverType), 15000);
        selectWaiverType.click();

        var waiverFor =  element(by.css("[for='prTypeI']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(waiverFor), 15000);
        waiverFor.click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.title').sendKeys('Waiver 1')), 25000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.description').sendKeys('description')), 30000);


        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.reasonForVariance').sendKeys('reasonForVariance')), 35000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.currentRequirement').sendKeys('currentRequirement')), 35000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.requirementDeviation').sendKeys('requirementDeviation')), 35000);


        var originator =  element(by.model('newVarianceVm.newVariance.originator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(originator), 15000);
        originator.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newVarianceVm.newVariance.notes').sendKeys('notes')), 35000);


        var workflow =  element(by.model('newVarianceVm.newVariance.workflowDefinition'));
       var EC = protractor.ExpectedConditions;
       browser.wait(EC.presenceOf(workflow), 15000);
       workflow.click(); 
       element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

       browser.getCurrentUrl().then(function(url){
       var waiver_id=url.split("variance/")[1].replace("?tab=details.basic","");
        expect(browser.getCurrentUrl())
              .toEqual(urlsPage.baseUrl+'changes/variance/'+waiver_id+'?tab=details.basic');
      });

    });
})