const Path = require('path');
const TouchFiles = require('./bin/touch-files');

let firstCallback = true;

module.exports = {
  // default working directory (can be changed per 'cwd' in every asset option)
  context: __dirname,

  /*
  PROJECT       : CLIENTLIBS
  mcd-us        : mcd-us & mcd-usRTL
  mcd-arabia    : mcd-arabia (rtl)
  mcd-global    : mcd-rwd-global, mcd-rwd-globalRTL
  mcd-newsroom  : mcd-newsroom, mcd-newsroomRTL
  mcd-yrtk      : mcd-yrtk, mcd-yrtkRTL


  mcd-us (all components sans yrtk, newsroom)
  mcd-yrtk (yrtk components)
  mcd-newsroom (newsroom components)
  */
  logLevel: 'info',
  cleanBuildsOnce: false,
  watchPaths: [
    { path: Path.resolve(__dirname, 'src/legacy'), match: '**' },
    { path: Path.resolve(__dirname, '../'), match: '**/jcr_root/apps/**/*.html', syncOnly: true }
  ],
  libs: [
    {
      name: 'mcd-us',
      categoryName: 'mcd.us',
      destination: '../mcd-us/ui.apps/src/main/content/jcr_root/etc/clientlibs',
      dependencies: 'mcd.global',
      baseTxtFile: {
        js: Path.resolve(__dirname, 'src/legacy/mcd-us/js.txt')
      },
      assets: {
        js: [
          'build/dist/mcd-us.js',
          { src: 'src/legacy/mcd-us/js/**', excludeFromTxt: true, base: 'src/legacy/mcd-us/js/' }
        ],
        css: [
          'build/dist/mcd-us.css'
        ],
        resources: [
          { src: 'src/assets/fonts/**', dest: './fonts/', base: 'src/assets/fonts/' },
          { src: 'src/assets/images/**', dest: './images/', base: 'src/assets/images/' }
        ],
        scss: []
      }
    },
    {
      name: 'mcd-usRTL',
      categoryName: 'mcd.usRTL',
      destination: '../mcd-us/ui.apps/src/main/content/jcr_root/etc/clientlibs',
      dependencies: 'mcd.globalRTL',
      assets: {
        css: [
          'build/dist/mcd-us.rtl.css'
        ],
        resources: [
          { src: 'src/assets/fonts/**', dest: './fonts/', base: 'src/assets/fonts/' },
          { src: 'src/assets/images/**', dest: './images/', base: 'src/assets/images/' }
        ],
        scss: []
      }
    },
    {
      name: 'mcd-newsroom',
      categoryName: 'mcd.newsroom',
      destination: '../mcd-newsroom/ui.apps/src/main/content/jcr_root/etc/clientlibs',
      dependencies: 'mcd.us',
      baseTxtFile: {
        js: Path.resolve(__dirname, 'src/legacy/mcd-newsroom/js.txt')
      },
      assets: {
        js: [
          'build/dist/mcd-newsroom.js',
          { src: 'src/legacy/mcd-newsroom/js/**', excludeFromTxt: true, base: 'src/legacy/mcd-newsroom/js/' }
        ],
        css: [
          'build/dist/mcd-newsroom.css'
        ],
        resources: [],
        scss: []
      }
    },
    {
      name: 'mcd-newsroomRTL',
      categoryName: 'mcd.newsroomRTL',
      destination: '../mcd-newsroom/ui.apps/src/main/content/jcr_root/etc/clientlibs',
      dependencies: 'mcd.usRTL',
      assets: {
        css: [
          'build/dist/mcd-newsroom.css'
        ],
        resources: [],
        scss: []
      }
    },
    {
      name: 'mcd-yrtk',
      categoryName: 'mcd.yrtk',
      destination: '../mcd-yrtk/mcd-yrtk-ui/src/main/content/jcr_root/etc/clientlibs',
      dependencies: 'mcd.us',
      baseTxtFile: {
        js: Path.resolve(__dirname, 'src/legacy/mcd-yrtk/js.txt')
      },
      assets: {
        js: [
          'build/dist/mcd-yrtk.js',
          { src: 'src/legacy/mcd-yrtk/js/**', excludeFromTxt: true, base: 'src/legacy/mcd-yrtk/js/' }
        ],
        css: [
          'build/dist/mcd-yrtk.css'
        ],
        resources: [],
        scss: []
      }
    },
    {
      name: 'mcd-yrtkRTL',
      categoryName: 'mcd.yrtkRTL',
      destination: '../mcd-yrtk/mcd-yrtk-ui/src/main/content/jcr_root/etc/clientlibs',
      dependencies: 'mcd.usRTL',
      assets: {
        css: [
          'build/dist/mcd-yrtk.css'
        ],
        resources: [],
        scss: []
      }
    },


  ],

  beforeEach(lib) {
    return Promise.resolve(lib);
  },

  sync: () => (process.argv.indexOf('--watch') > -1 ? {
    targets: [
      'http://admin:admin@127.0.0.1:4502'
    ],
    pushInterval: 1000,
    onPushEnd: ((err, host, pusher) => {
      if (err) {
        console.error(err);
        return;
      }
      console.log(`Last sync to ${host} at ${(new Date()).toTimeString()}`);
    }),
    pushEntireClientlibOnFirstRun: true
  } : null),

  build: {
    force: false
  },

  before: plugin => {
    if (firstCallback && plugin.options.sync) {
      firstCallback = false;
      const promise = new Promise((resolve, reject) => {
        const tf = new TouchFiles();
        // touching files changed in last one month,
        // so syncer will upload a package to AEM
        tf.init(1).then(({ touchedFiles, svnBaseDir }) => {
          console.log(`Touching ${touchedFiles.length} files for sync under ${svnBaseDir}.`);
          setTimeout(resolve, 3000); // wait for package push to be complete
        }).catch(error => {
          console.error(`Failed to touch file. ${error}`);
          reject();
        });
      });
      return promise;
    }
    return Promise.resolve(true);
  }
};
