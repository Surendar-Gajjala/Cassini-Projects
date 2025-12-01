var urlsPage = require('../../../pages/urlsPage');

it('Check Customers tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 10000);
    sideManuButton.click();
    browser.sleep(2000);

    var customersTab = element(by.id('customers'));
    browser.executeScript("arguments[0].scrollIntoView();", customersTab);
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(customersTab), 10000);
    customersTab.click();
    browser.sleep(3000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allCustomers);
    browser.sleep(5000);
});

describe('All Customers page Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allCustomers);
        browser.sleep(5000);
    });

    it('New Customer Button Text and Title test case', function () {
        var customerButton = element(by.id('new-customer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(customerButton), 15000);
        expect(customerButton.getText()).toEqual('New Customer');
        browser.sleep(2000);
        expect(customerButton.getAttribute('title')).toEqual('New Customer');
        browser.sleep(5000);
    });

    it('New Customer Button click -> open new Customer creation Side panel test case', function () {
        var customerButton = element(by.id('new-customer'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(customerButton), 10000);
        customerButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Customer');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close new Customer creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#new-customer').isDisplayed(true));
        browser.sleep(5000);
    });

});

describe('update customer Test cases:', function () {

    it('Actions -> Edit button -> open Update Customer sidepanel test case :', function () {

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

    it('Click Close(X) button -> close Update Customer Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#new-customer').isDisplayed(true));
        browser.sleep(5000);
    });

});


