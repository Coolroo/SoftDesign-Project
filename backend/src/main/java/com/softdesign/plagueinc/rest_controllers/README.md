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

**PlayerID**: UUID

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

**PlayerID**: UUID

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

**PlayerID**: UUID

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

**PlayerID**: UUID

**cardIndex**: Integer

### Responses

**200**: Chosen Country

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

**PlayerID**: UUID

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

**PlayerID**: UUID

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

**PlayerID**: UUID
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

**PlayerID**: UUID

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

**PlayerID**: UUID
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

**PlayerID**: UUID

### Responses

**200**: Nothing

### Body

```
{
    "playerId": String
}
```
