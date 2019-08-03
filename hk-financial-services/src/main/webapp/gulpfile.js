var gulp = require('gulp'),
    less = require('gulp-less'),
    sourcemaps = require('gulp-sourcemaps');

gulp.task('testLess', function () {
    gulp.src('src/less/*.less')
    	.pipe(sourcemaps.init())
        .pipe(less())
        .pipe(sourcemaps.write())
        .pipe(gulp.dest('src/css'));
});

gulp.task('testWatch', function () {
    gulp.watch('src/less/*.less', ['testLess']); //当所有less文件发生改变时，调用testLess任务
});
