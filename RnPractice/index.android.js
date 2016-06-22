/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {
    Component,
} from 'react';

import {
    AppRegistry,
    Image,
    ListView,
    StyleSheet,
    Text,
    View,
} from 'react-native';


var API_KEY = '7waqfqbprs7pajbz28mqf6vz';
var API_URL = 'http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json';
var PAGE_SIZE = 25;
var PARAMS = '?apikey=' + API_KEY + '&page_limit=' + PAGE_SIZE;
var REQUEST_URL = API_URL + PARAMS;

class AwesomeProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dataSource: new ListView.DataSource({
                rowHasChanged: (row1, row2) => row1 !== row2,
            }),
            loaded: false,
        };
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        fetch(REQUEST_URL)
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({
                    dataSource: this.state.dataSource.cloneWithRows(responseData.movies),
                    loaded: true,
                });
            })
            .done();
    }

    render() {
        if (!this.state.loaded) {
            return this.renderLoadingView();
        }

        return (
            <ListView
                dataSource={this.state.dataSource}
                renderRow={this.renderMovie}
                style={styles.listView}
            />
        );
    }

    renderLoadingView() {
        return (
            <View style={styles.container}>
                <Text>
                    Loading movies...
                </Text>
            </View>
        );
    }

    renderMovie(movie) {
        return (
            <View style={styles.container}>
                <Image
                    source={{uri: movie.posters.thumbnail}}
                    style={styles.thumbnail}
                />
                <View style={styles.rightContainer}>
                    <Text style={styles.title}>{movie.title}</Text>
                    <Text style={styles.year}>{movie.year}</Text>
                </View>
            </View>
        );
    }
}
;

var styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    rightContainer: {
        flex: 1,
    },
    title: {
        fontSize: 20,
        marginBottom: 8,
        textAlign: 'center',
    },
    year: {
        textAlign: 'center',
    },
    thumbnail: {
        width: 53,
        height: 81,
    },
    listView: {
        paddingTop: 20,
        backgroundColor: '#F5FCFF',
    },
});

var styles = StyleSheet.create({
    style_1: {
        height: 40,
        borderWidth: 1,
        borderColor: 'red',
    }
});

var HelloWorld = React.createClass({
    render: function () {
        return (
            <View >
                <View style={styles.style_1}></View>
            </View>
        );
    }
});

var SplashScreen = require('./SplashScreen');
var TimerMixin = require('react-timer-mixin');

var RCTZhiHuDaily = React.createClass({
    mixins: [TimerMixin],
    componentDidMount: function () {
        this.setTimeout(
            () => {
                this.setState({splashed: true});
            },
            2000,
        );
    },
    RouteMapper: function (route, navigationOperations, onComponentRef) {
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
    },
    getInitialState: function () {
        return {
            splashed: false
        };
    },
    onActionSelected: function (position) {
    },
    render: function () {
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
});

AppRegistry.registerComponent('RnPractice', () => RCTZhiHuDaily);

// import React, { Component } from 'react';
// import {
//   AppRegistry,
//   StyleSheet,
//   Text,
//   Image,
//   View
// } from 'react-native';

// var MOCKED_MOVIES_DATA = [
//   {title: '标题', year: '2015', posters: {thumbnail: 'http://i.imgur.com/UePbdph.jpg'}},
// ];

// class RnPractice extends Component {
//   render() {
//     var movie = MOCKED_MOVIES_DATA[0];
//     return (
//       <View style={styles.container}>
//         <Text style={styles.welcome}>
//           Welcome to React Native!
//         </Text>
//         <Text style={styles.instructions}>
//           To get started, edit index.android.js  kai掌2
//         </Text>
//         <Text style={styles.instructions}>
//           Shake or press menu button for dev menu
//         </Text>

//         <Image source={{uri: movie.posters.thumbnail}} 
//           style={styles.thumbnail}/>
//           <View style={styles.rightContainer}>
//           <Text>{movie.title}</Text>
//         <Text>{movie.year}</Text>
//           </View>
//       </View>
//     );
//   }
// }

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     justifyContent: 'center',
//     alignItems: 'center',
//     backgroundColor: '#F5FCFF',
//   },
//   welcome: {
//     fontSize: 20,
//     textAlign: 'center',
//     margin: 10,
//   },
//   instructions: {
//     textAlign: 'center',
//     color: '#333333',
//     marginBottom: 5,
//   },
//   thumbnail:{
//       width: 100,
//       height: 200,
//   },
// });

// AppRegistry.registerComponent('RnPractice', () => RnPractice);
