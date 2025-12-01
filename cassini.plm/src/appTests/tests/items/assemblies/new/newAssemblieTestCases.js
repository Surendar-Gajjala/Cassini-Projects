//var assembliePage = require('../../../../pages/assembliePage');
var urlsPage = require('../../../../pages/urlsPage');

describe('New Assemblie Test Cases', function () {

    beforeAll(function(){
        browser.get(urlsPage.allAssembliesItems);
        browser.sleep(10000);
    });

    it('with out Item Type -> create new Item', function () {
        var newButtonClick = element(by.css('[ng-click="itemsVm.showNewItem()"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(5000);

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

    it('with out Item Name -> create new Item', function () {
        var selectbuttonClick = element(by.id('select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 15000);
        selectbuttonClick.click();
        browser.sleep(1000);

        var selectItemType =  element(by.id('itemClassTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectItemType), 15000);
        selectItemType.click();
        browser.sleep(5000);

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

    it('creation of item with mandatory fields',function(){
        browser.get(urlsPage.allAssembliesItems);
        browser.sleep(8000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

        var selectbuttonClick = element(by.id('select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();

        var selectItemType =  element(by.id('itemClassTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectItemType), 15000);
        selectItemType.click();

        var ItemName =  element(by.model('newItemVm.newItem.itemName'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(ItemName), 15000);
        ItemName.sendKeys('item100');

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(4000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Item created successfully');
        browser.sleep(8000)

    });

});