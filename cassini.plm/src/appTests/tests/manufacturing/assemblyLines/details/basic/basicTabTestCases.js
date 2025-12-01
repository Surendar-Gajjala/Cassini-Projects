var urlsPage = require('../../../../../pages/urlsPage.js');

//---------Click Assembly Line Number hyperlink -> goto Basic details tab Successfully-------------
it('Click Assembly Line Number hyperlink -> goto Basic details tab Successfully', function () {
    browser.get(urlsPage.allAssembliLines);
    browser.sleep(8000);
    var number = element.all(by.css("[ng-click='allAssemblyLineVm.showAssemblyLine(assemblyLine)']")).first();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(number), 15000);
    number.click();
    browser.sleep(5000);

    browser.getCurrentUrl().then(function (url) {
        urlsPage.url_id = url.split("assemblyline/")[1].replace("?tab=details.basic", "");
        browser.sleep(3000);
        expect(browser.getCurrentUrl())
            .toEqual(urlsPage.baseUrl + 'mes/assemblyline/' +  urlsPage.url_id + '?tab=details.basic');
    });

    browser.sleep(5000);
});

describe('Check data update with Cancel option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'mes/assemblyline/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(500);
    });

    it('Name Field', function () {

        var beforeNameValue = element(by.css("[ng-bind-html='assemblyLineBasicVm.assemblyLine.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNameValue), 15000);

        var editClick = element(by.css("[ng-bind-html='assemblyLineBasicVm.assemblyLine.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Assembly Line 275')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterNameValue = element(by.css("[ng-bind-html='assemblyLineBasicVm.assemblyLine.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNameValue), 15000);

        browser.sleep(1000);
        expect(beforeNameValue.getText()).toEqual(afterNameValue.getText());

    });

    it('Name Field empty -> Click ok button -> error message successful', function () {

        var beforeNameValue = element(by.css("[ng-bind-html='assemblyLineBasicVm.assemblyLine.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNameValue), 15000);

        var editClick = element(by.css("[ng-bind-html='assemblyLineBasicVm.assemblyLine.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear()), 35000);

        var clickOk = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickOk), 15000);
        clickOk.click();
        browser.sleep(3500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Name cannot be empty');
        browser.sleep(5000);

    });

    it('Description Field', function () {

        var beforeDescriptionValue = element(by.css("[editable-textarea='assemblyLineBasicVm.assemblyLine.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeDescriptionValue), 15000);

        var editClick = element(by.css("[editable-textarea='assemblyLineBasicVm.assemblyLine.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Description')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterDescriptionValue = element(by.css("[editable-textarea='assemblyLineBasicVm.assemblyLine.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterDescriptionValue), 15000);

        browser.sleep(1000);
        expect(beforeDescriptionValue.getText()).toEqual(afterDescriptionValue.getText());

    });

});

describe('Check data update with OK option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'mes/assemblyline/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(2000);
    });

    it('Name Field', function () {

        var editClick = element(by.css("[ng-bind-html='assemblyLineBasicVm.assemblyLine.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Assembly Line 27')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Assembly line updated successfully');
        browser.sleep(5000);

    });

    it('Description Field', function () {

        var editClick = element(by.css("[editable-textarea='assemblyLineBasicVm.assemblyLine.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Description')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Assembly line updated successfully');
        browser.sleep(5000);

    });


});

