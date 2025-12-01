var urlsPage = require('../../../../pages/urlsPage.js');

//---------Check New Vault creation->without entering mandatory fields data-----------
describe('Check New Vault creation->without entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allVaults);
        browser.sleep(5000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);
    });


    it('Without Vault name', function () {
        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Vault name is required');
        browser.sleep(5000);
    });

    it('Without Description ', function () {

        var vaultName = element(by.model('newVaultVm.newVault.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(vaultName), 15000);
        vaultName.sendKeys('Vaults 1');
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Vault description is required');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });
    
});

 //---------Check New Vault creation->with entering mandatory fields data-----------
 describe('Check New Vault creation->with entering mandatory fields data', function () {

    beforeAll(function () {
        browser.get(urlsPage.allVaults);
        browser.sleep(5000);
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);
    });


    it('New Vault creation Successfull test case', function () {

        var vaultName = element(by.model('newVaultVm.newVault.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(vaultName), 15000);
        vaultName.sendKeys('Vaults 11');
        browser.sleep(3000);

        var vaultDescrption = element(by.model('newVaultVm.newVault.description'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(vaultDescrption), 15000);
        vaultDescrption.sendKeys('Description');
        browser.sleep(3000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Vault created successfully');
        browser.sleep(10000);

    });
    
});