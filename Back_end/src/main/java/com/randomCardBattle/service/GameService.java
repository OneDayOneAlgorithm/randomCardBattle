package com.randomCardBattle.service;

import com.randomCardBattle.dto.GameResponseDTO;
import com.randomCardBattle.model.*;
import com.randomCardBattle.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public GameResponseDTO createGame(Long player1ID) {
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
        List<Card> player1Cards = distributeCardsToPlayer(createdGame, player1);
        List<Card> player2Cards = distributeCardsToPlayer(createdGame, player2);

        // 게임과 플레이어의 손 패 정보를 포함한 DTO 반환
        return new GameResponseDTO(createdGame, player1Cards, player2Cards);
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

    private List<Card> distributeCardsToPlayer(Game game, Member player) {
        // 게임 덱에서 카드 5장 가져오기
        List<GameDeck> availableCards = gameDeckRepository.findByGameAndInDeckTrue(game).stream().limit(5).collect(Collectors.toList());
        if (availableCards.size() < 5) {
            throw new IllegalStateException("Not enough cards in the deck.");
        }

        // 플레이어의 GameAction 생성
        GameAction playerAction = new GameAction();
        playerAction.setGame(game);
        playerAction.setMember(player);
        playerAction.setCard1(availableCards.get(0).getGameDeckID());
        playerAction.setCard2(availableCards.get(1).getGameDeckID());
        playerAction.setCard3(availableCards.get(2).getGameDeckID());
        playerAction.setCard4(availableCards.get(3).getGameDeckID());
        playerAction.setCard5(availableCards.get(4).getGameDeckID());
        gameActionRepository.save(playerAction);

        // 게임 덱에서 선택된 카드들의 덱 내부 여부를 false로 업데이트
        for (GameDeck gameDeck : availableCards) {
            gameDeck.setInDeck(false);
            gameDeckRepository.save(gameDeck);
        }

        // 플레이어의 카드 목록을 반환
        return availableCards.stream().map(GameDeck::getCard).collect(Collectors.toList());
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