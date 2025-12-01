var loginPage = function(){

    var EC = protractor.ExpectedConditions;
    this.byCss = function(value) {return element(by.css(value));};
    this.byId = function(value) {return element(by.id(value));};
    this.byModel = function(value) {return element(by.model(value));};

    this.get = function(url){
        browser.waitForAngularEnabled(false);
        browser.ignoreSynchronization = true;
        browser.get(url);
        browser.sleep(8000);
    };

    this.enterUserName = function(userName){
        browser.wait(EC.presenceOf(this.byModel('loginVm.userName').sendKeys(userName)), 10000);
    };

    this.enterPassword = function(password){
        browser.wait(EC.presenceOf(this.byModel('loginVm.password').sendKeys(password)), 10000);
    };

    this.clickGo = function(){
        this.byId('btnSignin').click();
    };

    this.verifyUrl = function(result){
        expect(browser.getCurrentUrl()).toEqual(result);
    };

    this.verifyTextValue = function(result) {
        expect(this.byCss('[class="error-panel ng-scope ng-binding"]').getText()).toEqual(result);
    };

};

module.exports = new loginPage();