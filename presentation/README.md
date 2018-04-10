# McDonald's Front-end DEV Environment
=====================


## Prerequisites ##

You'll need node, yarn and npm installed. The environment was built using the following node, yarn and npm versions:

```
node 8.9.1
npm 5.5.1
yarn 1.3.2
aem 6.3
```

What we have:

* Lint tasks (css, ES6 Lint)
* BabelJS transpiler
* Babel-eslint / eslint-config-airbnb-base
* Sass support
* Sourcemaps support
* AEM syncing
* Clientlib generation
* RTL code generation


## Folder Structure ##

See docs/ folder for information on folder structure.


## Linting ##

For code structure consistency across developers and implementing best-practice code, linters for HTML/CSS/JS have been implemented. Please do not modifiy these files. If as a team we think changes need to be made, we'll make them as a team so everyone is working off of the same configuration files for linting.


### Linter

* eslint is running while you're developping, check your console for errors
* you can also launch it via `npm run lint`
* see `.eslintrc` for the configuration (currently, this project uses [the airbnb presets](https://www.npmjs.com/package/eslint-config-airbnb-base)

## Accessibility ##

Accessbiliity automated testing will be implemented on the front-end to ensure all code passes before going into QA. 
Currently evaluating: Pa11y, Google Accessibility Tools, and HTML Sniffer


### Install depdencies

```shell
svn co <repo_url> 
cd 
yarn install
```

### Run the project (developer)

```shell
yarn start
```

Running the project will find a local instance of AEM and sync the clientlibs to that instance. If an AEM is not running, then running the project will show errors in the terminal output.

Running the project will also update files in mcd-* folders in the parent directory of *presentation* folder.


NOTE: Though AEM 6.3 is listed as a pre-requisite. Building the FE project doesn't need AEM 6.3 running. It's only required for the local syncing functionality, which, is turned off for production build and turned on only for local runs. Hence, a CI server like Jenkins won't need AEM running. See *Build* section below.

### Build

The `./build` directory is ignored by git, it will contain a `dist` directory which holds the distribution version of your website 

All the build tasks will create a build version of the project in the `./build/dist` folder, cleaning it before making the build.

* `yarn build`
* `yarn build-prod` optimized / uglified version
* `yarn build-prod-all` will build:
	* production version (optimized / uglified)
	* a debuggable version accessible at `/devtools` shipping all the sourcemaps, to ease sharing transpiled source code

`npm run serve-dist` will serve your `./build/dist` folder at [http://localhost:3000](http://localhost:3000) so that you could test the built version you just made.


### Customizations

You can customize the behavior of the scripts by specifying environments vars:

* `NODE_ENV` by default at `development`, `NODE_ENV=production` when you `npm run build-prod`
* `LINTER=false` will disable the linter (enabled by default, ex: `LINTER=false npm start`)
* `STATS=true` will write `stats.json` profiling file on disk from webpack at build (disabled by default, ex: `STATS=true npm run build`)
* `FAIL_ON_ERROR=true` will break the build if any errors occurs (useful for CIs such as travis - at `false` in dev-server, at `true` when building)
* `LOCALHOST=false` to access via IP from other devices on the same network (ex: `LOCALHOST=false npm start` - default `true`)
* `DEVTOOLS`: By default at `null`. Used internally by `npm run build-prod-all` (you may not need that if you don't do OSS)

### Assets

The main image loaders are declared in the webpack config so that when you `require('./foo.png')` or use the helper `url('./bar.gif')` in your `.scss` files, at build time, those images will automatically be copied into `resources` folder for clientlibs.

### Version/build info in output

Check the source code of the html/js/css generated files, you'll see a banner containing informations such as:

* date the build was made
* version
* git revision / link to this revision on github

## Tools

### Yarn

Intall yarn globally by running `npm i -g yarn`.


### Code splitting with `import()`

See https://webpack.js.org/guides/code-splitting/ - this is currently *not* supported because of how AEM clientlibs work. We will explore ways to achieve this.


### Clientlib Generation

Project will generate clientlibs based on the configuration in clientlib.config.js file. This is achieved using [aem-clientlib-webpack-plugin](https://www.npmjs.com/package/aem-clientlib-webpack-plugin). 


# LICENSE
See the LICENSE.md file for legal info
