#pragma once

#include <iostream>
#include <string>
#include <array>

using namespace std;

#include "Hand.h"

#ifndef PERSON_H
#define PERSON_H

class Person
{
protected:
	Hand* hand;
	Deck* deck;

public:
	Person(Deck* deck);
	~Person();

	Hand* getHand();
	virtual void printHand() = 0;

	virtual void newHand() = 0;
};

#endif //PERSON_H