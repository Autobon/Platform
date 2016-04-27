import WebpackDevServer from 'webpack-dev-server';
import webpack from 'webpack';
import { development } from './config';
import path from 'path';

var port = 54321;
development.entry.app.push('webpack/hot/dev-server', 'webpack-dev-server/client?http://localhost:' + port);
const compiler = webpack(development);
const server = new WebpackDevServer(compiler, {
    contentBase: path.resolve(__dirname, '../build/'),
    quiet: false,
    noInfo: false,
    hot: true,
    inline: true,
    filename: 'bundle.js',
    publicPath: '/',
    headers: {'Access-Control-Allow-Origin': '*'},
    stats: {colors: true},
    historyApiFallback: true,
});

server.listen(port, '0.0.0.0', (err) => {
    if (err) {
        throw err;
    }
});
