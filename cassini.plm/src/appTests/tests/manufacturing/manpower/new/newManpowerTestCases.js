var urlsPage = require('../../../../pages/urlsPage.js');

//-------Check New Manpower creation->without entering mandatory fields data----------
describe('Check New Manpower creation->without entering mandatory fields data for New Person', function () {

    beforeAll(function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(5000);
    });


    it('Without Manpower Type', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);

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


    it('Without Name', function () {
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
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(5000);
    });

    it('Without First Name ', function () {
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

        browser.wait(EC.presenceOf(urlsPage.byModel('newManpowerVm.newManpower.name').sendKeys('Manpower 27')), 35000);
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

    it('Without E-mail ', function () {

        var firstName = element(by.model('person.firstName'))
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(firstName), 10000);
        firstName.sendKeys('person 1');

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

//-------Check New Manpower creation->with entering mandatory fields data for New Person----------
describe('Check New Manpower creation Successful entering mandatory fields data for New Person', function () {

    beforeAll(function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(5000);
    });

    afterAll(function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(5000);
    });

    it(' New Manpower create Successfull Test Case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

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

        browser.wait(EC.presenceOf(urlsPage.byModel('newManpowerVm.newManpower.name').sendKeys('Manpower 06')), 35000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('person.firstName').sendKeys('person 11')), 35000);
        browser.sleep(2000);

        browser.wait(EC.presenceOf(urlsPage.byModel('person.email').sendKeys('person111@gmail.com')), 35000);
        browser.sleep(2000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manpower created successfully');
        browser.sleep(8000);
    });

});


//-------Check New Manpower creation->without entering mandatory fields data for existing Person----------
describe('Check New Manpower creation->without entering mandatory fields data for existing Person ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(5000);
    });

    it('Without Manpower Type', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(3000);

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


    it('Without Name', function () {
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
        expect(alertMessage).toEqual('Please enter name');
        browser.sleep(5000);
    });

    it('Without select existing Person ', function () {
        // var selectbuttonClick = element(by.id('Select'));
        // var EC = protractor.ExpectedConditions;
        // browser.wait(EC.presenceOf(selectbuttonClick), 10000);
        // selectbuttonClick.click();
        // browser.sleep(1000);
        // var selectType = element(by.id('manufacturingTypeTree')).element(by.xpath('li/ul/li/div'));
        // var EC = protractor.ExpectedConditions;
        // browser.wait(EC.presenceOf(selectType), 15000);
        // selectType.click();
        // browser.sleep(1000);

        var name = element(by.model('newManpowerVm.newManpower.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 15000);
        name.sendKeys('Manpower 27');
        browser.sleep(2000);

        var clickExistPerson = element(by.css("[for='isExistPerson']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickExistPerson), 15000);
        clickExistPerson.click();
        browser.sleep(1000);

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(1500);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Please select any person');
        browser.sleep(5000);

        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);
    });

});


//-------Check New Manpower creation->with entering mandatory fields data for Existing Person----------
describe('Check New Manpower creation Successful entering mandatory fields data for Existing Person', function () {

    beforeAll(function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(5000);
    });

    afterAll(function () {
        browser.get(urlsPage.allManpower);
        browser.sleep(5000);
    });

    it(' New Manpower create Successfull Test Case', function () {
        var newButton = element(by.css('.new-button'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButton), 15000);
        newButton.click();
        browser.sleep(2000);

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

        var name = element(by.model('newManpowerVm.newManpower.name'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(name), 15000);
        name.sendKeys('Manpower 6');
        browser.sleep(2000);

        var clickExistPersonButton = element(by.css("[for='isExistPerson']"));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(clickExistPersonButton), 15000);
        clickExistPersonButton.click();
        browser.sleep(1000);

        var selectPerson = element(by.model('newManpowerVm.newManpower.person'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectPerson), 15000);
        selectPerson.click();
        browser.sleep(1000);
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(3000);
        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Manpower created successfully');
        browser.sleep(8000);
    });

});
