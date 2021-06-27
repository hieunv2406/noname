import React, { useState } from 'react';
import './App.css';
import List from './components/List';

function App() {

  interface IState {
    people: {
      name: string,
      age: number,
      url: string,
      note?: string
    }[]
  }

  const [people, setPeople] = useState<IState["people"]>([
    {
      name: "Hieu Nguyen",
      age: 25,
      url: "",
      note: "Hieu 123123412341"
    }
  ])


  return (
    <div className="App">
      <h1>People invited to my party</h1>
      <List people={people} />
    </div>
  );
}

export default App;
