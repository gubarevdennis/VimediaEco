// Generated using webpack-cli https://github.com/webpack/webpack-cli

const path = require('path');
const { VueLoaderPlugin } = require('vue-loader');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
// const WorkboxWebpackPlugin = require('workbox-webpack-plugin');
//
// const isProduction = process.env.NODE_ENV == 'production';
//
//
// const stylesHandler = MiniCssExtractPlugin.loader;


module.exports = {
  entry: {
    app: './src/main/resources/js/main.js',
  },
  plugins: [
    new VueLoaderPlugin(),
    new HtmlWebpackPlugin({
      title: 'Production',
    }),
  ],
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/i,
        loader: 'babel-loader',
        options: {
          presets: ['@babel/preset-env']
        }
      },
      {
        test: /\.css$/i,
        use: ['vue-style-loader', 'css-loader'],
      },
      {
        test: /\.(eot|svg|ttf|woff|woff2|png|jpg|gif)$/i,
        type: 'asset',
      },
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },

      // Add your rules for custom modules here
      // Learn more about loaders from https://webpack.js.org/loaders/
    ],
  },

  output: {
    filename: 'main.js',
    path: path.resolve(__dirname, 'src', 'main', 'resources', 'static', 'js',),
  },
};
