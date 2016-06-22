'use strict';

// var React = require('react-native');
// var {
//     AsyncStorage,
//     Image,
//     StyleSheet,
//     Text,
//     View,
//     Dimensions,
// } = React;

import React, {
    Component,
} from 'react';

import {
    AsyncStorage,
    Image,
    ListView,
    StyleSheet,
    Text,
    View,
    Dimensions,
} from 'react-native';

var Animated = require('Animated');

var WINDOW_WIDTH = Dimensions.get('window').width;

var DataRepository = require('./DataRepository');
var repository = new DataRepository();

var SplashScreen = React.createClass({
    fetchData: function () {
        repository.getCover()
            .then((result) => {
                if (result) {
                    this.setState({cover: result});
                }
            })
            .catch((error) => {
                console.error(error);
            })
            .done();
        repository.updateCover();
    },
    getInitialState: function () {
        return {
            cover: null,
            bounceValue: new Animated.Value(1),
        };
    },
    componentDidMount: function () {
        // setTimeout(() => this.fetchData(), 1000);
        this.fetchData();
        this.state.bounceValue.setValue(1);
        Animated.timing(
            this.state.bounceValue,
            {
                toValue: 1.2,
                duration: 5000,
            }
        ).start();
    },
    render: function () {
        var img, text;
        if (this.state.cover) {
            img = {uri: this.state.cover.img};
            text = this.state.cover.text;
            // console.log('run over conver')
        } else {
            // console.log('run over no conver')
            // img = require('image!splash');
            img = require("../pic/splash.png");
            text = '';
        }

        return (
            <View style={styles.container}>
                <Animated.Image
                    source={img}
                    style={{
            flex: 1,
            width: WINDOW_WIDTH,
            height: 1,
            transform: [
              {scale: this.state.bounceValue},
            ]
          }}/>
                <Text style={styles.text}>
                    {text}
                </Text>
                <Image style={styles.logo} source={require('../pic/splash_logo.png')}/>
            </View>
        );
    }
});

var styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        // backgroundColor: '#F0F',
    },
    cover: {
        flex: 1,
        width: 200,
        height: 1,
    },
    logo: {
        resizeMode: 'contain',
        position: 'absolute',
        left: 0,
        right: 0,
        bottom: 36,
        height: 54,
        backgroundColor: 'transparent',
    },
    text: {
        flex: 1,
        fontSize: 16,
        textAlign: 'center',
        color: 'white',
        position: 'absolute',
        left: 0,
        right: 0,
        bottom: 10,
        backgroundColor: 'transparent',
    }
});

module.exports = SplashScreen;
