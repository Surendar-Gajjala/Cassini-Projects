var urlsPage = require('../../../pages/urlsPage');

describe('New Customer Test cases:',function(){

    beforeAll(function () {
        browser.get(urlsPage.allCustomers);
        browser.sleep(5000);
    });

    it('Without Name -> create New Customer', function () {
        var newButtonClick = element(by.id('new-customer'));
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
        browser.sleep(5000);
    });

    it('Without First Name -> create New Customer', function () {

        var name = element(by.model('newCustomerVm.newCustomer.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 15000);
        name.sendKeys('Customer 2');
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('First name cannot be empty');
        browser.sleep(5000);
    });

    it('Without E-mail -> create New Customer', function () {

        var firstName = element(by.model('newCustomerVm.newCustomer.person.firstName'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(firstName), 15000);
        firstName.sendKeys('Customer 22');
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter valid E-mail address for Contact Person');
        browser.sleep(5000);

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
    });


    it('New Customer  Creation Successful Test case', function () {
        var newButtonClick = element(by.id('new-customer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 15000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Customer');

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.name').sendKeys('Customer 2774')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.phone').sendKeys('9876543210')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.address').sendKeys('address')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.email').sendKeys('madalasalman95@gmail.com')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.notes').sendKeys('notes')), 30000);

        var sectionTitle = element(by.css('.form-horizontal > .section-title'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sectionTitle), 15000);
        expect(sectionTitle.getText()).toBe('Contact Person');

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.person.firstName').sendKeys('salmana')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.person.lastName').sendKeys('madalaa')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.person.phoneMobile').sendKeys('90339573455')), 30000);

        browser.wait(EC.presenceOf(urlsPage.byModel('newCustomerVm.newCustomer.person.email').sendKeys('madalasalmannbb@gmail.com')), 30000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(4500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Customer saved successfully');
        browser.sleep(5000);
        
    });

});

describe('update validation messages', function () {

    beforeAll(function () {
        var actionButtonClick = element.all(by.css('.fa-ellipsis-h')).get(0);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(actionButtonClick), 10000);
        actionButtonClick.click();

        var editButtonClick = element(by.css('[spellcheck="false"] > .dropdown-menu  [ng-click="customersVm.editCustomer(customer)"]'))
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(editButtonClick), 10000);
        editButtonClick.click();

        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('Update Customer');
        browser.sleep(5000);
    });

    it('without customer Name -> click Update Button ', function () {
        var beforeCustomerName = element(by.model('newCustomerVm.newCustomer.name')).getAttribute('value');
       
        var customerName = element(by.model('newCustomerVm.newCustomer.name'));
        customerName.clear();

        var createButton = element(by.buttonText('Update'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(5000);
        customerName.sendKeys(beforeCustomerName);
        browser.sleep(3000);
    });

    it('without Contact Person -> click Update Button ', function () {
        var beforeContactPerson = element(by.model('newCustomerVm.newCustomer.person.firstName')).getAttribute('value');
       
        var contactPerson = element(by.model('newCustomerVm.newCustomer.person.firstName'));
        contactPerson.clear();

        var createButton = element(by.buttonText('Update'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('First name cannot be empty');
        browser.sleep(5000);

        contactPerson.sendKeys(beforeContactPerson);
        browser.sleep(3000);
    });

    it('without Email -> click Update Button ', function () {
        var beforeEmail = element(by.model('newCustomerVm.newCustomer.person.email')).getAttribute('value');

        var email = element(by.model('newCustomerVm.newCustomer.person.email'));
        email.clear();

        var createButton = element(by.buttonText('Update'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please enter valid E-mail address for Contact Person');
        browser.sleep(5000);

        email.sendKeys(beforeEmail);
        browser.sleep(3000);

        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        closeRightSidePanel.click();
    });

});

it('Update customer Successfully', function () {

    var actionButtonClick = element.all(by.css('.fa-ellipsis-h')).get(0);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(actionButtonClick), 10000);
    actionButtonClick.click();
    browser.sleep(1000);

    var editButtonClick = element(by.css('[spellcheck="false"] > .dropdown-menu  [ng-click="customersVm.editCustomer(customer)"]'))
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(editButtonClick), 10000);
    editButtonClick.click();
    browser.sleep(2000);

    element(by.model('newCustomerVm.newCustomer.name')).clear().sendKeys('customeerrr 6')

    element(by.model('newCustomerVm.newCustomer.person.firstName')).clear().sendKeys('Contact Personnn 6');

    element(by.model('newCustomerVm.newCustomer.person.email')).clear().sendKeys('ContactPersonnn@gmail.com');

    var createButton = element(by.buttonText('Update'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(createButton), 10000);
    createButton.click();
    browser.sleep(1500);
    var alertMessage = element(by.id('alertMessage')).getText();
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
    expect(alertMessage).toEqual('Customer saved successfully');
    browser.sleep(5000);

});