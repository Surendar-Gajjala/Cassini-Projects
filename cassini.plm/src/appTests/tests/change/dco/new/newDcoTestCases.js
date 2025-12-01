var urlsPage = require('../../../../pages/urlsPage');

describe('New DCO Test Cases:',function(){

    
    beforeAll(function(){
        browser.get(urlsPage.allDcos);
        browser.sleep(5000);
    });

    it('with out Change Type -> create new Dco', function () {
        var newButtonClick = element(by.id('newButton'));
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
        expect(alertMessage).toEqual('DCO type cannot be empty');
        browser.sleep(5000);

    });

    it('with out Title -> create new Dco', function () {

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

    
    it('with out Reason for Change -> create new Dco', function () {

        var dcrTitle = element(by.model('newDcoVm.newDCO.title'));
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
        expect(alertMessage).toEqual('Reason for change cannot be empty');
        browser.sleep(8000);

    });

    it('with out select Change Analyst  -> create new Dco', function () {

        var reasonForChange =  element(by.model('newDcoVm.newDCO.reasonForChange'));
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
        expect(alertMessage).toEqual('Please select Change Analyst');
        browser.sleep(8000);

    });


    it('with out select Workflow -> create new Dco', function () {
        var changeAnalyst =  element(by.model('newDcoVm.newDCO.dcoOwnerObject'));
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
        expect(alertMessage).toEqual('Workflow cannot be empty');
        browser.sleep(8000);

        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

    });

    it('New DCO Creation Successfull Test case', function () {
        var newButtonClick = element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New DCO');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selectDcoType =  element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectDcoType), 15000);
        selectDcoType.click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newDcoVm.newDCO.title').sendKeys('DCO 4')), 25000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newDcoVm.newDCO.description').sendKeys('description')), 30000);


        browser.wait(EC.presenceOf(urlsPage.byModel('newDcoVm.newDCO.reasonForChange').sendKeys('reasonForChange')), 35000);


        var changeAnalyst =  element(by.model('newDcoVm.newDCO.dcoOwnerObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeAnalyst), 15000);
        changeAnalyst.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


       var workflow =  element(by.model('newDcoVm.newDCO.workflow'));
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
       var dco_id=url.split("dco/")[1].replace("?tab=details.basic","");
        expect(browser.getCurrentUrl())
              .toEqual(urlsPage.baseUrl+'changes/dco/'+dco_id+'?tab=details.basic');

      });
      browser.sleep(8000);
    });

});