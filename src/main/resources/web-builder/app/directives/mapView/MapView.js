import {Injector} from 'ngES6';
import $ from 'jquery';

export default class MapView extends Injector {
    static $inject = [];

    constructor(...args) {
        super(...args);
        this.template = '<div></div>';
        this.replace  = true;
        this.restrict = 'EA';
        this.scope    = {
            center   : '@',
            apiKey   : '@',
            positions: '@',
            onSelect : '&',
            iconUrl  : '@',
        };
    }

    link(scope, element) {
        let mapId = element.attr('id');
        if (!mapId) {
            mapId = 'map' + Math.random().toString().substr(2);
            element.attr('id', mapId);
        }

        if (scope.apiKey) {
            const callback = 'mapcb' + Math.random().toString().substr(2);
            $('body').append($(`<script src="http://api.map.baidu.com/api?v=2.0&ak=${scope.apiKey}&callback=${callback}"></script>`));
            window[callback] = () => {
                this._showPositions(scope, mapId);
                scope.$watch('positions', () => {
                    this._showPositions(scope, mapId);
                });
            };
        } else {
            this.$injected.$timeout(() => {
                this._showPositions(scope, mapId);
                scope.$watch('positions', () => {
                    this._showPositions(scope, mapId);
                });
            });
        }
    }

    _showPositions(scope, mapId) {

    }
}
