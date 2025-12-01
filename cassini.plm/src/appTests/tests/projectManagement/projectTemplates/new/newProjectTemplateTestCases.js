var urlsPage = require('../../../../pages/urlsPage');

describe('New Project Template Test cases:',function(){

    beforeAll(function () {
        browser.get(urlsPage.allProjectTemplates);
        browser.sleep(8000);
    })
    
    it('with out Name -> create New Project Template', function(){
        var newButtonClick =element(by.id('newTemplate'));
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
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(8000);

        var closeButtonClick = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButtonClick), 10000);
        closeButtonClick.click();
        browser.sleep(2000);

    });

    it('New Project Templates Creation Testing', function () {
        var newButtonClick =element(by.id('newTemplate'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Template');
        
       browser.wait(EC.presenceOf(urlsPage.byModel('newTemplateVm.template.name').sendKeys('Template 2')), 30000);

       browser.wait(EC.presenceOf(urlsPage.byModel('newTemplateVm.template.description').sendKeys('description')), 30000);
       
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(2500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Template created successfully');
        browser.sleep(8000);
    });

});