#pragma once

#include <iostream>

using namespace std;

#include "Player.h"

#ifndef DEALER_H
#define DEALER_H

class Dealer : public Player
{
private:
	int rounds;
	int wins;

public:
	Dealer(Deck* deck);
	~Dealer();

	void printHand();

	void newHand();
};

#endif //DEALER_H