/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {} from 'react';

import {
    AppRegistry,
} from 'react-native';


const MeiTuan = require('./meituan');
const ZhiHuDaily = require('./zhihu/ZhihuDaily');
import MovieFetcher from './MovieFetcher';
const MoviesApp = require('./movie/MoviesApp.android');
const TicTacToeApp = require('./TicTacToeApp');
const UIExplorerApp = require('./UIExplorer/UIExplorerApp.android');
const Game2048 = require('./2048/Game2048');

AppRegistry.registerComponent('RnPractice', () => ZhiHuDaily);
