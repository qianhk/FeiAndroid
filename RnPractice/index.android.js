/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {} from 'react';

import {
    AppRegistry,
    BackAndroid,
    Navigator,
    StyleSheet,
} from 'react-native';


const MeiTuan = require('./meituan');
const ZhiHuDaily = require('./zhihu/ZhihuDaily');
import MovieFetcher from './MovieFetcher';
const MoviesApp = require('./movie/MoviesApp');
const TicTacToeApp = require('./TicTacToeApp');
const UIExplorerApp = require('./UIExplorer/UIExplorerApp');
const Game2048 = require('./2048/Game2048');
import TestEntry from './TestEntry';

var _navigator;

BackAndroid.addEventListener('hardwareBackPress', function () {
    if (_navigator && _navigator.getCurrentRoutes().length > 1) {
        _navigator.pop();
        return true;
    }
    return false;
});

var RouteMapper = function (route, navigationOperations, onComponentRef) {
    _navigator = navigationOperations;
    if (route.name === "testEntry") {
        return (<TestEntry navigator={navigationOperations}/>);
    } else if (route.name === 'movieFetch') {
        return (<MovieFetcher navigator={navigationOperations}/>);
    } else if (route.name === 'game2048') {
        return (<Game2048 navigator={navigationOperations}/>);
    }
}

var EntryClass = React.createClass({
    render: function () {
        var initialRoute = {name: 'testEntry'};
        return (
            <Navigator
                style={styles.container}
                initialRoute={initialRoute}
                configureScene={() => Navigator.SceneConfigs.HorizontalSwipeJump}
                renderScene={RouteMapper}
            />
        );
    }
});

var styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
    },
});


AppRegistry.registerComponent('RnPractice', () => EntryClass);
