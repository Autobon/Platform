import WebpackDevServer from 'webpack-dev-server';
import webpack from 'webpack';
import { development } from './config';
import path from 'path';

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

server.listen(54321, '0.0.0.0', (err) => {
    if (err) {
        throw err;
    }
});
