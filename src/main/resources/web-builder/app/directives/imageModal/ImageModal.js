import {Injector} from 'ngES6';
import $ from 'jquery';

export default class ImageModal extends Injector {
    static $inject = ['$uibModal', '$timeout'];

    constructor(...args) {
        super(...args);
        this.restrict = 'A';
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
            const src      = element.attr('src');
            const imgId    = 'img' + Math.random().toString().substr(2);
            const maxWidth = $(window.document).width() - 100;

            let modal = this.$injected.$uibModal.open({
                size     : 'lg',
                animation: true,
                template : `<div style="display: none; position: relative;">
                        <img id="${imgId}" src="${src}">
                        <div>&times;</div>
                    </div>`,
            });
            modal.rendered.then(() => {
                const el  = $('#' + imgId);
                let width = el[0].naturalWidth;
                if (width > maxWidth) width = maxWidth;
                el.closest('.modal-dialog').width(width);
                el.css({width: width});
                el.parent().find('div').css({
                    top          : 0,
                    right        : 0,
                    width        : '40px',
                    height       : '40px',
                    color        : '#FFF',
                    cursor       : 'pointer',
                    position     : 'absolute',
                    background   : 'rgba(0,0,0,0.5)',
                    'font-size'  : '30px',
                    'line-height': '30px',
                    'text-align' : 'center',
                }).click(() => {
                    modal.close();
                });
                el.parent().css('display', '');
            });
        });
    }
}
