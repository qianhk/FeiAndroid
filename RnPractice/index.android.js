/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {
    Component,
} from 'react';

import {
    AppRegistry,
    Image,
    ListView,
    StyleSheet,
    Text,
    View,
} from 'react-native';




const MeiTuan = require('./meituan');
const ZhiHuDaily = require('./zhihu/ZhihuDaily');
import MovieFetcher from './MovieFetcher';
const MoviesApp = require('./movie/MoviesApp.android');

AppRegistry.registerComponent('RnPractice', () => MoviesApp);
