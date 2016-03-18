/**
 * Created by dave on 16/1/25.
 */
import { loader } from 'ngES6';
const MODULE_NAME = 'app.directives';

loader.directives(require.context('./', true, /^(?!\.\/index\.js$).+\.js$/), MODULE_NAME);
export default MODULE_NAME;
