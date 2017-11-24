
var exec = require('cordova/exec');

var PLUGIN_NAME = 'SimpleAlarmCordovaPlugin';

var SimpleAlarmCordovaPlugin = {
  echo: function(phrase, cb) {
    exec(cb, null, PLUGIN_NAME, 'echo', [phrase]);
  },
  getDate: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'getDate', []);
  },
  init: function(callback, error){
    exec(callback, error, PLUGIN_NAME, 'init', []);
  },
  createAlarm: function(idAlarm, date, title, message, callback, error) {
    exec(callback, error, PLUGIN_NAME, 'createAlarm', [idAlarm, date, title, message]);
  },
  cancelAlarm: function(idAlarm, callback, error) {
    exec(callback, error, PLUGIN_NAME, 'cancelAlarm', [idAlarm]);
  }
};

module.exports = SimpleAlarmCordovaPlugin;
