var urlsPage = require('../../../../pages/urlsPage');

describe('New MCO for Products Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMcos);
        browser.sleep(5000);
        var itemMcoType = element(by.id('itemMcoType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(itemMcoType), 15000);
        itemMcoType.click();
        browser.sleep(8000);
    });

    it('with out Change Type -> create new Mco', function () {
        browser.sleep(3000);
        var newButtonClick = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(3500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select Change Type');
        browser.sleep(5000);

    });

    it('with out Title -> create new Mco', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();
        browser.sleep(2000);
        var selectEcrType = element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectEcrType), 10000);
        selectEcrType.click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(3500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter title');
        browser.sleep(8000);

    });


    it('with out select Change Analyst  -> create new Mco', function () {

        var mcrTitle = element(by.model('newMcoVm.newMco.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(mcrTitle), 15000);
        mcrTitle.sendKeys('Dcr 1');
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(3500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select Change Analyst');
        browser.sleep(8000);

    });


    it('with out select Workflow -> create new Mco', function () {
        var changeAnalyst = element(by.model('newMcoVm.newMco.changeAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeAnalyst), 15000);
        changeAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();

        browser.sleep(3500);
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


    it('New Product MCO Creation Successfull Test case', function () {
        var newButtonClick = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New MCO');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(2000);

        var selectMcoType = element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMcoType), 15000);
        selectMcoType.click();
        browser.sleep(2000);
        // var qcr =  element(by.model('newMcoVm.newMco.qcr'));
        // var EC = protractor.ExpectedConditions;
        // browser.wait(EC.presenceOf(qcr), 15000);
        // qcr.click(); 
        // element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newMcoVm.newMco.title').sendKeys('MCO Product 4')), 20000);
        browser.sleep(2000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newMcoVm.newMco.description').sendKeys('description')), 30000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newMcoVm.newMco.reasonForChange').sendKeys('reasonForChange')), 35000);
        browser.sleep(2000);

        var changeAnalyst = element(by.model('newMcoVm.newMco.changeAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeAnalyst), 15000);
        changeAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var workflow = element(by.model('newMcoVm.newMco.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(workflow), 15000);
        workflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function (url) {
            var mco_id = url.split("mco/")[1].replace("?tab=details.basic", "");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl + 'changes/mco/' + mco_id + '?tab=details.basic');

        });

    });

});

//------------------------ Material Mco --------------------------------

describe('New MCO for Material Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allMcos);
        browser.sleep(5000);
        var materialType = element(by.id('manufacturerMcoType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(materialType), 15000);
        materialType.click();
        browser.sleep(5000);
    });

    it('with out Change Type -> create new Mco', function () {
        var newMcoButton = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newMcoButton), 10000);
        newMcoButton.click();
        browser.sleep(2000);

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

    it('with out Title -> create new Mco', function () {

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();
        browser.sleep(2000);
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


    it('with out select Change Analyst  -> create new Mco', function () {

        var mcrTitle = element(by.model('newMcoVm.newMco.title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(mcrTitle), 15000);
        mcrTitle.sendKeys('Dcr 1');
        browser.sleep(2000);
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


    it('with out select Workflow -> create new Mco', function () {
        var changeAnalyst = element(by.model('newMcoVm.newMco.changeAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeAnalyst), 15000);
        changeAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
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

        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(5000);

    });

    it('New MCO Creation  for Material Testcase', function () {

        var newButtonClick = element(by.id('newMcoButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New MCO');

        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();
        browser.sleep(2000);

        var selectMcoType = element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMcoType), 15000);
        selectMcoType.click();
        browser.sleep(2000);
        // var qcr =  element(by.model('newMcoVm.newMco.qcr'));
        // var EC = protractor.ExpectedConditions;
        // browser.wait(EC.presenceOf(qcr), 15000);
        // qcr.click(); 
        // element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        browser.wait(EC.presenceOf(urlsPage.byModel('newMcoVm.newMco.title').sendKeys('MCO Material 4')), 20000);
        browser.sleep(2000);
        browser.wait(EC.presenceOf(urlsPage.byModel('newMcoVm.newMco.description').sendKeys('description')), 30000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newMcoVm.newMco.reasonForChange').sendKeys('reasonForChange')), 35000);
        browser.sleep(2000);

        var changeAnalyst = element(by.model('newMcoVm.newMco.changeAnalyst'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(changeAnalyst), 15000);
        changeAnalyst.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var workflow = element(by.model('newMcoVm.newMco.workflow'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(workflow), 15000);
        workflow.click();
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function (url) {
            var mco_id = url.split("mco/")[1].replace("?tab=details.basic", "");
            expect(browser.getCurrentUrl())
                .toEqual(urlsPage.baseUrl + 'changes/mco/' + mco_id + '?tab=details.basic');

        });

    });

});