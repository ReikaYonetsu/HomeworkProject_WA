package com.mainor.demoGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class GuessingGame {

    private final Map<String, Integer> gameMap = new HashMap<>();
    private final Map<String, Integer> attemptsMap = new HashMap<>();

    @PostMapping("/game")
    public ResponseEntity<String> startGame() {
        String gameId = generateGameId();
        int secretNumber = generateSecretNumber();
        gameMap.put(gameId, secretNumber);
        attemptsMap.put(gameId, 0); // Initialize attempts count to 0
        return ResponseEntity.ok(gameId);
    }

    @GetMapping("/game/{gameId}/guess/{number}")
    public ResponseEntity<String> makeGuess(@PathVariable String gameId, @PathVariable int number) {
        if (!gameMap.containsKey(gameId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        int secretNumber = gameMap.get(gameId);
        int attempts = attemptsMap.get(gameId) + 1; // Increment attempts count

        if (number < secretNumber) {
            attemptsMap.put(gameId, attempts); // Update attempts count
            return ResponseEntity.ok("Guess higher");
        } else if (number > secretNumber) {
            attemptsMap.put(gameId, attempts); // Update attempts count
            return ResponseEntity.ok("Guess lower");
        } else {
            gameMap.remove(gameId);
            int attemptsTaken = attemptsMap.remove(gameId); // Remove and get attempts count
            return ResponseEntity.ok("Congratulations! You guessed the number in " + attemptsTaken + " attempts.");
        }
    }

    private String generateGameId() {
        return "game-" + new Random().nextInt(1000);
    }

    private int generateSecretNumber() {
        return new Random().nextInt(100) + 1;
    }

    public static void main(String[] args) {
        SpringApplication.run(GuessingGame.class, args);
    }
}
