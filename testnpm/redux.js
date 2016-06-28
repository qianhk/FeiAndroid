/**
 * Created by ttkai on 16-6-23.
 */
import React, {Component, PropTypes} from 'react'
import ReactDOM from 'react-dom'
import {createStore} from 'redux'
import {Provider, connect} from 'react-redux'

// React component
class Counter extends Component {
    render() {
        const {value, onDecreaseClick, dispatch} = this.props
        return (
            <div>
                <span>{value}</span>
                <button onClick={()=>{dispatch(increaseAction)}}>Increase</button>
                <button onClick={()=>{dispatch(decreaseAction)}}>Decrease</button>
            </div>
        )
    }
}

Counter.propTypes = {
    value: PropTypes.number.isRequired,
    // onIncreaseClick: PropTypes.func.isRequired,
    // onDecreaseClick: PropTypes.func.isRequired,
}

// Action
const increaseAction = {type: 'increase'}
const decreaseAction = {type: 'decrease'}

// Reducer
function counter2(state = {count: 82}, action) {
    const count = state.count
    switch (action.type) {
        case 'increase':
            return {count: count + 2}
        case 'decrease':
            return {count: count - 2}
        default:
            return state
    }
}

// Store
const store = createStore(counter2)

// Map Redux state to component props
function mapStateToProps(state) {
    return {
        value: state.count
    }
}

// Map Redux actions to component props
// function mapDispatchToProps(dispatch) {
//     return {
//         onIncreaseClick: () => dispatch(increaseAction),
//         onDecreaseClick: () => dispatch(decreaseAction),
//     }
// }

// Connected Component
const App = connect(
    mapStateToProps
    // , mapDispatchToProps
)(Counter)

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('root')
)
