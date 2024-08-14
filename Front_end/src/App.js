import React, { useState } from 'react';
import Game from './components/Game';
import Login from './components/Login';
import SignUp from './components/SignUp';
import axios from 'axios';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [memberID, setMemberID] = useState(null);
  const [isGameStarted, setIsGameStarted] = useState(false);
  const [gameID, setGameID] = useState(null);

  const handleLogin = (id) => {
    setIsAuthenticated(true);
    setMemberID(id);
  };

  const startGame = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/games', memberID, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
  
      setGameID(response.data.gameID);
      setIsGameStarted(true);
    } catch (error) {
      console.error('Error starting game:', error);
    }
  };

  return (
    <div className="App">
      <h1>Random Card Battle</h1>
      {!isAuthenticated ? (
        <>
          <Login onLogin={handleLogin} />
          <SignUp />
        </>
      ) : (
        <>
          {!isGameStarted ? (
            <button onClick={startGame}>Start Game</button>
          ) : (
            <Game memberID={memberID} gameID={gameID} />
          )}
        </>
      )}
    </div>
  );
}

export default App;
