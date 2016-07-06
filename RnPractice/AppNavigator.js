/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {BackAndroid} from 'react-native';
import Navigator from 'Navigator';

import TestEntry from './TestEntry';

export var globalNav = {};

var _navigator;

BackAndroid.addEventListener('hardwareBackPress', function () {
    if (_navigator && _navigator.getCurrentRoutes().length > 1) {
        _navigator.pop();
        return true;
    }
    return false;
});

const searchResultRegexp = /^search\/(.*)$/;

const reducerCreate = params=> {
    const defaultReducer = Reducer(params);
    return (state, action)=> {
        var currentState = state;

        if (currentState) {
            while (currentState.children) {
                currentState = currentState.children[currentState.index]
            }
        }
        return defaultReducer(state, action);
    }
};

var _routeMapper = function (route, navigationOperations, onComponentRef) {
    _navigator = navigationOperations;
    if (route.component == TestEntry) {
        // console.info("AppNavigator function is TestEntry", route)
        return (<TestEntry outprops={route.outprops} navigator={navigationOperations}/>);
    } else {
        // console.info("AppNavigator function is not TestEntry", route)
        return (<route.component navigator={navigationOperations}/>);
    }
};


class AppNavigator extends Component {

    constructor(props) {
        super(props);
        console.info("AppNavigator constructor ", props);
    }

    componentDidMount() {
        globalNav.navigator = this._navigator;
        // this.props.store.subscribe(() => {
        //     console.log("store changed", this.props.store.getState());
        //     if (this.props.store.getState().drawer.drawerState == 'opened')
        //         this.openDrawer();
        //
        //     if (this.props.store.getState().drawer.drawerState == 'closed')
        //         this._drawer.close()
        // });

        // BackAndroid.addEventListener('hardwareBackPress', () => {
        //     var routes = this._navigator.getCurrentRoutes();
        //
        //     if (routes[routes.length - 1].id == 'home' || routes[routes.length - 1].id == 'login') {
        //         return false;
        //     }
        //     else {
        //         this.popRoute();
        //         return true;
        //     }
        // });
    }

    popRoute() {
        this.props.popRoute();
    }

    render() {
        let initialRoute = {component: TestEntry, outprops: this.props.outprops};
        return (
            <Navigator
                // ref={(ref) => this._navigator = ref}
                configureScene={() => Navigator.SceneConfigs.PushFromRight}
                initialRoute={initialRoute}
                renderScene={_routeMapper}
            />
        );
    }

    renderSceneRouteMapper(route, navigator) {
        switch (route.id) {
            case 'login':
                return <Login navigator={navigator}/>;
            case 'home':
                return <Home navigator={navigator}/>;
            case 'blankPage':
                return <BlankPage navigator={navigator}/>;
            default :
                return <Login navigator={navigator}/>;
        }
    }
}

function bindAction(dispatch) {
    return {
        closeDrawer: () => dispatch(closeDrawer()),
        popRoute: () => dispatch(popRoute())
    }
}

// const mapStateToProps = (state) => {
//     return {
//         state: state
//     }
// }

// export default connect(mapStateToProps, bindAction)(AppNavigator);
export default AppNavigator;
