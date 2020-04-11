#include "Deck.h"


Deck::Deck() {
	cout << "Deck Constructor\n";
}

Deck::~Deck() {
	cout << "Deck Destructor\n";
}

string Deck::drawCard() {
	Sleep(1000);
	srand(time(0));
	int number = (rand() % 13) + 1;
	switch (number) {
	case 1:
		return "A";
	case 2:
		return "2";
	case 3:
		return "3";
	case 4:
		return "4";
	case 5:
		return "5";
	case 6:
		return "6";
	case 7:
		return "7";
	case 8:
		return "8";
	case 9:
		return "9";
	case 10:
		return "10";
	case 11:
		return "J";
	case 12:
		return "Q";
	case 13:
		return "K";
	}
}