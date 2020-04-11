#include "Player.h"

Player::Player(Deck * newdeck) : Person(newdeck)
{
	cout << "Player Constructor\n";
	name = "No Name";
	money = 0;
	expense = money;
}

Player::Player(Deck* newdeck, string newname, int newmoney) : Person(newdeck)
{
	cout << "Player constructor\n";
	name = newname;
	money = newmoney;
	expense = money;
}

Player::~Player()
{
	cout << "Player Destructor\n";
}

void Player::setBet(int newbet)
{
	bet = newbet;
}

string Player::getName()
{
	return name;
}

int Player::getMoney()
{
	return money;
}

int Player::getExpense()
{
	return expense;
}

void Player::printHand()
{
	cout << name << "'s hand: ";
	hand->printCards();
	cout << "\n";
}

void Player::newHand()
{
	hand = new Hand();
	hand->addCard(deck->drawCard());
	hand->addCard(deck->drawCard());
}

void Player::stand()
{
}

void Player::hit(string card)
{
}

void Player::split()
{
}

void Player::doubledown()
{
}
