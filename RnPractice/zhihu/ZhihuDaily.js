/**
 * Created by ttkai on 16-6-22.
 */

'use strict';

import React, {
    Component,
} from 'react';

import {
    Image,
    StyleSheet,
    Text,
    View,
    Navigator,
} from 'react-native';

var SplashScreen = require('./SplashScreen');
var TimerMixin = require('react-timer-mixin');
var MainScreen = require('./MainScreen');
var StoryScreen = require('./StoryScreen');

var _navigator;

BackAndroid.addEventListener('hardwareBackPress', function () {
    if (_navigator && _navigator.getCurrentRoutes().length > 1) {
        _navigator.pop();
        return true;
    }
    return false;
});

class RCTZhiHuDaily extends Component {
    // mixins: [TimerMixin]

    constructor() {
        super()
        // this.render = this.render.bind(this)
        this.state = {
            splashed: false,
        }
        // setTimeout = setTimeout.bind(this);
        // this.setState = this.setState.bind(this);
        // this.componentDidMount = this.componentDidMount.bind(this);
    }

    componentDidMount() {
        // let self = this;
        // setTimeout(function () {
        //     self.setState({splashed: true});
        // }, 2000);
        setTimeout(() => this.setState({splashed: true}), 2000);
    }

    RouteMapper(route, navigationOperations, onComponentRef) {
        _navigator = navigationOperations;
        if (route.name === 'home') {
            return (
                <View style={styles.container}>
                    <MainScreen navigator={navigationOperations}/>
                </View>
            );
        } else if (route.name === 'story') {
            return (
                <View style={styles.container}>
                    <StoryScreen
                        style={{flex: 1}}
                        navigator={navigationOperations}
                        story={route.story}/>
                </View>
            );
        }
    }

    onActionSelected(position) {
    }

    render() {
        if (this.state.splashed) {
            var initialRoute = {name: 'home'};
            return (
                <Navigator
                    style={styles.container}
                    initialRoute={initialRoute}
                    configureScene={() => Navigator.SceneConfigs.FadeAndroid}
                    renderScene={this.RouteMapper}
                />
            );
        } else {
            return (
                <SplashScreen />
            );
        }
    }
}

var styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});

module.exports = RCTZhiHuDaily;
