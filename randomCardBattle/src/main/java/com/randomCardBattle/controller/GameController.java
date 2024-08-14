package com.randomCardBattle.controller;

import com.randomCardBattle.model.Game;
import com.randomCardBattle.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @PostMapping
    public Game createGame(@RequestBody Game game) {
        return gameService.saveGame(game);
    }

    @PutMapping("/{id}")
    public Game updateGame(@PathVariable Long id, @RequestBody Game game) {
        Game existingGame = gameService.getGameById(id);
        if (existingGame != null) {
            game.setGameID(id);
            return gameService.saveGame(game);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }
}