import React, { useState, useEffect } from 'react';
import Player from './Player';
import axios from 'axios';

function Game() {
  const [deck, setDeck] = useState([]); // 초기값을 빈 배열로 설정
  const [players, setPlayers] = useState([
    { id: 1, health: 100, hand: [], curse: "지옥의 저주" },
    { id: 2, health: 100, hand: [], curse: null }
  ]);
  const [currentTurn, setCurrentTurn] = useState(0);
  const [currentAttack, setCurrentAttack] = useState(0);
  const [totalDamage, setTotalDamage] = useState(0);

  useEffect(() => {
    // API에서 데이터를 가져와 덱을 초기화하고 핸드를 생성
    const fetchInitialData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/cards');
        const data = response.data;
        setDeck(data); // 가져온 데이터를 deck 상태로 설정
        console.log("Fetched data:", data);
        drawInitialHands(data); // 핸드를 생성할 때 가져온 데이터를 사용
      } catch (error) {
        console.error('Error fetching initial game data:', error);
      }
    };

    fetchInitialData();
  }, []); // 빈 배열을 의존성 배열로 전달하여 컴포넌트가 처음 렌더링될 때만 실행되도록 함

  const drawInitialHands = (deckData) => {
    const updatedPlayers = players.map(player => {
      return { ...player, hand: deckData.slice(0, 5) };
    });
    setDeck(deckData.slice(10)); // 총 10장의 카드를 뽑아 5장씩 나눔
    setPlayers(updatedPlayers);
  };

  const drawNewCard = (playerId) => {
    const newCard = deck[0];
    setDeck(deck.slice(1));
    setPlayers(players.map(player => {
      if (player.id === playerId) {
        return { ...player, hand: [...player.hand, newCard] };
      }
      return player;
    }));
  };

  const handlePlayCard = (playerId, card) => {
    const updatedPlayers = players.map(player => {
      if (player.id === playerId) {
        // 공격 카드 처리
        if (card.type === 'attack') {
          if (card.value > currentAttack) {
            setCurrentAttack(card.value);
            setTotalDamage(totalDamage + card.value);
          } else {
            setPlayers(players.map(p => {
              if (p.id !== playerId) {
                return { ...p, health: p.health - totalDamage };
              }
              return p;
            }));
            setTotalDamage(card.value);
            setCurrentAttack(card.value);
          }
        }
        // 저주 카드 처리
        if (card.type === 'curse') {
          const targetPlayerId = playerId === 1 ? 2 : 1;
          setPlayers(players.map(p => {
            if (p.id === targetPlayerId) {
              return { ...p, curse: card };
            }
            return p;
          }));
        }
        // 카드 사용 후 핸드에서 제거
        return { ...player, hand: player.hand.filter(c => c.id !== card.id) };
      }
      return player;
    });
    setPlayers(updatedPlayers);
    drawNewCard(playerId);
  };

  const handleEndTurn = () => {
    setCurrentTurn(currentTurn === 0 ? 1 : 0);
  };

  return (
    <div className="game">
      <Player
        id={"상대방"}
        hand={players[0].hand}
        curse={players[0].curse}
        health={players[0].health}
        currentAttack={currentAttack}
        totalDamage={totalDamage}
        onPlayCard={(card) => handlePlayCard(players[0].id, card)}
      />
      <Player
        id={"나"}
        hand={players[1].hand}
        curse={players[1].curse}
        health={players[1].health}
        currentAttack={currentAttack}
        totalDamage={totalDamage}
        onPlayCard={(card) => handlePlayCard(players[1].id, card)}
      />
    </div>
  );
}

export default Game;
