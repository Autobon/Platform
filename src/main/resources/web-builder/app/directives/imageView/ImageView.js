import {Injector} from 'ngES6';
import $ from 'jquery';

export default class ImageView extends Injector {
    static $inject = ['$uibModal', '$timeout'];

    constructor(...args) {
        super(...args);
        this.restrict = 'A';
        this.link.$inject = ['scope', 'element'];
    }

    link(scope, element) {
        if (element[0].complete) {
            this._show(element);
        } else {
            element.one('load', () => {
                this._show(element);
            });
        }
    }

    _show(element) {
        const src = element.attr('src');
        const imgId = 'img' + Math.random().toString().substr(2);
        const maxWidth = $(window.document).width() - 20;

        console.log(element[0].naturalWidth);

        element.on('click', () => {
            this.$injected.$uibModal.open({
                size     : 'lg',
                animation: true,
                template : `<img id="${imgId}" src="${src}">`,
            }).rendered.then(() => {
                const el = $('#' + imgId);
                let width = el[0].naturalWidth;
                if (width > maxWidth) width = maxWidth;
                el.width(width);
                el.closest('.modal-dialog').width(width);
            });
        });
    }
}
