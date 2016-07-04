/* @flow */

'use strict';
import React,{Component} from "react";
import CodePush from 'react-native-code-push';
import { StyleSheet, Text, View, AppState} from 'react-native';
import AppNavigator from './AppNavigator';

class App extends Component {
    componentDidMount() {
        AppState.addEventListener('change', this.handleAppStateChange);
        CodePush.notifyApplicationReady();
    }

    componentWillUnmount() {
        AppState.removeEventListener('change', this.handleAppStateChange);
    }

    handleAppStateChange(state:string) {
        if (state === 'active') {
            CodePush.sync({installMode: CodePush.InstallMode.IMMEDIATE});
        }
    }
    render() {
        return (
            <AppNavigator store={this.props.store} />
        );
    }
}

export default App
