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

const TEST_ITEM_LIST = [
    {_id: 100, key: 'movieFetch', name: "MovieFetch"}
    , {
        _id: 101,
        key: 'game2048',
        name: "Game 2048"
    },];

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
            />
        );
    }

    renderTestItemSeparator() {
        return (
            <View style={styles.separator}/>
        );
    }

    _handleRowPress(item) {
        // ToastAndroid.show("haha " + item.name, ToastAndroid.SHORT)
        this.props.navigator.push({
            title: item.name,
            name: item.key,
            item: item,
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
