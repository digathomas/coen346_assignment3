#pragma once

#include <iostream>
#include <array>
#include <string>

using namespace std;

#include "Deck.h"
#include "Hand.h"
#include "Person.h"

#ifndef PLAYER_H
#define PLAYER_H

class Player : public Person
{
private:
	string name;
	int money;
	int expense; 
	int bet;

public:
	Player(Deck* newdeck);
	Player(Deck* newdeck, string newname, int newmoney);
	~Player();

	void setBet(int newbet);
	string getName();
	int getMoney();
	int getExpense();
	void printHand();

	void newHand();

	void stand();
	void hit(string card);
	void split();
	void doubledown();
};

#endif //PLAYER_H