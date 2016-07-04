/* @flow */

'use strict';

import React, {Component} from 'React';
import App from './App';
import configureStore from './configureStore'
import {Provider} from 'react-redux';
import {StyleSheet, View} from 'react-native';


export default class extends Component {

    constructor() {
        super();
        this.state = {
            isLoading: false,
            store: configureStore(()=> this.setState({isLoading: false})),
        };
    }

    render() {
        return (
            <Provider store={this.state.store}>
                <App store={this.state.store}/>
            </Provider>
        );
    }
}

