#include "Person.h"

Person::Person(Deck* newdeck)
{
	cout << "Person Constructor\n";
	hand = nullptr;
	deck = newdeck;
}

Person::~Person()
{
	cout << "Person Destructor\n";
}

Hand * Person::getHand()
{
	return hand;
}
