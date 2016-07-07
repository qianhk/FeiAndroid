/* @flow */

'use strict';
import React, {Component} from "react";
import CodePush from 'react-native-code-push';
import {StyleSheet, Text, View, AppState} from 'react-native';
import AppNavigator from './AppNavigator';

class App extends Component {
    constructor(props) {
        super(props);
        this.handleAppStateChange = this.handleAppStateChange.bind(this);
    }

    componentDidMount() {
        AppState.addEventListener('change', this.handleAppStateChange);
        if (!this.props.outprops.native_build_debug) {
            CodePush.notifyAppReady();
        }
    }

    componentWillUnmount() {
        AppState.removeEventListener('change', this.handleAppStateChange);
    }

    handleAppStateChange(state:string) {
        if (state === 'active') {
            if (this.props.outprops.native_build_debug) {
                console.info("app-in-js native_build_debug yes");
            } else {
                console.info("app-in-js native_build_debug no");
                CodePush.sync({installMode: CodePush.InstallMode.IMMEDIATE, minimumBackgroundDuration: 0});
                // if (stage) {
                //     CodePush.sync({installMode: CodePush.InstallMode.IMMEDIATE, minimumBackgroundDuration: 0});
                // } else {
                //     CodePush.sync({installMode: CodePush.InstallMode.ON_NEXT_RESTART, minimumBackgroundDuration: 60 * 5});
                // }
            }
        }
    }

    render() {
        return (
            <AppNavigator store={this.props.store} outprops={this.props.outprops}/>
        );
    }
}

export default App
