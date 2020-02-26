
var exec = require('cordova/exec');

var Scanner = function() {};

Scanner.coolMethod = function() {
  exec(null, null, 'Scanner2D', 'coolMethod', [arg0]);
  return this;
};
Scanner.onKeyDown = function() {
    exec(null, null, 'Scanner2D', 'onKeyDown', [arg0]);
    return this;
  };
/*
exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'Scanner', 'coolMethod', [arg0]);
};*/

// Init plugin
document.addEventListener('deviceready', function() {
    exec(
      function(args) {
        window.dispatchEvent(new CustomEvent(args[0], {
          detail: args[1]
        }));
      },
      null,
      'Scanner',
      'init',
      []
    );
  });

  module.exports = Scanner;


