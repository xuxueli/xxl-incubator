var settings = require('../settings.js');
var mysql = require('mysql');

module.exports = mysql.createPool({
   host: settings.host,
    user: settings.user,
    password: settings.password,
    database: settings.database,
    connectionLimit: 10,
    dateStrings: true
});