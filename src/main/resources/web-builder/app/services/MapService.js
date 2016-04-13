import {Injector} from 'ngES6';
import angular from 'angular';
import $ from 'jquery';
import './map-style.css';

export default class MapService extends Injector {
    static $inject = [];

    BackButtonCtrl = class extends window.BMap.Control {
        constructor() {
            super();
            this.defaultAnchor = window.BMAP_ANCHOR_TOP_LEFT;
            this.defaultOffset = new window.BMap.Size(10, 20);
        }

        initialize(map) {
            this.map = map;
            let div = $('<div>返回</div>').css({
                background   : '#FFF',
                color        : '#000',
                cursor       : 'pointer',
                padding      : '0 10px',
                border       : '1px solid #CCC',
                'font-size'  : '12px',
                'line-height': '22px',
            });
            div.on('click', () => {
                console.log(angular.element(div).scope());
                angular.element(div).scope().$emit('map.action.back', map);
            });
            map.getContainer().appendChild(div[0]);
            return div[0];
        }
    };
    
    FullScreenOverlay = class extends window.BMap.Control {

    };

    HtmlMarkerOverlay = class extends window.BMap.Overlay {
        constructor(scope, point, template, offset) {
            super();
            this.scope    = scope;
            this.point    = point;
            this.template = template;
            this.offset   = offset || {};
        }

        initialize(map) {
            const $injector    = angular.injector(['ng']);
            const $compile     = $injector.get('$compile');
            const $interpolate = $injector.get('$interpolate');
            this.map           = map;
            let template       = this.template || `<div class="mv-marker">
                                                            <span>★</span>
                                                            <div class="arrow"></div>
                                                        </div>`;
            let div            = this.div = $($compile($interpolate(template)(this.scope))(this.scope));
            div.click(e => {
                this.scope.$emit('map.marker.click', e);
            }).mouseenter(e => {
                this.scope.$emit('map.marker.mouseenter', e);
            }).mouseleave(e => {
                this.scope.$emit('map.marker.mouseleave', e);
            });
            map.getPanes().markerPane.appendChild(div[0]);
            return div[0];
        }

        draw() {
            let pixel = this.map.pointToOverlayPixel(this.point);

            if (this.offset.x === undefined) {
                this.offset.x = -10;
            } else if (typeof this.offset.x === 'function') {
                this.offset.x = this.offset.x(this.div.width());
            }
            if (this.offset.y === undefined) {
                this.offset.y = -(this.div.height() + 10);
            } else if (typeof this.offset.y === 'function') {
                this.offset.y = this.offset.y(this.div.height());
            }

            this.div.css({
                left: pixel.x + this.offset.x,
                top : pixel.y + this.offset.y,
            });
        }

        getPosition() {
            return this.point;
        }

        getMap() {
            return this.map;
        }
    };
}
