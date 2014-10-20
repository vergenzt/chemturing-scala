/** @jsx React.DOM */

var StateView = React.createClass({

  loadState: function() {
    $.ajax({
      url: this.props.url,
      dataType: 'json',
      success: function(state) {
        this.setState({state: state});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },

  getInitialState: function() {
    return {state: {}};
  },

  componentDidMount: function() {
    this.loadState();
    setInterval(this.loadState, this.props.pollInterval);
  },

  render: function() {
    var state = this.state.state
    return (
      <div className="stateView">
        <table>
          <thead>
            <th>Data</th>
            <th>Program Pointer</th>
            <th>Data Pointer</th>
            <th>Mem</th>
          </thead>
          <tr>
            <td>{state.data}</td>
            <td>{state.progPtr}</td>
            <td>{state.dataPtr}</td>
            <td>{state.mem}</td>
          </tr>
        </table>
      </div>
    );
  }
})

React.renderComponent(
  <StateView url='state.json' pollInterval={2000}/>,
  document.getElementById('content')
)

