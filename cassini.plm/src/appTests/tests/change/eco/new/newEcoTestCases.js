var ecoPage = require('../../../../pages/ecoPage');
var urlsPage = require('../../../../pages/urlsPage');

describe('New ECO Test Cases', function () {

    beforeAll(function(){
        browser.get(urlsPage.allEcos);
        browser.sleep(5000);
    });

    it('with out Change Type -> create new Eco', function () {
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
        expect(alertMessage).toEqual('Please select type');
        browser.sleep(5000);

    });

    it('with out Change Analyst -> create new Eco', function () {
        browser.sleep(5000);
        var selectbuttonClick = element(by.id('select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();

        var selectEcoType = element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectEcoType), 15000);
        selectEcoType.click();

        browser.sleep(3000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select change analyst');
        browser.sleep(10000);

    });

    it('with out Title -> create new Eco', function () {
        browser.sleep(2000);
        var owner = element(by.model('newEcoVm.newECO.ecoOwnerObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(owner), 15000);
        owner.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('ECO title cannot be empty');
        browser.sleep(8000);

    });

    it('with out Reason for Change -> create new Eco', function () {

        var EcoTitle = element(by.model('newEcoVm.newECO.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(EcoTitle), 15000);
        EcoTitle.sendKeys('Eco 1');

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

    it('with out Workflow  -> create new Eco', function () {

        var reasonForChange = element(by.model('newEcoVm.newECO.reasonForChange'));
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
        expect(alertMessage).toEqual('ECO workflow cannot be empty');
        browser.sleep(8000);

        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

    });

    it('New ECO Creation successfull entering mandatory fields Test case', function () {
        var newButtonClick = element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New ECO');

        var selectbuttonClick = element(by.id('select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();


        var selectEcoType = element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectEcoType), 15000);
        selectEcoType.click();


        var owner = element(by.model('newEcoVm.newECO.ecoOwnerObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(owner), 15000);
        owner.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(ecoPage.byModel('newEcoVm.newECO.title').sendKeys('ECO 4')), 25000);
        browser.wait(EC.presenceOf(ecoPage.byModel('newEcoVm.newECO.description').sendKeys('description')), 30000);
        browser.wait(EC.presenceOf(ecoPage.byModel('newEcoVm.newECO.reasonForChange').sendKeys('reasonForChange')), 35000);

        var workflow = element(by.model('newEcoVm.newECO.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(workflow), 15000);
        workflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(8000);

        browser.getCurrentUrl().then(function (url) {
            var eco_id = url.split("eco/")[1].replace("?tab=details.basic","");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl + 'changes/eco/'+eco_id+'?tab=details.basic');
        });
        browser.sleep(10000);
    });
});