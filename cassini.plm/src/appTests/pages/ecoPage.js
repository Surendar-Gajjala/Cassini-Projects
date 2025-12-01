var ecoPage = function(){

    var EC = protractor.ExpectedConditions;
    this.byCss = function(value) {return element(by.css(value));};
    this.byId = function(value) {return element(by.id(value));};
    this.byModel = function(value) {return element(by.model(value));};


    this.get = function(url){
        browser.waitForAngularEnabled(false);
        browser.ignoreSynchronization = true;
        browser.get(url);
        browser.sleep(9000);
    };


    this.verifyUrl = function(result){
        expect(browser.getCurrentUrl()).toEqual(result);
    };

};

module.exports = new ecoPage();