var urlsPage = require('../../../../pages/urlsPage.js');

describe('New Project creation Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allProjects);
        browser.sleep(10000);
        var newProjectButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newProjectButton), 15000);
        newProjectButton.click();
        browser.sleep(5000);
    });


    it('without Name -> create New Project', function () {
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var WarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(WarnMes), 5000, "Text is not something I've expected");
        expect(WarnMes).toEqual('Name cannot be empty');
        browser.sleep(8000);
    });

    it('without Project Manager -> create New Project', function () {
        var name =element(by.model('newProjectVm.project.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 10000);
        name.sendKeys('Project 3');
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var WarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(WarnMes), 5000, "Text is not something I've expected");
        expect(WarnMes).toEqual('Project Manager Cannot be empty');
        browser.sleep(8000);
    });

    it('without Planned Start Date -> create New Project', function () {

        var SelectProjectManager = element(by.model('newProjectVm.project.projectManager'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(SelectProjectManager), 15000);
        SelectProjectManager.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();


        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var WarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(WarnMes), 5000, "Text is not something I've expected");
        expect(WarnMes).toEqual('Please select planned start date');
        browser.sleep(8000);
    });

    it('without Planned Finished Date -> create New Project', function () {
        browser.sleep(2000);
        element(by.model("newProjectVm.project.plannedStartDate")).sendKeys('12/05/2021');
        browser.sleep(2000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var WarnMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(WarnMes), 5000, "Text is not something I've expected");
        expect(WarnMes).toEqual('Please select planned finish date');
        browser.sleep(8000);
    });

    it('Check New Project creation Successful test case', function () {
        browser.get(urlsPage.allProjects);
        browser.sleep(8000);
        var newProjectButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newProjectButton), 15000);
        newProjectButton.click();
        browser.sleep(3000);
      
        var name =element(by.model('newProjectVm.project.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 10000);
        name.sendKeys('Project 3');
        browser.sleep(3000);
    
        var SelectProjectManager = element(by.model('newProjectVm.project.projectManager'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(SelectProjectManager), 15000);
        SelectProjectManager.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
        browser.sleep(3000);
        element(by.model("newProjectVm.project.plannedStartDate")).sendKeys('12/05/2021');
        browser.sleep(3000);
        element(by.model("newProjectVm.project.plannedFinishDate")).sendKeys('12/06/2021');
        browser.sleep(3000);
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 8000);
        createButton.click();
        browser.sleep(5000);

        browser.getCurrentUrl().then(function(url){
            var project_id=url.split("pm/project/details/")[1].replace("?tab=details.plan","");
             expect(browser.getCurrentUrl())
                   .toEqual(urlsPage.baseUrl+'pm/project/details/'+project_id+'?tab=details.plan');
           });
           browser.sleep(10000);
    });

});

