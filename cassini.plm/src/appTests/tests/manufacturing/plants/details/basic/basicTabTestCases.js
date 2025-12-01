var urlsPage = require('../../../../../pages/urlsPage.js');

//---------Click Plant Number hyperlink -> goto Basic details tab Successfully-------------
it('Click Plant Number hyperlink -> goto Basic details tab Successfully', function () {
    browser.get(urlsPage.allPlants);
    browser.sleep(8000);
    var numberColumn = element.all(by.id('plantNumber')).first();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(numberColumn), 15000);
    numberColumn.click();
    browser.sleep(5000);

    browser.getCurrentUrl().then(function (url) {
        urlsPage.url_id = url.split("plant/")[1].replace("?tab=details.basic", "");
        browser.sleep(3000);
        expect(browser.getCurrentUrl())
            .toEqual(urlsPage.baseUrl + 'mes/plant/' +  urlsPage.url_id + '?tab=details.basic');
    });

    browser.sleep(8000);
});

describe('Check data update with Cancel option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'mes/plant/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(8000);
    });
    beforeEach(function () {
        browser.sleep(1000);
    });

    it('Name Field', function () {

        var beforeNameValue = element(by.css("[ng-bind-html='plantBasicVm.plant.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNameValue), 15000);

        var editClick = element(by.css("[ng-bind-html='plantBasicVm.plant.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Plant 275')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterNameValue = element(by.css("[ng-bind-html='plantBasicVm.plant.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNameValue), 15000);

        browser.sleep(1000);
        expect(beforeNameValue.getText()).toEqual(afterNameValue.getText());

    });

    it('Description Field', function () {

        var beforeDescriptionValue = element(by.css("[editable-textarea='plantBasicVm.plant.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeDescriptionValue), 15000);

        var editClick = element(by.css("[editable-textarea='plantBasicVm.plant.description']"));
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
        var afterDescriptionValue = element(by.css("[editable-textarea='plantBasicVm.plant.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterDescriptionValue), 15000);

        browser.sleep(1000);
        expect(beforeDescriptionValue.getText()).toEqual(afterDescriptionValue.getText());

    });

    it('Address Field', function () {

        var beforeAddressValue = element(by.css("[editable-textarea='plantBasicVm.plant.address']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeAddressValue), 15000);

        var editClick = element(by.css("[editable-textarea='plantBasicVm.plant.address']"));
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
        var afterAddressValue = element(by.css("[editable-textarea='plantBasicVm.plant.address']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterAddressValue), 15000);

        browser.sleep(1000);
        expect(beforeAddressValue.getText()).toEqual(afterAddressValue.getText());

    });

    it('City Field', function () {

        var beforeCityValue = element(by.css("[editable-text='plantBasicVm.plant.city']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeCityValue), 15000);

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.city']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('City')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterCityValue = element(by.css("[editable-text='plantBasicVm.plant.city']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterCityValue), 15000);

        browser.sleep(1000);
        expect(beforeCityValue.getText()).toEqual(afterCityValue.getText());

    });

    it('Country Field', function () {

        var beforeCountryValue = element(by.css("[editable-text='plantBasicVm.plant.country']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeCountryValue), 15000);

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.country']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('India')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterCountryValue = element(by.css("[editable-text='plantBasicVm.plant.country']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterCountryValue), 15000);

        browser.sleep(1000);
        expect(beforeCountryValue.getText()).toEqual(afterCountryValue.getText());

    });

    it('PostalCode Field', function () {

        var beforePostalCodeValue = element(by.css("[editable-text='plantBasicVm.plant.postalCode']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforePostalCodeValue), 15000);

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.postalCode']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('123456')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterPostalCodeValue = element(by.css("[editable-text='plantBasicVm.plant.postalCode']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterPostalCodeValue), 15000);

        browser.sleep(1000);
        expect(beforePostalCodeValue.getText()).toEqual(afterPostalCodeValue.getText());

    });

    it('PhoneNumber Field', function () {

        var beforePhoneNumberValue = element(by.css("[editable-text='plantBasicVm.plant.phoneNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforePhoneNumberValue), 15000);

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.phoneNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('083555555')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterPhoneNumberValue = element(by.css("[editable-text='plantBasicVm.plant.phoneNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterPhoneNumberValue), 15000);

        browser.sleep(1000);
        expect(beforePhoneNumberValue.getText()).toEqual(afterPhoneNumberValue.getText());

    });

    it('MobileNumber Field', function () {

        var beforeMobileNumberValue = element(by.css("[editable-text='plantBasicVm.plant.mobileNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeMobileNumberValue), 15000);

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.mobileNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('9874563214')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterMobileNumberValue = element(by.css("[editable-text='plantBasicVm.plant.mobileNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterMobileNumberValue), 15000);

        browser.sleep(1000);
        expect(beforeMobileNumberValue.getText()).toEqual(afterMobileNumberValue.getText());

    });

    it('FaxAddress Field', function () {

        var beforeFaxAddressValue = element(by.css("[editable-text='plantBasicVm.plant.faxAddress']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeFaxAddressValue), 15000);

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.faxAddress']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Hyderabad fax')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterFaxAddressValue = element(by.css("[editable-text='plantBasicVm.plant.faxAddress']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterFaxAddressValue), 15000);

        browser.sleep(1000);
        expect(beforeFaxAddressValue.getText()).toEqual(afterFaxAddressValue.getText());

    });

    it('Email Field', function () {

        var beforeEmailNumberValue = element(by.css("[editable-text='plantBasicVm.plant.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeEmailNumberValue), 15000);

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('salman@gmail.com')), 35000);

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterEmailNumberValue = element(by.css("[editable-text='plantBasicVm.plant.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterEmailNumberValue), 15000);

        browser.sleep(1000);
        expect(beforeEmailNumberValue.getText()).toEqual(afterEmailNumberValue.getText());

    });



    it('Notes Field', function () {

        var beforeNotesValue = element(by.css("[editable-textarea='plantBasicVm.plant.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforeNotesValue), 15000);

        var editClick = element(by.css("[editable-textarea='plantBasicVm.plant.notes']"));
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
        var afterNotesValue = element(by.css("[editable-textarea='plantBasicVm.plant.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterNotesValue), 15000);

        browser.sleep(1000);
        expect(beforeNotesValue.getText()).toEqual(afterNotesValue.getText());

    });


    it('Plant Manager Field', function () {

        var beforePlantManagerValue = element(by.css("[editable-select='plantBasicVm.plant.plantPersonObject']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(beforePlantManagerValue), 15000);

        var editClick = element(by.css("[editable-select='plantBasicVm.plant.plantPersonObject']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        element(by.tagName("select")).click();
        browser.sleep(3000);
        element.all(by.tagName("option")).get(2).click();

        var cancelClick = element(by.css('span.glyphicon-remove'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(cancelClick), 15000);
        cancelClick.click();

        browser.sleep(1500);
        var afterPlantManagerValue = element(by.css("[editable-select='plantBasicVm.plant.plantPersonObject']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(afterPlantManagerValue), 15000);

        browser.sleep(1000);
        expect(beforePlantManagerValue.getText()).toEqual(afterPlantManagerValue.getText());

    });

});

describe('Check data update with OK option', function () {

    beforeAll(function () {
        browser.get(urlsPage.baseUrl + 'mes/plant/' +  urlsPage.url_id + '?tab=details.basic');
        browser.sleep(5000);
    });
    beforeEach(function () {
        browser.sleep(2000);
    });

    it('Name Field', function () {

        var editClick = element(by.css("[ng-bind-html='plantBasicVm.plant.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Plant 2337')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(2500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

   
    it('Name Field save with empty data', function () {

        var editClick = element(by.css("[ng-bind-html='plantBasicVm.plant.name']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear()), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(2500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Name cannot be empty');

    });


    it('Description Field', function () {

        var editClick = element(by.css("[editable-textarea='plantBasicVm.plant.description']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Description')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3000);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');
    });

    it('Address Field', function () {

        var editClick = element(by.css("[editable-textarea='plantBasicVm.plant.address']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Address')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3000);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('City Field', function () {

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.city']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Guntur')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(3000);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('Country Field', function () {

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.country']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('India')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('PostalCode Field', function () {



        var editClick = element(by.css("[editable-text='plantBasicVm.plant.postalCode']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('123-456')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('PhoneNumber Field', function () {


        var editClick = element(by.css("[editable-text='plantBasicVm.plant.phoneNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('0863 522213')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('MobileNumber Field', function () {

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.mobileNumber']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('9703395735')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('FaxAddress Field', function () {

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.faxAddress']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Suddapalli')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('Email Field', function () {

        var editClick = element(by.css("[editable-text='plantBasicVm.plant.email']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('madalasalmann@gmail.com')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('Notes Field', function () {

        var editClick = element(by.css("[editable-textarea='plantBasicVm.plant.notes']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editClick), 15000);
        editClick.click();
        browser.sleep(1000);

        browser.wait(EC.presenceOf(urlsPage.byModel('$parent.$data').clear().sendKeys('Notes')), 35000);

        var submitClick = element(by.css('span.glyphicon-ok'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(submitClick), 15000);
        submitClick.click();

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');

    });

    it('Plant Manager Field', function () {

        var editClick = element(by.css("[editable-select='plantBasicVm.plant.plantPersonObject']"));
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

        browser.sleep(1500);

        var plantUpdateMes = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(plantUpdateMes), 5000, "Text is not something I've expected");
        expect(plantUpdateMes).toEqual('Plant updated successfully');


    });


});

