var urlsPage = require('../../../../../../pages/urlsPage.js');

//---------Click Ecr Number hyperlink -> goto Basic details tab Successfully-------------
it('Click Deviation Number hyperlink -> goto Basic details tab Successfully', function () {
    browser.get(urlsPage.allDeviation);
    browser.sleep(8000);
    var number = element.all(by.css("[ng-click='allVarianceVm.showVariance(variance)']")).first();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(number), 15000);
    number.click();
    browser.sleep(5000);

    browser.getCurrentUrl().then(function (url) {
        urlsPage.url_id = url.split("variance/")[1].replace("?tab=details.basic","");
        browser.sleep(3000);
        expect(browser.getCurrentUrl())
            .toEqual(urlsPage.baseUrl+'changes/variance/'+urlsPage.url_id+'?tab=details.basic');
    });

    browser.sleep(5000);
});

describe(' Deviation Details ->  Basic -> Check data update with Cancel option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'changes/variance/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(500);
    });

    it('Title Field', function () {

        var beforeTitleValue = element(by.css("[editable-text='varianceBasicVm.variance.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeTitleValue), 15000);

        var editClick = element(by.css("[editable-text='varianceBasicVm.variance.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Deviation 275')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterTitleValue = element(by.css("[editable-text='varianceBasicVm.variance.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterTitleValue), 15000);

        browser.sleep(1000);
        expect(beforeTitleValue.getText()).toEqual(afterTitleValue.getText());

    });

    it('Title Field empty -> Click ok button -> error message successful', function () {

     
        var editClick = element(by.css("[editable-text='varianceBasicVm.variance.title']"));
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
        expect(alertMessage).toEqual('Title cannot be empty');
        browser.sleep(5000);

    });

   
    it('Description Field', function () {

        var beforeDescriptionValue = element(by.css("[editable-textarea='varianceBasicVm.variance.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeDescriptionValue), 15000);

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('description')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterDescriptionValue = element(by.css("[editable-textarea='varianceBasicVm.variance.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterDescriptionValue), 15000);

        browser.sleep(1000);
        expect(beforeDescriptionValue.getText()).toEqual(afterDescriptionValue.getText());

    });

    it('Reason for Change Field', function () {

        var beforeReasonForChangeValue = element(by.css("[editable-textarea='varianceBasicVm.variance.reasonForVariance']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeReasonForChangeValue), 15000);

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.reasonForVariance']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('reasonForChange')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterReasonForChangeValue = element(by.css("[editable-textarea='varianceBasicVm.variance.reasonForVariance']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterReasonForChangeValue), 15000);

        browser.sleep(1000);
        expect(beforeReasonForChangeValue.getText()).toEqual(afterReasonForChangeValue.getText());

    });

    it('Current Requirement  Field', function () {

        var beforeCurrentReqValue = element(by.css("[editable-textarea='varianceBasicVm.variance.currentRequirement']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeCurrentReqValue), 15000);

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.currentRequirement']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('currentRequirement')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterCurrentReqValue = element(by.css("[editable-textarea='varianceBasicVm.variance.currentRequirement']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterCurrentReqValue), 15000);

        browser.sleep(1000);
        expect(beforeCurrentReqValue.getText()).toEqual(afterCurrentReqValue.getText());

    });

    it('Requirement Deviation Field', function () {

        var beforeRequirementDeviationValue = element(by.css("[editable-textarea='varianceBasicVm.variance.requirementDeviation']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeRequirementDeviationValue), 15000);

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.requirementDeviation']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('requirementDeviation')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterRequirementDeviationValue = element(by.css("[editable-textarea='varianceBasicVm.variance.requirementDeviation']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterRequirementDeviationValue), 15000);

        browser.sleep(1000);
        expect(beforeRequirementDeviationValue.getText()).toEqual(afterRequirementDeviationValue.getText());

    });

    it('Effective from Field', function () {

        var beforeEffectiveFromValue = element(by.css('[ng-click="varianceBasicVm.editEffectiveFrom = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeEffectiveFromValue), 15000);

        var editClick = element(by.css('[ng-click="varianceBasicVm.editEffectiveFrom = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        var effectiveTo= element(by.model('varianceBasicVm.variance.effectiveFrom'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(effectiveTo), 15000);
        effectiveTo.click();
        browser.executeScript("document.getElementById('effectiveFrom').value='18/06/2021'");
        browser.sleep(1000);

        var cancelEffectiveFrom = element(by.id('cancelEffectiveFrom'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelEffectiveFrom), 15000);
        cancelEffectiveFrom.click();

        browser.sleep(1500);
        var afterEffectiveFromValue = element(by.css('[ng-click="varianceBasicVm.editEffectiveFrom = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterEffectiveFromValue), 15000);

        browser.sleep(1000);
        expect(beforeEffectiveFromValue.getText()).toEqual(afterEffectiveFromValue.getText());

    });

    it('Effective To Field', function () {

        var beforeEffectiveToValue = element(by.css('[ng-click="varianceBasicVm.editEffectiveTo = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeEffectiveToValue), 15000);

        var editClick = element(by.css('[ng-click="varianceBasicVm.editEffectiveTo = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        var effectiveTo= element(by.model('varianceBasicVm.variance.effectiveTo'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(effectiveTo), 15000);
        effectiveTo.click();
        browser.executeScript("document.getElementById('effectiveTo').value='18/07/2021'");
        browser.sleep(1000);

        var cancelClick = element.all(by.css('.fa-times')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterEffectiveToValue = element(by.css('[ng-click="varianceBasicVm.editEffectiveTo = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterEffectiveToValue), 15000);

        browser.sleep(1000);
        expect(beforeEffectiveToValue.getText()).toEqual(afterEffectiveToValue.getText());

    });

   

});

describe(' Deviation Details -> Basic -> Check data update with OK option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'changes/variance/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(2000);
    });

    it('Title Field', function () {

        var editClick = element(by.css("[editable-text='varianceBasicVm.variance.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Deviation 27')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Deviation updated successfully');
        browser.sleep(5000);

    });

    it('Description  Field', function () {

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('description')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Deviation updated successfully');
        browser.sleep(5000);

    });

    it('Reason for Change Field', function () {

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.reasonForVariance']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('reasonForChange')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Deviation updated successfully');
        browser.sleep(5000);

    });

    it('Current Requirement  Field', function () {

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.currentRequirement']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('currentRequirement')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Deviation updated successfully');
        browser.sleep(5000);

    });

    it('Requirement Deviation Field', function () {

        var editClick = element(by.css("[editable-textarea='varianceBasicVm.variance.requirementDeviation']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('requirementDeviation')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Deviation updated successfully');
        browser.sleep(5000);

    });

    it('Effective from Field', function () {

        var editClick = element(by.css('[ng-click="varianceBasicVm.editEffectiveFrom = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        var effectiveFrom= element(by.model('varianceBasicVm.variance.effectiveFrom'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(effectiveFrom), 15000);
        effectiveFrom.click();
        browser.executeScript("document.getElementById('effectiveFrom').value='18/06/2021'");
        browser.sleep(1000);
        

        var submitClick = element(by.css('[ng-click="varianceBasicVm.updateEffectiveFrom()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Deviation updated successfully');
        browser.sleep(5000);

    });

    it('Effective To Field', function () {

        var editClick = element(by.css('[ng-click="varianceBasicVm.editEffectiveTo = true"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        var effectiveTo= element(by.model('varianceBasicVm.variance.effectiveTo'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(effectiveTo), 15000);
        effectiveTo.click();
        browser.executeScript("document.getElementById('effectiveTo').value='18/07/2021'");
        browser.sleep(1000);

        var submitClick = element(by.css('[ng-click="varianceBasicVm.updateEffectiveTo()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Deviation updated successfully');
        browser.sleep(5000);

    });

});

