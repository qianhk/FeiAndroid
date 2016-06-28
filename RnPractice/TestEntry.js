/**
 * Created by ttkai on 16-6-27.
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
    ToastAndroid,
    TouchableHighlight,
} from 'react-native';

const MeiTuan = require('./meituan');
const ZhiHuDaily = require('./zhihu/ZhihuDaily');
import MovieFetcher from './MovieFetcher';
const MoviesApp = require('./movie/MoviesApp');
const TicTacToeApp = require('./TicTacToeApp');
const UIExplorerApp = require('./UIExplorer/UIExplorerApp');
const Game2048 = require('./2048/Game2048');

const TEST_ITEM_LIST = [
    {_id: 100, component: MovieFetcher, name: "MovieFetch"}
    , {_id: 101, component: Game2048, name: "Game 2048"}
    , {_id: 102, component: MeiTuan, name: "MeiTuan"}
    , {_id: 103, component: ZhiHuDaily, name: "ZhiHuDaily"}
    , {_id: 104, component: MoviesApp, name: "MoviesApp"}
    , {_id: 105, component: UIExplorerApp, name: "UIExplorerApp"}
    , {_id: 106, component: TicTacToeApp, name: "TicTacToeApp"}
];

export default class TestEntry extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dataSource: new ListView.DataSource({
                rowHasChanged: (row1, row2) => row1 !== row2,
            })
        };
        this._handleRowPress = this._handleRowPress.bind(this);
        this.renderTestItem = this.renderTestItem.bind(this);
    }

    componentDidMount() {
        this.setState({dataSource: this.state.dataSource.cloneWithRows(TEST_ITEM_LIST)});
    }

    render() {
        return (
            <ListView
                dataSource={this.state.dataSource}
                renderRow={this.renderTestItem}
                renderSeparator={this.renderTestItemSeparator}
                style={styles.listView}
                renderHeader={this.renderHeader}
            />
        );
    }

    renderHeader() {
        return (<View style={{flex:1, paddingVertical: 6
            , justifyContent:'center',alignItems: 'center'}}><Text > kai Test Entry Header</Text></View>);
    }

    renderTestItemSeparator() {
        return (
            <View style={styles.separator}/>
        );
    }

    _handleRowPress(item) {
        // ToastAndroid.show("haha " + item.name, ToastAndroid.SHORT)
        this.props.navigator.push({
            item: item,
            component: item.component
        });
    }

    renderTestItem(item, sectionId, rowId, highlightRow) {
        return (
            <TouchableHighlight
                activeOpacit={0.1}
                underlayColor="#00F"
                style={styles.itemContainer}
                onPress={()=> {
                highlightRow(sectionId, rowId);
                this._handleRowPress(item);
                }}
            >
                <Text style={styles.title}>{item.name}</Text>
            </TouchableHighlight>
        );
    }

}

var styles = StyleSheet.create({

    itemContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 0,
        backgroundColor: '#F0F',
        paddingVertical: 10,
    },

    title: {
        flex: 1,
        fontSize: 20,
        marginBottom: 8,
        textAlign: 'center',
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
