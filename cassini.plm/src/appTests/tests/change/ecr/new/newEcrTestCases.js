var urlsPage = require('../../../../pages/urlsPage');

describe('New ECR Test Cases:',function(){

    beforeAll(function(){
        browser.get(urlsPage.allEcrs);
        browser.sleep(5000);
    });

    it('with out Change Type -> create new Ecr', function () {
        var newButtonClick = element(by.id('newEcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select Change Type');
        browser.sleep(5000);

    });

    it('with out Title -> create new Ecr', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();

        var selectEcrType = element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectEcrType), 10000);
        selectEcrType.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter title');
        browser.sleep(8000);

    });

    it('with out Select Change Reason -> create new Ecr', function () {

        var ecrTitle = element(by.model('newEcrVm.newEcr.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ecrTitle), 15000);
        ecrTitle.sendKeys('ecr 1');

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select Change Reason');
        browser.sleep(8000);

    });

    it('with out select Originator -> create new Ecr', function () {

        var changeReasonType =  element(by.model('newEcrVm.newEcr.changeReasonType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeReasonType), 15000);
        changeReasonType.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


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

    it('with out select Requested By -> create new Ecr', function () {

        var originator =  element(by.model('newEcrVm.newEcr.originator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(originator), 15000);
        originator.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select Requested By');
        browser.sleep(8000);

    });

    it('with out select Change Analyst -> create new Ecr', function () {

        var requestedBy =  element(by.model('newEcrVm.newEcr.requestedBy'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(requestedBy), 15000);
        requestedBy.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select Change Analyst');
        browser.sleep(8000);

    });

    it('with out select Workflow -> create new Ecr', function () {

        var changeAnalyst =  element(by.model('newEcrVm.newEcr.changeAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeAnalyst), 15000);
        changeAnalyst.click(); 
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

        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

    });


    it('New ECR Created successfully Test case', function () {
        var newButtonClick = element(by.id('newEcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New ECR');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();


        var selectEcrType =  element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectEcrType), 15000);
        selectEcrType.click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newEcrVm.newEcr.title').sendKeys('ECR 5')), 25000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newEcrVm.newEcr.descriptionOfChange').sendKeys('descriptionOfChange')), 30000);


        var changeReasonType =  element(by.model('newEcrVm.newEcr.changeReasonType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeReasonType), 15000);
        changeReasonType.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newEcrVm.newEcr.reasonForChange').sendKeys('reasonForChange')), 35000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newEcrVm.newEcr.proposedChanges').sendKeys('proposedChanges')), 35000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newEcrVm.newEcr.impactAnalysis').sendKeys('impactAnalysis')), 35000);


        var originator =  element(by.model('newEcrVm.newEcr.originator'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(originator), 15000);
        originator.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var requestedBy =  element(by.model('newEcrVm.newEcr.requestedBy'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(requestedBy), 15000);
        requestedBy.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var changeAnalyst =  element(by.model('newEcrVm.newEcr.changeAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeAnalyst), 15000);
        changeAnalyst.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var urgency =  element(by.model('newEcrVm.newEcr.urgency'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(urgency), 15000);
        urgency.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


       var workflow =  element(by.model('newEcrVm.newEcr.workflow'));
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
       var ecr_id=url.split("ecr/")[1].replace("?tab=details.basic","");
        expect(browser.getCurrentUrl())
              .toEqual(urlsPage.baseUrl+'changes/ecr/'+ecr_id+'?tab=details.basic');
      });

    });

});