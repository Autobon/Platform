/**
 * Created by dave on 16/1/25.
 */
import { load } from 'angular-es6';
const MODULE_NAME = 'app.filters';

load.filters(require.context('./', true, /.*\.js$/), MODULE_NAME);
export default MODULE_NAME;