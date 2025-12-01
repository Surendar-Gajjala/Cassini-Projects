String.prototype.format = String.prototype.f = function() {
    var s = this,
        i = arguments.length;

    while (i--) {
        s = s.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
    }
    return s;
};


Array.range = function(n) {
    // Array.range(5) --> [0,1,2,3,4]
    return Array.apply(null,Array(n)).map(function(x,i){return i})
};

Object.defineProperty(Array.prototype, 'chunk', {
    value: function(n) {

        // ACTUAL CODE FOR CHUNKING ARRAY:
        return Array.range(Math.ceil(this.length/n)).map(function(x,i){
            return this.slice(i*n,i*n+n);
        }.bind(this));

    }
});
