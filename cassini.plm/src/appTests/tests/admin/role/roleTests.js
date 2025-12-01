var urlsPage = require('../../../pages/urlsPage.js');

describe('Admin-> Roles: ', function () {

    var role_id;


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

        var switchToRole = element(by.css("[for='groups']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(switchToRole), 15000);
        switchToRole.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.adminRolesUrl)
    });

    //---------Check New Role Button Text and Tooltip-----------
    it(" New Role Button Text and Tooltip testCase ", function () {
        //browser.get(urlsPage.adminRolesUrl);
        browser.sleep(1000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Role');
        expect(newButtonn.getAttribute('title')).toEqual('New Role');
    });
    //---------Check New Role button click-----------
    it('Check New Role button click', function () {
        //browser.get(urlsPage.adminRolesUrl);
        browser.sleep(1000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Role');
    });

    // ----------Check Close(X) button on the New Role screen-----------
    it("Check Close(X) button on the New Role screen", function () {
        browser.get(urlsPage.adminRolesUrl);
        browser.sleep(2000);

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
        expect(newButton.getText()).toEqual('New Role');

    });

    //-----------create new user without entering mandatory fields data Existing Person is No  ------------
    describe('create new Role without entering mandatory fields data Existing Person is No ', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminRolesUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
        })

        it('Without Name', function () {
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Name cannot be empty');
            browser.sleep(5000);
        });

        
        it('Without select profile ', function () {

            var name = element(by.model('newGroupVm.personGroup.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 10000);
            name.sendKeys('name 1');
            browser.sleep(2000);


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual("Please select profile");
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

       

    });

    //--------------create new user entering mandatory fields data Existing Person is No -------------
    describe('create new Role entering mandatory fields data Existing Person is No', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminRolesUrl);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(2000);
        });

        it('New Role created successfull Test case ', function () {

            var name = element(by.model('newGroupVm.personGroup.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 10000);
            name.sendKeys('manager');
            browser.sleep(2000);

            var description = element(by.model('newGroupVm.personGroup.description'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(description), 10000);
            description.sendKeys('description');
            browser.sleep(2000);

            var selectProfile = element(by.model('newGroupVm.personGroup.profile'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectProfile), 15000);
            selectProfile.click();
            browser.sleep(1000);
            element.all(by.css('.ui-select-choices-row-inner div')).last().click();


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(3000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Role created successfully');
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });

    });

   

});