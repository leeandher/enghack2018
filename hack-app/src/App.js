import React, { Component } from 'react';
import './App.processed.css';

function Editor(props) {
  return(
    <div>
      <div className="write-interface">
        <div className="area">
          <div className="subheading">Write your notes:</div>
          <textarea className="write" value={props.input} onChange={props.onChange}></textarea>
        </div>
      </div>

    </div>
  );
}

function Deck(props) {
  <div className="read-interface">
    <div className="area">
    <div className="subheading">{props.status}</div>
    <ul>
      {
        props.deckList.map( (card, i) => {
          <li key={`card_${i}`}>
            <div className="term"></div>
            <div className="definition"></div>
          </li>
        });
      }
    </ul>
  </div>
</div>

}
class Interface extends Component {
  constructor(props) {
    super(props)
    this.state = {
      input: ''
    }
    this.handleInput = this.handleInput.bind(this);
  }

  handleInput(e) {
    const value = e.target.value;
    this.setState({input: value});
  }

  render() {
    return(
      <div>
        <Header />
        <Editor input={this.state.input} onChange={this.handleInput} />
        <Deck />
      </div>
    )
  }
}

function Header(props) {
  return(
    <div className="header-container">
      <h1 className="title"><span className="part1">flash</span>Dash</h1>
      <h3>dynamic flash cards</h3>
    </div>
  )
}

class App extends Component {
  render() {
    return (
      <Interface />
    );
  }
}

export default App;
