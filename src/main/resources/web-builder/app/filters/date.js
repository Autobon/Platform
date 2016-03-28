import moment from 'moment';
export default function date() {
    return function(input, format) {
        return input && moment(input).isValid() ? moment(input).format(format) : input;
    };
}
