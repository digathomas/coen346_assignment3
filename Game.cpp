#include "Game.h"

Game::Game()
{
	cout << "Game Constructor\n";
	deck = new Deck();
	dealer = new Dealer(deck);
	players = new Player*[8]; //maximum of 8 players on a table
	for (int i = 0; i < 8; i++) {
		players[i] = nullptr;
	}
	numberOfPlayers = 0;
	exit = false;
}

Game::~Game()
{
	cout << "Game Destructor\n";
}

bool Game::getExit()
{
	return exit;
}

void Game::setExit(bool newexit)
{
	exit = newexit;
}

void Game::addPlayer(Player* newplayer)
{
	players[numberOfPlayers] = newplayer;
	numberOfPlayers++;
}

void Game::addPlayer()
{
	if (numberOfPlayers >= 8) {
		cout << "Maximum number of players reached\n";
		return;
	}
	cout << "Enter player details\n";
	cout << "Name: ";
	string name = "";
	cin >> name;
	cout << "Money: ";
	int money = 0;
	cin >> money;

	players[numberOfPlayers] = new Player(deck, name, money);
	numberOfPlayers++;
}

void Game::removePlayer()
{
	cout << "List of all current players: \n";
	for (int i = 0; i < numberOfPlayers; i++) {
		cout << players[i]->getName() << "\n";
	}
	cout << "Player to remove: ";
	string name;
	cin >> name;
	for (int i = 0; i < numberOfPlayers; i++) {
		if (players[i]->getName() == name) {
			cout << players[i]->getName() << " removed.\n";
			delete players[i];
			numberOfPlayers--;
			for (int j = i; j < numberOfPlayers; j++) {
				players[j] = players[j + 1];
			}
		}
	}
}

void Game::setup()
{
	system("CLS");
	cout << "=============================\n";
	cout << "SET UP\n";
	cout << "=============================\n";
	cout << "[1] Add player \n";
	cout << "[2] Remove player \n";
	cout << "[3] Exit \n";
	cout << "[4] Continue \n";
	cout << "Enter Choice: ";
	enum choice { addplayer = 1, removeplayer = 2, exitgame = 3};
	int c = -1;
	cin >> c;
	switch (c) {
	case addplayer:
		this->addPlayer();
		this->setup();
		break;
	case removeplayer:
		this->removePlayer();
		this->setup();
	case exitgame:
		exit = true;
		break;
	}
}

void Game::bets()
{
	system("CLS");
	cout << "=============================\n";
	cout << "BETS OPENED\n";
	cout << "=============================\n";

	int newbet = 0;
	for (int i = 0; i < numberOfPlayers; i++) {
		cout << players[i]->getName() << " has " << players[i]->getMoney() << "$, from his/her initial " << players[i]->getExpense() << "$.\n";
		cout << "New bet: ";
		cin >> newbet;
		players[i]->setBet(newbet);
	}

	cout << "=============================\n";
	cout << "BETS CLOSED\n";
	cout << "=============================\n";


	for (int i = 0; i < numberOfPlayers; i++) {
		players[i]->newHand();
	}
	dealer->newHand();
}

void Game::playersTurn()
{
	for (int i = 0; i < numberOfPlayers; i++) {
		system("CLS");
		cout << "=============================\n";
		cout << players[i]->getName() << "'S TURN\n";
		cout << "=============================\n";

		enum choice { hit = 1, stand = 2, doubledown = 3, split = 4 };
		int c = -1;

		while (c != stand && c != doubledown) {
			dealer->printHand();
			players[i]->printHand();
			cout << "Total of: " << players[i]->getHand()->getTotal() << "\n";
			cout << "[1] HIT \n";
			cout << "[2] STAND \n";
			cout << "[3] DOUBLE DOWN \n";
			cout << "[4] SPLIT \n";
			cout << "Enter Choice: ";
			cin >> c;
			switch (c) {
			case hit:
				cout << "{HIT " << "" << "}\n";
				break;
			case stand:
				cout << "{STAND}\n";
				break;
			case doubledown:
				cout << "{HIT " << "" << "}\n";
				break;
			case split:
				cout << "{SPLIT}\n";
				break;
			default:
				cout << "unknowned command, try again \n";
			}
		}
	}
}

void Game::dealerTurn()
{
	system("CLS");
}

void Game::pay()
{
	system("CLS");
}
