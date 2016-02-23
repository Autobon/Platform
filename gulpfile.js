var gulp = require('gulp');
var newer = require('gulp-newer');
var changed = require('gulp-changed');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var sourcemaps = require('gulp-sourcemaps');
var del = require('del');
var path = require('path');
var debug = require('gulp-debug');

var paths = {};

paths.requirejs = {
    src: 'src/main/resources/static/script/lib/requirejs/require.js',
    dst: 'src/main/resources/static/script/lib/requirejs/'
}
gulp.task('require.min.js', function(){
    return gulp.src(paths.requirejs.src)
        .pipe(newer(path.join(paths.requirejs.dst,'require.min.js')))
        .pipe(sourcemaps.init())
        .pipe(uglify())
        .pipe(concat('require.min.js'))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(paths.requirejs.dst));
});

paths.chai = {
    src: 'src/main/resources/static/script/lib/chai/chai.js',
    dst: 'src/main/resources/static/script/lib/chai/'
}
gulp.task('chai.min.js', function(){
    return gulp.src(paths.chai.src)
        .pipe(newer(path.join(paths.chai.dst,'chai.min.js')))
        .pipe(sourcemaps.init())
        .pipe(uglify())
        .pipe(concat('chai.min.js'))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(paths.chai.dst));
});

gulp.task('clean-min-js', function(){
    del([
        path.join(paths.requirejs.dst, 'require.min.js'),
        path.join(paths.requirejs.dst, 'require.min.js.map'),
        path.join(paths.chai.dst, 'chai.min.js'),
        path.join(paths.chai.dst, 'chai.min.js.map'),
    ]);
});

gulp.task('build', ['require.min.js', 'chai.min.js']);

gulp.task('clean-log', function(){
    del(['Rental.log', 'Rental.log.*']);
});

gulp.task('clean', ['clean-min-js', 'clean-log']);

paths.static = {
    src: 'src/main/resources/static/**/*',
    dst: 'build/resources/main/static/'
}
gulp.task('hotswap-static', function(){
    return gulp.src(paths.static.src)
        .pipe(changed(paths.static.dst))
        .pipe(debug({ title: 'hotswap detected : ' }))
        .pipe(gulp.dest(paths.static.dst));
});

gulp.task('watch', function(){
    gulp.watch([paths.static.src], ['hotswap-static']);
});

gulp.task('default', ['build']);