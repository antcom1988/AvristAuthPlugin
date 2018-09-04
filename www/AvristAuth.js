var exec = require('cordova/exec');

function plugin() {}

plugin.prototype.run = function(param, callback) {
    exec(callback, function(err){
        alert(err);
    }, "AvristAuth", "login", [param]);
}

plugin.prototype.profile = function(param, callback) {
    exec(callback, function(err){
        alert(err);
    }, "AvristAuth", "profile", [param]);
}

plugin.prototype.change = function(param, callback) {
    exec(callback, function(err){
        alert(err);
    }, "AvristAuth", "change", [param]);
}

plugin.prototype.logout = function(param, callback) {
    exec(callback, function(err){
        alert(err);
    }, "AvristAuth", "logout", [param]);
}

module.exports = new plugin();
