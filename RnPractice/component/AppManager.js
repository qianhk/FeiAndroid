/**
 * Created by ttkai on 16-7-7.
 */

'use strict';

import React, {
    PropTypes,
} from 'react';

import {NativeModules} from 'react-native';

var RCTAppManager = NativeModules.TestAppManager;

RCTAppManager.propTypes = {
    restartPage: PropTypes.func
};

module.exports = RCTAppManager;
