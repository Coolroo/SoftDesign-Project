# REST Endpoints

# GameStateEndpoints.java

## POST /joinGame

called when a player attempts to join the game

### Expects

Nothing

### Responses

**200**: Player ID

### Body

```
{}
```

## POST /voteToStart

Called when a player votes to start the game

### Expects

**PlayerID**: String

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String
}
```

### POST /proceedState

Called when a player is ready to proceed to the next turn phase

### Expects

**PlayerID**: String

### Returns

**200**: Drawn Country

### Body

```
{
    "playerId": String
}
```

### POST /drawCountry

Called during the Country phase, when a player chooses to take a card from the deck

### Expects

**PlayerID**: String

### Responses

**200**: Drawn Country

### Body

```
{
    "playerId": String
}
```

### POST /takeCountry

Called during the Country phase, when a plyer chooses to take one of the revealed cards

### Expects

**PlayerID**: String
**cardIndex**: Integer

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String,
    "cardIndex": Integer
}
```

### POST /playCountry

Called during the Country phase, when a player decides to play the revealed country

### Expects

**PlayerID**: String

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String
}
```

### POST /discardCountry

Called during the Country phase, when a player decides to discard the revealed country

### Expects

**PlayerID**: String

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String
}
```

### POST /evolve

Called during the Evolution phase, when a player decides to evolve a trait in their hand

### Expects

**PlayerID**: String
**traitIndex**: Integer
**traitSlot**: Integer

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String,
    "traitIndex": Integer,
    "traitSlot": Integer
}
```

### POST /skipEvolution

Called during the evolution phase, when a player decides not to evolve any traits

### Expects

**PlayerID**: String

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String
}
```

### POST /infect

Called during the infection phase, when a player tries to infect a country on the board

### Expects

**PlayerID**: String
**CountryName**: String

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String,
    "countryName": String
}
```

### POST /rollDeathDice

Called during the DEATH phase when a player attempts to kill a country

### Expects

**PlayerID**: String

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String
}
```