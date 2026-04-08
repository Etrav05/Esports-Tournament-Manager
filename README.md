# Esports Tournament Manager

A command-line Java application for managing esports tournaments. Supports Single Elimination, Double Elimination, and Round Robin formats with match result tracking, standings, undo functionality, and file save/load.

---

## Requirements

- **Java JDK 17 or higher**  
  Download: https://www.oracle.com/java/technologies/downloads/

To verify you have Java installed, run:
```
java -version
javac -version
```

---

## How to Compile

Open a terminal and navigate to the folder **containing** the `src/` directory, then run:

```
javac -d out src/main.java src/commands/*.java src/core/*.java src/factories/*.java src/io/*.java src/models/*.java src/models/modelstates/*.java src/states/*.java
```

This compiles all files and places the output into an `out/` folder.

---

## How to Run

After compiling, run:

```
java -cp out src.main
```

---

## Main Menu

```
+----------------------------------+
|      ESPORTS TOURNAMENT MANAGER  |
+----------------------------------+
|  1. Create new tournament        |
|  2. Edit existing tournament     |
|  3. View all tournaments         |
|  4. Load tournament from file    |
|  5. Save tournament to file      |
|  6. Exit                         |
+----------------------------------+
```

---

## Creating a Tournament

1. Select **1** from the main menu
2. Choose a format:
   - **Round Robin** — every team plays every other team
   - **Single Elimination** — one loss and you're out
   - **Double Elimination** — two losses and you're out
3. Enter a tournament name
4. Enter the number of teams (must be a power of 2, e.g. 2, 4, 8, 16, 32)
5. Enter each team name when prompted

The bracket will be generated automatically.

---

## Entering Match Results

1. Select **2** (Edit existing tournament) from the main menu
2. Select your tournament by name
3. Select **2** (Enter match results)
4. You will be shown one match at a time — enter the score for each team
5. Scores cannot be tied
6. Once all matches in a round are complete, the next round is generated automatically

---

## Edit Tournament Menu

```
+----------------------------------+
|          EDIT TOURNAMENT         |
+----------------------------------+
|  1. Rename tournament            |
|  2. Enter match results          |
|  3. View current matches         |
|  4. View results                 |
|  5. View standings               |
|  6. Undo last result             |
|  7. Back                         |
+----------------------------------+
```

- **View current matches** — shows all matches in the current round and their status
- **View results** — shows completed match results across all rounds
- **View standings** — shows win/loss record and points ratio for all teams
- **Undo last result** — reverses the most recently entered match result

---

## Saving a Tournament

1. Select **5** from the main menu
2. Enter the tournament name to save
3. Enter a file path to save to (e.g. `results/mymatch.txt`)

### Saved file format:
```
Tournament Name: March Madness
Format: SINGLE_ELIM
MaxTeams: 4

Teams:
 - Lions
 - Tigers
 - Bears
 - Wolves

Results:
Match 1: Lions def. Tigers (3 - 1)
Match 2: Bears def. Wolves (2 - 0)
```

---

## Loading a Tournament

1. Select **4** from the main menu
2. Enter the path to a valid tournament file

### Load file format (must match exactly):
```
TournamentName=March Madness
Format=SINGLE_ELIM
MaxTeams=4
Teams=
Lions
Tigers
Bears
Wolves
```

Supported format values: `SINGLE_ELIM`, `DOUBLE_ELIM`, `ROUND_ROBIN`

---

## Notes

- Team count must be a power of 2 (2, 4, 8, 16, 32) for all formats
- Tied scores are not allowed
- Undo only goes back one match at a time
- Round Robin generates all matches upfront; standings update after each match