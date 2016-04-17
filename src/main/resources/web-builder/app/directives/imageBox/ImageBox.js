import {Injector} from 'ngES6';
import $ from 'jquery';
import './box.scss';

export default class ImageBox extends Injector {
    static $inject = ['$uibModal'];

    constructor(...args) {
        super(...args);
        this.template = '<div class="image-box"></div>';
        this.replace  = true;
        this.restrict = 'EA';
        this.scope    = {
            url   : '@',
            width : '@',
            height: '@',
        };
    }

    link(scope, ele) {
        let element = scope.element = $(ele);
        element.css({cursor: 'pointer'});

        scope.image        = new Image();
        scope.image.src    = scope.url;
        scope.image.onload = () => {
            let img       = $(scope.image);
            let imgWidth  = img[0].naturalWidth;
            let imgHeight = img[0].naturalHeight;
            let elWidth   = scope.elWidth = parseInt(scope.width || element.width(), 10);
            let elHeight = scope.elHeight = parseInt(scope.height || element.height() || parseInt(element.css('line-height'), 10), 10);
            scope.direction = imgWidth / imgHeight > elWidth / elHeight ? 'H' : 'V';
            scope.maxOffset = scope.direction === 'H' ? (elHeight / imgHeight) * imgWidth - elWidth : (elWidth / imgWidth) * imgHeight - elHeight;
            element.css({
                visibility        : 'visible',
                width             : elWidth,
                height            : elHeight,
                'background-image': 'url(' + scope.url + ')',
                'background-size' : 'cover',
            }).on('click', () => {
                this.onclick(scope.url);
            }).on('mousemove', e => {
                this.onmousemove(e, scope);
            });
        };
    }

    onmousemove(e, scope) {
        if (scope.direction === 'H') {
            scope.element.css('background-position-x', -Math.min(e.offsetX / scope.elWidth * scope.maxOffset, scope.maxOffset) + 'px');
        } else {
            scope.element.css('background-position-y', -Math.min(e.offsetY / scope.elHeight * scope.maxOffset, scope.maxOffset) + 'px');
        }
    }

    onclick(url) {
        const src      = url;
        const imgId    = 'img' + Math.random().toString().substr(2);
        const maxWidth = $(window.document).width() - 100;

        let modal = this.$injected.$uibModal.open({
            size     : 'lg',
            animation: true,
            template : `<div style="display: none; position: relative;">
                        <img id="${imgId}" src="${src}">
                        <div class="image-modal-close">&times;</div>
                    </div>`,
        });
        modal.rendered.then(() => {
            const el  = $('#' + imgId);
            let width = el[0].naturalWidth;
            if (width > maxWidth) width = maxWidth;
            el.closest('.modal-dialog').width(width);
            el.css({width: width});
            el.parent().find('div').click(() => {
                modal.close();
            });
            el.parent().css('display', '');
        });
    }
}
