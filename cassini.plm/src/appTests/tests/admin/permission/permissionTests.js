var urlsPage = require('../../../pages/urlsPage.js');

describe('Admin-> Roles: ', function () {

    var permission_id;


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

        var switchToRole = element(by.css("[for='permissions']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(switchToRole), 15000);
        switchToRole.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.adminPermissionsUrl)
    });

    //---------Check New Permission Button Text and Tooltip-----------
    it(" New Permission Button Text and Tooltip testCase ", function () {
        //browser.get(urlsPage.adminPermissionsUrl);
        browser.sleep(1000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Permission');
        expect(newButtonn.getAttribute('title')).toEqual('New Permission');
    });
    //---------Check New Permission button click-----------
    it('Check New Permission button click', function () {
        //browser.get(urlsPage.adminPermissionsUrl);
        browser.sleep(1000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Permission');
    });

    // ----------Check Close(X) button on the New Permission screen-----------
    it("Check Close(X) button on the New Permission screen", function () {
        browser.get(urlsPage.adminPermissionsUrl);
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
        expect(newButton.getText()).toEqual('New Permission');

    });

    //-----------create New Permission without entering mandatory fields data Existing Person is No  ------------
    describe('create New Permission without entering mandatory fields data ', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminPermissionsUrl);
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
            expect(alertMessage).toEqual('Please enter name');
            browser.sleep(5000);
        });


        it('Without select Object Type ', function () {

            var name = element(by.model('newPermissionsVm.permission.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 10000);
            name.sendKeys('Permission 1');
            browser.sleep(2000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual("Please select object type");
            browser.sleep(2000);

        });

       
        it('Without select Privilege', function () {

            var selectProfile = element(by.model('newPermissionsVm.permission.objectType'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectProfile), 15000);
            selectProfile.click();
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
            expect(alertMessage).toEqual("Please select privilege");
            browser.sleep(5000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
        });

    });

    //--------------create New Permission entering mandatory fields data Existing Person is No -------------
    describe('create New Permission entering mandatory fields data ', function () {

        beforeAll(function () {
            browser.get(urlsPage.adminPermissionsUrl);
            browser.sleep(5000);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 5000);
            newButton.click();
            browser.sleep(2000);
        });

        it('New Permission created successfull Test case ', function () {
           
            var name = element(by.model('newPermissionsVm.permission.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 5000);
            name.sendKeys('Permission 1');
            browser.sleep(2000);
            var selectObjectType = element(by.model('newPermissionsVm.permission.objectType'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectObjectType), 5000);
            selectObjectType.click();
            browser.sleep(2000);
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();
            browser.sleep(2000);

            var selectPrivilege = element(by.model('newPermissionsVm.permission.privilege'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectPrivilege), 5000);
            selectPrivilege.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 5000);
            createButton.click();
            browser.sleep(4000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Permission saved successfully');
            browser.sleep(5000);
        });

    });



});