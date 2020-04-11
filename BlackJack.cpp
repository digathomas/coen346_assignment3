#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>

using namespace std;

#include "Game.h"
#include "Person.h"
#include "Dealer.h"
#include "Player.h"
#include "Hand.h"
#include "Deck.h"

int main()
{
	Game* game = new Game();
	Deck* deck = new Deck();

	Player* whikweon = new Player(deck, "Whi Kweon Kang", 500);
	game->addPlayer(whikweon);

	while (game->getExit() == false) {
		game->setup();
		if (game->getExit() == false) {
			game->bets();

			game->playersTurn();
			game->dealerTurn();

			game->pay();
		}
	}

	delete game;

	system("pause");
	return 0;
}