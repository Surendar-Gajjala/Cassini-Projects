String.prototype.format = String.prototype.f = function() {
    var s = this,
        i = arguments.length;

    while (i--) {
        s = s.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
    }
    return s;
};

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
};

String.prototype.insert = function (index, string) {
    if (index > 0)
        return this.substring(0, index) + string + this.substring(index, this.length);
    else
        return string + this;
};

String.prototype.trunc = function( n, useWordBoundary ){
    var isTooLong = this.length > n,
        s_ = isTooLong ? this.substr(0,n-1) : this;
    s_ = (useWordBoundary && isTooLong) ? s_.substr(0,s_.lastIndexOf(' ')) : s_;
    return  isTooLong ? s_ + '...' : s_;
};


Number.prototype.toFileSize = function() {
    var bytes = this;
    if (bytes == 0) { return "0.00 B"; }
    var e = Math.floor(Math.log(bytes) / Math.log(1024));
    return (bytes/Math.pow(1024, e)).toFixed(2)+' '+' KMGTP'.charAt(e)+'B';
};


Array.range = function(n) {
    // Array.range(5) --> [0,1,2,3,4]
    return Array.apply(null,Array(n)).map(function(x,i){return i})
};


Array.prototype.remove = function(obj) {
    var index = this.indexOf(obj);
    if(index != -1) {
        this.splice(index, 1);
    }
};


Object.defineProperty(Array.prototype, 'chunk', {
    value: function(n) {

        // ACTUAL CODE FOR CHUNKING ARRAY:
        return Array.range(Math.ceil(this.length/n)).map(function(x,i){
            return this.slice(i*n,i*n+n);
        }.bind(this));

    }
});
