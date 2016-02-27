/**
 * Created by Dave on 2016/1/25.
 */
import { load } from 'angular-es6';
const MODULE_NAME = 'app.services';

load.services(require.context('./', true, /.*\.js$/), MODULE_NAME);
export default MODULE_NAME;
