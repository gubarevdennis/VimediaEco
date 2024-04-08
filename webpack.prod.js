const path = require('path');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
//вукычукрурруруруур
module.exports = merge(common, {
  mode: 'production',
});
