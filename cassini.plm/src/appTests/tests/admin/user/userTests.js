var urlsPage = require('../../../pages/urlsPage.js');

describe('Admin-> Users: ', function () {

    var user_id;

    //---------Check Admin tab in the side navigation-------
    it('Check Admin tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var adminButton = element(by.id('nav-admin'));
        browser.executeScript("arguments[0].scrollIntoView();", adminButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(adminButton), 15000);
        adminButton.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.adminUsersUrl)
    });

    //---------Check New User Button Text and Tooltip-----------
    it(" New User Button Text and Tooltip testCase ", function () {
        //browser.get(urlsPage.adminUsersUrl);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New User');
        expect(newButtonn.getAttribute('title')).toEqual('New User');
    });
    //---------Check New User button click-----------
    it('Check New User button click', function () {
        //browser.get(urlsPage.adminUsersUrl);
        browser.sleep(2000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New User');
    });

    // ----------Check Close(X) button on the New User screen-----------
    it("Check Close(X) button on the New User screen", function () {
        browser.get(urlsPage.adminUsersUrl);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New User');

    });

    //-----------create new user without entering mandatory fields data Existing Person is No  ------------
    describe('create new user without entering mandatory fields data Existing Person is No ', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
        })

        it('Without firstname', function () {
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

        it('Without Username ', function () {

            var firstName = element(by.model('newUserVm.newPerson.firstName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(firstName), 10000);
            firstName.sendKeys('john');
            browser.sleep(2000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User name cannot be empty');
            browser.sleep(5000);
        });

        it('Without Default Role ', function () {

            var username = element(by.model('newUserVm.newLogin.loginName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(username), 10000);
            username.sendKeys('john');
            browser.sleep(2000);


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual("Please select user's default role");
            browser.sleep(5000);
        });

        it('Without E-mail', function () {

            var selectDefaultGroup = element(by.model('newUserVm.newPerson.defaultGroup'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectDefaultGroup), 15000);
            selectDefaultGroup.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).last().click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('E-mail cannot be empty');
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

    });

    //--------------create new user entering mandatory fields data Existing Person is No -------------
    describe('create new user entering mandatory fields data Existing Person is No', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
        });

        it('New User created successfull Test case ', function () {

            var firstName = element(by.model('newUserVm.newPerson.firstName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(firstName), 10000);
            firstName.sendKeys('john');
            browser.sleep(2000);

            var username = element(by.model('newUserVm.newLogin.loginName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(username), 10000);
            username.sendKeys('johnnxzxzn');
            browser.sleep(2000);

            var selectDefaultGroup = element(by.model('newUserVm.newPerson.defaultGroup'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectDefaultGroup), 15000);
            selectDefaultGroup.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).last().click();


            var username = element(by.model('newUserVm.newPerson.email'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(username), 10000);
            username.sendKeys('johnnxzan@gmail.com');
            browser.sleep(2000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(6000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User created successfully');
            browser.sleep(10000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

    });

    //--------------create new user without entering mandatory fields data  Existing Person is Yes and PersonType is customer " ---------
    describe('create new user without entering mandatory fields data Existing Person is Yes and PersonType is customer', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
        });

        it('Without Customer Contact', function () {

            var existingPersonClick = element(by.css("[for='newPerson']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(existingPersonClick), 10000);
            existingPersonClick.click();
            browser.sleep(5000);

            var selectPersonType = element(by.css("[for='customer']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectPersonType), 10000);
            selectPersonType.click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select customer');
            browser.sleep(10000);
        });

        it('Without Username ', function () {

            var selectCustomerContact = element(by.id('selectCustomer'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectCustomerContact), 15000);
            selectCustomerContact.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).last().click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User name cannot be empty');
            browser.sleep(10000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

    });
    //--------------create new user  entering mandatory fields data  Existing Person is Yes and Person Type is customer -------------
    describe('create new user  entering mandatory fields data Existing Person is Yes and PersonType is customer', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
        })

        it('create new User successfully test case ', function () {

            var existingPersonClick = element(by.css("[for='newPerson']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(existingPersonClick), 10000);
            existingPersonClick.click();
            browser.sleep(5000);
            var selectPersonType = element(by.css("[for='customer']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectPersonType), 10000);
            selectPersonType.click();
            
            var selectCustomerContact = element(by.id('selectCustomer'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectCustomerContact), 15000);
            selectCustomerContact.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).last().click();

            var username = element(by.model('newUserVm.newLogin.loginName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(username), 10000);
            username.sendKeys('abc');
            browser.sleep(2000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(6000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User created successfully');
            browser.sleep(10000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });


    });

    // ----------------create new user without entering mandatory fields data  Existing Person is Yes and Person Type is supplierContact  ---------
    describe('create new user without entering mandatory fields data Existing Person is Yes and Person Type is supplierContact', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);

            var existingPersonClick = element(by.css("[for='newPerson']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(existingPersonClick), 10000);
            existingPersonClick.click();
            browser.sleep(2000);
            var selectPersonType = element(by.css("[for='supplierContact']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectPersonType), 10000);
            selectPersonType.click();
            browser.sleep(4000);

        })

        it('Without supplierContact', function () {
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select supplier contact');
            browser.sleep(10000);
        });

        it('Without Username ', function () {

            var selectSupplierContact = element(by.id('selectSupplierContact'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectSupplierContact), 15000);
            selectSupplierContact.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User name cannot be empty');
            browser.sleep(10000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

       

    });
    //----------------create new user  entering mandatory fields data Existing Person is Yes and Person Type is supplierContact  -----------
    describe('create new user  entering mandatory fields data Existing Person is Yes and Person Type is supplierContact ', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
        })

        it('create new User successfully test case ', function () {
            
            var existingPersonClick = element(by.css("[for='newPerson']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(existingPersonClick), 10000);
            existingPersonClick.click();
            
            var selectPersonType = element(by.css("[for='supplierContact']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectPersonType), 10000);
            selectPersonType.click();
            browser.sleep(4000);

            var selectSupplierContact = element(by.id('selectSupplierContact'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectSupplierContact), 15000);
            selectSupplierContact.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).last().click();

            var username = element(by.model('newUserVm.newLogin.loginName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(username), 10000);
            username.sendKeys('salmann');
            browser.sleep(2000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(6000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User created successfully');
            browser.sleep(10000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });


    });

 //----------------create new user without entering mandatory fields data " Existing Person = Yes , Person Type = manPowers " -----------
    describe('create new user without entering mandatory fields data Existing Person is Yes and Person Type is manPowers ', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);

            
            var existingPersonClick = element(by.css("[for='newPerson']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(existingPersonClick), 10000);
            existingPersonClick.click();
            browser.sleep(5000);
            var selectPersonType = element(by.css("[for='manPowers']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectPersonType), 10000);
            selectPersonType.click();
            browser.sleep(5000);
           
        })

        it('Without Manpower', function () {
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(3500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select manpower');
             browser.sleep(10000);
        });

        it('Without Username ', function () {

            var selectManpower = element(by.id('selectManpower'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectManpower), 15000);
            selectManpower.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(3500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User name cannot be empty');
            browser.sleep(10000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

        

    });
 //----------------create new user  entering mandatory fields data " Existing Person = Yes , Person Type = manPowers " -----------
    describe('create new user  entering mandatory fields data Existing Person is Yes and Person Type is manPowers', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminUsersUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
           
        })

        it('create new User successfully test case ', function () {

            var existingPersonClick = element(by.css("[for='newPerson']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(existingPersonClick), 10000);
            existingPersonClick.click();
            browser.sleep(5000);
            var selectPersonType = element(by.css("[for='manPowers']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectPersonType), 10000);
            selectPersonType.click();
            browser.sleep(5000);
           
            var selectManpower = element(by.id('selectManpower'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectManpower), 15000);
            selectManpower.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var username = element(by.model('newUserVm.newLogin.loginName'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(username), 10000);
            username.sendKeys('salmannaaadsedw');
            browser.sleep(2000);


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(6000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('User created successfully');
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });
    });

});