var exec = require('cordova/exec');

exports.sign = function(path,successCallback, errorCallback) {
    exec( successCallback, 
            errorCallback, 
            'SignUtil', 
			'SIGN',
            [{                 
                "path": path
             }]);
};