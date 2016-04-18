import {Injector} from 'ngES6';
import $ from 'jquery';
import './box.scss';

export default class ImageBox extends Injector {
    static $inject = ['$uibModal', '$timeout'];

    constructor(...args) {
        super(...args);
        this.template = '<div class="image-box"></div>';
        this.replace  = true;
        this.restrict = 'EA';
        this.scope    = {
            url: '@',
        };
    }

    link(scope, ele) {
        let element = scope.element = $(ele);
        element.css({cursor: 'pointer'});

        scope.image        = new Image();
        scope.image.src    = scope.url;
        scope.image.onload = () => {
            let img         = $(scope.image);
            scope.imgWidth  = img[0].naturalWidth;
            scope.imgHeight = img[0].naturalHeight;

            element.css({
                visibility        : 'visible',
                'background-image': 'url(' + scope.url + ')',
                'background-size' : 'cover',
            }).on('click', () => {
                this.onclick(scope.url);
            }).on('mousemove', e => {
                this.onmousemove(e, scope);
            });
        };

        const resizeEvent = 'resize.ib' + Math.random().toString().substr(2);
        $(window).on(resizeEvent, () => {
            element.css('background-position', '0 0');
        });
        scope.$on('$destroy', () => {
            console.log('event callback distached: ' + resizeEvent);
            $(window).off(resizeEvent);
        });
    }

    onmousemove(e, scope) {
        let elWidth   = scope.element.width();
        let elHeight  = scope.element.height() || parseInt(scope.element.css('line-height'), 10);
        let direction = scope.imgWidth / scope.imgHeight > elWidth / elHeight ? 'H' : 'V';
        let maxOffset = direction === 'H' ? (elHeight / scope.imgHeight) * scope.imgWidth - elWidth : (elWidth / scope.imgWidth) * scope.imgHeight - elHeight;
        if (direction === 'H') {
            scope.element.css('background-position-x', -Math.min(e.offsetX / elWidth * maxOffset, maxOffset) + 'px');
        } else {
            scope.element.css('background-position-y', -Math.min(e.offsetY / elHeight * maxOffset, maxOffset) + 'px');
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
