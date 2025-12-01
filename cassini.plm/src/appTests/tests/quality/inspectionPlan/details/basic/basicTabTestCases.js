var urlsPage = require('../../../../../pages/urlsPage.js');

//---------Click Inspection Plan Number hyperlink -> goto Basic details tab Successfully-------------
it('Click Inspection Plan Number hyperlink -> goto Basic details tab Successfully', function () {
    browser.get(urlsPage.allInspectionPlan);
    browser.sleep(8000);
    var number = element.all(by.css("[ng-click='allInspectionPlansVm.showPlan(inspectionPlan)']")).first();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(number), 15000);
    number.click();
    browser.sleep(5000);

    browser.getCurrentUrl().then(function (url) {
        urlsPage.url_id = url.split("inspectionPlan/")[1].replace("?tab=details.basic","");
        browser.sleep(3000);
        expect(browser.getCurrentUrl())
            .toEqual(urlsPage.baseUrl+'pqm/inspectionPlan/'+urlsPage.url_id+'?tab=details.basic');
    });

    browser.sleep(5000);
});

describe('Inspection Plan Details -> Basic -> Check data update with Cancel option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'pqm/inspectionPlan/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(500);
    });

    it('Name Field', function () {

        var beforeNameValue = element(by.css("[editable-text='planBasicVm.inspectionPlan.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNameValue), 15000);

        var editClick = element(by.css("[editable-text='planBasicVm.inspectionPlan.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Inspection Plan 275')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterNameValue = element(by.css("[editable-text='planBasicVm.inspectionPlan.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNameValue), 15000);

        browser.sleep(1000);
        expect(beforeNameValue.getText()).toEqual(afterNameValue.getText());

    });

    it('Name Field empty -> Click ok button -> error message successful', function () {

        var editClick = element(by.css("[editable-text='planBasicVm.inspectionPlan.name']"));
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

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Name cannot be empty');
        browser.sleep(5000);

    });

    it('Description Field', function () {

        var beforeDescriptionValue = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeDescriptionValue), 15000);

        var editClick = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.description']"));
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
        var afterDescriptionValue = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterDescriptionValue), 15000);

        browser.sleep(1000);
        expect(beforeDescriptionValue.getText()).toEqual(afterDescriptionValue.getText());

    });

    it('Notes Field', function () {

        var beforeNotesValue = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNotesValue), 15000);

        var editClick = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Notes')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterNotesValue = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNotesValue), 15000);

        browser.sleep(1000);
        expect(beforeNotesValue.getText()).toEqual(afterNotesValue.getText());

    });

});

describe('Inspection Plan Details -> Basic -> Check data update with OK option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'pqm/inspectionPlan/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(2000);
    });

    it('Name Field', function () {

        var editClick = element(by.css("[editable-text='planBasicVm.inspectionPlan.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Inspec plan 2027')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Inspection plan updated successfully');
        browser.sleep(5000);

    });

    it('Description Field', function () {

        var editClick = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.description']"));
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

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Inspection plan updated successfully');
        browser.sleep(5000);

    });

    it('Notes Field', function () {

        var editClick = element(by.css("[editable-textarea='planBasicVm.inspectionPlan.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Notes')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Inspection plan updated successfully');
        browser.sleep(5000);
    });

});

