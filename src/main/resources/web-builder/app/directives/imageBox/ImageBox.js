import {Injector} from 'ngES6';

export default class ImageBox extends Injector {
    static $inject = [];
    static defaults = {
        height: 90,
    };

    constructor(...args) {
        super(...args);
    }

    link() {}
}
