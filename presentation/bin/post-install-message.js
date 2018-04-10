const log = require('npmlog');
log.level = 'silly';

log.info('dependency', 'post install message:');
log.info('dependency', `run \`yarn start\` now`);
console.log('');
