import { load } from 'angular-es6';

const MODULE_NAME = 'app.factories';

load.factories(require.context('./', true, /.*\.js$/), MODULE_NAME);

export default MODULE_NAME;
