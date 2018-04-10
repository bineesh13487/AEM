const Path = require('path');
const Touch = require('touch');
const FS = require('fs-extra');
const _ = require('lodash');
const sqlite3 = require('sqlite3').verbose();
const isRoot = require('path-is-root');
const mm = require('micromatch');

class TouchFiles {
  // init function will touch files changed recently in the SVN project
  init(ageInMonths = 1, pattern = '**/jcr_root/apps/**/*.html') {
    const promise = new Promise((resolve, reject) => {
      const svnBaseDir = this.getSvnAbsDir();
      this.getSvnRevision(svnBaseDir).then(info => {
        console.log(`Current revision is: ${info.revision}`);
      }).catch(err => {
        console.error('Unable to get revision');
        reject(err);
      });
      this.getChangedFiles(svnBaseDir, ageInMonths).then(rows => {
        const changedHtmlFiles = mm(rows, pattern);
        const touchedFiles = this.touchFiles(changedHtmlFiles, svnBaseDir);
        resolve({ touchedFiles, svnBaseDir });
      }).catch(err => {
        console.trace(`Unable to touch files. ${err}`);
        reject(err);
      });
    });
    return promise;
  }

  touchFiles(paths, baseDir) {
    const base = baseDir || this.getSvnAbsDir();
    const touchedFiles = [];
    paths.forEach(path => {
      const filepath = Path.resolve(base, path);
      if (FS.existsSync(filepath)) {
        Touch.sync(filepath);
        touchedFiles.push(filepath);
      }
    });
    return touchedFiles;
  }

  getSvnRevision(path) {
    const promise = new Promise((resolve, reject) => {
      // console.log('opening db file: ' + Path.resolve(path, '.svn/wc.db'));
      const db = new sqlite3.Database(Path.resolve(path, '.svn/wc.db'), sqlite3.OPEN_READONLY, err => {
        if (err) {
          console.error('Unable to open svn db ');
          reject(err);
        }
      });

      db.get(`SELECT revision AS revision FROM NODES ORDER BY changed_date DESC LIMIT 1`, (err, row) => {
        if (err) {
          console.error('Unable to query svn db ');
          reject(err);
        }
        else {
          resolve(row);
        }
      });

      db.close(err => {
        if (err) {
          console.error('Unable to close svn db ');
          reject(err);
        }
      });
    });
    return promise;
  }

  getChangedFiles(dbfilepath, ageInMonth) {
    const ageInMonths = parseInt(ageInMonth, 10);
    const promise = new Promise((resolve, reject) => {
      const db = new sqlite3.Database(Path.resolve(dbfilepath, '.svn/wc.db'), sqlite3.OPEN_READONLY, err => {
        if (err) {
          console.error('Unable to open svn db ');
          reject(err);
        }
      });

      db.all(`SELECT DISTINCT local_relpath AS filepath
        FROM NODES WHERE changed_date > strftime('%s', 'now', '-${ageInMonths} month')*1000000
        ORDER BY changed_date DESC`, (err, rows) => {
        if (err) {
          console.error('Unable to query svn db ');
          reject(err);
        }
        else {
          resolve(_.map(rows, 'filepath'));
        }
      });

      db.close(err => {
        if (err) {
          console.error('Unable to close svn db ');
          reject(err);
        }
      });
    });
    return promise;
  }

  getSvnAbsDir() {
    let currentDir = process.cwd();
    while (!FS.existsSync(Path.resolve(currentDir, '.svn', 'wc.db'))) {
      currentDir = Path.resolve(currentDir, '..');
      if (isRoot(currentDir)) {
        throw new Error(`Not a SVN folder: ${process.cwd()}`);
      }
    }
    return currentDir;
  }
}

module.exports = TouchFiles;
