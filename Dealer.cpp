#include "Dealer.h"

Dealer::Dealer(Deck* deck) : Player(deck)
{
	cout << "Dealer Constructor\n";
	wins = 0;
}

Dealer::~Dealer()
{
	cout << "Dealer Destructor\n";
}

void Dealer::printHand()
{
	cout << "Dealer's hand: ";
	hand->printCards();
	cout << "\n";
}

void Dealer::newHand()
{
	hand = new Hand();
	hand->addCard(deck->drawCard());
	//hand->addCard(deck->drawCard());
}
