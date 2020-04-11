#pragma once

#include <iostream>
#include <array>
#include <string>

using namespace std;

#include "Deck.h"
#include "Hand.h"
#include "Person.h"
#include "Player.h"
#include "Dealer.h"

#ifndef GAME_H
#define GAME_H

class Game
{
private:
	Dealer* dealer;
	Player** players;
	int numberOfPlayers;

	Deck* deck;

	bool exit;

public:
	Game();
	~Game();

	bool getExit();
	void setExit(bool newexit);

	void addPlayer(Player* newplayer);
	void addPlayer();
	void removePlayer();

	void setup();
	void bets();
	void playersTurn();
	void dealerTurn();
	void pay();
};

#endif GAME_H