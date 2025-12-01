var urlsPage = require('../../../../pages/urlsPage.js');

//---------Click Customer Number hyperlink -> goto Basic details tab Successfully-------------
it('Click Customer Number hyperlink -> goto Basic details tab Successfully', function () {
    browser.get(urlsPage.allCustomers);
    browser.sleep(8000);
    var number = element.all(by.css("[ng-click='customersVm.showCustomer(customer)']")).first();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(number), 15000);
    number.click();
    browser.sleep(5000);

    browser.getCurrentUrl().then(function (url) {
        urlsPage.url_id = url.split("customers/")[1].replace("?tab=details.basic","");
        browser.sleep(3000);
        expect(browser.getCurrentUrl())
            .toEqual(urlsPage.baseUrl+'customers/'+urlsPage.url_id+'?tab=details.basic');
    });

    browser.sleep(5000);
});

describe('Customer Details -> Basic -> Check data update with Cancel option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'customers/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(500);
    });

    it('Name Field', function () {

        var beforeNameValue = element(by.css("[editable-text='customerBasicVm.customer.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNameValue), 15000);

        var editClick = element(by.css("[editable-text='customerBasicVm.customer.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Customer 275')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterNameValue = element(by.css("[editable-text='customerBasicVm.customer.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNameValue), 15000);

        browser.sleep(1000);
        expect(beforeNameValue.getText()).toEqual(afterNameValue.getText());

    });

    it('Name Field empty -> Click ok button -> error message successful', function () {

     
        var editClick = element(by.css("[editable-text='customerBasicVm.customer.name']"));
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

    it('Phone Number Field', function () {

        var beforePhoneNumberValue = element(by.css("[editable-text='customerBasicVm.customer.phone']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforePhoneNumberValue), 15000);

        var editClick = element(by.css("[editable-text='customerBasicVm.customer.phone']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('123456789')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterPhoneNumberValue = element(by.css("[editable-text='customerBasicVm.customer.phone']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterPhoneNumberValue), 15000);

        browser.sleep(1000);
        expect(beforePhoneNumberValue.getText()).toEqual(afterPhoneNumberValue.getText());

    });

    it('Email Field', function () {

        var beforeEmailValue = element(by.css("[editable-text='customerBasicVm.customer.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeEmailValue), 15000);

        var editClick = element(by.css("[editable-text='customerBasicVm.customer.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('mad@gmail.com')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterEmailValue = element(by.css("[editable-text='customerBasicVm.customer.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterEmailValue), 15000);

        browser.sleep(1000);
        expect(beforeEmailValue.getText()).toEqual(afterEmailValue.getText());

    });

    it('Address Field', function () {

        var beforeAddressValue = element(by.css("[editable-textarea='customerBasicVm.customer.address']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeAddressValue), 15000);

        var editClick = element(by.css("[editable-textarea='customerBasicVm.customer.address']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Address')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterAddressValue = element(by.css("[editable-textarea='customerBasicVm.customer.address']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterAddressValue), 15000);

        browser.sleep(1000);
        expect(beforeAddressValue.getText()).toEqual(afterAddressValue.getText());

    });

    it('Notes Field', function () {

        var beforeNotesValue = element(by.css("[editable-textarea='customerBasicVm.customer.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNotesValue), 15000);

        var editClick = element(by.css("[editable-textarea='customerBasicVm.customer.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('notes')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterNotesValue = element(by.css("[editable-textarea='customerBasicVm.customer.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNotesValue), 15000);

        browser.sleep(1000);
        expect(beforeNotesValue.getText()).toEqual(afterNotesValue.getText());

    });

});

describe('Customer Details -> Basic -> Check data update with Submit option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'customers/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(2000);
    });

    it('Name Field', function () {

        var editClick = element(by.css("[editable-text='customerBasicVm.customer.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Customer 27')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Customer updated successfully');
        browser.sleep(5000);

    });

    it('Phone Number Field', function () {

        var editClick = element(by.css("[editable-text='customerBasicVm.customer.phone']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('9874563214')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Customer updated successfully');
        browser.sleep(5000);

    });

    it('Email Field', function () {

        var editClick = element(by.css("[editable-text='customerBasicVm.customer.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('salll@gmail.com')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Customer updated successfully');
        browser.sleep(5000);

    });

    it('Address Field', function () {

        var editClick = element(by.css("[editable-textarea='customerBasicVm.customer.address']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('address')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Customer updated successfully');
        browser.sleep(5000);

    });

    it('Notes Field', function () {

        var editClick = element(by.css("[editable-textarea='customerBasicVm.customer.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('notes')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3500);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Customer updated successfully');
        browser.sleep(5000);

    });

});