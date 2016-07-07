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

import CodePush from 'react-native-code-push';

import AppManager from './component/AppManager';

const MeiTuan = require('./meituan');
const ZhiHuDaily = require('./zhihu/ZhihuDaily');
import MovieFetcher from './MovieFetcher';
const MoviesApp = require('./movie/MoviesApp');
const TicTacToeApp = require('./TicTacToeApp');
const UIExplorerApp = require('./UIExplorer/UIExplorerApp');
const Game2048 = require('./2048/Game2048');

const TEST_ITEM_LIST = [
    {key: 100, component: MovieFetcher, name: "MovieFetch"}
    , {key: 101, component: Game2048, name: "Game 2048"}
    , {key: 102, component: MeiTuan, name: "MeiTuan"}
    , {key: 103, component: ZhiHuDaily, name: "ZhiHuDaily"}
    , {key: 104, component: MoviesApp, name: "MoviesApp"}
    , {key: 105, component: UIExplorerApp, name: "UIExplorerApp"}
    , {key: 106, component: TicTacToeApp, name: "TicTacToeApp"}
    , {key: -1, name: "Reload js"}
];

export default class TestEntry extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dataSource: new ListView.DataSource({
                rowHasChanged: (row1, row2) => row1 !== row2,
            }),
            packageInfo: "loading info..."
        };
        this._handleRowPress = this._handleRowPress.bind(this);
        this.renderTestItem = this.renderTestItem.bind(this);
        this.renderHeader = this.renderHeader.bind(this);
        this.renderFooter = this.renderFooter.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
        this.formatDate = this.formatDate.bind(this);
    }

    formatDate(dateMs) {
        let myDate = new Date(dateMs);
        let displayDate = myDate.getFullYear() + '-' + (myDate.getMonth() + 1) + '-' + myDate.getDate() + ' ' + this.formatAMPM(myDate);
        return displayDate;
    }

    formatAMPM(date) { // This is to display 12 hour format like you asked
        var hours = date.getHours();
        var minutes = date.getMinutes();
        // var ampm = hours >= 12 ? 'pm' : 'am';
        // hours = hours % 12;
        // hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0' + minutes : minutes;
        var strTime = hours + ':' + minutes + ':' + date.getSeconds(); // + ' ' + ampm;
        return strTime;
    }

    componentDidMount() {
        this.setState({dataSource: this.state.dataSource.cloneWithRows(TEST_ITEM_LIST)});

        if (CodePush) {
            console.info('will get package info');
            CodePush.getCurrentPackage().then((info) => {
                // console.info('package info', info);
                if (!info || info == '') {
                    console.info('package is empty');
                    this.setState({packageInfo: 'package is empty'});
                } else {
                    let infoTxt = 'label:' + info.label + ' packageSize:' + info.packageSize
                        + " time:" + this.formatDate(parseInt(info.binaryModifiedTime)) + " description:" + info.description
                        + " deploymentKey:" + info.deploymentKey;
                    this.setState({packageInfo: infoTxt});
                }
            }).catch((x) => {
                console.info('done get package info', x);
                this.setState({packageInfo: 'catch no info'});
            }).done();
        } else {
            console.info('no package info, not use code-push');
            this.setState({packageInfo: 'not use code-push'});
        }
    }

    render() {
        return (
            <ListView
                dataSource={this.state.dataSource}
                renderRow={this.renderTestItem}
                style={styles.listView}
                renderHeader={this.renderHeader}
                renderSeparator={this.renderTestItemSeparator}
                renderFooter={this.renderFooter}
            />
        );
    }

    renderHeader() {
        // let {key_from} = this.props;
        return (
            <View style={{flex:1, paddingVertical: 6, justifyContent:'center',alignItems: 'center'}}>
                <Text >Test Entry Header v07-07 16:39</Text>
                <Text >native version: {this.props.outprops.native_version}</Text>
            </View>
        );
    }

    renderFooter() {
        return (
            <View style={{flex:1, paddingVertical: 6, justifyContent:'center',alignItems: 'center'}}>
                <Text >js version: {this.state.packageInfo} </Text>
            </View>
        );
    }

    renderTestItemSeparator(sectionId, rowId, highlightRow) {
        let keyId = 'sep_' + sectionId + '_' + rowId;
        return (
            <View key={keyId} style={styles.separator}/>
        );
    }

    _handleRowPress(item) {
        // ToastAndroid.show("haha " + item.name, ToastAndroid.SHORT)
        if (item.key == -1) {
            AppManager.restartPage();
        } else {
            this.props.navigator.push({
                item: item,
                component: item.component
            });
        }
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

/*
 'package info', { install: [Function: install],
 isPending: false,
 failedInstall: false,
 label: 'v8',
 bundlePath: '/bundles/index.android.bundle',
 deploymentKey: 'hv4DIHXtcGtdChiPqxgwXDFL9kSKVyyaBGmLZ',
 packageSize: 474925,
 packageHash: '6a619a581de1c36140a9164ca88c3324cdf5ed763d43ae3e42ffb7498cb181d6',
 binaryModifiedTime: '1467720624000',
 downloadUrl: 'https://codepush.blob.core.windows.net/storagev2/YAPeqOHNKfbAt2q-n6yv27bU6N6lVyyaBGmLZ',
 description: 'test listview header and footer',
 isMandatory: false,
 appVersion: '1.0.1',
 isFirstRun: false }
 */