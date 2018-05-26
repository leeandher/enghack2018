import React, { Component } from 'react';
import './App.processed.css';
//import './marked.min.js'

function Header(props) {
  return(
    <div className="header-container">
      <h1 className="title">flashDash</h1>
      <h3 className="subtitle">dynamic flash cards</h3>
    </div>
  )
}

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
  return(
    <div className="read-interface">
      <div className="area">
      <div className="subheading">{props.status}</div>
        <ul className="deck-stack">
          {
            props.deckList.map(
              (card, i) => <li className="card" key={`card_${i}`}>
                <div className="term">{card.term}</div>
                <div className="definition">{card.definition}</div>
              </li>
            )
          }
        </ul>
      </div>
    </div>
  );

}

class StickyNav extends Component {
  constructor(props) {
    super(props)
    this.handleDownload = this.handleDownload.bind(this);
  }

  handleDownload() {
    var textToWrite = this.props.text//Your text input;
    var textFileAsBlob = new Blob([textToWrite], {type:'text/plain'});
    var fileNameToSaveAs = 'flashDash.txt'//Your filename;

    var downloadLink = document.createElement("a");
    downloadLink.download = fileNameToSaveAs;
    downloadLink.innerHTML = "Download File";
    if (window.webkitURL != null)
    {
        // Chrome allows the link to be clicked
        // without actually adding it to the DOM.
        downloadLink.href = window.webkitURL.createObjectURL(textFileAsBlob);
    }
    // else
    // {
    //     // Firefox requires the link to be added to the DOM
    //     // before it can be clicked.
    //     downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
    //     downloadLink.onclick = destroyClickedElement;
    //     downloadLink.style.display = "none";
    //     document.body.appendChild(downloadLink);
    // }

    if (textToWrite) downloadLink.click();
  }

  render() {
    return(
      <div className="function-container">
        <button id="download" onClick={this.handleDownload}>Download Note</button>
        <button id="review" disabled>Mark for Review</button>
        <button id="skip" disabled>Skip</button>
        <button id="reveal" disabled>Reveal</button>
        <button id="review">Quiz Me!</button>
      </div>
    );
  }
}


class Interface extends Component {
  constructor(props) {
    super(props)
    this.state = {
      input: '',
      deck: [],
    }
    this.handleInput = this.handleInput.bind(this);
  }

  handleInput(e) {
    const value = e.target.value;
    this.setState({input: value});
    let terms = this.state.input.match(/.+:/g);
    let definitions = this.state.input.match(/:.+\s/g);
    let newState = [];
    if (terms && definitions && terms.length === definitions.length) {
      terms.forEach((term, i) => {
        let temp = {}
        temp['term'] = term.substr(0,term.length-1);
        temp['definition'] = definitions[i].substr(2, definitions[i].length-3);
        newState.push(temp)
      });
    }
    console.log(newState);
    this.setState({'deck':newState});
  }

  render() {
    return(
      <div>
        <Header />
        <Editor input={this.state.input} onChange={this.handleInput} />
        <Deck status={Object.values(this.state.deck) ? "Your deck:" :"← Start a deck!"} deckList={this.state.deck || []}/>
        <StickyNav text={this.state.input}/>
      </div>
    )
  }
}


class App extends Component {
  render() {
    return (
      <Interface />
    );
  }
}

export default App;
