import {Injector} from 'ngES6';
import $ from 'jquery';

export default class ImageModal extends Injector {
    static $inject = ['$uibModal', '$timeout'];

    constructor(...args) {
        super(...args);
        this.restrict = 'A';
        this.link.$inject = ['scope', 'element'];
    }

    link(scope, element) {
        $(element).css({cursor: 'pointer'});
        if (element[0].complete) {
            this._show(element);
        } else {
            element.one('load', () => {
                this._show(element);
            });
        }
    }

    _show(element) {
        element.on('click', () => {
            const src = element.attr('src');
            const imgId = 'img' + Math.random().toString().substr(2);
            const maxWidth = $(window.document).width() - 100;

            this.$injected.$uibModal.open({
                size     : 'lg',
                animation: true,
                template : `<img style="display: none;" id="${imgId}" src="${src}">`,
            }).rendered.then(() => {
                const el = $('#' + imgId);
                let width = el[0].naturalWidth;
                if (width > maxWidth) width = maxWidth;
                el.closest('.modal-dialog').width(width);
                el.css({width: width, display: ''});
            });
        });
    }
}
