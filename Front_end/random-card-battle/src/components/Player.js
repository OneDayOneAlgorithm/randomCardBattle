import React from 'react';

function Player({ id, hand, curse, health, onPlayCard }) {
  return (
    <div className="player">
      <h2>{id}</h2>
      <div>
        <h3>Hand:</h3>
        <div>
          {hand.map(card => (
            <button key={card.id} onClick={() => onPlayCard(card)}>
              <img src={card.cardImageURL} alt={card.name} style={{ width: '100px', height: '150px' }} />
              <br />
              {card.type === 'attack' ? `Attack: ${card.value}` : card.type}
            </button>
          ))}
        </div>
      </div>
      <div>
        <h3>저주:</h3>
        <p>{curse ? curse : '없음'}</p>
      </div>
      <div>
        <h3>남은 체력 : {health}</h3>
      </div>
      <br />
      <br />
    </div>
  );
}

export default Player;
