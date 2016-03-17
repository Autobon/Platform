/**
 * Created by dave on 16/3/17.
 */
export default function httpConfig($httpProvider) {
    let transformParams = (obj) => {
        let query = '', name, value, fullSubName, subName, subValue, innerObj;

        for (name in obj) {
            value = obj[name];

            if (value instanceof Array) {
                for (let i = 0; i < value.length; ++i) {
                    subValue = value[i];
                    fullSubName = name + '[' + i + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += transformParams(innerObj) + '&';
                }
            } else if (value instanceof Object) {
                for (subName in value) {
                    subValue = value[subName];
                    fullSubName = name + '[' + subName + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += transformParams(innerObj) + '&';
                }
            } else if (value !== undefined && value !== null) {
                query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
            }
        }

        return query.length ? query.substr(0, query.length - 1) : query;
    };

    // setup params data encode
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [(data) => {
        return typeof data === 'object' && String(data) !== '[object File]' ? transformParams(data) : data;
    }];
}

httpConfig.$inject = ['$httpProvider'];
