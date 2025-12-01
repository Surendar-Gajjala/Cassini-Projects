var urlsPage = require('../../../../pages/urlsPage');

describe('New Declaration Test cases:',function(){
    beforeAll(function () {
        browser.get(urlsPage.allDeclarations);
        browser.sleep(5000);
    });

    it('Without select Declaration Type -> create New Declaration', function () {
        var newButtonClick = element(by.id('newDeclaration'));
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
        expect(alertMessage).toEqual('Please select type');
        browser.sleep(5000);
    });

    it('Without Name -> create New Declaration', function () {
        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteDeclarationType =  element(by.id('complianceTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteDeclarationType), 15000);
        selecteDeclarationType.click(); 
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(5000);
    });

    it('Without Select supplier -> create New Declaration', function () {

        var name = element(by.model('newDeclarationVm.newDeclaration.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 10000);
        name.sendKeys('Declaration 25');

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Select supplier');
        browser.sleep(5000);
    });

    it('Without Select Contact -> create New Declaration', function () {

        var selectSupplier =  element(by.model('newDeclarationVm.newDeclaration.supplier'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSupplier), 15000);
        selectSupplier.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Select contact');
        browser.sleep(8000);

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
    });


    it('New Declaration Creation Successful Test case', function () {
        var newButtonClick =element(by.id('newDeclaration'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Declaration');


        var selectbuttonClick = element(by.id('Select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        selectbuttonClick.click();

        var selecteDeclarationType =  element(by.id('complianceTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selecteDeclarationType), 15000);
        selecteDeclarationType.click();
        browser.sleep(2000); 
        
        browser.wait(EC.presenceOf(urlsPage.byModel('newDeclarationVm.newDeclaration.name').sendKeys('Declaration 25')), 30000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newDeclarationVm.newDeclaration.description').sendKeys('description')), 30000);

        
        var selectSupplier =  element(by.model('newDeclarationVm.newDeclaration.supplier'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectSupplier), 15000);
        selectSupplier.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        
        var selectContact =  element(by.model('newDeclarationVm.newDeclaration.contact'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectContact), 15000);
        selectContact.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(2500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Declaration created successfully');
        browser.sleep(8000);
    });

});