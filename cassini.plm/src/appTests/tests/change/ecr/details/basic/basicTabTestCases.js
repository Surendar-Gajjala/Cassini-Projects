var urlsPage = require('../../../../../pages/urlsPage.js');

//---------Click Ecr Number hyperlink -> goto Basic details tab Successfully-------------
it('Click Ecr Number hyperlink -> goto Basic details tab Successfully', function () {
    browser.get(urlsPage.allEcrs);
    browser.sleep(8000);
    var number = element.all(by.css("[ng-click='ecrsVm.showEcr(ecr)']")).first();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(number), 15000);
    number.click();
    browser.sleep(5000);

    browser.getCurrentUrl().then(function (url) {
        urlsPage.url_id = url.split("ecr/")[1].replace("?tab=details.basic","");
        browser.sleep(3000);
        expect(browser.getCurrentUrl())
            .toEqual(urlsPage.baseUrl+'changes/ecr/'+urlsPage.url_id+'?tab=details.basic');
    });

    browser.sleep(5000);
});

xdescribe('ECR Details -> Basic ->Check data update with Cancel option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'changes/ecr/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(500);
    });

    it('Title Field', function () {

        var beforeTitleValue = element(by.css("[editable-text='ecrBasicVm.ecr.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeTitleValue), 15000);

        var editClick = element(by.css("[editable-text='ecrBasicVm.ecr.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Ecr 275')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterTitleValue = element(by.css("[editable-text='ecrBasicVm.ecr.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterTitleValue), 15000);

        browser.sleep(1000);
        expect(beforeTitleValue.getText()).toEqual(afterTitleValue.getText());

    });

    it('Title Field empty -> Click ok button -> error message successful', function () {

     
        var editClick = element(by.css("[editable-text='ecrBasicVm.ecr.title']"));
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

    it('Description Of Change Field', function () {

        var beforeDescriptionValue = element(by.css("[editable-textarea='ecrBasicVm.ecr.descriptionOfChange']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeDescriptionValue), 15000);

        var editClick = element(by.css("[editable-textarea='ecrBasicVm.ecr.descriptionOfChange']"));
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
        var afterDescriptionValue = element(by.css("[editable-textarea='ecrBasicVm.ecr.descriptionOfChange']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterDescriptionValue), 15000);

        browser.sleep(1000);
        expect(beforeDescriptionValue.getText()).toEqual(afterDescriptionValue.getText());

    });

    it('Reason for Change Field', function () {

        var beforeReasonForChangeValue = element(by.css("[editable-textarea='ecrBasicVm.ecr.reasonForChange']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeReasonForChangeValue), 15000);

        var editClick = element(by.css("[editable-textarea='ecrBasicVm.ecr.reasonForChange']"));
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
        var afterReasonForChangeValue = element(by.css("[editable-textarea='ecrBasicVm.ecr.reasonForChange']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterReasonForChangeValue), 15000);

        browser.sleep(1000);
        expect(beforeReasonForChangeValue.getText()).toEqual(afterReasonForChangeValue.getText());

    });

    it('Proposed Changes Field', function () {

        var beforeProposedChangeValue = element(by.css("[editable-textarea='ecrBasicVm.ecr.proposedChanges']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeProposedChangeValue), 15000);

        var editClick = element(by.css("[editable-textarea='ecrBasicVm.ecr.proposedChanges']"));
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
        var afterProposedChangeValue = element(by.css("[editable-textarea='ecrBasicVm.ecr.proposedChanges']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterProposedChangeValue), 15000);

        browser.sleep(1000);
        expect(beforeProposedChangeValue.getText()).toEqual(afterProposedChangeValue.getText());

    });

    it('Change Analyst Field', function () {

        var beforeChangeAnalystValue = element(by.css("[editable-select='ecrBasicVm.ecr.changeAnalystObject']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeChangeAnalystValue), 15000);

        var editClick = element(by.css("[editable-select='ecrBasicVm.ecr.changeAnalystObject']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        element(by.tagName("select")).click();
        browser.sleep(3000);
        element.all(by.tagName("option")).get(0).click();

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(2500);

        var afterChangeAnalystValue = element(by.css("[editable-select='ecrBasicVm.ecr.changeAnalystObject']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterChangeAnalystValue), 15000);

        browser.sleep(1000);
        expect(beforeChangeAnalystValue.getText()).toEqual(afterChangeAnalystValue.getText());

    });


});

xdescribe('ECR Details -> Basic -> Check data update with OK option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'changes/ecr/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(2000);
    });

    it('Title Field', function () {

        var editClick = element(by.css("[editable-text='ecrBasicVm.ecr.title']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Ecr 27')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('ECR updated successfully');
        browser.sleep(5000);

    });

    it('Description Field', function () {

        var editClick = element(by.css("[editable-textarea='ecrBasicVm.ecr.descriptionOfChange']"));
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
        expect(alertMessage).toEqual('ECR updated successfully');
        browser.sleep(5000);

    });

    it('Reason for Change Field', function () {

        var editClick = element(by.css("[editable-textarea='ecrBasicVm.ecr.reasonForChange']"));
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
        expect(alertMessage).toEqual('ECR updated successfully');
        browser.sleep(5000);

    });

    it('Proposed Changes Field', function () {

        var editClick = element(by.css("[editable-textarea='ecrBasicVm.ecr.proposedChanges']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('proposedChanges')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('ECR updated successfully');
        browser.sleep(5000);

    });


    it('Change Analyst Field', function () {

        var editClick = element(by.css("[editable-select='ecrBasicVm.ecr.changeAnalystObject']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        element(by.tagName("select")).click();
        browser.sleep(3000);
        element.all(by.tagName("option")).get(0).click();

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('ECR updated successfully');
        browser.sleep(5000);

    });


});

