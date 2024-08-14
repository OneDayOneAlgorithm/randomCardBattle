package com.randomCardBattle.service;

import com.randomCardBattle.model.*;
import com.randomCardBattle.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameActionRepository gameActionRepository;
    private final GameDeckRepository gameDeckRepository;
    private final MemberRepository memberRepository;

    private final CardRepository cardRepository;
    @Autowired
    public GameService(GameRepository gameRepository, GameActionRepository gameActionRepository,
                       GameDeckRepository gameDeckRepository, MemberRepository memberRepository, CardRepository cardRepository) {
        this.gameRepository = gameRepository;
        this.gameActionRepository = gameActionRepository;
        this.gameDeckRepository = gameDeckRepository;
        this.memberRepository = memberRepository;
        this.cardRepository = cardRepository;
    }

    public Game createGame(Long player1ID) {
        Member player1 = memberRepository.findById(player1ID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player1 ID"));

        Member player2 = memberRepository.findById(7L) // 임의의 상대 플레이어를 선택하는 메소드
                .orElseThrow(() -> new IllegalArgumentException("Invalid player2 ID"));

        // 기본적으로 설정할 값들
        Game game = new Game();
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setStartTime(LocalDateTime.now().toString());
        game.setEndTime(LocalDateTime.now().plusMinutes(30).toString()); // 기본 30분 게임 시간 설정
        game.setPlayer1Health(200L);
        game.setPlayer2Health(200L);
        game.setPlayer1CurseCardId(0L);
        game.setPlayer2CurseCardId(0L);
        game.setTotalDamage(0L);
        game.setCurrentDamage(0L);
        game.setCurrentTurnPlayerID(player1ID);
        game.setWinnerID(null);

        // 게임 생성
        Game createdGame = gameRepository.save(game);

        // 게임 덱 초기화
        initializeGameDeck(createdGame);

        // 카드 분배 로직 추가
        distributeCardsToPlayers(createdGame);

        return createdGame;
    }

    private void initializeGameDeck(Game game) {
        // 모든 카드를 가져옴
        List<Card> allCards = cardRepository.findAll();

        // 각 카드에 대해 GameDeck 엔트리를 생성하고 덱 내부에 있는지 여부를 true로 설정
        for (Card card : allCards) {
            GameDeck gameDeck = new GameDeck();
            gameDeck.setGame(game);
            gameDeck.setCard(card);
            gameDeck.setInDeck(true);
            gameDeckRepository.save(gameDeck);
        }
    }

    private void distributeCardsToPlayers(Game game) {
        // 게임 덱에서 카드 10장 가져오기
        List<GameDeck> availableCards = gameDeckRepository.findByInDeckTrue();
        if (availableCards.size() < 10) {
            throw new IllegalStateException("Not enough cards in the deck.");
        }

        // 카드 섞기 (랜덤으로 선택하기 위해)
        Collections.shuffle(availableCards);

        List<GameDeck> selectedCards = availableCards.subList(0, 10);

        // 각 플레이어에게 5장씩 배분
        List<GameDeck> player1Cards = selectedCards.subList(0, 5);
        List<GameDeck> player2Cards = selectedCards.subList(5, 10);

        // 플레이어1의 GameAction 생성
        GameAction player1Action = new GameAction();
        player1Action.setGame(game);
        player1Action.setMember(game.getPlayer1());
        player1Action.setCard1(player1Cards.get(0).getGameDeckID());
        player1Action.setCard2(player1Cards.get(1).getGameDeckID());
        player1Action.setCard3(player1Cards.get(2).getGameDeckID());
        player1Action.setCard4(player1Cards.get(3).getGameDeckID());
        player1Action.setCard5(player1Cards.get(4).getGameDeckID());
        gameActionRepository.save(player1Action);

        // 플레이어2의 GameAction 생성
        GameAction player2Action = new GameAction();
        player2Action.setGame(game);
        player2Action.setMember(game.getPlayer2());
        player2Action.setCard1(player2Cards.get(0).getGameDeckID());
        player2Action.setCard2(player2Cards.get(1).getGameDeckID());
        player2Action.setCard3(player2Cards.get(2).getGameDeckID());
        player2Action.setCard4(player2Cards.get(3).getGameDeckID());
        player2Action.setCard5(player2Cards.get(4).getGameDeckID());
        gameActionRepository.save(player2Action);

        // 게임 덱에서 선택된 카드들의 덱 내부 여부를 false로 업데이트
        for (GameDeck card : selectedCards) {
            card.setInDeck(false);
            gameDeckRepository.save(card);
        }
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    // 모든 게임을 가져오는 메서드 추가
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // 특정 게임 ID로 게임을 가져오는 메서드 추가
    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));
    }
}