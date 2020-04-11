#pragma once

#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>
#include <conio.h>
#include <Windows.h>

using namespace std;

#ifndef DECK_H
#define DECK_H

class Deck
{
private:
	int numberOfCards;

public:
	Deck();
	~Deck();

	string drawCard();
};

#endif //DECK_H

