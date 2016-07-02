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
    return (<route.component navigator={navigationOperations}/>);
}

var EntryClass = React.createClass({
    render: function () {
        let initialRoute = {component: TestEntry};
        return (
            <Navigator
                style={styles.container}
                initialRoute={initialRoute}
                configureScene={() => Navigator.SceneConfigs.PushFromRight}
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
