import webpack from 'webpack';
import path from 'path';
import autoprefixer from 'autoprefixer';
import ExtractTextPlugin from 'extract-text-webpack-plugin';

export const production = {
    context : path.resolve(__dirname, '..'),
    entry   : {
        app   : [path.resolve(__dirname, '../app/main.js')],
        vendor: ['angular', 'angular-animate', 'angular-ui-router',
            'angular-ui-bootstrap', 'angular-bootstrap-datetimepicker',
            'jquery', 'moment', 'babel-polyfill', 'chart.js'],
    },
    output  : {
        path      : path.resolve(__dirname, '../../static'),
        publicPath: '/',
        filename  : 'bundle.js',
    },
    postcss : [
        autoprefixer({browsers: ['last 2 versions']}),
    ],
    module  : {
        preLoaders: [
            {test: /\.js$|.jsx$/, exclude: /node_modules/, loader: 'eslint-loader'},
        ],
        loaders   : [
            {test: /[\/]angular\.js$/, loader: 'exports?angular'},
            {test: /\.css$/, loader: ExtractTextPlugin.extract('style-loader', 'css!postcss')},
            {test: /\.less$/, loader: ExtractTextPlugin.extract('style-loader', 'css!postcss!less')},
            {test: /\.scss$/, loader: ExtractTextPlugin.extract('style-loader', 'css!postcss!sass')},
            {test: /\.js$/, exclude: /node_modules/, loader: 'babel'},
            {test: /ngES6.*\.js$/, loader: 'babel'},
            {test: /\.json$/, loader: 'json'},
            {test: /\.html$/, exclude: /node_modules/, loader: 'html'},
            {test: /\.(jpg|png|gif|woff|woff2)(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: 'url?limit=50000'},
            {test: /\.(svg|ttf|eot)(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: 'file'}
        ],
    },
    resolve : {
        root              : [path.join(__dirname, '../bower_components')],
        extensions        : ['', '.js'],
        modulesDirectories: ['node_modules'],
        alias             : {
            angular: require.resolve('angular'),
        }
    },
    progress: true,
    plugins : [
        new webpack.ProvidePlugin({
            $     : 'jquery',
            jQuery: 'jquery',
        }),
        new webpack.ResolverPlugin(
            new webpack.ResolverPlugin.DirectoryDescriptionFilePlugin('bower.json', ['main'])
        ),
        new ExtractTextPlugin('[name].css'),
        new webpack.optimize.DedupePlugin(),
        new webpack.optimize.CommonsChunkPlugin('vendor', 'vendor.bundle.js'),
        new webpack.optimize.UglifyJsPlugin({
            compressor: {
                screw_ie8: true,
                warnings : false,
            },
        }),
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify('production'),
                BROWSER : JSON.stringify(true),
            },
        }),
        // print a webpack progress
        new webpack.ProgressPlugin((percentage, message) => {
            const MOVE_LEFT  = new Buffer('1b5b3130303044', 'hex').toString();
            const CLEAR_LINE = new Buffer('1b5b304b', 'hex').toString();

            process.stdout.write(CLEAR_LINE + Math.round(percentage * 100) + '% :' + message + MOVE_LEFT);
        }),
    ],
    // devtool : 'source-map',
};

export const development = {
    ...production,
    output : {
        path      : path.resolve(__dirname, '../build'),
        publicPath: '/',
        filename  : 'bundle.js',
    },
    plugins: [
        new webpack.ProvidePlugin({
            $     : "jquery",
            jQuery: "jquery"
        }),
        new webpack.ResolverPlugin(
            new webpack.ResolverPlugin.DirectoryDescriptionFilePlugin('bower.json', ['main'])
        ),
        new ExtractTextPlugin('[name].css'),
        new webpack.optimize.CommonsChunkPlugin('vendor', 'vendor.bundle.js'),
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify('development'),
                BROWSER : JSON.stringify(true),
            },
        }),
        new webpack.HotModuleReplacementPlugin(),
        // print a webpack progress
        new webpack.ProgressPlugin((percentage, message) => {
            const MOVE_LEFT  = new Buffer('1b5b3130303044', 'hex').toString();
            const CLEAR_LINE = new Buffer('1b5b304b', 'hex').toString();

            process.stdout.write(CLEAR_LINE + Math.round(percentage * 100) + '% :' + message + MOVE_LEFT);
        }),
    ],
    watch  : true,
    devtool: 'source-map',
};
