var urlsPage = require('../../../../../../pages/urlsPage.js');

//---------Click Material Inspection Number hyperlink -> goto Basic details tab Successfully-------------
it('Click Material Inspection Number hyperlink -> goto Basic details tab Successfully', function () {
    browser.get(urlsPage.allInspections);
    browser.sleep(4000);
    var selectMaterial = element(by.id('materialType'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(selectMaterial), 15000);
    selectMaterial.click();
    browser.sleep(8000);
    urlsPage.url_id = null;
    browser.sleep(2000);
    var number = element.all(by.css("[ng-click='allInspectionsVm.showInspection(inspection)']")).first();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(number), 15000);
    number.click();
    browser.sleep(5000);
    browser.getCurrentUrl().then(function (url) {
        urlsPage.url_id = url.split("inspections/")[1].replace("?tab=details.basic","");
        browser.sleep(3000);
        expect(browser.getCurrentUrl())
            .toEqual(urlsPage.baseUrl+'pqm/inspections/'+urlsPage.url_id+'?tab=details.basic');
    });

    browser.sleep(5000);
});

describe('Material Inspection Details -> Basic -> Check data update with Cancel option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'pqm/inspections/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(500);
    });

    
    it('deviation Summary Field', function () {

        var beforeDeviationSummaryValue = element(by.css("[editable-textarea='inspectionBasicVm.inspection.deviationSummary']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeDeviationSummaryValue), 15000);

        var editClick = element(by.css("[editable-textarea='inspectionBasicVm.inspection.deviationSummary']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('deviationSummary')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterDeviationSummaryValue = element(by.css("[editable-textarea='inspectionBasicVm.inspection.deviationSummary']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterDeviationSummaryValue), 15000);

        browser.sleep(1000);
        expect(beforeDeviationSummaryValue.getText()).toEqual(afterDeviationSummaryValue.getText());

    });

    it('Notes Field', function () {

        var beforeNotesValue = element(by.css("[editable-textarea='inspectionBasicVm.inspection.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNotesValue), 15000);

        var editClick = element(by.css("[editable-textarea='inspectionBasicVm.inspection.notes']"));
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
        var afterNotesValue = element(by.css("[editable-textarea='inspectionBasicVm.inspection.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNotesValue), 15000);

        browser.sleep(1000);
        expect(beforeNotesValue.getText()).toEqual(afterNotesValue.getText());

    });

});

describe('Material Inspection Details -> Basic -> Check data update with OK option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'pqm/inspections/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(2000);
    });

    
    it('Deviation Summary Field', function () {

        var editClick = element(by.css("[editable-textarea='inspectionBasicVm.inspection.deviationSummary']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Material deviationSummary')), 35000);
        browser.sleep(1000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Inspection updated successfully');
        browser.sleep(5000);

    });

    it('Notes Field', function () {

        var editClick = element(by.css("[editable-textarea='inspectionBasicVm.inspection.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Material Notes')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Inspection updated successfully');
        browser.sleep(5000);
    });

});

it('Material Inspection Details -> Basic -> Click on Inspection Plan -> go to the Inspection plan Basic details  page',function() {

    var InspectionPlan = element(by.css('[title="Click to show details"]'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(InspectionPlan), 15000);
    browser.sleep(2000);
    expect(InspectionPlan.getAttribute('title')).toEqual('Click to show details');
    browser.sleep(2000);
    InspectionPlan.click();
    browser.sleep(5000);

    expect(browser.getCurrentUrl()).toContain(urlsPage.baseUrl+'pqm/inspectionPlan/');
    browser.sleep(5000);
    browser.navigate().back();
    browser.sleep(5000);
});