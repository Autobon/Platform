import {Injector} from 'ngES6';
import $ from 'jquery';
import template from './uploader.html';

export default class FileUploader extends Injector {
    static $inject = ['$http', '$parse', '$timeout'];

    constructor(...args) {
        super(...args);

        this.template     = template;
        this.replace      = true;
        this.require      = 'ngModel';
        this.restrict     = 'EA';
        this.scope        = {
            name     : '@',
            accept   : '@',
            callback : '&',
            imageUrl : '=ngModel',
            uploadUrl: '@',
        };
    }

    link(scope, element) {
        let file  = $(element).find('input:file');

        file.on('click', e => {
            e.stopPropagation();
        });
        $(element).on('click', () => {
            file.click();
        });

        file.on('change', () => {
            if (file[0].files.length) {
                let form = new FormData();
                form.append(scope.name, file[0].files[0]);
                this.$injected.$http({
                    url             : scope.uploadUrl,
                    method          : 'POST',
                    headers         : {'Content-Type': undefined},
                    data            : form,
                    transformRequest: (data) => data,
                }).then(res => {
                    scope.uploading = false;
                    if (res.data) {
                        const url      = scope.callback({data: res.data});
                        scope.imageUrl = url;
                    } else {
                        console.log(res);
                    }
                });
                scope.uploading = true;
            }
        });
    }
}
