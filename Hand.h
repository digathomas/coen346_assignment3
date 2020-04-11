#pragma once

#include <iostream>
#include <array>
#include <string>

using namespace std;

#include "Deck.h"

#ifndef HAND_H
#define HAND_H

class Hand
{
private:
	string* cards;
	int size;

	bool soft;
	bool pair;

public:
	Hand();
	~Hand();

	bool getSoft();
	bool getPair();
	int getTotal();

	void printCards();

	void addCard(string card);
};

#endif //HAND_H