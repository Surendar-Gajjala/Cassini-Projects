var urlsPage = require('../../../../pages/urlsPage');

describe('New DCR TestCases:',function(){

    beforeAll(function(){
        browser.get(urlsPage.allDcrs);
        browser.sleep(5000);
    });

    it('with out Change Type -> create new Dcr', function () {
        var newButtonClick = element(by.id('newDcrButton'));
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
        expect(alertMessage).toEqual('DCR type cannot be empty');
        browser.sleep(5000);

    });

    it('with out Title -> create new Dcr', function () {

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
        expect(alertMessage).toEqual('Title cannot be empty');
        browser.sleep(8000);

    });

    it('with out Select Change Reason -> create new Dcr', function () {

        var dcrTitle = element(by.model('newDcrVm.newDCR.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(dcrTitle), 15000);
        dcrTitle.sendKeys('Dcr 1');

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

    it('with out Reason for Change -> create new Dcr', function () {

        var changeReasonType =  element(by.model('newDcrVm.newDCR.changeReasonType'));
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
        expect(alertMessage).toEqual('Reason for change cannot be empty');
        browser.sleep(8000);

    });

    it('with out select Originator -> create new Dcr', function () {

        var reasonForChange =  element(by.model('newDcrVm.newDCR.reasonForChange'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(reasonForChange), 15000);
        reasonForChange.sendKeys('ReasonForChange'); 

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Originator cannot be empty');
        browser.sleep(8000);

    });

    it('with out select Requested By -> create new Dcr', function () {
        browser.sleep(4000);
        var originator =  element(by.model('newDcrVm.newDCR.originatorObject'));
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
        expect(alertMessage).toEqual('Requested by cannot be empty');
        browser.sleep(8000);

    });

    it('with out select Workflow -> create new Dcr', function () {
        browser.sleep(4000);
        var requestedBy =  element(by.model('newDcrVm.newDCR.requestedByObject'));
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
        expect(alertMessage).toEqual('Workflow cannot be empty');
        browser.sleep(8000);

        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(5000);

    });

    it('New DCR Creation Successfull TestCase', function () {
        var newButtonClick = element(by.id('newDcrButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New DCR');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();


        var selectDcrType =  element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDcrType), 15000);
        selectDcrType.click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newDcrVm.newDCR.title').sendKeys('DCR 5')), 25000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newDcrVm.newDCR.descriptionOfChange').sendKeys('descriptionOfChange')), 30000);


        var changeReasonType =  element(by.model('newDcrVm.newDCR.changeReasonType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeReasonType), 15000);
        changeReasonType.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newDcrVm.newDCR.reasonForChange').sendKeys('reasonForChange')), 35000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newDcrVm.newDCR.proposedChanges').sendKeys('proposedChanges')), 35000);


        var originator =  element(by.model('newDcrVm.newDCR.originatorObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(originator), 15000);
        originator.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var requestedBy =  element(by.model('newDcrVm.newDCR.requestedByObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(requestedBy), 15000);
        requestedBy.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var owner =  element(by.model('newDcrVm.newDCR.dcrOwnerObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(owner), 15000);
        owner.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var urgency =  element(by.model('newDcrVm.newDCR.urgency'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(urgency), 15000);
        urgency.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


       var workflow =  element(by.model('newDcrVm.newDCR.workflow'));
       var EC = protractor.ExpectedConditions;
       browser.wait(EC.presenceOf(workflow), 15000);
       workflow.click(); 
       element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
      
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('DCR created successfully');
        browser.sleep(8000);
        
    //    browser.getCurrentUrl().then(function(url){
    //    var dcr_id=url.split("dcr/")[1].replace("?tab=details.basic","");
    //     expect(browser.getCurrentUrl())
    //           .toEqual(urlsPage.baseUrl+'changes/dcr/'+dcr_id+'?tab=details.basic');

    //   });

    });
})