# GitRepository
sign dir is a plugin which allows android users to sign.

you can use it easily

 var path = null; 
        var success = function(message) { 
        document.querySelector("#ImgSign").src = message;
        }; 
        var error = function(message) { alert("Oopsie! " + message); }; 
        cordova.plugins.SignUtil.sign(path, success, error); 
