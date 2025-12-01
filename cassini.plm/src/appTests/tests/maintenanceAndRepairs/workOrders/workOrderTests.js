var urlsPage = require('../../../pages/urlsPage.js');

describe('Work Order: ', function () {

    var workOrder_id;

    //---------Check Work Orders tab in the side navigation-------
    it('Check Work Orders tab in the side navigation', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();

        var maintenanceAndRepairButton = element(by.id('maintenanceAndRepairs'));
        browser.executeScript("arguments[0].scrollIntoView();", maintenanceAndRepairButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(maintenanceAndRepairButton), 15000);
        maintenanceAndRepairButton.click();

        var sidePanelButton = element(by.id('workOrders'));
        browser.executeScript("arguments[0].scrollIntoView();", sidePanelButton);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sidePanelButton), 25000);
        sidePanelButton.click();
        browser.sleep(2000);
        expect(browser.getCurrentUrl()).toBe(urlsPage.allWorkOrders);
        browser.sleep(4000);
    });

     //---------Check New Work Order Button Text and Tooltip-----------
     it(" New Work Order Button Text and Tooltip testCase ", function () {
        //browser.get(urlsPage.allWorkOrders);
        browser.sleep(2000);
        var newButtonn = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Work Order');
        expect(newButtonn.getAttribute('title')).toEqual('New Work Order');
        browser.sleep(4000);
    });
    
    //---------Check New Work Order button Click -> open new WorkOrder sidepanel open and Close(X)-----------
    it('Check New Work Order button Click -> open new WorkOrder sidepanel open and Close(X)', function () {
        //browser.get(urlsPage.allWorkOrders);
        browser.sleep(2000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New Work Order');
        browser.sleep(4000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        expect(newButton.getText()).toEqual('New Work Order');
    });

    //-------Check New Work Order creation->without entering mandatory fields data----------
    describe('Check New Work Order creation->without entering mandatory fields data', function () {

        beforeAll(function () {
            //browser.get(urlsPage.allWorkOrders);
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });


        it('Without  Type', function () {
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select type');
            browser.sleep(5000);
        });

        it('Without select Asset', function () {

            var selectbuttonClick = element(by.id('Select'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectbuttonClick), 10000);
            selectbuttonClick.click();
            browser.sleep(1000);
            var selectType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectType), 15000);
            selectType.click();
            browser.sleep(1000);

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select asset');
            browser.sleep(5000);
        });

        it('Without Maintenance Plan', function () {

            var selectAsset = element(by.model('newWorkOrderVm.newWorkOrder.asset'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select maintenance plan');
            browser.sleep(5000);

        });


        it('Without Name', function () {

            var selectAsset = element(by.model('newWorkOrderVm.newWorkOrder.plan'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();


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

        it('Without Assigned To Field', function () {
            browser.sleep(1000);
            var name = element(by.model('newWorkOrderVm.newWorkOrder.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 10000);
            name.sendKeys('Work Order 200');
            browser.sleep(5000);


            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Please select assigned to');
            browser.sleep(8000);

            var closeButton = element(by.id('closeRightSidePanel'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(closeButton), 15000);
            closeButton.click();
            browser.sleep(1000);
        });


        
    });

    //-------Check New Work Order creation->with entering mandatory fields data----------
    describe('Check New Work Order creation->by entering mandatory fields data', function () {

        beforeAll(function () {
            var newButton = element(by.css('.new-button'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(newButton), 15000);
            newButton.click();
            browser.sleep(1000);
        });

        it(' New Work Order create Successfull Test Case', function () {

            var selectbuttonClick = element(by.id('Select'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectbuttonClick), 10000);
            selectbuttonClick.click();
            browser.sleep(1000);
            var equipmentType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(equipmentType), 15000);
            equipmentType.click();
            browser.sleep(1000);

            var selectAsset = element(by.model('newWorkOrderVm.newWorkOrder.asset'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var selectAsset = element(by.model('newWorkOrderVm.newWorkOrder.plan'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var name = element(by.model('newWorkOrderVm.newWorkOrder.name'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(name), 15000);
            name.sendKeys('Work Order 200');
            browser.sleep(3000);

            var selectAsset = element(by.model('newWorkOrderVm.newWorkOrder.assignedTo'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(selectAsset), 15000);
            selectAsset.click();
            element.all(by.css('.ui-select-choices-row-inner div')).first().click();

            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(2000);
            var alertMessage = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
            expect(alertMessage).toEqual('Work order created successfully');
            browser.sleep(8000);
            //browser.get(urlsPage.allWorkOrders);
        });

    });

});

