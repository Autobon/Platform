/**
 * Created by Dave on 2016/1/25.
 */
import { loader } from 'ngES6';
const MODULE_NAME = 'app.services';

loader.services(require.context('./', true, /^(?!\.\/index\.js$).+\.js$/), MODULE_NAME);
export default MODULE_NAME;
