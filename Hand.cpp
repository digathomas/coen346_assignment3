#include "Hand.h"

Hand::Hand()
{
	cout << "Hand Constructor\n";
	cards = new string[22]; //22 because it's the maximum of cards one can draw before busting (drawing 22 Aces)
	for (int i = 0; i < 22; i++) {
		cards[i] = "";
	}
	size = 0;
	soft = false;
	pair = false;
}

Hand::~Hand()
{
	cout << "Hand Destructor\n";
	delete[] cards;
}

bool Hand::getSoft()
{
	return soft;
}

bool Hand::getPair()
{
	return pair;
}

int Hand::getTotal()
{
	int total = 0;
	for (int i = 0; i < size; i++) {
		string card = cards[i];
		if (card == "A") {
			total += 11;
		}
		else if (card == "2") {
			total += 2;
		}
		else if (card == "3") {
			total += 3;
		}
		else if (card == "4") {
			total += 4;
		}
		else if (card == "5") {
			total += 5;
		}
		else if (card == "6") {
			total += 6;
		}
		else if (card == "7") {
			total += 7;
		}
		else if (card == "8") {
			total += 8;
		}
		else if (card == "9") {
			total += 9;
		}
		else if (card == "10") {
			total += 10;
		}
		else if (card == "J") {
			total += 10;
		}
		else if (card == "Q") {
			total += 10;
		}
		else if (card == "K") {
			total += 10;
		}
	}
	return total;
}

void Hand::printCards()
{
	for (int i = 0; i < size; i++) {
		cout << cards[i] << " ";
	}
}

void Hand::addCard(string card)
{
	cards[size] = card;
	size++;
}
