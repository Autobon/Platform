import {Injector} from 'ngES6';
import $ from 'jquery';

export default class Resize extends Injector {
    static $inject = ['$timeout'];

    constructor(...args) {
        super(...args);
        this.restrict = 'A';
        this.scope    = {
            resize: '@',
        };

        this.link.$inject = ['scope', 'element'];
    }

    link(scope, element) {
        let width,
            height,
            ele  = $(element),
            size = scope.resize.split(',');

        width = size[0].trim();
        if (size.length === 1) {
            height = width;
        } else {
            height = size[1].trim();
        }

        if (ele[0].tagName === 'IMG' && !ele[0].complete) {
            ele.css({'display': 'none'});
            ele.on('load', () => {
                this.$injected.$timeout(() => {
                    this.setSize(ele[0], width, height);
                    ele.css({'display': ''});
                });
            });
        } else {
            this.setSize(ele[0], width, height);
        }
    }

    setSize(ele, width, height) {
        let _width, _height;

        if (ele.tagName === 'IMG') {
            _width  = ele.naturalWidth;
            _height = ele.naturalHeight;
        } else {
            _width  = $(ele).width();
            _height = $(ele).height();
        }

        if (/^\d+%$/.test(width)) {
            $(ele).width(_width * parseInt(width, 10) / 100);
        } else if (/^\d+(px)?$/.test(width)) {
            $(ele).width(width);
        }

        if (/^\d+%$/.test(height)) {
            $(ele).height(_height * parseInt(height, 10) / 100);
        } else if (/^\d+(px)?$/.test(height)) {
            $(ele).height(height);
        }
    }


}
