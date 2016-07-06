/* @flow */

'use strict';
import React,{Component} from "react";
import CodePush from 'react-native-code-push';
import { StyleSheet, Text, View, AppState} from 'react-native';
import AppNavigator from './AppNavigator';

class App extends Component {
    constructor(props) {
        super(props);
        this.handleAppStateChange = this.handleAppStateChange.bind(this);
    }

    componentDidMount() {
        AppState.addEventListener('change', this.handleAppStateChange);
        // CodePush.notifyAppReady();
    }

    componentWillUnmount() {
        AppState.removeEventListener('change', this.handleAppStateChange);
    }

    handleAppStateChange(state:string) {
        if (state === 'active') {
            if (this.props.outprops.native_build_debug) {
                console.warn("app-in-js native_build_debug yes");
            } else {
                console.warn("app-in-js native_build_debug no");
                CodePush.sync({installMode: CodePush.InstallMode.IMMEDIATE, minimumBackgroundDuration: 60 * 5});
            }
        }
    }
    render() {
        return (
            <AppNavigator store={this.props.store} outprops={this.props.outprops} />
        );
    }
}

export default App
