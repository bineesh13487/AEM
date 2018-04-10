/**
 * Inspired by https://github.com/topheman/react-es6-redux
 */

const log = require('npmlog');
const Webpack = require('webpack');
const FS = require('fs');
const Path = require('path');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const StylelintWebpackPlugin = require('stylelint-webpack-plugin');
const AEMClientlibWebpackPlugin = require('aem-clientlib-webpack-plugin').default;
const RtlPlugin = require('webpack-rtl-plugin');
const CSSNextPostCSSPlugin = require('postcss-cssnext');
const MyLocalIp = require('my-local-ip');
const Common = require('./bin/common');
const ClientlibConfig = require('./clientlib.config');

const plugins = [];

/** environment setup */
const BUILD_DIR = './build';
const DIST_DIR = process.env.DIST_DIR || 'dist'; // relative to BUILD_DIR
const NODE_ENV = process.env.NODE_ENV ? process.env.NODE_ENV.toLowerCase() : 'development';
const DEVTOOLS = process.env.DEVTOOLS ? JSON.parse(process.env.DEVTOOLS) : null; // can be useful in case you have web devtools (null by default to differentiate from true or false)
// optimize in production by default - otherwize, override with OPTIMIZE=false flag (if not optimized, sourcemaps will be generated)
const OPTIMIZE = process.env.OPTIMIZE ? JSON.parse(process.env.OPTIMIZE) : NODE_ENV === 'production';
const MODE_DEV_SERVER = process.argv[1].indexOf('webpack-dev-server') > -1;
const LINTER = process.env.LINTER ? JSON.parse(process.env.LINTER) : true;
const FAIL_ON_ERROR = process.env.FAIL_ON_ERROR ? JSON.parse(process.env.FAIL_ON_ERROR) : !MODE_DEV_SERVER;// disabled on dev-server mode, enabled in build mode
const STATS = process.env.STATS ? JSON.parse(process.env.STATS) : false; // to output a stats.json file (from webpack at build - useful for debuging)
const LOCALHOST = process.env.LOCALHOST ? JSON.parse(process.env.LOCALHOST) : true;
const ASSETS_LIMIT = typeof process.env.ASSETS_LIMIT !== 'undefined' ? parseInt(process.env.ASSETS_LIMIT, 10) : 5000;// limit bellow the assets will be inlines
const hash = (NODE_ENV === 'production' && DEVTOOLS ? '-devtools' : '') + (NODE_ENV === 'production' ? '-[hash]' : '');

log.level = 'silly';
log.info('webpack', `Launched in ${MODE_DEV_SERVER ? 'dev-server' : 'build'} mode`);

/** integrity checks */

if (/^\w+/.test(DIST_DIR) === false || /\/$/.test(DIST_DIR) === true) { // @todo make a better regexp that accept valid unicode leading chars
  log.error('webpack', `DIST_DIR should not contain trailing slashes nor invalid leading chars - you passed "${DIST_DIR}"`);
  process.exit(1);
}

log.info('webpack', `${NODE_ENV.toUpperCase()} mode`);

if (DEVTOOLS) {
  log.info('webpack', 'DEVTOOLS active');
}
if (!OPTIMIZE) {
  log.info('webpack', 'SOURCEMAPS activated');
}
if (FAIL_ON_ERROR) {
  log.info('webpack', 'NoErrorsPlugin disabled, build will stop when there are error(s)');
}

/** plugins setup */

if (!FAIL_ON_ERROR) {
  plugins.push(new Webpack.NoEmitOnErrorsPlugin());
}

const entry = {};

ClientlibConfig.libs.forEach(({ name }) => {
  entry[name] = `./src/clientlibs/${name}.js`;
});

plugins.push(new Webpack.BannerPlugin(Common.getBanner()));
plugins.push(new Webpack.DefinePlugin({
  // Lots of library source code are based on process.env.NODE_ENV
  // (all development related code is wrapped inside a conditional that can be dropped if equal to "production"
  // this way you get your own react.min.js build)
  'process.env': {
    NODE_ENV: JSON.stringify(NODE_ENV),
    DEVTOOLS: typeof DEVTOOLS !== 'undefined' ? DEVTOOLS : false, // You can rely on this var in your code to enable specific features only related to development (that are not related to NODE_ENV)
    LINTER // You can choose to log a warning in dev if the linter is disabled
  }
}));

if (OPTIMIZE) {
  plugins.push(new Webpack.optimize.UglifyJsPlugin({
    compress: {
      warnings: true
    }
  }));
}

if (NODE_ENV !== 'production') {
  // to keep compatibility with old loaders - debug: true was previously on config
  plugins.push(new Webpack.LoaderOptionsPlugin({
    debug: true
  }));
}

if (MODE_DEV_SERVER) {
  // webpack-dev-server mode
  if (LOCALHOST) {
    log.info('webpack', 'Check http://localhost:4000');
  }
  else {
    log.info('webpack', `Check http://${MyLocalIp()}:4000`);
  }
}
else if (STATS) {
  // write infos about the build (to retrieve the hash) https://webpack.github.io/docs/long-term-caching.html#get-filenames-from-stats
  plugins.push(function writeBuildInfo() {
    this.plugin('done', stats => {
      FS.writeFileSync(
        Path.join(__dirname, BUILD_DIR, DIST_DIR, 'stats.json'),
        JSON.stringify(stats.toJson())
      );
    });
  });
}

/** preloaders */

const preLoaders = [];

if (LINTER) {
  log.info('webpack', 'LINTER ENABLED');
  plugins.push(new StylelintWebpackPlugin());
  preLoaders.push({
    test: /\.js$/,
    exclude: [/node_modules/, /legacy/],
    loader: 'eslint-loader',
    enforce: 'pre'
  });
}
else {
  log.warn('webpack', 'LINTER DISABLED');
}

const extractSass = new ExtractTextPlugin({
  filename: `[name].css`,
  disable: false,
  allChunks: true
});
plugins.push(extractSass);

plugins.push(new RtlPlugin({ minify: false }));

/** aem specific plugins */
plugins.push(new AEMClientlibWebpackPlugin(ClientlibConfig));


/** webpack config */

const config = {
  bail: FAIL_ON_ERROR,
  entry,
  output: {
    publicPath: '',
    filename: `[name]${hash}.js`,
    chunkFilename: `[id]${hash}.chunk.js`,
    path: Path.join(__dirname, BUILD_DIR, DIST_DIR)
  },
  watch: MODE_DEV_SERVER,
  cache: true,
  devtool: OPTIMIZE ? false : 'sourcemap',
  devServer: {
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept'
    },
    overlay: {
      warnings: false,
      errors: true
    },
    host: LOCALHOST ? 'localhost' : MyLocalIp(),
    hot: MODE_DEV_SERVER
  },
  module: {
    rules: [
      ...preLoaders,
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader'
        }
      },
      {
        test: /\.ejs$/,
        exclude: /node_modules/,
        loader: 'ejs-compiled-loader'
      },
      {
        test: /\.(s)?css/,
        use: extractSass.extract([
          {
            loader: 'css-loader',
            options: {
              url: false,
              sourceMap: OPTIMIZE,
              minimize: false
            }
          },
          {
            loader: 'postcss-loader',
            options: {
              ident: 'postcss',
              plugins: () => [
                CSSNextPostCSSPlugin()
              ],
              sourceMap: OPTIMIZE,
              minify: false
            }
          },
          {
            loader: 'sass-loader',
            options: {
              minimize: false,
              includePaths: [
                Path.resolve(__dirname, 'src/scss'),
                Path.resolve(__dirname, 'src/components'),
                Path.resolve(__dirname, 'node_modules')
              ],
              sourceMap: OPTIMIZE
            }
          }
        ])
      },
      { test: /\.(png)$/, loader: `url-loader?limit=${ASSETS_LIMIT}&name=assets/[hash].[ext]` },
      { test: /\.woff(\?v=\d+\.\d+\.\d+)?$/, loader: `url-loader?limit=${ASSETS_LIMIT}&mimetype=application/font-woff&name=assets/[hash].[ext]` },
      { test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/, loader: `url-loader?limit=${ASSETS_LIMIT}&mimetype=application/font-woff&name=assets/[hash].[ext]` },
      { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: `url-loader?limit=${ASSETS_LIMIT}&mimetype=application/octet-stream&name=assets/[hash].[ext]` },
      { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: 'file-loader?&name=assets/[hash].[ext]' },
      { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: `url-loader?limit=${ASSETS_LIMIT}&mimetype=image/svg+xml&&name=assets/[hash].[ext]` }
    ]
  },
  plugins,
  node: {
    console: true,
    fs: 'empty',
    net: 'empty',
    tls: 'empty'
  }
};

module.exports = config;
