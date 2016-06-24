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
    ListView,
    TouchableHighlight,
} from 'react-native';


var PAGE_SIZE = 25;
var API_KEY = '7waqfqbprs7pajbz28mqf6vz';
var API_URL = 'http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json';
var PARAMS = '?apikey=' + API_KEY + '&page_limit=' + PAGE_SIZE;
var REQUEST_URL = API_URL + PARAMS;

export default class MovieFetcher extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dataSource: new ListView.DataSource({
                rowHasChanged: (row1, row2) => row1 !== row2,
            })
            , loadState: 0
        };
        this.fetchData = this.fetchData.bind(this);
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
                    loadState: 1,
                });
            })
            .catch((error) => {
                this.setState({
                    loadState: -1
                });
            })
            .done();
    }

    render() {
        if (this.state.loadState === 0) {
            return this.renderLoadingView();
        } else if (this.state.loadState < 0) {
            return this.renderLoadFailedView();
        }

        return (
            <ListView
                dataSource={this.state.dataSource}
                renderRow={this.renderMovie}
                renderSeparator={this.renderMovieSeparator}
                style={styles.listView}
            />
        );
    }

    renderLoadingView() {
        return (
            <View style={styles.loadingContainer}>
                <Text style={{color:'#FFF'}}>
                    正在加载电影数据……
                </Text>
            </View>
        );
    }

    renderLoadFailedView() {
        return (
            <View style={styles.loadingContainer}>
                <Text style={{color:'#F00'}}>
                    加载失败
                </Text>
            </View>
        );
    }

    renderMovieSeparator() {
        return (
            <View style={styles.separator}/>
        );
    }

    renderMovie(movie, sectionId, rowId, highlightRow) {
        return (
            <TouchableHighlight
                activeOpacit={0.1}
                underlayColor="#00F"
                onPress={()=> {highlightRow(sectionId, rowId)}}
            >
                <View style={[styles.itemContainer, rowId % 2 == 0 && {backgroundColor: '#0FF'}]}>
                    <Image
                        source={{uri: movie.posters.thumbnail}}
                        style={styles.thumbnail}
                    />
                    <View style={styles.rightContainer}>
                        <Text style={styles.title}>{movie.title}</Text>
                        <Text style={styles.year}>{movie.year}</Text>
                    </View>
                </View>
            </TouchableHighlight>
        );
    }
}

var styles = StyleSheet.create({
    loadingContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 0,
        backgroundColor: '#2862C2',
    },
    itemContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 0,
        backgroundColor: '#F0F',
        paddingVertical: 10,
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
        width: 60,
        height: 80,
    },
    separator: {
        backgroundColor: 'rgba(0, 0, 0, 0.1)',
        height: 1,
        marginVertical: 0,
    },
    listView: {
        paddingTop: 20,
        backgroundColor: '#58E2C2',
    },
});
